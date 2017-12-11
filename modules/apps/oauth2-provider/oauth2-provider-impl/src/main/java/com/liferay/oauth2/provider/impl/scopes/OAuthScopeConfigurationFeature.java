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

import com.liferay.oauth2.provider.api.scopes.OAuth2Scope;
import com.liferay.oauth2.provider.api.scopes.RequiresScope;
import com.liferay.oauth2.provider.api.scopes.ScopeFinder;
import com.liferay.portal.kernel.util.ResourceBundleLoader;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.ServiceScope;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

@Component(
	scope = ServiceScope.PROTOTYPE,
	service = Feature.class
)
public class OAuthScopeConfigurationFeature implements Feature, DynamicFeature {

	private BundleContext _bundleContext;
	private Collection<OAuth2Scope> _oAuth2Scopes;
	private Set<String> _scopeNames;
	private ServiceRegistration<ScopeFinder>
		_serviceRegistration;
	private ResourceBundleLoader _resourceBundleLoader;

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;
		_oAuth2Scopes = new CopyOnWriteArrayList<>();
		_scopeNames = new HashSet<>();
	}

	@Override
	public boolean configure(FeatureContext context) {
		Configuration configuration = context.getConfiguration();

		Map<String, Object> properties = configuration.getProperties();

		Map<String, Object> serviceProperties = (Map<String, Object>)
			properties.get("osgi.jaxrs.application.serviceProperties");

		_resourceBundleLoader = (ResourceBundleLoader) serviceProperties.get(
			"com.liferay.oauth2.provider.resource.bundle.loader");

		_serviceRegistration = _bundleContext.registerService(
			ScopeFinder.class,
			name -> _oAuth2Scopes,
			new Hashtable<>(serviceProperties)
		);

		return true;
	}

	@Override
	public void configure(ResourceInfo resourceInfo, FeatureContext context) {
		Method resourceMethod =
			resourceInfo.getResourceMethod();

		RequiresScope requiresScope = resourceMethod.getAnnotation(
			RequiresScope.class);

		if (requiresScope == null) {
			return;
		}

		String value = requiresScope.value();

		if (_scopeNames.add(value)) {
			return;
		}

		_oAuth2Scopes.add(new OAuth2Scope() {
			@Override
			public String getLocalName() {
				return value;
			}

			@Override
			public String getDescription(Locale locale) {
				return _resourceBundleLoader.loadResourceBundle(locale).getString(value);
			}
		});
	}

	@Deactivate
	protected void deactivate() {
		if (_serviceRegistration != null) {
			_serviceRegistration.unregister();
		}
	}

}
