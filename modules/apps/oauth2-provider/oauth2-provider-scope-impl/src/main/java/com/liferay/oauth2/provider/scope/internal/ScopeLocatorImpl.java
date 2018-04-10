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

package com.liferay.oauth2.provider.scope.internal;

import com.liferay.oauth2.provider.scope.internal.model.LiferayOAuth2ScopeImpl;
import com.liferay.oauth2.provider.scope.liferay.LiferayOAuth2Scope;
import com.liferay.oauth2.provider.scope.liferay.ScopeLocator;
import com.liferay.oauth2.provider.scope.liferay.ScopedServiceTrackerMap;
import com.liferay.oauth2.provider.scope.liferay.ScopedServiceTrackerMapFactory;
import com.liferay.oauth2.provider.scope.spi.prefix.handler.PrefixHandler;
import com.liferay.oauth2.provider.scope.spi.prefix.handler.PrefixHandlerFactory;
import com.liferay.oauth2.provider.scope.spi.scope.finder.ScopeFinder;
import com.liferay.oauth2.provider.scope.spi.scope.mapper.ScopeMapper;
import com.liferay.oauth2.provider.scope.spi.scope.matcher.ScopeMatcher;
import com.liferay.oauth2.provider.scope.spi.scope.matcher.ScopeMatcherFactory;
import com.liferay.osgi.service.tracker.collections.ServiceReferenceServiceTuple;
import com.liferay.osgi.service.tracker.collections.map.PropertyServiceReferenceMapper;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapListener;
import com.liferay.petra.string.StringBundler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Carlos Sierra Andrés
 */
@Component(immediate = true, service = ScopeLocator.class)
public class ScopeLocatorImpl implements ScopeLocator {

	@Override
	public Collection<LiferayOAuth2Scope> getLiferayOAuth2Scopes(
		long companyId, String scopesAlias) {

		return _getFromCacheOr(
			StringBundler.concat(
				"getLiferayOAuth2Scopes", Long.toString(companyId),
				scopesAlias),
			() -> _getLiferayOAuth2Scopes(companyId, scopesAlias));
	}

	@Override
	public Collection<LiferayOAuth2Scope> getLiferayOAuth2Scopes(
		long companyId, String scopesAlias, String applicationName) {

		return _getFromCacheOr(
			StringBundler.concat(
				"getLiferayOAuth2Scopes", Long.toString(companyId),
				applicationName, scopesAlias),
			() -> _getLiferayOAuth2Scopes(
				companyId, scopesAlias, applicationName));
	}

	@Override
	public Collection<String> getScopeAliases(long companyId) {
		return _getFromCacheOr(
			StringBundler.concat("getScopeAliases", Long.toString(companyId)),
			() -> _getScopesAliases(companyId));
	}

	@Override
	public Collection<String> getScopeAliases(
		long companyId, String applicationName) {

		return _getFromCacheOr(
			StringBundler.concat(
				"getScopeAliases", Long.toString(companyId), applicationName),
			() -> _getScopesAliases(companyId, applicationName));
	}

	public void setScopedScopeMapper(
		ScopedServiceTrackerMap<ScopeMapper> scopedScopeMapper) {

		_scopedScopeMapper = scopedScopeMapper;
	}

	public void setScopedScopeMatcherFactories(
		ServiceTrackerMap<String, ScopeMatcherFactory>
			scopedScopeMatcherFactories) {

		_scopedScopeMatcherFactories = scopedScopeMatcherFactories;
	}

	public void setScopeFinderByNameServiceTrackerMap(
		ServiceTrackerMap
			<String, List<ServiceReferenceServiceTuple<?, ScopeFinder>>>
				scopeFinderByNameServiceTrackerMap) {

		_scopeFinderByNameServiceTrackerMap =
			scopeFinderByNameServiceTrackerMap;
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		setScopeFinderByNameServiceTrackerMap(
			ServiceTrackerMapFactory.openMultiValueMap(
				bundleContext, ScopeFinder.class, "(osgi.jaxrs.name=*)",
				new PropertyServiceReferenceMapper<>("osgi.jaxrs.name"),
				new ScopeFinderServiceTupleServiceTrackerCustomizer(
					bundleContext),
				Comparator.naturalOrder(),
				new ClearCacheServiceTrackerMapListener()));

		setScopedPrefixHandlerFactories(
			_scopedServiceTrackerMapFactory.create(
				bundleContext, PrefixHandlerFactory.class, "osgi.jaxrs.name",
				() -> {
					if (_defaultPrefixHandlerFactory != null) {
						return _defaultPrefixHandlerFactory;
					}
					else {
						return propertyAccessor ->
							PrefixHandler.PASSTHROUGH_PREFIXHANDLER;
					}
				},
				_invocationResultCache::clear));

		setScopedScopeFinders(
			_scopedServiceTrackerMapFactory.create(
				bundleContext, ScopeFinder.class, "osgi.jaxrs.name", null,
				_invocationResultCache::clear));

		setScopedScopeMapper(
			_scopedServiceTrackerMapFactory.create(
				bundleContext, ScopeMapper.class, "osgi.jaxrs.name",
				() -> {
					if (_defaultScopeMapper != null) {
						return _defaultScopeMapper;
					}
					else {
						return ScopeMapper.PASSTHROUGH_SCOPEMAPPER;
					}
				},
				_invocationResultCache::clear));

		setScopedScopeMatcherFactories(
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext, ScopeMatcherFactory.class, "company.id"));
	}

	@Deactivate
	protected void deactivate() {
		_scopeFinderByNameServiceTrackerMap.close();
		_scopedPrefixHandlerFactories.close();
		_scopedScopeFinders.close();
		_scopedScopeMatcherFactories.close();
		_scopedScopeMapper.close();
	}

	@Reference(
		cardinality = ReferenceCardinality.OPTIONAL,
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(osgi.jaxrs.name=Default)", unbind = "-"
	)
	protected void setDefaultPrefixHandlerFactory(
		PrefixHandlerFactory defaultPrefixHandlerFactory) {

		_defaultPrefixHandlerFactory = defaultPrefixHandlerFactory;
	}

	@Reference(
		cardinality = ReferenceCardinality.OPTIONAL,
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(osgi.jaxrs.name=Default)", unbind = "-"
	)
	protected void setDefaultScopeMapper(ScopeMapper defaultScopeMapper) {
		_defaultScopeMapper = defaultScopeMapper;
	}

	@Reference(name = "default", unbind = "-")
	protected void setDefaultScopeMatcherFactory(
		ScopeMatcherFactory defaultScopeMatcherFactory) {

		_defaultScopeMatcherFactory = defaultScopeMatcherFactory;
	}

	protected void setScopedPrefixHandlerFactories(
		ScopedServiceTrackerMap<PrefixHandlerFactory>
			scopedPrefixHandlerFactories) {

		_scopedPrefixHandlerFactories = scopedPrefixHandlerFactories;
	}

	protected void setScopedScopeFinders(
		ScopedServiceTrackerMap<ScopeFinder> scopedScopeFinders) {

		_scopedScopeFinders = scopedScopeFinders;
	}

	@Reference(unbind = "-")
	protected void setScopedServiceTrackerMapFactory(
		ScopedServiceTrackerMapFactory scopedServiceTrackerMapFactory) {

		_scopedServiceTrackerMapFactory = scopedServiceTrackerMapFactory;
	}

	@SuppressWarnings("unchecked")
	private <T> T _getFromCacheOr(String key, Supplier<T> supplier) {
		Object value = _invocationResultCache.get(key);

		if (value == null) {
			value = supplier.get();

			_invocationResultCache.put(key, value);
		}

		return (T)value;
	}

	private Collection<LiferayOAuth2Scope> _getLiferayOAuth2Scopes(
		long companyId, String scopesAlias) {

		Collection<LiferayOAuth2Scope> liferayOAuth2Scopes = new ArrayList<>();

		Set<String> names = _scopeFinderByNameServiceTrackerMap.keySet();

		for (String name : names) {
			liferayOAuth2Scopes.addAll(
				getLiferayOAuth2Scopes(companyId, scopesAlias, name));
		}

		return liferayOAuth2Scopes;
	}

	private Collection<LiferayOAuth2Scope> _getLiferayOAuth2Scopes(
		long companyId, String scopesAlias, String applicationName) {

		ScopeMatcherFactory scopeMatcherFactory =
			_scopedScopeMatcherFactories.getService(Long.toString(companyId));

		ScopeMatcherFactory finalScopeMatcherFactory;

		if (scopeMatcherFactory == null) {
			finalScopeMatcherFactory = _defaultScopeMatcherFactory;
		}
		else {
			finalScopeMatcherFactory = scopeMatcherFactory;
		}

		List<ServiceReferenceServiceTuple<?, ScopeFinder>> serviceTuples =
			_scopeFinderByNameServiceTrackerMap.getService(applicationName);

		if ((serviceTuples == null) || serviceTuples.isEmpty()) {
			return Collections.emptyList();
		}

		ServiceReferenceServiceTuple<?, ScopeFinder> serviceTuple =
			serviceTuples.get(0);

		ServiceReference<?> serviceReference =
			serviceTuple.getServiceReference();

		PrefixHandlerFactory prefixHandlerFactory =
			_scopedPrefixHandlerFactories.getService(
				companyId, applicationName);

		PrefixHandler prefixHandler = prefixHandlerFactory.create(
			serviceReference::getProperty);

		ScopeFinder scopeFinder = _scopedScopeFinders.getService(
			companyId, applicationName);

		Collection<String> scopes = scopeFinder.findScopes();

		if (scopes.isEmpty()) {
			return Collections.emptyList();
		}

		ScopeMapper scopeMapper = _scopedScopeMapper.getService(
			companyId, applicationName);

		Map<String, Boolean> matchCache = new HashMap<>();
		Collection<LiferayOAuth2Scope> locatedScopes = new ArrayList<>(
			scopes.size());
		Bundle bundle = serviceReference.getBundle();

		for (String scope : scopes) {
			for (String mappedScope : scopeMapper.map(scope)) {
				boolean matched = matchCache.computeIfAbsent(
					mappedScope,
					input -> _scopeMatchesScopesAlias(
						input, finalScopeMatcherFactory, prefixHandler,
						scopesAlias));

				if (matched) {
					locatedScopes.add(
						new LiferayOAuth2ScopeImpl(
							applicationName, bundle, scope));
				}
			}
		}

		return locatedScopes;
	}

	private Collection<String> _getScopesAliases(long companyId) {
		Collection<String> scopesAliases = new HashSet<>();

		Set<String> applicationNames =
			_scopeFinderByNameServiceTrackerMap.keySet();

		for (String applicationName : applicationNames) {
			scopesAliases.addAll(getScopeAliases(companyId, applicationName));
		}

		return scopesAliases;
	}

	private Collection<String> _getScopesAliases(
		long companyId, String applicationName) {

		List<ServiceReferenceServiceTuple<?, ScopeFinder>> tuples =
			_scopeFinderByNameServiceTrackerMap.getService(applicationName);

		if ((tuples == null) || tuples.isEmpty()) {
			return Collections.emptyList();
		}

		ServiceReferenceServiceTuple<?, ScopeFinder> tuple = tuples.get(0);

		ServiceReference<?> serviceReference = tuple.getServiceReference();

		ScopeFinder scopeFinder = _scopedScopeFinders.getService(
			companyId, applicationName);

		Collection<String> scopes = scopeFinder.findScopes();

		PrefixHandlerFactory prefixHandlerFactory =
			_scopedPrefixHandlerFactories.getService(
				companyId, applicationName);

		PrefixHandler prefixHandler = prefixHandlerFactory.create(
			serviceReference::getProperty);

		ScopeMapper scopeMapper = _scopedScopeMapper.getService(
			companyId, applicationName);

		Collection<String> scopesAliases = new ArrayList<>();

		for (String scope : scopes) {
			Set<String> mappedScopes = scopeMapper.map(scope);

			for (String mappedScope : mappedScopes) {
				String externalAlias = prefixHandler.addPrefix(mappedScope);

				scopesAliases.add(externalAlias);
			}
		}

		return scopesAliases;
	}

	private Boolean _scopeMatchesScopesAlias(
		String scope, ScopeMatcherFactory scopeMatcherFactory,
		PrefixHandler prefixHandler, String scopesAlias) {

		String prefixedScope = prefixHandler.addPrefix(scope);

		if (scope.length() > prefixedScope.length()) {
			return false;
		}

		String prefix = prefixedScope.substring(
			0, prefixedScope.length() - scope.length());

		if (!scopesAlias.startsWith(prefix)) {
			return false;
		}

		ScopeMatcher scopeMatcher = scopeMatcherFactory.create(
			scopesAlias.substring(prefix.length()));

		return scopeMatcher.match(scope);
	}

	private PrefixHandlerFactory _defaultPrefixHandlerFactory;
	private ScopeMapper _defaultScopeMapper;
	private ScopeMatcherFactory _defaultScopeMatcherFactory;
	private final ConcurrentMap<String, Object> _invocationResultCache =
		new ConcurrentHashMap<>();
	private ScopedServiceTrackerMap<PrefixHandlerFactory>
		_scopedPrefixHandlerFactories;
	private ScopedServiceTrackerMap<ScopeFinder> _scopedScopeFinders;
	private ScopedServiceTrackerMap<ScopeMapper> _scopedScopeMapper;
	private ServiceTrackerMap<String, ScopeMatcherFactory>
		_scopedScopeMatcherFactories;
	private ScopedServiceTrackerMapFactory _scopedServiceTrackerMapFactory;
	private ServiceTrackerMap<String,
		List<ServiceReferenceServiceTuple<?, ScopeFinder>>>
			_scopeFinderByNameServiceTrackerMap;

	private static class ScopeFinderServiceTupleServiceTrackerCustomizer
		implements
			ServiceTrackerCustomizer
				<ScopeFinder, ServiceReferenceServiceTuple<?, ScopeFinder>> {

		public ScopeFinderServiceTupleServiceTrackerCustomizer(
			BundleContext bundleContext) {

			_bundleContext = bundleContext;
		}

		@Override
		public ServiceReferenceServiceTuple<?, ScopeFinder> addingService(
			ServiceReference<ScopeFinder> reference) {

			ScopeFinder scopeFinder = _bundleContext.getService(reference);

			return new ServiceReferenceServiceTuple<>(reference, scopeFinder);
		}

		@Override
		public void modifiedService(
			ServiceReference<ScopeFinder> reference,
			ServiceReferenceServiceTuple<?, ScopeFinder> tuple) {
		}

		@Override
		public void removedService(
			ServiceReference<ScopeFinder> reference,
			ServiceReferenceServiceTuple<?, ScopeFinder> tuple) {

			_bundleContext.ungetService(reference);
		}

		private final BundleContext _bundleContext;

	}

	private class ClearCacheServiceTrackerMapListener implements
		ServiceTrackerMapListener<String,
			ServiceReferenceServiceTuple<?, ScopeFinder>,
			List<ServiceReferenceServiceTuple<?, ScopeFinder>>> {

		@Override
		public void keyEmitted(
			ServiceTrackerMap<String,
				List<ServiceReferenceServiceTuple<?, ScopeFinder>>>
					serviceTrackerMap,
			String key, ServiceReferenceServiceTuple<?, ScopeFinder> service,
			List<ServiceReferenceServiceTuple<?, ScopeFinder>> content) {

			_invocationResultCache.clear();
		}

		@Override
		public void keyRemoved(
			ServiceTrackerMap<String,
				List<ServiceReferenceServiceTuple<?, ScopeFinder>>>
					serviceTrackerMap,
			String key, ServiceReferenceServiceTuple<?, ScopeFinder> service,
			List<ServiceReferenceServiceTuple<?, ScopeFinder>> content) {

			_invocationResultCache.clear();
		}

	}

}