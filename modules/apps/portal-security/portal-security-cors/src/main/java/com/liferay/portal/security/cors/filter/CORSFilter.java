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

package com.liferay.portal.security.cors.filter;

import com.liferay.portal.kernel.servlet.CORSServletResponse;
import com.liferay.portal.kernel.servlet.HttpMethods;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.servlet.filters.BasePortalFilter;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Marta Medio
 */
public class CORSFilter extends BasePortalFilter {

	public static final String ACCEPT_CONTENT = "Accept";

	public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS =
		"Access-Control-Allow-Credentials";

	public static final String ACCESS_CONTROL_ALLOW_HEADERS =
		"Access-Control-Allow-Headers";

	public static final String ACCESS_CONTROL_ALLOW_METHODS =
		"Access-Control-Allow-Methods";

	public static final String ACCESS_CONTROL_ALLOW_ORIGIN =
		"Access-Control-Allow-Origin";

	public static final String ALLOW_REQUEST = "Allow";

	@Override
	public void init(FilterConfig filterConfig) {
		super.init(filterConfig);

		Enumeration<String> enu = filterConfig.getInitParameterNames();

		while (enu.hasMoreElements()) {
			String name = enu.nextElement();

			String value = filterConfig.getInitParameter(name);

			_initParametersMap.put(name, value);
		}

		if (_initParametersMap.containsKey("accept.allowed")) {
			_acceptedContent = GetterUtil.getString(
				_initParametersMap.get("accept.allowed"), null);
		}

		if (_initParametersMap.containsKey(
				"access.control.credentials.allowed")) {

			_accessControlCredentials = GetterUtil.getString(
				_initParametersMap.get("access.control.credentials.allowed"),
				null);
		}

		if (_initParametersMap.containsKey("access.control.headers.allowed")) {
			_accessControlHeaders = GetterUtil.getString(
				_initParametersMap.get("access.control.headers.allowed"), null);
		}

		if (_initParametersMap.containsKey("access.control.methods.allowed")) {
			_accessControlMethods = GetterUtil.getString(
				_initParametersMap.get("access.control.methods.allowed"), null);
		}

		if (_initParametersMap.containsKey("access.control.origin.allowed")) {
			_accessControlOrigin = GetterUtil.getString(
				_initParametersMap.get("access.control.origin.allowed"), null);
		}

		if (_initParametersMap.containsKey("allow.allowed")) {
			_allowedRequest = GetterUtil.getString(
				_initParametersMap.get("allow.allowed"), null);
		}

		if (_initParametersMap.containsKey("preflight.allowed")) {
			_corsPreflightAllowed = GetterUtil.getBoolean(
				_initParametersMap.get("preflight.allowed"), true);

			_initParametersMap.remove("preflight.allowed");
		}

		if (_initParametersMap.containsKey("override.allowed")) {
			_overrideAllowed = GetterUtil.getBoolean(
				_initParametersMap.get("override.allowed"), true);

			_initParametersMap.remove("override.allowed");
		}
	}

	@Override
	protected void processFilter(
			HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain)
		throws Exception {

		if (_isApplyCORSPreflight(request, response)) {
			return;
		}

		HttpServletResponse httpServletResponse;

		if (_overrideAllowed) {
			httpServletResponse = response;
		}
		else {
			httpServletResponse = new CORSServletResponse(response);
		}

		_setConfiguredHeaders(response);

		Class<?> clazz = getClass();

		processFilter(
			clazz.getName(), request, httpServletResponse, filterChain);
	}

	private boolean _isApplyCORSPreflight(
		HttpServletRequest request, HttpServletResponse response) {

		if (!_corsPreflightAllowed) {
			return false;
		}

		String method = request.getMethod();

		if (!StringUtil.equals(method, HttpMethods.OPTIONS)) {
			return false;
		}

		response.setHeader(
			ACCESS_CONTROL_ALLOW_CREDENTIALS, _accessControlCredentials);
		response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS, _accessControlHeaders);
		response.setHeader(ACCESS_CONTROL_ALLOW_METHODS, _accessControlMethods);
		response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, _accessControlOrigin);

		return true;
	}

	private void _setConfiguredHeaders(HttpServletResponse response) {
		response.setHeader(ACCEPT_CONTENT, _acceptedContent);
		response.setHeader(
			ACCESS_CONTROL_ALLOW_CREDENTIALS, _accessControlCredentials);
		response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS, _accessControlHeaders);
		response.setHeader(ACCESS_CONTROL_ALLOW_METHODS, _accessControlMethods);
		response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, _accessControlOrigin);
		response.setHeader(ALLOW_REQUEST, _allowedRequest);
	}

	private String _acceptedContent;
	private String _accessControlCredentials;
	private String _accessControlHeaders;
	private String _accessControlMethods;
	private String _accessControlOrigin;
	private String _allowedRequest;
	private boolean _corsPreflightAllowed = true;
	private final Map<String, Object> _initParametersMap = new HashMap<>();
	private boolean _overrideAllowed = true;

}