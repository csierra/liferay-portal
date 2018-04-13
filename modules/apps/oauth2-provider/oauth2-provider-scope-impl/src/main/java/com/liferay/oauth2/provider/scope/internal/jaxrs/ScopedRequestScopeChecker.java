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

package com.liferay.oauth2.provider.scope.internal.jaxrs;

import com.liferay.oauth2.provider.rest.spi.request.scope.checker.filter.RequestScopeCheckerFilter;
import com.liferay.oauth2.provider.scope.ScopeChecker;
import com.liferay.oauth2.provider.scope.liferay.ScopedServiceTrackerMap;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.util.PortalUtil;

import java.io.IOException;

import java.util.Collection;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

/**
 * @author Carlos Sierra Andrés
 */
public class ScopedRequestScopeChecker implements ContainerRequestFilter {

	public ScopedRequestScopeChecker(
		ScopedServiceTrackerMap<RequestScopeCheckerFilter>
			scopedServiceTrackerMap, ScopeChecker scopeChecker) {

		_scopedServiceTrackerMap = scopedServiceTrackerMap;

		_scopeChecker = scopeChecker;
	}

	@Override
	public void filter(ContainerRequestContext requestContext)
		throws IOException {

		try {
			Company company = PortalUtil.getCompany(_httpServletRequest);

			Class<? extends Application> clazz = _application.getClass();

			Bundle bundle = FrameworkUtil.getBundle(clazz);

			if (bundle == null) {
				return;
			}

			String osgiJaxrsName;

			BundleContext bundleContext = bundle.getBundleContext();

			Collection<ServiceReference<Application>> serviceReferences =
				bundleContext.getServiceReferences(
					Application.class,
					"(component.name=" + clazz.getName() + ")");

			Stream<ServiceReference<Application>> serviceReferencesStream =
				serviceReferences.stream();

			osgiJaxrsName = (String)serviceReferencesStream.map(
				serviceReference ->
					serviceReference.getProperty("osgi.jaxrs.name")
			).filter(
				obj -> obj != null
			).findFirst(
			).orElse(
				clazz.getName()
			);

			RequestScopeCheckerFilter requestScopeChecker =
				_scopedServiceTrackerMap.getService(
					company.getCompanyId(), osgiJaxrsName);

			if (!requestScopeChecker.isAllowed(
					_scopeChecker, requestContext.getRequest(),
					_resourceInfo)) {

				requestContext.abortWith(Response.status(403).build());
			}
		}
		catch (InvalidSyntaxException | PortalException e) {
			requestContext.abortWith(
				Response.status(
					500
				).entity(
					e.getMessage()
				).build());
		}
	}

	@Context
	private Application _application;

	@Context
	private HttpServletRequest _httpServletRequest;

	@Context
	private ResourceInfo _resourceInfo;

	private final ScopeChecker _scopeChecker;
	private final ScopedServiceTrackerMap<RequestScopeCheckerFilter>
		_scopedServiceTrackerMap;

}