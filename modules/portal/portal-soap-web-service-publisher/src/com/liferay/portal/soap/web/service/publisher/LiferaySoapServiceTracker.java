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

package com.liferay.portal.soap.web.service.publisher;

import com.liferay.portal.soap.web.service.publisher.configuration.ExtensionManager;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.Servlet;

import javax.xml.namespace.QName;
import javax.xml.ws.Binding;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.spi.Provider;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.bus.CXFBusFactory;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.jaxws.support.JaxWsEndpointImpl;
import org.apache.cxf.jaxws22.spi.ProviderImpl;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.http.context.ServletContextHelper;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Carlos Sierra Andrés
 */
public class LiferaySoapServiceTracker {

	public static final String CONTEXT_NAME = "LIFERAY_CXF_CONTEXT";

	public LiferaySoapServiceTracker(
		BundleContext bundleContext, String contextPath,
		ExtensionManager extensionManager) {

		_bundleContext = bundleContext;
		_contextPath = contextPath;
		_extensionManager = extensionManager;
	}

	protected void start() {
		Bus bus = _createBus();

		BusFactory.setDefaultBus(bus);
		
		_registerCXFServlet(bus, _contextPath);

		try {
			Filter servicesFilter = _bundleContext.createFilter(
				"(&(address=*)(jaxws=true))");

			_serverServiceTracker = new ServiceTracker<>(
				_bundleContext, servicesFilter,
				new ServerServiceTrackerCustomizer(bus));
		}
		catch (InvalidSyntaxException e) {
			throw new RuntimeException(e);
		}

		_serverServiceTracker.open();
	}

	protected void stop() {
		try {
			_serverServiceTracker.close();
		}
		catch (Exception e) {
		}

		try {
			_servletServiceRegistration.unregister();
		}
		catch (Exception e) {
		}

		try {
			_servletContextHelperRegistration.unregister();
		}
		catch (Exception e) {
		}
	}

	private Bus _createBus() {
		CXFBusFactory busFactory = (CXFBusFactory)CXFBusFactory.newInstance(
			"org.apache.cxf.bus.CXFBusFactory");

		return busFactory.createBus(_extensionManager.getExtensions());
	}

	private void _registerCXFServlet(Bus bus, String contextPath) {
		Dictionary<String, Object> properties = new Hashtable<>();

		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME, CONTEXT_NAME);
		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_PATH, contextPath);

		_servletContextHelperRegistration = _bundleContext.registerService(
			ServletContextHelper.class,
			new ServletContextHelper(_bundleContext.getBundle()) {},
			properties);

		properties = new Hashtable<>();

		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT,
			CONTEXT_NAME);
		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_NAME, "CXFServlet");
		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_PATTERN, "/*");

		_cxfServlet = new CXFNonSpringServlet();

		_cxfServlet.setBus(bus);

		_servletServiceRegistration = _bundleContext.registerService(
			Servlet.class, _cxfServlet, properties);
	}

	private final BundleContext _bundleContext;
	private final String _contextPath;
	private CXFNonSpringServlet _cxfServlet;
	private final ExtensionManager _extensionManager;
	private ServiceRegistration<Provider> _providerServiceRegistration;
	private ServiceTracker<Object, ServerTrackingInformation>
		_serverServiceTracker;
	private ServiceRegistration<ServletContextHelper>
		_servletContextHelperRegistration;
	private ServiceRegistration<Servlet> _servletServiceRegistration;

	private static class ServerTrackingInformation {

		public ServerTrackingInformation(
			Server server, ServiceTracker<Handler, Handler> serviceTracker) {

			_server = server;
			_serviceTracker = serviceTracker;
		}

		public Server getServer() {
			return _server;
		}

		public ServiceTracker<Handler, Handler> getServiceTracker() {
			return _serviceTracker;
		}

		private final Server _server;
		private final ServiceTracker<Handler, Handler> _serviceTracker;

	}

	private class HandlerServiceTrackerCustomizer
		implements ServiceTrackerCustomizer<Handler, Handler> {

		public HandlerServiceTrackerCustomizer(Server server) {
			_server = server;
		}

		@Override
		public Handler addingService(ServiceReference<Handler> reference) {
			Handler handler = _bundleContext.getService(reference);

			JaxWsEndpointImpl jaxWsEndpoint =
				(JaxWsEndpointImpl) _server.getEndpoint();

			Binding jaxwsBinding = jaxWsEndpoint.getJaxwsBinding();

			List<Handler> handlerChain = jaxwsBinding.getHandlerChain();

			handlerChain.add(handler);

			jaxwsBinding.setHandlerChain(handlerChain);

			return handler;
		}

		@Override
		public void modifiedService(
			ServiceReference<Handler> reference, Handler service) {
		}

		@Override
		public void removedService(
			ServiceReference<Handler> reference, Handler handler) {

			JaxWsEndpointImpl jaxWsEndpoint =
				(JaxWsEndpointImpl) _server.getEndpoint();

			Binding jaxwsBinding = jaxWsEndpoint.getJaxwsBinding();

			List<Handler> handlerChain = jaxwsBinding.getHandlerChain();

			handlerChain.remove(handler);

			jaxwsBinding.setHandlerChain(handlerChain);

			_bundleContext.ungetService(reference);
		}

		private final Server _server;

	}

	private class ServerServiceTrackerCustomizer
		implements ServiceTrackerCustomizer<Object, ServerTrackingInformation> {

		public ServerServiceTrackerCustomizer(Bus bus) {
			_bus = bus;
		}

		@Override
		public ServerTrackingInformation addingService(
			ServiceReference<Object> serviceReference) {

			Object service = _bundleContext.getService(serviceReference);

			JaxWsServerFactoryBean jaxWsServerFactoryBean =
				new JaxWsServerFactoryBean();

			HashMap<String, Object> properties = getPropertiesAsMap(
				serviceReference);

			jaxWsServerFactoryBean.setProperties(properties);

			jaxWsServerFactoryBean.setBus(_bus);

			Object addressObject = serviceReference.getProperty("address");

			String address = addressObject.toString();

			jaxWsServerFactoryBean.setAddress(address);

			Object endpointNameObject = serviceReference.getProperty(
				"endpointName");

			if ((endpointNameObject != null) &&
				endpointNameObject instanceof QName) {

				QName endpointName = (QName)endpointNameObject;

				jaxWsServerFactoryBean.setEndpointName(endpointName);
			}

			Object serviceClassObject = serviceReference.getProperty(
				"serviceClass");

			if ((serviceClassObject != null) &&
				serviceClassObject instanceof Class<?>) {

				Class<?> serviceClass = (Class<?>)serviceClassObject;

				jaxWsServerFactoryBean.setServiceClass(serviceClass);
			}

			Object wsdlLocationObject = serviceReference.getProperty(
				"wsdlLocation");

			if (wsdlLocationObject != null) {
				jaxWsServerFactoryBean.setWsdlLocation(
					wsdlLocationObject.toString());
			}

			jaxWsServerFactoryBean.setServiceBean(service);

			Thread thread = Thread.currentThread();

			ClassLoader contextClassLoader = thread.getContextClassLoader();

			try {
				Bundle bundle = serviceReference.getBundle();

				BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);

				thread.setContextClassLoader(bundleWiring.getClassLoader());

				Server server = jaxWsServerFactoryBean.create();

				ServiceTracker<Handler, Handler> handlerServiceTracker =
					trackHandlers(address, server);

				//TODO: log

				return new ServerTrackingInformation(
					server, handlerServiceTracker);
			}
			catch (Throwable t) {
				_bundleContext.ungetService(serviceReference);

				//TODO: log

				return null;
			}
			finally {
				thread.setContextClassLoader(contextClassLoader);
			}
		}

		@Override
		public void modifiedService(
			ServiceReference<Object> reference,
			ServerTrackingInformation server) {

			removedService(reference, server);

			addingService(reference);
		}

		@Override
		public void removedService(
			ServiceReference<Object> reference,
			ServerTrackingInformation serverTrackingInformation) {

			serverTrackingInformation.getServer().destroy();

			serverTrackingInformation.getServiceTracker().close();

			_bundleContext.ungetService(reference);
		}

		private HashMap<String, Object> getPropertiesAsMap(
			ServiceReference<Object> serviceReference) {

			String[] propertyKeys = serviceReference.getPropertyKeys();

			HashMap<String, Object> properties = new HashMap<>(
				propertyKeys.length);

			for (String propertyKey : propertyKeys) {
				properties.put(
					propertyKey, serviceReference.getProperty(propertyKey));
			}

			return properties;
		}

		private ServiceTracker<Handler, Handler> trackHandlers(
			String address, final Server server) {

			String handlersFilterString =
				"(&(objectClass=" + Handler.class.getName() + ")(address=" +
					address + "))";

			Filter handlersFilter;

			try {
				handlersFilter = _bundleContext.createFilter(
					handlersFilterString);
			}
			catch (InvalidSyntaxException ise) {
				throw new RuntimeException(ise);
			}

			ServiceTracker<Handler, Handler> handlerServiceTracker =
				new ServiceTracker<>(
					_bundleContext, handlersFilter,
					new HandlerServiceTrackerCustomizer(server));

			handlerServiceTracker.open();

			return handlerServiceTracker;
		}

		private final Bus _bus;

	}

}