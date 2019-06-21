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

import java.util.Collections;
import java.util.List;

/**
 * @author Stian Sigvartsen
 */
public final class OAuth2Scope {

	public interface Builder {

		public ScopeAssigner forApplication(String applicationName);

		public interface ScopeAssigner {

			public default <T extends ScopeAssigner & Finished> T assignScope(
				String scope) {

				return assignScope(scope, Collections.singletonList(scope));
			}

			public <T extends ScopeAssigner & Finished> T assignScope(
				String scope, List<String> scopeAliases);

			public default <T extends ScopeAssigner & Finished> T assignScope(
				String scope, String scopeAlias) {

				return assignScope(
					scope, Collections.singletonList(scopeAlias));
			}

		}

	}

	public interface Finished {
	}

}