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
import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.servlet.HttpMethods;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.remote.cors.configuration.PortalCORSConfiguration;
import com.liferay.portal.remote.cors.internal.CORSSupport;
import com.liferay.portal.remote.cors.internal.path.pattern.matcher.PathPatternMatcher;
import com.liferay.portal.remote.cors.internal.path.pattern.matcher.PatternTuple;

import java.io.IOException;

import java.util.ArrayList;
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
public class PortalCORSServletFilter implements Filter, ManagedServiceFactory {

	@Override
	public void deleted(String pid) {
		Long companyId = _pidToCompany.remove(pid);

		if (companyId == null) {
			return;
		}

		CORSFactoryConfigurationStore corsFactoryConfigurationStore =
			_corsFactoryConfigurationStores.get(companyId);

		if (corsFactoryConfigurationStore != null) {
			corsFactoryConfigurationStore.removeCORSFactoryConfiguration(pid);
		}

		if (corsFactoryConfigurationStore.isEmpty()) {
			_corsFactoryConfigurationStores.remove(companyId);
		}

		_build(corsFactoryConfigurationStore);

		if (_corsFactoryConfigurationStores.isEmpty()) {
			_build(_defaultCORSFactoryConfigurationStore());
		}
	}

	@Override
	public void destroy() {
		_corsFactoryConfigurationStores.clear();
		_pidToCompany.clear();
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
	public void updated(String pid, Dictionary<String, ?> properties)
		throws ConfigurationException {

		long companyId = GetterUtil.getLong(
			properties.get("companyId"), CompanyConstants.SYSTEM);

		_pidToCompany.put(pid, companyId);

		CORSFactoryConfigurationStore corsFactoryConfigurationStore =
			_corsFactoryConfigurationStores.get(companyId);

		// First time adding configuration for a company

		if (corsFactoryConfigurationStore == null) {
			corsFactoryConfigurationStore = new CORSFactoryConfigurationStore(
				companyId);

			_corsFactoryConfigurationStores.put(
				companyId, corsFactoryConfigurationStore);
		}

		PortalCORSConfiguration portalCORSConfiguration =
			ConfigurableUtil.createConfigurable(
				PortalCORSConfiguration.class, properties);

		CORSFactoryConfiguration corsFactoryConfiguration =
			new CORSFactoryConfiguration(
				portalCORSConfiguration.filterMappingURLPatterns(),
				CORSSupport.buildCORSHeaders(
					portalCORSConfiguration.headers()));

		corsFactoryConfigurationStore.setCORSFactoryConfiguration(
			pid, corsFactoryConfiguration);

		_build(corsFactoryConfigurationStore);
	}

	protected void processCORSRequest(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, FilterChain filterChain)
		throws Exception {

		Long companyId = (Long)httpServletRequest.getAttribute("COMPANY_ID");

		if ((companyId == null) || (companyId == 0)) {
			return;
		}

		PathPatternMatcher<Map<String, String>> pathPatternMatcher =
			_pathPatternMatchers.get(companyId);

		if (pathPatternMatcher == null) {

			// if cannot find an CORS instance setting, we still need
			// to check if there is a system setting

			pathPatternMatcher = _pathPatternMatchers.get((long)0);

			if (pathPatternMatcher == null) {
				return;
			}
		}

		PatternTuple<Map<String, String>> patternTuple =
			pathPatternMatcher.getPatternTuple(_getURI(httpServletRequest));

		corsSupport.setCORSHeaders(patternTuple.getValue());

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

	private void _addHeadersToMap(
		HashMap<String, CORSSupport> pathPatternsHeadersMap,
		Set<String> addedPatterns,
		List<CORSFactoryConfiguration> corsFactoryConfigurations) {

		for (int i = 0; i < corsFactoryConfigurations.size(); ++i) {
			CORSFactoryConfiguration corsFactoryConfiguration =
				corsFactoryConfigurations.get(i);

			for (String pattern : corsFactoryConfiguration._pathPatterns) {
				if (addedPatterns.contains(pattern)) {
					continue;
				}

				CORSSupport corsSupport = new CORSSupport();

				corsSupport.setCORSHeaders(
					corsFactoryConfiguration._corsHeaders);

				pathPatternsHeadersMap.put(pattern, corsSupport);

				addedPatterns.add(pattern);
			}
		}
	}

	private void _build(
		CORSFactoryConfigurationStore corsFactoryConfigurationStore) {

		long companyId = corsFactoryConfigurationStore.getCompanyId();

		if (companyId != CompanyConstants.SYSTEM) {
			_buildPathPatternMatcher(
				_corsFactoryConfigurationStores.get(CompanyConstants.SYSTEM),
				corsFactoryConfigurationStore);

			return;
		}

		_buildPathPatternMatcher(corsFactoryConfigurationStore, null);

		for (long existingCompanyId : _pathPatternMatchers.keySet()) {
			if (existingCompanyId == 0) {
				continue;
			}

			_buildPathPatternMatcher(
				corsFactoryConfigurationStore,
				_corsFactoryConfigurationStores.get(existingCompanyId));
		}
	}

	private void _buildPathPatternMatcher(
		CORSFactoryConfigurationStore systemCORSFactoryConfigurationStore,
		CORSFactoryConfigurationStore instanceCORSFactoryConfigurationStore) {

		HashMap<String, CORSSupport> pathPatternsHeadersMap = new HashMap<>();

		List<CORSFactoryConfiguration> corsFactoryConfigurations =
			new ArrayList<>();

		Set<String> addedPatterns = new HashSet<>();

		long companyId = 0;

		if (instanceCORSFactoryConfigurationStore != null) {
			companyId = instanceCORSFactoryConfigurationStore.getCompanyId();

			if (!instanceCORSFactoryConfigurationStore.isEmpty()) {
				corsFactoryConfigurations =
					instanceCORSFactoryConfigurationStore.
						getCORSFactoryConfigurations();

				_addHeadersToMap(
					pathPatternsHeadersMap, addedPatterns,
					corsFactoryConfigurations);
			}
		}

		if ((systemCORSFactoryConfigurationStore != null) &&
			!systemCORSFactoryConfigurationStore.isEmpty()) {

			corsFactoryConfigurations =
				systemCORSFactoryConfigurationStore.
					getCORSFactoryConfigurations();

			_addHeadersToMap(
				pathPatternsHeadersMap, addedPatterns,
				corsFactoryConfigurations);
		}

		if (pathPatternsHeadersMap.isEmpty()) {
			_pathPatternMatchers.remove(companyId);

			return;
		}

		PathPatternMatcher<CORSSupport> patternMatcher =
			_pathPatternMatcherFactory.createPatternMatcher(
				pathPatternsHeadersMap);

		_pathPatternMatchers.put(companyId, patternMatcher);
	}

	private CORSFactoryConfigurationStore
		_defaultCORSFactoryConfigurationStore() {

		PortalCORSConfiguration portalCORSConfiguration =
			ConfigurableUtil.createConfigurable(
				PortalCORSConfiguration.class, new HashMapDictionary<>());

		CORSFactoryConfigurationStore corsFactoryConfigurationStore =
			new CORSFactoryConfigurationStore(0);

		corsFactoryConfigurationStore.setCORSFactoryConfiguration(
			PortalCORSConfiguration.class.getName() + "_default",
			new CORSFactoryConfiguration(
				portalCORSConfiguration.filterMappingURLPatterns(),
				CORSSupport.buildCORSHeaders(
					portalCORSConfiguration.headers())));

		return corsFactoryConfigurationStore;
	}

	private String _getURI(HttpServletRequest httpServletRequest) {
		String uri = httpServletRequest.getRequestURI();

		if (Validator.isNotNull(_contextPath) &&
			!_contextPath.equals(StringPool.SLASH) &&
			uri.startsWith(_contextPath)) {

			uri = uri.substring(_contextPath.length());
		}

		return _http.normalizePath(uri);
	}

	private String _contextPath;
	private final Map<Long, CORSFactoryConfigurationStore>
		_corsFactoryConfigurationStores = new HashMap<>(16);

	@Reference
	private Http _http;

	private final Map<Long, PathPatternMatcher<Map<String, String>>>
		_pathPatternMatchers = new HashMap<>(16);
	private final Map<String, Long> _pidToCompany = new HashMap<>(16);

	/**
	 * Each CORSFactoryConfiguration instance contains PortalCORSConfiguration
	 * entries.
	 */
	private static class CORSFactoryConfiguration {

		public CORSFactoryConfiguration(
			String[] pathPatterns, Map<String, String> corsHeaders) {

			_pathPatterns = new HashSet<>(pathPatterns.length);

			for (String pattern : pathPatterns) {
				_pathPatterns.add(pattern);
			}

			_corsHeaders = corsHeaders;
		}

		private final Map<String, String> _corsHeaders;
		private final Set<String> _pathPatterns;

	}

	/**
	 * Each CORSFactoryConfigurationStore instance contains a list of
	 * CORSFactoryConfigurations for a company. When companyId is 0, it is
	 * System.
	 */
	private static class CORSFactoryConfigurationStore {

		public CORSFactoryConfigurationStore(long companyId) {
			_pidIndexes = new HashMap<>();

			_factoryConfigurations = new ArrayList<>();

			_companyId = companyId;
		}

		public long getCompanyId() {
			return _companyId;
		}

		public List<CORSFactoryConfiguration> getCORSFactoryConfigurations() {
			return _factoryConfigurations;
		}

		public boolean isEmpty() {
			return _factoryConfigurations.isEmpty();
		}

		public void removeCORSFactoryConfiguration(String pid) {
			Integer index = _pidIndexes.remove(pid);

			if (_pidIndexes.isEmpty()) {
				_factoryConfigurations.clear();

				return;
			}

			if (index != null) {
				_factoryConfigurations.remove((int)index);

				for (Map.Entry<String, Integer> entry :
						_pidIndexes.entrySet()) {

					if (entry.getValue() > index) {
						_pidIndexes.put(entry.getKey(), entry.getValue() - 1);
					}
				}
			}
		}

		public void setCORSFactoryConfiguration(
			String pid, CORSFactoryConfiguration corsFactoryConfiguration) {

			Integer index = _pidIndexes.get(pid);

			if (index != null) {
				_factoryConfigurations.set(index, corsFactoryConfiguration);

				return;
			}

			_factoryConfigurations.add(corsFactoryConfiguration);

			_pidIndexes.put(pid, _factoryConfigurations.size() - 1);
		}

		private final long _companyId;
		private final List<CORSFactoryConfiguration> _factoryConfigurations;
		private final Map<String, Integer> _pidIndexes;

	}

}