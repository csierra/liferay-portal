/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.oauth2.provider.impl.scopes;

import com.liferay.oauth2.provider.api.scopes.Scope;
import com.liferay.oauth2.provider.api.scopes.ScopeMatcher;
import org.osgi.service.component.annotations.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DefaultNamespaceManager implements NamespaceManager {

	@Override
	public Namespace createNamespace() {
		Collection<NamespacedScope> scopes = new HashSet<>();

		return new Namespace() {

			@Override
			public NamespacedScope addScope(Class<? extends Scope> scopeType) {
				NamespacedScope namespacedScope =
					new NamespacedScopeImpl(scopeType);

				scopes.add(namespacedScope);

				return namespacedScope;
			}

			@Override
			public Collection<Class<? extends Scope>> findScopes(
				ScopeMatcher matcher) {

				Stream<NamespacedScope> stream = scopes.stream();

				return stream.map(
					NamespacedScope::getScopeType
				).filter(
					matcher::matches
				).collect(
					Collectors.toList()
				);
			}

			@Override
			public void forEach(Consumer<NamespacedScope> consumer) {
				scopes.forEach(consumer);
			}

		};

	}

	private static class NamespacedScopeImpl implements NamespacedScope {

		private final Class<? extends Scope> _scopeType;

		public NamespacedScopeImpl(Class<? extends Scope> scopeType) {
			if (scopeType == null) {
				throw new IllegalArgumentException("ScopeType can't be null");
			}

			_scopeType = scopeType;
		}

		@Override
		public Class<? extends Scope> getScopeType() {
			return _scopeType;
		}

		@Override
		public boolean equals(Object object) {
			if (this == object) {
				return true;
			}
			if ((object == null) || getClass() != object.getClass()) {
				return false;
			}

			NamespacedScopeImpl namespacedScope = (NamespacedScopeImpl) object;

			return _scopeType.equals(namespacedScope._scopeType);
		}

		@Override
		public int hashCode() {
			return _scopeType.hashCode();
		}

	}

}
