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

package com.liferay.portal.crypto.hash.processor.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.crypto.hash.processor.HashProcessor;
import com.liferay.portal.crypto.hash.processor.factory.HashProcessorFactory;
import com.liferay.portal.crypto.hash.request.HashRequest;
import com.liferay.portal.crypto.hash.request.salt.command.SaltProvider;
import com.liferay.portal.crypto.hash.response.HashResponse;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Arthur Chan
 * @author Carlos Sierra Andrés
 */
@RunWith(Arquillian.class)
public class HashProcessorTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_hashProcessor = _hashProcessorFactory.createHashProcessor(
			_MESSAGE_DIGEST_ALGORITHM, null);
	}

	@Test
	public void testBuilderCanBeReused() throws Exception {
		HashRequest.InputBuilder inputBuilder = HashRequest.newBuilder(
		).saltProvider(
			saltProvider -> saltProvider.provideVariableSizeSalt(
				_VARIABLE_SIZE
			).orElse(
				null
			)
		);

		HashResponse hashResponse1 = _hashProcessor.process(
			inputBuilder.input(_PASSWORD.getBytes()));
		HashResponse hashResponse2 = _hashProcessor.process(
			inputBuilder.input(_PASSWORD.getBytes()));

		Assert.assertNotEquals(
			hashResponse1.getHash(), hashResponse2.getHash());
		Assert.assertEquals(
			hashResponse1.getPepper(), hashResponse2.getPepper());
		Assert.assertNotEquals(
			hashResponse1.getSalt(), hashResponse2.getSalt());
	}

	@Test
	public void testBuilderCanBeReusedWithPepper() throws Exception {
		HashRequest.InputBuilder inputBuilder = HashRequest.newBuilder(
		).pepper(
			_PEPPER.getBytes()
		).saltProvider(
			saltProvider -> saltProvider.provideVariableSizeSalt(
				_VARIABLE_SIZE
			).orElse(
				null
			)
		);

		HashResponse hashResponse1 = _hashProcessor.process(
			inputBuilder.input(_PASSWORD.getBytes()));

		Optional<byte[]> optionalPepper1 = hashResponse1.getPepper();

		Optional<byte[]> optionalSalt1 = hashResponse1.getSalt();

		HashResponse hashResponse2 = _hashProcessor.process(
			inputBuilder.input(_PASSWORD.getBytes()));

		Optional<byte[]> optionalPepper2 = hashResponse2.getPepper();

		Optional<byte[]> optionalSalt2 = hashResponse2.getSalt();

		Assert.assertFalse(
			Arrays.equals(hashResponse1.getHash(), hashResponse2.getHash()));

		Assert.assertArrayEquals(optionalPepper1.get(), optionalPepper2.get());

		Assert.assertFalse(
			Arrays.equals(optionalSalt1.get(), optionalSalt2.get()));
	}

	@Test
	public void testGenerateDefaultSizeSaltCommandTest() throws Exception {
		HashRequest hashRequest1 = HashRequest.newBuilder(
		).saltProvider(
			SaltProvider::provideDefaultSizeSalt
		).input(
			_PASSWORD.getBytes()
		);

		HashResponse hashResponse1 = _hashProcessor.process(hashRequest1);

		Optional<byte[]> optionalSalt = hashResponse1.getSalt();

		HashRequest hashRequest2 = HashRequest.newBuilder(
		).salt(
			optionalSalt.get()
		).input(
			_PASSWORD.getBytes()
		);

		HashResponse hashResponse2 = _hashProcessor.process(hashRequest2);

		Assert.assertArrayEquals(
			hashResponse1.getHash(), hashResponse2.getHash());
	}

	@Test
	public void testGenerateHashWithoutSaltAndPepperTest() throws Exception {
		HashRequest hashRequest = HashRequest.newBuilder(
		).input(
			_PASSWORD.getBytes()
		);

		HashResponse hashResponse = _hashProcessor.process(hashRequest);

		Assert.assertEquals(_toHex(hashResponse.getHash()), _PASSWORD_HASH);
	}

	@Test
	public void testGenerateVariableSizeSaltCommandTest() throws Exception {
		HashRequest hashRequest1 = HashRequest.newBuilder(
		).saltProvider(
			saltProvider -> saltProvider.provideVariableSizeSalt(
				_VARIABLE_SIZE
			).orElse(
				null
			)
		).input(
			_PASSWORD.getBytes()
		);

		HashResponse hashResponse1 = _hashProcessor.process(hashRequest1);

		Optional<byte[]> optionalSalt = hashResponse1.getSalt();

		HashRequest hashRequest2 = HashRequest.newBuilder(
		).salt(
			optionalSalt.get()
		).input(
			_PASSWORD.getBytes()
		);

		HashResponse hashResponse2 = _hashProcessor.process(hashRequest2);

		Assert.assertArrayEquals(
			hashResponse1.getHash(), hashResponse2.getHash());
	}

	@Test
	public void testMultipleSaltCommandsTest() throws Exception {
		HashRequest hashRequest = HashRequest.newBuilder(
		).salt(
			_SALT.getBytes()
		).input(
			_PASSWORD.getBytes()
		);

		HashResponse hashResponse = _hashProcessor.process(hashRequest);

		Assert.assertEquals(
			_toHex(hashResponse.getHash()), _PASSWORD_SALT_HASH);
	}

	@Test
	public void testPepperCommandTest() throws Exception {
		HashRequest hashRequest = HashRequest.newBuilder(
		).pepper(
			_PEPPER.getBytes()
		).input(
			_PASSWORD.getBytes()
		);

		HashResponse hashResponse = _hashProcessor.process(hashRequest);

		Assert.assertEquals(
			_toHex(hashResponse.getHash()), _PASSWORD_PEPPER_HASH);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testSaltProviderException() throws Exception {
		HashRequest hashRequest = HashRequest.newBuilder(
		).saltProvider(
			saltProvider -> {
				throw new UnsupportedOperationException();
			}
		).input(
			_PASSWORD.getBytes()
		);

		_hashProcessor.process(hashRequest);
	}

	@Test
	public void testUseSaltCommandTest() throws Exception {
		HashRequest hashRequest = HashRequest.newBuilder(
		).salt(
			_SALT.getBytes()
		).input(
			_PASSWORD.getBytes()
		);

		HashResponse hashResponse = _hashProcessor.process(hashRequest);

		Assert.assertEquals(
			_toHex(hashResponse.getHash()), _PASSWORD_SALT_HASH);
	}

	private String _toHex(byte[] hash) {
		StringBuilder sb = new StringBuilder(hash.length);

		for (byte b : hash) {
			sb.append(String.format("%02x", b));
		}

		return sb.toString();
	}

	private static final String _MESSAGE_DIGEST_ALGORITHM = "SHA-256";

	private static final String _PASSWORD = "password";

	private static final String _PASSWORD_HASH =
		"5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";

	private static final String _PASSWORD_PEPPER_HASH =
		"4b65d30b048d9eab292a2ea50fd60423d3d5d581a6ed85169b8a0c4f7dd10c00";

	private static final String _PASSWORD_SALT_HASH =
		"13601bda4ea78e55a07b98866d2be6be0744e3866f13c00c811cab608a28f322";

	private static final String _PEPPER = "pepper";

	private static final String _SALT = "salt";

	private static final int _VARIABLE_SIZE = 10;

	private HashProcessor _hashProcessor;

	@Inject
	private HashProcessorFactory _hashProcessorFactory;

}