package com.liferay.oauth2.provider.api.scopes;

import com.liferay.oauth2.provider.api.scopes.exceptions.ScopeNotGrantedException;

import java.io.Serializable;

public interface ScopeChecker {

	public void requires(Serializable UUID) throws ScopeNotGrantedException;

	public boolean isGrantedScope(Serializable UUID);

}
