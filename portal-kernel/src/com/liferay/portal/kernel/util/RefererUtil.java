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

package com.liferay.portal.kernel.util;

import com.liferay.portal.kernel.theme.ThemeDisplay;

import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Marta Medio
 */
public class RefererUtil implements Callable<String> {

	public RefererUtil(HttpServletRequest request, ThemeDisplay themeDisplay) {
		_request = request;
		_themeDisplay = themeDisplay;
	}

	public String call() {
		String referer = null;

		String refererParam = PortalUtil.escapeRedirect(
			_request.getParameter(WebKeys.REFERER));
		String refererRequest = (String)_request.getAttribute(WebKeys.REFERER);

		String refererSession = null;

		HttpSession session = _request.getSession(false);

		if (session != null) {
			refererSession = (String)session.getAttribute(WebKeys.REFERER);
		}

		if (Validator.isNotNull(refererParam)) {
			referer = refererParam;
		}
		else if (Validator.isNotNull(refererRequest)) {
			referer = refererRequest;
		}
		else if (Validator.isNotNull(refererSession)) {
			referer = refererSession;
		}
		else if (_themeDisplay != null) {
			referer = _themeDisplay.getPathMain();
		}
		else {
			referer = PortalUtil.getPathMain();
		}

		if ((session != null) && !CookieKeys.hasSessionId(_request) &&
			Validator.isNotNull(referer)) {

			referer = PortalUtil.getURLWithSessionId(referer, session.getId());
		}

		return referer;
	}

	private final HttpServletRequest _request;
	private final ThemeDisplay _themeDisplay;

}