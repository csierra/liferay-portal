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

package com.liferay.portal.security.credential.generator;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.security.credential.model.AlgorithmEntry;
import com.liferay.portal.security.credential.model.CredentialMeta;

import java.util.List;

/**
 * @author arthurchan35
 */
public class CredentialGeneratorUtil {

	public static String generateSalt(AlgorithmEntry algorithm)
		throws Exception {

		String algorithmName = algorithm.getType();

		if (algorithmName.equals("MD2") || algorithmName.equals("MD5") ||
			algorithmName.equals("SHA-1") || algorithmName.equals("SHA-224") ||
			algorithmName.equals("SHA-256") ||
			algorithmName.equals("SHA-384") ||
			algorithmName.equals("SHA-512")) {

			return MessageDigestCredentialGenerator.generateSalt(algorithm);
		}

		if (algorithmName.equals("BCRYPT")) {
			return BCryptCredentialGenerator.generateSalt(algorithm);
		}

		if (algorithmName.equals("PBKDF2")) {
			return PBKDF2CredentialGenerator.generateSalt(algorithm);
		}

		if (algorithmName.equals("UFC-CRYPT")) {
			return CryptCredentialGenerator.generateSalt(algorithm);
		}

		if (algorithmName.equals("NONE")) {
			return StringPool.BLANK;
		}

		throw new Exception("Invalid Encryption Algorithm");
	}

	public static String generateSecret(String password, CredentialMeta meta)
		throws Exception {

		AlgorithmEntry algorithm = meta.getAlgorithmEntry();

		String algorithmName = algorithm.getType();

		if (algorithmName.equals("MD2") || algorithmName.equals("MD5") ||
			algorithmName.equals("SHA-1") || algorithmName.equals("SHA-224") ||
			algorithmName.equals("SHA-256") ||
			algorithmName.equals("SHA-384") ||
			algorithmName.equals("SHA-512")) {

			return MessageDigestCredentialGenerator.generateSecret(
				password, meta);
		}

		if (algorithmName.equals("BCRYPT")) {
			return BCryptCredentialGenerator.generateSecret(password, meta);
		}

		if (algorithmName.equals("PBKDF2")) {
			return PBKDF2CredentialGenerator.generateSecret(password, meta);
		}

		if (algorithmName.equals("UFC-CRYPT")) {
			return CryptCredentialGenerator.generateSecret(password, meta);
		}

		if (algorithmName.equals("NONE")) {
			return password;
		}

		throw new Exception("Invalid Encryption Algorithm");
	}

	public static String generateSecret(
			String password, List<CredentialMeta> metas)
		throws Exception {

		for (CredentialMeta meta : metas) {
			password = generateSecret(password, meta);
		}

		return password;
	}

}