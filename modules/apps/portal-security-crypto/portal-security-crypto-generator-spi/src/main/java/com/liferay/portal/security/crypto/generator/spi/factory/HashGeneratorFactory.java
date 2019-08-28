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

package com.liferay.portal.security.crypto.generator.spi.factory;

import com.liferay.portal.security.crypto.generator.spi.HashGenerator;

import org.json.JSONObject;

/**
 * @author arthurchan35
 */
public interface HashGeneratorFactory {

	/**
	 * Construct a {@link com.liferay.portal.security.crypto.generator.spi.HasherGenerator} from given algorithm.
	 *
	 * @param generatorName The name of algorithm this generator is associated with
	 * @param generatorMeta A JSON Object of meta info required by some algorithms
	 * @return An instance of HashGenerator
	 */
	public HashGenerator getGenerator(
			String generatorName, JSONObject generatorMeta)
		throws Exception;

	/**
	 * Construct a {@link com.liferay.portal.security.crypto.generator.spi.HasherGenerator} from given algorithm.
	 *
	 * @param generatorName The name of algorithm this generator is associated with
	 * @param generatorMeta A JSON Object formatted String of meta info required by some algorithms
	 * @return An instance of HashGenerator
	 */
	public default HashGenerator getGenerator(
			String generatorName, String generatorMeta)
		throws Exception {

		JSONObject generatorMetaJSONObject = new JSONObject(generatorMeta);

		return getGenerator(generatorName, generatorMetaJSONObject);
	}

}