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

package com.liferay.portal.security.auth.verifier.internal.tracker;

import com.liferay.osgi.service.tracker.collections.ServiceTrackerMapBuilder;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerCustomizerFactory;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapListener;
import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.AccessControlContext;
import com.liferay.portal.kernel.security.auth.verifier.AuthVerifier;
import com.liferay.portal.kernel.security.auth.verifier.AuthVerifierConfiguration;
import com.liferay.portal.kernel.security.auth.verifier.AuthVerifierRegistry;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.security.auth.AuthVerifierPipeline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.function.Consumer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import jodd.util.Wildcard;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.http.context.ServletContextHelper;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Stian Sigvartsen
 */
@Component(service = AuthVerifierRegistry.class)
public class AuthVerifierRegistryImpl implements AuthVerifierRegistry {

	public List<AuthVerifierConfiguration> getAuthVerifierConfigurations(
		AccessControlContext accessControlContext) {

		HttpServletRequest httpServletRequest =
			accessControlContext.getRequest();

		List<AuthVerifierConfiguration> authVerifierConfigurations =
			new ArrayList<>();

		String requestURI = httpServletRequest.getRequestURI();

		String contextPath = httpServletRequest.getContextPath();

		requestURI = requestURI.substring(contextPath.length());

		List<AuthVerifierConfiguration> contextPathAuthVerifierConfigurations =
			_authVerifierConfigurations.get(contextPath);

		if (contextPathAuthVerifierConfigurations == null) {
			return authVerifierConfigurations;
		}

		for (AuthVerifierConfiguration authVerifierConfiguration :
				contextPathAuthVerifierConfigurations) {

			authVerifierConfiguration = _mergeAuthVerifierConfiguration(
				authVerifierConfiguration, accessControlContext);

			if (_isMatchingRequestURI(authVerifierConfiguration, requestURI)) {
				authVerifierConfigurations.add(authVerifierConfiguration);
			}
		}

		return authVerifierConfigurations;
	}

	@Activate
	protected void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		_bundleContext = bundleContext;

		ServiceTrackerFactory.create(
			bundleContext,
			StringBundler.concat(
				"((objectClass=",
				ServletContext.class.getName(),
				")", "(osgi.web.contextpath=*))"),
			new ServiceTrackerCustomizer<
				ServletContext, ServiceReference<ServletContext>>() {

				@Override
				public ServiceReference<ServletContext> addingService(
					ServiceReference<ServletContext> reference) {

					_servletContextReferences.add(reference);

					return reference;
				}

				@Override
				public void modifiedService(
					ServiceReference<ServletContext> reference,
					ServiceReference<ServletContext> serviceReference) {

				}

				@Override
				public void removedService(
					ServiceReference<ServletContext> reference,
					ServiceReference<ServletContext> serviceReference) {

					_servletContextReferences.remove(reference);
				}
			});

		ServiceTrackerMapBuilder.SelectorFactory.newSelector(
			bundleContext, AuthVerifier.class
		).newSelector(
			new ServiceTrackerCustomizer<
				AuthVerifier, ServiceTuple<AuthVerifier>>() {

				@Override
				public ServiceTuple<AuthVerifier> addingService(
					ServiceReference<AuthVerifier> reference) {

					return new ServiceTuple<>(
						bundleContext.getService(reference), reference);
				}

				@Override
				public void modifiedService(
					ServiceReference<AuthVerifier> reference,
					ServiceTuple<AuthVerifier> service) {

				}

				@Override
				public void removedService(
					ServiceReference<AuthVerifier> reference,
					ServiceTuple<AuthVerifier> service) {

					bundleContext.ungetService(reference);
				}
			}
		).map(
			"auth.type"
		).collectSingleValue(
		).newCollector(
			new ServiceTrackerMapListener<
				String, ServiceTuple<AuthVerifier>,
				ServiceTuple<AuthVerifier>>() {

				@Override
				public void keyEmitted(
					ServiceTrackerMap<String, ServiceTuple<AuthVerifier>>
						serviceTrackerMap,
					String key, ServiceTuple<AuthVerifier> service,
					ServiceTuple<AuthVerifier> content) {

					if (service.equals(content) ) {
						_authVerifiers.add(service);

						_recalculate();
					}
				}

				@Override
				public void keyRemoved(
					ServiceTrackerMap<String, ServiceTuple<AuthVerifier>>
						serviceTrackerMap,
					String key, ServiceTuple<AuthVerifier> service,
					ServiceTuple<AuthVerifier> content) {

					boolean remove = _authVerifiers.remove(service);

					if (content != null) {
						remove |= _authVerifiers.add(content);
					}

					if (remove) {
						//TODO: recalculate
					}
				}
			}
		);
	}

	private void _recalculate() {
		_authVerifierConfigurations.clear();

		for (ServiceTuple<AuthVerifier> authVerifierServiceTuple :
			_authVerifiers) {

			ServiceReference<AuthVerifier> serviceReference =
				authVerifierServiceTuple.getServiceReference();

			String selectFilterString = GetterUtil.getString(
				serviceReference.getProperty(
					"servlet.context.helper.select.filter"));

			try {
				Filter filter = FrameworkUtil.createFilter(selectFilterString);

				for (ServiceReference<ServletContext> servletContextReference :
					_servletContextReferences) {

					ArrayList<Object> arrayList = new ArrayList<>();

					if (filter.match(servletContextReference)) {
						String webContextPath = GetterUtil.getString(
							servletContextReference.getProperty(
								"osgi.web.contextpath"));

						AuthVerifierConfiguration authVerifierConfiguration =
							new AuthVerifierConfiguration();

						_authVerifierConfigurations.put(
							webContextPath, arrayList);
					}
				}
			}
			catch (InvalidSyntaxException e) {

			}


		}
	}

	private Set<ServiceReference<ServletContext>> _servletContextReferences;
	private Set<ServiceTuple<AuthVerifier>> _authVerifiers;

	@Deactivate
	protected void deactivate() {
		_serviceTracker.close();
	}

	private static boolean _validateAuthVerifierProperties(
		String authVerifierClassName, Properties properties) {

		String[] urlsIncludes = StringUtil.split(
			properties.getProperty("urls.includes"));

		if (urlsIncludes.length == 0) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Auth verifier " + authVerifierClassName +
						" does not have URLs configured");
			}

			return false;
		}

		return true;
	}

	private boolean _isMatchingRequestURI(
		AuthVerifierConfiguration authVerifierConfiguration,
		String requestURI) {

		Properties properties = authVerifierConfiguration.getProperties();

		String[] urlsExcludes = StringUtil.split(
			properties.getProperty("urls.excludes"));

		if ((urlsExcludes.length > 0) &&
			(Wildcard.matchOne(requestURI, urlsExcludes) > -1)) {

			return false;
		}

		String[] urlsIncludes = StringUtil.split(
			properties.getProperty("urls.includes"));

		if (urlsIncludes.length == 0) {
			return false;
		}

		if (Wildcard.matchOne(requestURI, urlsIncludes) > -1) {
			return true;
		}

		return false;
	}

	private AuthVerifierConfiguration _mergeAuthVerifierConfiguration(
		AuthVerifierConfiguration authVerifierConfiguration,
		AccessControlContext accessControlContext) {

		Map<String, Object> settings = accessControlContext.getSettings();

		String authVerifierSettingsKey =
			AuthVerifierPipeline.getAuthVerifierPropertyName(
				authVerifierConfiguration.getAuthVerifierClassName());

		boolean merge = false;

		Set<String> settingsKeys = settings.keySet();

		Iterator<String> iterator = settingsKeys.iterator();

		while (iterator.hasNext() && !merge) {
			String settingsKey = iterator.next();

			if (settingsKey.startsWith(authVerifierSettingsKey) &&
				(settings.get(settingsKey) instanceof String)) {

				merge = true;
			}
		}

		if (!merge) {
			return authVerifierConfiguration;
		}

		AuthVerifierConfiguration mergedAuthVerifierConfiguration =
			new AuthVerifierConfiguration();

		mergedAuthVerifierConfiguration.setAuthVerifier(
			authVerifierConfiguration.getAuthVerifier());

		Properties mergedProperties = new Properties(
			authVerifierConfiguration.getProperties());

		for (Map.Entry<String, Object> entry : settings.entrySet()) {
			String settingsKey = entry.getKey();

			if (settingsKey.startsWith(authVerifierSettingsKey)) {
				Object settingsValue = entry.getValue();

				if (settingsValue instanceof String) {
					String propertiesKey = settingsKey.substring(
						authVerifierSettingsKey.length());

					mergedProperties.setProperty(
						propertiesKey, (String)settingsValue);
				}
			}
		}

		mergedAuthVerifierConfiguration.setProperties(mergedProperties);

		return mergedAuthVerifierConfiguration;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AuthVerifierRegistryImpl.class);

	private final Map<String, List<AuthVerifierConfiguration>>
		_authVerifierConfigurations = new HashMap<>();
	private BundleContext _bundleContext;

	@Reference
	private Portal _portal;

	private ServiceTracker<AuthVerifier, AuthVerifier>
		_serviceTracker;


	private static class ServiceTuple<T> {
		private final T _service;
		private final ServiceReference<T> _serviceReference;

		public T getService() {
			return _service;
		}

		public ServiceReference<T> getServiceReference() {
			return _serviceReference;
		}

		public ServiceTuple(
			T service, ServiceReference<T> serviceReference) {
			_service = service;
			_serviceReference = serviceReference;
		}

		@Override
		public boolean equals(Object object) {
			if (this == object) {
				return true;
			}
			if (object == null || getClass() != object.getClass()) {
				return false;
			}

			ServiceTuple<?> serviceTuple = (ServiceTuple<?>) object;

			return _serviceReference.equals(serviceTuple._serviceReference);
		}

		@Override
		public int hashCode() {
			return Objects.hash(_serviceReference);
		}
	}

}