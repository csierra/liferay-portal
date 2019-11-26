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

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * @author Stian Sigvartsen
 * @author Carlos Sierra
 */
public interface OAuth2Scope {

	public interface Builder {

		public OAuth2Scope.Built forApplication(
			String applicationName,
			Function<ApplicationScopeAssigner, ApplicationScope>
				applicationScopeAssignerFunction);

		public interface ApplicationScope extends ApplicationScopeAssigner {
		}

		public interface ApplicationScopeAssigner {

			public default ApplicationScope assignScope(
				List<String> scopeList) {

				return assignScope(scopeList, __ -> scopeList);
			}

			public ApplicationScope assignScope(
				List<String> scope,
				Function<String, List<String>> scopeMapperFunction);

			public default ApplicationScope assignScope(String... scopes) {
				List<String> scopeList = Arrays.asList(scopes);

				return assignScope(scopeList, __ -> scopeList);
			}

		}

	}

	public interface Built extends Builder {
	}

}