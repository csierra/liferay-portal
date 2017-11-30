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

package com.liferay.oauth2.provider.sample.oauth;

import com.liferay.oauth2.provider.api.scopes.OAuth2Scopes;
import com.liferay.oauth2.provider.api.scopes.OAuth2Scopes.LocalizedScopesDescription;

public class ScopeExtender implements OAuth2Scopes.Extender<ScopeRegistrator.Scopes> {

	@Override
	public LocalizedScopesDescription getLocalizedScopesDescription() {
		return LocalizedScopesDescription.fromResourceBundleLoader(null);
	}
}
