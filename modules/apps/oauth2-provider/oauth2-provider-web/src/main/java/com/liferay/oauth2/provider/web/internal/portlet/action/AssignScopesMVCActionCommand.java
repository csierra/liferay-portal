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

import com.liferay.oauth2.provider.model.OAuth2Application;
import com.liferay.oauth2.provider.service.OAuth2ApplicationLocalService;
import com.liferay.oauth2.provider.web.OAuth2AdminPortletKeys;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tomas Polesovsky
 */
@Component(
	property = {
		"javax.portlet.name=" + OAuth2AdminPortletKeys.OAUTH2_ADMIN,
		"mvc.command.name=/admin/assign_scopes"
	}
)
public class AssignScopesMVCActionCommand implements MVCActionCommand {

	@Override
	public boolean processAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws PortletException {

		List<String> scopes = new ArrayList<>();
		for (String parameterName : actionRequest.getParameterMap().keySet()) {
			if (parameterName.startsWith("scope_") &&
				ParamUtil.getBoolean(actionRequest, parameterName)) {
				scopes.add(parameterName.substring("scope_".length()));
			}
		}

		long oAuth2ApplicationId = ParamUtil.getLong(
			actionRequest, "oAuth2ApplicationId");

		try {
			OAuth2Application oAuth2Application =
				_oAuth2ApplicationLocalService.getOAuth2Application(
					oAuth2ApplicationId);

			oAuth2Application.setScopesList(scopes);

			_oAuth2ApplicationLocalService.updateOAuth2Application(
				oAuth2Application);
		}
		catch (PortalException e) {
			if (_log.isDebugEnabled()) {
				_log.warn(
					"Unable to load OAuth2Application with id " +
					oAuth2ApplicationId);
			}
		}

		return true;
	}

	@Reference
	OAuth2ApplicationLocalService _oAuth2ApplicationLocalService;

	private Log _log = LogFactoryUtil.getLog(AssignScopesMVCActionCommand.class);

}