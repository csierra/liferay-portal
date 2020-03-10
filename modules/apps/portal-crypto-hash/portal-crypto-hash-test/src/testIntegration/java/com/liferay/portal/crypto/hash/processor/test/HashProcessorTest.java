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
import com.liferay.portal.crypto.hash.context.builder.HashContextBuilder;
import com.liferay.portal.crypto.hash.generation.context.HashGenerationContext;
import com.liferay.portal.crypto.hash.generation.context.salt.SaltCommand;
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
	public void testHashGenerationAndHashVerificationWithMultipleContexts()
		throws Exception {

		HashContextBuilder hashContextBuilder =
			_hashProcessor.createHashContextBuilder();

		HashGenerationContext hashGenerationContext1 =
			hashContextBuilder.generationHashProvider(
				_MESSAGE_DIGEST_ALGO_1
			).pepper(
				_PEPPER.getBytes()
			).saltCommand(
				SaltCommand.generateDefaultSizeSalt()
			).build();

		HashGenerationResponse hashGenerationResponse1 =
			_hashProcessor.generate(
				_PASSWORD.getBytes(), hashGenerationContext1);

		Optional<byte[]> optionalSalt1 = hashGenerationResponse1.getSalt();

		HashGenerationContext hashGenerationContext2 =
			hashContextBuilder.generationHashProvider(
				_MESSAGE_DIGEST_ALGO_2
			).pepper(
				_PEPPER.getBytes()
			).saltCommand(
				SaltCommand.generateVariableSizeSalt(_VARIABLE_SIZE)
			).build();

		HashGenerationResponse hashGenerationResponse2 =
			_hashProcessor.generate(
				hashGenerationResponse1.getHash(), hashGenerationContext2);

		Optional<byte[]> optionalSalt2 = hashGenerationResponse2.getSalt();

		HashVerificationContext hashVerificationContext1 =
			hashContextBuilder.verificationHashProvider(
				_MESSAGE_DIGEST_ALGO_1
			).pepper(
				_PEPPER.getBytes()
			).salt(
				optionalSalt1.get()
			).build();

		HashVerificationContext hashVerificationContext2 =
			hashContextBuilder.verificationHashProvider(
				_MESSAGE_DIGEST_ALGO_2
			).pepper(
				_PEPPER.getBytes()
			).salt(
				optionalSalt2.get()
			).build();

		Assert.assertTrue(
			_hashProcessor.verify(
				_PASSWORD.getBytes(), hashGenerationResponse2.getHash(),
				hashVerificationContext1, hashVerificationContext2));

		Assert.assertFalse(
			_hashProcessor.verify(
				_WRONG_PASSWORD.getBytes(), hashGenerationResponse2.getHash(),
				hashVerificationContext1, hashVerificationContext2));
	}

	@Test
	public void testHashGenerationDefaultSizeSalt() throws Exception {
		HashContextBuilder hashContextBuilder =
			_hashProcessor.createHashContextBuilder();

		HashGenerationContext hashGenerationContext =
			hashContextBuilder.generationHashProvider(
				_MESSAGE_DIGEST_ALGO_1
			).pepper(
				_PEPPER.getBytes()
			).saltCommand(
				SaltCommand.generateDefaultSizeSalt()
			).build();

		_testHashGenerationCommon(hashGenerationContext);
	}

	@Test
	public void testHashGenerationVariableSizeSalt() throws Exception {
		HashContextBuilder hashContextBuilder =
			_hashProcessor.createHashContextBuilder();

		HashGenerationContext hashGenerationContext =
			hashContextBuilder.generationHashProvider(
				_MESSAGE_DIGEST_ALGO_1
			).pepper(
				_PEPPER.getBytes()
			).saltCommand(
				SaltCommand.generateVariableSizeSalt(_VARIABLE_SIZE)
			).build();

		_testHashGenerationCommon(hashGenerationContext);
	}

	@Test
	public void testHashGenerationWithoutPepperWithDefaultSizeSalt()
		throws Exception {

		HashContextBuilder hashContextBuilder =
			_hashProcessor.createHashContextBuilder();

		HashGenerationContext hashGenerationContext =
			hashContextBuilder.generationHashProvider(
				_MESSAGE_DIGEST_ALGO_1
			).saltCommand(
				SaltCommand.generateDefaultSizeSalt()
			).build();

		_testHashGenerationCommon(hashGenerationContext);
	}

	@Test
	public void testHashGenerationWithoutPepperWithoutSalt() throws Exception {
		HashContextBuilder hashContextBuilder =
			_hashProcessor.createHashContextBuilder();

		HashGenerationContext hashGenerationContext =
			hashContextBuilder.generationHashProvider(
				_MESSAGE_DIGEST_ALGO_1
			).build();

		_testHashGenerationCommon(hashGenerationContext);
	}

	@Test
	public void testHashGenerationWithPepperWithoutSalt() throws Exception {
		HashContextBuilder hashContextBuilder =
			_hashProcessor.createHashContextBuilder();

		HashGenerationContext hashGenerationContext =
			hashContextBuilder.generationHashProvider(
				_MESSAGE_DIGEST_ALGO_1
			).pepper(
				_PEPPER.getBytes()
			).build();

		_testHashGenerationCommon(hashGenerationContext);
	}

	@Test
	public void testHashVerification() throws Exception {
		HashContextBuilder hashContextBuilder =
			_hashProcessor.createHashContextBuilder();

		HashVerificationContext hashVerificationContext1 =
			hashContextBuilder.verificationHashProvider(
				_MESSAGE_DIGEST_ALGO_1
			).pepper(
				_PEPPER.getBytes()
			).salt(
				_SALT_1.getBytes()
			).build();

		HashVerificationContext hashVerificationContext2 =
			hashContextBuilder.verificationHashProvider(
				_MESSAGE_DIGEST_ALGO_2
			).pepper(
				_PEPPER.getBytes()
			).salt(
				_SALT_2.getBytes()
			).build();

		Assert.assertTrue(
			_hashProcessor.verify(
				_PASSWORD.getBytes(), _FINAL_HASH, hashVerificationContext1,
				hashVerificationContext2));

		Assert.assertFalse(
			_hashProcessor.verify(
				_WRONG_PASSWORD.getBytes(), _FINAL_HASH,
				hashVerificationContext1, hashVerificationContext2));
	}

	@Test
	public void testReusableHashGenerationContextBuilder() throws Exception {
		HashContextBuilder hashContextBuilder =
			_hashProcessor.createHashContextBuilder();

		HashGenerationContext.Builder hashProviderBuilder =
			hashContextBuilder.generationHashProvider(
				_MESSAGE_DIGEST_ALGO_1
			).pepper(
				_PEPPER.getBytes()
			).saltCommand(
				SaltCommand.generateDefaultSizeSalt()
			);

		HashGenerationResponse hashGenerationResponse1 =
			_hashProcessor.generate(
				_PASSWORD.getBytes(), hashProviderBuilder.build());

		HashGenerationResponse hashGenerationResponse2 =
			_hashProcessor.generate(
				_PASSWORD.getBytes(), hashProviderBuilder.build());

		Optional<byte[]> optionalSalt1 = hashGenerationResponse1.getSalt();

		Optional<byte[]> optionalSalt2 = hashGenerationResponse2.getSalt();

		Assert.assertFalse(
			Arrays.equals(optionalSalt1.get(), optionalSalt2.get()));

		Assert.assertFalse(
			Arrays.equals(
				hashGenerationResponse1.getHash(),
				hashGenerationResponse2.getHash()));
	}

	@Test
	public void testReusableHashGenerationContextBuilderWithDifferentSalts()
		throws Exception {

		HashContextBuilder hashContextBuilder =
			_hashProcessor.createHashContextBuilder();

		HashGenerationContext.SaltCommandBuilder saltCommandBuilder =
			hashContextBuilder.generationHashProvider(
				_MESSAGE_DIGEST_ALGO_1
			).pepper(
				_PEPPER.getBytes()
			);

		HashGenerationResponse hashGenerationResponse1 =
			_hashProcessor.generate(
				_PASSWORD.getBytes(),
				saltCommandBuilder.saltCommand(
					SaltCommand.generateDefaultSizeSalt()
				).build());

		HashGenerationResponse hashGenerationResponse2 =
			_hashProcessor.generate(
				_PASSWORD.getBytes(), saltCommandBuilder.build());

		Optional<byte[]> optionalSalt1 = hashGenerationResponse1.getSalt();
		Optional<byte[]> optionalSalt2 = hashGenerationResponse2.getSalt();

		Assert.assertTrue(optionalSalt1.isPresent());
		Assert.assertFalse(optionalSalt2.isPresent());

		Assert.assertFalse(
			Arrays.equals(
				hashGenerationResponse1.getHash(),
				hashGenerationResponse2.getHash()));
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
			HashGenerationContext hashGenerationContext)
		throws Exception {

		Optional<byte[]> optionalPepper = hashGenerationContext.getPepper();

		HashGenerationResponse hashGenerationResponse = _hashProcessor.generate(
			_PASSWORD.getBytes(), hashGenerationContext);

		Optional<byte[]> optionalSalt = hashGenerationResponse.getSalt();

		HashContextBuilder hashContextBuilder =
			_hashProcessor.createHashContextBuilder();

		HashVerificationContext hashVerificationContext =
			hashContextBuilder.verificationHashProvider(
				_MESSAGE_DIGEST_ALGO_1
			).pepper(
				optionalPepper.orElse(new byte[0])
			).salt(
				optionalSalt.orElse(new byte[0])
			).build();

		Assert.assertTrue(
			_hashProcessor.verify(
				_PASSWORD.getBytes(), hashGenerationResponse.getHash(),
				hashVerificationContext));

		Assert.assertFalse(
			_hashProcessor.verify(
				_WRONG_PASSWORD.getBytes(), hashGenerationResponse.getHash(),
				hashVerificationContext));
	}

	private static final byte[] _FINAL_HASH = _hexToBytes(
		"5d4f56d5d78b4d7854e2943db62c33b83b12a540c7d2c6769332ea6cd6d8b786" +
			"817660a273f32cf7d82ad592081941fa096a83c60dec5feed6065abf4b98ab86");

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