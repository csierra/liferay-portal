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

package com.liferay.frontend.taglib.form.navigator.internal;

import com.liferay.frontend.taglib.form.navigator.FormNavigatorEntry;
import com.liferay.frontend.taglib.form.navigator.internal.servlet.taglib.ui.WrapperFormNavigatorEntry;
import com.liferay.portal.kernel.util.HashMapDictionary;

import java.util.Dictionary;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Eudaldo Alonso
 */
public class FormNavigatorEntryServiceTrackerCustomizer
	implements ServiceTrackerCustomizer
		<com.liferay.portal.kernel.servlet.taglib.ui.FormNavigatorEntry<?>,
		 FormNavigatorEntry<?>> {

	public FormNavigatorEntryServiceTrackerCustomizer(
		BundleContext bundleContext,
		Map
			<ServiceReference
				<com.liferay.portal.kernel.servlet.taglib.ui.FormNavigatorEntry
					<?>>,
			 ServiceRegistration<FormNavigatorEntry<?>>> serviceRegistrations) {

		_bundleContext = bundleContext;
		_serviceRegistrations = serviceRegistrations;
	}

	@Override
	public FormNavigatorEntry<?> addingService(
		ServiceReference
			<com.liferay.portal.kernel.servlet.taglib.ui.FormNavigatorEntry<?>>
				serviceReference) {

		FormNavigatorEntry<?> formNavigatorEntry =
			new WrapperFormNavigatorEntry(
				_bundleContext.getService(serviceReference));

		Dictionary<String, Object> properties = new HashMapDictionary<>();

		properties.put(
			"form.navigator.entry.order",
			serviceReference.getProperty("form.navigator.entry.order"));

		ServiceRegistration<FormNavigatorEntry<?>> serviceRegistration =
			_bundleContext.registerService(
				(Class<FormNavigatorEntry<?>>)
					(Class<?>)FormNavigatorEntry.class,
				formNavigatorEntry, properties);

		_serviceRegistrations.put(serviceReference, serviceRegistration);

		return formNavigatorEntry;
	}

	@Override
	public void modifiedService(
		ServiceReference
			<com.liferay.portal.kernel.servlet.taglib.ui.FormNavigatorEntry<?>>
				serviceReference,
		FormNavigatorEntry<?> formNavigatorEntry) {

		removedService(serviceReference, formNavigatorEntry);

		addingService(serviceReference);
	}

	@Override
	public void removedService(
		ServiceReference
			<com.liferay.portal.kernel.servlet.taglib.ui.FormNavigatorEntry<?>>
				serviceReference,
		FormNavigatorEntry<?> formNavigatorEntry) {

		ServiceRegistration<FormNavigatorEntry<?>> serviceRegistration =
			_serviceRegistrations.remove(serviceReference);

		serviceRegistration.unregister();
	}

	private final BundleContext _bundleContext;
	private final Map
		<ServiceReference
			<com.liferay.portal.kernel.servlet.taglib.ui.FormNavigatorEntry<?>>,
		 ServiceRegistration<FormNavigatorEntry<?>>> _serviceRegistrations;

}