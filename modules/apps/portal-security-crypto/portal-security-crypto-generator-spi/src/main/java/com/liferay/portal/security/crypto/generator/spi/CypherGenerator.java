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

package com.liferay.portal.security.crypto.generator.spi;

/**
 * @author arthurchan35
 */
public interface CypherGenerator extends Generator {

	public byte[] decrypt(byte[] cypherText, byte[] key) throws Exception;

	public default byte[] decrypt(String cypherText, String key)
		throws Exception {

		return decrypt(cypherText.getBytes(), key.getBytes());
	}

	public byte[] encrypt(byte[] plainText, byte[] key) throws Exception;

	public default byte[] encrypt(String plainText, String key)
		throws Exception {

		return encrypt(plainText.getBytes(), key.getBytes());
	}

}