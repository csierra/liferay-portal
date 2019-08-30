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

package com.liferay.portal.security.crypto.hashing.algorithm.bcrypt;

import com.liferay.portal.security.crypto.generator.spi.AbstractHashGenerator;
import com.liferay.portal.security.crypto.generator.spi.HashGenerator;

import jodd.util.BCrypt;

import org.osgi.service.component.annotations.Component;

/**
 * @author arthurchan35
 */
@Component(
	immediate = true, property = "crypto.hashing.algorithm=BCrypt",
	service = HashGenerator.class
)
public class BCryptHashGenerator extends AbstractHashGenerator {

	public BCryptHashGenerator(int rounds) {
		_rounds = rounds;
	}

	@Override
	public String generateSalt() throws Exception {
		return BCrypt.gensalt(_rounds);
	}

	@Override
	public String getGeneratorName() {
		return "BCrypt";
	}

	@Override
	public byte[] hash(String toBeHashed) throws Exception {
		String hash = BCrypt.hashpw(toBeHashed + pepper, salt);

		return hash.getBytes();
	}

	private BCryptHashGenerator() {
	}

	private int _rounds;

}