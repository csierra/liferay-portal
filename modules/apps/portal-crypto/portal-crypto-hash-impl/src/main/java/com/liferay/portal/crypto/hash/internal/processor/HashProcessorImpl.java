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
import com.liferay.portal.crypto.hash.internal.request.command.pepper.UsePepperCommand;
import com.liferay.portal.crypto.hash.internal.request.command.salt.BaseSaltCommand;
import com.liferay.portal.crypto.hash.internal.response.HashResponseImpl;
import com.liferay.portal.crypto.hash.processor.HashProcessor;
import com.liferay.portal.crypto.hash.request.HashRequest;
import com.liferay.portal.crypto.hash.request.command.pepper.PepperCommand;
import com.liferay.portal.crypto.hash.request.command.salt.SaltCommand;
import com.liferay.portal.crypto.hash.response.HashResponse;

import java.util.Optional;

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
		Optional<PepperCommand> optionalPepperCommand =
			hashRequest.getPepperCommand();

		Optional<byte[]> optionalPepper = optionalPepperCommand.map(
			UsePepperCommand.class::cast
		).map(
			UsePepperCommand::getPepper
		);

		optionalPepper.ifPresent(_hashGenerator::setPepper);

		Optional<SaltCommand> optionalSaltCommand =
			hashRequest.getSaltCommand();

		Optional<byte[]> optionalSalt = optionalSaltCommand.map(
			BaseSaltCommand.class::cast
		).map(
			baseSaltCommand -> baseSaltCommand.getSalt(_hashGenerator)
		);

		optionalSalt.ifPresent(_hashGenerator::setSalt);

		return new HashResponseImpl(
			optionalPepper, optionalSalt,
			_hashGenerator.hash(hashRequest.getInput()));
	}

	private final HashGenerator _hashGenerator;

}