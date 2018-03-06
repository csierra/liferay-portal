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

package com.liferay.oauth2.provider.scope.liferay;

import java.util.Collection;

/**
 * This interface allows to access the ScopeFinders with <i>per-company</i>
 * configurations in a Liferay environment.
 *
 * @author Carlos Sierra Andrés
 * @review
 */
public interface ScopeLocator {

	/**
	 * Returns a list of the scope aliases that would be a match if
	 * requested for a company.
	 * @param company the company to list the scope names
	 * @return a collection of scope aliases that would be a match for the
	 * company.
	 * @review
	 */
	public Collection<String> locateScopeAliases(long companyId);

	/**
	 * Returns a list of the scope aliases that would be a match if
	 * requested for an application.
	 * @param company the application to list the scope aliases
	 * @return a collection of scope aliases that would be a match for the
	 * company.
	 * @review
	 */
	public Collection<String> locateScopeAliasesForApplication(
		long companyId, String applicationName);

	/**
	 * returns a collection of matching scope according to the configuration
	 * for that company.
	 * @param company the company for which the scopes are to be located
	 * @param scope the requested scope
	 * @return a collection of matching scopes for the given company and
	 * scope name
	 * @review
	 */
	public Collection<LiferayOAuth2Scope> locateScopes(
		long companyId, String scopesAlias);

	/**
	 * returns a collection of matching scope according to the configuration
	 * for that application.
	 * @param applicationName the application for which the scopes are to be
	 * located
	 * @param scope the requested scope
	 * @return a collection of matching scopes for the given application and
	 * scope name
	 * @review
	 */
	public Collection<LiferayOAuth2Scope> locateScopesForApplication(
		long companyId, String scopesAlias, String applicationName);

}