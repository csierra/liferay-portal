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

package com.liferay.oauth2.provider.scopes.impl.jaxrs;


import com.liferay.oauth2.provider.scopes.liferay.api.ScopeContext;
import org.osgi.framework.Bundle;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

public class ScopeContextBundleContainerRequestFilter
	implements ContainerRequestFilter {

	public ScopeContextBundleContainerRequestFilter(
		ScopeContext scopeContext, Bundle bundle, String applicationName) {

		_scopeStack = scopeContext;
		_bundle = bundle;
		_applicationName = applicationName;
	}

	@Override
	public void filter(ContainerRequestContext requestContext)
		throws IOException {

		_scopeStack.setBundle(_bundle);
		_scopeStack.setApplicationName(_applicationName);
	}

	private final ScopeContext _scopeStack;
	private final Bundle _bundle;
	private final String _applicationName;

}