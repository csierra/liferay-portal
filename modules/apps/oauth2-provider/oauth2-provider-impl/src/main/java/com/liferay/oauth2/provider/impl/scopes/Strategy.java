package com.liferay.oauth2.provider.impl.scopes;

import com.liferay.oauth2.provider.api.scopes.ScopeMatcher;

public interface Strategy {

	public ScopeMatcher matches(String scopeIdentifier);

}
