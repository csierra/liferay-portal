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

package com.liferay.portal.crypto.hash.generator.spi;

import com.liferay.portal.crypto.hash.generator.salt.GenerateVariableSizeSalt;
import com.liferay.portal.kernel.security.SecureRandomUtil;

/**
 * @author Arthur Chan
 */
public abstract class VariableSizeSaltHashGenerator
	extends BaseHashGenerator implements GenerateVariableSizeSalt {

	@Override
	public byte[] generateSalt(int size) {
		byte[] salt = new byte[size];

		for (int i = 0; i < size; ++i) {
			salt[0] = SecureRandomUtil.nextByte();
		}

		return salt;
	}

}