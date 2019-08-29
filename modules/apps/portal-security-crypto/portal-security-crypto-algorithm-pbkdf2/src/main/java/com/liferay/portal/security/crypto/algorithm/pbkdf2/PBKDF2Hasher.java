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

package com.liferay.portal.security.crypto.algorithm.pbkdf2;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.security.crypto.hasher.spi.Hasher;

/**
 * @author arthurchan35
 */
@Component(
	immediate = true, property = "crypto.hashing.algorithm=PBKDF2",
	service = Hasher.class
)
public class PBKDF2Hasher implements Hasher {

	/**
	 * Generates a Hash using password based derivition function 2 for password
	 * See https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#SecretKeyFactory
	 *
	 * @param password the password used to generate hash from
	 * @param salt the bytes of salt used by PBKDF2
	 * @param pepper the bytes of pepper used to enhance security
	 * @param algorithmMeta other meta infomation used by PBKDF2
	 * @return bytes of hash
	 */
	public byte[] hash(
			byte[] password, byte[] salt, byte[] pepper,
			JSONObject algorithmMeta)
		throws Exception {

		String prfName = algorithmMeta.getString("prfName");
		int rounds = algorithmMeta.getInt("rounds");
		int keySize = algorithmMeta.getInt("keySize");

		String passwordString = new String(password);
		String pepperString = new String(pepper);

		return PBKDF2.generate(
			prfName, passwordString + pepperString, salt, rounds, keySize);
	}

	/**
	 * Generates a Hash using password based derivition function 2 for password
	 * See https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#SecretKeyFactory
	 *
	 * @param password the password used to generate hash from
	 * @param salt the String of salt used by PBKDF2
	 * @param pepper the String of pepper used to enhance security
	 * @param algorithmMeta other meta infomation used by PBKDF2
	 * @return bytes of hash
	 */
	public byte[] hash(
			String password, String salt, String pepper,
			JSONObject algorithmMeta)
		throws Exception {

		String prfName = algorithmMeta.getString("prfName");
		int rounds = algorithmMeta.getInt("rounds");
		int keySize = algorithmMeta.getInt("keySize");

		return PBKDF2.generate(
			prfName, password + pepper, salt.getBytes(), rounds, keySize);
	}

}