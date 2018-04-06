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

package com.liferay.oauth2.provider.rest.internal.request.scope.checker.filter;

import com.liferay.oauth2.provider.rest.spi.request.scope.checker.filter.RequestScopeCheckerFilter;
import com.liferay.oauth2.provider.scope.RequiresNoScope;
import com.liferay.oauth2.provider.scope.RequiresScope;
import com.liferay.oauth2.provider.scope.ScopeChecker;

import java.lang.reflect.Method;

import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Request;

import org.osgi.service.component.annotations.Component;

/**
 * @author Carlos Sierra Andrés
 */
@Component(property = "type=annotation")
public class AnnotationRequestScopeChecker
	implements RequestScopeCheckerFilter {

	@Override
	public boolean isAllowed(
		ScopeChecker scopeChecker, Request request, ResourceInfo resourceInfo) {

		Method method = resourceInfo.getResourceMethod();

		RequiresNoScope requiresNoScope = method.getAnnotation(
			RequiresNoScope.class);

		if (requiresNoScope != null) {
			return true;
		}

		RequiresScope annotation = method.getAnnotation(RequiresScope.class);

		if (annotation == null) {
			Class<?> resourceClass = resourceInfo.getResourceClass();

			requiresNoScope = resourceClass.getAnnotation(
				RequiresNoScope.class);

			if (requiresNoScope != null) {
				return true;
			}

			annotation = resourceClass.getAnnotation(RequiresScope.class);
		}

		if (annotation != null) {
			if (annotation.allNeeded()) {
				return scopeChecker.checkAllScopes(annotation.value());
			}
			else {
				return scopeChecker.checkAnyScope(annotation.value());
			}
		}

		return false;
	}

}