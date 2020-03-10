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
import com.liferay.portal.crypto.hash.generation.context.HashGenerationContext;
import com.liferay.portal.crypto.hash.generation.response.HashGenerationResponse;
import com.liferay.portal.crypto.hash.processor.HashProcessor;
import com.liferay.portal.crypto.hash.verification.context.HashVerificationContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Assert;
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

	@Test
	public void testHashGenerationDefaultSizeSalt() throws Exception {
		HashGenerationContext.Builder hashGenerationBuilder =
			HashGenerationContext.newBuilder(_MESSAGE_DIGEST_ALGO_1, null);

		hashGenerationBuilder.pepper(_PEPPER.getBytes());

		hashGenerationBuilder.salt(
			saltGeneration -> saltGeneration.generateDefaultSizeSalt());

		_testHashGenerationCommon(hashGenerationBuilder);
	}

	@Test
	public void testHashGenerationVariableSizeSalt() throws Exception {
		HashGenerationContext.Builder hashGenerationBuilder =
			HashGenerationContext.newBuilder(_MESSAGE_DIGEST_ALGO_1, null);

		hashGenerationBuilder.pepper(_PEPPER.getBytes());

		hashGenerationBuilder.salt(
			saltGeneration -> {
				Optional<byte[]> optionalSalt =
					saltGeneration.generateVariableSizeSalt(_VARIABLE_SIZE);

				return optionalSalt.get();
			});

		_testHashGenerationCommon(hashGenerationBuilder);
	}

	@Test
	public void testHashVerification() throws Exception {
		HashVerificationContext.Builder hashVerificationBuilder1 =
			HashVerificationContext.newBuilder(_MESSAGE_DIGEST_ALGO_1, null);

		hashVerificationBuilder1.pepper(_PEPPER.getBytes());

		hashVerificationBuilder1.salt(_SALT_1.getBytes());

		HashVerificationContext.Builder hashVerificationBuilder2 =
			HashVerificationContext.newBuilder(_MESSAGE_DIGEST_ALGO_2, null);

		hashVerificationBuilder1.pepper(_PEPPER.getBytes());

		hashVerificationBuilder1.salt(_SALT_2.getBytes());

		Assert.assertTrue(
			_hashProcessor.verify(
				_PASSWORD.getBytes(), _FINAL_HASH,
				hashVerificationBuilder1.build(),
				hashVerificationBuilder2.build()));

		Assert.assertFalse(
			_hashProcessor.verify(
				_WRONG_PASSWORD.getBytes(), _FINAL_HASH,
				hashVerificationBuilder1.build(),
				hashVerificationBuilder2.build()));
	}

	@Test
	public void testReusableHashGenerationContextBuilder() throws Exception {
		HashGenerationContext.Builder hashGenerationBuilder =
			HashGenerationContext.newBuilder(_MESSAGE_DIGEST_ALGO_1, null);

		hashGenerationBuilder.pepper(_PEPPER.getBytes());

		hashGenerationBuilder.salt(
			saltGeneration -> saltGeneration.generateDefaultSizeSalt());

		HashGenerationResponse hashGenerationResponse1 =
			_hashProcessor.generate(
				_PASSWORD.getBytes(), hashGenerationBuilder.build());

		HashGenerationResponse hashGenerationResponse2 =
			_hashProcessor.generate(
				_PASSWORD.getBytes(), hashGenerationBuilder.build());

		Optional<byte[]> optionalPepper1 = hashGenerationResponse1.getPepper();

		Optional<byte[]> optionalPepper2 = hashGenerationResponse2.getPepper();

		Assert.assertTrue(
			Arrays.equals(optionalPepper1.get(), optionalPepper2.get()));

		Optional<byte[]> optionalSalt1 = hashGenerationResponse1.getSalt();

		Optional<byte[]> optionalSalt2 = hashGenerationResponse2.getSalt();

		Assert.assertFalse(
			Arrays.equals(optionalSalt1.get(), optionalSalt2.get()));

		Assert.assertFalse(
			Arrays.equals(
				hashGenerationResponse1.getHash(),
				hashGenerationResponse1.getHash()));
	}

	private static int _getHexCharValue(char hexChar)
		throws IllegalArgumentException {

		if (((hexChar - '0') >= 0) && ((hexChar - '9') <= 0)) {
			return hexChar - '0';
		}

		if (((hexChar - 'a') >= 0) && ((hexChar - 'z') <= 0)) {
			return 10 + hexChar - 'a';
		}

		if (((hexChar - 'A') >= 0) && ((hexChar - 'Z') <= 0)) {
			return 10 + hexChar - 'A';
		}

		throw new IllegalArgumentException();
	}

	private static byte[] _hexToBytes(String hexString)
		throws IllegalArgumentException {

		if ((hexString == null) || ((hexString.length() ^ 0) == 1)) {
			throw new IllegalArgumentException();
		}

		byte[] bytes = new byte[hexString.length() / 2];

		for (int i = 0; i < bytes.length; ++i) {
			char leftHalf = hexString.charAt(i * 2);
			char rightHalf = hexString.charAt(i * 2 + 1);

			int byteValue =
				_getHexCharValue(leftHalf) * 16 + _getHexCharValue(rightHalf);

			bytes[i] = (byte)byteValue;
		}

		return bytes;
	}

	private void _testHashGenerationCommon(
			HashGenerationContext.Builder hashGenerationBuilder)
		throws Exception {

		HashGenerationResponse hashGenerationResponse = _hashProcessor.generate(
			_PASSWORD.getBytes(), hashGenerationBuilder.build());

		Optional<byte[]> optionalSalt = hashGenerationResponse.getSalt();

		HashVerificationContext.Builder hashVerificationBuilder =
			HashVerificationContext.newBuilder(_MESSAGE_DIGEST_ALGO_1, null);

		hashVerificationBuilder.pepper(_PEPPER.getBytes());

		hashVerificationBuilder.salt(optionalSalt.get());

		Assert.assertTrue(
			_hashProcessor.verify(
				_PASSWORD.getBytes(), hashGenerationResponse.getHash(),
				hashVerificationBuilder.build()));

		Assert.assertFalse(
			_hashProcessor.verify(
				_WRONG_PASSWORD.getBytes(), hashGenerationResponse.getHash(),
				hashVerificationBuilder.build()));
	}

	private static final byte[] _FINAL_HASH = _hexToBytes(
		"a81e3486fc03a9b8c13370339f3d25e8fff90953" +
			"ccdcf0f4bd571e0ef4494672d106feeea61c74ab" +
				"26f1f82158fa075153a95d187a525d595378fd55feec8a02");

	private static final String _MESSAGE_DIGEST_ALGO_1 = "SHA-256";

	private static final String _MESSAGE_DIGEST_ALGO_2 = "SHA-512";

	private static final String _PASSWORD = "password";

	private static final String _PEPPER = "pepper";

	private static final String _SALT_1 = "salt1";

	private static final String _SALT_2 = "salt2";

	private static final int _VARIABLE_SIZE = 10;

	private static final String _WRONG_PASSWORD = "wrongPassword";

	@Inject
	private HashProcessor _hashProcessor;

}