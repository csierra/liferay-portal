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

package com.liferay.oauth2.provider.scopes.impl;

import com.liferay.oauth2.provider.model.LiferayAliasedOAuth2Scope;
import com.liferay.oauth2.provider.model.LiferayOAuth2Scope;
import com.liferay.oauth2.provider.scopes.impl.model.LiferayAliasedOAuth2ScopeImpl;
import com.liferay.oauth2.provider.scopes.impl.model.LiferayOAuth2ScopeImpl;
import com.liferay.oauth2.provider.scopes.liferay.api.ScopeFinderLocator;
import com.liferay.oauth2.provider.scopes.liferay.api.ScopeMatcherFactoryLocator;
import com.liferay.oauth2.provider.scopes.liferay.api.ScopedServiceTrackerMap;
import com.liferay.oauth2.provider.scopes.spi.ScopeDescriptor;
import com.liferay.oauth2.provider.scopes.spi.ScopeFinder;
import com.liferay.oauth2.provider.scopes.spi.ScopeMapper;
import com.liferay.oauth2.provider.scopes.spi.ScopeMatcher;
import com.liferay.oauth2.provider.scopes.spi.NamespaceApplicator;
import com.liferay.oauth2.provider.scopes.spi.NamespaceApplicatorFactory;
import com.liferay.oauth2.provider.scopes.spi.ScopeMatcherFactory;
import com.liferay.oauth2.provider.service.OAuth2ScopeGrantLocalService;
import com.liferay.osgi.service.tracker.collections.ServiceReferenceServiceTuple;
import com.liferay.osgi.service.tracker.collections.map.PropertyServiceReferenceMapper;
import com.liferay.osgi.service.tracker.collections.map.ServiceReferenceMapper;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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

	@Override
	public Collection<LiferayOAuth2Scope> locateScopes(
		long companyId, String scopesAlias) {

		Collection<LiferayOAuth2Scope> grants = new ArrayList<>();

		Set<String> names = _scopeFinderByNameServiceTrackerMap.keySet();

		ScopeMatcherFactory scopeMatcherFactory =
			_scopeMatcherFactoryLocator.locateScopeMatcherFactory(companyId);

		for (String name : names) {
			ScopeMatcher scopeMatcher = scopeMatcherFactory.createScopeMatcher(scopesAlias);

			ServiceReferenceServiceTuple<?, ScopeFinder> tuple =
				_scopeFinderByNameServiceTrackerMap.getService(name);

			ServiceReference<?> serviceReference = tuple.getServiceReference();

			NamespaceApplicatorFactory namespaceApplicatorFactory =
				_scopedNamespaceApplicatorFactories.getService(companyId, name);

			NamespaceApplicator namespaceApplicator = namespaceApplicatorFactory.mapFrom(
				serviceReference::getProperty);

			scopeMatcher = scopeMatcher.withNamespaceApplicator(namespaceApplicator);

			scopeMatcher = scopeMatcher.withScopeMapper(
				_scopedScopeMapper.getService(companyId, name));

			ScopeFinder scopeFinder = tuple.getService();

			Collection<String> grantedScopes = scopeFinder.findScopes(
				scopeMatcher);

			for (String grantedScope : grantedScopes) {
				Bundle bundle = serviceReference.getBundle();

				grants.add(
					new LiferayOAuth2ScopeImpl(name, bundle, grantedScope));
			}
		}

		return grants;
	}

	private ScopedServiceTrackerMap<NamespaceApplicatorFactory>
		_scopedNamespaceApplicatorFactories;
	private ScopedServiceTrackerMap<ScopeMapper>
		_scopedScopeMapper;

	@Override
	public Collection<LiferayAliasedOAuth2Scope> listScopes(long companyId) {
		Collection<LiferayAliasedOAuth2Scope> scopes = new HashSet<>();

		Set<String> names = _scopeFinderByNameServiceTrackerMap.keySet();

		for (String name : names) {
			ServiceReferenceServiceTuple<?, ScopeFinder> tuple =
				_scopeFinderByNameServiceTrackerMap.getService(name);

			ServiceReference<?> serviceReference =
				tuple.getServiceReference();

			ScopeFinder scopeFinder = tuple.getService();

			Collection<String> availableScopes = scopeFinder.findScopes(
				ScopeMatcher.ALL);

			NamespaceApplicatorFactory namespaceApplicatorFactory =
				_scopedNamespaceApplicatorFactories.getService(companyId, name);

			NamespaceApplicator namespaceApplicator = namespaceApplicatorFactory.mapFrom(
				serviceReference::getProperty);

			ScopeMapper scopeMapper =
				_scopedScopeMapper.getService(companyId, name);

			for (String availableScope : availableScopes) {
				Bundle bundle = serviceReference.getBundle();

				Set<String> mappedScopes = scopeMapper.map(availableScope);

				for (String mappedScope : mappedScopes) {
					String externalAlias = namespaceApplicator.applyNamespace(mappedScope);

					scopes.add(
						new LiferayAliasedOAuth2ScopeImpl(
							new LiferayOAuth2ScopeImpl(
								name, bundle, availableScope), externalAlias));
				}
			}
		}

		return scopes;
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_scopeFinderByNameServiceTrackerMap =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext, ScopeFinder.class, "osgi.jaxrs.name",
				new ScopeFinderServiceTupleServiceTrackerCustomizer(
					bundleContext));

		_scopedNamespaceApplicatorFactories = new ScopedServiceTrackerMap<>(
			bundleContext, NamespaceApplicatorFactory.class, "osgi.jaxrs.name",
			() -> _defaultNamespaceApplicatorFactory);

		_scopedScopeMapper = new ScopedServiceTrackerMap<>(
			bundleContext, ScopeMapper.class, "osgi.jaxrs.name",
			() -> ScopeMapper.NULL);
	}

	@Deactivate
	protected void deactivate() {
		_scopeFinderByNameServiceTrackerMap.close();
		_scopedNamespaceApplicatorFactories.close();
		_scopedScopeMapper.close();
	}

	@Reference(
		target = "(default=true)",
		policyOption = ReferencePolicyOption.GREEDY
	)
	private NamespaceApplicatorFactory _defaultNamespaceApplicatorFactory;

	@Reference
	private OAuth2ScopeGrantLocalService _oAuth2ScopeGrantLocalService;

	@Reference
	private ScopeMatcherFactoryLocator _scopeMatcherFactoryLocator;

	private ServiceTrackerMap<
		String, ServiceReferenceServiceTuple<?, ScopeFinder>>
			_scopeFinderByNameServiceTrackerMap;

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

}