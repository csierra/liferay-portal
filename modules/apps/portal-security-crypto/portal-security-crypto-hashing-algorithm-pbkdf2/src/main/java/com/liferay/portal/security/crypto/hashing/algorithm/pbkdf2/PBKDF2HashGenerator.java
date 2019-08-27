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

package com.liferay.portal.security.crypto.hashing.algorithm.pbkdf2;

import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.security.crypto.generator.spi.AbstractHashGenerator;
import com.liferay.portal.security.crypto.generator.spi.HashGenerator;

import org.osgi.service.component.annotations.Component;

/**
 * @author arthurchan35
 */
@Component(
	immediate = true, property = "crypto.hashing.algorithm=PBKDF2",
	service = HashGenerator.class
)
public class PBKDF2HashGenerator extends AbstractHashGenerator {

	public PBKDF2HashGenerator(String prfName, int rounds, int keySize) {
		_prfName = prfName;
		_rounds = rounds;
		_keySize = keySize;
	}

	@Override
	public HashGenerator addSalt(byte[] salt) throws Exception {
		if (salt == null) {
			throw new Exception("Do not add null for salt");
		}

		return addSalt(Base64.encode(salt));
	}

	@Override
	public String getGeneratorName() {
		return "PBKDF2";
	}

	@Override
	public byte[] hash(String password) throws Exception {
		return PBKDF2.generate(
			_prfName, password + pepper, Base64.decode(salt), _rounds,
			_keySize);
	}

	private PBKDF2HashGenerator() {
	}

	private int _keySize;
	private String _prfName;
	private int _rounds;

}