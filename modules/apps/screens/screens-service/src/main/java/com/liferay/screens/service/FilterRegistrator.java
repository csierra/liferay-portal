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

package com.liferay.screens.service;

import com.liferay.portal.servlet.filters.authverifier.AuthVerifierFilter;

import java.util.Hashtable;

import javax.servlet.Filter;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Carlos Sierra Andrés
 */
@Component(immediate = true)
public class FilterRegistrator {

	@Activate
	protected void activate(BundleContext bundleContext) {
		Hashtable<String, Object> properties = new Hashtable<>();

		properties.put("before-filter", "Auto Login Filter");
		properties.put("dispatcher", "REQUEST");
		properties.put("servlet-context-name", "");
		properties.put("servlet-filter-name", "ScreensAuthVerifierFilter");
		properties.put("url-pattern", "/documents/*");
		properties.put(
			"init.param.auth.verifier.BasicAuthHeaderAuthVerifier.urls." +
				"includes",
			"/*");

		_serviceRegistration = bundleContext.registerService(
			Filter.class, new AuthVerifierFilter(), properties);
	}

	@Deactivate
	protected void deactivate() {
		_serviceRegistration.unregister();
	}

	private ServiceRegistration<Filter> _serviceRegistration;

}