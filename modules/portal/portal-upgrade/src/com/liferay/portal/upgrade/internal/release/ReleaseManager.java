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
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.model.Release;
import com.liferay.portal.service.ReleaseLocalService;
import com.liferay.portal.upgrade.constants.UpgradeWhiteboardConstants;
import com.liferay.portal.upgrade.internal.UpgradeProcessInfo;
import com.liferay.portal.upgrade.internal.graph.ReleaseGraphManager;

import java.util.Collections;
import java.util.List;

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
		List<UpgradeProcessInfo> upgradeProcessInfos =
			_serviceTrackerMap.getService(componentName);

		String buildNumber = getBuildNumber(componentName);

		ReleaseGraphManager releaseGraphManager = new ReleaseGraphManager(
			upgradeProcessInfos);

		List<UpgradeProcessInfo> upgradePath =
			releaseGraphManager.getUpgradePath(buildNumber);

		executeUpgradePath(componentName, upgradePath);
	}

	public void execute(String componentName, String to)
		throws PortalException {

		List<UpgradeProcessInfo> upgradeProcessInfos =
			_serviceTrackerMap.getService(componentName);

		String buildNumber = getBuildNumber(componentName);

		ReleaseGraphManager releaseGraphManager = new ReleaseGraphManager(
			upgradeProcessInfos);

		List<UpgradeProcessInfo> upgradePath =
			releaseGraphManager.getUpgradePath(buildNumber, to);

		executeUpgradePath(componentName, upgradePath);
	}

	public void list() {
		for (String key : _serviceTrackerMap.keySet()) {
			list(key);
		}
	}

	public void list(String componentName) {
		List<UpgradeProcessInfo> upgradeProcesses =
			_serviceTrackerMap.getService(componentName);

		System.out.println(
			"Registered upgrade commands for component " + componentName);

		for (UpgradeProcessInfo upgradeProcess : upgradeProcesses) {
			System.out.println(upgradeProcess);
		}
	}

	@Activate
	protected void activate(final BundleContext bundleContext)
		throws InvalidSyntaxException {

		_serviceTrackerMap = ServiceTrackerMapFactory.multiValueMap(
			bundleContext, UpgradeProcess.class,
			"(" + UpgradeWhiteboardConstants.APPLICATION_NAME + "=*)",
			new PropertyServiceReferenceMapper<String, UpgradeProcess>(
				UpgradeWhiteboardConstants.APPLICATION_NAME),
			new UpgradeProcessCustomizer(bundleContext),
			Collections.reverseOrder(
				new PropertyServiceReferenceComparator<UpgradeProcess>(
					UpgradeWhiteboardConstants.FROM)));

		_serviceTrackerMap.open();
	}

	protected List<UpgradeProcessInfo> buildUpgradePath(
		List<UpgradeProcessInfo> upgradeProcessesInfo, String from, String to) {

		ReleaseGraphManager releaseGraphManager = new ReleaseGraphManager(
			upgradeProcessesInfo);

		return releaseGraphManager.getUpgradePath(from, to);
	}

	@Deactivate
	protected void deactivate() {
		_serviceTrackerMap.close();
	}

	protected void executeUpgradePath(
			String componentName, List<UpgradeProcessInfo> upgradeProcessInfos)
		throws PortalException {

		for (UpgradeProcessInfo upgradeProcessInfo : upgradeProcessInfos) {
			UpgradeProcess upgradeProcess =
				upgradeProcessInfo.getUpgradeProcess();

			upgradeProcess.upgrade();

			_releaseLocalService.updateRelease(
				componentName,
				upgradeProcessInfo.getTo(), upgradeProcessInfo.getFrom());
		}
	}

	protected String getBuildNumber(String componentName) {
		Release release = _releaseLocalService.fetchRelease(componentName);

		String buildNumber = "0.0.0";

		if (release != null) {
			buildNumber = release.getBuildNumber();
		}

		return buildNumber;
	}

	@Reference
	protected void setReleaseLocalService(
		ReleaseLocalService releaseLocalService) {

		_releaseLocalService = releaseLocalService;
	}

	private ReleaseLocalService _releaseLocalService;
	private ServiceTrackerMap<String, List<UpgradeProcessInfo>>
		_serviceTrackerMap;

	private static class UpgradeProcessCustomizer
		implements
			ServiceTrackerCustomizer<UpgradeProcess, UpgradeProcessInfo> {

		public UpgradeProcessCustomizer(BundleContext bundleContext) {
			_bundleContext = bundleContext;
		}

		@Override
		public UpgradeProcessInfo addingService(
			ServiceReference<UpgradeProcess> serviceReference) {

			String from = (String)serviceReference.getProperty(
				UpgradeWhiteboardConstants.FROM);
			String to = (String)serviceReference.getProperty(
				UpgradeWhiteboardConstants.TO);

			UpgradeProcess upgradeProcess = _bundleContext.getService(
				serviceReference);

			return new UpgradeProcessInfo(from, to, upgradeProcess);
		}

		@Override
		public void modifiedService(
			ServiceReference<UpgradeProcess> serviceReference,
			UpgradeProcessInfo upgradeProcessInfo) {
		}

		@Override
		public void removedService(
			ServiceReference<UpgradeProcess> serviceReference,
			UpgradeProcessInfo upgradeProcessInfo) {

			_bundleContext.ungetService(serviceReference);
		}

		private final BundleContext _bundleContext;

	}

}