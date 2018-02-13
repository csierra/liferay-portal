package com.liferay.oauth2.provider.scopes.spi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * This interface represents the strategy used to match scopes. Some of these
 * strategies may be:
 *
 * <ul>
 *     <li>NONE: no scope will match</li>
 *     <li>ALL: all scopes will match</li>
 *     <li>STRICT: only scopes matching a particular string or strings
 *     will match</li>
 *     <li>HIERARCHICAL: scopes following some naming rules might match more
 *     general scopes. For instance using <i>dot notation</i> we can code that
 *     shorter scopes that share a common prefix, for example
 *     <i>everything</i> imply longer scopes such as <i>everything.readonly</i>.
 *	   </li>
 * </ul>
 *
 * ScopeMatcher might also be combined with {@link NamespaceApplicator} and
 * {@link ScopeMapper} to tailor the matching strategy to the framework
 * configuration.
 *
 * {@link ScopeFinder} implementations SHOULD take this into account to support
 * as many strategies as possible.
 */

public interface ScopeMatcher {

	/**
	 * Specifies if a given scope matches according to the {@link ScopeMatcher}.
	 *
	 * @param name
	 * @return true if the input scope is a match for the {@link ScopeMatcher},
	 * false otherwise.
	 */
	public boolean match(String scopesAlias);

	/**
	 * Applies the matcher to a collection of scopes. Some implementations
	 * might have optimization opportunities.
	 *
	 * @param names the collection of scopes to match.
	 * @return a collection containing those scopes that matched.
	 */
	public default Collection<String> filter(Collection<String> scopesAliases) {
		return scopesAliases.stream().filter(this::match).collect(Collectors.toList());
	}

	/**
	 * Returns a new {@link ScopeMatcher} that takes into account the effect
	 * of the given {@link NamespaceApplicator}. Some implementations might have
	 * optimization opportunities.
	 *
	 * @param namespaceApplicator the prefix handler that will affect the scope
	 * matcher.
	 * @return the new ScopeMatcher that takes into account the
	 * {@link NamespaceApplicator}
	 */
	public default ScopeMatcher withNamespaceApplicator(NamespaceApplicator namespaceApplicator) {
		return scopesAlias -> match(namespaceApplicator.applyNamespace(scopesAlias));
	}

	/**
	 * Returns a new {@link ScopeMatcher} that takes into account the effect
	 * of the given {@link ScopeMapper}. Some implementations might have
	 * optimization opportunities.
	 * @param scopeMapper the scope mapper that affects the scope matcher.
	 * @return the new {@link ScopeMatcher} that takes into account the given
	 * {@link ScopeMapper}.
	 */
	public default ScopeMatcher withScopeMapper(ScopeMapper scopeMapper) {
		return localName ->
			scopeMapper.map(localName).stream().anyMatch(this::match);
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
