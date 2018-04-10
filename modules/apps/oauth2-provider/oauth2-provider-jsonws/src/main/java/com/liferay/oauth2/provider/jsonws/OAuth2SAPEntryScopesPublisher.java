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

import com.liferay.oauth2.provider.scope.spi.application.descriptor.ApplicationDescriptor;
import com.liferay.oauth2.provider.scope.spi.scope.descriptor.ScopeDescriptor;
import com.liferay.oauth2.provider.scope.spi.scope.finder.ScopeFinder;
import com.liferay.portal.instance.lifecycle.BasePortalInstanceLifecycleListener;
import com.liferay.portal.instance.lifecycle.PortalInstanceLifecycleListener;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.ResourceBundleLoader;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.security.service.access.policy.model.SAPEntry;
import com.liferay.portal.security.service.access.policy.service.SAPEntryLocalService;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

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
	immediate = true, property = "oauth2.portal.jsonws.application.name=JSONWS",
	service = OAuth2SAPEntryScopesPublisher.class
)
public class OAuth2SAPEntryScopesPublisher {

	@Activate
	public void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		_bundleContext = bundleContext;

		_oAuth2PortalJSONWSApplicationName = MapUtil.getString(
			properties, "oauth2.portal.jsonws.application.name",
			_oAuth2PortalJSONWSApplicationName);

		Dictionary<String, Object> applicationDescriptorProperties =
			new Hashtable<>();

		applicationDescriptorProperties.put(
			"osgi.jaxrs.name", _oAuth2PortalJSONWSApplicationName);

		_serviceRegistrations.add(
			bundleContext.registerService(
				ApplicationDescriptor.class, this::describeApplication,
				applicationDescriptorProperties));

		_serviceRegistrations.add(
			bundleContext.registerService(
				PortalInstanceLifecycleListener.class,
				new BasePortalInstanceLifecycleListener() {

					public void portalInstanceRegistered(Company company) {
						registerPortalInstance(company.getCompanyId());
					}

				},
				null));
	}

	@Deactivate
	public void deactivate() {
		for (ServiceRegistration serviceRegistration : _serviceRegistrations) {
			serviceRegistration.unregister();
		}

		_serviceRegistrations.clear();

		for (ServiceRegistration serviceRegistration :
				_sapEntryScopeDescriptorFinderServiceRegistry.values()) {

			serviceRegistration.unregister();
		}

		_sapEntryScopeDescriptorFinderServiceRegistry.clear();
	}

	public String describeApplication(Locale locale) {
		String languageId = LocaleUtil.toLanguageId(locale);

		ResourceBundle resourceBundle =
			_resourceBundleLoader.loadResourceBundle(languageId);

		String key =
			"oauth2.application.description." +
				_oAuth2PortalJSONWSApplicationName;

		return ResourceBundleUtil.getString(resourceBundle, key);
	}

	public String getOAuth2PortalJSONWSApplicationName() {
		return _oAuth2PortalJSONWSApplicationName;
	}

	public void publishSAPEntries(long companyId) {
		Dictionary<String, Object> properties = new Hashtable<>();

		properties.put("osgi.jaxrs.name", _oAuth2PortalJSONWSApplicationName);
		properties.put("companyId", String.valueOf(companyId));

		_sapEntryScopeDescriptorFinderServiceRegistry.compute(
			companyId,
			(__, serviceRegistration) -> {
				if (serviceRegistration != null) {
					serviceRegistration.unregister();
				}

				serviceRegistration = _bundleContext.registerService(
					new String[] {
						ScopeDescriptor.class.getName(),
						ScopeFinder.class.getName()
					},
					new SAPEntryScopeDescriptorFinder(
						_sapEntryScopeRegistry.getSAPEntryScopes(companyId)),
					properties);

				return serviceRegistration;
			});
	}

	public void registerPortalInstance(long companyId) {
		try {
			registerSAPEntries(companyId);
			publishSAPEntries(companyId);
		}
		catch (Exception e) {
			_log.error(
				"Unable to get and register SAP entries for company " +
					companyId,
				e);
		}
	}

	protected void registerSAPEntries(long companyId) {
		List<SAPEntry> sapEntries = _sapEntryLocalService.getCompanySAPEntries(
			companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		for (SAPEntry sapEntry : sapEntries) {
			_sapEntryScopeRegistry.registerSAPEntry(sapEntry);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		OAuth2SAPEntryScopesPublisher.class);

	private BundleContext _bundleContext;
	private String _oAuth2PortalJSONWSApplicationName = "JSONWS";

	@Reference(
		target = "(bundle.symbolic.name=com.liferay.oauth2.provider.jsonws)"
	)
	private ResourceBundleLoader _resourceBundleLoader;

	@Reference
	private SAPEntryLocalService _sapEntryLocalService;

	private final ConcurrentHashMap<Long, ServiceRegistration>
		_sapEntryScopeDescriptorFinderServiceRegistry =
			new ConcurrentHashMap<>();

	@Reference
	private SAPEntryScopeRegistry _sapEntryScopeRegistry;

	private final List<ServiceRegistration> _serviceRegistrations =
		new CopyOnWriteArrayList<>();

}