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

package com.liferay.oauth2.provider.service.impl;

import com.liferay.oauth2.provider.constants.OAuth2ProviderActionKeys;
import com.liferay.oauth2.provider.constants.OAuth2ProviderConstants;
import com.liferay.oauth2.provider.exception.NoSuchOAuth2ApplicationException;
import com.liferay.oauth2.provider.model.OAuth2Application;
import com.liferay.oauth2.provider.service.OAuth2ApplicationLocalService;
import com.liferay.oauth2.provider.service.base.OAuth2ApplicationServiceBaseImpl;
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.util.List;

/**
 * The implementation of the o auth2 application remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.liferay.oauth2.provider.service.OAuth2ApplicationService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see OAuth2ApplicationServiceBaseImpl
 * @see com.liferay.oauth2.provider.service.OAuth2ApplicationServiceUtil
 */
public class OAuth2ApplicationServiceImpl
	extends OAuth2ApplicationServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Always use {@link com.liferay.oauth2.provider.service.OAuth2ApplicationServiceUtil} to access the o auth2 application remote service.
	 */

	@Override
	public OAuth2Application addOAuth2Application(
			long userId, String name, String description, String webURL,
			boolean oAuth2ClientConfidential, String oAuth2ClientId,
			String oAuth2ClientSecret, String oAuth2RedirectURI,
			ServiceContext serviceContext)
		throws PortalException {

		check(OAuth2ProviderActionKeys.ACTION_ADD_APPLICATION);

		return oAuth2ApplicationLocalService.addOAuth2Application(
			userId, name, description, webURL, oAuth2ClientConfidential,
			oAuth2ClientId, oAuth2ClientSecret, oAuth2RedirectURI,
			serviceContext);
	}

	@Override
	public OAuth2Application fetchOAuth2Application(long companyId, String clientId)
		throws PrincipalException {

		OAuth2Application oAuth2Application =
			oAuth2ApplicationLocalService.fetchOAuth2Application(
				companyId, clientId);

		if (oAuth2Application != null) {
			check(oAuth2Application, ActionKeys.VIEW);
		}

		return oAuth2Application;
	}

	@Override
	public OAuth2Application getOAuth2Application(long companyId, String clientId)
		throws NoSuchOAuth2ApplicationException, PrincipalException {

		OAuth2Application oAuth2Application =
			oAuth2ApplicationLocalService.getOAuth2Application(
				companyId, clientId);

		check(oAuth2Application, ActionKeys.VIEW);

		return oAuth2Application;
	}

	@Override
	public OAuth2Application getOAuth2Application(long oAuth2ApplicationId)
		throws PortalException {

		OAuth2Application oAuth2Application =
			oAuth2ApplicationLocalService.getOAuth2Application(
				oAuth2ApplicationId);

		check(oAuth2Application, ActionKeys.VIEW);

		return oAuth2Application;
	}

	@Override
	public List<OAuth2Application> getOAuth2Applications(
		long companyId, int start, int end,
		OrderByComparator<OAuth2Application> orderByComparator) {

		return oAuth2ApplicationPersistence.filterFindByC(
			companyId, start, end, orderByComparator);
	}

	@Override
	public int getOAuth2ApplicationsCount(long companyId) {
		return oAuth2ApplicationPersistence.filterCountByC(companyId);
	}

	@Override
	public OAuth2Application updateOAuth2Application(
			long userId, long oAuth2ApplicationId, String name,
			String description, String webURL, boolean oAuth2ClientConfidential,
			String oAuth2ClientId, String oAuth2ClientSecret,
			String oAuth2RedirectURI, ServiceContext serviceContext)
		throws PortalException {

		OAuth2Application oAuth2Application =
			oAuth2ApplicationLocalService.getOAuth2Application(
				oAuth2ApplicationId);

		check(oAuth2Application, ActionKeys.UPDATE);

		return oAuth2ApplicationLocalService.updateOAuth2Application(
			userId, oAuth2ApplicationId, name, description, webURL,
			oAuth2ClientConfidential, oAuth2ClientId, oAuth2ClientSecret,
			oAuth2RedirectURI, serviceContext);
	}

	@Override
	public OAuth2Application updateScopes(
			long oAuth2ApplicationId, List<String> scopes)
		throws PortalException {

		OAuth2Application oAuth2Application =
			oAuth2ApplicationLocalService.getOAuth2Application(
				oAuth2ApplicationId);

		check(oAuth2Application, ActionKeys.UPDATE);

		return oAuth2ApplicationLocalService.updateScopes(
			oAuth2ApplicationId, scopes);
	}


	protected void check(String action) throws PrincipalException {
		PermissionChecker permissionChecker = getPermissionChecker();

		if (!permissionChecker.hasPermission(
			0, OAuth2ProviderConstants.RESOURCE_NAME, 0, action)) {

			throw new PrincipalException.MustHavePermission(
				permissionChecker, OAuth2ProviderConstants.RESOURCE_NAME, 0,
				action);
		}
	}

	protected void check(OAuth2Application oAuth2Application, String action)
		throws PrincipalException {

		PermissionChecker permissionChecker = getPermissionChecker();

		if (permissionChecker.hasOwnerPermission(
			oAuth2Application.getCompanyId(), OAuth2Application.class.getName(),
			oAuth2Application.getOAuth2ApplicationId(),
			oAuth2Application.getUserId(), action)) {

			return;
		}

		if (!permissionChecker.hasPermission(
			0, OAuth2Application.class.getName(),
			oAuth2Application.getOAuth2ApplicationId(), action)) {

			throw new PrincipalException.MustHavePermission(
				permissionChecker, OAuth2Application.class.getName(),
				oAuth2Application.getOAuth2ApplicationId(), action);
		}
	}

}