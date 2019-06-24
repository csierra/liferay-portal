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

package com.liferay.oauth2.provider.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Carlos Sierra Andrés
 */
public final class OAuth2ApplicationScopes {

	public static OAuth2ApplicationScopes create(
		ApplicationScopes applicationScopes,
		ApplicationScopes... applicationScopesArray) {

		List<ApplicationScopes> applicationScopesList = new ArrayList<>();

		applicationScopesList.add(applicationScopes);

		Collections.addAll(applicationScopesList, applicationScopesArray);

		return new OAuth2ApplicationScopes(applicationScopesList);
	}

	public List<ApplicationScopes> getApplicationScopes() {
		return _applicationScopes;
	}

	public static final class ApplicationScope {

		public static ApplicationScope create(
			String scopeAlias, String scopeName) {

			return new ApplicationScope(scopeAlias, scopeName);
		}

		public String getScopeAlias() {
			return _scopeAlias;
		}

		public String getScopeName() {
			return _scopeName;
		}

		private ApplicationScope(String scopeAlias, String scopeName) {
			_scopeAlias = scopeAlias;
			_scopeName = scopeName;
		}

		private final String _scopeAlias;
		private final String _scopeName;

	}

	public static final class ApplicationScopes {

		public static ApplicationScopes create(
			String applicationName, ApplicationScope applicationScope,
			ApplicationScope... applicationScopes) {

			List<ApplicationScope> applicationScopeList = new ArrayList<>();

			applicationScopeList.add(applicationScope);

			Collections.addAll(applicationScopeList, applicationScopes);

			return new ApplicationScopes(applicationName, applicationScopeList);
		}

		public String getApplicationName() {
			return _applicationName;
		}

		public List<ApplicationScope> getApplicationScopes() {
			return _applicationScopes;
		}

		private ApplicationScopes(
			String applicationName, List<ApplicationScope> applicationScopes) {

			_applicationName = applicationName;
			_applicationScopes = applicationScopes;
		}

		private final String _applicationName;
		private final List<ApplicationScope> _applicationScopes;

	}

	private OAuth2ApplicationScopes(List<ApplicationScopes> applicationScopes) {
		_applicationScopes = applicationScopes;
	}

	private List<ApplicationScopes> _applicationScopes;

}