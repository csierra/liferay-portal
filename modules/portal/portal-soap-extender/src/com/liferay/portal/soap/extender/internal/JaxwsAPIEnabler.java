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

package com.liferay.portal.soap.extender.internal;

import aQute.bnd.annotation.component.Modified;
import aQute.bnd.annotation.metatype.Configurable;

import com.liferay.portal.soap.extender.internal.configuration.JaxwsApiConfiguration;

import java.util.Map;

import javax.xml.ws.spi.Provider;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.jaxws22.spi.ProviderImpl;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Carlos Sierra Andrés
 */
@Component(
	configurationPid = "com.liferay.portal.soap.extender.configuration.JaxwsApiConfiguration",
	configurationPolicy = ConfigurationPolicy.REQUIRE
)
public class JaxwsAPIEnabler {

	@Activate
	protected void activate(
			BundleContext bundleContext, Map<String, Object> properties)
		throws InterruptedException, InvalidSyntaxException {

		JaxwsApiConfiguration configuration = Configurable.createConfigurable(
			JaxwsApiConfiguration.class, properties);

		String contextPath = configuration.contextPath();

		Filter filter = bundleContext.createFilter(
			"(&(objectClass=org.apache.cxf.Bus)(" +
				HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_PATH + "=" +
				contextPath + "))");

		_busServiceTracker = new ServiceTracker<>(bundleContext, filter, null);

		_busServiceTracker.open();

		Bus bus = _busServiceTracker.waitForService(configuration.timeout());

		if (bus != null) {
			BusFactory.setDefaultBus(bus);

			ProviderImpl providerImpl = new ProviderImpl();

			_providerServiceRegistration = bundleContext.registerService(
				Provider.class, providerImpl, null);
		}
	}

	@Deactivate
	protected void deactivate() {
		_providerServiceRegistration.unregister();

		_busServiceTracker.close();
	}

	@Modified
	protected void modified(
			BundleContext bundleContext, Map<String, Object> properties)
		throws InterruptedException, InvalidSyntaxException {

		deactivate();

		activate(bundleContext, properties);
	}

	private ServiceTracker<Bus, Bus> _busServiceTracker;
	private ServiceRegistration<Provider> _providerServiceRegistration;

}