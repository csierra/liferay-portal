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

package com.liferay.oauth2.provider.service.impl;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.not;

import com.liferay.oauth2.provider.service.OAuth2Scope;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.HashMapBuilder;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.apache.commons.compress.utils.Sets;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Stian Sigvartsen
 */
@RunWith(PowerMockRunner.class)
public class OAuth2ScopeBuilderTest extends PowerMockito {

	@Test
	public void testApplicationIsolation() {
		String applicationName1 = "Test.Application1";
		String applicationName2 = "Test.Application2";

		String[] scopes = {"everything.read", "everything.write"};

		String scopeAlias = "everything";

		BiFunction<String, String, String> scopeAliasFunction =
			(applicationName, scope) ->
				applicationName + StringPool.PERIOD + scope;

		Map<AbstractMap.SimpleEntry<String, String>, Set<String>>
			simpeEntryScopeAliases = _exerciseBuilder(
				builder -> {
					builder.forApplication(
						applicationName1,
						applicationScopeAssigner ->
							applicationScopeAssigner.assignScope(
								scopes
							).mapToScopeAlias(
								scopeAliasFunction.apply(
									applicationName1, scopeAlias)
							));

					builder.forApplication(
						applicationName2,
						applicationScopeAssigner ->
							applicationScopeAssigner.assignScope(
								scopes
							).mapToScopeAlias(
								scopeAliasFunction.apply(
									applicationName2, scopeAlias)
							));
				});

		simpeEntryScopeAliases.forEach(
			(key, value) -> {
				Assert.assertThat(value, not(hasItems(scopes)));
				Assert.assertEquals(1, value.size());
				Assert.assertThat(
					value,
					hasItems(
						scopeAliasFunction.apply(key.getKey(), scopeAlias)));
			});
	}

	@Test
	public void testApplicationIsolationWithScopeAliases() {
		String[] scopes = {"everything.read", "everything.write"};

		Map<String, Set<String>> applicationScopeAlias =
			HashMapBuilder.<String, Set<String>>put(
				"Test.Application1", Sets.newHashSet("application1.everything")
			).put(
				"Test.Application2", Sets.newHashSet("application2.everything")
			).build();

		Map<AbstractMap.SimpleEntry<String, String>, Set<String>>
			simpeEntryScopeAliases = _exerciseBuilder(
				builder -> applicationScopeAlias.forEach(
					(applicationName, scopeAliases) -> builder.forApplication(
						applicationName,
						applicationScopeAssigner ->
							applicationScopeAssigner.assignScope(
								scopes
							).mapToScopeAlias(
								scopeAliases
							))));

		simpeEntryScopeAliases.forEach(
			(key, value) -> {
				Assert.assertThat(value, not(hasItems(scopes)));
				Assert.assertEquals(
					value, applicationScopeAlias.get(key.getKey()));
			});
	}

	@Test
	public void testMapToScopeAlias() {
		String applicationName = "Test.Application";

		String[] scopes = {"everything.read", "everything.write"};

		String scopeAlias = "everything";

		Map<AbstractMap.SimpleEntry<String, String>, Set<String>>
			simpeEntryScopeAliases = _exerciseBuilder(
				builder -> builder.forApplication(
					applicationName,
					applicationScopeAssigner ->
						applicationScopeAssigner.assignScope(
							scopes
						).mapToScopeAlias(
							scopeAlias
						)));

		simpeEntryScopeAliases.forEach(
			(key, value) -> {
				Assert.assertThat(value, not(hasItems(scopes)));
				Assert.assertEquals(1, value.size());
				Assert.assertThat(value, hasItems(scopeAlias));
			});
	}

	@Test
	public void testMultipleSeparateAssignmentsToSameScopeAlias() {
		String applicationName = "Test.Application";

		Set<String> scopes = Sets.newHashSet(
			"everything.read", "everything.write");

		Collection<String> scopeAlias = Collections.singleton("everything");

		Map<AbstractMap.SimpleEntry<String, String>, Set<String>>
			simpeEntryScopeAliases = _exerciseBuilder(
				builder -> builder.forApplication(
					applicationName,
					applicationScopeAssigner -> {
						applicationScopeAssigner.assignScope(
							"everything.read"
						).mapToScopeAlias(
							scopeAlias
						);

						applicationScopeAssigner.assignScope(
							"everything.write"
						).mapToScopeAlias(
							scopeAlias
						);
					}));

		Assert.assertEquals(
			simpeEntryScopeAliases.toString(), 2,
			simpeEntryScopeAliases.size());

		simpeEntryScopeAliases.forEach(
			(key, value) -> {
				Assert.assertEquals(scopeAlias, value);

				scopes.remove(key.getValue());
			});

		Assert.assertEquals(scopes.toString(), 0, scopes.size());
	}

	@Test
	public void testNoSpecifiedScopeAlias() {
		String applicationName = "Test.Application";

		String[] scopesArray = {"everything.read", "everything.write"};

		Set<String> scopes = Sets.newHashSet(scopesArray);

		// Test assigning scopes using Collection

		Map<AbstractMap.SimpleEntry<String, String>, Set<String>>
			simpeEntryScopeAliases1 = _exerciseBuilder(
				builder -> builder.forApplication(
					applicationName,
					applicationScopeAssigner ->
						applicationScopeAssigner.assignScope(scopes)));

		// Test that the resulting scope aliases all map to all scopes

		simpeEntryScopeAliases1.forEach(
			(key, value) -> Assert.assertEquals(value, scopes));

		// Repeat using VarArgs

		Map<AbstractMap.SimpleEntry<String, String>, Set<String>>
			simpeEntryScopeAliases2 = _exerciseBuilder(
				builder -> builder.forApplication(
					applicationName,
					applicationScopeAssigner ->
						applicationScopeAssigner.assignScope(scopesArray)));

		// Assert the result is identical

		Assert.assertEquals(
			simpeEntryScopeAliases2.toString(), simpeEntryScopeAliases1.size(),
			simpeEntryScopeAliases2.size());

		Set<Map.Entry<AbstractMap.SimpleEntry<String, String>, Set<String>>>
			entrySet = simpeEntryScopeAliases1.entrySet();

		Stream<Map.Entry<AbstractMap.SimpleEntry<String, String>, Set<String>>>
			stream = entrySet.stream();

		Assert.assertTrue(
			stream.allMatch(
				entry -> Objects.equals(
					entry.getValue(),
					simpeEntryScopeAliases2.get(entry.getKey()))));

		// Test separate calls result in each scope mapping to a different scope
		// alias

		simpeEntryScopeAliases1 = _exerciseBuilder(
			builder -> builder.forApplication(
				applicationName,
				applicationScopeAssigner -> {
					applicationScopeAssigner.assignScope(scopesArray[0]);
					applicationScopeAssigner.assignScope(scopesArray[1]);
				}));

		simpeEntryScopeAliases1.forEach(
			(key, value) -> Assert.assertEquals(
				Collections.singleton(key.getValue()), value));
	}

	private Map<AbstractMap.SimpleEntry<String, String>, Set<String>>
		_exerciseBuilder(Consumer<OAuth2Scope.Builder> builderConsumer) {

		Map<AbstractMap.SimpleEntry<String, String>, List<String>>
			simpleEntryScopeAliases = new HashMap<>();

		OAuth2Scope.Builder builder =
			new OAuth2ApplicationScopeAliasesLocalServiceImpl.
				OAuth2ScopeBuilderImpl(simpleEntryScopeAliases);

		builderConsumer.accept(builder);

		Map<AbstractMap.SimpleEntry<String, String>, Set<String>>
			simpleEntryScopeAliasesSet = new HashMap<>();

		simpleEntryScopeAliases.forEach(
			(key, value) -> simpleEntryScopeAliasesSet.put(
				key, new HashSet<>(value)));

		return simpleEntryScopeAliasesSet;
	}

}