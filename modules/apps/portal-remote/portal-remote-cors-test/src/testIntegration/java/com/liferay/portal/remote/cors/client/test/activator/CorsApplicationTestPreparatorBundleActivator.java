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

package com.liferay.portal.remote.cors.client.test.activator;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.remote.cors.test.CorsTestApplication;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.ListIterator;

import javax.ws.rs.core.Application;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * @author Marta Medio
 */
public class CorsApplicationTestPreparatorBundleActivator
	implements BundleActivator {

	@Override
	public void start(BundleContext bundleContext) {
		this.bundleContext = bundleContext;

		autoCloseables = new ArrayList<>();

		try {
			prepareTest();
		}
		catch (Exception e) {
			stop(bundleContext);

			throw new RuntimeException(e);
		}
	}

	@Override
	public void stop(BundleContext bundleContext) {
		ListIterator<AutoCloseable> listIterator = autoCloseables.listIterator(
			autoCloseables.size());

		while (listIterator.hasPrevious()) {
			AutoCloseable previousAutoCloseable = listIterator.previous();

			try {
				previousAutoCloseable.close();
			}
			catch (Exception e) {
				_log.error(e, e);
			}
		}
	}

	protected void prepareTest() throws Exception {
		registerJaxRsApplication(
			new CorsTestApplication(), "test", new HashMapDictionary<>());
	}

	protected ServiceRegistration<Application> registerJaxRsApplication(
		Application application, String path,
		Dictionary<String, Object> properties) {

		if ((properties == null) || properties.isEmpty()) {
			properties = new HashMapDictionary<>();
		}

		properties.put("osgi.jaxrs.application.base", "/" + path);
		properties.put("liferay.cors.annotation", true);
		properties.put("liferay.oauth2", false);

		ServiceRegistration<Application> serviceRegistration =
			bundleContext.registerService(
				Application.class, application, properties);

		autoCloseables.add(serviceRegistration::unregister);

		return serviceRegistration;
	}

	protected ArrayList<AutoCloseable> autoCloseables;
	protected BundleContext bundleContext;

	private static final Log _log = LogFactoryUtil.getLog(
		CorsApplicationTestPreparatorBundleActivator.class);

}