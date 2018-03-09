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

import com.liferay.oauth2.provider.scope.impl.jaxrs.CompanyRetrieverContainerRequestFilter;
import com.liferay.oauth2.provider.scope.impl.jaxrs.RunnableExecutorContainerResponseFilter;
import com.liferay.oauth2.provider.scope.liferay.ScopeContext;
import com.liferay.oauth2.provider.scope.spi.application.descriptor.ApplicationDescriptor;
import com.liferay.oauth2.provider.scope.spi.scope.descriptor.ScopeDescriptor;
import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ResourceBundleLoader;
import com.liferay.portal.servlet.filters.authverifier.AuthVerifierFilter;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.osgi.service.component.annotations.ServiceScope;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;
import org.osgi.util.tracker.ServiceTracker;

import javax.servlet.Filter;
import javax.ws.rs.Priorities;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

@Component(
	property = {
		"osgi.jaxrs.extension=true",
		"osgi.jaxrs.name=Liferay.OAuth2",
		"osgi.jaxrs.application.select=(osgi.jaxrs.extension.select=\\(liferay.extension=OAuth2\\))",
		"liferay.extension=OAuth2"
	},
	scope = ServiceScope.PROTOTYPE
)
@Provider
public class LiferayOAuth2OSGiFeature implements Feature {

	private BundleContext _bundleContext;
	private Bundle _bundle;

	@Activate
	protected void activate(ComponentContext componentContext) {
		_bundle = componentContext.getUsingBundle();

		_bundleContext = componentContext.getBundleContext();
	}

	@Context
	private Application _application;

	private Collection<ServiceRegistration<?>> _serviceRegistrations =
		new ArrayList<>();
	private Collection<ServiceTracker<?, ?>> _serviceTrackers =
		new ArrayList<>();

	@Override
	public boolean configure(FeatureContext context) {
		Configuration configuration = context.getConfiguration();

		Map<String, Object> applicationProperties =
			(Map<String, Object>) configuration.getProperty(
				"osgi.jaxrs.application.serviceProperties");

		if (applicationProperties.get("osgi.jaxrs.name") == null) {
			Class<? extends Application> applicationClass =
				_application.getClass();

			applicationProperties.put(
				"osgi.jaxrs.name", applicationClass.getName());
		}

		String applicationName =
			applicationProperties.get("osgi.jaxrs.name").toString();

		context.register(
			new CompanyRetrieverContainerRequestFilter(
				_scopeContext::setCompanyId),
			Priorities.AUTHORIZATION - 10);

		context.register(
			new RunnableContainerRequestFilter(
				() -> {
					_scopeContext.setBundle(_bundle);
					_scopeContext.setApplicationName(applicationName);
				}
			),
			Priorities.AUTHORIZATION - 9);

		context.register(
			new RunnableExecutorContainerResponseFilter(_scopeContext::clear),
			Priorities.AUTHORIZATION - 8);

		registerDescriptors(applicationName);

		registerAuthVerifierFilter(
			"(" + HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME +
				"=context.for" + applicationName + ")");

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

	@Deactivate
	protected void deactivate() {
		for (ServiceRegistration<?> serviceRegistration :
			_serviceRegistrations) {

			try {
				serviceRegistration.unregister();
			}
			catch (Exception e) {
			}
		}
		for (ServiceTracker<?, ?> serviceTracker : _serviceTrackers) {
			serviceTracker.close();
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

	private void registerAuthVerifierFilter(String contextSelect) {
		Dictionary<String, Object> properties = new Hashtable<>();

		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT,
			contextSelect);
		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_NAME,
			AuthVerifierFilter.class.getName());
		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_PATTERN, "/*");
		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_INIT_PARAM_PREFIX +
				"auth.verifier.OAuth2RestAuthVerifier.urls.includes",
			"*");

		_serviceRegistrations.add(
			_bundleContext.registerService(
				Filter.class, new AuthVerifierFilter(), properties));
	}


	@Reference(policyOption = ReferencePolicyOption.GREEDY)
	private ScopeContext _scopeContext;

	@Reference(
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(default=true)"
	)
	private volatile ScopeDescriptor _defaultScopeDescriptor;

}
