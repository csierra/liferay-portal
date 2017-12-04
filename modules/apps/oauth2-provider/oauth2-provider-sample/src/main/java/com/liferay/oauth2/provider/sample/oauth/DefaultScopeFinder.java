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

import com.liferay.oauth2.provider.api.scopes.OAuth2Scope;
import com.liferay.oauth2.provider.api.scopes.ScopeFinder;
import com.liferay.portal.kernel.util.ResourceBundleLoader;
import com.liferay.portal.kernel.util.ResourceBundleUtil;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class DefaultScopeFinder implements ScopeFinder {

	public DefaultScopeFinder(
		ResourceBundleLoader resourceBundleLoader,
		String namespace, String ... scopes) {

		_resourceBundleLoader = resourceBundleLoader;
		_namespace = namespace;
		_scopes = Arrays.asList(scopes);
	}

	public DefaultScopeFinder(
		String namespace, List<String> scopes) {
		_namespace = namespace;
		_scopes = scopes;
		_resourceBundleLoader = new ResourceBundleLoader() {
			@Override
			public ResourceBundle loadResourceBundle(Locale locale) {
				return ResourceBundleUtil.EMPTY_RESOURCE_BUNDLE;
			}

			@Override
			public ResourceBundle loadResourceBundle(String languageId) {
				return ResourceBundleUtil.EMPTY_RESOURCE_BUNDLE;
			}
		};
	}

	@Override
	public Collection<OAuth2Scope> findScopes(String name) {
		if (_scopes.contains(name)) {
			return Collections.singleton(
				new OAuth2Scope() {
					@Override
					public String scopeUUID() {
						return _namespace + name;
					}

					@Override
					public String getDescription(Locale locale) {
						ResourceBundle resourceBundle =
							_resourceBundleLoader.loadResourceBundle(locale);

						return resourceBundle.getString(name);
					}
				}
			);
		}

		return Collections.emptyList();
	}

	private ResourceBundleLoader _resourceBundleLoader;
	private String _namespace;
	private List<String> _scopes;

}
