/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.crypto.hash.provider.spi.factory;

import com.liferay.portal.crypto.hash.provider.spi.HashProvider;

import org.json.JSONObject;

import org.osgi.annotation.versioning.ConsumerType;

/**
 * @author Arthur Chan
 */
@ConsumerType
public interface HashProviderFactory {

	/**
	 * Construct a {@link HashProvider} from given provider information.
	 *
	 * @param providerName The name of the provider
	 * @param providerMeta A JSON Object of meta info required by some algorithms
	 * @return An instance of HashProvider
	 */
	public HashProvider create(String providerName, JSONObject providerMeta)
		throws Exception;

}