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

package com.liferay.portal.remote.cors.internal.path.pattern.matcher;

import com.liferay.portal.remote.cors.internal.CORSSupport;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.Map;

/**
 * @author Arthur Chan
 */
public class PathPatternMatcher {

	public static boolean isExtensionPattern(String pathPattern) {
		if ((pathPattern.length() < 3) || (pathPattern.charAt(0) != '*') ||
			(pathPattern.charAt(1) != '.')) {

			return false;
		}

		for (int i = 2; i < pathPattern.length(); ++i) {
			if (pathPattern.charAt(i) == '/') {
				return false;
			}

			if (pathPattern.charAt(i) == '.') {
				return false;
			}
		}

		return true;
	}

	public static boolean isWildcardPattern(String pathPattern) {
		if ((pathPattern.length() < 2) || (pathPattern.charAt(0) != '/') ||
			(pathPattern.charAt(pathPattern.length() - 1) != '*') ||
			(pathPattern.charAt(pathPattern.length() - 2) != '/')) {

			return false;
		}

		try {
			String path = pathPattern.substring(0, pathPattern.length() - 1);

			URI uri = new URI("https://test" + path);

			if (!path.contentEquals(uri.getPath())) {
				return false;
			}
		}
		catch (URISyntaxException uriSyntaxException) {
			return false;
		}

		return true;
	}

	public PathPatternMatcher(
		Map<String, CORSSupport> exactPatternCORSSupports,
		Map<String, CORSSupport> wildcardPatternCORSSupports,
		Map<String, CORSSupport> extensionPatternCORSSupports) {

		_exactPatternCORSSupports = exactPatternCORSSupports;
		_wildcardPatternCORSSupports = wildcardPatternCORSSupports;
		_extensionPatternCORSSupports = extensionPatternCORSSupports;
	}

	public CORSSupport getCORSSupport(String urlPath) {
		CORSSupport corsSupport = _exactPatternCORSSupports.get(urlPath);

		if (corsSupport != null) {
			return corsSupport;
		}

		int lastDot = 0;

		for (int i = urlPath.length(); i > 0; --i) {
			corsSupport = _wildcardPatternCORSSupports.get(
				urlPath.substring(0, i) + "*");

			if (corsSupport != null) {
				return corsSupport;
			}

			if ((lastDot < 1) && (urlPath.charAt(i - 1) == '.')) {
				lastDot = i - 1;
			}
		}

		return _extensionPatternCORSSupports.get(
			"*" + urlPath.substring(lastDot));
	}

	private final Map<String, CORSSupport> _exactPatternCORSSupports;
	private final Map<String, CORSSupport> _extensionPatternCORSSupports;
	private final Map<String, CORSSupport> _wildcardPatternCORSSupports;

}