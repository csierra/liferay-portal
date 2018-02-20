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

import com.liferay.oauth2.provider.scopes.spi.model.ScopeMatcher;

/**
 * Factory that creates {@link ScopeMatcher} for a given input.
 * This allow for components to switch matching strategies using configuration.
 */
public interface ScopeMatcherFactory {

	/**
	 * Creates a {@link ScopeMatcher} for the given input.
	 * @param input the input the matcher will match against.
	 * @return the ScopeMatcher that will match against the input.
	 */
	ScopeMatcher create(String input);
}
