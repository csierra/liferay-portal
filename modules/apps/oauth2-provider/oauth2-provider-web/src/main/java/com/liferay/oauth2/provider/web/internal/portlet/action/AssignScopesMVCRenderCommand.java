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

import com.liferay.oauth2.provider.model.LiferayOAuth2Scope;
import com.liferay.oauth2.provider.scopes.liferay.api.ScopeFinderLocator;
import com.liferay.oauth2.provider.web.OAuth2AdminPortletKeys;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.WebKeys;
import org.osgi.framework.Bundle;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.liferay.oauth2.provider.web.internal.constants.OAuth2AdminWebKeys.SCOPES;

/**
 * @author Tomas Polesovsky
 */
@Component(
	property = {
		"javax.portlet.name=" + OAuth2AdminPortletKeys.OAUTH2_ADMIN,
		"mvc.command.name=/admin/assign_scopes"
	}
)
public class AssignScopesMVCRenderCommand implements MVCRenderCommand {

	@Override
	public String render(
		RenderRequest renderRequest, RenderResponse renderResponse) {

		Collection<LiferayOAuth2Scope> scopes =
			_scopeFinderLocator.listScopes();

		Map<String, List<LiferayOAuth2Scope>> applicationScopes =
			new HashMap<>();

		for (LiferayOAuth2Scope scope : scopes) {
			List<LiferayOAuth2Scope> applicationScopeList =
				applicationScopes.computeIfAbsent(
					scope.getApplicationName(), key -> new ArrayList<>());

			applicationScopeList.add(new SerializableLiferayOAuth2Scope(scope));
		}

		renderRequest.setAttribute(SCOPES, applicationScopes);

		return "/admin/assign_scopes.jsp";
	}

	@Reference
	private ScopeFinderLocator _scopeFinderLocator;
}

class SerializableLiferayOAuth2Scope implements LiferayOAuth2Scope {

	public SerializableLiferayOAuth2Scope(
		LiferayOAuth2Scope liferayOAuth2Scope) {
		_liferayOAuth2Scope = liferayOAuth2Scope;
	}

	@Override
	public Bundle getBundle() {
		return _liferayOAuth2Scope.getBundle();
	}

	@Override
	public String getApplicationName() {
		return _liferayOAuth2Scope.getApplicationName();
	}

	@Override
	public String getScope() {
		return _liferayOAuth2Scope.getScope();
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(5);

		sb.append(getBundle().getSymbolicName());
		sb.append("/");
		sb.append(getApplicationName());
		sb.append("/");
		sb.append(getScope());

		return sb.toString();
	}

	private LiferayOAuth2Scope _liferayOAuth2Scope;
}