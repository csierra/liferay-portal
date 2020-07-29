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

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Arthur Chan
 * @author Carlos Sierra Andrés
 */
public abstract class URLPathPatternMatcher<T> {

	/**
	 * https://download.oracle.com/otndocs/jcp/servlet-4-final-eval-spec/index.html#12.1
	 *
	 * Get the matching pattern of the given urlPath, following the order of:
	 * 1. Exact matching pattern
	 * 2. Wild card matching the longest pattern
	 * 3. Extension pattern
	 * 4. Default pattern
	 *
	 * @param urlPath a legal urlPath from a URL
	 * @return the matched pattern
	 */
	public abstract PatternTuple<T> getPatternTuple(String urlPath);

	/**
	 * https://download.oracle.com/otndocs/jcp/servlet-4-final-eval-spec/index.html#12.2
	 *
	 * In the Web application deployment descriptor, the following syntax is
	 * used to define mappings:
	 * 1. Wild Card urlPath pattern 1:
	 *        A string beginning with a ' / ' character and ending with a ' /* '
	 *        suffix is used for urlPath mapping.
	 * 2. Wild Card urlPath pattern 2, aka extension matching:
	 *        A string beginning with a ' *. ' prefix is used as an extension
	 *        mapping.
	 * 3. Special urlPath pattern 1:
	 *        The empty string ("") is a special URL pattern that exactly maps
	 *        to the application's context root, i.e., requests of the form
	 *        http://host:port/'<'context-root'>'/. In this case the urlPath info is
	 *        ' / ' and the servlet urlPath and context urlPath is empty string ("").
	 * 4. Special urlPath pattern 2:
	 *        A string containing only the ' / ' character indicates the
	 *        "default" servlet of the application. In this case the servlet
	 *        urlPath is the request URI minus the context urlPath and the urlPath info
	 *        is null.
	 * 5. Exact urlPath pattern:
	 *        All other strings are used for exact matches only.
	 *
	 * @param urlPathPattern the pattern of urlPath, used for pattern matching
	 * @param value an non null object associated with urlPathPattern
	 */
	public abstract void insert(String urlPathPattern, T value)
		throws IllegalArgumentException;

	/**
	 * A valid ExtensionPattern:
	 * 1. Without the leading '*', it abides by the format of a segment of urlPath
	 *         of URI specification: https://tools.ietf.org/html/rfc3986#section-3.3
	 * 2. It also abides by:
	 *         https://download.oracle.com/otndocs/jcp/servlet-4-final-eval-spec/index.html#12.1.3, and
	 *         https://download.oracle.com/otndocs/jcp/servlet-4-final-eval-spec/index.html#12.2
	 *
	 * @param urlPathPattern the given urlPathPattern
	 * @return a boolean value indicating if the urlPathPattern a valid extensionPattern
	 */
	protected boolean isValidExtensionPattern(String urlPathPattern) {
		if ((urlPathPattern.length() < 3) ||
			(urlPathPattern.charAt(0) != '*') ||
			(urlPathPattern.charAt(1) != '.')) {

			return false;
		}

		for (int i = 2; i < urlPathPattern.length(); ++i) {
			if (urlPathPattern.charAt(i) == '/') {
				return false;
			}

			if (urlPathPattern.charAt(i) == '.') {
				return false;
			}
		}

		return true;
	}

	/**
	 * A valid WildCardPattern:
	 * 1. Without the trailing '*', it abides by the format of urlPath of URI specification:
	 *         https://tools.ietf.org/html/rfc3986#section-3.3
	 * 2. It also abides by:
	 *         https://download.oracle.com/otndocs/jcp/servlet-4-final-eval-spec/index.html#12.2
	 *
	 * @param urlPathPattern the given urlPathPattern
	 * @return a boolean value indicating if the urlPathPattern a valid wildCardPattern
	 */
	protected boolean isValidWildCardPattern(String urlPathPattern) {
		if ((urlPathPattern.length() < 2) ||
			(urlPathPattern.charAt(0) != '/') ||
			(urlPathPattern.charAt(urlPathPattern.length() - 1) != '*') ||
			(urlPathPattern.charAt(urlPathPattern.length() - 2) != '/')) {

			return false;
		}

		try {
			String urlPath = urlPathPattern.substring(
				0, urlPathPattern.length() - 1);

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

	/**
	 * Total number of printable ASCII characters
	 * Index from 32(space) to 127(delete)
	 */
	protected static final byte ASCII_CHARACTER_RANGE = 96;

	/**
	 * Total number of printable ASCII characters
	 * Index from 32(space) to 127(delete)
	 */
	protected static final byte ASCII_PRINTABLE_OFFSET = 32;

}