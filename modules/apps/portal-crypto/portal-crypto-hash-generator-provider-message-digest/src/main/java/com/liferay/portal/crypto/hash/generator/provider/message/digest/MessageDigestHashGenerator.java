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

package com.liferay.portal.crypto.hash.generator.provider.message.digest;

import com.liferay.portal.crypto.hash.generator.spi.VariableSizeSaltHashGenerator;

import java.security.MessageDigest;

/**
 * @author Arthur Chan
 */
public class MessageDigestHashGenerator extends VariableSizeSaltHashGenerator {

	public MessageDigestHashGenerator() {
		_chosenGenerator = "SHA-512";
	}

	public MessageDigestHashGenerator(String generatorName) {
		_chosenGenerator = generatorName;
	}

	@Override
	public byte[] hash(byte[] input) throws Exception {
		MessageDigest messageDigest = MessageDigest.getInstance(
			_chosenGenerator);

		byte[] combined = new byte[input.length + pepper.length + salt.length];

		System.arraycopy(input, 0, combined, 0, input.length);
		System.arraycopy(pepper, 0, combined, input.length, pepper.length);
		System.arraycopy(
			salt, 0, combined, input.length + pepper.length, salt.length);

		return messageDigest.digest(combined);
	}

	private final String _chosenGenerator;

}