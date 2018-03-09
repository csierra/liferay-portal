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
import com.liferay.oauth2.provider.scope.spi.scope.finder.ScopeFinder;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;

@Component(
	property = {
		"osgi.jaxrs.extension=true",
		"osgi.jaxrs.name=Liferay.OAuth2.annotations.checker",
		"osgi.jaxrs.extension.select=(osgi.jaxrs.name=Liferay.OAuth2)",
		"osgi.jaxrs.application.select=(&(osgi.jaxrs.extension.select=\\(liferay.extension=OAuth2\\))(oauth2.scopechecker.type=annotations))"
	},
	scope = ServiceScope.PROTOTYPE
)
@Provider
public class AnnotationRequestScopeChecker implements Feature {

	private BundleContext _bundleContext;
	private ServiceRegistration<ScopeFinder>
		_scopeFinderServiceRegistration;

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;
	}


	@Deactivate
	protected void deactivate() {
		if (_scopeFinderServiceRegistration != null) {
			_scopeFinderServiceRegistration.unregister();
		}
	}

	@Reference
	private ScopeChecker _scopeChecker;

	@Override
	public boolean configure(FeatureContext context) {
		Configuration configuration = context.getConfiguration();

		Map<String, Object> applicationProperties =
			(Map<String, Object>) configuration.getProperty(
				"osgi.jaxrs.application.serviceProperties");

		HashSet<String> scopes = new HashSet<>();

		context.register(
			(DynamicFeature) (resourceInfo, __) ->
				scopes.addAll(
					ScopeAnnotationFinder.find(
						resourceInfo.getResourceClass())));

		context.register(
			new AnnotationContainerRequestFilter(),
			Priorities.AUTHORIZATION - 8);

		_scopeFinderServiceRegistration = _bundleContext.registerService(
			ScopeFinder.class, new CollectionScopeFinder(scopes),
			new Hashtable<>(applicationProperties));

		return true;
	}

	private class AnnotationContainerRequestFilter implements ContainerRequestFilter {
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

	}

}