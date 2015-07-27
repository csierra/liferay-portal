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

package com.liferay.portal.upgrade.internal.release;

import com.liferay.osgi.service.tracker.map.PropertyServiceReferenceComparator;
import com.liferay.osgi.service.tracker.map.PropertyServiceReferenceMapper;
import com.liferay.osgi.service.tracker.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.map.ServiceTrackerMapFactory;
import com.liferay.portal.DatabaseContext;
import com.liferay.portal.DatabaseProcessContext;
import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.StreamUtil;
import com.liferay.portal.model.Release;
import com.liferay.portal.service.ReleaseLocalService;
import com.liferay.portal.upgrade.api.OutputStreamProvider;
import com.liferay.portal.upgrade.api.OutputStreamProviderTracker;
import com.liferay.portal.Upgrade;
import com.liferay.portal.upgrade.constants.UpgradeWhiteboardConstants;
import com.liferay.portal.upgrade.internal.UpgradeInfo;
import com.liferay.portal.upgrade.internal.bundle.ServiceConfiguratorRegistrator;
import com.liferay.portal.upgrade.internal.graph.ReleaseGraphManager;

import java.io.IOException;
import java.io.OutputStream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.Collections;
import java.util.List;

import org.apache.felix.utils.log.Logger;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Miguel Pastor
 * @author Carlos Sierra Andrés
 */
@Component(
	immediate = true,
	property = {
		"osgi.command.function=execute", "osgi.command.function=list",
		"osgi.command.scope=upgrade"
	},
	service = Object.class
)
public class ReleaseManager {

	public void execute(String componentName) throws PortalException {
		List<UpgradeInfo> upgradeInfos = _serviceTrackerMap.getService(
			componentName);

		String buildNumber = _getBuildNumber(componentName);

		ReleaseGraphManager releaseGraphManager = new ReleaseGraphManager(
			upgradeInfos);

		List<UpgradeInfo> upgradePath = releaseGraphManager.getUpgradePath(
			buildNumber);

		executeUpgradePath(componentName, upgradePath);

		UpgradeInfo upgradeInfo = upgradePath.get(upgradePath.size() - 1);
	}

	public void execute(String componentName, String to)
		throws PortalException {

		List<UpgradeInfo> upgradeInfos = _serviceTrackerMap.getService(
			componentName);

		String buildNumber = _getBuildNumber(componentName);

		ReleaseGraphManager releaseGraphManager = new ReleaseGraphManager(
			upgradeInfos);

		List<UpgradeInfo> upgradePath = releaseGraphManager.getUpgradePath(
			buildNumber, to);

		executeUpgradePath(componentName, upgradePath);
	}

	public void list() {
		for (String key : _serviceTrackerMap.keySet()) {
			list(key);
		}
	}

	public void list(String componentName) {
		List<UpgradeInfo> upgradeProcesses = _serviceTrackerMap.getService(
			componentName);

		System.out.println(
			"Registered upgrade commands for component " + componentName);

		for (UpgradeInfo upgradeProcess : upgradeProcesses) {
			System.out.println(upgradeProcess);
		}
	}

	@Reference
	public void setOutputStreamTracker(
		OutputStreamProviderTracker outputStreamProviderTracker) {

		_outputStreamProviderTracker = outputStreamProviderTracker;
	}

	@Activate
	protected void activate(final BundleContext bundleContext)
		throws InvalidSyntaxException {

		_log = new Logger(bundleContext);

		_serviceTrackerMap = ServiceTrackerMapFactory.multiValueMap(
			bundleContext, Upgrade.class,
				"(&(" + UpgradeWhiteboardConstants.APPLICATION_NAME +
					"=*)(|(database=" +
						UpgradeWhiteboardConstants.ALL_DATABASES +
							")(database=" + DBFactoryUtil.getDB().getType() +
								")))",
			new PropertyServiceReferenceMapper<String, Upgrade>(
				UpgradeWhiteboardConstants.APPLICATION_NAME),
			new UpgradeCustomizer(bundleContext),
			Collections.reverseOrder(
				new PropertyServiceReferenceComparator<Upgrade>(
					UpgradeWhiteboardConstants.FROM)));

		_serviceTrackerMap.open();
	}

	protected List<UpgradeInfo> buildUpgradePath(
		List<UpgradeInfo> upgradeProcessesInfo, String from, String to) {

		ReleaseGraphManager releaseGraphManager = new ReleaseGraphManager(
			upgradeProcessesInfo);

		return releaseGraphManager.getUpgradePath(from, to);
	}

	@Deactivate
	protected void deactivate() {
		_serviceTrackerMap.close();
	}

	protected void executeUpgradePath(
		final String componentName, final List<UpgradeInfo> upgradeInfos) {

		OutputStreamProvider outputStreamProvider =
			_outputStreamProviderTracker.getDefaultOutputStreamProvider();

		OutputStreamProvider.OutputStreamInformation outputStreamInformation =
			outputStreamProvider.create("upgrade-" + componentName);

		final OutputStream outputStream =
			outputStreamInformation.getOutputStream();

		StreamUtil.withStdOut(outputStream, new Runnable() {
			@Override
			public void run() {
				for (UpgradeInfo upgradeInfo : upgradeInfos) {
					Upgrade upgrade = upgradeInfo.getUpgrade();

					try {
						upgrade.upgrade(new DatabaseProcessContext() {

							@Override
							public DatabaseContext getDatabaseContext() {
								return new DatabaseContext();
							}

							@Override
							public OutputStream getOutputStream() {
								return outputStream;
							}
						});

						_releaseLocalService.updateRelease(
							componentName, upgradeInfo.getTo(),
							upgradeInfo.getFrom());
					}
					catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
		});

		try {
			outputStream.close();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}

		Release release = _releaseLocalService.fetchRelease(componentName);

		if (release != null) {
			_serviceConfiguratorRegistrator.signalRelease(release);
		}
	}

	@Reference
	protected void setReleaseLocalService(
		ReleaseLocalService releaseLocalService) {

		_releaseLocalService = releaseLocalService;
	}

	private String _getBuildNumber(String servletContextName) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DataAccess.getConnection();

			ps = con.prepareStatement(
				"select buildNumber from Release_ where " +
					"servletContextName = ?");

			ps.setString(1,servletContextName);

			rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getString("buildNumber");
			}

		} catch (Exception e) {
			_log.log(
				Logger.LOG_ERROR,
				"Unexpected error retrieving the current build number of " +
					servletContextName, e);
		}
		finally {
			DataAccess.cleanUp(con, ps, rs);
		}

		return "";
	}

	private static Logger _log;

	private OutputStreamProviderTracker _outputStreamProviderTracker;
	private ReleaseLocalService _releaseLocalService;
	private ServiceConfiguratorRegistrator _serviceConfiguratorRegistrator;
	private ServiceTrackerMap<String, List<UpgradeInfo>> _serviceTrackerMap;

	private static class UpgradeCustomizer
			implements ServiceTrackerCustomizer<Upgrade, UpgradeInfo> {

		public UpgradeCustomizer(BundleContext bundleContext) {
			_bundleContext = bundleContext;
		}

		@Override
		public UpgradeInfo addingService(
			ServiceReference<Upgrade> serviceReference) {

			String from = (String) serviceReference.getProperty(
				UpgradeWhiteboardConstants.FROM);
			String to = (String) serviceReference.getProperty(
				UpgradeWhiteboardConstants.TO);

			Upgrade upgradeProcess = _bundleContext.getService(
				serviceReference);

			if (upgradeProcess == null) {
				_log.log(Logger.LOG_WARNING,
					"Service " + serviceReference + " is registered as " +
						"an upgrade but it is not implementing Upgrade " +
						"interface. Not tracking.");

				return null;
			}

			return new UpgradeInfo(from, to, upgradeProcess);
		}

		@Override
		public void modifiedService(
			ServiceReference<Upgrade> serviceReference,
			UpgradeInfo upgradeInfo) {
		}

		@Override
		public void removedService(
			ServiceReference<Upgrade> serviceReference,
			UpgradeInfo upgradeInfo) {

			_bundleContext.ungetService(serviceReference);
		}

		private final BundleContext _bundleContext;

	}

}