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

import com.liferay.oauth2.provider.rest.internal.jaxrs.feature.configuration.ConfigurableScopeCheckerFeatureConfiguration;
import com.liferay.oauth2.provider.scope.ScopeChecker;
import com.liferay.oauth2.provider.scope.spi.scope.finder.ScopeFinder;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Priority;

import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Carlos Sierra Andrés
 */
@Component(
	configurationPid = "com.liferay.oauth2.provider.rest.internal.jaxrs.feature.configuration.ConfigurableScopeCheckerFeatureConfiguration",
	configurationPolicy = ConfigurationPolicy.REQUIRE,
	property = {
		"osgi.jaxrs.extension=true",
		"osgi.jaxrs.extension.select=(osgi.jaxrs.name=Liferay.OAuth2)",
		"osgi.jaxrs.name=Liferay.OAuth2.HTTP.configurable.request.checker"
	},
	scope = ServiceScope.PROTOTYPE
)
@Priority(Priorities.AUTHORIZATION - 8)
@Provider
public class ConfigurableScopeCheckerFeature implements Feature {

	@Activate
	public void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		_bundleContext = bundleContext;

		ConfigurableScopeCheckerFeatureConfiguration
			configurableCheckerFeatureConfiguration =
				ConfigurableUtil.createConfigurable(
					ConfigurableScopeCheckerFeatureConfiguration.class,
					properties);

		_allowUnmatched =
			configurableCheckerFeatureConfiguration.allowUnmatched();

		for (String pattern :
				configurableCheckerFeatureConfiguration.patterns()) {

			String[] split = pattern.split("::");

			if (split.length != 3) {
				_log.error(
					StringBundler.concat(
						"Incorrect syntax of ", pattern,
						" 3 sequences of :: are expected"));

				return;
			}

			String methodPatternString = split[0];
			String urlPatternString = split[1];
			String scopesString = split[2];

			String[] scopes = scopesString.split(StringPool.COMMA);

			try {
				_checkPatterns.add(
					new CheckPattern(
						Pattern.compile(methodPatternString),
						Pattern.compile(urlPatternString), scopes));
			}
			catch (PatternSyntaxException pse) {
				_log.error("Invalid pattern in " + pattern, pse);

				throw new IllegalArgumentException(pse);
			}
		}
	}

	@Override
	public boolean configure(FeatureContext context) {
		if (_checkPatterns.isEmpty()) {
			return false;
		}

		Map<Class<?>, Integer> contracts = new HashMap<>();

		contracts.put(
			ContainerRequestFilter.class, Priorities.AUTHORIZATION - 8);

		context.register(
			new ConfigurableCheckerContainerRequestFilter(), contracts);

		Configuration configuration = context.getConfiguration();

		Stream<CheckPattern> stream = _checkPatterns.stream();

		_serviceRegistration = _bundleContext.registerService(
			ScopeFinder.class,
			new CollectionScopeFinder(
				stream.flatMap(
					c -> Arrays.stream(c.getScopes())
				).filter(
					Validator::isNotNull
				).collect(
					Collectors.toSet()
				)
			),
			buildProperties(configuration));

		return true;
	}

	protected Dictionary<String, Object> buildProperties(
		Configuration configuration) {

		HashMapDictionary<String, Object> properties =
			new HashMapDictionary<>();

		properties.putAll(
			(Map<String, Object>)configuration.getProperty(
				"osgi.jaxrs.application.serviceProperties"));

		properties.put(Constants.SERVICE_RANKING, Integer.MIN_VALUE);

		return properties;
	}

	@Deactivate
	protected void deactivate() {
		if (_serviceRegistration != null) {
			_serviceRegistration.unregister();
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ConfigurableScopeCheckerFeature.class);

	private boolean _allowUnmatched;
	private BundleContext _bundleContext;
	private final List<CheckPattern> _checkPatterns = new ArrayList<>();

	@Reference
	private ScopeChecker _scopeChecker;

	private ServiceRegistration<ScopeFinder> _serviceRegistration;

	private static class CheckPattern {

		public CheckPattern(
			Pattern methodPattern, Pattern urlPattern, String[] scopes) {

			_methodPatternPredicate = methodPattern.asPredicate();
			_urlPatternPredicate = urlPattern.asPredicate();
			_scopes = scopes;
		}

		public Predicate<String> getMethodPatternPredicate() {
			return _methodPatternPredicate;
		}

		public String[] getScopes() {
			return _scopes;
		}

		public Predicate<String> getUrlPatternPredicate() {
			return _urlPatternPredicate;
		}

		private final Predicate<String> _methodPatternPredicate;
		private final String[] _scopes;
		private final Predicate<String> _urlPatternPredicate;

	}

	private class ConfigurableCheckerContainerRequestFilter
		implements ContainerRequestFilter {

		@Override
		public void filter(ContainerRequestContext containerRequestContext) {
			Request request = containerRequestContext.getRequest();

			String path = StringPool.SLASH + _uriInfo.getPath();

			for (CheckPattern checkPattern : _checkPatterns) {
				if (!matches(checkPattern, path, request)) {
					continue;
				}

				String[] scopes = checkPattern.getScopes();

				if (requiresNoScope(scopes)) {
					_logDebug(
						"Path  ", path,
						" was approved, doesn't require any scope");

					return;
				}

				if (_scopeChecker.checkAllScopes(scopes)) {
					_logDebug(
						"Path ", path,
						" was approved, token includes all scopes ",
						StringUtil.merge(scopes));

					return;
				}
			}

			if (!_allowUnmatched) {
				_logDebug(
					"Path ", path, " was disallowed, doesn't match ",
					"any pattern");

				abortRequest(containerRequestContext);
			}
			else {
				_logDebug(
					"Path ", path, " was approved, doesn't match any pattern");
			}
		}

		protected void abortRequest(
			ContainerRequestContext containerRequestContext) {

			containerRequestContext.abortWith(
				Response.status(
					403
				).build());
		}

		protected boolean matches(
			CheckPattern checkPattern, String path, Request request) {

			Predicate<String> urlPatternPredicate =
				checkPattern.getUrlPatternPredicate();

			if (!urlPatternPredicate.test(path)) {
				return false;
			}

			Predicate<String> methodPatternPredicate =
				checkPattern.getMethodPatternPredicate();

			if (!methodPatternPredicate.test(request.getMethod())) {
				return false;
			}

			return true;
		}

		protected boolean requiresNoScope(String[] scopes) {
			if (ArrayUtil.isEmpty(scopes)) {
				return true;
			}

			if (scopes.length == 1) {
				if (Validator.isNull(scopes[0])) {
					return true;
				}
			}

			return false;
		}

		private void _logDebug(String... message) {
			if (!_log.isDebugEnabled()) {
				return;
			}

			_log.debug(new StringBundler(message));
		}

		@Context
		private UriInfo _uriInfo;

	}

}