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

package com.liferay.oauth2.provider.web.internal.portlet;

import com.liferay.oauth2.provider.model.OAuth2Application;
import com.liferay.oauth2.provider.service.OAuth2ApplicationService;
import com.liferay.oauth2.provider.service.OAuth2AuthorizationService;
import com.liferay.oauth2.provider.service.OAuth2RefreshTokenService;
import com.liferay.oauth2.provider.service.OAuth2TokenService;
import com.liferay.oauth2.provider.web.internal.constants.OAuth2ProviderPortletKeys;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringPool;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;

import com.liferay.portal.kernel.util.StringUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Stian Sigvartsen
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.hidden",
		"com.liferay.portlet.preferences-owned-by-group=true",
		"com.liferay.portlet.preferences-unique-per-layout=false",
		"javax.portlet.display-name=OAuth2 Admin",
		"javax.portlet.init-param.portlet-title-based-navigation=true",
		"javax.portlet.init-param.template-path=/admin/",
		"javax.portlet.init-param.view-template=/admin/view.jsp",
		"javax.portlet.name=" + OAuth2ProviderPortletKeys.OAUTH2_ADMIN_PORTLET,
		"javax.portlet.resource-bundle=content.Language"
	},
	service = Portlet.class
)
public class OAuth2AdminPortlet extends MVCPortlet {

	public void deleteOAuth2Application(
			ActionRequest request, ActionResponse response)
		throws PortalException {

		long oAuth2ApplicationId = ParamUtil.getLong(
			request, "oAuth2ApplicationId");

		try {
			_oAuth2ApplicationService.deleteOAuth2Application(
				oAuth2ApplicationId);
		}
		catch (PortalException pe) {
			SessionErrors.add(request, pe.getClass());
		}
	}

	public void updateOAuth2Application(
			ActionRequest request, ActionResponse response)
		throws PortalException {

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			OAuth2Application.class.getName(), request);

		long oAuth2ApplicationId = ParamUtil.getLong(
			request, "oAuth2ApplicationId");

		boolean clientConfidential = ParamUtil.get(
			request, "clientConfidential", false);
				
		String clientId = ParamUtil.get(
			request, "clientId", StringPool.BLANK);

		String clientSecret = ParamUtil.get(
			request, "clientSecret", StringPool.BLANK);

		String name = ParamUtil.get(request, "name", StringPool.BLANK);

		String description = ParamUtil.get(
			request, "description", StringPool.BLANK);

		String homePageURL = ParamUtil.get(
			request, "homePageURL", StringPool.BLANK);

		List<String> allowedGrantTypesList = Arrays.asList(
			ParamUtil.getParameterValues(
				request, "allowedGrantTypes", new String[0]));

		String privacyPolicyURL = ParamUtil.get(
			request, "privacyPolicyURL", StringPool.BLANK);

		List<String> redirectURIsList = Arrays.asList(
			StringUtil.splitLines(
				ParamUtil.get(request, "redirectURIs", StringPool.BLANK)));

		List<String> scopesList = Collections.emptyList();
		long iconFileEntryId = 0;

		OAuth2Application oAuth2Application = null;
		try {
			if (oAuth2ApplicationId == 0) {
				oAuth2Application =
					_oAuth2ApplicationService.addOAuth2Application(
						allowedGrantTypesList, clientConfidential, clientId,
						clientSecret, description, homePageURL, iconFileEntryId,
						name, privacyPolicyURL, redirectURIsList, scopesList,
						serviceContext);
			}
			else {
				oAuth2Application =
					_oAuth2ApplicationService.getOAuth2Application(
						oAuth2ApplicationId);

				iconFileEntryId = oAuth2Application.getIconFileEntryId();
				scopesList = oAuth2Application.getScopesList();

				oAuth2Application =
					_oAuth2ApplicationService.updateOAuth2Application(
						oAuth2ApplicationId, allowedGrantTypesList,
						clientConfidential, clientId, clientSecret, description,
						homePageURL, iconFileEntryId, name, privacyPolicyURL,
						redirectURIsList, scopesList, serviceContext);
			}

			UploadPortletRequest uploadPortletRequest =
				_portal.getUploadPortletRequest(request);

			String sourceFileName = uploadPortletRequest.getFileName("icon");

			if (sourceFileName != null) {
				try (InputStream inputStream =
						 uploadPortletRequest.getFileAsStream("icon")) {

					_oAuth2ApplicationService.updateIcon(
						oAuth2Application.getOAuth2ApplicationId(),
						inputStream);
				}
			}
		}
		catch (PortalException | IOException pe) {
			_log.error(pe);

			response.setRenderParameter(
				"mvcPath", "/admin/edit_application.jsp");

			Class<?> peClass = pe.getClass();

			SessionErrors.add(request, peClass.getName());
		}
	}

	public void revokeAuthorizationTokens(
			ActionRequest request, ActionResponse response)
		throws PortalException {

		long oAuth2TokenId = ParamUtil.getLong(request, "oAuth2TokenId");

		long oAuth2RefreshTokenId = ParamUtil.getLong(
			request, "oAuth2RefreshTokenId");

		_oAuth2AuthorizationService.revokeAuthorization(
			oAuth2TokenId, oAuth2RefreshTokenId);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		OAuth2AdminPortlet.class);

	@Reference
	private OAuth2ApplicationService _oAuth2ApplicationService;
	@Reference
	private OAuth2RefreshTokenService _oAuth2RefreshTokenService;
	@Reference
	private OAuth2AuthorizationService _oAuth2AuthorizationService;
	@Reference
	private OAuth2TokenService _oAuth2TokenService;
	@Reference
	private Portal _portal;

}