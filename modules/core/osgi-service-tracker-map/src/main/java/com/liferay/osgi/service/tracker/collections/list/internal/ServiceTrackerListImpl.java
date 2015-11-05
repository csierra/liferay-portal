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

package com.liferay.osgi.service.tracker.collections.list.internal;

import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerList;

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
			Comparator<ServiceReference<T>> comparator)
		throws InvalidSyntaxException {

		_bundleContext = bundleContext;
		_serviceTrackerCustomizer = serviceTrackerCustomizer;

		if (comparator == null) {
			_comparator = new ServiceReferenceNaturalOrderComparator<>();
		}
		else {
			_comparator = new ServiceReferenceServiceWrapperComparator<>(
				comparator);
		}

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
	public void close() {
		_serviceTracker.close();
	}

	@Override
	public Iterator<T> iterator() {
		return new ServiceTrackerListIterator<>(_services.iterator());
	}

	@Override
	public void open() {
		_serviceTracker.open();
	}

	@Override
	public int size() {
		return _services.size();
	}

	private final BundleContext _bundleContext;
	private final Comparator<ServiceWrapper<T>> _comparator;
	private final List<ServiceWrapper<T>> _services =
		new CopyOnWriteArrayList<>();
	private final ServiceTracker<T, T> _serviceTracker;
	private final ServiceTrackerCustomizer<T, T> _serviceTrackerCustomizer;

	private static class ServiceReferenceNaturalOrderComparator<T>
		implements Comparator<ServiceWrapper<T>> {

		@Override
		public int compare(
			ServiceWrapper<T> serviceWrapper1,
			ServiceWrapper<T> serviceWrapper2) {

			ServiceReference<T> serviceReference1 =
				serviceWrapper1.getServiceReference();
			ServiceReference<T> serviceReference2 =
				serviceWrapper2.getServiceReference();

			return serviceReference1.compareTo(serviceReference2);
		}

	}

	private static class ServiceTrackerListIterator<T> implements Iterator<T> {

		public ServiceTrackerListIterator(
			Iterator<ServiceWrapper<T>> iterator) {

			_iterator = iterator;
		}

		@Override
		public boolean hasNext() {
			return _iterator.hasNext();
		}

		@Override
		public T next() {
			ServiceWrapper<T> serviceWrapper = _iterator.next();

			return serviceWrapper.getService();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		private final Iterator<ServiceWrapper<T>> _iterator;

	}

	private static class ServiceWrapper<T> {

		public ServiceWrapper(ServiceReference<T> serviceReference, T service) {
			_serviceReference = serviceReference;
			_service = service;
		}

		public T getService() {
			return _service;
		}

		public ServiceReference<T> getServiceReference() {
			return _serviceReference;
		}

		private final T _service;
		private final ServiceReference<T> _serviceReference;

	}

	private class ServiceReferenceServiceTrackerCustomizer
		implements ServiceTrackerCustomizer<T, T> {

		@Override
		public T addingService(ServiceReference<T> serviceReference) {
			return update(
				serviceReference, getService(serviceReference), false);
		}

		@Override
		public void modifiedService(
			ServiceReference<T> serviceReference, T service) {

			if (_serviceTrackerCustomizer != null) {
				_serviceTrackerCustomizer.modifiedService(
					serviceReference, service);
			}

			update(serviceReference, service, false);
		}

		@Override
		public void removedService(
			ServiceReference<T> serviceReference, T service) {

			if (_serviceTrackerCustomizer != null) {
				_serviceTrackerCustomizer.removedService(
					serviceReference, service);
			}

			update(serviceReference, service, true);

			_bundleContext.ungetService(serviceReference);
		}

		protected T getService(ServiceReference<T> serviceReference) {
			if (_serviceTrackerCustomizer == null) {
				return _bundleContext.getService(serviceReference);
			}

			return _serviceTrackerCustomizer.addingService(serviceReference);
		}

		private T update(
			ServiceReference<T> serviceReference, T service, boolean remove) {

			if (service == null) {
				return service;
			}

			ServiceWrapper<T> serviceWrapper = new ServiceWrapper<>(
				serviceReference, service);

			synchronized(_services) {
				int index = Collections.binarySearch(
					_services, serviceWrapper, _comparator);

				if (remove) {
					if (index >= 0) {
						_services.remove(index);
					}
				}
				else if (index < 0) {
					_services.add(((-index) - 1), serviceWrapper);
				}
			}

			return service;
		}

	}

	private class ServiceReferenceServiceWrapperComparator<T>
		implements Comparator<ServiceWrapper<T>> {

		public ServiceReferenceServiceWrapperComparator(
			Comparator<ServiceReference<T>> comparator) {

			_comparator = comparator;
		}

		@Override
		public int compare(
			ServiceWrapper<T> serviceWrapper1,
			ServiceWrapper<T> serviceWrapper2) {

			return _comparator.compare(
				serviceWrapper1.getServiceReference(),
				serviceWrapper2.getServiceReference());
		}

		private final Comparator<ServiceReference<T>> _comparator;

	}

}