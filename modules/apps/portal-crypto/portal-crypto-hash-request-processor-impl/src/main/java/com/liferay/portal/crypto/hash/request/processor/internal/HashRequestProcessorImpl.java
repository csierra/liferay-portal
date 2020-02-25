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
import com.liferay.portal.crypto.hash.request.HashRequest;
import com.liferay.portal.crypto.hash.request.command.pepper.PepperCommand;
import com.liferay.portal.crypto.hash.request.command.salt.GenerateDefaultSizeSaltCommand;
import com.liferay.portal.crypto.hash.request.command.salt.GenerateVariableSizeSaltCommand;
import com.liferay.portal.crypto.hash.request.command.salt.SaltCommand;
import com.liferay.portal.crypto.hash.request.command.salt.SaltCommandVisitor;
import com.liferay.portal.crypto.hash.request.command.salt.UseSaltCommand;
import com.liferay.portal.crypto.hash.request.processor.HashRequestProcessor;
import com.liferay.portal.crypto.hash.response.HashResponse;

/**
 * @author Arthur Chan
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
			SaltCommandVisitor<byte[]> visitor =
				new SaltCommandVisitor<byte[]>() {

					@Override
					public byte[] visit(
						GenerateDefaultSizeSaltCommand command) {

						return _hashGenerator.generateSalt();
					}

					@Override
					public byte[] visit(
						GenerateVariableSizeSaltCommand command) {

						return _hashGenerator.generateSalt(
							command.getSaltSize());
					}

					@Override
					public byte[] visit(UseSaltCommand command) {
						return command.getSalt();
					}

				};

			salt = saltCommand.accept(visitor);

			_hashGenerator.withSalt(salt);
		}

		PepperCommand pepperCommand = hashRequest.getPepperCommand();

		if (pepperCommand != null) {
			_hashGenerator.withPepper(pepperCommand.getPepper());
		}

		byte[] hash = _hashGenerator.hash(hashRequest.getInput());

		HashResponse.Builder builder = new HashResponse.Builder(hash);

		builder.setSalt(salt);

		return builder.build();
	}

	private final HashGenerator _hashGenerator;

}