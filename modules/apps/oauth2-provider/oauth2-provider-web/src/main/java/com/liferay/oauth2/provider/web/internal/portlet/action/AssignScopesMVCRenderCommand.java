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
import com.liferay.oauth2.provider.scopes.liferay.api.ScopedServiceTrackerMap;
import com.liferay.oauth2.provider.scopes.spi.ScopeDescriptor;
import com.liferay.oauth2.provider.web.OAuth2AdminPortletKeys;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static com.liferay.oauth2.provider.web.internal.constants.OAuth2AdminWebKeys.SCOPES;
import static com.liferay.oauth2.provider.web.internal.constants.OAuth2AdminWebKeys.SCOPES_DESCRIPTIONS;

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

	@Activate
	protected void activate(BundleContext bundleContext) {
		_scopedScopeDescriptors = new ScopedServiceTrackerMap<>(
			bundleContext, ScopeDescriptor.class, "osgi.jaxrs.name",
			() -> _defaultScopeDescriptor);
	}

	@Override
	public String render(
		RenderRequest renderRequest, RenderResponse renderResponse) {

		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		Company company = themeDisplay.getCompany();

		Collection<LiferayOAuth2Scope> scopes = _scopeFinderLocator.listScopes(
			company.getCompanyId());

		Map<String, List<LiferayOAuth2Scope>> aliasedScopes = new HashMap<>();
		Map<String, Set<String>> scopesDescriptions = new HashMap<>();

		for (LiferayOAuth2Scope scope : scopes) {
			List<LiferayOAuth2Scope> aliasedScopesList =
				aliasedScopes.computeIfAbsent(
					scope.getScope(), __ -> new ArrayList<>());

			aliasedScopesList.add(scope);

			ScopeDescriptor scopeDescriptor =
				_scopedScopeDescriptors.getService(
					company.getCompanyId(), scope.getApplicationName());

			String description = scopeDescriptor.describe(
				scope.getInternalScope(), themeDisplay.getLocale());

			Set<String> descriptionSet = scopesDescriptions.computeIfAbsent(
				scope.getScope(), __ -> new HashSet<>());

			descriptionSet.add(description);
		}

		renderRequest.setAttribute(SCOPES, aliasedScopes);
		renderRequest.setAttribute(SCOPES_DESCRIPTIONS, scopesDescriptions);

		return "/admin/assign_scopes.jsp";
	}

	@Deactivate
	protected void deactivate() {
		_scopedScopeDescriptors.close();
	}


	@Reference
	private ScopeFinderLocator _scopeFinderLocator;

	private ScopedServiceTrackerMap<ScopeDescriptor> _scopedScopeDescriptors;

	@Reference(
		target = "(default=true)",
		policyOption = ReferencePolicyOption.GREEDY
	)
	private ScopeDescriptor _defaultScopeDescriptor;


}