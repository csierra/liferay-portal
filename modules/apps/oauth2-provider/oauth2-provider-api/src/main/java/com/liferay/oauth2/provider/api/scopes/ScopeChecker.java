package com.liferay.oauth2.provider.api.scopes;

public interface ScopeChecker {

	public void pushNamespace(NamespaceAdder namespaceAdder);

	public void requireScope(String scope);

	public boolean hasScope(String scope);
}
