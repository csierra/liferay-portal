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

import com.liferay.oauth2.provider.scope.liferay.api.ScopeDescriptorLocator;
import com.liferay.oauth2.provider.scope.spi.scopedescriptor.ScopeDescriptor;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

@Component
public class DefaultScopeDescriptorLocator implements ScopeDescriptorLocator {

	private ServiceTrackerMap<String, ScopeDescriptor>
		_scopeDescriptorsByCompany;
	private ServiceTrackerMap<String, ScopeDescriptor>
		_scopeDescriptorsByApplicationName;

	@Override
	public ScopeDescriptor locateScopeDescriptorForCompany(long companyId) {

		ScopeDescriptor scopeDescriptor =
			_scopeDescriptorsByCompany.getService(Long.toString(companyId));

		if (scopeDescriptor == null) {
			return _defaultScopeDescriptor;
		}

		return scopeDescriptor;
	}

	@Override
	public ScopeDescriptor locateScopeDescriptorForApplication(
		String applicationName) {

		ScopeDescriptor scopeDescriptor =
			_scopeDescriptorsByApplicationName.getService(applicationName);

		if (scopeDescriptor == null) {
			return _defaultScopeDescriptor;
		}

		return scopeDescriptor;
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_scopeDescriptorsByCompany =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext, ScopeDescriptor.class, "company.id");
		_scopeDescriptorsByApplicationName =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext, ScopeDescriptor.class, "osgi.jaxrs.name");
	}

	@Deactivate
	protected void deactivate() {
		_scopeDescriptorsByApplicationName.close();

		_scopeDescriptorsByCompany.close();
	}

	@Reference(target = "(default=true)")
	ScopeDescriptor _defaultScopeDescriptor;
}
