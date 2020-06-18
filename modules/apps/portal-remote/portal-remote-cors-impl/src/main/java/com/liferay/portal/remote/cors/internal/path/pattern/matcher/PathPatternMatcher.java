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

import java.util.List;

/**
 * @author Arthur Chan
 */
public abstract class PathPatternMatcher<T> {

	/**
	 * https://download.oracle.com/otndocs/jcp/servlet-4-final-eval-spec/index.html#12.1
	 *
	 * Get the matching pattern of the given path, following the order of:
	 * 1. Exact matching pattern
	 * 2. Wild card matching the longest pattern
	 * 3. Extension pattern
	 * 4. Default pattern
	 *
	 * @param path a legal path from a URL
	 * @return the matched pattern
	 */
	public PatternTuple<T> getPatternTuple(String path) {
		PatternTuple<T> patternTuple = getExactPatternTuple(path);

		if (patternTuple != null) {
			return patternTuple;
		}

		patternTuple = getWildcardPatternTuple(path);

		if (patternTuple != null) {
			return patternTuple;
		}

		return getExtensionPatternTuple(path);
	}

	/**
	 * https://download.oracle.com/otndocs/jcp/servlet-4-final-eval-spec/index.html#12.1
	 *
	 * Get all matching patterns of the given path, including:
	 * 1. Exact matching pattern
	 * 2. Wild card matching patterns
	 * 3. Extension pattern
	 *
	 * @param path a legal path from a URL
	 * @return all the matched patterns
	 */
	public abstract List<PatternTuple<T>> getPatternTuples(String path);

	public abstract void insert(String urlPattern, T value)
		throws IllegalArgumentException;

	protected abstract PatternTuple<T> getExactPatternTuple(String path);

	protected abstract PatternTuple<T> getExtensionPatternTuple(String path);

	protected abstract PatternTuple<T> getWildcardPatternTuple(String path);

	/**
	 * A valid ExtensionPattern:
	 * 1. Without the leading '*', it abides by the format of a segment of path
	 *         of URI specification: https://tools.ietf.org/html/rfc3986#section-3.3
	 * 2. It also abides by:
	 *         https://download.oracle.com/otndocs/jcp/servlet-4-final-eval-spec/index.html#12.1.3, and
	 *         https://download.oracle.com/otndocs/jcp/servlet-4-final-eval-spec/index.html#12.2
	 *
	 * @param urlPattern the given urlPattern
	 * @return a boolean value indicating if the urlPattern a valid extensionPattern
	 */
	protected boolean isValidExtensionPattern(String urlPattern) {
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
	 * A valid WildCardPattern:
	 * 1. Without the trailing '*', it abides by the format of path of URI specification:
	 *         https://tools.ietf.org/html/rfc3986#section-3.3
	 * 2. It also abides by:
	 *         https://download.oracle.com/otndocs/jcp/servlet-4-final-eval-spec/index.html#12.2
	 *
	 * @param urlPattern the given urlPattern
	 * @return a boolean value indicating if the urlPattern a valid wildCardPattern
	 */
	protected boolean isValidWildCardPattern(String urlPattern) {
		if ((urlPattern.length() < 2) || (urlPattern.charAt(0) != '/') ||
			(urlPattern.charAt(urlPattern.length() - 1) != '*') ||
			(urlPattern.charAt(urlPattern.length() - 2) != '/')) {

			return false;
		}

		try {
			String path = urlPattern.substring(0, urlPattern.length() - 1);

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