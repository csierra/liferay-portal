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

import com.liferay.portal.kernel.model.Company;

import org.osgi.framework.Bundle;

/**
 * This interface represents the context associated to the scope.
 * This scope, together with the scope name, will univocally identify
 * a checking point in a Liferay environment.
 *
 * @author Carlos Sierra Andrés
 */
public interface ScopeContext {

	public void clear();

	public void setApplicationName(String applicationName);

	public void setBundle(Bundle bundle);

	public void setCompany(Company company);

	public void setTokenString(String tokenString);

}