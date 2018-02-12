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

package com.liferay.oauth2.provider.jsonws;

import com.liferay.oauth2.provider.scopes.spi.ScopeDescriptor;
import com.liferay.oauth2.provider.scopes.spi.ScopeFinder;
import com.liferay.oauth2.provider.scopes.spi.ScopeMatcher;
import com.liferay.portal.instance.lifecycle.BasePortalInstanceLifecycleListener;
import com.liferay.portal.instance.lifecycle.PortalInstanceLifecycleListener;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.security.service.access.policy.model.SAPEntry;
import com.liferay.portal.security.service.access.policy.service.SAPEntryLocalService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Tomas Polesovsky
 */
@Component(immediate=true)
public class OAuth2SAPEntryScopesPublisher {
	public static final String OAUTH2_SAP_PREFIX = "OAUTH2_";
	public static final String JSONWS_APPLICATION_NAME = "jsonws";

	@Activate
	public void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		_serviceRegistrations.add(
			bundleContext.registerService(
				PortalInstanceLifecycleListener.class,
				new BasePortalInstanceLifecycleListener(){
					public void portalInstanceRegistered(Company company) {
						registerSAPEntryScopeFinderDescriptor(
							company.getCompanyId());
					}
				},
				null));
	}

	@Deactivate
	public void deactivate(){
		for (ServiceRegistration serviceRegistration : _serviceRegistrations) {
			serviceRegistration.unregister();
		}

		_serviceRegistrations.clear();
	}

	protected void registerSAPEntryScopeFinderDescriptor(long companyId) {
		Dictionary<String, Object> properties = new Hashtable<>();

		properties.put("osgi.jaxrs.name", JSONWS_APPLICATION_NAME);
		properties.put("companyId", String.valueOf(companyId));

		_serviceRegistrations.add(
			_bundleContext.registerService(
				new String[]{
					ScopeDescriptor.class.getName(),
					ScopeFinder.class.getName()},
				new SAPEntryScopeFinderDescriptor(companyId),
				properties));
	}

	private static final Log _log = LogFactoryUtil.getLog(
		OAuth2SAPEntryScopesPublisher.class);

	private volatile BundleContext _bundleContext;

	@Reference
	private volatile SAPEntryLocalService _sapEntryLocalService;

	private List<ServiceRegistration> _serviceRegistrations =
		new CopyOnWriteArrayList();

	private class SAPEntryScopeFinderDescriptor
		implements ScopeFinder, ScopeDescriptor {

		public SAPEntryScopeFinderDescriptor(long companyId) {
			_companyId = companyId;
		}

		@Override
		public Collection<String> findScopes(ScopeMatcher scopeMatcher) {
			List<String> names = new ArrayList<>();

			try {
				List<SAPEntry> policies =
					_sapEntryLocalService.getCompanySAPEntries(
						_companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS);

				for (SAPEntry policy : policies) {
					String policyName = policy.getName();

					if (!StringUtil.startsWith(policyName, OAUTH2_SAP_PREFIX)) {
						continue;
					}

					String scopeName = policyName.substring(
						OAUTH2_SAP_PREFIX.length());

					names.add(scopeName);
				}
			}
			catch (Exception e) {
				_log.error(
					"Unable to publish OAuth2 scopes from SAP for company "
					+ _companyId,
					e);
			}

			return scopeMatcher.filter(names);
		}

		@Override
		public String describeScope(String scope, Locale locale) {
			try {
				String sapEntryName = OAUTH2_SAP_PREFIX + scope;

				SAPEntry sapEntry = _sapEntryLocalService.fetchSAPEntry(
					_companyId, sapEntryName);

				if (sapEntry == null) {
					return scope;
				}

				return sapEntry.getTitle(locale);
			}
			catch (Exception e) {
				_log.error("Unable to localize SAP scope " + scope, e);
			}

			return scope;
		}

		private final long _companyId;
	}

}