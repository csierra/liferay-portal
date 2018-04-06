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
import com.liferay.oauth2.provider.rest.internal.endpoint.liferay.LiferayAccessTokenGrantHandlerHelper;
import com.liferay.oauth2.provider.rest.internal.endpoint.liferay.LiferayOAuthDataProvider;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken;
import org.apache.cxf.rs.security.oauth2.grants.refresh.RefreshTokenGrantHandler;
import org.apache.cxf.rs.security.oauth2.provider.AccessTokenGrantHandler;
import org.apache.cxf.rs.security.oauth2.tokens.refresh.RefreshToken;
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

@Component(
	configurationPid = "com.liferay.oauth2.provider.configuration.OAuth2ProviderConfiguration",
	immediate = true
)
public class LiferayRefreshTokenGrantHandlerRegistrator {

	private ServiceRegistration<AccessTokenGrantHandler>
		_serviceRegistration;

	@Activate
	protected void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		OAuth2ProviderConfiguration oAuth2ProviderConfiguration =
			ConfigurableUtil.createConfigurable(
				OAuth2ProviderConfiguration.class, properties);

		if (!oAuth2ProviderConfiguration.allowRefreshTokenGrant()) {
			return;
		}

		RefreshTokenGrantHandler refreshTokenGrantHandler =
			new RefreshTokenGrantHandler();

		refreshTokenGrantHandler.setDataProvider(
			_liferayOAuthDataProvider);

		_serviceRegistration = bundleContext.registerService(
			AccessTokenGrantHandler.class,
			new LiferayPermissionedAccessTokenGrantHandler(
				refreshTokenGrantHandler,
				this::hasPermission),
			new Hashtable<>());
	}

	@Deactivate
	protected void deactivate() {
		if (_serviceRegistration != null) {
			_serviceRegistration.unregister();
		}
	}

	protected boolean hasPermission(
		Client client, MultivaluedMap<String, String> params) {

		String refreshTokenString = params.getFirst("refresh_token");

		if (refreshTokenString == null) {
			if (_log.isDebugEnabled()) {
				_log.debug("No refresh_token parameter was provided.");
			}

			return false;
		}

		RefreshToken refreshToken =
			_liferayOAuthDataProvider.getRefreshToken(refreshTokenString);

		if (refreshToken == null) {
			if (_log.isDebugEnabled()) {
				_log.debug("No refresh token found for " + refreshTokenString);
			}

			return false;
		}

		if(!_accessTokenGrantHandlerHelper.clientsMatch(
			client, refreshToken.getClient())) {

			// audit: Trying to refresh token with other client's authentication

			_liferayOAuthDataProvider.doRevokeRefreshToken(refreshToken);

			for (String accessToken : refreshToken.getAccessTokens()) {
				ServerAccessToken serverAccessToken =
					_liferayOAuthDataProvider.getAccessToken(accessToken);

				_liferayOAuthDataProvider.doRevokeAccessToken(
					serverAccessToken);
			}

			if (_log.isDebugEnabled()) {
				_log.debug(
					"Client authentication doesn't mach refresh token's client");
			}

			return false;
		}

		String subjectId = refreshToken.getSubject().getId();

		long userId = Long.parseLong(subjectId);

		OAuth2Application oAuth2Application =
			_liferayOAuthDataProvider.resolveOAuth2Application(
				refreshToken.getClient());

		return _accessTokenGrantHandlerHelper.hasCreateTokenPermission(
			userId, oAuth2Application);
	}

	private static Log _log =
		LogFactoryUtil.getLog(
			LiferayClientCredentialsGrantHandlerRegistrator.class);

	@Reference(policyOption = ReferencePolicyOption.GREEDY)
	private LiferayOAuthDataProvider _liferayOAuthDataProvider;

	@Reference
	private LiferayAccessTokenGrantHandlerHelper _accessTokenGrantHandlerHelper;

}
