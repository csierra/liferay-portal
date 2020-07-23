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

import com.liferay.oauth2.provider.scope.liferay.OAuth2ProviderScopeLiferayAccessControlContext;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.servlet.BaseFilter;
import com.liferay.portal.kernel.servlet.HttpMethods;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.remote.cors.internal.CORSSupport;
import com.liferay.portal.remote.cors.internal.configuration.manager.PortalCORSConfigurationManager;

import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Arthur Chan
 * @author Carlos Sierra Andrés
 */
@Component(
	immediate = true,
	property = {
		"before-filter=Upload Servlet Request Filter", "dispatcher=FORWARD",
		"dispatcher=REQUEST", "servlet-context-name=",
		"servlet-filter-name=Portal CORS Servlet Filter", "url-pattern=/*"
	},
	service = Filter.class
)
public class PortalCORSServletFilter extends BaseFilter {

	@Override
	public void init(FilterConfig filterConfig) {
		ServletContext servletContext = filterConfig.getServletContext();

		_contextPath = servletContext.getContextPath();
	}

	@Override
	public boolean isFilterEnabled(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) {

		if (CORSSupport.isCORSRequest(httpServletRequest::getHeader)) {
			return true;
		}

		return false;
	}

	@Override
	protected Log getLog() {
		return _log;
	}

	@Override
	protected void processFilter(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, FilterChain filterChain)
		throws Exception {

		long companyId = _portal.getCompanyId(httpServletRequest);

		if (companyId == CompanyConstants.SYSTEM) {
			return;
		}

		CORSSupport corsSupport = _getCORSSupport(
			_getURI(httpServletRequest), companyId);

		if (corsSupport != null) {
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
					httpServletRequest::getHeader) &&
				(OAuth2ProviderScopeLiferayAccessControlContext.
					isOAuth2AuthVerified() ||
				 _isGuest())) {

				corsSupport.writeResponseHeaders(
					httpServletRequest::getHeader,
					httpServletResponse::setHeader);
			}
		}

		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}

	/**
	 * Order of finding pattern is important, which is specified by Servlet specs.
	 */
	private CORSSupport _getCORSSupport(String urlPath, long companyId) {
		Map<String, CORSSupport> exactPatternCORSSupports =
			_portalCORSConfigurationManager.getExactPatternCORSSupports(
				companyId);

		if (exactPatternCORSSupports == null) {
			exactPatternCORSSupports =
				_portalCORSConfigurationManager.getExactPatternCORSSupports(
					CompanyConstants.SYSTEM);
		}

		CORSSupport corsSupport = exactPatternCORSSupports.get(urlPath);

		if (corsSupport != null) {
			return corsSupport;
		}

		Map<String, CORSSupport> wildcardPatternCORSSupports =
			_portalCORSConfigurationManager.getWildcardPatternCORSSupports(
				companyId);

		if (wildcardPatternCORSSupports == null) {
			wildcardPatternCORSSupports =
				_portalCORSConfigurationManager.getWildcardPatternCORSSupports(
					CompanyConstants.SYSTEM);
		}

		int lastDot = 0;

		for (int i = urlPath.length(); i > 0; --i) {
			corsSupport = wildcardPatternCORSSupports.get(
				urlPath.substring(0, i) + "*");

			if (corsSupport != null) {
				return corsSupport;
			}

			if ((lastDot < 1) && (urlPath.charAt(i - 1) == '.')) {
				lastDot = i - 1;
			}
		}

		Map<String, CORSSupport> extensionPatternCORSSupports =
			_portalCORSConfigurationManager.getExtensionPatternCORSSupports(
				companyId);

		if (extensionPatternCORSSupports == null) {
			extensionPatternCORSSupports =
				_portalCORSConfigurationManager.getExtensionPatternCORSSupports(
					CompanyConstants.SYSTEM);
		}

		return extensionPatternCORSSupports.get(
			"*" + urlPath.substring(lastDot));
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

	private boolean _isGuest() {
		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		if (permissionChecker == null) {
			return true;
		}

		User user = permissionChecker.getUser();

		return user.isDefaultUser();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PortalCORSServletFilter.class);

	private String _contextPath;

	@Reference
	private Http _http;

	@Reference
	private Portal _portal;

	@Reference
	private PortalCORSConfigurationManager _portalCORSConfigurationManager;

}