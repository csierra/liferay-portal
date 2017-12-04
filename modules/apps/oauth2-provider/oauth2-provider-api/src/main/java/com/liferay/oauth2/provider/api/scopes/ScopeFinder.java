package com.liferay.oauth2.provider.api.scopes;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

@FunctionalInterface
public interface ScopeFinder {

	public Collection<OAuth2Scope> findScopes(String name);

	default ScopeFinder merge(ScopeFinder other) {

		return name -> {
			Collection<OAuth2Scope> scopes = new HashSet<>();

			scopes.addAll(findScopes(name));

			scopes.addAll(other.findScopes(name));

			return scopes;
		};
	}

	static ScopeFinder merge(Collection<ScopeFinder> scopeFinders) {
		return scopeFinders.stream().collect(
			ScopeFinder::empty, ScopeFinder::merge, ScopeFinder::merge);
	}

	static ScopeFinder empty() {
		return __ -> Collections.emptySet();
	}

}
