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

import com.liferay.oauth2.provider.scope.spi.scope.matcher.ScopeMatcherFactory;
import com.liferay.oauth2.provider.scope.liferay.ScopeMatcherFactoryLocator;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

@Component
public class DefaultScopeMatcherFactoryLocator implements
	ScopeMatcherFactoryLocator {

	private ServiceTrackerMap<String, ScopeMatcherFactory>
		_scopedScopeMatcherFactories;

	@Override
	public ScopeMatcherFactory locateScopeMatcherFactory(long companyId) {
		ScopeMatcherFactory scopeMatcherFactory =
			_scopedScopeMatcherFactories.getService(Long.toString(companyId));

		if (scopeMatcherFactory == null) {
			return _defaultScopeMatcherFactory;
		}

		return scopeMatcherFactory;
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_scopedScopeMatcherFactories =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext, ScopeMatcherFactory.class, "company.id");
	}

	@Deactivate
	protected void deactivate() {
		_scopedScopeMatcherFactories.close();
	}

	@Reference(name = "default")
	private ScopeMatcherFactory _defaultScopeMatcherFactory;



}
