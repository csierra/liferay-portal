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

package com.liferay.osgi.service.tracker.list.internal;

import com.liferay.osgi.service.tracker.list.ServiceTrackerList;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Adolfo Pérez
 */
public class ServiceTrackerListImpl<T> implements ServiceTrackerList<T> {

	public ServiceTrackerListImpl(
			BundleContext bundleContext, Class<T> clazz, String filterString,
			ServiceTrackerCustomizer<T, T> serviceTrackerCustomizer,
			Comparator<Entry<T>> comparator)
		throws InvalidSyntaxException {

		_bundleContext = bundleContext;
		_serviceTrackerCustomizer = serviceTrackerCustomizer;
		_comparator = comparator;

		if (filterString != null) {
			Filter filter = bundleContext.createFilter(
				"(&(objectClass=" + clazz.getName() + ")" + filterString + ")");

			_serviceTracker = new ServiceTracker<>(
				bundleContext, filter,
				new ServiceReferenceServiceTrackerCustomizer());
		}
		else {
			_serviceTracker = new ServiceTracker<>(
				bundleContext, clazz,
				new ServiceReferenceServiceTrackerCustomizer());
		}
	}

	@Override
	public Iterator<T> iterator() {
		throw new UnsupportedOperationException();
	}

	private final BundleContext _bundleContext;
	private final Comparator<Entry<T>> _comparator;
	private final List<Entry<T>> _services = new CopyOnWriteArrayList<>();
	private final ServiceTracker<T, T> _serviceTracker;
	private final ServiceTrackerCustomizer<T, T> _serviceTrackerCustomizer;

	private class ServiceReferenceServiceTrackerCustomizer
		implements ServiceTrackerCustomizer<T, T> {

		@Override
		public T addingService(ServiceReference<T> serviceReference) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void modifiedService(ServiceReference<T> serviceReference, T t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void removedService(ServiceReference<T> serviceReference, T t) {
			throw new UnsupportedOperationException();
		}

		private final BundleContext _bundleContext;
		private final ServiceTrackerCustomizer<T, T> _serviceTrackerCustomizer;

	}

}