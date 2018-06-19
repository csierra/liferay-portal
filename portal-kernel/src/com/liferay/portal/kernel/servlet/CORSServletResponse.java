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

package com.liferay.portal.kernel.servlet;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * @author Marta Medio
 */
public class CORSServletResponse extends HttpServletResponseWrapper {

	public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS =
		"Access-Control-Allow-Credentials";

	public static final String ACCESS_CONTROL_ALLOW_HEADERS =
		"Access-Control-Allow-Headers";

	public static final String ACCESS_CONTROL_ALLOW_METHODS =
		"Access-Control-Allow-Methods";

	public static final String ACCESS_CONTROL_ALLOW_ORIGIN =
		"Access-Control-Allow-Origin";

	public CORSServletResponse(HttpServletResponse response) {
		super(response);
	}

	@Override
	public void setHeader(String name, String value) {
		if (!name.equals(ACCESS_CONTROL_ALLOW_ORIGIN) &&
			!name.equals(ACCESS_CONTROL_ALLOW_HEADERS) &&
			!name.equals(ACCESS_CONTROL_ALLOW_METHODS) &&
			!name.equals(ACCESS_CONTROL_ALLOW_CREDENTIALS)) {

			super.setHeader(name, value);
		}
	}

}