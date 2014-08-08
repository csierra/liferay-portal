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
package com.liferay.portal.kernel.servlet.taglib;

import com.liferay.registry.Filter;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceTracker;
import com.liferay.registry.ServiceTrackerCustomizer;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Raymond Augé
 */
public class ViewExtensionUtil {

	public static List<ViewExtension> getExtensions(String extensionId) {
		return _instance._extensions.get(extensionId);
	}

	private ViewExtensionUtil() {
		Registry registry = RegistryUtil.getRegistry();

		Filter filter = registry.getFilter(
			"(&(extension.id=*)(objectClass=" + ViewExtension.class.getName() +
				"))");

		_serviceTracker = registry.trackServices(
			filter, new ViewExtensionServiceTrackerCustimozer());

		_serviceTracker.open();
	}

	private static ViewExtensionUtil _instance = new ViewExtensionUtil();

	private ConcurrentMap<String, List<ViewExtension>> _extensions =
		new ConcurrentHashMap<String, List<ViewExtension>>();
	private ServiceTracker<ViewExtension, ViewExtension> _serviceTracker;

	private class ViewExtensionServiceTrackerCustimozer
		implements ServiceTrackerCustomizer<ViewExtension, ViewExtension> {

		@Override
		public ViewExtension addingService(
			ServiceReference<ViewExtension> serviceReference) {

			Registry registry = RegistryUtil.getRegistry();

			ViewExtension viewExtension = registry.getService(serviceReference);

			String extensionId = (String)serviceReference.getProperty(
				"extension.id");

			List<ViewExtension> extensions = _extensions.get(extensionId);

			if (extensions == null) {
				extensions = new CopyOnWriteArrayList<ViewExtension>();

				List<ViewExtension> previousList = _extensions.putIfAbsent(
					extensionId, extensions);

				if (previousList != null) {
					extensions = previousList;
				}
			}

			extensions.add(viewExtension);

			return viewExtension;
		}

		@Override
		public void modifiedService(
			ServiceReference<ViewExtension> serviceReference,
			ViewExtension viewExtension) {
		}

		@Override
		public void removedService(
			ServiceReference<ViewExtension> serviceReference,
			ViewExtension viewExtension) {

			Registry registry = RegistryUtil.getRegistry();

			registry.ungetService(serviceReference);

			String extensionId = (String)serviceReference.getProperty(
				"extension.id");

			List<ViewExtension> extensions = _extensions.get(extensionId);

			if (extensions != null) {
				extensions.remove(viewExtension);
			}
		}

	}

}