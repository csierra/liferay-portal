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
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.servlet.HttpMethods;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.remote.cors.configuration.PortalCORSConfiguration;
import com.liferay.portal.remote.cors.internal.CORSSupport;
import com.liferay.portal.remote.cors.internal.path.pattern.matcher.DynamicPathPatternMatcher;
import com.liferay.portal.remote.cors.internal.path.pattern.matcher.PathPatternMatcher;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
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
import org.osgi.service.component.annotations.Activate;
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

		_updatePatternMatcher(companyId, corsFactoryConfigurationStore);
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
	public void updated(String pid, Dictionary<String, ?> properties)
		throws ConfigurationException {

		long companyId = GetterUtil.getLong(
			properties.get("companyId"), CompanyConstants.SYSTEM);

		_pidToCompany.put(pid, companyId);

		PortalCORSConfiguration portalCORSConfiguration =
			ConfigurableUtil.createConfigurable(
				PortalCORSConfiguration.class, properties);

		Map<String, String> corsHeaders = CORSSupport.buildCORSHeaders(
			portalCORSConfiguration.headers());

		String[] urlPathPatterns =
			portalCORSConfiguration.filterMappingURLPatterns();

		CORSFactoryConfiguration corsFactoryConfiguration =
			new CORSFactoryConfiguration(urlPathPatterns, corsHeaders);

		CORSFactoryConfigurationStore corsFactoryConfigurationStore =
			_corsFactoryConfigurationStores.get(companyId);

		// first time adding configuration for a company

		if (corsFactoryConfigurationStore == null) {
			_corsFactoryConfigurationStores.put(
				companyId, new CORSFactoryConfigurationStore());

			corsFactoryConfigurationStore = _corsFactoryConfigurationStores.get(
				companyId);
		}

		corsFactoryConfigurationStore.setCORSFactoryConfiguration(
			pid, corsFactoryConfiguration);

		_updatePatternMatcher(companyId, corsFactoryConfigurationStore);
	}

	@Activate
	protected void activate() {
		PortalCORSConfiguration portalCORSConfiguration =
			ConfigurableUtil.createConfigurable(
				PortalCORSConfiguration.class, new HashMapDictionary<>());

		Map<String, String> corsHeaders = CORSSupport.buildCORSHeaders(
			portalCORSConfiguration.headers());

		String[] urlPathPatterns =
			portalCORSConfiguration.filterMappingURLPatterns();

		CORSFactoryConfiguration corsFactoryConfiguration =
			new CORSFactoryConfiguration(urlPathPatterns, corsHeaders);

		CORSFactoryConfigurationStore corsFactoryConfigurationStore =
			new CORSFactoryConfigurationStore();

		_corsFactoryConfigurationStores.put(
			CompanyConstants.SYSTEM, corsFactoryConfigurationStore);

		corsFactoryConfigurationStore.setCORSFactoryConfiguration(
			"default", corsFactoryConfiguration);

		_updatePatternMatcher(
			CompanyConstants.SYSTEM, corsFactoryConfigurationStore);
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

			// if cannot find an CORS instance setting, we still need
			// to check if there is a system setting

			pathPatternMatcher = _pathPatternMatchers.get((long)0);

			if (pathPatternMatcher == null) {
				filterChain.doFilter(httpServletRequest, httpServletResponse);

				return;
			}
		}

		List<Map<String, String>> headersList = pathPatternMatcher.getCargoList(
			getURI(httpServletRequest));

		corsSupport.setCORSHeaders(headersList.get(0));

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

	protected void setPathPatternMatcher(
		CORSFactoryConfigurationStore systemCORSFactoryConfigurationStore,
		CORSFactoryConfigurationStore instanceCORSFactoryConfigurationStore,
		long companyId) {

		PathPatternMatcher<Map<String, String>> pathPatternMatcher =
			_pathPatternMatchers.computeIfAbsent(
				companyId, cid -> new DynamicPathPatternMatcher<>());

		Set<String> addedPatterns = new HashSet<>();

		_insertPatternMatcher(
			pathPatternMatcher,
			instanceCORSFactoryConfigurationStore.
				getCORSFactoryConfigurations(),
			addedPatterns);

		_insertPatternMatcher(
			pathPatternMatcher,
			systemCORSFactoryConfigurationStore.getCORSFactoryConfigurations(),
			addedPatterns);
	}

	protected final CORSSupport corsSupport = new CORSSupport();

	private void _insertPatternMatcher(
		PathPatternMatcher<Map<String, String>> pathPatternMatcher,
		List<CORSFactoryConfiguration> corsFactoryConfigurations,
		Set<String> alreadyExistingPatterns) {

		for (int i = corsFactoryConfigurations.size() - 1; i > -1; --i) {
			CORSFactoryConfiguration corsFactoryConfiguration =
				corsFactoryConfigurations.get(i);

			for (String pattern : corsFactoryConfiguration._pathPatterns) {
				if (alreadyExistingPatterns.contains(pattern)) {
					continue;
				}

				pathPatternMatcher.insert(
					pattern, corsFactoryConfiguration._corsHeaders);
				alreadyExistingPatterns.add(pattern);
			}
		}
	}

	private void _updatePatternMatcher(
		long companyId,
		CORSFactoryConfigurationStore corsFactoryConfigurationStore) {

		if (companyId != CompanyConstants.SYSTEM) {
			setPathPatternMatcher(
				_corsFactoryConfigurationStores.get(CompanyConstants.SYSTEM),
				corsFactoryConfigurationStore, companyId);

			return;
		}

		setPathPatternMatcher(corsFactoryConfigurationStore, null, companyId);

		for (long existingCompanyId : _pathPatternMatchers.keySet()) {
			if (existingCompanyId == 0) {
				continue;
			}

			setPathPatternMatcher(
				corsFactoryConfigurationStore,
				_corsFactoryConfigurationStores.get(existingCompanyId),
				existingCompanyId);
		}
	}

	private String _contextPath;
	private final Map<Long, CORSFactoryConfigurationStore>
		_corsFactoryConfigurationStores = new HashMap<>(16);

	@Reference
	private Http _http;

	private final Map<Long, PathPatternMatcher<Map<String, String>>>
		_pathPatternMatchers = new HashMap<>(16);
	private final Map<String, Long> _pidToCompany = new HashMap<>(16);

	@Reference
	private Portal _portal;

	private static class CORSFactoryConfiguration {

		public CORSFactoryConfiguration(
			String[] pathPatterns, Map<String, String> corsHeaders) {

			_pathPatterns = new HashSet<>(pathPatterns.length);

			Collections.addAll(_pathPatterns, pathPatterns);

			_corsHeaders = corsHeaders;
		}

		private final Map<String, String> _corsHeaders;
		private final Set<String> _pathPatterns;

	}

	private static class CORSFactoryConfigurationStore {

		public CORSFactoryConfigurationStore() {
			_pidIndexes = new HashMap<>();

			_factoryConfigurations = new ArrayList<>();
		}

		public List<CORSFactoryConfiguration> getCORSFactoryConfigurations() {
			return _factoryConfigurations;
		}

		public void removeCORSFactoryConfiguration(String pid) {
			Integer index = _pidIndexes.remove(pid);

			if (index != null) {
				_factoryConfigurations.remove((int)index);
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

		private final List<CORSFactoryConfiguration> _factoryConfigurations;
		private final Map<String, Integer> _pidIndexes;

	}

}