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

package com.liferay.portal.kernel.util;

import com.liferay.portal.kernel.security.pacl.permission.PortalRuntimePermission;

import javax.portlet.PortletSession;

/**
 * @author Carlos Sierra Andrés
 */
public class CasServiceUtil {

	public static String getProxyTicketFor(
			PortletSession portletSession, String service)
		throws CasNotAvailableException {

		return _casService.getProxyTicketFor(portletSession, service);
	}

	public void setCasService(CasService casService) {
		PortalRuntimePermission.checkSetBeanProperty(getClass());

		_casService = casService;
	}

	private static CasService _casService;
}
