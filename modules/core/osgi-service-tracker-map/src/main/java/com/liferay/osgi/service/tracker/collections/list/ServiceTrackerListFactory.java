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
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Adolfo Pérez
 */
public class ServiceTrackerListFactory {

	public static <T> ServiceTrackerList<T> create(
			BundleContext bundleContext, Class<T> clazz)
		throws InvalidSyntaxException {

		return new ServiceTrackerListImpl<>(
			bundleContext, clazz, null, null, null);
	}

	public static <T> ServiceTrackerList<T> create(
			BundleContext bundleContext, Class<T> clazz,
			Comparator<ServiceTrackerList.Entry<T>> comparator)
		throws InvalidSyntaxException {

		return new ServiceTrackerListImpl<>(
			bundleContext, clazz, null, null, comparator);
	}

	public static <T> ServiceTrackerList<T> create(
			BundleContext bundleContext, Class<T> clazz, String filterString)
		throws InvalidSyntaxException {

		return new ServiceTrackerListImpl<>(
			bundleContext, clazz, filterString, null, null);
	}

	public static <T> ServiceTrackerList<T> create(
			BundleContext bundleContext, Class<T> clazz, String filterString,
			Comparator<ServiceTrackerList.Entry<T>> comparator)
		throws InvalidSyntaxException {

		return new ServiceTrackerListImpl<>(
			bundleContext, clazz, filterString, null, comparator);
	}

	public static <T> ServiceTrackerList<T> create(
			BundleContext bundleContext, Class<T> clazz, String filterString,
			ServiceTrackerCustomizer<T, T> serviceTrackerCustomizer)
		throws InvalidSyntaxException {

		return new ServiceTrackerListImpl<>(
			bundleContext, clazz, filterString, serviceTrackerCustomizer, null);
	}

	public static <T> ServiceTrackerList<T> create(
			BundleContext bundleContext, Class<T> clazz, String filterString,
			ServiceTrackerCustomizer<T, T> serviceTrackerCustomizer,
			Comparator<ServiceTrackerList.Entry<T>> comparator)
		throws InvalidSyntaxException {

		return new ServiceTrackerListImpl<>(
			bundleContext, clazz, filterString, serviceTrackerCustomizer,
			comparator);
	}

}