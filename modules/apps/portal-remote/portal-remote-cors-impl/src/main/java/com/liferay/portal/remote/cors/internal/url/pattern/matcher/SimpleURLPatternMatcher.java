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

package com.liferay.portal.remote.cors.internal.url.pattern.matcher;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Arthur Chan
 */
public class SimpleURLPatternMatcher<T> extends URLPatternMatcher<T> {

	@Override
	public T getValue(String urlPath) {
		T value = _exactPatternCORSSupports.get(urlPath);

		if (value != null) {
			return value;
		}

		int lastDot = 0;

		for (int i = urlPath.length(); i > 0; --i) {
			value = _wildcardPatternCORSSupports.get(
				urlPath.substring(0, i) + "*");

			if (value != null) {
				return value;
			}

			if ((lastDot < 1) && (urlPath.charAt(i - 1) == '.')) {
				lastDot = i - 1;
			}
		}

		return _extensionPatternCORSSupports.get(
			"*" + urlPath.substring(lastDot));
	}

	@Override
	public void putValue(String urlPattern, T value)
		throws IllegalArgumentException {

		if (URLPatternMatcher.isWildcardPattern(urlPattern)) {
			if (!_wildcardPatternCORSSupports.containsKey(urlPattern)) {
				_wildcardPatternCORSSupports.put(urlPattern, value);
			}

			return;
		}

		if (URLPatternMatcher.isExtensionPattern(urlPattern)) {
			if (!_extensionPatternCORSSupports.containsKey(urlPattern)) {
				_extensionPatternCORSSupports.put(urlPattern, value);
			}

			return;
		}

		if (!_exactPatternCORSSupports.containsKey(urlPattern)) {
			_exactPatternCORSSupports.put(urlPattern, value);
		}
	}

	private final Map<String, T> _exactPatternCORSSupports = new HashMap<>();
	private final Map<String, T> _extensionPatternCORSSupports =
		new HashMap<>();
	private final Map<String, T> _wildcardPatternCORSSupports = new HashMap<>();

}