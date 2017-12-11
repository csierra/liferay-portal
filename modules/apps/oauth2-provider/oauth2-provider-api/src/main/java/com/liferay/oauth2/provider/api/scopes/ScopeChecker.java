package com.liferay.oauth2.provider.api.scopes;

public interface ScopeChecker {
	void pushNamespace(NamespaceAdder namespaceAdder);

	void requireScope(String read);

}
