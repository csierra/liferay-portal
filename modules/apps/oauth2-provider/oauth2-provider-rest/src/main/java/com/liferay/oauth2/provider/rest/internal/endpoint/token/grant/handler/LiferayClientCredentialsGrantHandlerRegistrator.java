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
import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.grants.clientcred.ClientCredentialsGrantHandler;
import org.apache.cxf.rs.security.oauth2.provider.AccessTokenGrantHandler;
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
public class LiferayClientCredentialsGrantHandlerRegistrator {

	private ServiceRegistration<AccessTokenGrantHandler>
		_serviceRegistration;

	@Activate
	protected void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		OAuth2ProviderConfiguration oAuth2ProviderConfiguration =
			ConfigurableUtil.createConfigurable(
				OAuth2ProviderConfiguration.class, properties);

		if (oAuth2ProviderConfiguration.allowClientCredentialsGrant()) {
			ClientCredentialsGrantHandler clientCredentialsGrantHandler =
				new ClientCredentialsGrantHandler();

			clientCredentialsGrantHandler.setDataProvider(
				_liferayOAuthDataProvider);

			_serviceRegistration = bundleContext.registerService(
				AccessTokenGrantHandler.class,
				new LiferayPermissionedAccessTokenGrantHandler(
					clientCredentialsGrantHandler,
					this::hasPermission),
				new Hashtable<>());
		}
	}

	@Deactivate
	protected void deactivate() {
		if (_serviceRegistration != null) {
			_serviceRegistration.unregister();
		}
	}

	protected boolean hasPermission(
		Client client, MultivaluedMap<String, String> params) {

		OAuth2Application oAuth2Application =
			_liferayOAuthDataProvider.resolveOAuth2Application(client);

		long userId = oAuth2Application.getUserId();

		return _accessTokenGrantHandlerHelper.hasCreateTokenPermission(
			userId, oAuth2Application);
	}

	@Reference(policyOption = ReferencePolicyOption.GREEDY)
	private LiferayOAuthDataProvider _liferayOAuthDataProvider;

	@Reference
	private LiferayAccessTokenGrantHandlerHelper _accessTokenGrantHandlerHelper;

}
