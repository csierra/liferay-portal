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

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * @author arthurchan35
 */
public class PBKDF2 {

	/**
	 * Generates a key using password based derivition function 2 from a password
	 * See https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#SecretKeyFactory
	 *
	 * @param prfName the name of pseudo-random function used by PBKDF2
	 * @param password the password used to generate key from
	 * @param saltBytes the bytes of salt used by PBKDF2
	 * @param rounds the number of PBKDF2 iterations
	 * @param keySize the size of generated key in bits
	 * @return bytes of generated key
	 */
	protected static byte[] generate(
			String prfName, String password, byte[] saltBytes, int rounds,
			int keySize)
		throws Exception {

		PBEKeySpec pbeKeySpec = new PBEKeySpec(
			password.toCharArray(), saltBytes, rounds, keySize);

		String fullAlgorithmName = "PBKDF2With" + prfName;

		SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(
			fullAlgorithmName);

		SecretKey secretKey = secretKeyFactory.generateSecret(pbeKeySpec);

		return secretKey.getEncoded();
	}

}