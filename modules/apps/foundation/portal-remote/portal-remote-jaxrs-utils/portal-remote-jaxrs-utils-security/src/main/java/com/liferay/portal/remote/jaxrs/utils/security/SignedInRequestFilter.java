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

package com.liferay.portal.remote.jaxrs.utils.security;

import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;

import java.io.IOException;

import javax.annotation.Priority;

import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * @author Carlos Sierra Andrés
 */
@Priority(Priorities.AUTHENTICATION)
@Provider
public class SignedInRequestFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext)
		throws IOException {

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		if (permissionChecker == null) {
			Response.ResponseBuilder responseBuilder = Response.status(
				Response.Status.UNAUTHORIZED);

			responseBuilder = responseBuilder.entity(
				"You must setup security infrastructure.");

			requestContext.abortWith(responseBuilder.build());

			return;
		}

		if (!(permissionChecker.isSignedIn())) {
			Response.ResponseBuilder responseBuilder = Response.status(
				Response.Status.UNAUTHORIZED);

			responseBuilder = responseBuilder.entity("You must be signed in.");

			requestContext.abortWith(responseBuilder.build());
		}
	}

}