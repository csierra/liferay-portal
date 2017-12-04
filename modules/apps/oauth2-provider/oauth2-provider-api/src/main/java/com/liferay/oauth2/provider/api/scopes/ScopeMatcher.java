package com.liferay.oauth2.provider.api.scopes;

public interface ScopeMatcher {

	boolean matches(Class<? extends OAuth2Scope> scopeType);

}
