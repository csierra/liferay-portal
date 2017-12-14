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
import com.liferay.oauth2.provider.api.scopes.NamespaceAdderMapper;
import com.liferay.oauth2.provider.api.scopes.OAuth2Scope;
import com.liferay.oauth2.provider.api.scopes.ScopeFinder;
import com.liferay.oauth2.provider.api.scopes.ScopeFinderLocator;
import com.liferay.osgi.service.tracker.collections.ServiceReferenceServiceTuple;
import com.liferay.osgi.service.tracker.collections.internal.DefaultServiceTrackerCustomizer;
import com.liferay.osgi.service.tracker.collections.map.PropertyServiceReferenceMapper;
import com.liferay.osgi.service.tracker.collections.map.ServiceReferenceMapper;
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
public class ScopeRegistry implements
	ScopeFinderLocator, NamespaceAdderMapperLocator {

	private ServiceTrackerMap<
		String, ServiceReferenceServiceTuple<?, ScopeFinder>>
			_scopeFinderByNameServiceTrackerMap;
	private ServiceTrackerMap<String, NamespaceAdderMapper> _namespacesByName;
	private ServiceTrackerMap<String, NamespaceAdderMapper> _namespacesByCompany;
	private ServiceTrackerMap<String, NamespaceAdderMapper>
		_namespacesByCompanyAndName;

	@Activate
	protected void activate(BundleContext bundleContext) {
		_scopeFinderByNameServiceTrackerMap =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext, ScopeFinder.class, "osgi.jaxrs.name",
				new ScopeFinderServiceTupleServiceTrackerCustomizer(
					bundleContext));

		_namespacesByCompany = ServiceTrackerMapFactory.openSingleValueMap(
			bundleContext, NamespaceAdderMapper.class, "companyId");

		_namespacesByName = ServiceTrackerMapFactory.openSingleValueMap(
			bundleContext, NamespaceAdderMapper.class, "osgi.jaxrs.name");

		_namespacesByCompanyAndName =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext, NamespaceAdderMapper.class,
				"(&(companyId=*)(osgi.jaxrs.name=*))",
				(serviceReference, emitter) -> {
					ServiceReferenceMapper<String, NamespaceAdderMapper>
						companyMapper = new PropertyServiceReferenceMapper<>(
							"companyId");
					ServiceReferenceMapper<String, NamespaceAdderMapper> nameMapper =
						new PropertyServiceReferenceMapper<>("osgi.jaxrs.name");

					companyMapper.map(
						serviceReference,
						key1 ->
							nameMapper.map(
								serviceReference,
								key2 -> emitter.emit(key1 + "-" + key2)));
				});

	}

	@Override
	public ScopeFinder locateScopeFinder(Company company) {
		return () -> {
			Set<String> names = _scopeFinderByNameServiceTrackerMap.keySet();

			Stream<OAuth2Scope> stream = Stream.empty();

			for (String name : names) {
				ServiceReferenceServiceTuple<?, ScopeFinder> tuple =
					_scopeFinderByNameServiceTrackerMap.getService(name);

				NamespaceAdderMapper namespaceAdderMapper =
					locateMapper(company);

				ServiceReference<?> serviceReference =
					tuple.getServiceReference();

				NamespaceAdder namespaceAdder = namespaceAdderMapper.mapFrom(
					serviceReference::getProperty);

				ScopeFinder scopeFinder = namespaceAdder.prepend(
					tuple.getService());

				stream = Stream.concat(stream, scopeFinder.findScopes());
			}

			return stream;
		};
	}

	@Reference
	private NamespaceAdderMapper _defaultNamespaceAdderMapper;

	@Override
	public NamespaceAdderMapper locateMapper(Company company) {
		return propertyHolder -> {
			Object nameProperty = propertyHolder.get("osgi.jaxrs.name");

			String name = nameProperty.toString();

			long companyId = company.getCompanyId();

			NamespaceAdderMapper namespaceAdder =
				_namespacesByCompanyAndName.getService(companyId + '-' + name);

			if (namespaceAdder != null) {
				return namespaceAdder.mapFrom(propertyHolder);
			}

			namespaceAdder = _namespacesByName.getService(name);

			if (namespaceAdder != null) {
				return namespaceAdder.mapFrom(propertyHolder);
			}

			namespaceAdder = _namespacesByCompany.getService(
				Long.toString(companyId));

			if (namespaceAdder != null) {
				return namespaceAdder.mapFrom(propertyHolder);
			}

			return _defaultNamespaceAdderMapper.mapFrom(propertyHolder);
		};
	}

	private static class ScopeFinderServiceTupleServiceTrackerCustomizer
		implements
		ServiceTrackerCustomizer<
			ScopeFinder, ServiceReferenceServiceTuple<?, ScopeFinder>> {

		private final ServiceTrackerCustomizer<ScopeFinder, ScopeFinder>
			_defaultServiceTrackerCustomizer;

		public ScopeFinderServiceTupleServiceTrackerCustomizer(
			BundleContext bundleContext) {

			_defaultServiceTrackerCustomizer =
				new DefaultServiceTrackerCustomizer<>(bundleContext);
		}

		@Override
		public ServiceReferenceServiceTuple<?, ScopeFinder> addingService(
			ServiceReference<ScopeFinder> reference) {

			ScopeFinder scopeFinder =
				_defaultServiceTrackerCustomizer.addingService(
					reference);

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

			_defaultServiceTrackerCustomizer.removedService(
				reference, tuple.getService());
		}
	}
}