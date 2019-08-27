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

package com.liferay.portal.security.crypto.hashing.algorithm.message.digest;

import com.liferay.portal.security.crypto.generator.spi.AbstractHashGenerator;
import com.liferay.portal.security.crypto.generator.spi.HashGenerator;

import java.security.MessageDigest;

import org.osgi.service.component.annotations.Component;

/**
 * @author arthurchan35
 */
@Component(
	immediate = true,
	property = {
		"crypto.hashing.algorithm=MD2", "crypto.hashing.algorithm=MD5",
		"crypto.hashing.algorithm=SHA-1", "crypto.hashing.algorithm=SHA-224",
		"crypto.hashing.algorithm=SHA-256", "crypto.hashing.algorithm=SHA-384",
		"crypto.hashing.algorithm=SHA-512"
	},
	service = HashGenerator.class
)
public class MessageDigestHashGenerator extends AbstractHashGenerator {

	public MessageDigestHashGenerator(String algorithmName) {
		_algorithmName = algorithmName;
	}

	@Override
	public String getGeneratorName() {
		return _algorithmName;
	}

	@Override
	public byte[] hash(String toBeHashed) throws Exception {
		MessageDigest messageDigest = MessageDigest.getInstance(_algorithmName);

		String combined = toBeHashed + pepper + salt;

		return messageDigest.digest(combined.getBytes());
	}

	private MessageDigestHashGenerator() {
	}

	private String _algorithmName;

}