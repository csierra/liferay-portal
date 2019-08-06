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

package com.liferay.portal.security.crypto.generator.secret;

import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.Digester;

import java.security.MessageDigest;

/**
 * @author arthurchan35
 */
public class MessageDigestSecretGenerator {

	/**
	 * Generates a message digest for password using algorithm specified with algorithmName
	 * See https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#MessageDigest
	 *
	 * @param algorithmName the name of the message digest algorithm
	 * @param password the password used to generate message digest from
	 * @return Base64 encoded string of secret
	 */
	public static String generate(String algorithmName, String password)
		throws Exception {

		return Base64.encode(_generateBytes(algorithmName, password));
	}

	/**
	 * Generates a salted message digest for password using algorithm specified with algorithmName
	 *
	 * @param algorithmName the name of the message digest algorithm
	 * @param password the password used to generate message digest from
	 * @param saltBytes byte array of crypto salt
	 * @return Base64 encoded string of secret with salt
	 */
	public static String generate(
			String algorithmName, String password, byte[] saltBytes)
		throws Exception {

		byte[] messageDigestBytes = _generateBytes(algorithmName, password);

		MessageDigest messageDigest = MessageDigest.getInstance(algorithmName);

		messageDigestBytes = messageDigest.digest(
			ArrayUtil.append(messageDigestBytes, saltBytes));

		return Base64.encode(messageDigestBytes);
	}

	/**
	 * Generates a salted message digest for password using algorithm specified with algorithmName
	 *
	 * @param algorithmName the name of the message digest algorithm
	 * @param password the password used to generate message digest from
	 * @param salt the crypto salt
	 * @return Base64 encoded string of secret with salt
	 */
	public static String generate(
			String algorithmName, String password, String salt)
		throws Exception {

		byte[] saltBytes = Base64.decode(salt);

		return generate(algorithmName, password, saltBytes);
	}

	private static byte[] _generateBytes(String algorithmName, String password)
		throws Exception {

		MessageDigest messageDigest = MessageDigest.getInstance(algorithmName);

		byte[] passwordBytes = password.getBytes(Digester.ENCODING);

		return messageDigest.digest(passwordBytes);
	}

}