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

package com.liferay.oauth2.provider.internal.upgrade;

import com.liferay.oauth2.provider.internal.upgrade.v1_1_0.UpgradeOAuth2ScopeGrant;
import com.liferay.oauth2.provider.internal.upgrade.v2_0_0.UpgradeOAuth2ApplicationScopeAliases;
import com.liferay.oauth2.provider.internal.upgrade.v2_0_0.UpgradeOAuth2ApplicationScopeAliases2;
import com.liferay.oauth2.provider.internal.upgrade.v2_0_0.UpgradeOAuth2ScopeGrant2;
import com.liferay.oauth2.provider.scope.liferay.ScopeLocator;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Carlos Sierra Andrés
 */
@Component(service = UpgradeStepRegistrator.class)
public class OAuth2ServiceUpgrade implements UpgradeStepRegistrator {

	@Override
	public void register(Registry registry) {
		registry.register("1.0.0", "1.1.0", new UpgradeOAuth2ScopeGrant());
		registry.register("1.1.0", "1.2.0", new UpgradeOAuth2ScopeGrant2());
		registry.register(
			"1.2.0", "1.2.1",
			new UpgradeOAuth2ApplicationScopeAliases(
				_companyLocalService, _scopeLocator));
		registry.register(
			"1.2.1", "2.0.0", new UpgradeOAuth2ApplicationScopeAliases2());
	}

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private ScopeLocator _scopeLocator;

}