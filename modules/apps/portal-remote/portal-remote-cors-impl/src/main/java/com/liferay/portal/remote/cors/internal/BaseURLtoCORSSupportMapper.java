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

/**
 * @author Arthur Chan
 */
public abstract class BaseURLtoCORSSupportMapper
	implements URLtoCORSSupportMapper {

	public static final byte ASCII_CHARACTER_RANGE = 96;

	public static final byte ASCII_PRINTABLE_OFFSET = 32;

	@Override
	public CORSSupport get(String urlPath) {
		CORSSupport corsSupport = getWildcardCORSSupport(urlPath);

		if (corsSupport != null) {
			return corsSupport;
		}

		return getExtensionCORSSupport(urlPath);
	}

	@Override
	public void put(String urlPattern, CORSSupport corsSupport)
		throws IllegalArgumentException {

		if (corsSupport == null) {
			throw new IllegalArgumentException("Value can not be null");
		}

		if (isWildcardURLPattern(urlPattern)) {
			put(urlPattern, corsSupport, true);

			return;
		}

		if (isExtensionURLPattern(urlPattern)) {
			put(urlPattern, corsSupport, false);

			return;
		}

		put(urlPattern, corsSupport, true);
	}

	protected static boolean isExtensionURLPattern(String urlPattern) {

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

	protected static boolean isWildcardURLPattern(String urlPattern) {

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

	protected abstract CORSSupport getExtensionCORSSupport(String urlPath);

	protected abstract CORSSupport getWildcardCORSSupport(String urlPath);

	protected abstract void put(
		String urlPattern, CORSSupport corsSupport, boolean forward);

}