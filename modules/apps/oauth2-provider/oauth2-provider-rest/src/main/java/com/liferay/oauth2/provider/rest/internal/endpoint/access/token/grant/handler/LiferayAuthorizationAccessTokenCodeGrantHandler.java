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

package com.liferay.oauth2.provider.rest.internal.endpoint.access.token.grant.handler;

import com.liferay.oauth2.provider.configuration.OAuth2ProviderConfiguration;
import com.liferay.oauth2.provider.model.OAuth2Application;
import com.liferay.oauth2.provider.rest.internal.endpoint.constants.OAuth2ProviderRestEndpointConstants;
import com.liferay.oauth2.provider.rest.internal.endpoint.liferay.LiferayOAuthDataProvider;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.service.UserLocalService;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.common.UserSubject;
import org.apache.cxf.rs.security.oauth2.grants.code.AuthorizationCodeGrantHandler;
import org.apache.cxf.rs.security.oauth2.grants.code.DigestCodeVerifier;
import org.apache.cxf.rs.security.oauth2.grants.code.ServerAuthorizationCodeGrant;
import org.apache.cxf.rs.security.oauth2.provider.AccessTokenGrantHandler;
import org.apache.cxf.rs.security.oauth2.utils.OAuthConstants;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Tomas Polesovsky
 */
@Component(
	configurationPid = "com.liferay.oauth2.provider.configuration.OAuth2ProviderConfiguration",
	immediate = true, service = AccessTokenGrantHandler.class
)
public class LiferayAuthorizationAccessTokenCodeGrantHandler
	extends BaseAccessTokenGrantHandler {

	@Activate
	protected void activate(Map<String, Object> properties) {
		_oAuth2ProviderConfiguration = ConfigurableUtil.createConfigurable(
			OAuth2ProviderConfiguration.class, properties);

		_authorizationCodeGrantHandler = new AuthorizationCodeGrantHandler();

		_authorizationCodeGrantHandler.setDataProvider(
			_liferayOAuthDataProvider);

		_authorizationCodeGrantHandler.setExpectCodeVerifierForPublicClients(
			_oAuth2ProviderConfiguration.allowAuthorizationCodePKCEGrant());

		_authorizationCodeGrantHandler.setCodeVerifierTransformer(
			new DigestCodeVerifier());
	}

	@Override
	protected AccessTokenGrantHandler getAccessTokenGrantHandler() {
		return _authorizationCodeGrantHandler;
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

		if (!clientsMatch(client, serverAuthorizationCodeGrant.getClient())) {

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

		UserSubject userSubject = serverAuthorizationCodeGrant.getSubject();

		long userId = Long.parseLong(userSubject.getId());

		return hasCreateTokenPermission(userId, oAuth2Application);
	}

	@Override
	protected boolean isGrantHandlerEnabled() {
		if (!_oAuth2ProviderConfiguration.allowAuthorizationCodeGrant() &&
			!_oAuth2ProviderConfiguration.allowAuthorizationCodePKCEGrant()) {

			return false;
		}

		return true;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LiferayAuthorizationAccessTokenCodeGrantHandler.class);

	private AuthorizationCodeGrantHandler _authorizationCodeGrantHandler;

	@Reference
	private LiferayOAuthDataProvider _liferayOAuthDataProvider;

	private OAuth2ProviderConfiguration _oAuth2ProviderConfiguration;

}