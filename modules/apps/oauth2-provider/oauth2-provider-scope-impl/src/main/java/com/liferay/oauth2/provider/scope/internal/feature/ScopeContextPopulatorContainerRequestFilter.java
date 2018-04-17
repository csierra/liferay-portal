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

package com.liferay.oauth2.provider.scope.internal.feature;

import com.liferay.oauth2.provider.scope.liferay.ScopeContext;

import java.io.IOException;

import java.util.Collection;
import java.util.stream.Stream;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

/**
 * @author Carlos Sierra Andrés
 */
public class ScopeContextPopulatorContainerRequestFilter
	implements ContainerRequestFilter {

	public ScopeContextPopulatorContainerRequestFilter(
		ScopeContext scopeContext) {

		_scopeContext = scopeContext;
	}

	@Override
	public void filter(ContainerRequestContext requestContext)
		throws IOException {

		Class<? extends Application> clazz = _application.getClass();

		Bundle bundle = FrameworkUtil.getBundle(clazz);

		if (bundle == null) {
			return;
		}

		try {
			BundleContext bundleContext = bundle.getBundleContext();

			Collection<ServiceReference<Application>> serviceReferences =
				bundleContext.getServiceReferences(
					Application.class,
					"(component.name=" + clazz.getName() + ")");

			Stream<ServiceReference<Application>> serviceReferencesStream =
				serviceReferences.stream();

			String osgiJaxrsName = (String)serviceReferencesStream.map(
				serviceReference ->
					serviceReference.getProperty("osgi.jaxrs.name")
			).filter(
				obj -> obj != null
			).findFirst(
			).orElse(
				clazz.getName()
			);

			_scopeContext.setApplicationName(osgiJaxrsName);
		}
		catch (InvalidSyntaxException ise) {
			throw new IOException(ise);
		}

		_scopeContext.setBundle(bundle);
	}

	@Context
	private Application _application;

	private final ScopeContext _scopeContext;

}