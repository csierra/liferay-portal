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

import com.liferay.portal.kernel.util.MapUtil;
import org.apache.cxf.rs.security.oauth2.services.TokenIntrospectionService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import java.util.Hashtable;
import java.util.Map;

@Component(immediate=true)
public class TokenIntrospectionServiceRegistrator {

	private ServiceRegistration<Object> _serviceRegistration;

	@Activate
	protected void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		boolean enabled = MapUtil.getBoolean(
			properties, "oauth2.allow.authorization.code.grant", true);

		if (enabled) {
			TokenIntrospectionService tokenIntrospectionService =
				new TokenIntrospectionService();

			tokenIntrospectionService.setBlockUnauthorizedRequests(true);
			tokenIntrospectionService.setBlockUnsecureRequests(true);
			tokenIntrospectionService.setDataProvider(
				_liferayOAuthDataProvider);

			Hashtable<String, Object> endpointProperties = new Hashtable<>();

			endpointProperties.put("liferay.oauth2.endpoint", true);

			_serviceRegistration = bundleContext.registerService(
				Object.class, tokenIntrospectionService, endpointProperties);
		}
	}

	@Deactivate
	protected void deactivate() {
		if (_serviceRegistration!=null) {
			_serviceRegistration.unregister();
		}
	}

	@Reference(policyOption = ReferencePolicyOption.GREEDY)
	private LiferayOAuthDataProvider _liferayOAuthDataProvider;

}
