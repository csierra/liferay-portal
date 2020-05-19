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

package com.liferay.portal.remote.cors.internal.servlet.filter;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.servlet.HttpMethods;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.remote.cors.internal.CORSSupport;
import com.liferay.portal.remote.cors.internal.url.path.pattern.map.URLPathPatternMap;

import java.io.IOException;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
public class PortalCORSServletFilter implements Filter, ManagedServiceFactory {

	@Override
	public void deleted(String pid) {
		long companyId = _pidToCompanyId.remove(pid);

		if (companyId == CompanyConstants.SYSTEM) {
			_corsSystemConfiguration.remove(pid);

			for (long existingCompanyId : _urlPathPatternMaps.keySet()) {
				buildURLPathPatternMap(
					_corsInstanceConfigurations.get(existingCompanyId),
					_corsSystemConfiguration, existingCompanyId);
			}
		}
		else {
			Map<String, CORSFactoryConfiguration> corsInstanceConfiguration =
				_corsInstanceConfigurations.computeIfAbsent(
					companyId, key -> new HashMap<>());

			corsInstanceConfiguration.remove(pid);

			buildURLPathPatternMap(
				_corsInstanceConfigurations.get(companyId),
				_corsSystemConfiguration, companyId);
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

		if (corsSupport.isCORSRequest(httpServletRequest::getHeader)) {
			try {
				processCORSRequest(
					httpServletRequest, (HttpServletResponse)servletResponse);
			}
			catch (Exception exception) {
				throw new ServletException(exception);
			}
		}

		filterChain.doFilter(servletRequest, servletResponse);
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
	public void updated(String pid, Dictionary<String, ?> properties)
		throws ConfigurationException {

		long companyId = GetterUtil.getLong(
			properties.get("companyId"), CompanyConstants.SYSTEM);

		_pidToCompanyId.put(pid, companyId);

		Map<String, String> corsHeaders = CORSSupport.buildCORSHeaders(
			(String[])properties.get("headers"));

		String[] urlPathPatterns = (String[])properties.get(
			"filter.mapping.url.pattern");

		CORSFactoryConfiguration corsFactoryConfiguration =
			new CORSFactoryConfiguration();

		corsFactoryConfiguration.setCORSHeaders(corsHeaders);

		for (String urlPathPattern : urlPathPatterns) {
			corsFactoryConfiguration.addURLPathPattern(urlPathPattern);
		}

		if (companyId == CompanyConstants.SYSTEM) {
			_corsSystemConfiguration.put(pid, corsFactoryConfiguration);

			for (long existingCompanyId : _urlPathPatternMaps.keySet()) {
				buildURLPathPatternMap(
					_corsInstanceConfigurations.get(existingCompanyId),
					_corsSystemConfiguration, existingCompanyId);
			}
		}
		else {
			Map<String, CORSFactoryConfiguration> corsInstanceConfiguration =
				_corsInstanceConfigurations.computeIfAbsent(
					companyId, key -> new HashMap<>());

			corsInstanceConfiguration.put(pid, corsFactoryConfiguration);

			buildURLPathPatternMap(
				_corsInstanceConfigurations.get(companyId),
				_corsSystemConfiguration, companyId);
		}
	}

	protected void buildURLPathPatternMap(
		Map<String, CORSFactoryConfiguration> corsInstanceConfiguration,
		Map<String, CORSFactoryConfiguration> corsSystemConfiguration,
		long companyId) {

		URLPathPatternMap<Map<String, String>> urlPathPatternMap =
			_urlPathPatternMaps.get(companyId);

		if (urlPathPatternMap != null) {
			urlPathPatternMap.clear();
		}
		else {
			urlPathPatternMap = new URLPathPatternMap<>();

			_urlPathPatternMaps.put(companyId, urlPathPatternMap);
		}

		Set<String> patternsInInstance = new HashSet<>();

		for (Map.Entry<String, CORSFactoryConfiguration> instanceEntry :
				corsInstanceConfiguration.entrySet()) {

			CORSFactoryConfiguration corsFactoryConfiguration =
				instanceEntry.getValue();

			patternsInInstance.addAll(
				corsFactoryConfiguration._urlPathPatterns);

			for (String pattern : corsFactoryConfiguration._urlPathPatterns) {
				urlPathPatternMap.insert(
					pattern, corsFactoryConfiguration._corsHeaders);
			}
		}

		for (Map.Entry<String, CORSFactoryConfiguration> systemEntry :
				corsSystemConfiguration.entrySet()) {

			CORSFactoryConfiguration corsFactoryConfiguration =
				systemEntry.getValue();

			for (String pattern : corsFactoryConfiguration._urlPathPatterns) {
				if (!patternsInInstance.contains(pattern)) {
					urlPathPatternMap.insert(
						pattern, corsFactoryConfiguration._corsHeaders);
				}
			}
		}
	}

	protected String getURI(HttpServletRequest httpServletRequest) {
		String uri = httpServletRequest.getRequestURI();

		if (Validator.isNotNull(_contextPath) &&
			!_contextPath.equals(StringPool.SLASH) &&
			uri.startsWith(_contextPath)) {

			uri = uri.substring(_contextPath.length());
		}

		return HttpUtil.normalizePath(uri);
	}

	protected void processCORSPreflightRequest(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws Exception {

		if (corsSupport.isValidCORSPreflightRequest(
				httpServletRequest::getHeader)) {

			corsSupport.writeResponseHeaders(
				httpServletRequest::getHeader, httpServletResponse::setHeader);
		}
	}

	protected void processCORSRequest(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws Exception {

		Long companyId = (Long)httpServletRequest.getAttribute("COMPANY_ID");

		String uri = getURI(httpServletRequest);

		URLPathPatternMap<Map<String, String>> urlPathPatternMap =
			_urlPathPatternMaps.get(companyId);

		List<Map<String, String>> corsHeaders =
			urlPathPatternMap.getCargosOfMatchingPattern(uri);

		if (corsHeaders.isEmpty()) {
			return;
		}

		corsSupport.setCORSHeaders(corsHeaders);

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
	}

	protected final CORSSupport corsSupport = new CORSSupport();

	private String _contextPath;
	private final Map<Long, Map<String, CORSFactoryConfiguration>>
		_corsInstanceConfigurations = new HashMap<>();
	private final Map<String, CORSFactoryConfiguration>
		_corsSystemConfiguration = new HashMap<>();
	private final Map<String, Long> _pidToCompanyId = new HashMap<>();
	private final Map<Long, URLPathPatternMap<Map<String, String>>>
		_urlPathPatternMaps = new HashMap<>();

	private class CORSFactoryConfiguration {

		public CORSFactoryConfiguration() {
			_urlPathPatterns = new HashSet<>();
		}

		public void addURLPathPattern(String urlPathPattern) {
			_urlPathPatterns.add(urlPathPattern);
		}

		public void setCORSHeaders(Map<String, String> corsHeaders) {
			_corsHeaders = corsHeaders;
		}

		private Map<String, String> _corsHeaders;
		private final Set<String> _urlPathPatterns;

	}

}