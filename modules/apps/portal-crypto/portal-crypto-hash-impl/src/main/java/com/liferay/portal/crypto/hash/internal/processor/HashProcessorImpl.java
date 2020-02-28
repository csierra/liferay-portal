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
import com.liferay.portal.crypto.hash.generator.spi.salt.GenerateVariableSizeSalt;
import com.liferay.portal.crypto.hash.internal.response.HashResponseImpl;
import com.liferay.portal.crypto.hash.processor.HashProcessor;
import com.liferay.portal.crypto.hash.request.HashRequest;
import com.liferay.portal.crypto.hash.request.command.pepper.PepperCommand;
import com.liferay.portal.crypto.hash.request.command.salt.FirstAvailableSaltCommand;
import com.liferay.portal.crypto.hash.request.command.salt.GenerateDefaultSizeSaltCommand;
import com.liferay.portal.crypto.hash.request.command.salt.GenerateVariableSizeSaltCommand;
import com.liferay.portal.crypto.hash.request.command.salt.SaltCommand;
import com.liferay.portal.crypto.hash.request.command.salt.SaltCommandVisitor;
import com.liferay.portal.crypto.hash.request.command.salt.UseSaltCommand;
import com.liferay.portal.crypto.hash.response.HashResponse;

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
		SaltCommand saltCommand = hashRequest.getSaltCommand();

		byte[] salt = null;

		if (saltCommand != null) {
			SaltCommandVisitor<byte[]> saltCommandVisitor =
				new GetSaltCommandVisitor();

			salt = saltCommand.accept(saltCommandVisitor);

			_hashGenerator.setSalt(salt);
		}

		PepperCommand pepperCommand = hashRequest.getPepperCommand();

		byte[] pepper = pepperCommand.getPepper();

		if (pepperCommand != null) {
			_hashGenerator.setPepper(pepper);
		}

		byte[] hash = _hashGenerator.hash(hashRequest.getInput());

		return new HashResponseImpl(hash, pepper, salt);
	}

	private final HashGenerator _hashGenerator;

	private class GetSaltCommandVisitor implements SaltCommandVisitor<byte[]> {

		@Override
		public byte[] visit(
			FirstAvailableSaltCommand firstAvailableSaltCommand) {

			SaltCommandVisitor<byte[]> visitor = new GetSaltCommandVisitor();

			for (SaltCommand each :
					firstAvailableSaltCommand.getSaltCommands()) {

				return each.accept(visitor);
			}

			return null;
		}

		@Override
		public byte[] visit(
			GenerateDefaultSizeSaltCommand generateDefaultSizeSaltCommand) {

			return _hashGenerator.generateSalt();
		}

		@Override
		public byte[] visit(
			GenerateVariableSizeSaltCommand generateVariableSizeSaltCommand) {

			if (_hashGenerator instanceof GenerateVariableSizeSalt) {
				GenerateVariableSizeSalt generateVariableSizeSalt =
					(GenerateVariableSizeSalt)_hashGenerator;

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