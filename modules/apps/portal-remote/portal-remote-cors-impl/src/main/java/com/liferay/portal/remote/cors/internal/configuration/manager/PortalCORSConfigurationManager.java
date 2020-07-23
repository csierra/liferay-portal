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

package com.liferay.portal.remote.cors.internal.configuration.manager;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.remote.cors.configuration.PortalCORSConfiguration;
import com.liferay.portal.remote.cors.internal.CORSSupport;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.osgi.framework.Constants;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 * @author Carlos Sierra Andrés
 * @author Arthur Chan
 */
@Component(
	immediate = true,
	property = Constants.SERVICE_PID + "=com.liferay.portal.remote.cors.configuration.PortalCORSConfiguration",
	service = {
		ManagedServiceFactory.class, PortalCORSConfigurationManager.class
	}
)
public class PortalCORSConfigurationManager implements ManagedServiceFactory {

	public static boolean isExtensionPattern(String pathPattern) {
		if ((pathPattern.length() < 3) || (pathPattern.charAt(0) != '*') ||
			(pathPattern.charAt(1) != '.')) {

			return false;
		}

		for (int i = 2; i < pathPattern.length(); ++i) {
			if (pathPattern.charAt(i) == '/') {
				return false;
			}

			if (pathPattern.charAt(i) == '.') {
				return false;
			}
		}

		return true;
	}

	public static boolean isWildcardPattern(String pathPattern) {
		if ((pathPattern.length() < 2) || (pathPattern.charAt(0) != '/') ||
			(pathPattern.charAt(pathPattern.length() - 1) != '*') ||
			(pathPattern.charAt(pathPattern.length() - 2) != '/')) {

			return false;
		}

		try {
			String path = pathPattern.substring(0, pathPattern.length() - 1);

			URI uri = new URI("https://test" + path);

			if (!path.contentEquals(uri.getPath())) {
				return false;
			}
		}
		catch (URISyntaxException uriSyntaxException) {
			return false;
		}

		return true;
	}

	@Override
	public void deleted(String pid) {
		Dictionary<String, ?> properties = _configurationPidsProperties.remove(
			pid);

		long companyId = GetterUtil.getLong(properties.get("companyId"));

		if (companyId == CompanyConstants.SYSTEM) {
			_rebuild();
		}
		else {
			_rebuild(companyId);
		}

		if (_configurationPidsProperties.isEmpty()) {
			_rebuildDefault();
		}
	}

	public Map<String, Dictionary<String, ?>> getConfigurationPidsProperties() {
		return _configurationPidsProperties;
	}

	public Map<String, CORSSupport> getExactPatternCORSSupports(
		long companyId) {

		return _exactPatternCORSSupports.get(companyId);
	}

	public Map<String, CORSSupport> getExtensionPatternCORSSupports(
		long companyId) {

		return _extensionPatternCORSSuports.get(companyId);
	}

	@Override
	public String getName() {
		return StringPool.BLANK;
	}

	public Map<String, CORSSupport> getWildcardPatternCORSSupports(
		long companyId) {

		return _wildcardPatternCORSSupports.get(companyId);
	}

	@Override
	public void updated(String pid, Dictionary<String, ?> properties)
		throws ConfigurationException {

		Dictionary<String, ?> oldProperties = _configurationPidsProperties.put(
			pid, properties);

		long companyId = GetterUtil.getLong(
			properties.get("companyId"), CompanyConstants.SYSTEM);

		if (companyId == CompanyConstants.SYSTEM) {
			_rebuild();

			return;
		}

		if (oldProperties != null) {
			long oldCompanyId = GetterUtil.getLong(
				oldProperties.get("companyId"));

			if (oldCompanyId == CompanyConstants.SYSTEM) {
				_rebuild();

				return;
			}

			if (oldCompanyId != companyId) {
				_rebuild(oldCompanyId);
			}
		}

		_rebuild(companyId);
	}

	@Activate
	protected void activate() {
		_rebuildDefault();
	}

	private void _addCORSConfigurationToPatternCORSSupport(
		PortalCORSConfiguration portalCORSConfiguration,
		Map<String, CORSSupport> exactPatternCORSSupport,
		Map<String, CORSSupport> extensionPatternCORSSupport,
		Map<String, CORSSupport> wildcardPatternCORSSupport) {

		Map<String, String> corsHeaders = CORSSupport.buildCORSHeaders(
			portalCORSConfiguration.headers());

		CORSSupport corsSupport = new CORSSupport();

		corsSupport.setCORSHeaders(corsHeaders);

		for (String pattern :
				portalCORSConfiguration.filterMappingURLPatterns()) {

			if (isWildcardPattern(pattern)) {
				if (!wildcardPatternCORSSupport.containsKey(pattern)) {
					wildcardPatternCORSSupport.put(
						pattern.substring(0), corsSupport);
				}

				continue;
			}

			if (isExtensionPattern(pattern)) {
				if (!extensionPatternCORSSupport.containsKey(pattern)) {
					extensionPatternCORSSupport.put(
						pattern.substring(0), corsSupport);
				}

				continue;
			}

			if (!exactPatternCORSSupport.containsKey(pattern)) {
				exactPatternCORSSupport.put(pattern, corsSupport);
			}
		}
	}

	private void _addCORSSupport(
		Map<String, CORSSupport> exactPatternCORSSupport,
		Map<String, CORSSupport> extensionPatternCORSSupport,
		Map<String, CORSSupport> wildcardPatternCORSSupport, long companyId) {

		_exactPatternCORSSupports.put(companyId, exactPatternCORSSupport);
		_extensionPatternCORSSuports.put(
			companyId, extensionPatternCORSSupport);
		_wildcardPatternCORSSupports.put(companyId, wildcardPatternCORSSupport);
	}

	private boolean _isCORSSupportEmpty(
		Map<String, CORSSupport> exactPatternCORSSupport,
		Map<String, CORSSupport> extensionPatternCORSSupport,
		Map<String, CORSSupport> wildcardPatternCORSSupport) {

		if (exactPatternCORSSupport.isEmpty() &&
			extensionPatternCORSSupport.isEmpty() &&
			wildcardPatternCORSSupport.isEmpty()) {

			return true;
		}

		return false;
	}

	private void _mergeCORSConfiguration(
		Map<String, CORSSupport> exactPatternCORSSupport,
		Map<String, CORSSupport> extensionPatternCORSSupport,
		Map<String, CORSSupport> wildcardPatternCORSSupport, long companyId) {

		for (Dictionary<String, ?> properties :
				_configurationPidsProperties.values()) {

			if (companyId != GetterUtil.getLong(properties.get("companyId"))) {
				continue;
			}

			PortalCORSConfiguration portalCORSConfiguration =
				ConfigurableUtil.createConfigurable(
					PortalCORSConfiguration.class, properties);

			_addCORSConfigurationToPatternCORSSupport(
				portalCORSConfiguration, exactPatternCORSSupport,
				extensionPatternCORSSupport, wildcardPatternCORSSupport);
		}
	}

	private void _rebuild() {
		Map<String, CORSSupport> exactPatternCORSSupport = new HashMap<>();
		Map<String, CORSSupport> extensionPatternCORSSupport = new HashMap<>();
		Map<String, CORSSupport> wildcardPatternCORSSupport = new HashMap<>();

		_mergeCORSConfiguration(
			exactPatternCORSSupport, extensionPatternCORSSupport,
			wildcardPatternCORSSupport, CompanyConstants.SYSTEM);

		// A system level CORSSupport is always required even if it's empty

		_addCORSSupport(
			exactPatternCORSSupport, extensionPatternCORSSupport,
			wildcardPatternCORSSupport, CompanyConstants.SYSTEM);

		for (long companyId : _exactPatternCORSSupports.keySet()) {
			if (companyId != CompanyConstants.SYSTEM) {
				_rebuild(companyId);
			}
		}
	}

	private void _rebuild(long companyId) {
		Map<String, CORSSupport> exactPatternCORSSupport = new HashMap<>();
		Map<String, CORSSupport> extensionPatternCORSSupport = new HashMap<>();
		Map<String, CORSSupport> wildcardPatternCORSSupport = new HashMap<>();

		// If there are same patterns in both instance settings and system
		// settings, the pattern in instance settings will be used.

		_mergeCORSConfiguration(
			exactPatternCORSSupport, extensionPatternCORSSupport,
			wildcardPatternCORSSupport, companyId);

		if (_isCORSSupportEmpty(
				exactPatternCORSSupport, extensionPatternCORSSupport,
				wildcardPatternCORSSupport)) {

			_removeCORSSupport(companyId);

			return;
		}

		// If there are patterns not in instance settings but in system
		// settings, these patterns will also be used.

		_mergeCORSConfiguration(
			exactPatternCORSSupport, extensionPatternCORSSupport,
			wildcardPatternCORSSupport, CompanyConstants.SYSTEM);

		_addCORSSupport(
			exactPatternCORSSupport, extensionPatternCORSSupport,
			wildcardPatternCORSSupport, companyId);
	}

	/**
	 * Backward compatibility with current portal behavior:
	 * Add default configuration to CORSSupport when there is no entry in
	 * properties map, because empty properties map means no CORS
	 * settings is configured.
	 */
	private void _rebuildDefault() {
		Map<String, CORSSupport> exactPatternCORSSupport = new HashMap<>();
		Map<String, CORSSupport> extensionPatternCORSSupport = new HashMap<>();
		Map<String, CORSSupport> wildcardPatternCORSSupport = new HashMap<>();

		_addCORSConfigurationToPatternCORSSupport(
			ConfigurableUtil.createConfigurable(
				PortalCORSConfiguration.class, new HashMapDictionary<>()),
			exactPatternCORSSupport, extensionPatternCORSSupport,
			wildcardPatternCORSSupport);

		_addCORSSupport(
			exactPatternCORSSupport, extensionPatternCORSSupport,
			wildcardPatternCORSSupport, CompanyConstants.SYSTEM);
	}

	private void _removeCORSSupport(long companyId) {
		_exactPatternCORSSupports.remove(companyId);
		_extensionPatternCORSSuports.remove(companyId);
		_wildcardPatternCORSSupports.remove(companyId);
	}

	private final Map<String, Dictionary<String, ?>>
		_configurationPidsProperties = Collections.synchronizedMap(
			new LinkedHashMap<>());
	private final Map<Long, Map<String, CORSSupport>>
		_exactPatternCORSSupports = new LinkedHashMap<>();
	private final Map<Long, Map<String, CORSSupport>>
		_extensionPatternCORSSuports = new LinkedHashMap<>();
	private final Map<Long, Map<String, CORSSupport>>
		_wildcardPatternCORSSupports = new LinkedHashMap<>();

}