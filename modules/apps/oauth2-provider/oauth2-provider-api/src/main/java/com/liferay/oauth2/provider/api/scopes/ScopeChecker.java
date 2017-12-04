package com.liferay.oauth2.provider.api.scopes;

import com.liferay.oauth2.provider.api.scopes.exceptions.ScopeNotGrantedException;

public interface ScopeChecker {

	public void requires(Class<? extends OAuth2Scope.Scope> scope)
		throws ScopeNotGrantedException;

	public boolean isGrantedScope(Class<? extends OAuth2Scope.Scope> scope);

}
