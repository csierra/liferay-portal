package com.liferay.oauth2.provider.scope.spi;

import com.liferay.oauth2.provider.scope.spi.model.ScopeMatcher;

import java.util.Collections;
import java.util.Set;

public interface ScopeMapper {
	
	/**
	 * Returns a new {@link ScopeMatcher} that takes into account the effect
	 * of the given {@link ScopeMapper}. Some implementations might have
	 * optimization opportunities.
	 * @param scopeMatcher the scope matcher that should take into account this 
	 * scope mapper.
	 * @return the new {@link ScopeMatcher} that takes into account the given
	 * {@link ScopeMapper}.
	 */
	public default ScopeMatcher applyTo(ScopeMatcher scopeMatcher) {
		return localName ->
			map(localName).stream().anyMatch(scopeMatcher::match);
	}

	/**
	 * Renames an application provided scope to new scope names
	 *
	 * @param scope application provided scope
	 * @return set of new names for the scope
	 */
	public Set<String> map(String scope);

	public static final ScopeMapper PASSTHROUGH_SCOPEMAPPER =
		Collections::singleton;

}
