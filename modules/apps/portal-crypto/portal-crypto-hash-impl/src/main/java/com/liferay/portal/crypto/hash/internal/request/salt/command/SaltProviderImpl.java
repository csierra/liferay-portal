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

package com.liferay.portal.crypto.hash.internal.request.salt.command;

import com.liferay.portal.crypto.hash.generator.spi.HashGenerator;
import com.liferay.portal.crypto.hash.generator.spi.salt.VariableSizeSaltGenerator;
import com.liferay.portal.crypto.hash.request.salt.command.SaltProvider;

import java.util.Optional;

/**
 * @author Arthur Chan
 */
public class SaltProviderImpl implements SaltProvider {

	public SaltProviderImpl(HashGenerator hashGenerator) {
		_hashGenerator = hashGenerator;
	}

	@Override
	public byte[] provideDefaultSizeSalt() {
		return _hashGenerator.generateSalt();
	}

	@Override
	public Optional<byte[]> provideVariableSizeSalt(int size) {
		if (_hashGenerator instanceof VariableSizeSaltGenerator) {
			VariableSizeSaltGenerator variableSizeSaltGenerator =
				(VariableSizeSaltGenerator)_hashGenerator;

			try {
				return Optional.of(
					variableSizeSaltGenerator.generateSalt(size));
			}
			catch (Exception exception) {
				return Optional.empty();
			}
		}

		return Optional.empty();
	}

	private final HashGenerator _hashGenerator;

}