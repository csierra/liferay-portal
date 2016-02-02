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

package com.liferay.portal.kernel.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Adolfo Pérez
 * @author Carlos Sierra
 */
public class ClassResourceBundleLoader implements ResourceBundleLoader {

	public ClassResourceBundleLoader(String baseName, Class<?> clazz) {
		this(baseName, clazz.getClassLoader());
	}

	public ClassResourceBundleLoader(String baseName, ClassLoader classLoader) {
		_baseName = baseName;
		_classLoader = classLoader;
	}

	@Override
	public ResourceBundle loadResourceBundle(String languageId) {
		if (_resourceBundles.containsKey(languageId)) {
			return _resourceBundles.get(languageId);
		}
		else {
			synchronized (_resourceBundles) {
				if (_resourceBundles.containsKey(languageId)) {
					return _resourceBundles.get(languageId);
				}

				ResourceBundle resourceBundle;

				Locale locale = LocaleUtil.fromLanguageId(languageId);

				try {
					resourceBundle = ResourceBundleUtil.getBundle(
						_baseName, locale, _classLoader);
				}
				catch (Exception e) {
					resourceBundle = null;
				}

				_resourceBundles.put(languageId, resourceBundle);

				return resourceBundle;
			}
		}
	}

	private final String _baseName;
	private final ClassLoader _classLoader;
	private final Map<String, ResourceBundle> _resourceBundles =
		new HashMap<>();

}