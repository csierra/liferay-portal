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
import com.liferay.oauth2.provider.scopes.prefixhandler.PrefixHandler;
import com.liferay.oauth2.provider.scopes.scopematcher.ScopeMatcher;
import com.liferay.oauth2.provider.scopes.spi.ScopeFinder;
import com.liferay.oauth2.provider.scopes.spi.ScopeMapper;
import com.liferay.oauth2.provider.scopes.spi.ScopeMatcherFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ScopeMatcherTest {

	@Test
	public void testFindScopes() {
		ScopeFinder testScopeFinder = new TestHierarchyScopeFinder();

		assertEquals(
			Arrays.asList("RO"), testScopeFinder.findScopes("RO"::equals));

		assertEquals(
			Arrays.asList("RO", "RW"),
			testScopeFinder.findScopes("RW"::equals));
	}

	@Test
	public void testFindScopesWithMapper() {
		ScopeFinder testScopeFinder = new TestHierarchyScopeFinder();

		ScopeMatcher scopeMatcher = ScopeMatcherFactory.STRICT.create("RO2");

		TestScopeMapper testScopeMapper1 = new TestScopeMapper();
		
		scopeMatcher = testScopeMapper1.applyTo(scopeMatcher);

		assertEquals(
			Arrays.asList("RO"), testScopeFinder.findScopes(scopeMatcher));

		scopeMatcher = ScopeMatcherFactory.STRICT.create("RW2");

		TestScopeMapper testScopeMapper2 = new TestScopeMapper();
		
		scopeMatcher = testScopeMapper2.applyTo(scopeMatcher);

		assertEquals(
			Arrays.asList("RO", "RW"),
			testScopeFinder.findScopes(scopeMatcher));
	}

	@Test
	public void testFindScopesWithNamespace() {
		ScopeFinder testScopeFinder = new TestHierarchyScopeFinder();

		PrefixHandler namespaceAdder = (target) -> "TEST/" + target;
		
		ScopeMatcher scopeMatcher = "TEST/RO"::equals;

		assertEquals(
			Arrays.asList("RO"),
			new ArrayList<>(
				testScopeFinder.findScopes(
					namespaceAdder.applyTo(scopeMatcher))));

		scopeMatcher = "TEST/RW"::equals;

		assertEquals(
			Arrays.asList("RO", "RW"),
			new ArrayList<>(
				testScopeFinder.findScopes(
					namespaceAdder.applyTo(scopeMatcher))));
	}

	@Test
	public void testFindScopesWithMultipleNamespaces() {
		ScopeFinder testScopeFinder = new TestHierarchyScopeFinder();

		PrefixHandler namespaceAdder = (target) -> "TEST/" + target;
		
		PrefixHandler nestedNamespaceAdder = (target) -> "NESTED/" + target;

		namespaceAdder = namespaceAdder.append(nestedNamespaceAdder);

		ScopeMatcher scopeMatcher = "TEST/NESTED/RO"::equals;

		assertEquals(
			Arrays.asList("RO"),
			testScopeFinder.findScopes(namespaceAdder.applyTo(scopeMatcher)));

		scopeMatcher = "TEST/NESTED/RW"::equals;

		assertEquals(
			Arrays.asList("RO", "RW"),
			testScopeFinder.findScopes(namespaceAdder.applyTo(scopeMatcher)));
	}

	@Test
	public void testFindScopesWithPrefixMatcher() {
		ScopeFinder scopeFinder = new TestTestAllScopeFinder(
			"everything", "everything.readonly");

		assertEquals(
			Arrays.asList("everything", "everything.readonly"),
			scopeFinder.findScopes(s -> s.startsWith("everything")));

		assertEquals(
			Arrays.asList("everything.readonly"),
			scopeFinder.findScopes(s -> s.startsWith("everything.readonly")));
	}

	@Test
	public void testFindScopesWithPrefixMatcherAndNamespace() {
		ScopeFinder scopeFinder = new TestTestAllScopeFinder(
			"everything", "everything.readonly");

		PrefixHandler liferayNamespaceAdder = (target) -> "http://www.liferay.com/" + target;
				
		PrefixHandler apioNamespaceAdder = (target) -> "apio/" + target;
		
		PrefixHandler namespaceAdder =
			liferayNamespaceAdder.append(apioNamespaceAdder);

		ScopeMatcher scopeMatcher = new ChunkScopeMatcherFactory().create(
			"http://www.liferay.com/apio/everything");

		assertEquals(
			Arrays.asList("everything", "everything.readonly"),
			scopeFinder.findScopes(namespaceAdder.applyTo(scopeMatcher)));

		scopeMatcher = new ChunkScopeMatcherFactory().create(
			"http://www.liferay.com/apio/everything.readonly");

		assertEquals(
			Arrays.asList("everything.readonly"),
			scopeFinder.findScopes(namespaceAdder.applyTo(scopeMatcher)));
	}

	@Test
	public void testScopeFinderReturnAll() {
		String[] strings =
			{"everything", "everything.readonly", "another"};

		ScopeFinder scopeFinder = new TestTestAllScopeFinder(strings);

		Collection<String> oAuth2Grants = scopeFinder.findScopes(__ -> true);

		List<String> scopes = Arrays.asList(strings);

		assertTrue(scopes.containsAll(oAuth2Grants));
	}

	public static class TestHierarchyScopeFinder implements ScopeFinder {

		@Override
		public Collection<String> findScopes(ScopeMatcher scopeMatcher) {
			Collection<String> strings = new TreeSet<>();

			if (scopeMatcher.match("RO")) {
				strings.add("RO");
			}
			if (scopeMatcher.match("RW")) {
				strings.addAll(Arrays.asList("RO", "RW"));
			}

			return new ArrayList<>(strings);
		}
	}

	public static class TestTestAllScopeFinder implements ScopeFinder {

		private final Set<String> _scopes;

		public TestTestAllScopeFinder(String ... scopes) {
			_scopes = new TreeSet<>(Arrays.asList(scopes));
		}

		@Override
		public Collection<String> findScopes(ScopeMatcher scopeMatcher) {
			return _scopes.stream().filter(
				scopeMatcher::match
			).collect(
				Collectors.toList()
			);
		}
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
