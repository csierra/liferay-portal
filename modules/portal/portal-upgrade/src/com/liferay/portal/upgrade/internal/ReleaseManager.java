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

package com.liferay.portal.upgrade.internal;

import com.liferay.osgi.service.tracker.map.*;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.service.ReleaseLocalService;


import com.liferay.portal.upgrade.constants.UpgradeConstants;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.*;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import java.util.Collections;
import java.util.List;

/**
 * @author Miguel Pastor
 */
@Component(
	immediate = true,
	property = {
		"osgi.command.function=execute",
		"osgi.command.function=list",
		"osgi.command.scope=upgrade"
	},
	service = Object.class)
public class ReleaseManager {

	public void execute(String componentName) throws PortalException {
		List<UpgradeProcessInfo> upgradeProcesses = _serviceTrackerMap.getService(componentName);

		for (UpgradeProcessInfo upgradeProcessInfo : upgradeProcesses) {

			upgradeProcessInfo._UpgradeProcess.upgrade();

			_releaseLocalService.updateRelease(
				componentName,
				Collections.<UpgradeProcess>emptyList(),
				upgradeProcessInfo.to(), upgradeProcessInfo.from(), false);
		}

	}

	public void list() {
		for (String key : _serviceTrackerMap.keySet()) {
			list(key);
		}
	}

	public void list(String componentName) {
		List<UpgradeProcessInfo> upgradeProcesses = _serviceTrackerMap.getService(componentName);

		System.out.println("Registered upgrade commands for component " + componentName);

		for (UpgradeProcessInfo upgradeProcess : upgradeProcesses) {
			System.out.println(upgradeProcess);
		}
	}

	@Activate
	protected void activate(final BundleContext bundleContext)
		throws InvalidSyntaxException {

		_serviceTrackerMap = ServiceTrackerMapFactory.multiValueMap(
			bundleContext, UpgradeProcess.class, "(" + UpgradeConstants.APPLICATION_NAME + "=*)",
			new PropertyServiceReferenceMapper<String, UpgradeProcess>(UpgradeConstants.APPLICATION_NAME),
			new ServiceTrackerCustomizer<UpgradeProcess, UpgradeProcessInfo>() {

				@Override
				public UpgradeProcessInfo addingService(ServiceReference<UpgradeProcess> serviceReference) {
					String from = (String) serviceReference.getProperty(UpgradeConstants.FROM);
					String to = (String) serviceReference.getProperty(UpgradeConstants.TO);

					UpgradeProcess service = bundleContext.getService(serviceReference);

					return new UpgradeProcessInfo(from, to, service);
				}

				@Override
				public void modifiedService(ServiceReference<UpgradeProcess> serviceReference, UpgradeProcessInfo upgradeProcessInfo) {

				}

				@Override
				public void removedService(ServiceReference<UpgradeProcess> serviceReference, UpgradeProcessInfo upgradeProcessInfo) {
					bundleContext.ungetService(serviceReference);
				}
			},
			Collections.reverseOrder(
				new PropertyServiceReferenceComparator<UpgradeProcess>(
					UpgradeConstants.FROM)));

		_serviceTrackerMap.open();
	}

	@Deactivate
	protected void deactivate() {
		_serviceTrackerMap.close();
	}

	@Reference
	protected void setReleaseLocalService(
		ReleaseLocalService releaseLocalService) {

		_releaseLocalService = releaseLocalService;
	}

	ServiceTrackerMap<String, List<UpgradeProcessInfo>> _serviceTrackerMap;

	private ReleaseLocalService _releaseLocalService;

	private static class UpgradeProcessInfo {
		public UpgradeProcessInfo(
			String from, String to, UpgradeProcess _UpgradeProcess) {

			this.from = from;
			this.to = to;
			this._UpgradeProcess = _UpgradeProcess;
		}

		public int to() {
			return transform(to);
		}

		public int from() {
			return transform(from);
		}

		protected int transform(String s) {
			return Integer.parseInt(s.replace(".", ""));
		}

		private String from;
		private String to;
		private UpgradeProcess _UpgradeProcess;
	}

}