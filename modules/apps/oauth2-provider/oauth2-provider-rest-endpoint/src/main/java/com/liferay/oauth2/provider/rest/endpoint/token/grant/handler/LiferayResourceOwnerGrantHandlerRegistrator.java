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

package com.liferay.oauth2.provider.rest.endpoint.token.grant.handler;

import com.liferay.oauth2.provider.configuration.OAuth2ProviderConfiguration;
import com.liferay.oauth2.provider.model.OAuth2Application;
import com.liferay.oauth2.provider.rest.endpoint.liferay.LiferayAccessTokenGrantHandlerHelper;
import com.liferay.oauth2.provider.rest.endpoint.liferay.LiferayOAuthDataProvider;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.common.UserSubject;
import org.apache.cxf.rs.security.oauth2.grants.owner.ResourceOwnerGrantHandler;
import org.apache.cxf.rs.security.oauth2.grants.owner.ResourceOwnerLoginHandler;
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
public class LiferayResourceOwnerGrantHandlerRegistrator {

	private ServiceRegistration<AccessTokenGrantHandler>
		_serviceRegistration;

	@Activate
	protected void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		OAuth2ProviderConfiguration oAuth2ProviderConfiguration =
			ConfigurableUtil.createConfigurable(
				OAuth2ProviderConfiguration.class, properties);

		if (oAuth2ProviderConfiguration.allowResourceOwnerPasswordCredentialsGrant()) {
			ResourceOwnerGrantHandler resourceOwnerGrantHandler =
				new ResourceOwnerGrantHandler();

			resourceOwnerGrantHandler.setLoginHandler(
				_liferayLoginHandler);
			resourceOwnerGrantHandler.setDataProvider(
				_liferayOAuthDataProvider);

			_serviceRegistration = bundleContext.registerService(
				AccessTokenGrantHandler.class,
				new LiferayPermissionedAccessTokenGrantHandler(
					resourceOwnerGrantHandler,
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

		String userName = params.getFirst("username");
		String password = params.getFirst("password");

		if (userName == null || password == null) {
			if (_log.isDebugEnabled()) {
				_log.debug("username or password parameter was not provided.");
			}

			return false;
		}

		UserSubject userSubject = _liferayLoginHandler.createSubject(
			userName, password);

		String subjectId = userSubject.getId();

		long userId = Long.parseLong(subjectId);

		OAuth2Application oAuth2Application =
			_liferayOAuthDataProvider.resolveOAuth2Application(
				client);

		return _accessTokenGrantHandlerHelper.hasCreateTokenPermission(
			userId, oAuth2Application);
	}

	private static Log _log =
		LogFactoryUtil.getLog(
			LiferayResourceOwnerGrantHandlerRegistrator.class);

	@Reference(policyOption = ReferencePolicyOption.GREEDY)
	private LiferayOAuthDataProvider _liferayOAuthDataProvider;

	@Reference(policyOption = ReferencePolicyOption.GREEDY)
	private ResourceOwnerLoginHandler _liferayLoginHandler;

	@Reference
	private LiferayAccessTokenGrantHandlerHelper _accessTokenGrantHandlerHelper;

}
