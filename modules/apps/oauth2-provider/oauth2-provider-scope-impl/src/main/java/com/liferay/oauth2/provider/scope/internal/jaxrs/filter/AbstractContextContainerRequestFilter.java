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

package com.liferay.oauth2.provider.scope.internal.jaxrs.filter;

import com.liferay.oauth2.provider.scope.internal.constants.OAuth2ProviderScopeConstants;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

/**
 * @author Tomas Polesovsky
 */
public abstract class AbstractContextContainerRequestFilter
	implements ContainerRequestFilter {

	public String getApplicationName() {
		Bundle bundle = getBundle();

		if (bundle == null) {
			return null;
		}

		BundleContext bundleContext = bundle.getBundleContext();

		Class<?> applicationClass = application.getClass();

		String applicationClassName = applicationClass.getName();

		try {
			Collection<ServiceReference<Application>> serviceReferences =
				bundleContext.getServiceReferences(
					Application.class,
					"(component.name=" + applicationClassName + ")");

			if (!serviceReferences.isEmpty()) {
				Iterator<ServiceReference<Application>> it =
					serviceReferences.iterator();

				ServiceReference<Application> serviceReference = it.next();

				return GetterUtil.getString(
					serviceReference.getProperty(
						OAuth2ProviderScopeConstants.OSGI_JAXRS_NAME),
					applicationClassName);
			}
		}
		catch (InvalidSyntaxException ise) {
			return null;
		}

		return applicationClassName;
	}

	public Bundle getBundle() {
		Class<?> applicationClass = application.getClass();

		return FrameworkUtil.getBundle(applicationClass);
	}

	public long getCompanyId() {
		return PortalUtil.getCompanyId(httpServletRequest);
	}

	@Context
	protected Application application;

	@Context
	protected HttpServletRequest httpServletRequest;

}