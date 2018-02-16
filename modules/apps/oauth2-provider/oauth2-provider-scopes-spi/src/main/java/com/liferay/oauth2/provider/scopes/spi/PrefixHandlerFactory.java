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

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.liferay.oauth2.provider.scopes.prefixhandler.PrefixHandler;

/**
 * Interface to create {@link PrefixHandler} using a given prefix.
 * This allows components to switch prefixing strategies using configuration,
 * such as using different characters <i>'_'</i> or <i>'.'</i>, thus keeping
 * the prefixing strategy consistent across components.
 */
public interface PrefixHandlerFactory {

	/**
	 * This method allows to create a {@link PrefixHandler} using the properties
	 * in the {@link PropertyGetter}
	 *
	 * @param propertyGetter the {@link PropertyGetter} to configure the
	 *                       {@link PrefixHandler}
	 * @return the {@link PrefixHandler} for the given {@link PropertyGetter}
	 */
	public PrefixHandler mapFrom(PropertyGetter propertyGetter);
}