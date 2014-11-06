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

package com.lferay.portal.view.extensions;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.repository.model.FileVersion;
import com.liferay.portal.kernel.servlet.taglib.ui.MenuItem;
import com.liferay.portal.kernel.servlet.taglib.ui.URLMenuItem;
import com.liferay.portlet.documentlibrary.context.BaseDLViewFileVersionDisplayContext;
import com.liferay.portlet.documentlibrary.context.DLViewFileVersionDisplayContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

/**
 * @author Carlos Sierra Andrés
 */
public class NewMenuDisplayContext extends BaseDLViewFileVersionDisplayContext {

	public NewMenuDisplayContext(
		DLViewFileVersionDisplayContext parentDLDisplayContext,
		HttpServletRequest request, HttpServletResponse response,
		FileVersion fileVersion) {

		super(
			UUID.fromString("8AA71D4E-B441-4BF7-B0F2-28C41E2AC2D3"),
			parentDLDisplayContext, request, response, fileVersion);
	}

	@Override
	public List<MenuItem> getMenuItems() throws PortalException {
		List<MenuItem> menuItems = super.getMenuItems();

		URLMenuItem urlMenuItem = new URLMenuItem();

		urlMenuItem.setMethod("GET");
		urlMenuItem.setTarget("_blank");
		urlMenuItem.setURL("http://www.google.com");
		urlMenuItem.setLabel("New mwnu item");

		menuItems.add(urlMenuItem);

		return menuItems;
	}
}
