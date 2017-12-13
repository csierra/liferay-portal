package com.liferay.oauth2.provider.api.scopes;

import java.util.stream.Stream;

@FunctionalInterface
public interface ScopeFinder {

	public Stream<OAuth2Scope> findScopes();

	default ScopeFinder merge(ScopeFinder other) {
		return () -> Stream.concat(findScopes(), other.findScopes());
	}

	static ScopeFinder empty() {
		return Stream::empty;
	}

}
