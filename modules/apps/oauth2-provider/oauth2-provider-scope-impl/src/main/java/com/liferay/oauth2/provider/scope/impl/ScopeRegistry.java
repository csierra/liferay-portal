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

package com.liferay.oauth2.provider.scope.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

import com.liferay.oauth2.provider.scope.liferay.ScopedServiceTrackerMap;
import com.liferay.oauth2.provider.scope.liferay.ScopedServiceTrackerMapFactory;
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

import com.liferay.oauth2.provider.scope.impl.model.LiferayOAuth2ScopeImpl;
import com.liferay.oauth2.provider.scope.liferay.LiferayOAuth2Scope;
import com.liferay.oauth2.provider.scope.liferay.ScopeLocator;
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

@Component(immediate = true, service = ScopeLocator.class)
public class ScopeRegistry implements ScopeLocator {

	private ConcurrentMap<String, Object> _invocationCache =
		new ConcurrentHashMap<>();
	private ScopedServiceTrackerMap<ScopeFinder> _scopedScopeFinders;
	private ServiceTrackerMap<String, ScopeMatcherFactory>
		_scopedScopeMatcherFactories;

	private Collection<LiferayOAuth2Scope> _doLocateScopes(
		long companyId, String scopesAlias) {

		Collection<LiferayOAuth2Scope> grants = new ArrayList<>();

		Set<String> applicationNames =
			_scopeFinderByNameServiceTrackerMap.keySet();

		for (String applicationName : applicationNames) {
			grants.addAll(
				getLiferayOAuth2Scopes(
					companyId, scopesAlias, applicationName));
		}

		return grants;
	}

	private Collection<LiferayOAuth2Scope> _doLocateScopesForApplication(
		long companyId, String scopesAlias, String applicationName) {

		ScopeMatcherFactory scopeMatcherFactory = 
			Optional.ofNullable(
				_scopedScopeMatcherFactories.getService(Long.toString(companyId))
			).orElse(_defaultScopeMatcherFactory);
		
		List<ServiceReferenceServiceTuple<?, ScopeFinder>> tuples =
			_scopeFinderByNameServiceTrackerMap.getService(applicationName);

		if (tuples == null || tuples.isEmpty()) {
			return Collections.emptyList();
		}

		ServiceReferenceServiceTuple<?, ScopeFinder> tuple = tuples.get(0);

		ServiceReference<?> serviceReference = tuple.getServiceReference();

		PrefixHandlerFactory prefixHandlerFactory =
			_scopedPrefixHandlerFactories.getService(companyId, applicationName);

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
		Collection<LiferayOAuth2Scope> locatedScopes = 
			new ArrayList<>(scopes.size());
		Bundle bundle = serviceReference.getBundle();

		for (String scope : scopes) {
			for (String mappedScope : scopeMapper.map(scope)) {
			
				boolean matched = 
					matchCache.computeIfAbsent(
						mappedScope, 
						input -> 
							scopeMatchesScopesAlias(
								input, scopeMatcherFactory, 
								prefixHandler, scopesAlias)
					);
				
				if (matched) {
					locatedScopes.add(
						new LiferayOAuth2ScopeImpl(
							applicationName, bundle, scope));
				}
			}
		}

		return locatedScopes;
	}

	private Boolean scopeMatchesScopesAlias(
			String scope, ScopeMatcherFactory scopeMatcherFactory,
			PrefixHandler prefixHandler, String scopesAlias) {
		
		String prefixedMappedScope = 
			prefixHandler.addPrefix(scope);
		
		String prefix = 
			prefixedMappedScope.substring(
				0, prefixedMappedScope.length() - scope.length());

		if (!scopesAlias.startsWith(prefix)) {
			return false;
		}
		
		scopesAlias = scopesAlias.substring(prefix.length());
		
		ScopeMatcher scopeMatcher = 
			scopeMatcherFactory.create(scopesAlias);
			
		return scopeMatcher.match(scope);
	}

	private ScopedServiceTrackerMap<PrefixHandlerFactory>
		_scopedPrefixHandlerFactories;
	private ScopedServiceTrackerMap<ScopeMapper>
		_scopedScopeMapper;

	private Collection<String> _doListScopesAliases(long companyId) {
		Collection<String> scopesAliases = new HashSet<>();

		Set<String> applicationNames =
			_scopeFinderByNameServiceTrackerMap.keySet();

		for (String applicationName : applicationNames) {
			scopesAliases.addAll(
				getScopeAliases(companyId, applicationName));
		}

		return scopesAliases;
	}

	private Collection<String> _doListScopesAliasesForApplication(
		long companyId, String applicationName) {

		List<ServiceReferenceServiceTuple<?, ScopeFinder>> tuples =
			_scopeFinderByNameServiceTrackerMap.getService(applicationName);

		if (tuples == null || tuples.isEmpty()) {
			return Collections.emptyList();
		}

		ServiceReferenceServiceTuple<?, ScopeFinder> tuple = tuples.get(0);

		ServiceReference<?> serviceReference =
			tuple.getServiceReference();

		ScopeFinder scopeFinder = _scopedScopeFinders.getService(
			companyId, applicationName);

		Collection<String> scopes = scopeFinder.findScopes();

		PrefixHandlerFactory prefixHandlerFactory =
			_scopedPrefixHandlerFactories.getService(
				companyId, applicationName);

		PrefixHandler prefixHandler = prefixHandlerFactory.create(
			serviceReference::getProperty);

		ScopeMapper scopeMapper =
			_scopedScopeMapper.getService(companyId, applicationName);

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

	@Activate
	protected void activate(BundleContext bundleContext) {
		_scopeFinderByNameServiceTrackerMap =
			ServiceTrackerMapFactory.openMultiValueMap(
				bundleContext, ScopeFinder.class, "(osgi.jaxrs.name=*)",
				new PropertyServiceReferenceMapper<>("osgi.jaxrs.name"),
				new ScopeFinderServiceTupleServiceTrackerCustomizer(
					bundleContext), Comparator.naturalOrder(),
				new CacheClearServiceTrackerMapListener());

		_scopedScopeFinders = _scopedServiceTrackerMapFactory.create(
			bundleContext, ScopeFinder.class, "osgi.jaxrs.name", null,
			_invocationCache::clear);

		_scopedPrefixHandlerFactories = _scopedServiceTrackerMapFactory.create(
			bundleContext, PrefixHandlerFactory.class, "osgi.jaxrs.name",
			() -> (_defaultPrefixHandlerFactory != null ? 
				_defaultPrefixHandlerFactory : 
					(propertyAccessor) -> 
						PrefixHandler.PASSTHROUGH_PREFIXHANDLER),
			_invocationCache::clear);

		_scopedScopeMapper = _scopedServiceTrackerMapFactory.create(
			bundleContext, ScopeMapper.class, "osgi.jaxrs.name",
			() -> (_defaultScopeMapper != null ? 
				_defaultScopeMapper : ScopeMapper.PASSTHROUGH_SCOPEMAPPER), 
			_invocationCache::clear);

		_scopedScopeMatcherFactories =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext, ScopeMatcherFactory.class, "company.id");
	}

	@Deactivate
	protected void deactivate() {
		_scopeFinderByNameServiceTrackerMap.close();
		_scopedPrefixHandlerFactories.close();
		_scopedScopeMapper.close();
	}

	@Reference(
		target = "(default=true)",
		policyOption = ReferencePolicyOption.GREEDY,
		cardinality = ReferenceCardinality.OPTIONAL
	)
	private PrefixHandlerFactory _defaultPrefixHandlerFactory;

	@Reference(
		target = "(default=true)",
		policyOption = ReferencePolicyOption.GREEDY,
		cardinality = ReferenceCardinality.OPTIONAL
	)
	private ScopeMapper _defaultScopeMapper;

	@Reference
	ScopedServiceTrackerMapFactory _scopedServiceTrackerMapFactory;
	
	private ServiceTrackerMap<
		String, List<ServiceReferenceServiceTuple<?, ScopeFinder>>>
			_scopeFinderByNameServiceTrackerMap;

	@Reference(name = "default")
	private ScopeMatcherFactory _defaultScopeMatcherFactory;

	@Override
	public Collection<LiferayOAuth2Scope> getLiferayOAuth2Scopes(
		long companyId, String scopesAlias) {

		return readFromCache("locateScopes" + companyId + scopesAlias,
			__ -> this._doLocateScopes(companyId, scopesAlias));
	}

	@Override
	public Collection<LiferayOAuth2Scope> getLiferayOAuth2Scopes(
		long companyId, String scopesAlias, String applicationName) {

		return readFromCache(
			"locateScopes" + companyId + applicationName + scopesAlias,
			__ -> this._doLocateScopesForApplication(
				companyId, scopesAlias, applicationName));
	}

	@Override
	public Collection<String> getScopeAliases(long companyId) {
		return readFromCache(
			"listAliases" + companyId,
			__ -> this._doListScopesAliases(companyId));
	}

	@Override
	public Collection<String> getScopeAliases(
		long companyId, String applicationName) {

		return readFromCache(
			"listAliases" + companyId + applicationName,
			__ -> this._doListScopesAliasesForApplication(
				companyId, applicationName));
	}

	@SuppressWarnings("unchecked")
	private <T> T readFromCache(
		String key, Function<String, T> mappingFunction) {

		T value = (T) _invocationCache.get(key);

		if (value == null) {
			value = mappingFunction.apply(key);
			_invocationCache.put(key, value);
		}

		return value;
	}

	private static class ScopeFinderServiceTupleServiceTrackerCustomizer
		implements
		ServiceTrackerCustomizer
			<ScopeFinder, ServiceReferenceServiceTuple<?, ScopeFinder>> {

		private BundleContext _bundleContext;

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

	}

	private class CacheClearServiceTrackerMapListener implements
		ServiceTrackerMapListener<
			String, ServiceReferenceServiceTuple<?, ScopeFinder>,
				List<ServiceReferenceServiceTuple<?, ScopeFinder>>> {

		@Override
		public void keyEmitted(
			ServiceTrackerMap<
				String, List<ServiceReferenceServiceTuple<?, ScopeFinder>>>
					serviceTrackerMap,
			String key,
			ServiceReferenceServiceTuple<?, ScopeFinder> service,
			List<ServiceReferenceServiceTuple<?, ScopeFinder>> content) {

			_invocationCache.clear();
		}

		@Override
		public void keyRemoved(
			ServiceTrackerMap<
				String, List<ServiceReferenceServiceTuple<?, ScopeFinder>>>
					serviceTrackerMap,
			String key,
			ServiceReferenceServiceTuple<?, ScopeFinder> service,
			List<ServiceReferenceServiceTuple<?, ScopeFinder>> content) {

			_invocationCache.clear();
		}
	}

}