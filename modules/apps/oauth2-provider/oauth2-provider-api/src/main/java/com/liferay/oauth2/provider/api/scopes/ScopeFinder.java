package com.liferay.oauth2.provider.api.scopes;

import java.util.Collection;

@FunctionalInterface
public interface ScopeFinder {

	public Collection<OAuth2Scope> findScopes(String name);

}
