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

package com.liferay.portal.users.rest.api.application;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.Function;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserService;
import com.liferay.portal.users.rest.api.model.RestUser;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.util.List;

/**
 * @author Carlos Sierra Andrés
 */
@Component(immediate = true, property = "jaxws=true", service=UsersSoap.class)
@WebService
public class UsersSoap {

	public RestUser getUser(long userId) throws PortalException {
		return new RestUser(_userService.getUserById(userId));
	}

	@WebMethod(operationName = "list")
	public List<RestUser> getUsers() throws PortalException {
		MessageContext messageContext = _webServiceContext.getMessageContext();

		Company company = (Company) messageContext.get(Company.class.getName());

		return ListUtil.toList(_userService.getCompanyUsers(
			company.getCompanyId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS),
			new Function<User, RestUser>() {

				@Override
				public RestUser apply(User user) {
					return new RestUser(user);
				}

			});
	}

	@Reference
	protected void setUserService(UserService userService) {
		_userService = userService;
	}

	@Resource
	private WebServiceContext _webServiceContext;

	private UserService _userService;

}
