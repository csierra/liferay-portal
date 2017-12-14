package com.liferay.oauth2.provider.api.scopes;

public interface ScopeStack {
	void pushNamespace(NamespaceAdder namespaceAdder);

	NamespaceAdder popNamespace();
}
