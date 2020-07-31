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

import com.liferay.portal.remote.cors.internal.CORSSupport;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.Map;

/**
 * @author Arthur Chan
 */
public class URLPatternMatcher {

	/**
	 *  https://download.oracle.com/otndocs/jcp/servlet-4-final-eval-spec/index.html#12.1.3
	 *  https://download.oracle.com/otndocs/jcp/servlet-4-final-eval-spec/index.html#12.2
	 *
	 * @param urlPattern the given urlPattern
	 * @return a boolean value indicating if the urlPattern a valid extensionPattern
	 */
	public static boolean isExtensionPattern(String urlPattern) {
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

	/**
	 *  https://tools.ietf.org/html/rfc3986#section-3.3
	 *  https://download.oracle.com/otndocs/jcp/servlet-4-final-eval-spec/index.html#12.2
	 *
	 * @param urlPattern the given urlPattern
	 * @return a boolean value indicating if the urlPattern a valid wildCardPattern
	 */
	public static boolean isWildcardPattern(String urlPattern) {
		if ((urlPattern.length() < 2) || (urlPattern.charAt(0) != '/') ||
			(urlPattern.charAt(urlPattern.length() - 1) != '*') ||
			(urlPattern.charAt(urlPattern.length() - 2) != '/')) {

			return false;
		}

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

	public URLPatternMatcher(
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