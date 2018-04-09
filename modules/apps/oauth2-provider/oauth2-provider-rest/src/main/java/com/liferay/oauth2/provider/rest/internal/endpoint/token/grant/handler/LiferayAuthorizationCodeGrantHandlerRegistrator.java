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

package com.liferay.oauth2.provider.rest.internal.endpoint.token.grant.handler;

import com.liferay.oauth2.provider.configuration.OAuth2ProviderConfiguration;
import com.liferay.oauth2.provider.model.OAuth2Application;
import com.liferay.oauth2.provider.rest.internal.endpoint.constants.OAuth2ProviderRestEndpointConstants;
import com.liferay.oauth2.provider.rest.internal.endpoint.liferay.LiferayAccessTokenGrantHandlerHelper;
import com.liferay.oauth2.provider.rest.internal.endpoint.liferay.LiferayOAuthDataProvider;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.service.UserLocalService;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.grants.code.AuthorizationCodeGrantHandler;
import org.apache.cxf.rs.security.oauth2.grants.code.DigestCodeVerifier;
import org.apache.cxf.rs.security.oauth2.grants.code.ServerAuthorizationCodeGrant;
import org.apache.cxf.rs.security.oauth2.provider.AccessTokenGrantHandler;
import org.apache.cxf.rs.security.oauth2.utils.OAuthConstants;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Tomas Polesovsky
 */
@Component(
	configurationPid = "com.liferay.oauth2.provider.configuration.OAuth2ProviderConfiguration",
	immediate = true
)
public class LiferayAuthorizationCodeGrantHandlerRegistrator {

	@Activate
	protected void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		_oAuth2ProviderConfiguration = ConfigurableUtil.createConfigurable(
			OAuth2ProviderConfiguration.class, properties);

		if (!_oAuth2ProviderConfiguration.allowAuthorizationCodeGrant() &&
			!_oAuth2ProviderConfiguration.allowAuthorizationCodePKCEGrant()) {

			return;
		}

		AuthorizationCodeGrantHandler authorizationCodeGrantHandler =
			new AuthorizationCodeGrantHandler();

		authorizationCodeGrantHandler.setDataProvider(
			_liferayOAuthDataProvider);

		authorizationCodeGrantHandler.setExpectCodeVerifierForPublicClients(
			_oAuth2ProviderConfiguration.allowAuthorizationCodePKCEGrant());

		authorizationCodeGrantHandler.setCodeVerifierTransformer(
			new DigestCodeVerifier());

		_grantHandlerServiceRegistration = bundleContext.registerService(
			AccessTokenGrantHandler.class,
			new LiferayPermissionedAccessTokenGrantHandler(
				authorizationCodeGrantHandler, this::hasPermission),
			new Hashtable<>());
	}

	@Deactivate
	protected void deactivate() {
		if (_grantHandlerServiceRegistration != null) {
			_grantHandlerServiceRegistration.unregister();
		}
	}

	protected boolean hasPermission(
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

		if (!_accessTokenGrantHandlerHelper.clientsMatch(
				client, serverAuthorizationCodeGrant.getClient())) {

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

		long companyId = oAuth2Application.getCompanyId();

		if (client.isConfidential()) {
			if (!_oAuth2ProviderConfiguration.allowAuthorizationCodeGrant()) {
					if (_log.isDebugEnabled()) {
					_log.debug(
						"Auhotization code grant is disabled in " + companyId);
				}

				return false;
			}

			List<String> allowedGrantTypes = client.getAllowedGrantTypes();

			if (!allowedGrantTypes.contains(
					OAuthConstants.AUTHORIZATION_CODE_GRANT)) {

				if (_log.isDebugEnabled()) {
					_log.debug(
						"Client is not allowed to use " +
						OAuthConstants.AUTHORIZATION_CODE_GRANT + " grant");
				}

				return false;
			}
		}
		else {
			if (!_oAuth2ProviderConfiguration.
					allowAuthorizationCodePKCEGrant()) {

					if (_log.isDebugEnabled()) {
					_log.debug("PKCE grant is disabled in " + companyId);
				}

				return false;
			}

			List<String> allowedGrantTypes = client.getAllowedGrantTypes();

			if (!allowedGrantTypes.contains(
					OAuth2ProviderRestEndpointConstants.
						AUTHORIZATION_CODE_PKCE_GRANT)) {

				if (_log.isDebugEnabled()) {
					_log.debug(
						"Client is not allowed to use " +
						OAuth2ProviderRestEndpointConstants.
							AUTHORIZATION_CODE_PKCE_GRANT + " grant");
				}

				return false;
			}
		}

		String subjectId = serverAuthorizationCodeGrant.getSubject().getId();

		long userId = Long.parseLong(subjectId);

		return _accessTokenGrantHandlerHelper.hasCreateTokenPermission(
			userId, oAuth2Application);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LiferayAuthorizationCodeGrantHandlerRegistrator.class);

	@Reference
	private LiferayAccessTokenGrantHandlerHelper _accessTokenGrantHandlerHelper;

	private ServiceRegistration<AccessTokenGrantHandler>
		_grantHandlerServiceRegistration;

	@Reference(policyOption = ReferencePolicyOption.GREEDY)
	private LiferayOAuthDataProvider _liferayOAuthDataProvider;

	@Reference(target = "(model.class.name=com.liferay.oauth2.provider.model.OAuth2Application)")
	private ModelResourcePermission<OAuth2Application> _modelResourcePermission;

	private OAuth2ProviderConfiguration _oAuth2ProviderConfiguration;

	@Reference
	private UserLocalService _userLocalService;

}