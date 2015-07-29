/*
 * *
 *  * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *  *
 *  * This library is free software; you can redistribute it and/or modify it under
 *  * the terms of the GNU Lesser General Public License as published by the Free
 *  * Software Foundation; either version 2.1 of the License, or (at your option)
 *  * any later version.
 *  *
 *  * This library is distributed in the hope that it will be useful, but WITHOUT
 *  * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 *  * details.
 *
 */

package com.liferay.portal.upgrade.internal.core;

import com.liferay.portal.model.Release;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Migue Pastor
 */
@Component(service = PortalCoreServices.class)
public class PortalCoreServices {

	@Activate
	protected void activate() {
		System.out.println("-------------------");
		System.out.println("Register the full Application Context and publish a Servlet Context");
		System.out.println("-------------------");
	}

	@Deactivate
	protected void deactivate() {
		System.out.println("-------------------");
		System.out.println("Unregister the full Application Context and unregister the Servlet Context");
		System.out.println("-------------------");
	}

	@Reference(target = "(&(application.name=portal)(release.build.number=1.0.0))")
	protected void setRelease(Release release) {
	}

}