/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.oauth2.provider.internal.upgrade.v2_0_0;

import com.liferay.oauth2.provider.internal.upgrade.v2_0_0.util.OAuth2ApplicationScopeAliasesTable;
import com.liferay.oauth2.provider.scope.liferay.LiferayOAuth2Scope;
import com.liferay.oauth2.provider.scope.liferay.ScopeLocator;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LoggingTimer;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Stian Sigvartsen
 */
public class UpgradeOAuth2ApplicationScopeAliases extends UpgradeProcess {

	public UpgradeOAuth2ApplicationScopeAliases(
		CompanyLocalService companyLocalService, ScopeLocator scopeLocator) {

		_companyLocalService = companyLocalService;
		_scopeLocator = scopeLocator;
	}

	@Override
	protected void doUpgrade() throws Exception {
		for (Company company : _companyLocalService.getCompanies()) {
			upgradeCompany(company.getCompanyId());
		}

		alter(
			OAuth2ApplicationScopeAliasesTable.class,
			new AlterTableDropColumn("scopeAliases"));
	}

	protected void upgradeCompany(long companyId) throws Exception {
		Map<LiferayOAuth2Scope, Set<String>> liferayOAuth2ScopesScopeAliases =
			new HashMap<>();

		List<String> scopeAliasesList = new ArrayList<>(
			_scopeLocator.getScopeAliases(companyId));

		for (String scopeAlias : scopeAliasesList) {
			for (LiferayOAuth2Scope liferayOAuth2Scope :
					_scopeLocator.getLiferayOAuth2Scopes(
						companyId, scopeAlias)) {

				Set<String> scopeAliases =
					liferayOAuth2ScopesScopeAliases.computeIfAbsent(
						liferayOAuth2Scope, x -> new HashSet<>());

				scopeAliases.add(scopeAlias);
			}
		}

		try (LoggingTimer loggingTimer = new LoggingTimer();
			ResultSet applicationScopeAliasesResultSet =
				_getApplicationScopeAliases(companyId)) {

			while (applicationScopeAliasesResultSet.next()) {
				String scopeAliases =
					applicationScopeAliasesResultSet.getString("scopeAliases");

				if (Validator.isNull(scopeAliases)) {
					continue;
				}

				long oAuth2ApplicationScopeAliasesId =
					applicationScopeAliasesResultSet.getLong(
						"oA2AScopeAliasesId");

				Set<String> assignedScopeAliases = SetUtil.fromArray(
					StringUtil.split(scopeAliases, StringPool.SPACE));

				_upgradeOAuth2ScopeGrants(
					oAuth2ApplicationScopeAliasesId, companyId,
					liferayOAuth2ScopesScopeAliases, assignedScopeAliases);
			}
		}
	}

	private ResultSet _getApplicationScopeAliases(long companyId)
		throws SQLException {

		String sql = StringBundler.concat(
			"select oa2asa.oA2AScopeAliasesId, oa2asa.scopeAliases from ",
			"OAuth2Application oa2a inner join OAuth2ApplicationScopeAliases ",
			"oa2asa on oa2a.oA2AScopeAliasesId = oa2asa.oA2AScopeAliasesId ",
			"where oa2a.companyId = ?");

		PreparedStatement preparedStatement = connection.prepareStatement(sql);

		preparedStatement.setLong(1, companyId);

		return preparedStatement.executeQuery();
	}

	private ResultSet _getOAuth2ScopeGrants(
			long oAuth2ApplicationScopeAliasesId)
		throws SQLException {

		String sql =
			"select * from OAuth2ScopeGrant where oA2AScopeAliasesId = ?";

		PreparedStatement preparedStatement = connection.prepareStatement(sql);

		preparedStatement.setLong(1, oAuth2ApplicationScopeAliasesId);

		return preparedStatement.executeQuery();
	}

	private void _updateOAuth2ScopeGrant(
			long oAuth2ScopeGrantId, Set<String> scopeAliases)
		throws SQLException {

		String sql =
			"update OAuth2ScopeGrant set scopeAliases = ? where " +
				"oAuth2ScopeGrantId = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(
				sql)) {

			preparedStatement.setString(
				1,
				StringUtil.merge(
					ListUtil.sort(new ArrayList<>(scopeAliases)),
					StringPool.SPACE));

			preparedStatement.setLong(2, oAuth2ScopeGrantId);
			preparedStatement.execute();
		}
	}

	private void _upgradeOAuth2ScopeGrants(
			long oAuth2ApplicationScopeAliasesId, long companyId,
			Map<LiferayOAuth2Scope, Set<String>>
				liferayOAuth2ScopesScopeAliases,
			Set<String> assignedScopeAliases)
		throws SQLException {

		try (ResultSet oAuth2ScopeGrantResultSet = _getOAuth2ScopeGrants(
				oAuth2ApplicationScopeAliasesId)) {

			while (oAuth2ScopeGrantResultSet.next()) {
				long oAuth2ScopeGrantId = oAuth2ScopeGrantResultSet.getLong(
					"oAuth2ScopeGrantId");

				String applicationName = oAuth2ScopeGrantResultSet.getString(
					"applicationName");
				String scope = oAuth2ScopeGrantResultSet.getString("scope");

				LiferayOAuth2Scope liferayOAuth2Scope =
					_scopeLocator.getLiferayOAuth2Scope(
						companyId, applicationName, scope);

				if (liferayOAuth2Scope == null) {
					_updateOAuth2ScopeGrant(
						oAuth2ScopeGrantId, Collections.singleton("legacy"));

					continue;
				}

				Set<String> availableScopeAliases =
					liferayOAuth2ScopesScopeAliases.get(liferayOAuth2Scope);

				Set<String> assignedAvailableScopeAliases = new HashSet<>(
					assignedScopeAliases);

				assignedAvailableScopeAliases.retainAll(availableScopeAliases);

				if (assignedAvailableScopeAliases.isEmpty()) {
					_updateOAuth2ScopeGrant(
						oAuth2ScopeGrantId, Collections.singleton("legacy"));
				}
				else {
					_updateOAuth2ScopeGrant(
						oAuth2ScopeGrantId, assignedAvailableScopeAliases);
				}
			}
		}
	}

	private final CompanyLocalService _companyLocalService;
	private final ScopeLocator _scopeLocator;

}