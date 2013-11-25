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

package com.liferay.portal.servlet;

import com.liferay.portal.servlet.filters.sso.cas.LiferayEHCacheProxyGrantingTicketStorage;
import org.jasig.cas.client.proxy.ProxyGrantingTicketStorage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Carlos Sierra Andrés
 */
public class CASProxyGrantingTicketServlet extends HttpServlet {

	@Override
	protected void doGet(
			HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {

		String pgtId = request.getParameter("pgtId");
		String pgtIou = request.getParameter("pgtIou");

		_proxyGrantingTicketStorage.save(pgtIou, pgtId);

		System.out.println("CAS PGT: " + pgtId + " -> " + pgtIou);
	}

	private ProxyGrantingTicketStorage _proxyGrantingTicketStorage =
		new LiferayEHCacheProxyGrantingTicketStorage();
}
