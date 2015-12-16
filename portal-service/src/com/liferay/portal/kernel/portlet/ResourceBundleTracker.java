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
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil.ResourceBundleLoader;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.PortalUtil;
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

		private void rebuildLanguageId(
			String languageId,
			final ServiceTrackerMap<String, List<ResourceBundle>>
				serviceTrackerMap) {

			ResourceBundleUtil.loadResourceBundles(_resourceBundles,
				LocaleUtil.fromLanguageId(languageId),
				new ResourceBundleLoader() {
					@Override
					public ResourceBundle loadResourceBundle(
							String languageId) {

						List<ResourceBundle> resourceBundles =
								serviceTrackerMap.getService(languageId);

						if (resourceBundles == null) {
							return null;
						}

						return new AggregateResourceBundle(
							resourceBundles.toArray(new ResourceBundle[0]));
					}
				});
		}

		private void rebuildLanguageIds(
			final ServiceTrackerMap<String, List<ResourceBundle>>
				serviceTrackerMap,
			String languageId) {

			synchronized (_resourceBundles) {
				rebuildLanguageId(languageId, serviceTrackerMap);

				List<String> languageIds = new ArrayList<>(
					serviceTrackerMap.keySet());

				Collections.sort(languageIds);

				for (String subLanguageId : languageIds) {
					if (subLanguageId.startsWith(languageId + "_")) {
						rebuildLanguageId(subLanguageId, serviceTrackerMap);
					}
				}
			}
		}

		private final Map<String, ResourceBundle> _resourceBundles;

	}

}