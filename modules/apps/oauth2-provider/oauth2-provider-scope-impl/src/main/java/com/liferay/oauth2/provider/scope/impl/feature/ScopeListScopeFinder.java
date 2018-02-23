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

package com.liferay.oauth2.provider.scope.impl.feature;

import com.liferay.oauth2.provider.scope.spi.ScopeFinder;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

class ScopeListScopeFinder implements ScopeFinder {

	private final Collection<String> _scopes;

	public ScopeListScopeFinder(Collection<String> scopes) {
		_scopes = scopes;
	}

	@Override
	public Map<String, Set<String>> findScopes() {
		return _scopes.stream().collect(
			Collectors.toMap(Function.identity(), Collections::singleton));
	}
}
