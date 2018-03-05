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

package com.liferay.oauth2.provider.scope.impl.feature;

import com.liferay.oauth2.provider.rest.spi.RequestScopeCheckerFilter;
import com.liferay.oauth2.provider.scope.ScopeChecker;
import com.liferay.oauth2.provider.scope.impl.jaxrs.CompanyRetrieverContainerRequestFilter;
import com.liferay.oauth2.provider.scope.impl.jaxrs.RunnableExecutorContainerResponseFilter;
import com.liferay.oauth2.provider.scope.impl.jaxrs.ScopedRequestScopeChecker;
import com.liferay.oauth2.provider.scope.liferay.api.ScopeContext;
import com.liferay.oauth2.provider.scope.spi.application.descriptor.ApplicationDescriptor;
import com.liferay.oauth2.provider.scope.spi.scope.descriptor.ScopeDescriptor;
import com.liferay.oauth2.provider.scope.spi.scope.finder.ScopeFinder;
import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ResourceBundleLoader;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.PrototypeServiceFactory;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

class FeaturePrototypeServiceFactory
	implements PrototypeServiceFactory<Feature> {

	private BundleContext _bundleContext;
	private ScopeContext _scopeContext;
	private ScopeChecker _scopeChecker;
	private RequestScopeCheckerFilter _defaultRequestScopeChecker;
	private RequestScopeCheckerFilter _annotationRequestScopeChecker;
	private ScopeDescriptor _defaultScopeDescriptor;

	public FeaturePrototypeServiceFactory(
		BundleContext bundleContext, ScopeContext scopeContext,
		ScopeChecker scopeChecker,
		RequestScopeCheckerFilter defaultRequestScopeChecker,
		RequestScopeCheckerFilter annotationRequestScopeChecker,
		ScopeDescriptor defaultScopeDescriptor) {

		_bundleContext = bundleContext;
		_scopeContext = scopeContext;
		_scopeChecker = scopeChecker;
		_defaultRequestScopeChecker = defaultRequestScopeChecker;
		_annotationRequestScopeChecker = annotationRequestScopeChecker;
		_defaultScopeDescriptor = defaultScopeDescriptor;
	}

	@Override
	public Feature getService(
		Bundle bundle, ServiceRegistration<Feature> registration) {

		return new Oauth2Feature(bundle);
	}


	@Override
	public void ungetService(
		Bundle bundle, ServiceRegistration<Feature> registration,
		Feature service) {

		Oauth2Feature oauth2Feature = (Oauth2Feature)service;

		oauth2Feature.close();
	}

	private class Oauth2Feature implements Feature {

		@Context
		private Application _application;

		private Bundle _bundle;
		private Collection<ServiceRegistration<?>> _serviceRegistrations =
			new ArrayList<>();
		private Collection<ServiceTracker<?, ?>> _serviceTrackers =
			new ArrayList<>();

		public Oauth2Feature(Bundle bundle) {
			_bundle = bundle;
		}

		protected void processRequestOperationStrategy(
			Dictionary<String, Object> serviceProperties) {

			_serviceRegistrations.add(
				_bundleContext.registerService(
					ScopeFinder.class,
					new CollectionScopeFinder(
						Arrays.asList(
							HttpMethod.DELETE, HttpMethod.GET, HttpMethod.HEAD,
							HttpMethod.OPTIONS, HttpMethod.POST,
							HttpMethod.PUT)),
					serviceProperties));
		}

		protected void processAnnotationStrategy(
			Dictionary<String, Object> serviceProperties,
			Collection<String> scopes) {

			_serviceRegistrations.add(
				_bundleContext.registerService(
					ScopeFinder.class, new CollectionScopeFinder(scopes),
					serviceProperties));

			_serviceRegistrations.add(
				_bundleContext.registerService(
					RequestScopeCheckerFilter.class,
					_annotationRequestScopeChecker,
					serviceProperties));
		}


		@Override
		public boolean configure(FeatureContext context) {
			Configuration configuration = context.getConfiguration();

			Map<String, Object> applicationProperties =
				(Map<String, Object>) configuration.getProperty(
					"osgi.jaxrs.application.serviceProperties");

			context.register(
				new CompanyRetrieverContainerRequestFilter(
					_scopeContext::setCompany),
				Priorities.AUTHORIZATION - 10);

			context.register(
				new ScopeContextContainerRequestFilter(_scopeContext),
				Priorities.AUTHORIZATION - 9);

			context.register(
				new ScopedRequestScopeChecker(
					_bundleContext, _scopeChecker,
					() -> _defaultRequestScopeChecker),
				Priorities.AUTHORIZATION - 8);

			context.register(
				new RunnableExecutorContainerResponseFilter(
					_scopeContext::clear),
				Priorities.AUTHORIZATION - 8);

			Object oauth2ScopeCheckerTypeObject =
				applicationProperties.get("oauth2.scopechecker.type");

			String oauth2ScopeCheckerType;

			if (oauth2ScopeCheckerTypeObject == null) {
				oauth2ScopeCheckerType = "request.operation";
			}
			else {
				oauth2ScopeCheckerType =
					oauth2ScopeCheckerTypeObject.toString();
			}

			Dictionary<String, Object> serviceProperties =
				new Hashtable<>();
			for (String property : applicationProperties.keySet()) {
				if (property.startsWith("service.")) {
					continue;
				}
				serviceProperties.put(
					property, applicationProperties.get(property));
			}

			if (serviceProperties.get("osgi.jaxrs.name") == null) {
				Class<? extends Application> applicationClass =
					_application.getClass();

				serviceProperties.put(
					"osgi.jaxrs.name", applicationClass.getName());
			}

			String applicationName =
				serviceProperties.get("osgi.jaxrs.name").toString();

			if (oauth2ScopeCheckerType.equals("request.operation")) {
				processRequestOperationStrategy(serviceProperties);
			}

			if (oauth2ScopeCheckerType.equals("annotations")) {
				HashSet<String> scopes = new HashSet<>();

				context.register(
					(DynamicFeature) (resourceInfo, __) ->
						scopes.addAll(
							ScopeAnnotationFinder.find(
								resourceInfo.getResourceClass())));

				processAnnotationStrategy(serviceProperties, scopes);
			}

			registerDescriptors(applicationName);

			return true;
		}

		private void registerDescriptors(String applicationName) {
			String bundleSymbolicName = _bundle.getSymbolicName();

			ServiceTracker<ResourceBundleLoader, ResourceBundleLoader>
				serviceTracker = ServiceTrackerFactory.open(
				_bundleContext,
				"(&(objectClass=" + ResourceBundleLoader.class.getName() +
				")(bundle.symbolic.name=" + bundleSymbolicName + ")" +
				"(resource.bundle.base.name=content.Language))");

			_serviceTrackers.add(serviceTracker);

			_serviceRegistrations.add(
				_bundleContext.registerService(
					new String[]{
						ScopeDescriptor.class.getName(),
						ApplicationDescriptor.class.getName()
					},
					new ApplicationDescriptorsImpl(
						serviceTracker, applicationName),
					new Hashtable<String, Object>() {{
						put("osgi.jaxrs.name", applicationName);
					}}));
		}

		private void close() {
			for (ServiceRegistration<?> serviceRegistration :
				_serviceRegistrations) {

				serviceRegistration.unregister();
			}
			for (ServiceTracker<?, ?> serviceTracker : _serviceTrackers) {
				serviceTracker.close();
			}
		}

	}

	private class ApplicationDescriptorsImpl
		implements ScopeDescriptor, ApplicationDescriptor {

		private final ServiceTracker<?, ResourceBundleLoader>
			_serviceTracker;
		private String _applicationClassName;

		public ApplicationDescriptorsImpl(
			ServiceTracker<?, ResourceBundleLoader> serviceTracker,
			String applicationClassName) {

			_serviceTracker = serviceTracker;
			_applicationClassName = applicationClassName;
		}

		@Override
		public String describeScope(String scope, Locale locale) {
			ResourceBundleLoader resourceBundleLoader =
				_serviceTracker.getService();

			if (resourceBundleLoader == null) {
				return _defaultScopeDescriptor.describeScope(scope, locale);
			}

			ResourceBundle resourceBundle =
				resourceBundleLoader.loadResourceBundle(
					LocaleUtil.toLanguageId(locale));

			String key = "oauth2.scope." + scope;

			if (!resourceBundle.containsKey(key)) {
				return _defaultScopeDescriptor.describeScope(
					scope, locale);
			}

			return resourceBundle.getString(key);
		}

		@Override
		public String describeApplication(Locale locale) {
			ResourceBundleLoader resourceBundleLoader =
				_serviceTracker.getService();

			if (resourceBundleLoader == null) {
				return _applicationClassName;
			}

			ResourceBundle resourceBundle =
				resourceBundleLoader.loadResourceBundle(
					LocaleUtil.toLanguageId(locale));

			String key = "oauth2.application.description." +
				 _applicationClassName;

			return resourceBundle.getString(key);
		}
	}

}
