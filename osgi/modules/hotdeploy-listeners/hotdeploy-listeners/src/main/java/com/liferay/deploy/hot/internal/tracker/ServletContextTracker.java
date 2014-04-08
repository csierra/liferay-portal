/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.deploy.hot.internal.tracker;

import com.liferay.portal.deploy.hot.JSONWebServiceHotDeployListener;
import com.liferay.portal.deploy.hot.MessagingHotDeployListener;
import com.liferay.portal.deploy.hot.PluginPackageHotDeployListener;
import com.liferay.portal.deploy.hot.PortletHotDeployListener;
import com.liferay.portal.kernel.deploy.hot.HotDeployEvent;
import com.liferay.portal.kernel.deploy.hot.HotDeployException;
import com.liferay.portal.kernel.deploy.hot.HotDeployListener;
import com.liferay.portal.kernel.servlet.ServletContextPool;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.util.tracker.ServiceTracker;

import javax.servlet.ServletContext;

/**
 * This Service tracker tries to mimic the behaviour of our current hot deploy
 * mechanism.
 *
 * This is not the final solution: the next step will be removing the
 * auto-deploy and hot-deploy mechanisms, so we can decouple "service only"
 * applications from the Servlet API (mainly the ServletContext dependency).
 *
 * The newer code will fit within this module but the #HotDeployBundleListener
 * will be removed from the source code
 *
 * @author Miguel Pastor
 */
public class ServletContextTracker extends
	ServiceTracker<ServletContext, ServletContext> {

	public ServletContextTracker(BundleContext bundleContext) {
		super(bundleContext, ServletContext.class, null);

		_hotDeployListeners = new ArrayList<HotDeployListener>();

		_hotDeployListeners.add(new JSONWebServiceHotDeployListener());
		_hotDeployListeners.add(new PluginPackageHotDeployListener());
		_hotDeployListeners.add(new PortletHotDeployListener());
		_hotDeployListeners.add(new MessagingHotDeployListener());
	}

	@Override
	public ServletContext addingService(
		ServiceReference<ServletContext> serviceReference) {

		Bundle bundle = serviceReference.getBundle();

		if (bundle.getBundleId() == 0) {
			return getService();
		}

		ServletContext servletContext = super.addingService(serviceReference);

		_invokeDeploy(bundle, servletContext);

		return servletContext;
	}

	@Override
	public void removedService(
		ServiceReference<ServletContext> serviceReference,
		ServletContext servletContext) {

		Bundle bundle = serviceReference.getBundle();

		if (bundle.getBundleId() == 0) {
			return;
		}

		_invokeUndeploy(bundle, servletContext);

		super.removedService(serviceReference, servletContext);
	}

	private HotDeployEvent _buildHotDeployEvent(
		Bundle bundle, ServletContext servletContext) {

		ClassLoader bundleClassLoader = _getBundleClassLoader(bundle);

		HotDeployEvent hotDeployEvent = new HotDeployEvent(
			servletContext, bundleClassLoader);

		return hotDeployEvent;
	}

	private ClassLoader _getBundleClassLoader(Bundle bundle) {
		BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);

		return bundleWiring.getClassLoader();
	}

	private ServletContext _getServletContext(Bundle bundle) {
		Dictionary<String, String> headers = bundle.getHeaders();

		String webContextPath = headers.get("Web-ContextPath");

		if ( (webContextPath == null) || (webContextPath == "")) {
			throw new IllegalArgumentException(
				"Web-ContextPath attribute cannot be null");
		}

		return ServletContextPool.get(webContextPath.substring(1));
	}

	private void _invokeDeploy(Bundle bundle, ServletContext servletContext) {
		HotDeployEvent hotDeployEvent = _buildHotDeployEvent(
			bundle, servletContext);

		for (HotDeployListener hotDeployListener : _hotDeployListeners) {
			try {
				hotDeployListener.invokeDeploy(hotDeployEvent);
			} catch (HotDeployException hde) {
				throw new RuntimeException(
					"Unexpected error executing " +
						hotDeployListener.getClass(), hde);
			}
		}
	}

	private void _invokeUndeploy(Bundle bundle, ServletContext servletContext) {
		HotDeployEvent hotDeployEvent = _buildHotDeployEvent(
			bundle, servletContext);

		for (HotDeployListener hotDeployListener : _hotDeployListeners) {
			try {
				hotDeployListener.invokeUndeploy(hotDeployEvent);
			} catch (HotDeployException hde) {
				throw new RuntimeException(
					"Unexpected error executing " +
						hotDeployListener.getClass(), hde);
			}
		}
	}

	private List<HotDeployListener> _hotDeployListeners;

}
