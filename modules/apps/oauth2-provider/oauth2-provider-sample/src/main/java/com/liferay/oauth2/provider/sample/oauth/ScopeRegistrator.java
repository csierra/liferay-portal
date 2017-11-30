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
import com.liferay.portal.kernel.util.ResourceBundleLoader;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true)
public class ScopeRegistrator implements OAuth2Scopes.Registrator {

	public static interface Scopes extends OAuth2Scopes.Namespace {
		public interface READ extends OAuth2Scopes.Scope {}

		public interface WRITE extends OAuth2Scopes.Scope {}
	}

	@Override
	public void register(OAuth2Scopes.Registry registry) {
		registry.register(
			Scopes.class,
			LocalizedScopesDescription.fromResourceBundleLoader(
				_resourceBundleLoader));
	}

	@Reference(
		target ="(bundle.symbolic.name=com.liferay.oauth2.provider.sample)")
	private ResourceBundleLoader _resourceBundleLoader;

}
