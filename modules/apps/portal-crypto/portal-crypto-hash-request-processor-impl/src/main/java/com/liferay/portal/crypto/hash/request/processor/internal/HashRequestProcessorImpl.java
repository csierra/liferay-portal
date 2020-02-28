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

package com.liferay.portal.crypto.hash.request.processor.internal;

import com.liferay.portal.crypto.hash.generator.HashGenerator;
import com.liferay.portal.crypto.hash.generator.salt.GenerateVariableSizeSalt;
import com.liferay.portal.crypto.hash.request.HashRequest;
import com.liferay.portal.crypto.hash.request.command.pepper.PepperCommand;
import com.liferay.portal.crypto.hash.request.command.salt.FirstAvailableSaltCommand;
import com.liferay.portal.crypto.hash.request.command.salt.GenerateDefaultSizeSaltCommand;
import com.liferay.portal.crypto.hash.request.command.salt.GenerateVariableSizeSaltCommand;
import com.liferay.portal.crypto.hash.request.command.salt.SaltCommand;
import com.liferay.portal.crypto.hash.request.command.salt.SaltCommandVisitor;
import com.liferay.portal.crypto.hash.request.command.salt.UseSaltCommand;
import com.liferay.portal.crypto.hash.request.processor.HashRequestProcessor;
import com.liferay.portal.crypto.hash.response.HashResponse;

/**
 * @author Arthur Chan
 * @author Carlos Sierra Andrés
 */
public class HashRequestProcessorImpl implements HashRequestProcessor {

	public HashRequestProcessorImpl(HashGenerator hashGenerator) {
		_hashGenerator = hashGenerator;
	}

	@Override
	public HashResponse process(HashRequest hashRequest) throws Exception {
		SaltCommand saltCommand = hashRequest.getSaltCommand();

		byte[] salt = null;

		if (saltCommand != null) {
			SaltCommandVisitor<byte[]> visitor = new GetSaltCommandVisitor();

			salt = saltCommand.accept(visitor);

			_hashGenerator.withSalt(salt);
		}

		PepperCommand pepperCommand = hashRequest.getPepperCommand();

		byte[] pepper = pepperCommand.getPepper();

		if (pepperCommand != null) {
			_hashGenerator.withPepper(pepper);
		}

		byte[] hash = _hashGenerator.hash(hashRequest.getInput());

		HashResponse.Builder builder = new HashResponse.Builder(hash);

		builder.setSalt(salt);

		builder.setPepper(pepper);

		return builder.build();
	}

	private final HashGenerator _hashGenerator;

	private class GetSaltCommandVisitor implements SaltCommandVisitor<byte[]> {

		@Override
		public byte[] visit(FirstAvailableSaltCommand command)
			throws Exception {

			SaltCommandVisitor<byte[]> visitor = new GetSaltCommandVisitor();

			for (SaltCommand each : command.getSaltCommands()) {
				try {
					return each.accept(visitor);
				}
				catch (Exception exception) {
				}
			}

			throw new Exception("No available Salt Command");
		}

		@Override
		public byte[] visit(GenerateDefaultSizeSaltCommand command)
			throws Exception {

			return _hashGenerator.generateSalt();
		}

		@Override
		public byte[] visit(GenerateVariableSizeSaltCommand command)
			throws Exception {

			GenerateVariableSizeSalt generateVariableSizeSalt =
				(GenerateVariableSizeSalt)_hashGenerator;

			return generateVariableSizeSalt.generateSalt(command.getSaltSize());
		}

		@Override
		public byte[] visit(UseSaltCommand command) throws Exception {
			return command.getSalt();
		}

	}

}