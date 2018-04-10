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

package com.liferay.oauth2.provider.rest.endpoint.introspection;

import com.liferay.oauth2.provider.rest.endpoint.constants.OAuth2ProviderRestEndpointConstants;
import com.liferay.oauth2.provider.rest.endpoint.liferay.LiferayOAuthDataProvider;
import com.liferay.portal.kernel.util.MapUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import java.util.Hashtable;
import java.util.Map;

@Component(
	immediate=true,
	property = {
		"oauth2.allow.token.introspection.endpoint=true",
		"oauth2.allow.token.introspection.endpoint.public.clients=true"
	}
)
public class LiferayTokenIntrospectionServiceRegistrator {

	private ServiceRegistration<Object> _endpointServiceRegistration;
	private ServiceRegistration<Class> _classServiceRegistration;

	@Activate
	protected void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		boolean enabled = MapUtil.getBoolean(
			properties, "oauth2.allow.token.introspection.endpoint", true);

		if (enabled) {
			boolean publicClientsEnabled = MapUtil.getBoolean(
				properties,
				"oauth2.allow.token.introspection.endpoint.public.clients",
				true);

			LiferayTokenIntrospectionService liferayTokenIntrospectionService =
				new LiferayTokenIntrospectionService(
					_liferayOAuthDataProvider, publicClientsEnabled);

			Hashtable<String, Object> endpointProperties = new Hashtable<>();

			endpointProperties.put(
				OAuth2ProviderRestEndpointConstants.LIFERAY_OAUTH2_ENDPOINT, true);

			_endpointServiceRegistration = bundleContext.registerService(
				Object.class, liferayTokenIntrospectionService,
				endpointProperties);

			Hashtable<String, Object> classProperties = new Hashtable<>();

			classProperties.put(OAuth2ProviderRestEndpointConstants.LIFERAY_OAUTH2_ENDPOINT_CLASS, true);

			_classServiceRegistration = bundleContext.registerService(
				Class.class,
				TokenIntrospectionJSONProviderMessageBodyWriter.class,
				classProperties);
		}
	}

	@Deactivate
	protected void deactivate() {
		if (_classServiceRegistration != null) {
			_classServiceRegistration.unregister();
		}

		if (_endpointServiceRegistration != null) {
			_endpointServiceRegistration.unregister();
		}
	}

	@Reference(policyOption = ReferencePolicyOption.GREEDY)
	private LiferayOAuthDataProvider _liferayOAuthDataProvider;

}