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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;

/**
 * @author Carlos Sierra Andrés
 */
@Component(immediate = true)
public class Configurator {

	@Activate
	public void activate(BundleContext bundleContext) {
		_bundleTracker = new BundleTracker<>(
			bundleContext, Bundle.ACTIVE | Bundle.RESOLVED,
			new BundleTrackerCustomizer<Collection<Configuration>>() {

				@Override
				public Collection<Configuration> addingBundle(
					Bundle bundle, BundleEvent bundleEvent) {

					Dictionary<String, String> headers = bundle.getHeaders();

					String configurationPath = headers.get(
						"Bundle-ConfigurationPath");

					if (configurationPath == null) {
						return null;
					}

					Enumeration<URL> configurationUrls = bundle.findEntries(
						configurationPath, "*", true);

					if (configurationUrls == null) {
						return null;
					}

					Collection<Configuration> configurations =
						new ArrayList<>();

					while (configurationUrls.hasMoreElements()) {
						URL url = configurationUrls.nextElement();

						try {
							configurations.add(
								_createConfiguration(bundle, url));
						}
						catch (IOException e) {
							continue;
						}
					}

					return configurations;
				}

				@Override
				public void modifiedBundle(
					Bundle bundle, BundleEvent bundleEvent,
					Collection<Configuration> configurations) {
				}

				@Override
				public void removedBundle(
					Bundle bundle, BundleEvent bundleEvent,
					Collection<Configuration> configurations) {
				}
			});

		_bundleTracker.open();
	}

	@Deactivate
	protected void deactivate() {
		_bundleTracker.close();
	}

	@Reference
	protected void setConfigurationAdmin(
		ConfigurationAdmin configurationAdmin) {

		_configurationAdmin = configurationAdmin;
	}

	private Configuration _createConfiguration(Bundle bundle, URL url)
		throws IOException {

		String fileName = url.getFile();

		if (fileName.startsWith("/")) {
			fileName = fileName.substring(1);
		}

		int lastIndexOfDash = fileName.lastIndexOf('-');
		int lastIndexOfSlash = fileName.lastIndexOf('/');

		String pid = null;
		String factoryPid = null;

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
		catch (InvalidSyntaxException e) {
			throw new RuntimeException(e);
		}

		Configuration configuration = _configurationAdmin.getConfiguration(pid);

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
		catch (InvalidSyntaxException e) {
			throw new RuntimeException(e);
		}

		Configuration factoryConfiguration =
			_configurationAdmin.createFactoryConfiguration(factoryPid, null);

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

	private BundleTracker<Collection<Configuration>> _bundleTracker;
	private ConfigurationAdmin _configurationAdmin;

}