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

import com.liferay.oauth2.provider.scope.RequiresScope;
import org.apache.cxf.jaxrs.model.ClassResourceInfo;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ScopeAnnotationFinder {

	public static Collection<String> find(Class<?> clazz) {
		Set<String> scopes = new HashSet<>();

		doFind(new HashSet<>(), scopes, true, clazz);

		return scopes;
	}

	private static boolean isHttpMethod(Method method) {
		Annotation[] annotations = method.getAnnotations();

		for (Annotation annotation : annotations) {
			Class<? extends Annotation> annotationType =
				annotation.annotationType();
			if (annotationType.getAnnotationsByType(HttpMethod.class) != null) {
				return true;
			}
		}

		return false;
	}

	private static void doFind(
		Set<Class<?>> visited, Set<String> accum,
		boolean recurse, Class<?> resourceClass) {

		RequiresScope declaredAnnotation =
			resourceClass.getDeclaredAnnotation(RequiresScope.class);

		if (declaredAnnotation != null) {
			accum.addAll(Arrays.asList(declaredAnnotation.value()));
		}

		Stream<Method> stream = Arrays.stream(resourceClass.getMethods());

		List<Method> annotatedMethods = stream.filter(
			m -> m.getDeclaredAnnotation(Path.class) != null ||
				 isHttpMethod(m)
		).collect(
			Collectors.toList()
		);

		for (Method method : annotatedMethods) {
			doFind(visited, accum, recurse, method);
		}

		visited.remove(resourceClass);
	}

	private static boolean isSubResourceLocator(Method m) {
		return m.getDeclaredAnnotation(Path.class) != null ||
		!isHttpMethod(m);
	}

	private static void doFind(
		Set<Class<?>> visited, Set<String> accum, boolean recurse,
		Method annotatedMethod) {

		RequiresScope declaredAnnotation =
			annotatedMethod.getDeclaredAnnotation(RequiresScope.class);

		if (declaredAnnotation != null) {
			accum.addAll(Arrays.asList(declaredAnnotation.value()));
		}

		if (isSubResourceLocator(annotatedMethod)) {
			Class<?> returnType = annotatedMethod.getReturnType();

			if (recurse) {
				doFind(visited, accum, false, returnType);
			}
		}
	}

}
