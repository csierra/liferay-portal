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

import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.model.Release;
import com.liferay.portal.service.ReleaseLocalService;
import com.liferay.portal.service.configuration.configurator.ServiceConfigurator;

import java.io.InputStream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.felix.utils.log.Logger;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Miguel Pastor
 * @author Carlos Sierra Andrés
 */
@Component(immediate = true, service = ServiceConfiguratorRegistrator.class)
public final class ServiceConfiguratorRegistrator {

	public void signalRelease(Release release) {
		Dictionary<String, Object> properties = new Hashtable<>();

		String servletContextName = release.getServletContextName();

		ServiceRegistration<ServiceConfigurator> oldServiceRegistration =
			_serviceConfiguratorRegistrations.get(servletContextName);

		if (oldServiceRegistration != null) {
			oldServiceRegistration.unregister();
		}

		properties.put("component.name", servletContextName);
		properties.put("release.build.number", release.getBuildNumber());

		ServiceRegistration<ServiceConfigurator> serviceRegistration =
			_bundleContext.registerService(
				ServiceConfigurator.class, _serviceConfigurator, properties);

		_serviceConfiguratorRegistrations.put(
			servletContextName, serviceRegistration);
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		_log = new Logger(bundleContext);

		List<Release> releases = _getReleases();

		for (Release release : releases) {
			signalRelease(release);
		}
	}

	@Deactivate
	protected void deactivate() {
		for (
			ServiceRegistration<ServiceConfigurator> serviceRegistration :
			_serviceConfiguratorRegistrations.values()) {

			serviceRegistration.unregister();
		}
	}

	@Reference
	protected void setReleaseLocalService(
		ReleaseLocalService releaseLocalService) {

		_releaseLocalService = releaseLocalService;
	}

	@Reference
	protected void setServiceConfigurator(
		ServiceConfigurator serviceConfigurator) {

		_serviceConfigurator = serviceConfigurator;
	}

	@Reference(target = "(original.bean=true)")
	protected void setServletContext(ServletContext servletContext) {
	}

	private List<Release> _getReleases() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<Release> releases = new ArrayList<>();

		try {
			con = DataAccess.getConnection();

			ps = con.prepareStatement(
				"select servletContextName, buildNumber from Release_");

			rs = ps.executeQuery();

			while (rs.next()) {
				String serveltContextName = rs.getString("servletContextName");
				String buildNumber = rs.getString("buildNumber");

				Release release = _releaseLocalService.createRelease(-1);

				release.setServletContextName(serveltContextName);
				release.setBuildNumber(buildNumber);
			}
		}
		catch (Exception e) {
			_log.log(
				Logger.LOG_ERROR,
				"Unexpected error retrieving the current list of releases", e);
		}
		finally {
			DataAccess.cleanUp(con, ps, rs);
		}

		return releases;
	}

	private BundleContext _bundleContext;
	private Logger _log;
	private ReleaseLocalService _releaseLocalService;
	private ServiceConfigurator _serviceConfigurator;
	private final Map<String, ServiceRegistration<ServiceConfigurator>>
		_serviceConfiguratorRegistrations = new HashMap<>();

}