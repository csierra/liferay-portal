/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.example;

import com.liferay.annotations.Portlet;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.io.IOException;

/**
 * @author Carlos Sierra Andrés
 */

@Portlet(name = "ExamplePortlet")
public class AnnotatedPortlet implements javax.portlet.Portlet {

	@Override
	public void destroy() {

	}

	@Override
	public void init(PortletConfig portletConfig) throws PortletException {

	}

	@Override
	public void processAction(
		ActionRequest actionRequest, ActionResponse actionResponse)
		throws PortletException, IOException {

	}

	@Override
	public void render(
		RenderRequest renderRequest, RenderResponse renderResponse)
		throws PortletException, IOException {

	}

}
