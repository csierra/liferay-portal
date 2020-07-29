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

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;

/**
 * @author Arthur Chan
 */
public abstract class BasePathPatternMatcherTestCase {

	public void setUp() throws Exception {
		for (String pattern : PATTERNS) {
			urlPathPatternMatcher.putValue(pattern, pattern);
		}

		urlPathsValuesMap = new HashMap<>();

		for (int i = 0; i < PATHS.length; ++i) {
			if (MATCHED_PATTERNS_INDICES[i] > -1) {
				urlPathsValuesMap.put(
					PATHS[i], PATTERNS[MATCHED_PATTERNS_INDICES[i]]);
			}
		}
	}

	public void testGetValue() throws Exception {
		for (String urlPath : PATHS) {
			String expectedValue = urlPathsValuesMap.get(urlPath);

			if (urlPathPatternMatcher.getValue(urlPath) == null) {
				Assert.assertNull(expectedValue);

				continue;
			}

			Assert.assertEquals(
				expectedValue, urlPathPatternMatcher.getValue(urlPath));
		}
	}

	protected static final int[] MATCHED_PATTERNS_INDICES = {
		12, 13, 14, 15, 15, 16, 16, 17, 17, 15, 15, 18, 18, 19, 19, 20, 20, 21,
		21, 15, 15, 15, 15, 22, 22, 23, 23, 11, 11, 24, -1, 25, -1, -1, -1, 6,
		-1, -1, -1, 1
	};

	protected static final String[] PATHS = {
		"/", "/*", "/*/", "/documents", "/documents/", "/documents/user1",
		"/documents/user1/", "/documents/user2", "/documents/user2/",
		"/documents/user3", "/documents/user3/", "/documents/user1/folder1",
		"/documents/user1/folder1/", "/documents/user1/folder2",
		"/documents/user1/folder2/", "/documents/user2/folder1",
		"/documents/user2/folder1/", "/documents/user2/folder2",
		"/documents/user2/folder2/", "/documents/user3/folder1",
		"/documents/user3/folder1/", "/documents/user3/folder2",
		"/documents/user3/folder2/", "/documents/main.jspf",
		"/documents/main.jspf/", "/documents/main.jsp", "/documents/main.jsp/*",
		"/test", "/test/", "test/main.jspf", "test.jspf/", "test.jsp",
		"test/main.jsp/*", "test", "test/", "no/leading/slash/*",
		"no/leading/slash/test", "no/leading/slash", "no/leading/slash/",
		"/c/portal/j_login"
	};

	protected static final String[] PATTERNS = {
		"/c/portal/j_login*", "/c/portal/j_login", "/c/portal/j_login*/",
		"/c/*/j_login", "/**", "/**/", "no/leading/slash/*",
		"no/leading/slash/*/", "no/leading/slash/*/*", "no/leading/slash/*//*",
		"no/leading/slash//*", "/*", "//*", "/*/*", "/*//*", "/documents/*",
		"/documents/user1/*", "/documents/user2/*",
		"/documents/user1/folder1/*", "/documents/user1/folder2/*",
		"/documents/user2/folder1/*", "/documents/user2/folder2/*",
		"/documents/main.jspf/*", "/documents/main.jsp/*", "*.jspf", "*.jsp",
		"/c/portal/j_login/*"
	};

	protected URLPathPatternMatcher<String> urlPathPatternMatcher;
	protected Map<String, String> urlPathsValuesMap;

}