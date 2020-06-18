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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Arthur Chan
 */
@PrepareForTest(StaticPathPatternMatcher.class)
@RunWith(PowerMockRunner.class)
public class StaticPathPatternMatcherTest
	extends BasePathPatternMatcherTestCase {

	@Before
	public void setUp() throws Exception {
		matcher = new StaticPathPatternMatcher<>(64);

		super.setUp();
	}

	@Test
	public void testAllMatches() throws Exception {
		super.testAllMatches();
	}

	@Test
	public void testBestMatch() throws Exception {
		super.testBestMatch();
	}

}