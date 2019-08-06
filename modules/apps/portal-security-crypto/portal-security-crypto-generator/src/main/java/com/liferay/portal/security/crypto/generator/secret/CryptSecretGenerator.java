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

import com.liferay.portal.kernel.util.Digester;

import org.vps.crypt.Crypt;

/**
 * @author arthurchan35
 */
public class CryptSecretGenerator {

	public static String generate(String password, byte[] saltBytes)
		throws Exception {

		return Crypt.crypt(saltBytes, password.getBytes(Digester.ENCODING));
	}

	public static String generate(String password, String salt)
		throws Exception {

		byte[] saltBytes = saltBytes = salt.getBytes(Digester.ENCODING);

		return Crypt.crypt(saltBytes, password.getBytes(Digester.ENCODING));
	}

}