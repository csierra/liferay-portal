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

package com.liferay.deploy.hot.internal.event;


import com.liferay.portal.deploy.hot.JSONWebServiceHotDeployListener;
import com.liferay.portal.deploy.hot.MessagingHotDeployListener;
import com.liferay.portal.deploy.hot.PluginPackageHotDeployListener;
import com.liferay.portal.deploy.hot.PortletHotDeployListener;
import com.liferay.portal.kernel.deploy.hot.HotDeployEvent;
import com.liferay.portal.kernel.deploy.hot.HotDeployException;
import com.liferay.portal.kernel.deploy.hot.HotDeployListener;
import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Miguel Pastor
 */
public class ApplicationsHandler implements EventHandler {

	public static ApplicationsHandler getInstance() {
		return _instance;
	}

	@Override
	public void handleEvent(Event event) {

	}

	public void registerApplication(
		Bundle bundle, ServletContext servletContext) {

		HotDeployEvent hotDeployEvent = _buildHotDeployEvent(
			bundle, servletContext);

		_invokeDeploy(hotDeployEvent);
	}

	public void unregisterApplication(
		Bundle bundle, ServletContext servletContext) {

		HotDeployEvent hotDeployEvent = _buildHotDeployEvent(
			bundle, servletContext);

		_invokeUndeploy(hotDeployEvent);
	}

	private ApplicationsHandler() {
		_hotDeployListeners = new ArrayList<HotDeployListener>();

		_hotDeployListeners.add(new JSONWebServiceHotDeployListener());
		_hotDeployListeners.add(new PluginPackageHotDeployListener());
		_hotDeployListeners.add(new PortletHotDeployListener());
		_hotDeployListeners.add(new MessagingHotDeployListener());
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

	private static final ApplicationsHandler _instance =
		new ApplicationsHandler();

	private List<HotDeployListener> _hotDeployListeners;

}
