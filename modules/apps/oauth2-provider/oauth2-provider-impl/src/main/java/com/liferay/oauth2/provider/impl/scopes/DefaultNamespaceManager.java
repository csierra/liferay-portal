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

import com.liferay.oauth2.provider.api.scopes.OAuth2Scopes;
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
		return new NamespaceImpl();
	}

	private static class NamespacedScopeImpl implements NamespacedScope {

		private final Class<? extends OAuth2Scopes> _scopeType;

		public NamespacedScopeImpl(Class<? extends OAuth2Scopes> scopeType) {
			if (scopeType == null) {
				throw new IllegalArgumentException("ScopeType can't be null");
			}

			_scopeType = scopeType;
		}

		@Override
		public Class<? extends OAuth2Scopes> getScopeType() {
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

	private static class NamespaceImpl implements Namespace {

		private final Collection<NamespacedScope> _scopes;

		public NamespaceImpl() {
			_scopes = new HashSet<>();
		}

		@Override
		public NamespacedScope addScope(Class<? extends OAuth2Scopes> scopeType) {
			NamespacedScope namespacedScope =
				new NamespacedScopeImpl(scopeType);

			_scopes.add(namespacedScope);

			return namespacedScope;
		}

		@Override
		public Collection<Class<? extends OAuth2Scopes>> findScopes(
			ScopeMatcher matcher) {

			Stream<NamespacedScope> stream = _scopes.stream();

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
			_scopes.forEach(consumer);
		}

	}

}
