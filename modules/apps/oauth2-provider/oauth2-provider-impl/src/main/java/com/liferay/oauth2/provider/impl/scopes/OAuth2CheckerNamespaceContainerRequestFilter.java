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
import com.liferay.oauth2.provider.api.scopes.NamespaceAdderFactory;
import com.liferay.oauth2.provider.api.scopes.ScopeChecker;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.util.PortalUtil;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

import static aQute.bnd.component.FieldCollectionType.service;

@Component(
	immediate = true,
	scope = ServiceScope.PROTOTYPE
)
public class OAuth2CheckerNamespaceContainerRequestFilter implements
	ContainerRequestFilter {

	private BundleContext _bundleContext;
	private NamespaceAdder _namespaceAdder;

	@Override
	public void filter(ContainerRequestContext requestContext)
		throws IOException {

		try {
			Company company = PortalUtil.getCompany(_httpServletRequest);

			_scopeChecker.pushNamespace(
				_namespaceAdderFactory.create(
					Long.toString(company.getCompanyId())));

			_scopeChecker.pushNamespace(_namespaceAdder);
		}
		catch (PortalException e) {
			e.printStackTrace();
		}
	}

	@Context
	public void setConfiguration(Configuration configuration) {
		Map<String, Object> serviceProperties = (Map<String, Object>)
			configuration.getProperty(
				"osgi.jaxrs.application.serviceProperties");

		long bundleId = Long.parseLong(
			serviceProperties.get("service.bundleid").toString());

		Bundle bundle = _bundleContext.getBundle(bundleId);

		NamespaceAdder bundleSymbolicNameAdder = _namespaceAdderFactory.create(
			bundle.getSymbolicName());

		NamespaceAdder bundleVersionAdder = _namespaceAdderFactory.create(
			bundle.getVersion().toString());

		String applicationName =
			serviceProperties.get("osgi.jaxrs.name").toString();

		NamespaceAdder applicationNameAdder =
			_namespaceAdderFactory.create(applicationName);

		_namespaceAdder = bundleSymbolicNameAdder.append(
			bundleVersionAdder
		).append(
			applicationNameAdder
		);
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;
	}

	@Context
	private HttpServletRequest _httpServletRequest;

	@Reference
	ScopeChecker _scopeChecker;

	@Reference
	NamespaceAdderFactory _namespaceAdderFactory;
}
