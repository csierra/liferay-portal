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

import java.io.IOException;

import java.util.Arrays;

import javax.annotation.security.RolesAllowed;

import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.SecurityContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;

/**
 * @author Carlos Sierra Andrés
 */
@Component(
	property = {
		JaxrsWhiteboardConstants.JAX_RS_APPLICATION_SELECT + "=(!(liferay.roles.allowed=false))",
		JaxrsWhiteboardConstants.JAX_RS_EXTENSION + "=true",
		JaxrsWhiteboardConstants.JAX_RS_EXTENSION_SELECT + "=(osgi.jaxrs.name=Liferay.Security.Context)",
		JaxrsWhiteboardConstants.JAX_RS_EXTENSION_SELECT + "=(osgi.jaxrs.name=Liferay.Security.Exception.Mapper)",
		JaxrsWhiteboardConstants.JAX_RS_NAME + "=Liferay.Roles.Allowed"
	},
	scope = ServiceScope.PROTOTYPE, service = DynamicFeature.class
)
public class LiferayRolesAllowedDynamicFeature implements DynamicFeature {

	@Override
	public void configure(ResourceInfo resourceInfo, FeatureContext context) {
		RolesAllowed rolesAllowed = AnnotationLocator.locate(
			resourceInfo.getResourceMethod(), resourceInfo.getResourceClass(),
			RolesAllowed.class);

		if (rolesAllowed != null) {
			context.register(
				new LiferayRolesAllowedContainerRequestFilter(
					rolesAllowed.value()),
				Priorities.AUTHORIZATION);
		}
	}

	private static class LiferayRolesAllowedContainerRequestFilter
		implements ContainerRequestFilter {

		public LiferayRolesAllowedContainerRequestFilter(String[] roles) {
			_roles = roles;
		}

		@Override
		public void filter(ContainerRequestContext requestContext)
			throws IOException {

			SecurityContext securityContext =
				requestContext.getSecurityContext();

			for (String role : _roles) {
				if (securityContext.isUserInRole(role)) {
					return;
				}
			}

			throw new SecurityException(
				"User is not in any of the allowed roles: " +
					Arrays.asList(_roles));
		}

		private final String[] _roles;

	}

}