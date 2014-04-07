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

package com.liferay.deploy.hot.listeners;

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
import org.osgi.framework.BundleEvent;
import org.osgi.framework.SynchronousBundleListener;
import org.osgi.framework.wiring.BundleWiring;

import javax.servlet.ServletContext;

/**
 * This bundle listener tries to mimic the behaviour of our current hot deploy
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
public class HotDeployBundleListener implements SynchronousBundleListener {

	public HotDeployBundleListener() {
		_hotDeployListeners = new ArrayList<HotDeployListener>();

		_hotDeployListeners.add(new JSONWebServiceHotDeployListener());
		_hotDeployListeners.add(new PluginPackageHotDeployListener());
		_hotDeployListeners.add(new PortletHotDeployListener());
		_hotDeployListeners.add(new MessagingHotDeployListener());
	}

	@Override
	public void bundleChanged(BundleEvent event) {
		Bundle bundle = event.getBundle();

		HotDeployEvent hotDeployEvent = _buildHotDeployEvent(bundle);

		switch (event.getType()) {
			case BundleEvent.STARTED:
				_invokeDeploy(hotDeployEvent);

			case BundleEvent.STOPPED:
				_invokeUndeploy(hotDeployEvent);
		}
	}

	private HotDeployEvent _buildHotDeployEvent(Bundle bundle) {
		ClassLoader bundleClassLoader = _getBundleClassLoader(bundle);
		ServletContext servletContext = _getServletContext(bundle);

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

	private void _invokeDeploy(HotDeployEvent hotDeployEvent) {
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

	private void _invokeUndeploy(HotDeployEvent hotDeployEvent) {
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
