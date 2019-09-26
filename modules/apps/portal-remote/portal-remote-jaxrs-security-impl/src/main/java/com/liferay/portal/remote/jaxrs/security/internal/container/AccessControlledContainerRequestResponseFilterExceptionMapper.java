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

package com.liferay.portal.remote.jaxrs.security.internal.container;

import com.liferay.petra.reflect.AnnotationLocator;
import com.liferay.portal.kernel.security.access.control.AccessControlUtil;
import com.liferay.portal.kernel.security.access.control.AccessControlled;
import com.liferay.portal.kernel.security.auth.AccessControlContext;
import com.liferay.portal.security.access.control.AccessControlAdvisor;
import com.liferay.portal.security.access.control.AccessControlAdvisorImpl;

import java.io.IOException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.ext.Providers;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;

/**
 * @author Carlos Sierra Andrés
 */
@Component(
	property = {
		JaxrsWhiteboardConstants.JAX_RS_APPLICATION_SELECT + "=(!(liferay.access.control=false))",
		JaxrsWhiteboardConstants.JAX_RS_EXTENSION + "=true",
		JaxrsWhiteboardConstants.JAX_RS_EXTENSION_SELECT + "=(osgi.jaxrs.name=Liferay.Security.Exception.Mapper)",
		JaxrsWhiteboardConstants.JAX_RS_NAME + "=Liferay.Access.Control"
	},
	scope = ServiceScope.PROTOTYPE,
	service = {ContainerRequestFilter.class, ContainerResponseFilter.class}
)
public class AccessControlledContainerRequestResponseFilterExceptionMapper
	implements ContainerRequestFilter, ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext containerRequestContext)
		throws IOException {

		incrementServiceDepth();

		Method method = _resourceInfo.getResourceMethod();

		AccessControlled accessControlled = AnnotationLocator.locate(
			method, _resourceInfo.getClass(), AccessControlled.class);

		if (accessControlled == null) {
			accessControlled = _NULL_ACCESS_CONTROLLED;
		}

		_accessControlAdvisor.accept(method, new Object[0], accessControlled);
	}

	@Override
	public void filter(
			ContainerRequestContext containerRequestContext,
			ContainerResponseContext containerResponseContext)
		throws IOException {

		decrementServiceDepth();
	}

	protected void decrementServiceDepth() {
		AccessControlContext accessControlContext =
			AccessControlUtil.getAccessControlContext();

		if (accessControlContext == null) {
			return;
		}

		Map<String, Object> settings = accessControlContext.getSettings();

		Integer serviceDepth = (Integer)settings.get(
			AccessControlContext.Settings.SERVICE_DEPTH.toString());

		if (serviceDepth == null) {
			return;
		}

		serviceDepth--;

		settings.put(
			AccessControlContext.Settings.SERVICE_DEPTH.toString(),
			serviceDepth);
	}

	protected void incrementServiceDepth() {
		AccessControlContext accessControlContext =
			AccessControlUtil.getAccessControlContext();

		if (accessControlContext == null) {
			return;
		}

		Map<String, Object> settings = accessControlContext.getSettings();

		Integer serviceDepth = (Integer)settings.get(
			AccessControlContext.Settings.SERVICE_DEPTH.toString());

		if (serviceDepth == null) {
			serviceDepth = Integer.valueOf(1);
		}
		else {
			serviceDepth++;
		}

		settings.put(
			AccessControlContext.Settings.SERVICE_DEPTH.toString(),
			serviceDepth);
	}

	private static final AccessControlled _NULL_ACCESS_CONTROLLED =
		new AccessControlled() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return AccessControlled.class;
			}

			@Override
			public boolean guestAccessEnabled() {
				return false;
			}

			@Override
			public boolean hostAllowedValidationEnabled() {
				return false;
			}

		};

	private final AccessControlAdvisor _accessControlAdvisor =
		new AccessControlAdvisorImpl();

	@Context
	private HttpHeaders _httpHeaders;

	@Context
	private Providers _providers;

	@Context
	private Request _request;

	@Context
	private ResourceInfo _resourceInfo;

}