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

package com.liferay.oauth2.provider.web.internal.display.context;

import com.liferay.document.library.kernel.service.DLAppLocalServiceUtil;
import com.liferay.document.library.kernel.util.DLUtil;
import com.liferay.oauth2.provider.model.OAuth2Application;
import com.liferay.oauth2.provider.service.OAuth2ApplicationService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.PortletRequest;

/**
 * @author Tomas Polesovsky
 */
public class OAuth2ConnectedApplicationsPortletDisplayContext {

	public OAuth2ConnectedApplicationsPortletDisplayContext(
		AuthorizationModel authorizationModel, PortletRequest portletRequest,
		OAuth2ApplicationService oAuth2ApplicationService) {

		this(portletRequest);

		_authorizationModel = authorizationModel;
		_oAuth2ApplicationService = oAuth2ApplicationService;
	}

	public OAuth2ConnectedApplicationsPortletDisplayContext(
		PortletRequest portletRequest) {

		_portletRequest = portletRequest;

		_themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	public AuthorizationModel getAuthorizationModel() {
		return _authorizationModel;
	}

	public String getDefaultIconURL() {
		return _themeDisplay.getPathThemeImages() + "/common/portlet.png";
	}

	public OAuth2Application getOAuth2Application() throws PortalException {
		long oAuth2ApplicationId = ParamUtil.getLong(
			_portletRequest, "oAuth2ApplicationId");

		return _oAuth2ApplicationService.getOAuth2Application(
			oAuth2ApplicationId);
	}

	public String getThumbnailURL() throws Exception {
		OAuth2Application oAuth2Application = getOAuth2Application();

		if (oAuth2Application.getIconFileEntryId() <= 0) {
			return getDefaultIconURL();
		}

		FileEntry fileEntry = DLAppLocalServiceUtil.getFileEntry(
			oAuth2Application.getIconFileEntryId());

		return DLUtil.getThumbnailSrc(fileEntry, _themeDisplay);
	}

	private AuthorizationModel _authorizationModel;
	private OAuth2ApplicationService _oAuth2ApplicationService;
	private PortletRequest _portletRequest;
	private ThemeDisplay _themeDisplay;

}