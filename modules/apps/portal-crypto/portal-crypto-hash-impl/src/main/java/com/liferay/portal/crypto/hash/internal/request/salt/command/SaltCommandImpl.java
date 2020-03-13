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
import com.liferay.portal.crypto.hash.generator.spi.salt.DefaultSizeSaltGenerator;
import com.liferay.portal.crypto.hash.generator.spi.salt.VariableSizeSaltGenerator;
import com.liferay.portal.crypto.hash.request.salt.command.SaltCommand;

/**
 * @author Arthur Chan
 */
public class SaltCommandImpl implements SaltCommand {

	public SaltCommandImpl(HashGenerator hashGenerator) {
		_hashGenerator = hashGenerator;
	}

	@Override
	public byte[] generateDefaultSizeSalt() {
		if (_hashGenerator instanceof DefaultSizeSaltGenerator) {
			DefaultSizeSaltGenerator defaultSizeSaltGenerator =
				(DefaultSizeSaltGenerator)_hashGenerator;

			try {
				return defaultSizeSaltGenerator.generateSalt();
			}
			catch (Exception exception) {
				return null;
			}
		}

		return null;
	}

	@Override
	public byte[] generateVariableSizeSalt(int size) {
		if (_hashGenerator instanceof VariableSizeSaltGenerator) {
			VariableSizeSaltGenerator variableSizeSaltGenerator =
				(VariableSizeSaltGenerator)_hashGenerator;

			try {
				return variableSizeSaltGenerator.generateSalt(size);
			}
			catch (Exception exception) {
				return null;
			}
		}

		return null;
	}

	private final HashGenerator _hashGenerator;

}