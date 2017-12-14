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

package com.liferay.oauth2.provider.api.scopes;

import java.util.Collection;
import java.util.Locale;
import java.util.stream.Stream;

public interface NamespaceAdder {

	public String addNamespace(String localName);

	public default NamespaceAdder prepend(NamespaceAdder namespaceAdder) {
		return localName ->
			namespaceAdder.addNamespace(addNamespace(localName));
	}

	public default NamespaceAdder append(NamespaceAdder namespaceAdder) {
		return localName ->
			addNamespace(namespaceAdder.addNamespace(localName));
	}

	public static NamespaceAdder merge(
		Collection<NamespaceAdder> namespaceAdders) {

		NamespaceAdder namespaceAdder = NULL_ADDER;

		for (NamespaceAdder na : namespaceAdders) {
			namespaceAdder = namespaceAdder.append(na);
		}

		return namespaceAdder;
	}

	static NamespaceAdder NULL_ADDER = new NamespaceAdder() {

		@Override
		public String addNamespace(String localName) {
			return localName;
		}

		@Override
		public NamespaceAdder prepend(NamespaceAdder namespaceAdder) {
			return namespaceAdder;
		}

		@Override
		public NamespaceAdder append(NamespaceAdder namespaceAdder) {
			return namespaceAdder;
		}

		@Override
		public ScopeFinder prepend(ScopeFinder scopeFinder) {
			return scopeFinder;
		}

	};

	public default ScopeFinder prepend(ScopeFinder scopeFinder) {
		return () -> {
			Stream<OAuth2Scope> stream = scopeFinder.findScopes();

			return stream.map(
				o -> new OAuth2Scope() {
					@Override
					public String getLocalName() {
						return addNamespace(o.getLocalName());
					}

					@Override
					public String getDescription(Locale locale) {
						return o.getDescription(locale);
					}
				}
			);
		};
	}
}
