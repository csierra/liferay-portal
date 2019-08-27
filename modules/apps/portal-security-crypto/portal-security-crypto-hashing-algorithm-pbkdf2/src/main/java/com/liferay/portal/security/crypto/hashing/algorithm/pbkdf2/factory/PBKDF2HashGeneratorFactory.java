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

package com.liferay.portal.security.crypto.hashing.algorithm.pbkdf2.factory;

import com.liferay.portal.security.crypto.generator.spi.HashGenerator;
import com.liferay.portal.security.crypto.generator.spi.factory.HashGeneratorFactory;
import com.liferay.portal.security.crypto.hashing.algorithm.pbkdf2.PBKDF2HashGenerator;

import org.json.JSONObject;

import org.osgi.service.component.annotations.Component;

/**
 * @author arthurchan35
 */
@Component(
	immediate = true, property = "crypto.hash.generator.factory=PBKDF2",
	service = HashGeneratorFactory.class
)
public class PBKDF2HashGeneratorFactory implements HashGeneratorFactory {

	@Override
	public HashGenerator getGenerator(
			String generatorName, JSONObject generatorMeta)
		throws Exception {

		String prfName = generatorMeta.getString("prfName");
		int rounds = generatorMeta.getInt("rounds");
		int keySize = generatorMeta.getInt("keySize");

		return new PBKDF2HashGenerator(prfName, rounds, keySize);
	}

}