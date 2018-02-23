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

import com.liferay.oauth2.provider.scope.liferay.api.LiferayOAuth2Scope;
import com.liferay.oauth2.provider.scope.impl.model.LiferayOAuth2ScopeImpl;
import com.liferay.oauth2.provider.scope.liferay.api.ScopeFinderLocator;
import com.liferay.oauth2.provider.scope.liferay.api.ScopeMatcherFactoryLocator;
import com.liferay.oauth2.provider.scope.liferay.api.ScopedServiceTrackerMap;
import com.liferay.oauth2.provider.scope.spi.model.PrefixHandler;
import com.liferay.oauth2.provider.scope.spi.model.ScopeMatcher;
import com.liferay.oauth2.provider.scope.spi.ScopeFinder;
import com.liferay.oauth2.provider.scope.spi.ScopeMapper;
import com.liferay.oauth2.provider.scope.spi.PrefixHandlerFactory;
import com.liferay.oauth2.provider.scope.spi.ScopeMatcherFactory;
import com.liferay.oauth2.provider.service.OAuth2ScopeGrantLocalService;
import com.liferay.osgi.service.tracker.collections.ServiceReferenceServiceTuple;
import com.liferay.osgi.service.tracker.collections.map.PropertyServiceReferenceMapper;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapListener;
import com.liferay.portal.kernel.util.StringPool;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

@Component(immediate = true, service = ScopeFinderLocator.class)
public class ScopeRegistry implements ScopeFinderLocator {

	private ConcurrentMap<String, Object> _invocationCache =
		new ConcurrentHashMap<>();

	private Collection<LiferayOAuth2Scope> _doLocateScopes(
		long companyId, String scopesAlias) {

		Collection<LiferayOAuth2Scope> grants = new ArrayList<>();

		Set<String> applicationNames =
			_scopeFinderByNameServiceTrackerMap.keySet();

		for (String applicationName : applicationNames) {
			grants.addAll(
				locateScopesForApplication(
					companyId, scopesAlias, applicationName));
		}

		return grants;
	}

	private Collection<LiferayOAuth2Scope> _doLocateScopesForApplication(
		long companyId, String scopesAlias, String applicationName) {

		ScopeMatcherFactory scopeMatcherFactory =
			_scopeMatcherFactoryLocator.locateScopeMatcherFactory(companyId);

		List<ServiceReferenceServiceTuple<?, ScopeFinder>> tuples =
			_scopeFinderByNameServiceTrackerMap.getService(applicationName);

		if (tuples == null || tuples.isEmpty()) {
			return Collections.emptyList();
		}

		ServiceReferenceServiceTuple<?, ScopeFinder> tuple = tuples.get(0);

		ServiceReference<?> serviceReference = tuple.getServiceReference();

		PrefixHandlerFactory prefixHandlerMapper =
			_scopedPrefixHandlerFactories.getService(companyId, applicationName);

		PrefixHandler prefixHandler = prefixHandlerMapper.mapFrom(
			serviceReference::getProperty);

		String prefix = prefixHandler.addPrefix(StringPool.BLANK);

		if (!scopesAlias.startsWith(prefix)) {
			return Collections.emptyList();
		}

		ScopeMapper scopeMapper =
			_scopedScopeMapper.getService(companyId, applicationName);
		
		ScopeMatcher scopeMatcher = scopeMapper.applyTo(
			scopeMatcherFactory.create(scopesAlias.substring(prefix.length())));

		ScopeFinder scopeFinder = tuple.getService();

		Map<String, Set<String>> scopes = scopeFinder.findScopes();

		Set<Map.Entry<String, Set<String>>> entries = scopes.entrySet();

		Stream<Map.Entry<String, Set<String>>> stream = entries.stream();

		Set<String> grantedScopes = stream.filter(
			e -> scopeMatcher.match(e.getKey())
		).flatMap(
			e -> e.getValue().stream()
		).collect(
			Collectors.toSet()
		);

		Collection<LiferayOAuth2Scope> grants = new ArrayList<>();

		for (String grantedScope : grantedScopes) {
			Bundle bundle = serviceReference.getBundle();

			grants.add(new LiferayOAuth2ScopeImpl(
				applicationName, bundle, grantedScope));
		}

		return grants;
	}

	private ScopedServiceTrackerMap<PrefixHandlerFactory>
		_scopedPrefixHandlerFactories;
	private ScopedServiceTrackerMap<ScopeMapper>
		_scopedScopeMapper;

	private Collection<String> _doListScopesAliases(long companyId) {
		Collection<String> scopesAliases = new HashSet<>();

		Set<String> applicationNames = _scopeFinderByNameServiceTrackerMap.keySet();

		for (String applicationName : applicationNames) {
			scopesAliases.addAll(listScopesAliasesForApplication(companyId, applicationName));
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

		ScopeFinder scopeFinder = tuple.getService();

		Map<String, Set<String>> scopesMap = scopeFinder.findScopes();

		Collection<String> availableScopes = scopesMap.keySet();

		PrefixHandlerFactory prefixHandlerFactory =
			_scopedPrefixHandlerFactories.getService(
				companyId, applicationName);

		PrefixHandler prefixHandler = prefixHandlerFactory.mapFrom(
			serviceReference::getProperty);

		ScopeMapper scopeMapper =
			_scopedScopeMapper.getService(companyId, applicationName);

		Collection<String> scopesAliases = new ArrayList<>();

		for (String availableScope : availableScopes) {
			Set<String> mappedScopes = scopeMapper.map(availableScope);

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

		_scopedPrefixHandlerFactories = new ScopedServiceTrackerMap<>(
			bundleContext, PrefixHandlerFactory.class, "osgi.jaxrs.name",
			() -> _defaultPrefixHandlerFactory, _invocationCache::clear);

		_scopedScopeMapper = new ScopedServiceTrackerMap<>(
			bundleContext, ScopeMapper.class, "osgi.jaxrs.name",
			() -> _defaultScopeMapper, _invocationCache::clear);
	}

	@Deactivate
	protected void deactivate() {
		_scopeFinderByNameServiceTrackerMap.close();
		_scopedPrefixHandlerFactories.close();
		_scopedScopeMapper.close();
	}

	@Reference(
		target = "(default=true)",
		policyOption = ReferencePolicyOption.GREEDY
	)
	private PrefixHandlerFactory _defaultPrefixHandlerFactory;

	@Reference(
		target = "(&(default=true)(companyId=0))",
		policyOption = ReferencePolicyOption.GREEDY
	)
	private ScopeMapper _defaultScopeMapper;
	
	@Reference
	private OAuth2ScopeGrantLocalService _oAuth2ScopeGrantLocalService;

	@Reference
	private ScopeMatcherFactoryLocator _scopeMatcherFactoryLocator;

	private ServiceTrackerMap<
		String, List<ServiceReferenceServiceTuple<?, ScopeFinder>>>
			_scopeFinderByNameServiceTrackerMap;

	@Override
	public Collection<LiferayOAuth2Scope> locateScopes(
		long companyId, String scopesAlias) {

		return readFromCache("locateScopes" + companyId + scopesAlias,
			__ -> this._doLocateScopes(companyId, scopesAlias));
	}

	@Override
	public Collection<LiferayOAuth2Scope> locateScopesForApplication(
		long companyId, String scopesAlias, String applicationName) {

		return readFromCache(
			"locateScopes" + companyId + applicationName + scopesAlias,
			__ -> this._doLocateScopesForApplication(
				companyId, scopesAlias, applicationName));
	}

	@Override
	public Collection<String> listScopesAliases(long companyId) {
		return readFromCache(
			"listAliases" + companyId,
			__ -> this._doListScopesAliases(companyId));
	}

	@Override
	public Collection<String> listScopesAliasesForApplication(
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