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

package com.liferay.portal.security.crypto.hashing.algorithm.message.digest.factory;

import com.liferay.portal.security.crypto.generator.hashing.HashGenerator;
import com.liferay.portal.security.crypto.generator.spi.hashing.factory.HashGeneratorFactory;
import com.liferay.portal.security.crypto.hashing.algorithm.message.digest.MessageDigestHashGenerator;

import org.json.JSONObject;

import org.osgi.service.component.annotations.Component;

/**
 * @author Arthur Chan
 */
@Component(
	immediate = true,
	property = {
		"crypto.hashing.algorithm=MD2", "crypto.hashing.algorithm=MD5",
		"crypto.hashing.algorithm=SHA-1", "crypto.hashing.algorithm=SHA-224",
		"crypto.hashing.algorithm=SHA-256", "crypto.hashing.algorithm=SHA-384",
		"crypto.hashing.algorithm=SHA-512"
	},
	service = HashGeneratorFactory.class
)
public class MessageDigestHashGeneratorFactory implements HashGeneratorFactory {

	@Override
	public HashGenerator create(String generatorName, JSONObject generatorMeta)
		throws Exception {

		return new MessageDigestHashGenerator(generatorName);
	}

}