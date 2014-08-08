/**
 * Copyright (c) 2000-2014 Liferay, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.liferay.view.extension;

import com.liferay.portal.kernel.servlet.taglib.ViewExtension;

import java.io.IOException;

import java.util.Dictionary;
import java.util.EventListener;
import java.util.Hashtable;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.servlet.JspServlet;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.http.context.ServletContextHelper;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

/**
 * @author Carlos Sierra Andrés
 */
@Component(
	immediate = true,
	property = {"extension.id=login-form-after-password"},
	service = {ViewExtension.class}
)
public class LoginViewExtensionSample
	implements ViewExtension, ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		_servletContext = null;
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		_servletContext = servletContextEvent.getServletContext();
	}

	@Override
	public void render(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException {

		if (_servletContext == null) {
			return;
		}

		RequestDispatcher requestDispatcher =
			_servletContext.getRequestDispatcher("/extension.jsp");

		if (requestDispatcher != null) {
			try {
				requestDispatcher.include(request, response);
			}
			catch (ServletException se) {
				//throw new IOException(se);
			}
		}
	}

	@Activate
	protected void createJspServlet() {
		Bundle bundle = FrameworkUtil.getBundle(LoginViewExtensionSample.class);

		BundleContext bundleContext = bundle.getBundleContext();

		Dictionary<String, Object> properties = new Hashtable<String, Object>();

		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME,
			"sample-view-extension");
		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_PATH,
			"/sample-view-extension");

		ServletContextHelper servletContextHelper =
			new ServletContextHelper(bundle) {};

		_contextServiceRegistration = bundleContext.registerService(
			ServletContextHelper.class, servletContextHelper, properties);

		properties = new Hashtable<String, Object>();

		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT,
			"sample-view-extension");

		_listenerServiceRegistration = bundleContext.registerService(
			EventListener.class, this, properties);

		properties = new Hashtable<String, Object>();

		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT,
			"sample-view-extension");
		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_NAME, "jsp");
		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_PATTERN, "*.jsp");
		properties.put(
			"servlet.init.compilerClassName",
			"com.liferay.portal.servlet.jsp.compiler.compiler.JspCompiler");
		properties.put("servlet.init.httpMethods", "GET,POST,HEAD");
		properties.put("servlet.init.keepgenerated", "true");
		properties.put("servlet.init.logVerbosityLevel", "DEBUG");

		_servletServiceRegistration = bundleContext.registerService(
			Servlet.class, new JspServlet(), properties);
	}

	@Deactivate
	protected void deactivate() {
		_servletServiceRegistration.unregister();
		_listenerServiceRegistration.unregister();
		_contextServiceRegistration.unregister();
	}

	private volatile ServletContext _servletContext;
	private ServiceRegistration<ServletContextHelper>
		_contextServiceRegistration;
	private ServiceRegistration<EventListener> _listenerServiceRegistration;
	private ServiceRegistration<Servlet> _servletServiceRegistration;

}