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
	 * Get the matching pattern of the given urlPath, following the order of:
	 * 1. Exact matching pattern
	 * 2. Wild card matching the longest pattern
	 * 3. Extension pattern
	 * 4. Default pattern
	 *
	 * @param urlPath a legal urlPath from a URL
	 * @return the matched pattern
	 */
	public PatternTuple<T> getPatternPackage(String urlPath) {
		PatternTuple<T> patternTuple = getExactPatternPackage(urlPath);

		if (patternTuple != null) {
			return patternTuple;
		}

		patternTuple = getWildcardPatternPackage(urlPath);

		if (patternTuple != null) {
			return patternTuple;
		}

		return getExtensionPatternPackage(urlPath);
	}

	/**
	 * https://download.oracle.com/otndocs/jcp/servlet-4-final-eval-spec/index.html#12.1
	 *
	 * Get all matching patterns of the given urlPath, including:
	 * 1. Exact matching pattern
	 * 2. Wild card matching patterns
	 * 3. Extension pattern
	 *
	 * @param urlPath a legal urlPath from a URL
	 * @return all the matched patterns
	 */
	public List<PatternTuple<T>> getPatternPackages(String urlPath) {
		List<PatternTuple<T>> patternTuples = getWildcardPatternPackages(
			urlPath);

		PatternTuple<T> patternTuple = getExactPatternPackage(urlPath);

		if (patternTuple != null) {
			patternTuples.add(patternTuple);
		}

		patternTuple = getExtensionPatternPackage(urlPath);

		if (patternTuple != null) {
			patternTuples.add(patternTuple);
		}

		return patternTuples;
	}

	public abstract void insert(String urlPattern, T cargo)
		throws IllegalArgumentException;

	protected abstract PatternTuple<T> getExactPatternPackage(String urlPath);

	protected abstract PatternTuple<T> getExtensionPatternPackage(
		String urlPath);

	protected abstract PatternTuple<T> getWildcardPatternPackage(
		String urlPath);

	protected abstract List<PatternTuple<T>> getWildcardPatternPackages(
		String urlPath);

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
	protected static final byte CHARACTER_RANGE = 96;

	/**
	 * Total number of printable ASCII characters
	 * Index from 32(space) to 127(delete)
	 */
	protected static final byte PRINTABLE_OFFSET = 32;

}