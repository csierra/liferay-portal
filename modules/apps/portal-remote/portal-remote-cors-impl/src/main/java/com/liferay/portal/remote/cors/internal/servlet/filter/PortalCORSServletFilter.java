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
import com.liferay.portal.kernel.servlet.HttpMethods;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.remote.cors.internal.CORSSupport;
import com.liferay.portal.remote.cors.internal.path.pattern.matcher.PathPatternMatcher;
import com.liferay.portal.remote.cors.internal.path.pattern.matcher.PatternTuple;

import java.io.IOException;

import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Arthur Chan
 */
@Component(
	immediate = true,
	property = {
		"before-filter=Auto Login Filter", "dispatcher=FORWARD",
		"dispatcher=REQUEST", "servlet-context-name=",
		"servlet-filter-name=Portal CORS Servlet Filter", "url-pattern=/*"
	},
	service = Filter.class
)
public class PortalCORSServletFilter implements Filter {

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
	public void init(FilterConfig filterConfig) {
		ServletContext servletContext = filterConfig.getServletContext();

		_contextPath = servletContext.getContextPath();
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

	@Reference
	private Http _http;

}