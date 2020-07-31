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

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Arthur Chan
 */
public class URLPatternMatcherTest {

	@Test
	public void testURLPatternType() throws Exception {
		Set<String> wildcardPatterns = new HashSet<>();
		Set<String> extensionPatterns = new HashSet<>();
		Set<String> exactPatterns = new HashSet<>();

		for (String pattern : PATTERNS) {
			if (URLPatternMatcher.isWildcardPattern(pattern)) {
				wildcardPatterns.add(pattern);
			}
			else if (URLPatternMatcher.isExtensionPattern(pattern)) {
				extensionPatterns.add(pattern);
			}
			else {
				exactPatterns.add(pattern);
			}
		}

		for (int index : PATTERNS_INDEXES_EXACT) {
			Assert.assertTrue(exactPatterns.remove(PATTERNS[index]));
		}

		Assert.assertTrue(exactPatterns.isEmpty());

		for (int index : PATTERNS_INDEXES_WILDCARD) {
			Assert.assertTrue(wildcardPatterns.remove(PATTERNS[index]));
		}

		Assert.assertTrue(wildcardPatterns.isEmpty());

		for (int index : PATTERNS_INDEXES_EXTENSION) {
			Assert.assertTrue(extensionPatterns.remove(PATTERNS[index]));
		}

		Assert.assertTrue(extensionPatterns.isEmpty());
	}

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

	protected static final int[] PATTERNS_INDEXES_EXACT = {
		0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
	};

	protected static final int[] PATTERNS_INDEXES_EXTENSION = {24, 25};

	protected static final int[] PATTERNS_INDEXES_WILDCARD = {
		11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 26
	};

}