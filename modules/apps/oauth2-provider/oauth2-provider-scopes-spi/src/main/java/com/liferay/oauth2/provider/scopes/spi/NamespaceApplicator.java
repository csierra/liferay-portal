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

package com.liferay.oauth2.provider.scopes.spi;

//import com.liferay.portal.kernel.util.StringPool;

import java.util.Collection;

/**
 * Interface that represents a prefix for the input scopes. This abstraction
 * will allow the framework to adapt the applications to different scope
 * naming hopefully without having to change code.
 */
public interface NamespaceApplicator {

	public static NamespaceApplicator intersect(
		Collection<NamespaceApplicator> namespaceApplicators) {

		NamespaceApplicator namespaceApplicator = NULL_HANDLER;

		for (NamespaceApplicator na : namespaceApplicators) {
			namespaceApplicator = namespaceApplicator.intersect(na);
		}

		return namespaceApplicator;
	}

	/**
	 * Adds the prefix to a given input.
	 * @param target String to be prefixed.
	 * @return a new String with the prefix.
	 */
	public String applyNamespace(String target);

	/**
	 * A new {@link NamespaceApplicator} taking into account the given
	 * {@link NamespaceApplicator}
	 *
	 * @param prefixHandler the prefix handler to append.
	 * @return a new prefix handler combining both prefix handlers.
	 */
	public default NamespaceApplicator intersect(NamespaceApplicator prefixHandler) {
		return string -> applyNamespace(prefixHandler.applyNamespace(string));
	}

	/**
	 * A {@link NamespaceApplicator} that does nothing.
	 */
	static NamespaceApplicator NULL_HANDLER = new NamespaceApplicator() {

		@Override
		public String applyNamespace(String target) {
			return target;
		}

		@Override
		public NamespaceApplicator intersect(NamespaceApplicator prefixHandler) {
			return prefixHandler;
		}

	};


}