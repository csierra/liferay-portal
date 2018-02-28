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

package com.liferay.oauth2.provider.scope.spi.scope.matcher;

import aQute.bnd.annotation.ProviderType;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This interface represents the strategy used to match scope. Some of these
 * strategies may be:
 *
 * <ul>
 *     <li>NONE: no scope will match</li>
 *     <li>ALL: all scope will match</li>
 *     <li>STRICT: only scope matching a particular string or strings
 *     will match</li>
 *     <li>HIERARCHICAL: scope following some naming rules might match more
 *     general scope. For instance using <i>dot notation</i> we can code that
 *     shorter scope that share a common prefix, for example
 *     <i>everything</i> imply longer scope such as <i>everything.readonly</i>.
 *    </li>
 * </ul>
 *
 * ScopeMatcher might also be combined with {@link PrefixHandler} and
 * {@link ScopeMapper} to tailor the matching strategy to the framework
 * configuration.
 *
 * @author Carlos Sierra Andrés
 * @review
 */
@ProviderType
public interface ScopeMatcher {

	public static ScopeMatcher ALL = __ -> true;

	public static ScopeMatcher NONE = __ -> false;

	public default ScopeMatcher and(ScopeMatcher scopeMatcher) {
		return localName -> match(localName) && scopeMatcher.match(localName);
	}

	/**
	 * Applies the matcher to a collection of scope. Some implementations
	 * might have optimization opportunities.
	 *
	 * @param names the collection of scope to match.
	 * @return a collection containing those scope that matched.
	 * @review
	 */
	public default Collection<String> filter(Collection<String> names) {
		Stream<String> stream = names.stream();

		return stream.filter(
			this::match
		).collect(
			Collectors.toList()
		);
	}

	/**
	 * Specifies if a given scope matches according to the {@link ScopeMatcher}.
	 *
	 * @param name
	 * @return true if the input scope is a match for the {@link ScopeMatcher},
	 * false otherwise.
	 * @review
	 */
	public boolean match(String name);

	public default ScopeMatcher or(ScopeMatcher scopeMatcher) {
		return localName -> match(localName) || scopeMatcher.match(localName);
	}

}