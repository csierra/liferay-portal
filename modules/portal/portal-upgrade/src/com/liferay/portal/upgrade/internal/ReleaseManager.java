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

import com.liferay.osgi.service.tracker.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.map.ServiceTrackerMapFactory;
import com.liferay.portal.service.ReleaseLocalService;


import com.liferay.portal.upgrade.ModuleUpgradeManager;

import com.liferay.portal.upgrade.ModuleUpgradeManager.UpgradeRegistry;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.*;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Miguel Pastor
 */
@Component(
	immediate = true,
	property = {
		"osgi.command.function=list",
		"osgi.command.scope=upgrade"
	},
	service = Object.class)
public class ReleaseManager {

	public void list() {
		for (String key : _serviceTrackerMap.keySet()) {
			list(key);
		}
	}

	public void list(String componentName) {
		List<ModuleUpgradeManagerInfo> moduleUpgradeManagers =
			_serviceTrackerMap.getService(componentName);

		System.out.println("Registered upgrade commands for component " + componentName);

		for (ModuleUpgradeManagerInfo moduleUpgradeManagerInfo : moduleUpgradeManagers) {
			System.out.println(moduleUpgradeManagerInfo);
		}
	}

	@Activate
	protected void activate(final BundleContext bundleContext)
		throws InvalidSyntaxException {

		_serviceTrackerMap = ServiceTrackerMapFactory.singleValueMap(
			bundleContext, ModuleUpgradeManager.class, "component.name", new ServiceTrackerCustomizer<ModuleUpgradeManager, List<ModuleUpgradeManagerInfo>>() {
				@Override
				public List<ModuleUpgradeManagerInfo> addingService(ServiceReference<ModuleUpgradeManager> serviceReference) {
					final List<ModuleUpgradeManagerInfo> moduleUpgradeManagerInfos = new ArrayList<>();

					final ModuleUpgradeManager moduleUpgradeManager =
						bundleContext.getService(serviceReference);

					moduleUpgradeManager.register(new UpgradeRegistry() {
						@Override
						public void registerStep(String from, String to) {
							moduleUpgradeManagerInfos.add(
								new ModuleUpgradeManagerInfo(
									from, to, moduleUpgradeManager));
						}
					});

					return moduleUpgradeManagerInfos;
				}

				@Override
				public void modifiedService(
					ServiceReference<ModuleUpgradeManager> serviceReference,
					List<ModuleUpgradeManagerInfo> moduleUpgradeManagerInfos) {
				}

				@Override
				public void removedService(
					ServiceReference<ModuleUpgradeManager> serviceReference,
					List<ModuleUpgradeManagerInfo> moduleUpgradeManagerInfos) {

					bundleContext.ungetService(serviceReference);
				}
			});

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

	ServiceTrackerMap<String, List<ModuleUpgradeManagerInfo>> _serviceTrackerMap;

	private ReleaseLocalService _releaseLocalService;

	private static class ModuleUpgradeManagerInfo {
		public ModuleUpgradeManagerInfo(String from, String to, ModuleUpgradeManager _moduleUpgradeManager) {
			this.from = from;
			this.to = to;
			this._moduleUpgradeManager = _moduleUpgradeManager;
		}

		private String from;
		private String to;
		private ModuleUpgradeManager _moduleUpgradeManager;
	}

}