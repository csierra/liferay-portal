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

package com.liferay.oauth2.provider.scopes.api;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface ScopeChecker {

	public boolean hasScope(String scope);

	public default boolean hasAllScopes(String ... scopes) {
		for (String scope : scopes) {
			if (!hasScope(scope)) {
				return false;
			}
		}

		return true;
	}

	public default boolean hasAnyScope(String ... scopes) {
		for (String scope : scopes) {
			if (hasScope(scope)) {
				return true;
			}
		}

		return false;
	}

}