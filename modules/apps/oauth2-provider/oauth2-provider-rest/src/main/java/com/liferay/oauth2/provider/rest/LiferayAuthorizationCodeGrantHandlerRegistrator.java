/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.oauth2.provider.rest;

import com.liferay.oauth2.provider.constants.OAuth2ProviderActionKeys;
import com.liferay.oauth2.provider.model.OAuth2Application;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.MapUtil;
import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.grants.code.AuthorizationCodeGrantHandler;
import org.apache.cxf.rs.security.oauth2.grants.code.DigestCodeVerifier;
import org.apache.cxf.rs.security.oauth2.grants.code.ServerAuthorizationCodeGrant;
import org.apache.cxf.rs.security.oauth2.provider.AccessTokenGrantHandler;
import org.apache.cxf.rs.security.oauth2.provider.SubjectCreator;
import org.apache.cxf.rs.security.oauth2.services.AuthorizationCodeGrantService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import javax.ws.rs.core.MultivaluedMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

@Component(
	configurationPid = "com.liferay.oauth2.configuration.OAuth2Configuration",
	immediate = true
)
public class LiferayAuthorizationCodeGrantHandlerRegistrator {

	private ServiceRegistration<AccessTokenGrantHandler>
		_grantHandlerServiceRegistration;
	private ServiceRegistration<Object> _endpointServiceRegistration;

	@Activate
	protected void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		boolean enabled = MapUtil.getBoolean(
				properties, "oauth2.allow.authorization.code.grant", true);

		if (enabled) {
			AuthorizationCodeGrantService authorizationCodeGrantService =
				new AuthorizationCodeGrantService();

			authorizationCodeGrantService.setCanSupportPublicClients(true);
			authorizationCodeGrantService.setDataProvider(
				_liferayOAuthDataProvider);

			authorizationCodeGrantService.setSubjectCreator(_subjectCreator);

			Hashtable<String, Object> endpointProperties = new Hashtable<>();

			endpointProperties.put("liferay.oauth2.endpoint", true);

			_endpointServiceRegistration = bundleContext.registerService(
				Object.class, authorizationCodeGrantService,
				endpointProperties);

			AuthorizationCodeGrantHandler authorizationCodeGrantHandler =
				new AuthorizationCodeGrantHandler();

			authorizationCodeGrantHandler.setDataProvider(
				_liferayOAuthDataProvider);

			authorizationCodeGrantHandler.setExpectCodeVerifierForPublicClients(
				true);

			authorizationCodeGrantHandler.setCodeVerifierTransformer(
				new DigestCodeVerifier());

			_grantHandlerServiceRegistration = bundleContext.registerService(
				AccessTokenGrantHandler.class,
				new LiferayPermissionedAccessTokenGrantHandler(
					authorizationCodeGrantHandler,
					this::hasCreateTokenPermission),
				new Hashtable<>());
		}
	}

	protected boolean clientsMatch(Client client1, Client client2) {
		String client1Id = client1.getClientId();
		String client2Id = client2.getClientId();

		if (!Objects.equals(client1Id, client2Id)) {
			return false;
		}

		Map<String, String> properties = client1.getProperties();

		String companyId1 = properties.get("companyId");

		properties = client2.getProperties();

		String companyId2 = properties.get("companyId");

		if (!Objects.equals(companyId1, companyId2)) {
			return false;
		}

		return true;
	}

	protected boolean hasCreateTokenPermission(
		Client client, MultivaluedMap<String, String> params) {

		String code = params.getFirst("code");

		if (code == null) {
			if (_log.isDebugEnabled()) {
				_log.debug("No code parameter was provided.");
			}

			return false;
		}

		ServerAuthorizationCodeGrant serverAuthorizationCodeGrant =
			_liferayOAuthDataProvider.getCodeGrant(code);

		if (serverAuthorizationCodeGrant == null) {
			if (_log.isDebugEnabled()) {
				_log.debug("No code grant found for code " + code);
			}

			return false;
		}

		if(!clientsMatch(client, serverAuthorizationCodeGrant.getClient())) {
			// audit: Trying to get other client's code

			_liferayOAuthDataProvider.removeCodeGrant(code);

			if (_log.isDebugEnabled()) {
				_log.debug("Client authentication doesn't mach code's client");
			}

			return false;
		}

		OAuth2Application oAuth2Application =
			_liferayOAuthDataProvider.resolveOAuth2Application(
				serverAuthorizationCodeGrant.getClient());

		String subjectId = serverAuthorizationCodeGrant.getSubject().getId();

		long userId = Long.parseLong(subjectId);

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

		if (permissionChecker.hasOwnerPermission(
			oAuth2Application.getCompanyId(), OAuth2Application.class.getName(),
			oAuth2Application.getOAuth2ApplicationId(),
			oAuth2Application.getUserId(),
			OAuth2ProviderActionKeys.ACTION_CREATE_TOKEN)) {

			return true;
		}

		if (permissionChecker.hasPermission(
			0, OAuth2Application.class.getName(),
			oAuth2Application.getOAuth2ApplicationId(),
			OAuth2ProviderActionKeys.ACTION_CREATE_TOKEN)) {

			return true;
		}

		if (_log.isDebugEnabled()) {
			_log.debug(
				"User " + userId +
					" doesn't have permission to create access token for " +
						"client " + client.getClientId());
		}

		return false;
	}

	@Deactivate
	protected void deactivate() {
		if (_endpointServiceRegistration != null) {
			_endpointServiceRegistration.unregister();
		}
		if (_grantHandlerServiceRegistration != null) {
			_grantHandlerServiceRegistration.unregister();
		}
	}

	private static Log _log =
		LogFactoryUtil.getLog(
			LiferayAuthorizationCodeGrantHandlerRegistrator.class);

	@Reference(policyOption = ReferencePolicyOption.GREEDY)
	private LiferayOAuthDataProvider _liferayOAuthDataProvider;

	@Reference
	private UserLocalService _userLocalService;

	@Reference(policyOption = ReferencePolicyOption.GREEDY)
	private SubjectCreator _subjectCreator;

}
