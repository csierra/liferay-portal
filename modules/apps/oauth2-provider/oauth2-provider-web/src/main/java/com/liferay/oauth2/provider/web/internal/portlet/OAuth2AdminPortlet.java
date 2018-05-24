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

import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.oauth2.provider.configuration.OAuth2ProviderConfiguration;
import com.liferay.oauth2.provider.constants.GrantType;
import com.liferay.oauth2.provider.model.OAuth2Application;
import com.liferay.oauth2.provider.service.OAuth2ApplicationService;
import com.liferay.oauth2.provider.service.OAuth2AuthorizationService;
import com.liferay.oauth2.provider.web.internal.constants.ClientProfile;
import com.liferay.oauth2.provider.web.internal.constants.OAuth2ProviderPortletKeys;
import com.liferay.oauth2.provider.web.internal.constants.OAuth2ProviderWebKeys;
import com.liferay.oauth2.provider.web.internal.display.context.OAuth2AdminPortletDisplayContext;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Stian Sigvartsen
 */
@Component(
	configurationPid = "com.liferay.oauth2.provider.configuration.OAuth2ProviderConfiguration",
	immediate = true,
	property = {
		"com.liferay.portlet.css-class-wrapper=portlet-oauth2-provider-admin",
		"com.liferay.portlet.display-category=category.hidden",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.preferences-company-wide=true",
		"javax.portlet.display-name=OAuth2 Admin",
		"javax.portlet.init-param.portlet-title-based-navigation=true",
		"javax.portlet.init-param.template-path=/admin/",
		"javax.portlet.init-param.view-template=/admin/view.jsp",
		"javax.portlet.name=" + OAuth2ProviderPortletKeys.OAUTH2_ADMIN,
		"javax.portlet.preferences=classpath:/META-INF/portlet-preferences/default-portlet-preferences.xml",
		"javax.portlet.resource-bundle=content.Language"
	},
	service = Portlet.class
)
public class OAuth2AdminPortlet extends MVCPortlet {

	public void deleteOAuth2Application(
		ActionRequest request, ActionResponse response) {

		long oAuth2ApplicationId = ParamUtil.getLong(
			request, "oAuth2ApplicationId");

		try {
			_oAuth2ApplicationService.deleteOAuth2Application(
				oAuth2ApplicationId);
		}
		catch (PortalException pe) {
			if (_log.isDebugEnabled()) {
				_log.debug(pe);
			}

			SessionErrors.add(request, pe.getClass());
		}
	}

	public void deleteOAuth2Applications(
		ActionRequest request, ActionResponse response) {

		long[] oAuth2ApplicationIds = StringUtil.split(
			ParamUtil.getString(request, "oAuth2ApplicationIds"), 0L);

		try {
			for (long oAuth2ApplicationId : oAuth2ApplicationIds) {
				_oAuth2ApplicationService.deleteOAuth2Application(
					oAuth2ApplicationId);
			}
		}
		catch (PortalException pe) {
			if (_log.isDebugEnabled()) {
				_log.debug(pe);
			}

			SessionErrors.add(request, pe.getClass());
		}
	}

	@Override
	public void render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		OAuth2AdminPortletDisplayContext oAuth2AdminPortletDisplayContext =
			new OAuth2AdminPortletDisplayContext(
				_oAuth2ApplicationService, _oAuth2ProviderConfiguration,
				getThemeDisplay(renderRequest));

		renderRequest.setAttribute(
			OAuth2ProviderWebKeys.ADMIN_DISPLAY_CONTEXT,
			oAuth2AdminPortletDisplayContext);

		super.render(renderRequest, renderResponse);
	}

	public void revokeOAuth2Authorization(
			ActionRequest request, ActionResponse response)
		throws PortalException {

		long oAuth2AuthorizationId = ParamUtil.getLong(
			request, "oAuth2AuthorizationId");

		_oAuth2AuthorizationService.revokeOAuth2Authorization(
			oAuth2AuthorizationId);
	}

	public void revokeOAuth2Authorizations(
		ActionRequest request, ActionResponse response) {

		long[] oAuth2AuthorizationIds = StringUtil.split(
			ParamUtil.getString(request, "oAuth2AuthorizationIds"), 0L);

		try {
			for (long oAuth2AuthorizationId : oAuth2AuthorizationIds) {
				_oAuth2AuthorizationService.revokeOAuth2Authorization(
					oAuth2AuthorizationId);
			}
		}
		catch (PortalException pe) {
			if (_log.isDebugEnabled()) {
				_log.debug(pe);
			}

			SessionErrors.add(request, pe.getClass());

			response.setRenderParameter(
				"mvcPath", "/admin/application_authorizations.jsp");
		}
	}

	public void updateOAuth2Application(
			ActionRequest request, ActionResponse response)
		throws PortalException {

		long oAuth2ApplicationId = ParamUtil.getLong(
			request, "oAuth2ApplicationId");

		int clientProfileId = ParamUtil.getInteger(request, "clientProfile");

		ClientProfile clientProfile = null;

		for (ClientProfile clientProfile2 : ClientProfile.values()) {
			if (clientProfile2.id() == clientProfileId) {
				clientProfile = clientProfile2;

				break;
			}
		}

		if (clientProfile == null) {
			throw new IllegalArgumentException(
				"No ClientProfile enum constant found with ID " +
					clientProfileId);
		}

		String clientId = ParamUtil.get(request, "clientId", StringPool.BLANK);

		String clientSecret = ParamUtil.get(
			request, "clientSecret", StringPool.BLANK);

		String description = ParamUtil.get(
			request, "description", StringPool.BLANK);

		PortletPreferences portletPreferences = request.getPreferences();

		OAuth2AdminPortletDisplayContext oAuth2AdminPortletDisplayContext =
			new OAuth2AdminPortletDisplayContext(
				_oAuth2ApplicationService, _oAuth2ProviderConfiguration,
				getThemeDisplay(request));

		String[] oAuth2Features =
			oAuth2AdminPortletDisplayContext.getOAuth2Features(
				portletPreferences);

		List<String> featuresList = new ArrayList<>();

		for (String feature : oAuth2Features) {
			if (ParamUtil.getBoolean(request, "feature-" + feature, false)) {
				featuresList.add(feature);
			}
		}

		String homePageURL = ParamUtil.get(
			request, "homePageURL", StringPool.BLANK);

		String name = ParamUtil.get(request, "name", StringPool.BLANK);

		List<GrantType> oAuth2Grants =
			oAuth2AdminPortletDisplayContext.getOAuth2Grants(
				portletPreferences);

		List<GrantType> allowedGrantTypes = new ArrayList<>();

		for (GrantType grantType : clientProfile.grantTypes()) {
			if (!oAuth2Grants.contains(grantType)) {
				continue;
			}

			if (ParamUtil.getBoolean(request, "grant-" + grantType.name())) {
				allowedGrantTypes.add(grantType);
			}
		}

		String privacyPolicyURL = ParamUtil.get(
			request, "privacyPolicyURL", StringPool.BLANK);

		List<String> redirectURIsList = Arrays.asList(
			StringUtil.splitLines(
				ParamUtil.get(request, "redirectURIs", StringPool.BLANK)));

		List<String> scopesList = Collections.emptyList();

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			OAuth2Application.class.getName(), request);

		try {
			if (oAuth2ApplicationId == 0) {
				clientId = name;

				for (GrantType grantType : allowedGrantTypes) {
					if (!grantType.isSupportsPublicClients()) {
						clientSecret =
							oAuth2AdminPortletDisplayContext.
								generateRandomSecret();
					}
				}

				OAuth2Application oAuth2Application =
					_oAuth2ApplicationService.addOAuth2Application(
						allowedGrantTypes, clientId, clientProfile.id(),
						clientSecret, description, featuresList, homePageURL, 0,
						name, privacyPolicyURL, redirectURIsList, scopesList,
						serviceContext);

				oAuth2ApplicationId =
					oAuth2Application.getOAuth2ApplicationId();
			}
			else {
				OAuth2Application oAuth2Application =
					_oAuth2ApplicationService.getOAuth2Application(
						oAuth2ApplicationId);

				long iconFileEntryId = oAuth2Application.getIconFileEntryId();

				long oAuth2ApplicationScopeAliasesId =
					oAuth2Application.getOAuth2ApplicationScopeAliasesId();

				_oAuth2ApplicationService.updateOAuth2Application(
					oAuth2ApplicationId, allowedGrantTypes, clientId,
					clientProfile.id(), clientSecret, description, featuresList,
					homePageURL, iconFileEntryId, name, privacyPolicyURL,
					redirectURIsList, oAuth2ApplicationScopeAliasesId,
					serviceContext);

				long fileEntryId = ParamUtil.getLong(request, "fileEntryId");

				if (ParamUtil.getBoolean(request, "deleteLogo")) {
					_oAuth2ApplicationService.updateIcon(
						oAuth2ApplicationId, null);
				}
				else if (fileEntryId > 0) {
					FileEntry fileEntry = _dlAppLocalService.getFileEntry(
						fileEntryId);

					InputStream inputStream = fileEntry.getContentStream();

					_oAuth2ApplicationService.updateIcon(
						oAuth2ApplicationId, inputStream);

					_dlAppLocalService.deleteFileEntry(fileEntryId);
				}
			}
		}
		catch (PortalException pe) {
			if (_log.isDebugEnabled()) {
				_log.debug(pe);
			}

			Class<?> peClass = pe.getClass();

			SessionErrors.add(request, peClass.getName(), pe);

			response.setRenderParameter(
				"mvcPath", "/admin/edit_application.jsp");
		}
	}

	@Activate
	protected void activate(Map<String, Object> properties) {
		_oAuth2ProviderConfiguration = ConfigurableUtil.createConfigurable(
			OAuth2ProviderConfiguration.class, properties);
	}

	protected ThemeDisplay getThemeDisplay(PortletRequest portletRequest) {
		return (ThemeDisplay)portletRequest.getAttribute(WebKeys.THEME_DISPLAY);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		OAuth2AdminPortlet.class);

	@Reference
	private DLAppLocalService _dlAppLocalService;

	@Reference
	private OAuth2ApplicationService _oAuth2ApplicationService;

	@Reference
	private OAuth2AuthorizationService _oAuth2AuthorizationService;

	private OAuth2ProviderConfiguration _oAuth2ProviderConfiguration;

	@Reference
	private Portal _portal;

}