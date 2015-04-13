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

package com.liferay.portal.rest.extender.test.activator.config;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * @author Carlos Sierra Andrés
 */
public class ConfigAdminActivator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		ServiceReference<ConfigurationAdmin> serviceReference =
			context.getServiceReference(ConfigurationAdmin.class);

		ConfigurationAdmin configurationAdmin = context.getService(
			serviceReference);

		try {
			_cxfConfiguration = configurationAdmin.createFactoryConfiguration(
				"com.liferay.portal.ws.WebServicePublisherConfiguration", null);

			Dictionary<String, Object> properties = new Hashtable<>();

			properties.put("contextPath", "/rest-test");

			_cxfConfiguration.update(properties);

			_restConfiguration = configurationAdmin.createFactoryConfiguration(
				"com.liferay.portal.soap.extender.RestExtenderConfiguration",
				null);

			properties = new Hashtable<>();

			properties.put("contextPaths", new String[] {"/rest-test"});
			properties.put(
				"applicationFilters",
				new String[] {"(jaxrs.application=true)"});

			_restConfiguration.update(properties);
		}
		finally {
			context.ungetService(serviceReference);
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		_restConfiguration.delete();

		_cxfConfiguration.delete();
	}

	private Configuration _cxfConfiguration;
	private Configuration _restConfiguration;

}