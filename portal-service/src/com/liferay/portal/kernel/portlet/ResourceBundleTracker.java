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

package com.liferay.portal.kernel.portlet;

import com.liferay.portal.kernel.util.AggregateResourceBundle;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.registry.collections.ServiceReferenceMapperFactory;
import com.liferay.registry.collections.ServiceTrackerCollections;
import com.liferay.registry.collections.ServiceTrackerMap;
import com.liferay.registry.collections.ServiceTrackerMapListener;

import java.io.Closeable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Raymond Augé
 * @author Tomas Polesovsky
 */
public class ResourceBundleTracker implements Closeable {

	public ResourceBundleTracker(String portletId) {
		_serviceTrackerMap = ServiceTrackerCollections.multiValueMap(
			ResourceBundle.class,
			"(&(javax.portlet.name=" + portletId + ")(language.id=*))",
			ServiceReferenceMapperFactory.<String, Object>create("language.id"),
			new ResourceBundleTrackerServiceTrackerMapListener(
				_resourceBundles));

		_serviceTrackerMap.open();
	}

	@Override
	public void close() {
		_serviceTrackerMap.close();
	}

	public ResourceBundle getResourceBundle(String languageId) {
		return _resourceBundles.get(languageId);
	}

	private final Map<String, ResourceBundle> _resourceBundles =
		new HashMap<>();
	private final ServiceTrackerMap<String, List<ResourceBundle>>
		_serviceTrackerMap;

	private static class ResourceBundleTrackerServiceTrackerMapListener
		implements ServiceTrackerMapListener
			<String, ResourceBundle, List<ResourceBundle>> {

		public ResourceBundleTrackerServiceTrackerMapListener(
			Map<String, ResourceBundle> resourceBundles) {

			_resourceBundles = resourceBundles;
		}

		@Override
		public void keyEmitted(
			ServiceTrackerMap<String, List<ResourceBundle>>
				serviceTrackerMap,
			String languageId, ResourceBundle resourceBundle,
			List<ResourceBundle> content) {

			rebuildLanguageIds(serviceTrackerMap, languageId);
		}

		@Override
		public void keyRemoved(
			ServiceTrackerMap<String, List<ResourceBundle>>
				serviceTrackerMap,
			String languageId, ResourceBundle service,
			List<ResourceBundle> content) {

			rebuildLanguageIds(serviceTrackerMap, languageId);
		}

		private AggregateResourceBundle rebuildLanguageId(
			String languageId, ServiceTrackerMap<String, List<ResourceBundle>>
				serviceTrackerMap) {

			List<ResourceBundle> resourceBundles = new ArrayList<>();

			while (Validator.isNotNull(languageId)) {
				List<ResourceBundle> service = serviceTrackerMap.getService(
					languageId);

				if (service != null) {
					resourceBundles.addAll(service);
				}

				int indexOfUnderline = languageId.lastIndexOf(
					StringPool.UNDERLINE);

				if (indexOfUnderline > 0) {
					languageId = languageId.substring(0, indexOfUnderline);
				}
				else {
					break;
				}
			}

			List<ResourceBundle> defaultResourceBundle =
				serviceTrackerMap.getService("");

			if (defaultResourceBundle != null) {
				resourceBundles.addAll(defaultResourceBundle);
			}

			return new AggregateResourceBundle(
				resourceBundles.toArray(
					new ResourceBundle[resourceBundles.size()]));
		}

		private void rebuildLanguageIds(
			ServiceTrackerMap<String, List<ResourceBundle>> serviceTrackerMap,
			String languageId) {

			synchronized (_resourceBundles) {
				_resourceBundles.put(
					languageId,
					rebuildLanguageId(languageId, serviceTrackerMap));

				List<String> languageIds = new ArrayList<>(
					serviceTrackerMap.keySet());

				Collections.sort(languageIds);

				for (String subLanguageId : languageIds) {
					if (subLanguageId.startsWith(languageId)) {
						_resourceBundles.put(
							languageId,
							rebuildLanguageId(
								subLanguageId, serviceTrackerMap));
					}
				}
			}
		}

		private final Map<String, ResourceBundle> _resourceBundles;

	}

}