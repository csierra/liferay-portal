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

package com.liferay.portal.security.cas;

import com.liferay.portal.kernel.util.CasNotAvailableException;
import com.liferay.portal.kernel.util.CasService;
import org.jasig.cas.client.authentication.AttributePrincipal;

import javax.portlet.PortletSession;

/**
 * @author Carlos Sierra Andrés
 */
public class CasServiceImpl implements CasService {

	public static String LIFERAY_SHARED_CAS_PRINCIPIAL =
		"LIFERAY_SHARED_CAS_PRINCIPAL";

	public String getProxyTicketFor(
			PortletSession portletSession, String service)
		throws CasNotAvailableException {

		AttributePrincipal attributePrincipal =
			(AttributePrincipal)portletSession.getAttribute(
				LIFERAY_SHARED_CAS_PRINCIPIAL,
				PortletSession.APPLICATION_SCOPE);

		if (attributePrincipal == null) {
			throw new CasNotAvailableException(
				"Could not find " + LIFERAY_SHARED_CAS_PRINCIPIAL +
					" in the provided session");
		}

		return attributePrincipal.getProxyTicketFor(service);
	}

}
