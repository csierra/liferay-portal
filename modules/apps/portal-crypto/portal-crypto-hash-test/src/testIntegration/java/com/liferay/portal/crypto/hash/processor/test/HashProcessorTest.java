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
import com.liferay.portal.crypto.hash.request.command.pepper.PepperCommand;
import com.liferay.portal.crypto.hash.request.command.salt.FirstAvailableSaltCommand;
import com.liferay.portal.crypto.hash.request.command.salt.GenerateDefaultSizeSaltCommand;
import com.liferay.portal.crypto.hash.request.command.salt.GenerateVariableSizeSaltCommand;
import com.liferay.portal.crypto.hash.request.command.salt.UseSaltCommand;
import com.liferay.portal.crypto.hash.response.HashResponse;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Arthur Chan
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
	public void testFirstAvailableSaltCommandTest() throws Exception {
		HashRequest hashRequest = HashRequest.Builder.newBuilder(
		).saltCommand(
			new FirstAvailableSaltCommand(
				new FirstAvailableSaltCommand(
					new UseSaltCommand(_USE_SALT.getBytes()),
					new GenerateDefaultSizeSaltCommand(),
					new GenerateVariableSizeSaltCommand(10)),
				new GenerateDefaultSizeSaltCommand())
		).input(
			"password".getBytes()
		);

		HashResponse hashResponse = _hashProcessor.process(hashRequest);

		Assert.assertEquals(
			_toHex(hashResponse.getHash()), _PASSWORD_SALT_HASH);
	}

	@Test
	public void testGenerateDefaultSizeSaltCommandTest() throws Exception {
		HashRequest hashRequest1 = HashRequest.Builder.newBuilder(
		).saltCommand(
			new GenerateDefaultSizeSaltCommand()
		).input(
			"password".getBytes()
		);

		HashResponse hashResponse1 = _hashProcessor.process(hashRequest1);

		Optional<byte[]> generatedSalt = hashResponse1.getSalt();

		HashRequest hashRequest2 = HashRequest.Builder.newBuilder(
		).saltCommand(
			new UseSaltCommand(generatedSalt.get())
		).input(
			"password".getBytes()
		);

		HashResponse hashResponse2 = _hashProcessor.process(hashRequest2);

		Assert.assertArrayEquals(
			hashResponse1.getHash(), hashResponse2.getHash());
	}

	@Test
	public void testGenerateHashWithoutSaltAndPepperTest() throws Exception {
		HashRequest hashRequest = HashRequest.Builder.newBuilder(
		).input(
			"password".getBytes()
		);

		HashResponse hashResponse = _hashProcessor.process(hashRequest);

		Assert.assertEquals(_toHex(hashResponse.getHash()), _PASSWORD_HASH);
	}

	@Test
	public void testGenerateVariableSizeSaltCommandTest() throws Exception {
		HashRequest hashRequest1 = HashRequest.Builder.newBuilder(
		).saltCommand(
			new GenerateVariableSizeSaltCommand(_VARIABLE_SIZE)
		).input(
			"password".getBytes()
		);

		HashResponse hashResponse1 = _hashProcessor.process(hashRequest1);

		Optional<byte[]> generatedSalt = hashResponse1.getSalt();

		HashRequest hashRequest2 = HashRequest.Builder.newBuilder(
		).saltCommand(
			new UseSaltCommand(generatedSalt.get())
		).input(
			"password".getBytes()
		);

		HashResponse hashResponse2 = _hashProcessor.process(hashRequest2);

		Assert.assertArrayEquals(
			hashResponse1.getHash(), hashResponse2.getHash());
	}

	@Test
	public void testPepperCommandTest() throws Exception {
		HashRequest hashRequest = HashRequest.Builder.newBuilder(
		).pepperCommand(
			new PepperCommand(_PEPPER.getBytes())
		).input(
			"password".getBytes()
		);

		HashResponse hashResponse = _hashProcessor.process(hashRequest);

		Assert.assertEquals(
			_toHex(hashResponse.getHash()), _PASSWORD_PEPPER_HASH);
	}

	@Test
	public void testUseSaltCommandTest() throws Exception {
		HashRequest hashRequest = HashRequest.Builder.newBuilder(
		).saltCommand(
			new UseSaltCommand(_USE_SALT.getBytes())
		).input(
			"password".getBytes()
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

	private static final String _PASSWORD_HASH =
		"5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";

	private static final String _PASSWORD_PEPPER_HASH =
		"1c03943fd7783c66d7b5caef930448dd20bb6af87b61aa64ead5fb0183aebecf";

	private static final String _PASSWORD_SALT_HASH =
		"7a37b85c8918eac19a9089c0fa5a2ab4dce3f90528dcdeec108b23ddf3607b99";

	private static final String _PEPPER = "pepper";

	private static final String _USE_SALT = "salt";

	private static final int _VARIABLE_SIZE = 10;

	private HashProcessor _hashProcessor;

	@Inject
	private HashProcessorFactory _hashProcessorFactory;

}