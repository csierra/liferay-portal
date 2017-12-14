/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.oauth2.provider.impl.scopes;

import com.liferay.oauth2.provider.api.scopes.NamespaceAdder;
import com.liferay.oauth2.provider.api.scopes.NamespaceAdderMapper;
import com.liferay.oauth2.provider.api.scopes.ScopeStack;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.util.PortalUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.osgi.service.component.annotations.ServiceScope;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import java.io.IOException;

@Component(
	immediate = true,
	scope = ServiceScope.PROTOTYPE
)
public class OAuth2CheckerNamespaceContainerRequestFilter implements
	ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext)
		throws IOException {

		try {
			Company company = PortalUtil.getCompany(_httpServletRequest);

			NamespaceAdderMapper namespaceAdderMapper =
				_namespaceAdderMapperLocator.locateMapper(company);

			NamespaceAdder namespaceAdder =
				namespaceAdderMapper.mapFrom(_configuration::getProperty);

			_scopeStack.pushNamespace(namespaceAdder);
		}
		catch (PortalException e) {
			e.printStackTrace();
		}
	}

	@Context
	private HttpServletRequest _httpServletRequest;

	@Context
	private Configuration _configuration;

	@Reference(policyOption = ReferencePolicyOption.GREEDY)
	ScopeStack _scopeStack;

	@Reference(policyOption = ReferencePolicyOption.GREEDY)
	NamespaceAdderMapperLocator _namespaceAdderMapperLocator;
}
