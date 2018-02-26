/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.oauth2.provider.scope.impl;

import com.liferay.oauth2.provider.scope.impl.scopematcher.StrictScopeMatcherFactory;
import com.liferay.oauth2.provider.scope.spi.scopemapper.ScopeMapper;
import com.liferay.oauth2.provider.scope.spi.scopematcher.ScopeMatcher;
import com.liferay.oauth2.provider.scope.spi.scopematcher.ScopeMatcherFactory;
import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class ScopeMatcherTest {

	@Test
	public void testFindScopesWithMapper() {
		ScopeMatcherFactory strictScopeMatcherFactory
			= new StrictScopeMatcherFactory();

		ScopeMatcher scopeMatcher = strictScopeMatcherFactory.create("RO2");

		TestScopeMapper testScopeMapper1 = new TestScopeMapper();
		
		scopeMatcher = testScopeMapper1.applyTo(scopeMatcher);

		assertTrue(scopeMatcher.match("RO"));

		scopeMatcher = strictScopeMatcherFactory.create("RW2");

		TestScopeMapper testScopeMapper2 = new TestScopeMapper();
		
		scopeMatcher = testScopeMapper2.applyTo(scopeMatcher);

		assertTrue(scopeMatcher.match("RW"));
	}


	private static class TestScopeMapper implements ScopeMapper {

		@Override
		public Set<String> map(String s) {
			switch (s) {
				case "RW":
					return Collections.singleton("RW2");
				case "RO":
					return Collections.singleton("RO2");
			}

			return Collections.singleton(s);
		}
	}
}
