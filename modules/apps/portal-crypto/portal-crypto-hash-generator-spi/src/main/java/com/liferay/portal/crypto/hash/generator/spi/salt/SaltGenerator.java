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

package com.liferay.portal.crypto.hash.generator.spi.salt;

import com.liferay.portal.kernel.io.BigEndianCodec;
import com.liferay.portal.kernel.security.SecureRandomUtil;

import org.osgi.annotation.versioning.ConsumerType;

/**
 * @author Carlos Sierra Andrés
 */
@ConsumerType
public interface SaltGenerator {

	public default byte[] generateSalt() {
		byte[] salt = new byte[Long.BYTES];

		BigEndianCodec.putLong(salt, 0, SecureRandomUtil.nextLong());

		return salt;
	}

}