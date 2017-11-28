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

package com.liferay.oauth2.provider.api.scopes;

import java.util.Locale;

/**
 * @author Carlos Sierra Andrés
 */
public interface Scope {

	public interface Registrator {

		public void register(Registry registry);

	}

	interface LocalizedScopeDescription {
		public String getDescription(Locale language);
	}

	interface Registry {

		default public void register(Class<? extends Scope> scopeType) {
			register(scopeType, __ -> "No description");
		}

		public void register(
			Class<? extends Scope> scopeType,
			LocalizedScopeDescription description);

	}

}
