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

package com.liferay.portal.security.crypto.algorithm.message.digest;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.security.crypto.hasher.spi.Hasher;

import java.security.MessageDigest;

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
	service = Hasher.class
)
public class MessageDigestHasher implements Hasher {

	@Override
	public byte[] hash(
			byte[] password, byte[] salt, byte[] pepper,
			JSONObject algorithmMeta)
		throws Exception {

		String algorithmName = algorithmMeta.getString("algorithmName");

		MessageDigest messageDigest = MessageDigest.getInstance(algorithmName);

		byte[] combined =
			new byte[password.length + salt.length + pepper.length];

		for (int i = 0; i < password.length; ++i) {
			combined[i] = password[i];
		}

		for (int i = 0; i < salt.length; ++i) {
			combined[i + password.length] = salt[i];
		}

		for (int i = 0; i < pepper.length; ++i) {
			combined[password.length + salt.length] = pepper[i];
		}

		return messageDigest.digest(combined);
	}

	@Override
	public byte[] hash(
			String password, String salt, String pepper,
			JSONObject algorithmMeta)
		throws Exception {

		return hash(
			password.getBytes(), salt.getBytes(), pepper.getBytes(),
			algorithmMeta);
	}

}