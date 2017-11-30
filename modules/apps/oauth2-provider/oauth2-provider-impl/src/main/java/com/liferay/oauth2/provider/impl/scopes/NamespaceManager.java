package com.liferay.oauth2.provider.impl.scopes;

import com.liferay.oauth2.provider.api.scopes.OAuth2Scopes;
import com.liferay.oauth2.provider.api.scopes.ScopeMatcher;

import java.util.Collection;
import java.util.function.Consumer;

public interface NamespaceManager {

	interface Namespace {

		public NamespacedScope addScope(Class<? extends OAuth2Scopes> scopeType);

		public Collection<Class<? extends OAuth2Scopes>> findScopes(
			ScopeMatcher scopeMatcher);

		public void forEach(Consumer<NamespacedScope> consumer);

	}

	public Namespace createNamespace();

	public interface NamespacedScope {
		public Class<? extends OAuth2Scopes> getScopeType();
	}

}
