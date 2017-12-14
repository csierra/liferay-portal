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

package com.liferay.oauth2.provider.impl.scopes;

import com.liferay.oauth2.provider.api.scopes.NamespaceAdderFactory;
import org.junit.Test;

import java.util.Collections;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class ThreadLocalScopeCheckerTest {

	@Test
	public void testHasScopeNoNamespace() {
		ThreadLocalScopeChecker scopeChecker = new ThreadLocalScopeChecker();

		scopeChecker.setAllowedScopes(
			Collections.singleton(new TestOauth2Scope("READ")));

		assertTrue(scopeChecker.hasScope("READ"));
	}

	@Test
	public void testHasScopeWithNamespace() {
		ThreadLocalScopeChecker scopeChecker = new ThreadLocalScopeChecker();
		NamespaceAdderFactory namespaceAdderFactory =
			new DefaultNamespaceAdderFactory();

		scopeChecker.pushNamespace(namespaceAdderFactory.create("APP"));

		scopeChecker.setAllowedScopes(
			Collections.singleton(new TestOauth2Scope("READ")));

		assertFalse(scopeChecker.hasScope("READ"));
		assertTrue(scopeChecker.hasScope("APP_READ"));
	}

	@Test
	public void testHasScopeWithNamespaces() {
		ThreadLocalScopeChecker scopeChecker = new ThreadLocalScopeChecker();
		NamespaceAdderFactory namespaceAdderFactory =
			new DefaultNamespaceAdderFactory();

		scopeChecker.pushNamespace(namespaceAdderFactory.create("MY"));
		scopeChecker.pushNamespace(namespaceAdderFactory.create("APP"));

		scopeChecker.setAllowedScopes(
			Collections.singleton(new TestOauth2Scope("READ")));

		assertFalse(scopeChecker.hasScope("READ"));
		assertFalse(scopeChecker.hasScope("APP_READ"));
		assertTrue(scopeChecker.hasScope("MY_APP_READ"));
	}

	@Test
	public void testHasScopeWithNamespacesWhenPushedAndPoped() {
		ThreadLocalScopeChecker scopeChecker = new ThreadLocalScopeChecker();
		NamespaceAdderFactory namespaceAdderFactory =
			new DefaultNamespaceAdderFactory();

		scopeChecker.pushNamespace(namespaceAdderFactory.create("MY"));
		scopeChecker.pushNamespace(namespaceAdderFactory.create("APP"));

		scopeChecker.setAllowedScopes(
			Collections.singleton(new TestOauth2Scope("READ")));

		assertFalse(scopeChecker.hasScope("READ"));
		assertFalse(scopeChecker.hasScope("APP_READ"));
		assertTrue(scopeChecker.hasScope("MY_APP_READ"));

		scopeChecker.popNamespace();

		assertFalse(scopeChecker.hasScope("READ"));
		assertFalse(scopeChecker.hasScope("APP_READ"));
		assertFalse(scopeChecker.hasScope("MY_APP_READ"));
		assertTrue(scopeChecker.hasScope("MY_READ"));

		scopeChecker.popNamespace();

		assertFalse(scopeChecker.hasScope("APP_READ"));
		assertFalse(scopeChecker.hasScope("MY_APP_READ"));
		assertFalse(scopeChecker.hasScope("MY_READ"));
		assertTrue(scopeChecker.hasScope("READ"));

	}

}
