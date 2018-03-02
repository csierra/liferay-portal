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
import com.liferay.oauth2.provider.model.OAuth2Application;
import com.liferay.oauth2.provider.model.OAuth2RefreshToken;
import com.liferay.oauth2.provider.service.base.OAuth2AuthorizationServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.jsonwebservice.JSONWebService;
import com.liferay.portal.kernel.jsonwebservice.JSONWebServiceMode;

/**
 * The implementation of the o auth2 authorization remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.liferay.oauth2.provider.service.OAuth2AuthorizationService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see OAuth2AuthorizationServiceBaseImpl
 * @see com.liferay.oauth2.provider.service.OAuth2AuthorizationServiceUtil
 */
@JSONWebService(mode = JSONWebServiceMode.IGNORE)
public class OAuth2AuthorizationServiceImpl
	extends OAuth2AuthorizationServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Always use {@link com.liferay.oauth2.provider.service.OAuth2AuthorizationServiceUtil} to access the o auth2 authorization remote service.
	 */

	@Override
	public boolean revokeAuthorization(
			long oAuth2TokenId, long oAuth2RefreshTokenId)
		throws PortalException {

		long oAuth2ApplicationId = 0;

		if (oAuth2RefreshTokenId != 0) {
			OAuth2RefreshToken oAuth2RefreshToken =
				oAuth2RefreshTokenLocalService.getOAuth2RefreshToken(
					oAuth2RefreshTokenId);

			oAuth2ApplicationId = oAuth2RefreshToken.getOAuth2ApplicationId();
		}
		else if (oAuth2TokenId != 0) {
			OAuth2RefreshToken oAuth2RefreshToken =
				oAuth2RefreshTokenLocalService.getOAuth2RefreshToken(
					oAuth2RefreshTokenId);

			oAuth2ApplicationId = oAuth2RefreshToken.getOAuth2ApplicationId();
		}
		else {
			return false;
		}

		OAuth2Application oAuth2Application =
			oAuth2ApplicationService.getOAuth2Application(oAuth2ApplicationId);

		oAuth2ApplicationService.check(oAuth2Application,
			OAuth2ProviderActionKeys.ACTION_REVOKE_TOKEN);

		return oAuth2AuthorizationLocalService.revokeAuthorization(
			oAuth2TokenId, oAuth2RefreshTokenId);
	}

}