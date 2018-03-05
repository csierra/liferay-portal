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

import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.oauth2.provider.constants.OAuth2ProviderConstants;
import com.liferay.oauth2.provider.exception.DuplicateOAuth2ClientIdException;
import com.liferay.oauth2.provider.exception.NoSuchOAuth2ApplicationException;
import com.liferay.oauth2.provider.model.OAuth2Application;
import com.liferay.oauth2.provider.model.OAuth2RefreshToken;
import com.liferay.oauth2.provider.model.OAuth2ScopeGrant;
import com.liferay.oauth2.provider.model.OAuth2Token;
import com.liferay.oauth2.provider.service.base.OAuth2ApplicationLocalServiceBaseImpl;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Repository;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portletfilerepository.PortletFileRepositoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.ContentTypes;

import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * The implementation of the o auth2 application local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.liferay.oauth2.provider.service.OAuth2ApplicationLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see OAuth2ApplicationLocalServiceBaseImpl
 * @see com.liferay.oauth2.provider.service.OAuth2ApplicationLocalServiceUtil
 */
public class OAuth2ApplicationLocalServiceImpl
	extends OAuth2ApplicationLocalServiceBaseImpl {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Always use {@link com.liferay.oauth2.provider.service.OAuth2ApplicationLocalServiceUtil} to access the o auth2 application local service.
	 */

	@Override
	public OAuth2Application addOAuth2Application(
			long companyId, long userId,
			String userName, List<String> allowedGrantTypesList,
			boolean clientConfidential, String clientId, String clientSecret,
			String description, String homePageURL, long iconFileEntryId,
			String name, String privacyPolicyURL, List<String> redirectURIsList,
			List<String> scopesList, ServiceContext serviceContext)
		throws PortalException {

		if (oAuth2ApplicationPersistence.fetchByC_CI(
				serviceContext.getCompanyId(), clientId) != null) {

			throw new DuplicateOAuth2ClientIdException();
		}

		Date now = new Date();

		long oAuth2ApplicationId = counterLocalService.increment(
			OAuth2Application.class.getName());

		OAuth2Application oAuth2Application =
			oAuth2ApplicationPersistence.create(oAuth2ApplicationId);

		oAuth2Application.setCompanyId(companyId);
		oAuth2Application.setCreateDate(now);
		oAuth2Application.setModifiedDate(now);
		oAuth2Application.setUserId(userId);
		oAuth2Application.setUserName(userName);
		oAuth2Application.setAllowedGrantTypesList(allowedGrantTypesList);
		oAuth2Application.setClientConfidential(clientConfidential);
		oAuth2Application.setClientId(clientId);
		oAuth2Application.setClientSecret(clientSecret);
		oAuth2Application.setDescription(description);
		oAuth2Application.setHomePageURL(homePageURL);
		oAuth2Application.setIconFileEntryId(iconFileEntryId);
		oAuth2Application.setName(name);
		oAuth2Application.setPrivacyPolicyURL(privacyPolicyURL);
		oAuth2Application.setRedirectURIsList(redirectURIsList);
		oAuth2Application.setScopesList(scopesList);

		// Resources

		resourceLocalService.addResources(
			oAuth2Application.getCompanyId(), 0, oAuth2Application.getUserId(),
			OAuth2Application.class.getName(),
			oAuth2Application.getOAuth2ApplicationId(), false, false, false);

		return oAuth2ApplicationPersistence.update(oAuth2Application);
	}

	@Override
	public OAuth2Application deleteOAuth2Application(long oAuth2ApplicationId)
		throws PortalException {

		Collection<OAuth2Token> oAuth2Tokens =
			oAuth2TokenLocalService.findByApplicationId(oAuth2ApplicationId,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

		for (OAuth2Token oAuth2Token : oAuth2Tokens) {
			Collection<OAuth2ScopeGrant> grants =
				oAuth2ScopeGrantLocalService.findByToken(
					oAuth2Token.getOAuth2TokenId());

			for (OAuth2ScopeGrant grant : grants) {
				oAuth2ScopeGrantLocalService.deleteOAuth2ScopeGrant(grant);
			}

			oAuth2TokenLocalService.deleteOAuth2Token(oAuth2Token);
		}

		Collection<OAuth2RefreshToken> refreshTokens =
			oAuth2RefreshTokenLocalService.findByApplication(
				oAuth2ApplicationId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
				null);

		for (OAuth2RefreshToken refreshToken : refreshTokens) {
			oAuth2RefreshTokenLocalService.deleteOAuth2RefreshToken(
				refreshToken);
		}

		return super.deleteOAuth2Application(oAuth2ApplicationId);
	}

	@Override
	public OAuth2Application fetchOAuth2Application(
		long companyId, String clientId) {

		return oAuth2ApplicationPersistence.fetchByC_CI(companyId, clientId);
	}

	@Override
	public OAuth2Application getOAuth2Application(
			long companyId, String clientId)
		throws NoSuchOAuth2ApplicationException {

		return oAuth2ApplicationPersistence.findByC_CI(companyId, clientId);
	}

	@Override
	public OAuth2Application updateOAuth2Application(
			long oAuth2ApplicationId, List<String>  allowedGrantTypesList,
			boolean clientConfidential, String clientId, String clientSecret,
			String description, String homePageURL, long iconFileEntryId,
			String name, String privacyPolicyURL, List<String> redirectURIsList,
			List<String> scopesList, ServiceContext serviceContext)
		throws PortalException {

		OAuth2Application oAuth2Application =
			oAuth2ApplicationPersistence.findByPrimaryKey(oAuth2ApplicationId);

		Date now = new Date();

		oAuth2Application.setModifiedDate(now);

		oAuth2Application.setAllowedGrantTypesList(allowedGrantTypesList);
		oAuth2Application.setClientConfidential(clientConfidential);
		oAuth2Application.setClientId(clientId);
		oAuth2Application.setClientSecret(clientSecret);
		oAuth2Application.setDescription(description);
		oAuth2Application.setHomePageURL(homePageURL);
		oAuth2Application.setIconFileEntryId(iconFileEntryId);
		oAuth2Application.setName(name);
		oAuth2Application.setPrivacyPolicyURL(privacyPolicyURL);
		oAuth2Application.setRedirectURIsList(redirectURIsList);
		oAuth2Application.setScopesList(scopesList);

		return oAuth2ApplicationPersistence.update(oAuth2Application);
	}

	@Override
	public OAuth2Application updateIcon(
			long oAuth2ApplicationId, InputStream inputStream)
		throws PortalException {

		OAuth2Application oAuth2Application =
			getOAuth2Application(oAuth2ApplicationId);

		long oldIconFileEntryId = oAuth2Application.getIconFileEntryId();

		long companyId = oAuth2Application.getCompanyId();

		Group group = groupLocalService.getCompanyGroup(companyId);
		long defaultUserId = userLocalService.getDefaultUserId(companyId);

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddGuestPermissions(true);

		Repository repository = PortletFileRepositoryUtil.addPortletRepository(
			group.getGroupId(), OAuth2ProviderConstants.SERVICE_NAME,
			serviceContext);

		Folder folder = PortletFileRepositoryUtil.addPortletFolder(
			defaultUserId, repository.getRepositoryId(),
			DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, "icons",
			serviceContext);

		String fileName = PortletFileRepositoryUtil.getUniqueFileName(
			group.getGroupId(), folder.getFolderId(),
			oAuth2Application.getClientId());

		FileEntry fileEntry = PortletFileRepositoryUtil.addPortletFileEntry(
			group.getGroupId(), oAuth2Application.getUserId(),
			OAuth2Application.class.getName(),
			oAuth2ApplicationId, OAuth2ProviderConstants.SERVICE_NAME,
			folder.getFolderId(), inputStream, fileName, null, false);

		oAuth2Application.setIconFileEntryId(fileEntry.getFileEntryId());

		oAuth2Application = updateOAuth2Application(oAuth2Application);

		if (oldIconFileEntryId > 0) {
			PortletFileRepositoryUtil.deletePortletFileEntry(
				oldIconFileEntryId);
		}

		return oAuth2Application;
	}

	@Override
	public OAuth2Application updateScopes(
			long oAuth2ApplicationId, List<String> scopes)
		throws NoSuchOAuth2ApplicationException {

		OAuth2Application oAuth2Application =
			oAuth2ApplicationPersistence.findByPrimaryKey(oAuth2ApplicationId);

		Date now = new Date();

		oAuth2Application.setScopesList(scopes);
		oAuth2Application.setModifiedDate(now);

		return oAuth2ApplicationPersistence.update(oAuth2Application);
	}

}