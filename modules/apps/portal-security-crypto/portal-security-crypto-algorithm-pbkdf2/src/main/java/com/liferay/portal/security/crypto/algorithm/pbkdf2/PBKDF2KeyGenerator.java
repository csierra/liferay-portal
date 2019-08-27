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
import com.liferay.portal.security.crypto.key.generator.spi.PasswordBasedKeyGenerator;

/**
 * @author arthurchan35
 */
@Component(
	immediate = true, property = "crypto.key.generation.algorithm=PBKDF2",
	service = PasswordBasedKeyGenerator.class
)
public class PBKDF2KeyGenerator implements PasswordBasedKeyGenerator {

	/**
	 * Generates a key using password based derivition function 2 for password
	 * See https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#SecretKeyFactory
	 *
	 * @param password the password used to generate key from
	 * @param keyMaterial meta infomation used by PBKDF2
	 * @return bytes of key
	 */
	public byte[] generate(byte[] password, JSONObject keyMaterial)
		throws Exception {

		String prfName = keyMaterial.getString("prfName");
		String salt = keyMaterial.getString("salt");
		int rounds = keyMaterial.getInt("rounds");
		int keySize = keyMaterial.getInt("keySize");

		return PBKDF2.generate(
			prfName, new String(password), salt.getBytes(), rounds, keySize);
	}

}