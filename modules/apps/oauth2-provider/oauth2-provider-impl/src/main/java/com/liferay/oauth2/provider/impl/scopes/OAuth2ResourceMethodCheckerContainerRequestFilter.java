/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.oauth2.provider.impl.scopes;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;

public class OAuth2ResourceMethodCheckerContainerRequestFilter implements
	ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext)
		throws IOException {

		if (!_methodAllowedChecker.isAllowed(
			_resourceInfo.getResourceMethod())) {

			requestContext.abortWith(
				Response.status(Response.Status.NOT_FOUND).build());
		}
	}

	public OAuth2ResourceMethodCheckerContainerRequestFilter(
		MethodAllowedChecker methodScopeChecker) {

		_methodAllowedChecker = methodScopeChecker;
	}

	@Context
	ResourceInfo _resourceInfo;

	private MethodAllowedChecker _methodAllowedChecker;

}
