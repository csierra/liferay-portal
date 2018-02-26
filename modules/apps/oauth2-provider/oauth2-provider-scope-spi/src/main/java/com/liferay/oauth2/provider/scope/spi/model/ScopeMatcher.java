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

package com.liferay.oauth2.provider.scope.spi.model;

import java.util.Collection;
import java.util.stream.Collectors;

import aQute.bnd.annotation.ProviderType;
import com.liferay.oauth2.provider.scope.spi.ScopeFinder;
import com.liferay.oauth2.provider.scope.spi.ScopeMapper;

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
 *	   </li>
 * </ul>
 *
 * ScopeMatcher might also be combined with {@link PrefixHandler} and
 * {@link ScopeMapper} to tailor the matching strategy to the framework
 * configuration.
 */

@ProviderType
public interface ScopeMatcher {

	/**
	 * Specifies if a given scope matches according to the {@link ScopeMatcher}.
	 *
	 * @param name
	 * @return true if the input scope is a match for the {@link ScopeMatcher},
	 * false otherwise.
	 */
	public boolean match(String name);

	/**
	 * Applies the matcher to a collection of scope. Some implementations
	 * might have optimization opportunities.
	 *
	 * @param names the collection of scope to match.
	 * @return a collection containing those scope that matched.
	 */
	public default Collection<String> filter(Collection<String> names) {
		return names.stream().filter(this::match).collect(Collectors.toList());
	}

	public default ScopeMatcher and(ScopeMatcher scopeMatcher) {
		return localName ->
			match(localName) && scopeMatcher.match(localName);
	}

	public default ScopeMatcher or(ScopeMatcher scopeMatcher) {
		return localName ->
			match(localName) || scopeMatcher.match(localName);
	}

	public static ScopeMatcher ALL = __ -> true;

	public static ScopeMatcher NONE = __ -> false;

}
