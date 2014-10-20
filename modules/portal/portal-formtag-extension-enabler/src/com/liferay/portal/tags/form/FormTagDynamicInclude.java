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

package com.liferay.portal.tags.form;

import com.liferay.portal.kernel.servlet.taglib.DynamicInclude;
import com.liferay.portal.util.PortletKeys;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Carlos Sierra Andrés
 */
@Component(
	immediate = true, service = DynamicInclude.class, property =
		{"key=com.liferay.taglib.aui.FormTag#doStartTag#before#" +
			PortletKeys.LOGIN + "-fm"}
)
public class FormTagDynamicInclude implements DynamicInclude {

	@Override
	public void include(
		HttpServletRequest request, HttpServletResponse response, String key) {

		try {
			PrintWriter writer = response.getWriter();

			writer.println("<h2>extension</h2><br/>");
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}