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

import com.liferay.oauth2.provider.scope.ScopeChecker;
import com.liferay.oauth2.provider.scope.spi.application.descriptor.ApplicationDescriptor;
import com.liferay.oauth2.provider.scope.spi.scope.descriptor.ScopeDescriptor;
import com.liferay.oauth2.provider.scope.spi.scope.finder.ScopeFinder;
import com.liferay.oauth2.provider.rest.spi.RequestScopeCheckerFilter;
import com.liferay.oauth2.provider.scope.liferay.api.ScopeContext;
import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ResourceBundleLoader;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.osgi.util.tracker.ServiceTracker;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Feature;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.Arrays;
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
	}
)
@Provider
public class LiferayOAuth2OSGiFeatureRegistrator {

	private BundleContext _bundleContext;
	private ServiceRegistration<Feature> _serviceRegistration;

	@Activate
	protected void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		_bundleContext = bundleContext;

		_serviceRegistration = bundleContext.registerService(
			Feature.class,
			new FeaturePrototypeServiceFactory(
				_bundleContext, _scopeContext, _scopeChecker,
				_defaultRequestScopeChecker, _annotationRequestScopeChecker,
				_defaultScopeDescriptor),
			new Hashtable<>(properties));
	}

	@Deactivate
	protected void deactivate() {
		_serviceRegistration.unregister();
	}

	@Reference(policyOption = ReferencePolicyOption.GREEDY)
	private ScopeContext _scopeContext;

	@Reference(policyOption = ReferencePolicyOption.GREEDY)
	private ScopeChecker _scopeChecker;

	@Reference(
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(type=annotation)"
	)
	private RequestScopeCheckerFilter _annotationRequestScopeChecker;

	@Reference(
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(default=true)"
	)
	private RequestScopeCheckerFilter _defaultRequestScopeChecker;

	@Reference(
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(default=true)"
	)
	private volatile ScopeDescriptor _defaultScopeDescriptor;
}
