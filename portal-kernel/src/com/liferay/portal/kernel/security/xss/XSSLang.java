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

package com.liferay.portal.kernel.security.xss;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.language.LanguageWrapper;

import javax.servlet.http.HttpServletRequest;
import java.util.ResourceBundle;

/**
 * @author Tomas Polesovsky
 */
public class XSSLang {
	public static CharSequence get(ResourceBundle resourceBundle, String key) {
		String value = LanguageUtil.get(resourceBundle, key, null);

		if (value != null) {
			return XSS.safeHtml(value);
		}

		return key;
	}

	public static CharSequence format(
		HttpServletRequest request, String pattern, LanguageWrapper argument) {



	}



}
