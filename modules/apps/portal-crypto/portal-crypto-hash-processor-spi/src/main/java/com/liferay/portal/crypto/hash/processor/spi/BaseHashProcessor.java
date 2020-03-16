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

package com.liferay.portal.crypto.hash.processor.spi;

import com.liferay.portal.crypto.hash.processor.HashProcessor;
import com.liferay.portal.crypto.hash.processor.spi.salt.SaltGenerator;
import com.liferay.portal.crypto.hash.processor.spi.salt.VariableSizeSaltGenerator;
import com.liferay.portal.crypto.hash.request.HashRequest;
import com.liferay.portal.crypto.hash.request.command.pepper.PepperCommand;
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
 */
public abstract class BaseHashProcessor
	implements HashProcessor, SaltGenerator {

	@Override
	public final HashResponse process(HashRequest hashRequest)
		throws Exception {

		Optional<PepperCommand> optionalPepperCommand =
			hashRequest.getPepperCommand();

		Optional<byte[]> optionalPepper = optionalPepperCommand.map(
			UsePepperCommand.class::cast
		).map(
			UsePepperCommand::getPepper
		);

		optionalPepper.ifPresent(this::_setPepper);

		Optional<SaltCommand> optionalSaltCommand =
			hashRequest.getSaltCommand();

		Optional<byte[]> optionalSalt = optionalSaltCommand.map(
			SaltCommand.class::cast
		).map(
			saltCommand -> saltCommand.accept(_getSaltCommandVisitor)
		);

		optionalSalt.ifPresent(this::_setSalt);

		HashResponse.Builder builder = new HashResponse.Builder(
			hash(hashRequest.getInput()));

		return builder.build();
	}

	protected abstract byte[] hash(byte[] input) throws Exception;

	protected byte[] pepper = new byte[0];
	protected byte[] salt = new byte[0];

	private void _setPepper(byte[] pepper) {
		this.pepper = pepper;
	}

	private void _setSalt(byte[] salt) {
		this.salt = salt;
	}

	private final GetSaltCommandVisitor _getSaltCommandVisitor =
		new GetSaltCommandVisitor();

	private class GetSaltCommandVisitor implements SaltCommandVisitor<byte[]> {

		@Override
		public byte[] visit(
			FirstAvailableSaltCommand firstAvailableSaltCommand) {

			for (SaltCommand saltCommand :
					firstAvailableSaltCommand.getSaltCommands()) {

				byte[] salt;

				try {
					final SaltCommand nextSaltCommand =
						(SaltCommand)saltCommand;

					salt = nextSaltCommand.accept(this);
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

			return BaseHashProcessor.this.generateSalt();
		}

		@Override
		public byte[] visit(
			GenerateVariableSizeSaltCommand generateVariableSizeSaltCommand) {

			if (this instanceof VariableSizeSaltGenerator) {
				VariableSizeSaltGenerator generateVariableSizeSalt =
					(VariableSizeSaltGenerator)BaseHashProcessor.this;

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