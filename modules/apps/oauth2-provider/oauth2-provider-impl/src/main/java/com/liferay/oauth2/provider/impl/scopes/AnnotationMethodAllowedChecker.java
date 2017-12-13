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

import com.liferay.oauth2.provider.api.scopes.RequiresScope;
import com.liferay.oauth2.provider.api.scopes.ScopeChecker;

import java.lang.reflect.Method;

public class AnnotationMethodAllowedChecker implements MethodAllowedChecker {

	public AnnotationMethodAllowedChecker(
		ScopeChecker scopeChecker) {

		this(scopeChecker, false);
	}

	public AnnotationMethodAllowedChecker(
		ScopeChecker scopeChecker, boolean defaultReturn) {

		_scopeChecker = scopeChecker;
		_defaultReturn = defaultReturn;
	}

	@Override
	public boolean isAllowed(Method method) {
		RequiresScope annotation = method.getAnnotation(RequiresScope.class);

		if (annotation != null) {
			String scope = annotation.value();

			return _scopeChecker.hasScope(scope);
		}

		return _defaultReturn;
	}

	private ScopeChecker _scopeChecker;
	private boolean _defaultReturn;

}
