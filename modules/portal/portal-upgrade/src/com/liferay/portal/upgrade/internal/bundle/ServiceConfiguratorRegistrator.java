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

package com.liferay.portal.upgrade.internal.bundle;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Release;
import com.liferay.portal.service.ReleaseLocalService;

import java.io.InputStream;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import com.liferay.portal.service.configuration.configurator.ServiceConfigurator;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Miguel Pastor
 * @author Carlos Sierra Andrés
 */
@Component(immediate = true)
public final class ServiceConfiguratorRegistrator {

	private ClassLoader _bundleClassLoader;
	private ServiceConfigurator _serviceConfigurator;

	@Activate
	protected void activate(BundleContext bundleContext) {
		Bundle bundle = bundleContext.getBundle();

		BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);

		_bundleClassLoader = bundleWiring.getClassLoader();

		List<Release> releases = _releaseLocalService.getReleases(
			QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		for (Release release : releases) {
			signalRelease(bundleContext, release);
		}
	}

	private void signalRelease(BundleContext bundleContext, Release release) {
		Dictionary<String, Object> properties = new Hashtable<>();

		String servletContextName = release.getServletContextName();

		properties.put("component.name", servletContextName);
		properties.put("release.build.number", release.getBuildNumber());

		ServiceRegistration<ServiceConfigurator> serviceRegistration =
			bundleContext.registerService(
				ServiceConfigurator.class,
				_serviceConfigurator, properties);

		_serviceConfiguratorRegistrations.put(
			servletContextName, serviceRegistration);
	}

	protected InputStream buildBundle(Release release) {
		StringBundler sb = new StringBundler(18);

		sb.append("Bundle-Name: ");
		sb.append(release.getServletContextName());
		sb.append(" Schema Capabilities Provider");
		sb.append(StringPool.NEW_LINE);
		sb.append("Bundle-SymbolicName: ");
		sb.append("synthetic.");
		sb.append(release.getServletContextName());
		sb.append(StringPool.NEW_LINE);
		sb.append("Bundle-Version: ");
		sb.append(release.getBuildNumber());
		sb.append(StringPool.NEW_LINE);
		sb.append("Provide-Capability: schema.provider;");
		sb.append("schema.provider=\"");
		sb.append(release.getServletContextName());
		sb.append("\";");
		sb.append("version:Version=\"");
		sb.append(release.getBuildNumber());
		sb.append("\"");

		Thread thread = Thread.currentThread();

		ClassLoader contextClassLoader = thread.getContextClassLoader();

		thread.setContextClassLoader(_bundleClassLoader);

		try {
			JavaArchive javaArchive = ShrinkWrap.create(JavaArchive.class);

			javaArchive.add(
				new StringAsset(sb.toString()), "META-INF/MANIFEST.MF");

			ZipExporter zipExporter = javaArchive.as(ZipExporter.class);

			return zipExporter.exportAsInputStream();
		}
		finally {
			thread.setContextClassLoader(contextClassLoader);
		}
	}

	@Deactivate
	protected void deactivate() {
		for (ServiceRegistration<ServiceConfigurator> serviceRegistration :
			_serviceConfiguratorRegistrations.values()) {

			serviceRegistration.unregister();
		}
	}

	@Reference
	protected void setReleaseLocalService(
		ReleaseLocalService releaseLocalService) {

		_releaseLocalService = releaseLocalService;
	}

	@Reference(target = "(original.bean=true)")
	protected void setServletContext(ServletContext servletContext) {
	}

	@Reference
	protected void setServiceConfigurator(
		ServiceConfigurator serviceConfigurator) {

		_serviceConfigurator = serviceConfigurator;
	}

	private ReleaseLocalService _releaseLocalService;

	private final Map<String, ServiceRegistration<ServiceConfigurator>>
		_serviceConfiguratorRegistrations = new HashMap<>();

}