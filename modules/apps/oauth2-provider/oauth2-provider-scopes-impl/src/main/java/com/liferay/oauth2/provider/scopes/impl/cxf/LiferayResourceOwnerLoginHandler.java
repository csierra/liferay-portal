/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.oauth2.provider.scopes.impl.cxf;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.Authenticator;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.security.auth.session.AuthenticatedSessionManager;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.MapUtil;
import org.apache.cxf.rs.security.oauth2.common.UserSubject;
import org.apache.cxf.rs.security.oauth2.grants.owner.ResourceOwnerLoginHandler;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class LiferayResourceOwnerLoginHandler implements ResourceOwnerLoginHandler {

	@Override
	public UserSubject createSubject(String login, String password) {
		Long companyId = CompanyThreadLocal.getCompanyId();

		Company company = _companyLocalService.fetchCompany(companyId);

		String authType = company.getAuthType();

		int authResult = Authenticator.FAILURE;

		Map<String, String[]> headerMap = Collections.emptyMap();
		Map<String, String[]> parameterMap = Collections.emptyMap();
		Map<String, Object> resultsMap = new HashMap<>();

		try {
			if (authType.equals(CompanyConstants.AUTH_TYPE_EA)) {
				authResult = _userLocalService.authenticateByEmailAddress(
					company.getCompanyId(), login, password, headerMap,
					parameterMap, resultsMap);
			}
			else if (authType.equals(CompanyConstants.AUTH_TYPE_SN)) {
				authResult = _userLocalService.authenticateByScreenName(
					company.getCompanyId(), login, password, headerMap,
					parameterMap, resultsMap);
			}
			else if (authType.equals(CompanyConstants.AUTH_TYPE_ID)) {
				authResult = _userLocalService.authenticateByUserId(
					company.getCompanyId(), Long.parseLong(login),
					password, headerMap, parameterMap, resultsMap);
			}

		}
		catch (PortalException e) {
			return null;
		}

		if (authResult == Authenticator.FAILURE) {
			return null;
		}

		long userId = MapUtil.getLong(resultsMap, "userId", -1);

		if (userId == -1) {
			return null;
		}

		User user = _userLocalService.fetchUser(userId);

		if (user == null) {
			return null;
		}

		try {
			return new UserSubject(
				user.getLogin(), Long.toString(user.getUserId()));
		}
		catch (PortalException e) {
			return null;
		}
	}

	@Reference
	CompanyLocalService _companyLocalService;

	@Reference
	UserLocalService _userLocalService;

}
