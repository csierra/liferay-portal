/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.oauth2.provider.scope.impl.scopedservicetrackermap;

import com.liferay.oauth2.provider.scope.liferay.ScopedServiceTrackerMap;
import com.liferay.oauth2.provider.scope.liferay.ScopedServiceTrackerMapFactory;
import com.liferay.osgi.service.tracker.collections.map.PropertyServiceReferenceMapper;
import com.liferay.osgi.service.tracker.collections.map.ServiceReferenceMapper;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapListener;

import java.util.List;
import java.util.function.Supplier;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Component;

@Component
public class ScopedServiceTrackerMapFactoryImpl
	implements ScopedServiceTrackerMapFactory {

	@Override
	public <T> ScopedServiceTrackerMap<T> create(
		BundleContext bundleContext, Class<T> clazz, String property,
		Supplier<T> defaultServiceSupplier, Runnable onChange) {

		return new ScopedServiceTrackerMapImpl<>(
			bundleContext, clazz, property, defaultServiceSupplier, onChange);
	}

	private static class ScopedServiceTrackerMapImpl<T>
		implements ScopedServiceTrackerMap<T> {

		public ScopedServiceTrackerMapImpl(
			BundleContext bundleContext, Class<T> clazz, String property,
			Supplier<T> defaultServiceSupplier, Runnable onChange) {

			_defaultServiceSupplier = defaultServiceSupplier;
			_onChange = onChange;

			_servicesByCompany = ServiceTrackerMapFactory.openMultiValueMap(
				bundleContext, clazz, "(&(companyId=*)(!(" + property + "=*)))",
				new PropertyServiceReferenceMapper<>("companyId"),
				new ServiceTrackerMapListenerImpl<>());

			_servicesByKey = ServiceTrackerMapFactory.openMultiValueMap(
				bundleContext, clazz,
				"(&(" + property + "=*)(|(!(companyId=*))(companyId=0)))",
				new PropertyServiceReferenceMapper<>(property),
				new ServiceTrackerMapListenerImpl<>());

			_servicesByCompanyAndKey =
				ServiceTrackerMapFactory.openMultiValueMap(
					bundleContext, clazz,
					"(&(companyId=*)(" + property + "=*))",
					(serviceReference, emitter) -> {
						ServiceReferenceMapper<String, T> companyMapper =
								new PropertyServiceReferenceMapper<>(
									"companyId");
						ServiceReferenceMapper<String, T> nameMapper =
							new PropertyServiceReferenceMapper<>(property);

						companyMapper.map(
							serviceReference,
							key1 -> nameMapper.map(
								serviceReference,
								key2 -> emitter.emit(
									String.join("-", key1, key2))));
					},
					new ServiceTrackerMapListenerImpl<>());
		}

		@Override
		public void close() {
			_servicesByCompany.close();
			_servicesByCompanyAndKey.close();
			_servicesByKey.close();
		}

		@Override
		public T getService(long companyId, String key) {
			String companyIdString = Long.toString(companyId);
			List<T> services = _servicesByCompanyAndKey.getService(
				String.join("-", companyIdString, key));

			if (services != null && !services.isEmpty()) {
				return services.get(0);
			}

			services = _servicesByKey.getService(key);

			if (services != null && !services.isEmpty()) {
				return services.get(0);
			}

			services = _servicesByCompany.getService(companyIdString);

			if (services != null && !services.isEmpty()) {
				return services.get(0);
			}

			return _defaultServiceSupplier.get();
		}

		private final Supplier<T> _defaultServiceSupplier;
		private final Runnable _onChange;
		private final ServiceTrackerMap<String, List<T>> _servicesByCompany;
		private final ServiceTrackerMap<String, List<T>>
			_servicesByCompanyAndKey;
		private final ServiceTrackerMap<String, List<T>> _servicesByKey;

		private class ServiceTrackerMapListenerImpl<T>
			implements ServiceTrackerMapListener<String, T, List<T>> {

			@Override
			public void keyEmitted(
				ServiceTrackerMap<String, List<T>> serviceTrackerMap,
				String key, T service, List<T> content) {

				_onChange.run();
			}

			@Override
			public void keyRemoved(
				ServiceTrackerMap<String, List<T>> serviceTrackerMap,
				String key, T service, List<T> content) {

				_onChange.run();
			}

		}

	}

}