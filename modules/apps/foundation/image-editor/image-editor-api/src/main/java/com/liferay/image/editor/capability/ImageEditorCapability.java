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

package com.liferay.image.editor.capability;

import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContext;

/**
 * @author Bruno Basto
 */
public interface ImageEditorCapability {

	public String getLabel(Locale locale);

	public String getModuleName();

	public String getName();

	public List<URL> getResourceURLs();

	public ServletContext getServletContext();

	public void prepareContext(
		Map<String, Object> context, HttpServletRequest request);

}