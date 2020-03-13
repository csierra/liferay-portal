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

package com.liferay.portal.crypto.hash.provider.message.digest;

import com.liferay.portal.crypto.hash.provider.spi.BaseHashProvider;
import com.liferay.portal.crypto.hash.provider.spi.salt.VariableSizeSaltProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Arthur Chan
 */
public class MessageDigestHashProvider
	extends BaseHashProvider implements VariableSizeSaltProvider {

	public MessageDigestHashProvider() throws NoSuchAlgorithmException {
		this("SHA-512");
	}

	public MessageDigestHashProvider(String providerName)
		throws NoSuchAlgorithmException {

		_messageDigest = MessageDigest.getInstance(providerName);
	}

	@Override
	public byte[] hash(byte[] input) {
		byte[] bytes = new byte[pepper.length + salt.length + input.length];

		System.arraycopy(pepper, 0, bytes, 0, pepper.length);
		System.arraycopy(salt, 0, bytes, pepper.length, salt.length);
		System.arraycopy(
			input, 0, bytes, pepper.length + salt.length, input.length);

		return _messageDigest.digest(bytes);
	}

	private final MessageDigest _messageDigest;

}