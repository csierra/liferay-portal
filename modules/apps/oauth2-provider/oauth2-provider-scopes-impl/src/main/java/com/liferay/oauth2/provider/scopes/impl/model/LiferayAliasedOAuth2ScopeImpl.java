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

package com.liferay.oauth2.provider.scopes.impl.model;

import com.liferay.oauth2.provider.model.LiferayAliasedOAuth2Scope;
import com.liferay.oauth2.provider.model.LiferayOAuth2Scope;
import org.osgi.framework.Bundle;

public class LiferayAliasedOAuth2ScopeImpl implements LiferayAliasedOAuth2Scope{

	public LiferayAliasedOAuth2ScopeImpl(
		LiferayOAuth2Scope liferayOAuth2Scope, String externalAlias) {

		_liferayOAuth2Scope = liferayOAuth2Scope;
		_externalAlias = externalAlias;
	}

	@Override
	public Bundle getBundle() {
		return _liferayOAuth2Scope.getBundle();
	}

	@Override
	public String getApplicationName() {
		return _liferayOAuth2Scope.getApplicationName();
	}

	@Override
	public String getScope() {
		return _liferayOAuth2Scope.getScope();
	}

	@Override
	public String getExternalAlias() {
		return _externalAlias;
	}

	private final LiferayOAuth2Scope _liferayOAuth2Scope;

	private final String _externalAlias;

}
