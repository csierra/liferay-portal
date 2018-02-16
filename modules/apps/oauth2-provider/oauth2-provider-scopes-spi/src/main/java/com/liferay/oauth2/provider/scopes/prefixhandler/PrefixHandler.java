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

package com.liferay.oauth2.provider.scopes.prefixhandler;

import com.liferay.oauth2.provider.scopes.scopematcher.ScopeMatcher;
import com.liferay.portal.kernel.util.StringPool;

import java.util.Collection;

/**
 * Interface that represents a prefix for the input scopes. This abstraction
 * will allow the framework to adapt the applications to different scope
 * naming hopefully without having to change code.
 */
public interface PrefixHandler {

	public static PrefixHandler merge(
		Collection<PrefixHandler> namespaceAdders) {

		PrefixHandler namespaceAdder = NULL_HANDLER;

		for (PrefixHandler na : namespaceAdders) {
			namespaceAdder = namespaceAdder.append(na);
		}

		return namespaceAdder;
	}

	/**
	 * Adds the prefix to a given input.
	 * @param input String to be prefixed.
	 * @return a new String with the prefix.
	 */
	public String addPrefix(String input);

	/**
	 * A new {@link PrefixHandler} taking into account the given
	 * {@link PrefixHandler}
	 *
	 * @param prefixHandler the prefix handler to append.
	 * @return a new prefix handler combining both prefix handlers.
	 */
	public default PrefixHandler append(PrefixHandler prefixHandler) {
		return string -> addPrefix(prefixHandler.addPrefix(string));
	}

	/**
	 * Returns a new {@link ScopeMatcher} that takes into account the effect
	 * of the given {@link PrefixHandler}. Some implementations might have
	 * optimization opportunities.
	 *
	 * @param ScopeMatcher the scope matcher which should take into account 
	 * this prefix handler.
	 * @return the new ScopeMatcher that takes into account the
	 * {@link PrefixHandler}
	 */
	public default ScopeMatcher prepend(ScopeMatcher scopeMatcher) {
		return localName -> scopeMatcher.match(addPrefix(localName));
	}	
	
	/**
	 * A {@link PrefixHandler} that does nothing.
	 */
	static PrefixHandler NULL_HANDLER = new PrefixHandler() {

		@Override
		public String addPrefix(String input) {
			return input;
		}

		@Override
		public PrefixHandler append(PrefixHandler prefixHandler) {
			return prefixHandler;
		}

	};


}