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

package com.liferay.osgi.service.tracker.collections.list;

import com.liferay.osgi.service.tracker.collections.list.internal.ServiceTrackerListImpl;

import java.util.Comparator;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Adolfo Pérez
 */
public class ServiceTrackerListFactory {

	public static <S, T> ServiceTrackerList<S, T> create(
			BundleContext bundleContext, Class<S> clazz, String filterString,
			ServiceTrackerCustomizer<S, T> serviceTrackerCustomizer)
		throws InvalidSyntaxException {

		return new ServiceTrackerListImpl<>(
			bundleContext, clazz, filterString, serviceTrackerCustomizer, null);
	}

	public static <S, T> ServiceTrackerList<S, T> create(
			BundleContext bundleContext, Class<S> clazz, String filterString,
			ServiceTrackerCustomizer<S, T> serviceTrackerCustomizer,
			Comparator<ServiceReference<S>> comparator)
		throws InvalidSyntaxException {

		return new ServiceTrackerListImpl<>(
			bundleContext, clazz, filterString, serviceTrackerCustomizer,
			comparator);
	}

	public static <T> ServiceTrackerList<T, T> create(
			BundleContext bundleContext, Class<T> clazz)
		throws InvalidSyntaxException {

		ServiceTrackerCustomizer<T, T> serviceTrackerCustomizer =
			new IdenticalServiceTrackerCustomizer<>(bundleContext);

		return new ServiceTrackerListImpl<>(
			bundleContext, clazz, null, serviceTrackerCustomizer, null);
	}

	public static <T> ServiceTrackerList<T, T> create(
			BundleContext bundleContext, Class<T> clazz,
			Comparator<ServiceReference<T>> comparator)
		throws InvalidSyntaxException {

		ServiceTrackerCustomizer<T, T> serviceTrackerCustomizer =
			new IdenticalServiceTrackerCustomizer<>(bundleContext);

		return new ServiceTrackerListImpl<>(
			bundleContext, clazz, null, serviceTrackerCustomizer, comparator);
	}

	public static <T> ServiceTrackerList<T, T> create(
			BundleContext bundleContext, Class<T> clazz, String filterString)
		throws InvalidSyntaxException {

		ServiceTrackerCustomizer<T, T> serviceTrackerCustomizer =
			new IdenticalServiceTrackerCustomizer<>(bundleContext);

		return new ServiceTrackerListImpl<>(
			bundleContext, clazz, filterString, serviceTrackerCustomizer, null);
	}

	public static <T> ServiceTrackerList<T, T> create(
			BundleContext bundleContext, Class<T> clazz, String filterString,
			Comparator<ServiceReference<T>> comparator)
		throws InvalidSyntaxException {

		ServiceTrackerCustomizer<T, T> serviceTrackerCustomizer =
			new IdenticalServiceTrackerCustomizer<>(bundleContext);

		return new ServiceTrackerListImpl<>(
			bundleContext, clazz, filterString, serviceTrackerCustomizer,
			comparator);
	}

	private static class IdenticalServiceTrackerCustomizer<T>
		implements ServiceTrackerCustomizer<T, T> {

		public IdenticalServiceTrackerCustomizer(BundleContext bundleContext) {
			_bundleContext = bundleContext;
		}

		@Override
		public T addingService(ServiceReference<T> serviceReference) {
			return _bundleContext.getService(serviceReference);
		}

		@Override
		public void modifiedService(
			ServiceReference<T> serviceReference, T service) {
		}

		@Override
		public void removedService(
			ServiceReference<T> serviceReference, T service) {
		}

		private final BundleContext _bundleContext;

	}

}