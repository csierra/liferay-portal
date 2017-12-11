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

public interface NamespaceAdder {

	public String add(String localName);

	public default NamespaceAdder prepend(NamespaceAdder namespaceAdder) {
		return localName -> namespaceAdder.add(add(localName));
	}

	public default NamespaceAdder append(NamespaceAdder namespaceAdder) {
		return localName -> add(namespaceAdder.add(localName));
	}
}
