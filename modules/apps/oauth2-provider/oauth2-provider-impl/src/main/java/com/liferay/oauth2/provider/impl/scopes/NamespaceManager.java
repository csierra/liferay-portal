package com.liferay.oauth2.provider.impl.scopes;

import com.liferay.oauth2.provider.api.scopes.Scope;
import com.liferay.oauth2.provider.api.scopes.ScopeMatcher;

import java.util.Collection;
import java.util.function.Consumer;

public interface NamespaceManager {

	interface Namespace {

		public NamespacedScope addScope(Class<? extends Scope> scopeType);

		public Collection<Class<? extends Scope>> findScopes(
			ScopeMatcher scopeMatcher);

		public void forEach(Consumer<NamespacedScope> consumer);

	}

	public Namespace createNamespace();

	public interface NamespacedScope {
		public Class<? extends Scope> getScopeType();
	}

}
