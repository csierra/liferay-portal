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

package com.liferay.portal.configurator.extender;

import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.PropertiesUtil;

import java.io.IOException;

import java.net.URL;

import java.util.Dictionary;
import java.util.Enumeration;

import org.apache.felix.utils.extender.AbstractExtender;
import org.apache.felix.utils.extender.Extension;
import org.apache.felix.utils.log.Logger;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Carlos Sierra Andrés
 * @author Miguel Pastor
 */
@Component(immediate = true)
public class ConfiguratorExtender extends AbstractExtender {

	@Activate
	protected void activate(BundleContext bundleContext) throws Exception {
		setSynchronous(true);

		_logger = new Logger(bundleContext);

		start(bundleContext);
	}

	@Deactivate
	protected void deactivate(BundleContext bundleContext) throws Exception {
		stop(bundleContext);
	}

	@Override
	protected void debug(Bundle bundle, String s) {
		_logger.log(Logger.LOG_DEBUG, "[" + bundle + "] " + s);
	}

	@Override
	protected Extension doCreateExtension(Bundle bundle) throws Exception {
		Dictionary<String, String> headers = bundle.getHeaders();

		if (headers.get("Bundle-ConfigurationPath") == null) {
			return null;
		}

		return new ConfiguratorExtension(bundle);
	}

	@Override
	protected void error(String s, Throwable throwable) {
		_logger.log(Logger.LOG_ERROR, s, throwable);
	}

	@Reference
	protected void setConfigurationAdmin(
		ConfigurationAdmin configurationAdmin) {

		_configurationAdmin = configurationAdmin;
	}

	@Override
	protected void warn(Bundle bundle, String s, Throwable throwable) {
		_logger.log(Logger.LOG_DEBUG, "[" + bundle + "] " + s);
	}

	private ConfigurationAdmin _configurationAdmin;
	private Logger _logger;

	private class ConfiguratorExtension implements Extension {

		public ConfiguratorExtension(Bundle bundle) {
			_bundle = bundle;
		}

		@Override
		public void destroy() throws Exception {
		}

		@Override
		public void start() throws Exception {
			Dictionary<String, String> headers = _bundle.getHeaders();

			String configurationPath = headers.get("Bundle-ConfigurationPath");

			Enumeration<URL> configurationUrls = _bundle.findEntries(
				configurationPath, "*", true);

			if (configurationUrls == null) {
				return;
			}

			while (configurationUrls.hasMoreElements()) {
				URL url = configurationUrls.nextElement();

				try {
					_createConfiguration(_bundle, url);
				}
				catch (IOException ioe) {
					continue;
				}
			}
		}

		private Configuration _createConfiguration(Bundle bundle, URL url)
			throws IOException {

			String fileName = url.getFile();

			if (fileName.startsWith("/")) {
				fileName = fileName.substring(1);
			}

			int lastIndexOfDash = fileName.lastIndexOf('-');
			int lastIndexOfSlash = fileName.lastIndexOf('/');

			String factoryPid = null;
			String pid = null;

			if (lastIndexOfDash > 0) {
				factoryPid = fileName.substring(0, lastIndexOfDash);
				pid = fileName.substring(lastIndexOfDash + 1);

				if (lastIndexOfSlash > 0) {
					factoryPid = factoryPid.substring(lastIndexOfSlash + 1);
				}
			}
			else {
				pid = fileName;

				if (lastIndexOfSlash > 0) {
					pid = pid.substring(lastIndexOfSlash + 1);
				}
			}

			Dictionary<String, Object> properties = _loadProperties(url);

			if (factoryPid != null) {
				return _getOrCreateFactoryConfiguration(
							bundle, pid, factoryPid, properties);
			}
			else {
				return _getOrCreateConfiguration(pid, properties);
			}
		}

		private Configuration _getOrCreateConfiguration(
				String pid, Dictionary<String, Object> writable)
			throws IOException {

			try {
				Configuration[] configurations =
						_configurationAdmin.listConfigurations(
							"(service.pid=" + pid + ")");

				if (!ArrayUtil.isEmpty(configurations)) {
					return configurations[0];
				}
			}
			catch (InvalidSyntaxException ise) {
				throw new RuntimeException(ise);
			}

			Configuration configuration = _configurationAdmin.getConfiguration(
				pid);

			configuration.update(writable);

			return configuration;
		}

		private Configuration _getOrCreateFactoryConfiguration(
				Bundle bundle, String pid, String factoryPid,
				Dictionary<String, Object> writable)
			throws IOException {

			String configuratorUrl = bundle.getSymbolicName() + "#" + pid;

			try {
				Configuration[] configurations =
						_configurationAdmin.listConfigurations(
							"(configurator.url=" + configuratorUrl + ")");

				if (!ArrayUtil.isEmpty(configurations)) {
					return configurations[0];
				}
			}
			catch (InvalidSyntaxException ise) {
				throw new RuntimeException(ise);
			}

			Configuration factoryConfiguration =
					_configurationAdmin.createFactoryConfiguration(
						factoryPid, null);

			writable.put("configurator.url", configuratorUrl);

			factoryConfiguration.update(writable);

			return factoryConfiguration;
		}

		private Dictionary<String, Object> _loadProperties(URL url)
			throws IOException {

			Dictionary<?, ?> properties = PropertiesUtil.load(
				url.openStream(), "UTF-8");

			return (Dictionary<String, Object>)properties;
		}

		private final Bundle _bundle;

	}

}