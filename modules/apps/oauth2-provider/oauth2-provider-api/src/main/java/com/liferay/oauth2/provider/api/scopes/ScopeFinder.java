package com.liferay.oauth2.provider.api.scopes;

import java.util.Collection;

public interface ScopeFinder {

	public Collection<Class<? extends OAuth2Scopes>> findScopes(String name);

}
