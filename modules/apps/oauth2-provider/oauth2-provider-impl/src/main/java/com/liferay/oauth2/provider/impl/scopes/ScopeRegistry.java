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

import com.liferay.oauth2.provider.api.scopes.ScopeFinder;
import com.liferay.oauth2.provider.api.scopes.ScopeFinderLocator;
import com.liferay.oauth2.provider.api.scopes.ScopeMatcher;
import com.liferay.oauth2.provider.impl.scopes.NamespaceManager.Namespace;
import com.liferay.oauth2.provider.impl.scopes.NamespaceManager.NamespacedScope;
import com.liferay.oauth2.provider.api.scopes.OAuth2Scopes;
import com.liferay.oauth2.provider.api.scopes.OAuth2Scopes.LocalizedScopesDescription;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.kernel.model.Company;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component(immediate = true)
public class ScopeRegistry implements ScopeFinderLocator {

	private ConcurrentHashMap<String, Namespace> _nameSpaces;
	private ConcurrentHashMap<NamespacedScope, LocalizedScopesDescription>
		_scopeDescriptions;

	private ServiceTrackerMap<String, Strategy> _strategiesPerCompany;
	private ServiceTrackerMap<String, Strategy>
		_strategiesPerCompanyAndNamespace;
	private ServiceTrackerMap<String, Strategy> _strategiesPerNamespace;

	@Activate
	protected void activate(BundleContext bundleContext) {
		_nameSpaces = new ConcurrentHashMap<>();

		_scopeDescriptions = new ConcurrentHashMap<>();

		_strategiesPerCompany = ServiceTrackerMapFactory.openSingleValueMap(
			bundleContext, Strategy.class, "companyId");

		_strategiesPerNamespace = ServiceTrackerMapFactory.openSingleValueMap(
			bundleContext, Strategy.class, "namespace");

		_strategiesPerCompanyAndNamespace =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext, Strategy.class,
				String.format("(&(%s=*)(%s=*))", "companyId", "namespace"),
				(serviceReference, emitter) ->
					emitter.emit(
						serviceReference.getProperty("companyId") +
						"-" +
						serviceReference.getProperty("namespace")));
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		unbind = "removeScopeRegistrator"
	)
	protected void addScopeRegistrator(
		ServiceReference<OAuth2Scopes.Registrator> serviceReference,
		OAuth2Scopes.Registrator registrator) {

		Namespace namespace = _namespaceManager.createNamespace();

		registrator.register(
			(scopeType, description) ->
				_scopeDescriptions.put(
					namespace.addScope(scopeType), description));

		_nameSpaces.put(
			_namespaceIdGenerator.generateName(serviceReference), namespace);
	}

	protected void removeScopeRegistrator(
		ServiceReference<OAuth2Scopes.Registrator> serviceReference) {

		Namespace namespace = _nameSpaces.remove(
			_namespaceIdGenerator.generateName(serviceReference));

		namespace.forEach(_scopeDescriptions::remove);
	}

	protected Strategy getStrategy(long companyId, String namespaceId) {
		Strategy strategy =
			_strategiesPerCompanyAndNamespace.getService(
				companyId + "-" + namespaceId);

		if (strategy != null) {
			return strategy;
		}

		strategy = _strategiesPerNamespace.getService(namespaceId);

		if (strategy != null) {
			return strategy;
		}

		strategy = _strategiesPerCompany.getService(namespaceId);

		if (strategy != null) {
			return strategy;
		}

		return _defaultStrategy;
	}

	@Reference(name = "NamespaceManager")
	private NamespaceManager _namespaceManager;

	@Reference
	private NamespaceIdGenerator _namespaceIdGenerator;

	@Reference(target = "(&(|(!(company))(company=0))(!(namespace=*)))")
	private volatile Strategy _defaultStrategy;

	@Override
	public ScopeFinder locate(Company company) {
		return name -> {
			Stream<Map.Entry<String, Namespace>> stream =
				_nameSpaces.entrySet().stream();

			return stream.flatMap(
				entry -> {
					Strategy strategy = getStrategy(
						company.getCompanyId(), entry.getKey());

					ScopeMatcher matches = strategy.matches(name);

					return entry.getValue().findScopes(matches).stream();
				}
			).collect(
				Collectors.toList()
			);
		};
	}
}