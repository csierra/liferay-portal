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

package com.liferay.portal.remote.jaxrs.security.impl.test;

import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;

import java.util.ArrayDeque;
import java.util.Dictionary;
import java.util.Queue;

import javax.ws.rs.core.Application;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;

/**
 * @author Carlos Sierra Andrés
 */
public abstract class BaseJaxrsSecurityClientTestCase {

	@BeforeClass
	public static void setUpClass() {
		Bundle bundle = FrameworkUtil.getBundle(
			BaseJaxrsSecurityClientTestCase.class);

		_bundleContext = bundle.getBundleContext();
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		for (AutoCloseable autoCloseable : _autoCloseables) {
			autoCloseable.close();
		}

		_autoCloseables.clear();
	}

	protected User addUser() throws Exception {
		User user = UserTestUtil.addUser();

		_autoCloseables.add(
			() -> UserLocalServiceUtil.deleteUser(user.getUserId()));

		return user;
	}

	protected void registerJaxRsApplication(
		Application application, String path,
		Dictionary<String, Object> properties) {

		if ((properties == null) || properties.isEmpty()) {
			properties = new HashMapDictionary<>();
		}

		//properties.put("liferay.access.control.disable", true);
		//properties.put("liferay.oauth2", false);
		properties.put("osgi.jaxrs.application.base", "/" + path);

		ServiceRegistration<Application> serviceRegistration =
			_bundleContext.registerService(
				Application.class, application, properties);

		_autoCloseables.add(serviceRegistration::unregister);
	}

	private static final Queue<AutoCloseable> _autoCloseables =
		new ArrayDeque<>();
	private static BundleContext _bundleContext;

}