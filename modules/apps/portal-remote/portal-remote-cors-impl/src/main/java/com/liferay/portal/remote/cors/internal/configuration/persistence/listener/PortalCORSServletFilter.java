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
import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.servlet.HttpMethods;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.remote.cors.configuration.PortalCORSConfiguration;
import com.liferay.portal.remote.cors.internal.CORSSupport;
import com.liferay.portal.remote.cors.internal.path.pattern.matcher.DynamicPathPatternMatcher;
import com.liferay.portal.remote.cors.internal.path.pattern.matcher.PathPatternMatcher;
import com.liferay.portal.remote.cors.internal.path.pattern.matcher.PatternPackage;
import com.liferay.portal.remote.cors.internal.path.pattern.matcher.StaticPathPatternMatcher;

import java.io.IOException;

import java.util.Comparator;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
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
	implements ConfigurationModelListener, Filter, ManagedServiceFactory {

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
	public final void doFilter(
			ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain)
		throws IOException, ServletException {

		HttpServletRequest httpServletRequest =
			(HttpServletRequest)servletRequest;

		if (CORSSupport.isCORSRequest(httpServletRequest::getHeader)) {
			try {
				processCORSRequest(
					httpServletRequest, (HttpServletResponse)servletResponse,
					filterChain);
			}
			catch (Exception exception) {
				throw new ServletException(exception);
			}
		}
		else {
			filterChain.doFilter(servletRequest, servletResponse);
		}
	}

	@Override
	public String getName() {
		return StringPool.BLANK;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		ServletContext servletContext = filterConfig.getServletContext();

		_contextPath = servletContext.getContextPath();
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

	protected String getURI(HttpServletRequest httpServletRequest) {
		String uri = httpServletRequest.getRequestURI();

		if (Validator.isNotNull(_contextPath) &&
			!_contextPath.equals(StringPool.SLASH) &&
			uri.startsWith(_contextPath)) {

			uri = uri.substring(_contextPath.length());
		}

		return _http.normalizePath(uri);
	}

	protected void processCORSRequest(
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

		PatternPackage<Map<String, String>> patternPackage =
			pathPatternMatcher.getPatternPackage(getURI(httpServletRequest));

		if (patternPackage != null) {
			List<Map<String, String>> headersList =
				patternPackage.getCargoList();

			corsSupport.setCORSHeaders(headersList.get(0));
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

	private PathPatternMatcher<Map<String, String>> _createPatternMatcher(
		HashMap<String, Map<String, String>> patternsHeadersMap) {

		PathPatternMatcher<Map<String, String>> pathPatternMatcher;

		if (patternsHeadersMap.size() > 64) {
			pathPatternMatcher = new DynamicPathPatternMatcher<>();
		}
		else {
			Set<String> keySet = patternsHeadersMap.keySet();

			Stream<String> stream = keySet.stream();

			pathPatternMatcher = new StaticPathPatternMatcher<>(
				stream.map(
					String::length
				).max(
					Comparator.naturalOrder()
				).orElse(
					0
				));
		}

		for (Map.Entry<String, Map<String, String>> entry :
				patternsHeadersMap.entrySet()) {

			pathPatternMatcher.insert(entry.getKey(), entry.getValue());
		}

		return pathPatternMatcher;
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
			companyId, _createPatternMatcher(patternsHeadersMap));
	}

	private final Map<String, Dictionary<String, ?>>
		_configurationPidsProperties = new ConcurrentHashMap<>();
	private String _contextPath;

	@Reference
	private Http _http;

	private final Map<Long, PathPatternMatcher<Map<String, String>>>
		_pathPatternMatchers = new ConcurrentHashMap<>();

	@Reference
	private Portal _portal;

}