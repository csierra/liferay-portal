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

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.ResourceBundleLoader;

import java.util.Locale;

/**
 * @author Carlos Sierra Andrés
 */
public interface OAuth2Scopes {

	public interface Scope {}

	public interface Namespace {}

	public interface Registrator {

		public void register(Registry registry);

	}

	public interface Extender<T extends Namespace> {
		public LocalizedScopesDescription getLocalizedScopesDescription();
	}

	interface LocalizedScopesDescription {
		public String getDescription(
			Class<? extends Scope> scopeType, Locale language);

		public static LocalizedScopesDescription fromResourceBundleLoader(
			ResourceBundleLoader resourceBundleLoader) {

			return ((scopeType, language) ->
				resourceBundleLoader.loadResourceBundle(
					LanguageUtil.getLanguageId(language)).
					getString(
						scopeType.getSimpleName()));
		}
	}

	interface Registry {

		public void register(
			Class<? extends Namespace> namespaceType,
			LocalizedScopesDescription description);

	}

}
