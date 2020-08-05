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

package com.liferay.portal.remote.cors.internal;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Arthur Chan
 * @author Carlos Sierra Andrés
 * @author Brian Wing Shun Chan
 */
public class URLtoCORSSupportMapper {

	public static boolean isExtensionURLPattern(String urlPattern) {

		// Servlet 4 spec 12.1.3
		// Servlet 4 spec 12.2

		if ((urlPattern.length() < 3) || (urlPattern.charAt(0) != '*') ||
			(urlPattern.charAt(1) != '.')) {

			return false;
		}

		for (int i = 2; i < urlPattern.length(); ++i) {
			if (urlPattern.charAt(i) == '/') {
				return false;
			}

			if (urlPattern.charAt(i) == '.') {
				return false;
			}
		}

		return true;
	}

	public static boolean isWildcardURLPattern(String urlPattern) {

		// Servlet 4 spec 12.2

		if ((urlPattern.length() < 2) || (urlPattern.charAt(0) != '/') ||
			(urlPattern.charAt(urlPattern.length() - 1) != '*') ||
			(urlPattern.charAt(urlPattern.length() - 2) != '/')) {

			return false;
		}

		// RFC 3986 3.3

		try {
			String urlPath = urlPattern.substring(0, urlPattern.length() - 1);

			URI uri = new URI("https://test" + urlPath);

			if (!urlPath.contentEquals(uri.getPath())) {
				return false;
			}
		}
		catch (URISyntaxException uriSyntaxException) {
			return false;
		}

		return true;
	}

	public URLtoCORSSupportMapper(Map<String, CORSSupport> corsSupports) {
		for (Map.Entry<String, CORSSupport> entry : corsSupports.entrySet()) {
			_put(entry.getKey(), entry.getValue());
		}
	}

	public CORSSupport get(String urlPath) {
		CORSSupport corsSupport = _exactURLPatternCORSSupports.get(urlPath);

		if (corsSupport != null) {
			return corsSupport;
		}

		int index = 0;

		for (int i = urlPath.length(); i > 0; --i) {
			corsSupport = _wildcardURLPatternCORSSupports.get(
				urlPath.substring(0, i) + "*");

			if (corsSupport != null) {
				return corsSupport;
			}

			if ((index < 1) && (urlPath.charAt(i - 1) == '.')) {
				index = i - 1;
			}
		}

		return _extensionURLPatternCORSSupports.get(
			"*" + urlPath.substring(index));
	}

	private void _put(String urlPattern, CORSSupport corsSupport)
		throws IllegalArgumentException {

		if (isWildcardURLPattern(urlPattern)) {
			if (!_wildcardURLPatternCORSSupports.containsKey(urlPattern)) {
				_wildcardURLPatternCORSSupports.put(urlPattern, corsSupport);
			}

			return;
		}

		if (isExtensionURLPattern(urlPattern)) {
			if (!_extensionURLPatternCORSSupports.containsKey(urlPattern)) {
				_extensionURLPatternCORSSupports.put(urlPattern, corsSupport);
			}

			return;
		}

		if (!_exactURLPatternCORSSupports.containsKey(urlPattern)) {
			_exactURLPatternCORSSupports.put(urlPattern, corsSupport);
		}
	}

	private final Map<String, CORSSupport> _exactURLPatternCORSSupports =
		new HashMap<>();
	private final Map<String, CORSSupport> _extensionURLPatternCORSSupports =
		new HashMap<>();
	private final Map<String, CORSSupport> _wildcardURLPatternCORSSupports =
		new HashMap<>();

}