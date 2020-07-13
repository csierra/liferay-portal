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
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.remote.cors.configuration.PortalCORSConfiguration;
import com.liferay.portal.remote.cors.internal.CORSSupport;
import com.liferay.portal.remote.cors.internal.path.pattern.matcher.PathPatternMatcher;
import com.liferay.portal.remote.cors.internal.path.pattern.matcher.PathPatternMatcherFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.osgi.framework.Constants;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Arthur Chan
 * @author Carlos Sierra Andrés
 */
@Component(
	immediate = true,
	property = Constants.SERVICE_PID + "=com.liferay.portal.remote.cors.configuration.PortalCORSConfiguration",
	service = {
		ManagedServiceFactory.class, PortalCORSConfigurationManager.class
	}
)
public class PortalCORSConfigurationManager implements ManagedServiceFactory {

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
	}

	public Map<String, Dictionary<String, ?>> getConfigurationPidsProperties() {
		return _configurationPidsProperties;
	}

	@Override
	public String getName() {
		return StringPool.BLANK;
	}

	public PathPatternMatcher<CORSSupport> getPathPatternMatcher(
		long companyId) {

		return _pathPatternMatchers.get(companyId);
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
		_rebuild();
	}

	protected String getURI(HttpServletRequest httpServletRequest) {
		String uri = httpServletRequest.getRequestURI();

		if (Validator.isNotNull(_contextPath) &&
			!_contextPath.equals(StringPool.SLASH) &&
			uri.startsWith(_contextPath)) {

			uri = uri.substring(_contextPath.length());
		}

		return _http.normalizePath(uri);
	}

	private void _mergeCorsConfiguration(
		Map<String, CORSSupport> pathPatternsHeadersMap,
		Dictionary<String, ?> properties) {

		PortalCORSConfiguration portalCORSConfiguration =
			ConfigurableUtil.createConfigurable(
				PortalCORSConfiguration.class, properties);

		Map<String, String> corsHeaders = CORSSupport.buildCORSHeaders(
			portalCORSConfiguration.headers());

		CORSSupport corsSupport = new CORSSupport();

		corsSupport.setCORSHeaders(corsHeaders);

		for (String pathPattern :
				portalCORSConfiguration.filterMappingURLPatterns()) {

			pathPatternsHeadersMap.putIfAbsent(pathPattern, corsSupport);
		}
	}

	private void _mergeSystemCompanyProperties(
		Map<String, CORSSupport> pathPatternsHeadersMap) {

		List<Dictionary<String, ?>> systemProperties = new ArrayList<>();

		for (Dictionary<String, ?> properties :
				_configurationPidsProperties.values()) {

			long companyId = GetterUtil.getLong(properties.get("companyId"));

			if (companyId == CompanyConstants.SYSTEM) {
				systemProperties.add(properties);
			}
		}

		if (systemProperties.isEmpty()) {
			systemProperties.add(new HashMapDictionary<>());
		}

		for (Dictionary<String, ?> properties : systemProperties) {
			_mergeCorsConfiguration(pathPatternsHeadersMap, properties);
		}
	}

	private void _rebuild() {
		_rebuild(CompanyConstants.SYSTEM);

		for (long companyId : _pathPatternMatchers.keySet()) {
			if (companyId != CompanyConstants.SYSTEM) {
				_rebuild(companyId);
			}
		}
	}

	private void _rebuild(long companyId) {
		HashMap<String, CORSSupport> pathPatternsHeadersMap = new HashMap<>();

		for (Dictionary<String, ?> properties :
				_configurationPidsProperties.values()) {

			if (companyId != GetterUtil.getLong(properties.get("companyId"))) {
				continue;
			}

			_mergeCorsConfiguration(pathPatternsHeadersMap, properties);
		}

		if (pathPatternsHeadersMap.isEmpty()) {
			if (companyId != CompanyConstants.SYSTEM) {
				_pathPatternMatchers.remove(companyId);

				return;
			}

			_mergeSystemCompanyProperties(pathPatternsHeadersMap);
		}
		else if (companyId != CompanyConstants.SYSTEM) {
			_mergeSystemCompanyProperties(pathPatternsHeadersMap);
		}

		_pathPatternMatchers.put(
			companyId,
			_pathPatternMatcherFactory.createPatternMatcher(
				pathPatternsHeadersMap));
	}

	private final Map<String, Dictionary<String, ?>>
		_configurationPidsProperties = Collections.synchronizedMap(
			new LinkedHashMap<>());
	private String _contextPath;

	@Reference
	private Http _http;

	@Reference
	private PathPatternMatcherFactory _pathPatternMatcherFactory;

	private final Map<Long, PathPatternMatcher<CORSSupport>>
		_pathPatternMatchers = new ConcurrentHashMap<>();

	@Reference
	private Portal _portal;

}