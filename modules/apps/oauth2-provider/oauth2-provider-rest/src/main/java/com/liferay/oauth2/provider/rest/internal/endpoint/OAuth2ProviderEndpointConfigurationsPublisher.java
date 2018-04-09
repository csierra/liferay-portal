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

package com.liferay.oauth2.provider.rest.internal.endpoint;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.MapUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author Tomas Polesovsky
 */
@Component(immediate = true, property = "context.path=/oauth2")
public class OAuth2ProviderEndpointConfigurationsPublisher {

	private Configuration _restConfiguration;
	private Configuration _cxfConfiguration;

	@Activate
	public void activate(
		BundleContext bundleContext, final Map<String, Object> properties)
		throws IOException {

		ServiceReference<ConfigurationAdmin> serviceReference =
			bundleContext.getServiceReference(ConfigurationAdmin.class);

		try {
			ConfigurationAdmin configurationAdmin = bundleContext.getService(
				serviceReference);

			String contextPath = MapUtil.getString(
				properties, "context.path", "/oauth2");

			_cxfConfiguration =
				configurationAdmin.createFactoryConfiguration(
					"com.liferay.portal.remote.cxf.common.configuration." +
					"CXFEndpointPublisherConfiguration", "?");

			Dictionary<String, Object> dictionary = new Hashtable<>();

			dictionary.put("contextPath", contextPath);

			_cxfConfiguration.update(dictionary);

			_restConfiguration =
				configurationAdmin.createFactoryConfiguration(
					"com.liferay.portal.remote.rest.extender.configuration." +
					"RestExtenderConfiguration", "?");

			dictionary = new Hashtable<>();

			dictionary.put("contextPaths", new String[] {contextPath});
			dictionary.put(
				"jaxRsApplicationFilterStrings",
				new String[] {
					"(component.name=" +
						OAuth2EndpointApplication.class.getName() + ")"});

			_restConfiguration.update(dictionary);
		}
		finally {
			bundleContext.ungetService(serviceReference);
		}
	}

	public void deactivate(){
		try {
			_restConfiguration.delete();
		}
		catch (IOException ioe) {
			_log.error("Unable to remove REST configuration", ioe);
		}

		try {
			_cxfConfiguration.delete();
		}
		catch (IOException ioe) {
			_log.error("Unable to remove CXF configuration", ioe);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		OAuth2ProviderEndpointConfigurationsPublisher.class);

}