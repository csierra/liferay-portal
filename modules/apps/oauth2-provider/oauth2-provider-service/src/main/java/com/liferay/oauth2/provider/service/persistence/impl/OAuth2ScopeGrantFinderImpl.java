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

package com.liferay.oauth2.provider.service.persistence.impl;

import com.liferay.oauth2.provider.model.OAuth2ScopeGrant;
import com.liferay.oauth2.provider.model.impl.OAuth2ScopeGrantImpl;
import com.liferay.oauth2.provider.service.persistence.OAuth2ScopeGrantFinder;
import com.liferay.portal.dao.orm.custom.sql.CustomSQL;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.Type;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.spring.extender.service.ServiceReference;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Carlos Sierra Andrés
 */
public class OAuth2ScopeGrantFinderImpl
	extends OAuth2ScopeGrantFinderBaseImpl implements OAuth2ScopeGrantFinder {

	public static final String FIND_BY_C_A_B_A =
		OAuth2ScopeGrantFinder.class.getName() + ".findByC_A_B_A";

	public void afterPropertiesSet() {
		try {
			try (Connection con = DataAccess.getConnection()) {
				DatabaseMetaData metaData = con.getMetaData();

				String dbName = metaData.getDatabaseProductName();

				if (dbName.startsWith("MySQL")) {
					_supportsCLOB = false;
				}
				else if (dbName.startsWith("PostgreSQL")) {
					_supportsCLOB = false;
				}
				else if (dbName.startsWith("Sybase") || dbName.equals("ASE")) {
					_supportsCLOB = false;
				}
			}
		}
		catch (SQLException sqle) {
			_log.error("Unable to get SQL connection metadata", sqle);
		}
	}

	@Override
	public Collection<OAuth2ScopeGrant> findByC_A_B_A(
		long companyId, String applicationName, String bundleSymbolicName,
		String accessTokenContent) {

		Session session = null;

		try {
			session = openSession();

			String sql = _customSQL.get(getClass(), FIND_BY_C_A_B_A);

			SQLQuery q = session.createSynchronizedSQLQuery(sql);

			QueryPos qPos = QueryPos.getInstance(q);

			q.addEntity("OAuth2ScopeGrant", OAuth2ScopeGrantImpl.class);

			if (_supportsCLOB) {
				q.addScalar("accessTokenContent", Type.MATERIALIZED_CLOB);
			}
			else {
				q.addScalar("accessTokenContent", Type.TEXT);
			}

			qPos.add(companyId);
			qPos.add(applicationName);
			qPos.add(bundleSymbolicName);
			qPos.add(accessTokenContent.hashCode());

			List<Object[]> rows = (List<Object[]>)QueryUtil.list(
				q, getDialect(), QueryUtil.ALL_POS, QueryUtil.ALL_POS);

			ArrayList<OAuth2ScopeGrant> oAuth2ScopeGrants = new ArrayList<>();

			for (Object[] row : rows) {
				String string = (String)row[1];

				if (accessTokenContent.equals(string)) {
					oAuth2ScopeGrants.add((OAuth2ScopeGrant)row[0]);
				}
			}

			return oAuth2ScopeGrants;
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		OAuth2ScopeGrantFinderImpl.class);

	@ServiceReference(type = CustomSQL.class)
	private CustomSQL _customSQL;

	private boolean _supportsCLOB = true;

}