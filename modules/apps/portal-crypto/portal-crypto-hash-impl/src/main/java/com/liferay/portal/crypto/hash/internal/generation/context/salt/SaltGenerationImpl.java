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

package com.liferay.portal.crypto.hash.internal.generation.context.salt;

import com.liferay.portal.crypto.hash.generation.context.salt.SaltGeneration;
import com.liferay.portal.crypto.hash.provider.spi.HashProvider;
import com.liferay.portal.crypto.hash.provider.spi.salt.VariableSizeSaltProvider;

import java.util.Optional;

/**
 * @author Arthur Chan
 */
public class SaltGenerationImpl implements SaltGeneration {

	public SaltGenerationImpl(HashProvider hashProvider) {
		_hashProvider = hashProvider;
	}

	@Override
	public byte[] generateDefaultSizeSalt() {
		return _hashProvider.generateSalt();
	}

	@Override
	public Optional<byte[]> generateVariableSizeSalt(int size) {
		if (_hashProvider instanceof VariableSizeSaltProvider) {
			VariableSizeSaltProvider variableSizeSaltProvider =
				(VariableSizeSaltProvider)_hashProvider;

			try {
				return Optional.of(variableSizeSaltProvider.generateSalt(size));
			}
			catch (Exception exception) {
				return Optional.empty();
			}
		}

		return Optional.empty();
	}

	private final HashProvider _hashProvider;

}