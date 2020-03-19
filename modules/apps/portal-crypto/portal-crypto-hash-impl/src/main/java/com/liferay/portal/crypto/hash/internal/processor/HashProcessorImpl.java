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

package com.liferay.portal.crypto.hash.internal.processor;

import com.liferay.portal.crypto.hash.generator.spi.HashGenerator;
import com.liferay.portal.crypto.hash.internal.request.salt.command.SaltCommandImpl;
import com.liferay.portal.crypto.hash.internal.response.HashResponseImpl;
import com.liferay.portal.crypto.hash.processor.HashProcessor;
import com.liferay.portal.crypto.hash.request.HashRequest;
import com.liferay.portal.crypto.hash.request.pepper.command.PepperCommand;
import com.liferay.portal.crypto.hash.request.salt.command.SaltCommand;
import com.liferay.portal.crypto.hash.response.HashResponse;

import java.util.function.Function;

/**
 * @author Arthur Chan
 * @author Carlos Sierra Andrés
 */
public class HashProcessorImpl implements HashProcessor {

	public HashProcessorImpl(HashGenerator hashGenerator) {
		_hashGenerator = hashGenerator;
	}

	@Override
	public HashResponse process(HashRequest hashRequest) throws Exception {
		_processPepper(hashRequest);
		_processSalt(hashRequest);

		return new HashResponseImpl(
			Optional.ofNullable(_hashGenerator.getPepper()),
			Optional.ofNullable(_hashGenerator.getSalt()),
			_hashGenerator.hash(hashRequest.getInput()));
	}

	private byte[] _resolvePepperCommand(HashRequest hashRequest) {
		Function<PepperCommand, byte[]> pepperCommand =
			hashRequest.getPepperCommand();

		if (pepperCommand == null) {
			return null;
		}

		byte[] pepper = pepperCommand.apply(
			new PepperCommand() {
			});

		_hashGenerator.setPepper(pepper);

		return pepper;
	}

	private byte[] _resolveSaltCommand(HashRequest hashRequest) {
		Function<SaltCommand, byte[]> saltCommand =
			hashRequest.getSaltCommand();

		if (saltCommand == null) {
			return null;
		}

		byte[] salt = saltCommand.apply(new SaltCommandImpl(_hashGenerator));

		_hashGenerator.setSalt(salt);

		return salt;
	}

	private final HashGenerator _hashGenerator;

}