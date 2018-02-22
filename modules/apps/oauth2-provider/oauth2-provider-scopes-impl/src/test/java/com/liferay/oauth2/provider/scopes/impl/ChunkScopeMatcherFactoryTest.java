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

package com.liferay.oauth2.provider.scopes.impl;

import com.liferay.oauth2.provider.scopes.impl.scopematcher.ChunkScopeMatcherFactory;
import com.liferay.oauth2.provider.scopes.spi.model.PrefixHandler;
import com.liferay.oauth2.provider.scopes.spi.model.ScopeMatcher;
import com.liferay.oauth2.provider.scopes.spi.ScopeMapper;
import com.liferay.oauth2.provider.scopes.spi.ScopeMatcherFactory;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ChunkScopeMatcherFactoryTest {

	@Test
	public void testMatch() throws Exception {
		ScopeMatcherFactory chunkScopeMatcherFactory =
			new ChunkScopeMatcherFactory();

		ScopeMatcher scopeMatcher =
			chunkScopeMatcherFactory.create("everything.readonly");

		assertTrue(scopeMatcher.match("everything.readonly"));
		assertFalse(scopeMatcher.match("everything"));
	}

	@Test
	public void testMatch2() throws Exception {
		ScopeMatcherFactory chunkScopeMatcherFactory =
			new ChunkScopeMatcherFactory();

		ScopeMatcher scopeMatcher =
			chunkScopeMatcherFactory.create("everything");

		assertTrue(scopeMatcher.match("everything.readonly"));
		assertTrue(scopeMatcher.match("everything"));
	}

	@Test
	public void testMatchMatchesWholeChunksOnly() throws Exception {
		ScopeMatcherFactory chunkScopeMatcherFactory =
			new ChunkScopeMatcherFactory();

		ScopeMatcher scopeMatcher =
			chunkScopeMatcherFactory.create("everything");

		assertFalse(scopeMatcher.match("everything2.readonly"));
		assertFalse(scopeMatcher.match("everything2"));
	}


}
