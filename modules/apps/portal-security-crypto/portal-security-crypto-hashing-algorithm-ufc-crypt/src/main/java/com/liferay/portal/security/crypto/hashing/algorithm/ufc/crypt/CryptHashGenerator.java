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

package com.liferay.portal.security.crypto.hashing.algorithm.ufc.crypt;

import com.liferay.portal.kernel.security.SecureRandom;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.security.crypto.generator.spi.AbstractHashGenerator;
import com.liferay.portal.security.crypto.generator.spi.HashGenerator;

import java.util.Random;

import org.osgi.service.component.annotations.Component;

import org.vps.crypt.Crypt;

/**
 * @author arthurchan35
 */
@Component(
	immediate = true, property = "crypto.hashing.algorithm=UFC-Crypt",
	service = HashGenerator.class
)
public class CryptHashGenerator extends AbstractHashGenerator {

	@Override
	public String generateSalt() throws Exception {
		Random random = new SecureRandom();

		int x = random.nextInt(Integer.MAX_VALUE) % _SALT.length;
		int y = random.nextInt(Integer.MAX_VALUE) % _SALT.length;

		return _SALT[x].concat(_SALT[y]);
	}

	@Override
	public String getGeneratorName() {
		return "UFC-Crypt";
	}

	@Override
	public byte[] hash(String password) throws Exception {
		String combined = password + pepper;

		String hash = Crypt.crypt(salt.getBytes(), combined.getBytes());

		return hash.getBytes();
	}

	private static final String[] _SALT = ArrayUtil.toStringArray(
		"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789./".
			toCharArray());

}