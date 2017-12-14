package com.liferay.oauth2.provider.api.scopes;

import com.liferay.oauth2.provider.api.scopes.exceptions.ScopeNotGrantedException;

public interface ScopeChecker {

	public default void requireScope(String scope)
		throws ScopeNotGrantedException {

		if (!hasScope(scope)) {
			throw new ScopeNotGrantedException(scope);
		}
	}

	public boolean hasScope(String scope);

}
