package com.liferay.oauth2.provider.scopes.spi;

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
	public default ScopeMatcher withMapper(ScopeMatcher scopeMatcher) {
		return localName ->
			map(localName).stream().anyMatch(scopeMatcher::match);
	}
	
	public Set<String> map(String scope);

	public static ScopeMapper NULL = Collections::singleton;

}
