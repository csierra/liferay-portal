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
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.kernel.model.Company;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component(immediate = true)
public class ScopeRegistry implements ScopeFinderLocator {

	private BundleContext _bundleContext;
	private ServiceTrackerMap<String, List<ScopeFinder>>
		_scopesByCompany;
	private volatile ScopeFinder _defaultScopeFinder = ScopeFinder.empty();
	private Collection<ScopeFinder> _defaultScopeFinders =
		new CopyOnWriteArrayList<>();

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		_scopesByCompany = ServiceTrackerMapFactory.openMultiValueMap(
			bundleContext, ScopeFinder.class, "companyId");
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(!(companyId=*))",
		unbind = "removeScopeFinder"
	)
	protected void addScopeFinder(ScopeFinder scopeFinder) {
		_defaultScopeFinders.add(scopeFinder);

		_defaultScopeFinder = ScopeFinder.merge(_defaultScopeFinders);
	}

	protected void removeScopeFinder(ScopeFinder scopeFinder) {
		_defaultScopeFinders.remove(scopeFinder);

		_defaultScopeFinder = ScopeFinder.merge(_defaultScopeFinders);
	}

	@Override
	public ScopeFinder locate(Company company) {
		ScopeFinder scopeFinder = ScopeFinder.merge(_scopesByCompany.getService(
			Long.toString(company.getCompanyId())));

		if (scopeFinder != null) {
			return scopeFinder;
		}

		return _defaultScopeFinder;
	}
}