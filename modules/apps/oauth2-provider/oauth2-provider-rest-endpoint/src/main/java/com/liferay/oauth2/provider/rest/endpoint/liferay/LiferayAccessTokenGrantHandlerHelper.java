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

package com.liferay.oauth2.provider.rest.endpoint.liferay;

import com.liferay.oauth2.provider.constants.OAuth2ProviderActionKeys;
import com.liferay.oauth2.provider.model.OAuth2Application;
import com.liferay.oauth2.provider.rest.endpoint.constants.OAuth2ProviderRestEndpointConstants;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.service.UserLocalService;
import org.apache.cxf.rs.security.oauth2.common.Client;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Map;
import java.util.Objects;

/**
 * @author Tomas Polesovsky
 */
@Component (
	immediate = true, service = LiferayAccessTokenGrantHandlerHelper.class
)
public class LiferayAccessTokenGrantHandlerHelper {

	public boolean clientsMatch(Client client1, Client client2) {
		String client1Id = client1.getClientId();
		String client2Id = client2.getClientId();

		if (!Objects.equals(client1Id, client2Id)) {
			return false;
		}

		Map<String, String> properties = client1.getProperties();

		String companyId1 = properties.get(
			OAuth2ProviderRestEndpointConstants.COMPANY_ID);

		properties = client2.getProperties();

		String companyId2 = properties.get(
			OAuth2ProviderRestEndpointConstants.COMPANY_ID);

		if (!Objects.equals(companyId1, companyId2)) {
			return false;
		}

		return true;
	}

	public boolean hasCreateTokenPermission(
		long userId, OAuth2Application oAuth2Application){

		PermissionChecker permissionChecker = null;

		try {
			User user = _userLocalService.getUserById(userId);

			permissionChecker =
				PermissionCheckerFactoryUtil.create(user);
		}
		catch (Exception e) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"Unable to create PermissionChecker for user " + userId);
			}

			return false;
		}

		try {
			if (_modelResourcePermission.contains(
				permissionChecker, oAuth2Application,
				OAuth2ProviderActionKeys.ACTION_CREATE_TOKEN)) {

				return true;
			}
		}
		catch (PortalException pe) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"Unable to check permissions for application " +
						oAuth2Application,
					pe);
			}
		}

		if (_log.isDebugEnabled()) {
			_log.debug(
				"User " + userId +
					" doesn't have permission to create access token for " +
						"client " + oAuth2Application.getClientId());
		}

		return false;
	}

	private static Log _log = LogFactoryUtil.getLog(
		LiferayAccessTokenGrantHandlerHelper.class);

	@Reference
	private UserLocalService _userLocalService;

	@Reference(target = "(model.class.name=com.liferay.oauth2.provider.model.OAuth2Application)")
	private ModelResourcePermission<OAuth2Application>
		_modelResourcePermission;

}
