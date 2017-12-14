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

import com.liferay.oauth2.provider.api.scopes.NamespaceAdder;
import com.liferay.oauth2.provider.api.scopes.OAuth2Scope;
import com.liferay.oauth2.provider.api.scopes.ScopeChecker;
import com.liferay.oauth2.provider.api.scopes.ScopeStack;

import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.stream.Stream;

public class ThreadLocalScopeChecker implements ScopeChecker, ScopeStack {

	ThreadLocal<Collection<OAuth2Scope>> _allowedScopes =
		ThreadLocal.withInitial(Collections::emptySet);

	ThreadLocal<Deque<NamespaceAdder>> _namespaceAdders =
		ThreadLocal.withInitial(LinkedList::new);

	ThreadLocal<NamespaceAdder> _namespaceAdder = ThreadLocal.withInitial(
		() -> NamespaceAdder.NULL_ADDER);

	@Override
	public boolean hasScope(String scope) {
		Stream<OAuth2Scope> stream = _allowedScopes.get().stream();

		NamespaceAdder namespaceAdder = _namespaceAdder.get();

		return stream.map(
			o -> namespaceAdder.addNamespace(o.getLocalName())
		).anyMatch(
			s -> s.equals(scope)
		);
	}

	@Override
	public void pushNamespace(NamespaceAdder namespaceAdder) {
		Deque<NamespaceAdder> namespaceAdders = _namespaceAdders.get();

		namespaceAdders.addLast(namespaceAdder);

		_namespaceAdder.set(NamespaceAdder.merge(namespaceAdders));

	}

	@Override
	public NamespaceAdder popNamespace() {
		Deque<NamespaceAdder> namespaceAdders = _namespaceAdders.get();

		NamespaceAdder namespaceAdder = namespaceAdders.removeLast();

		_namespaceAdder.set(NamespaceAdder.merge(namespaceAdders));

		return namespaceAdder;

	}

	public void setAllowedScopes(Collection<OAuth2Scope> allowedScopes) {
		_allowedScopes.set(allowedScopes);
	}

}
