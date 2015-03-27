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

package com.liferay.portal.language;

import com.liferay.portal.kernel.language.LanguageResourceManager;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Adolfo Pérez
 */
public class LanguageResourceManagerImpl implements LanguageResourceManager {

	@Override
	public String getMessage(Locale locale, String key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResourceBundle getResourceBundle(Locale locale) {
		throw new UnsupportedOperationException();
	}

	public void setConfig(String config) {
		_configNames = StringUtil.split(
			config.replace(CharPool.PERIOD, CharPool.SLASH));
	}

	private String[] _configNames;

}