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
import com.liferay.portal.crypto.hash.generator.spi.salt.VariableSizeSaltGenerator;
import com.liferay.portal.crypto.hash.internal.response.HashResponseImpl;
import com.liferay.portal.crypto.hash.processor.HashProcessor;
import com.liferay.portal.crypto.hash.request.HashRequest;
import com.liferay.portal.crypto.hash.request.command.pepper.UsePepperCommand;
import com.liferay.portal.crypto.hash.request.command.salt.FirstAvailableSaltCommand;
import com.liferay.portal.crypto.hash.request.command.salt.GenerateDefaultSizeSaltCommand;
import com.liferay.portal.crypto.hash.request.command.salt.GenerateVariableSizeSaltCommand;
import com.liferay.portal.crypto.hash.request.command.salt.SaltCommand;
import com.liferay.portal.crypto.hash.request.command.salt.SaltCommandVisitor;
import com.liferay.portal.crypto.hash.request.command.salt.UseSaltCommand;
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
		Optional<UsePepperCommand> optionalPepperCommand =
			hashRequest.getPepperCommand();

		Optional<byte[]> optionalPepper = optionalPepperCommand.map(
			UsePepperCommand::getPepper);

		optionalPepper.ifPresent(_hashGenerator::setPepper);

		Optional<SaltCommand> optionalSaltCommand =
			hashRequest.getSaltCommand();

		Optional<byte[]> optionalSalt = optionalSaltCommand.map(
			saltCommand -> saltCommand.accept(new GetSaltCommandVisitor()));

		optionalSalt.ifPresent(_hashGenerator::setSalt);

		return new HashResponseImpl(
			optionalPepper, optionalSalt,
			_hashGenerator.hash(hashRequest.getInput()));
	}

	private final HashGenerator _hashGenerator;

	private class GetSaltCommandVisitor implements SaltCommandVisitor<byte[]> {

		@Override
		public byte[] visit(
			FirstAvailableSaltCommand firstAvailableSaltCommand) {

			SaltCommandVisitor<byte[]> visitor = new GetSaltCommandVisitor();

			for (SaltCommand each :
					firstAvailableSaltCommand.getSaltCommands()) {

				byte[] salt;

				try {
					salt = each.accept(visitor);
				}
				catch (Exception exception) {
					continue;
				}

				if (salt != null) {
					return salt;
				}
			}

			throw new UnsupportedOperationException();
		}

		@Override
		public byte[] visit(
			GenerateDefaultSizeSaltCommand generateDefaultSizeSaltCommand) {

			return _hashGenerator.generateSalt();
		}

		@Override
		public byte[] visit(
			GenerateVariableSizeSaltCommand generateVariableSizeSaltCommand) {

			if (_hashGenerator instanceof VariableSizeSaltGenerator) {
				VariableSizeSaltGenerator generateVariableSizeSalt =
					(VariableSizeSaltGenerator)_hashGenerator;

				return generateVariableSizeSalt.generateSalt(
					generateVariableSizeSaltCommand.getSaltSize());
			}

			return null;
		}

		@Override
		public byte[] visit(UseSaltCommand useSaltCommand) {
			return useSaltCommand.getSalt();
		}

	}

}