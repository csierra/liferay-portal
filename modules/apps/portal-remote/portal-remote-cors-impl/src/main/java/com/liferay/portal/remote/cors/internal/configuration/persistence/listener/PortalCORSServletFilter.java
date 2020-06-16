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

package com.liferay.portal.remote.cors.internal.configuration.persistence.listener;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.configuration.persistence.listener.ConfigurationModelListener;
import com.liferay.portal.configuration.persistence.listener.ConfigurationModelListenerException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.servlet.BaseFilter;
import com.liferay.portal.kernel.servlet.HttpMethods;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.remote.cors.configuration.PortalCORSConfiguration;
import com.liferay.portal.remote.cors.internal.CORSSupport;
import com.liferay.portal.remote.cors.internal.path.pattern.matcher.PathPatternMatcher;
import com.liferay.portal.remote.cors.internal.path.pattern.matcher.PathPatternMatcherFactory;
import com.liferay.portal.remote.cors.internal.path.pattern.matcher.PatternTuple;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.Constants;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Arthur Chan
 */
@Component(
	immediate = true,
	property = {
		Constants.SERVICE_PID + "=com.liferay.portal.remote.cors.configuration.PortalCORSConfiguration",
		"before-filter=Auto Login Filter", "dispatcher=FORWARD",
		"dispatcher=REQUEST", "servlet-context-name=",
		"servlet-filter-name=Portal CORS Servlet Filter", "url-pattern=/*"
	},
	service = {Filter.class, ManagedServiceFactory.class}
)
public class PortalCORSServletFilter
	extends BaseFilter
	implements ConfigurationModelListener, ManagedServiceFactory {

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

	@Override
	public void destroy() {
	}

	@Override
	public String getName() {
		return StringPool.BLANK;
	}

	@Override
	public void init(FilterConfig filterConfig) {
		ServletContext servletContext = filterConfig.getServletContext();

		_contextPath = servletContext.getContextPath();
	}

	@Override
	public boolean isFilterEnabled(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) {

		return CORSSupport.isCORSRequest(httpServletRequest::getHeader);
	}

	@Override
	public void onAfterDelete(String pid)
		throws ConfigurationModelListenerException {
	}

	@Override
	public void onAfterSave(String pid, Dictionary<String, Object> properties)
		throws ConfigurationModelListenerException {
	}

	@Override
	public void onBeforeDelete(String pid)
		throws ConfigurationModelListenerException {
	}

	@Override
	public void onBeforeSave(
			String pid, Dictionary<String, Object> newProperties)
		throws ConfigurationModelListenerException {

		HashSet<String> urlPathPatternsSet = new HashSet<>();
		HashSet<String> duplicatedUrlPathPatterns = new HashSet<>();

		long companyId = GetterUtil.getLong(newProperties.get("companyId"));

		for (Map.Entry<String, Dictionary<String, ?>> entry :
				_configurationPidsProperties.entrySet()) {

			if (StringUtil.equals(pid, entry.getKey())) {
				continue;
			}

			Dictionary<String, ?> properties = entry.getValue();

			if (companyId != GetterUtil.getLong(properties.get("companyId"))) {
				continue;
			}

			PortalCORSConfiguration portalCORSConfiguration =
				ConfigurableUtil.createConfigurable(
					PortalCORSConfiguration.class, properties);

			String[] urlPathPatterns =
				portalCORSConfiguration.filterMappingURLPatterns();

			for (String urlPathPattern : urlPathPatterns) {
				if (!urlPathPatternsSet.add(urlPathPattern)) {
					duplicatedUrlPathPatterns.add(urlPathPattern);
				}
			}
		}

		if (!duplicatedUrlPathPatterns.isEmpty()) {
			throw new ConfigurationModelListenerException(
				"Duplicated url path patterns: " + duplicatedUrlPathPatterns,
				PortalCORSConfiguration.class, PortalCORSServletFilter.class,
				newProperties);
		}
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

	@Override
	protected Log getLog() {
		return _log;
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

	@Override
	protected void processFilter(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, FilterChain filterChain)
		throws Exception {

		long companyId = _portal.getCompanyId(httpServletRequest);

		if (companyId == 0) {
			return;
		}

		PathPatternMatcher<Map<String, String>> pathPatternMatcher =
			_pathPatternMatchers.get(companyId);

		if (pathPatternMatcher == null) {
			pathPatternMatcher = _pathPatternMatchers.get(0L);

			if (pathPatternMatcher == null) {
				filterChain.doFilter(httpServletRequest, httpServletResponse);

				return;
			}
		}

		PatternTuple<Map<String, String>> patternTuple =
			pathPatternMatcher.getPatternPackage(getURI(httpServletRequest));

		if (patternTuple != null) {
			corsSupport.setCORSHeaders(patternTuple.getValue());
		}

		if (StringUtil.equals(
				HttpMethods.OPTIONS, httpServletRequest.getMethod())) {

			if (corsSupport.isValidCORSPreflightRequest(
					httpServletRequest::getHeader)) {

				corsSupport.writeResponseHeaders(
					httpServletRequest::getHeader,
					httpServletResponse::setHeader);
			}

			return;
		}

		if (corsSupport.isValidCORSRequest(
				httpServletRequest.getMethod(),
				httpServletRequest::getHeader)) {

			corsSupport.writeResponseHeaders(
				httpServletRequest::getHeader, httpServletResponse::setHeader);
		}

		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}

	protected final CORSSupport corsSupport = new CORSSupport();

	private void _rebuild() {
		_rebuild(CompanyConstants.SYSTEM);

		for (long companyId : _pathPatternMatchers.keySet()) {
			if (companyId != CompanyConstants.SYSTEM) {
				_rebuild(companyId);
			}
		}
	}

	private void _rebuild(long companyId) {
		HashSet<String> processedNames = new HashSet<>();
		HashMap<String, Map<String, String>> patternsHeadersMap =
			new HashMap<>();

		for (Dictionary<String, ?> properties :
				_configurationPidsProperties.values()) {

			if (companyId != GetterUtil.getLong(properties.get("companyId"))) {
				continue;
			}

			PortalCORSConfiguration portalCORSConfiguration =
				ConfigurableUtil.createConfigurable(
					PortalCORSConfiguration.class, properties);

			processedNames.add(portalCORSConfiguration.name());

			Map<String, String> corsHeaders = CORSSupport.buildCORSHeaders(
				portalCORSConfiguration.headers());

			for (String urlPathPattern :
					portalCORSConfiguration.filterMappingURLPatterns()) {

				patternsHeadersMap.put(urlPathPattern, corsHeaders);
			}
		}

		for (Dictionary<String, ?> properties :
				_configurationPidsProperties.values()) {

			if (companyId != CompanyConstants.SYSTEM) {
				continue;
			}

			PortalCORSConfiguration portalCORSConfiguration =
				ConfigurableUtil.createConfigurable(
					PortalCORSConfiguration.class, properties);

			if (processedNames.contains(portalCORSConfiguration.name())) {
				continue;
			}

			Map<String, String> corsHeaders = CORSSupport.buildCORSHeaders(
				portalCORSConfiguration.headers());

			for (String urlPathPattern :
					portalCORSConfiguration.filterMappingURLPatterns()) {

				patternsHeadersMap.putIfAbsent(urlPathPattern, corsHeaders);
			}
		}

		_pathPatternMatchers.put(
			companyId,
			_pathPatternMatcherFactory.createPatternMatcher(
				patternsHeadersMap));
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PortalCORSServletFilter.class);

	private final Map<String, Dictionary<String, ?>>
		_configurationPidsProperties = new ConcurrentHashMap<>();
	private String _contextPath;

	@Reference
	private Http _http;

	@Reference
	private PathPatternMatcherFactory<Map<String, String>>
		_pathPatternMatcherFactory;

	private final Map<Long, PathPatternMatcher<Map<String, String>>>
		_pathPatternMatchers = new ConcurrentHashMap<>();

	@Reference
	private Portal _portal;

}