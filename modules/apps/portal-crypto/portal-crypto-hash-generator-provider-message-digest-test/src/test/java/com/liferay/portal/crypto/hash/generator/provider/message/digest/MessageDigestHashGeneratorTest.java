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

package com.liferay.portal.crypto.hash.generator.provider.message.digest;

import com.liferay.portal.crypto.hash.generator.provider.message.digest.factory.MessageDigestHashGeneratorFactory;
import com.liferay.portal.crypto.hash.generator.spi.HashGenerator;
import com.liferay.portal.crypto.hash.generator.spi.salt.VariableSizeSaltGenerator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.MockitoAnnotations;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Arthur Chan
 */
@PrepareForTest(MessageDigestHashGenerator.class)
@RunWith(PowerMockRunner.class)
public class MessageDigestHashGeneratorTest extends PowerMockito {

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		_hashGenerator = _hashGeneratorFactory.create(
			_MESSAGE_DIGEST_ALGORITHM, null);
	}

	@Test
	public void testGenerateDefaultSizeSalt() throws Exception {
		byte[] salt = _hashGenerator.generateSalt();

		Assert.assertTrue(salt.length == Long.BYTES);
	}

	@Test
	public void testGenerateHashWithoutSaltAndPepper() throws Exception {
		byte[] hash = _hashGenerator.hash(_PASSWORD.getBytes());

		Assert.assertEquals(_PASSWORD_HASH, _toHex(hash));
	}

	@Test
	public void testGenerateHashWithPepper() throws Exception {
		_hashGenerator.setPepper(_PEPPER.getBytes());

		byte[] hash = _hashGenerator.hash(_PASSWORD.getBytes());

		Assert.assertEquals(_PASSWORD_PEPPER_HASH, _toHex(hash));

		_hashGenerator.setPepper(_EMPTY_BYTE_ARRAY);
	}

	@Test
	public void testGenerateHashWithSalt() throws Exception {
		_hashGenerator.setSalt(_USE_SALT.getBytes());

		byte[] hash = _hashGenerator.hash(_PASSWORD.getBytes());

		Assert.assertEquals(_PASSWORD_SALT_HASH, _toHex(hash));

		_hashGenerator.setSalt(_EMPTY_BYTE_ARRAY);
	}

	@Test
	public void testGenerateVariableSizeSalt() throws Exception {
		VariableSizeSaltGenerator variableSizeSaltGenerator =
			(VariableSizeSaltGenerator)_hashGenerator;

		byte[] salt = variableSizeSaltGenerator.generateSalt(_VARIABLE_SIZE);

		Assert.assertTrue(salt.length == _VARIABLE_SIZE);
	}

	private String _toHex(byte[] hash) {
		StringBuilder sb = new StringBuilder(hash.length);

		for (byte b : hash) {
			sb.append(String.format("%02x", b));
		}

		return sb.toString();
	}

	private static final byte[] _EMPTY_BYTE_ARRAY = new byte[0];

	private static final String _MESSAGE_DIGEST_ALGORITHM = "SHA-256";

	private static final String _PASSWORD = "password";

	private static final String _PASSWORD_HASH =
		"5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";

	private static final String _PASSWORD_PEPPER_HASH =
		"1c03943fd7783c66d7b5caef930448dd20bb6af87b61aa64ead5fb0183aebecf";

	private static final String _PASSWORD_SALT_HASH =
		"7a37b85c8918eac19a9089c0fa5a2ab4dce3f90528dcdeec108b23ddf3607b99";

	private static final String _PEPPER = "pepper";

	private static final String _USE_SALT = "salt";

	private static final int _VARIABLE_SIZE = 10;

	private static final MessageDigestHashGeneratorFactory
		_hashGeneratorFactory = new MessageDigestHashGeneratorFactory();

	private HashGenerator _hashGenerator;

}