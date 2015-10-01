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

package com.liferay.portal.users.rest.api.handler;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.model.Company;
import com.liferay.portal.service.CompanyLocalService;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import org.osgi.service.component.annotations.Component;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.LogicalHandler;
import javax.xml.ws.handler.LogicalMessageContext;
import javax.xml.ws.handler.MessageContext;

/**
 * @author Carlos Sierra Andrés
 */
@Component(
	immediate = true,
	property = "liferay.handler=true",
	service=Handler.class
)
public class CompanyHandler implements LogicalHandler<LogicalMessageContext>{

	@Override
	public boolean handleMessage(LogicalMessageContext context) {
		if (!(Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)) {
			HttpServletRequest httpServletRequest =
				(HttpServletRequest) context.get(
					MessageContext.SERVLET_REQUEST);

			Company company;

			try {
				company = PortalUtil.getCompany(httpServletRequest);
			}
			catch (PortalException e) {
				company = CompanyLocalServiceUtil.fetchCompany(
					PortalUtil.getDefaultCompanyId());
			}

			context.put(Company.class.getName(), company);

			context.setScope(
				Company.class.getName(), MessageContext.Scope.APPLICATION);
		}

		return true;
	}

	@Override
	public boolean handleFault(LogicalMessageContext context) {
		return true;
	}

	@Override
	public void close(MessageContext context) {

	}
}
