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

package com.liferay.portal.security.crypto.generator.spi;

import com.liferay.portal.kernel.io.BigEndianCodec;
import com.liferay.portal.kernel.security.SecureRandomUtil;

/**
 * @author Carlos Sierra Andrés
 */
public interface VariableLengthSaltGenerator extends SaltGenerator {

	public default byte[] generateSalt(int size) {
		byte[] saltBytes = new byte[size];

		BigEndianCodec.putLong(saltBytes, 0, SecureRandomUtil.nextLong());

		return saltBytes;
	}

}