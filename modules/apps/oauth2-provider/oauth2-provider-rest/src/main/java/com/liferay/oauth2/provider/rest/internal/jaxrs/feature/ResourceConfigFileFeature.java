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

package com.liferay.oauth2.provider.rest.internal.jaxrs.feature;

import com.liferay.oauth2.provider.rest.spi.scope.checker.container.request.filter.BaseScopeCheckerContainerRequestFilter;
import com.liferay.oauth2.provider.scope.ScopeChecker;
import com.liferay.oauth2.provider.scope.spi.scope.finder.ScopeFinder;
import com.liferay.petra.string.StringPool;
import com.liferay.petra.string.StringUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.lang.reflect.Method;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Carlos Sierra Andrés
 */
@Component(
	property = {
		"osgi.jaxrs.application.select=(|(oauth2.scope.checker.type=resource.config)(oauth2.scopechecker.type=resource.config))",
		"osgi.jaxrs.extension=true",
		"osgi.jaxrs.extension.select=(osgi.jaxrs.name=Liferay.OAuth2)",
		"osgi.jaxrs.name=Liferay.OAuth2.Resource.Config.checker"
	},
	scope = ServiceScope.PROTOTYPE, service = Feature.class
)
@Provider
public class ResourceConfigFileFeature implements Feature {

	@Override
	public boolean configure(FeatureContext context) {
		context.register(new ResourceConfigDynamicFeature());

		Configuration configuration = context.getConfiguration();

		Map<String, Object> applicationProperties =
			(Map<String, Object>)configuration.getProperty(
				"osgi.jaxrs.application.serviceProperties");

		_serviceRegistration = _bundleContext.registerService(
			ScopeFinder.class, new CollectionScopeFinder(_scopes),
			new Hashtable<>(applicationProperties));

		return true;
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		_scopes = new HashSet<>();
	}

	@Deactivate
	protected void deactivate() {
		if (_serviceRegistration != null) {
			_serviceRegistration.unregister();
		}
	}

	private static final ContainerRequestFilter
		_FORBIDDEN_CONTAINER_REQUEST_FILTER =
			containerRequestContext -> containerRequestContext.abortWith(
				ResourceConfigFileFeature._FORBIDDEN_RESPONSE);

	private static final Response _FORBIDDEN_RESPONSE = Response.status(
		Response.Status.FORBIDDEN
	).build();

	private BundleContext _bundleContext;
	private final Map<String, Map<String, String>> _resourceClassMethodScope =
		new HashMap<>();

	@Reference
	private ScopeChecker _scopeChecker;

	private Set<String> _scopes;
	private ServiceRegistration<ScopeFinder> _serviceRegistration;

	private class ResourceConfigDynamicFeature implements DynamicFeature {

		@Override
		public void configure(
			ResourceInfo resourceInfo, FeatureContext featureContext) {

			Class<?> resourceClass = resourceInfo.getResourceClass();

			Map<String, String> methodScopes = getMethodScopes(resourceClass);

			if (methodScopes == null) {
				featureContext.register(_FORBIDDEN_CONTAINER_REQUEST_FILTER);

				return;
			}

			Method resourceMethod = resourceInfo.getResourceMethod();

			String scope = methodScopes.get(resourceMethod.toString());

			if (scope == null) {
				featureContext.register(_FORBIDDEN_CONTAINER_REQUEST_FILTER);

				return;
			}

			_scopes.add(scope);

			featureContext.register(
				new BaseScopeCheckerContainerRequestFilter() {

					@Override
					protected boolean isContainerRequestContextAllowed(
						ContainerRequestContext containerRequestContext) {

						return _scopeChecker.checkScope(scope);
					}

				});
		}

		protected Map<String, String> getMethodScopes(Class<?> resourceClass) {
			String resourceClassName = resourceClass.getName();

			Map<String, String> methodScopes = _resourceClassMethodScope.get(
				resourceClassName);

			if (methodScopes != null) {
				return methodScopes;
			}

			InputStream inputStream = null;
			Class<?> currentClass = resourceClass;

			while ((inputStream == null) &&
				   !currentClass.equals(Object.class)) {

				inputStream = currentClass.getResourceAsStream(
					currentClass.getSimpleName() + ".oauth2.scopes");

				currentClass = currentClass.getSuperclass();
			}

			if (inputStream == null) {
				return null;
			}

			BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));

			Stream<String> stream = bufferedReader.lines();

			methodScopes = stream.filter(
				line -> line.indexOf(StringPool.COLON) > 1
			).map(
				line -> StringUtil.split(line, ':')
			).collect(
				Collectors.toMap(list -> list.get(0), list -> list.get(1))
			);

			_resourceClassMethodScope.put(resourceClassName, methodScopes);

			return methodScopes;
		}

	}

}