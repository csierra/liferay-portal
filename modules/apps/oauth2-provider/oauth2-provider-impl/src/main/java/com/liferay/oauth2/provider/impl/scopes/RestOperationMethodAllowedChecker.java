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

import com.liferay.oauth2.provider.api.scopes.ScopeChecker;

import javax.ws.rs.HttpMethod;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class RestOperationMethodAllowedChecker implements MethodAllowedChecker {

	public RestOperationMethodAllowedChecker(ScopeChecker scopeChecker) {
		this(scopeChecker, new DefaultOperationScopeMapper());
	}

	public RestOperationMethodAllowedChecker(
		ScopeChecker scopeChecker,
		OperationScopeMapper operationScopeMapper) {

		this(scopeChecker, operationScopeMapper, false);
	}

	public RestOperationMethodAllowedChecker(
		ScopeChecker scopeChecker, OperationScopeMapper operationScopeMapper,
		boolean defaultReturn) {

		_scopeChecker = scopeChecker;
		_operationScopeMapper = operationScopeMapper;
		_defaultReturn = defaultReturn;
	}

	@Override
	public boolean isAllowed(Method method) {
		Annotation[] annotations = method.getAnnotations();
		
		if ((annotations == null) || (annotations.length == 0)) {
			return _defaultReturn;
		}

		String methodString = getMethodString(annotations);

		if (methodString == null) {
			return _defaultReturn;
		}

		return _scopeChecker.hasScope(
			_operationScopeMapper.getScope(methodString));
	}

	private String getMethodString(Annotation[] annotations) {
		String httpMethodString = null;

		for (int i = 0; i < annotations.length; i++) {
			Annotation annotation = annotations[i];

			Class<? extends Annotation> annotationType = 
				annotation.annotationType();

			HttpMethod httpMethod = annotationType.getAnnotation(
				HttpMethod.class);

			if (httpMethod != null) {
				httpMethodString = httpMethod.value();

				break;
			}
		}

		return httpMethodString;
	}

	private OperationScopeMapper
		_operationScopeMapper;
	private ScopeChecker _scopeChecker;
	private boolean _defaultReturn;

	public interface OperationScopeMapper {
		String getScope(String httpOperation);
	}

	public static class DefaultOperationScopeMapper
		implements OperationScopeMapper {

		@Override
		public String getScope(String httpOperation) {
			switch (httpOperation) {
				case "HEAD":
				case "GET":
				case "OPTIONS":
					return "READ";
			}

			return "WRITE";
		}
	}

}
