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

package com.liferay.oauth2.provider.web.internal.portlet.action;

import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.oauth2.provider.configuration.OAuth2ProviderConfiguration;
import com.liferay.oauth2.provider.constants.GrantType;
import com.liferay.oauth2.provider.model.OAuth2Application;
import com.liferay.oauth2.provider.service.OAuth2ApplicationService;
import com.liferay.oauth2.provider.web.constants.ClientProfile;
import com.liferay.oauth2.provider.web.constants.OAuth2ProviderPortletKeys;
import com.liferay.oauth2.provider.web.internal.display.context.OAuth2AdminPortletDisplayContext;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.InputStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Tomas Polesovsky
 */
@Component(
	configurationPid = "com.liferay.oauth2.provider.configuration.OAuth2ProviderConfiguration",
	property = {
		"javax.portlet.name=" + OAuth2ProviderPortletKeys.OAUTH2_ADMIN,
		"mvc.command.name=/admin/update_oauth2_application"
	}
)
public class UpdateOAuth2ApplicationMVCActionCommand
	implements MVCActionCommand {

	@Override
	public boolean processAction(
		ActionRequest request, ActionResponse response) {

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
				request, _oAuth2ApplicationService,
				_oAuth2ProviderConfiguration, null);

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

		try {
			ServiceContext serviceContext = ServiceContextFactory.getInstance(
				OAuth2Application.class.getName(), request);

			if (oAuth2ApplicationId == 0) {
				clientId = name;

				for (GrantType grantType : allowedGrantTypes) {
					if (!grantType.isSupportsPublicClients()) {
						clientSecret =
							oAuth2AdminPortletDisplayContext.
								generateRandomSecret();
					}
				}

				_oAuth2ApplicationService.addOAuth2Application(
					allowedGrantTypes, clientId, clientProfile.id(),
					clientSecret, description, featuresList, homePageURL, 0,
					name, privacyPolicyURL, redirectURIsList, scopesList,
					serviceContext);
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

		return true;
	}

	@Activate
	protected void activate(Map<String, Object> properties) {
		_oAuth2ProviderConfiguration = ConfigurableUtil.createConfigurable(
			OAuth2ProviderConfiguration.class, properties);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		UpdateOAuth2ApplicationMVCActionCommand.class);

	@Reference
	private DLAppLocalService _dlAppLocalService;

	@Reference
	private OAuth2ApplicationService _oAuth2ApplicationService;

	private OAuth2ProviderConfiguration _oAuth2ProviderConfiguration;

}