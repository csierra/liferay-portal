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

package com.liferay.oauth2.provider.jsonws.internal;

import com.liferay.oauth2.provider.jsonws.internal.constants.OAuth2JSONWSConstants;
import com.liferay.oauth2.provider.jsonws.internal.service.access.policy.scope.SAPEntryScope;
import com.liferay.oauth2.provider.jsonws.internal.service.access.policy.scope.SAPEntryScopeDescriptorFinder;
import com.liferay.oauth2.provider.scope.spi.scope.descriptor.ScopeDescriptor;
import com.liferay.oauth2.provider.scope.spi.scope.finder.ScopeFinder;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.security.service.access.policy.model.SAPEntry;
import com.liferay.portal.security.service.access.policy.service.SAPEntryLocalService;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Tomas Polesovsky
 */
@Component(
	immediate = true, property = "sap.entry.oauth2.prefix=OAUTH2_",
	service = OAuth2JSONWSScopePublisher.class
)
public class OAuth2JSONWSScopePublisher {

	public List<SAPEntryScope> getPublishedScopes(long companyId) {
		return new ArrayList<>(_companySAPEntryScopes.get(companyId));
	}

	public void publishScopes(long companyId) {
		try {
			List<SAPEntry> sapEntries =
				_sapEntryLocalService.getCompanySAPEntries(
					companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS);

			List<SAPEntryScope> sapEntryScopes = new ArrayList<>(
				sapEntries.size());

			for (SAPEntry sapEntry : sapEntries) {
				String sapEntryName = sapEntry.getName();

				if (!StringUtil.startsWith(
						sapEntryName, _sapEntryOAuth2Prefix)) {

					continue;
				}

				SAPEntryScope sapEntryScope = new SAPEntryScope(
					sapEntry.getSapEntryId(), sapEntryName,
					sapEntryName.substring(_sapEntryOAuth2Prefix.length()),
					sapEntry.getTitle());

				sapEntryScopes.add(sapEntryScope);
			}

			Dictionary<String, Object> properties = new Hashtable<>();

			properties.put("companyId", String.valueOf(companyId));
			properties.put(
				"osgi.jaxrs.name",
				OAuth2JSONWSConstants.OSGI_JAXRS_APPLICATION_NAME);

			_sapEntryScopeDescriptorFinderServiceRegistry.compute(
				companyId,
				(key, serviceRegistration) -> {
					if (serviceRegistration != null) {
						serviceRegistration.unregister();
					}

					serviceRegistration = _bundleContext.registerService(
						new String[] {
							ScopeDescriptor.class.getName(),
							ScopeFinder.class.getName()
						},
						new SAPEntryScopeDescriptorFinder(sapEntryScopes),
						properties);

					_companySAPEntryScopes.put(companyId, sapEntryScopes);

					return serviceRegistration;
				});
		}
		catch (Exception e) {
			_log.error(
				"Unable to get and register SAP entries for company " +
					companyId,
				e);
		}
	}

	@Activate
	protected void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		_bundleContext = bundleContext;

		_sapEntryOAuth2Prefix = MapUtil.getString(
			properties, "sap.entry.oauth2.prefix", _sapEntryOAuth2Prefix);
	}

	@Deactivate
	protected void deactivate() {
		for (ServiceRegistration serviceRegistration :
				_sapEntryScopeDescriptorFinderServiceRegistry.values()) {

			serviceRegistration.unregister();
		}

		_sapEntryScopeDescriptorFinderServiceRegistry.clear();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		OAuth2JSONWSScopePublisher.class);

	private BundleContext _bundleContext;
	private final Map<Long, List<SAPEntryScope>> _companySAPEntryScopes =
		new ConcurrentHashMap<>();

	@Reference
	private SAPEntryLocalService _sapEntryLocalService;

	private String _sapEntryOAuth2Prefix = "OAUTH2_";
	private final Map<Long, ServiceRegistration>
		_sapEntryScopeDescriptorFinderServiceRegistry =
			new ConcurrentHashMap<>();

}