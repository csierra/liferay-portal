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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arthur Chan
 */
public abstract class PathPatternMatcher<T> {

	public abstract void clear();

	public abstract List<T> getCargoList(String urlPattern);

	public abstract List<List<T>> getCargoLists(String urlPattern);

	public abstract String getPattern(String urlPattern);

	public abstract List<String> getPatterns(String urlPattern);

	public void insert(String urlPattern, T cargo)
		throws IllegalArgumentException {

		if (cargo == null) {
			throw new IllegalArgumentException("cargo cannot be null");
		}

		// Special path pattern 1

		if (urlPattern.length() < 1) {
			contextRoot = true;
		}

		// Special path pattern 2

		else if ((urlPattern.length() == 1) && (urlPattern.charAt(0) == '/')) {
			defaultServlet = true;
		}
	}

	public boolean isContextRoot(String urlPath) {
		if (contextRoot & urlPath.equals("/")) {
			return true;
		}

		return false;
	}

	public boolean isDefautServlet(String urlPath) {
		if (defaultServlet & urlPath.equals("")) {
			return true;
		}

		return false;
	}

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

	protected boolean contextRoot;
	protected boolean defaultServlet;

	protected static class PatternPackage<T> {

		public PatternPackage() {
			_cargoList = new ArrayList<>(_BIG_ENOUGH);
		}

		public void clear() {
			_pattern = null;
			_cargoList.clear();
		}

		public List<T> getCargoList() {
			return _cargoList;
		}

		public String getPattern() {
			return _pattern;
		}

		public boolean isEmpty() {
			if (_pattern == null) {
				return true;
			}

			return false;
		}

		public void set(String pattern, T cargo) {
			if (_pattern == null) {
				_pattern = pattern;
			}

			_cargoList.add(cargo);
		}

		private static final byte _BIG_ENOUGH = 32;

		private final List<T> _cargoList;
		private String _pattern;

	}

}