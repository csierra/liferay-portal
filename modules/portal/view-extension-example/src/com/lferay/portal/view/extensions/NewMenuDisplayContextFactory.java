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

import com.liferay.portal.kernel.repository.model.FileVersion;
import com.liferay.portlet.documentlibrary.context.DLViewFileVersionDisplayContext;
import com.liferay.portlet.documentlibrary.context.DLViewFileVersionDisplayContextFactory;
import org.osgi.service.component.annotations.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Carlos Sierra Andrés
 */
@Component(immediate = true)
public class NewMenuDisplayContextFactory implements
	DLViewFileVersionDisplayContextFactory {


	@Override
	public DLViewFileVersionDisplayContext
		getDLFileVersionActionsDisplayContext(
			DLViewFileVersionDisplayContext
				parentDLViewFileVersionDisplayContext,
			HttpServletRequest request, HttpServletResponse response,
			FileVersion fileVersion) {

			return new NewMenuDisplayContext(
				parentDLViewFileVersionDisplayContext, request, response,
				fileVersion);
	}
}
