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

package com.liferay.oauth2.provider.rest.requestscopechecker;

import com.liferay.oauth2.provider.scope.RequiresNoScope;
import com.liferay.oauth2.provider.scope.RequiresScope;
import com.liferay.oauth2.provider.scope.ScopeChecker;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.lang.reflect.Method;

@Component(
	property = {
		"osgi.jaxrs.extension=true",
		"osgi.jaxrs.name=Liferay.OAuth2.annotations.checker",
		"osgi.jaxrs.extension.select=(liferay.extension=OAuth2)",
		"osgi.jaxrs.application.select=(&(osgi.jaxrs.extension.select=\\(liferay.extension=OAuth2\\))(oauth2.scopechecker.type=annotations))"
	},
	scope = ServiceScope.PROTOTYPE
)
@Priority(Priorities.AUTHORIZATION - 8)
public class AnnotationRequestScopeChecker implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext) {
		Method method = _resourceInfo.getResourceMethod();

		RequiresNoScope requiresNoScope = method.getAnnotation(
			RequiresNoScope.class);

		if (requiresNoScope != null) {
			return;
		}

		RequiresScope annotation = method.getAnnotation(RequiresScope.class);

		if (annotation == null) {
			Class<?> resourceClass = _resourceInfo.getResourceClass();

			requiresNoScope = resourceClass.getAnnotation(
				RequiresNoScope.class);

			if (requiresNoScope != null) {
				return;
			}

			annotation = resourceClass.getAnnotation(RequiresScope.class);
		}

		boolean allowed = false;

		if (annotation != null) {
			if (annotation.allNeeded()) {
				allowed = _scopeChecker.checkAllScopes(annotation.value());
			}
			else {
				allowed = _scopeChecker.checkAnyScope(annotation.value());
			}

		}

		if (annotation == null || !allowed) {
			requestContext.abortWith(
				Response.status(Response.Status.FORBIDDEN).build());
		}

	}

	@Context
	private ResourceInfo _resourceInfo;

	@Reference
	private ScopeChecker _scopeChecker;

}