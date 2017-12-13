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

import com.liferay.oauth2.provider.api.scopes.NamespaceAdder;
import com.liferay.oauth2.provider.api.scopes.NamespaceAdderFactory;
import com.liferay.oauth2.provider.api.scopes.OAuth2Scope;
import com.liferay.oauth2.provider.api.scopes.ScopeFinder;
import com.liferay.oauth2.provider.api.scopes.ScopeFinderLocator;
import com.liferay.osgi.service.tracker.collections.ServiceReferenceServiceTuple;
import com.liferay.osgi.service.tracker.collections.internal.DefaultServiceTrackerCustomizer;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.kernel.model.Company;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import java.util.Set;
import java.util.stream.Stream;

@Component(immediate = true)
public class ScopeRegistry implements ScopeFinderLocator {

	private ServiceTrackerMap<String, ServiceReferenceServiceTuple<?, ScopeFinder>>
		_scopeFinderByNameServiceTrackerMap;
	private ServiceTrackerMap<String, NamespaceAdder> _namespacesByName;

	@Activate
	protected void activate(BundleContext bundleContext) {
		ServiceTrackerCustomizer<ScopeFinder, ScopeFinder>
			defaultServiceTrackerCustomizer =
				new DefaultServiceTrackerCustomizer<>(bundleContext);
		_scopeFinderByNameServiceTrackerMap =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext, ScopeFinder.class, "osgi.jaxrs.name",
				new ServiceTrackerCustomizer<ScopeFinder,
					ServiceReferenceServiceTuple<?, ScopeFinder>>() {

					@Override
					public ServiceReferenceServiceTuple<?, ScopeFinder> addingService(
						ServiceReference<ScopeFinder> reference) {

						ScopeFinder scopeFinder =
							defaultServiceTrackerCustomizer.addingService(
								reference);

						return new ServiceReferenceServiceTuple<>(
							reference, scopeFinder);
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

						defaultServiceTrackerCustomizer.removedService(
							reference, tuple.getService());
					}
				});

		_namespacesByName = ServiceTrackerMapFactory.openSingleValueMap(
			bundleContext, NamespaceAdder.class, "osgi.jaxrs.name");

	}

	@Override
	public ScopeFinder locate(Company company) {
		return () -> {
			NamespaceAdder companyNamespaceAdder =
				_companyNamespaceAdderFactory.create(company);

			Set<String> names = _scopeFinderByNameServiceTrackerMap.keySet();

			Stream<OAuth2Scope> stream = Stream.empty();

			for (String name : names) {
				ServiceReferenceServiceTuple<?, ScopeFinder> tuple =
					_scopeFinderByNameServiceTrackerMap.getService(name);

				NamespaceAdder namespaceAdder = companyNamespaceAdder.append(
					_namespaceAdderFactory.create(tuple.getServiceReference()));

				ScopeFinder scopeFinder = namespaceAdder.prepend(
					tuple.getService());

				stream = Stream.concat(stream, scopeFinder.findScopes());
			}

			return stream;
		};
	}


	@Reference
	private NamespaceAdderFactory<Company> _companyNamespaceAdderFactory;

	@Reference(target = "(default=true)")
	private NamespaceAdderFactory<ServiceReference<?>>
		_defaultNamespaceAdderFactory;

	private NamespaceAdderFactory<ServiceReference<?>> _namespaceAdderFactory =
		sr -> {
			NamespaceAdder namespaceAdder = _namespacesByName.getService(
				sr.getProperty("osgi.jaxrs.name").toString());

			if (namespaceAdder == null) {
				return _defaultNamespaceAdderFactory.create(sr);
			}

			return namespaceAdder;
		};

}