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

package com.liferay.portal.security.crypto.generator.salt;

import com.liferay.portal.kernel.io.BigEndianCodec;
import com.liferay.portal.kernel.security.SecureRandomUtil;
import com.liferay.portal.kernel.util.Base64;

/**
 * @author arthurchan35
 */
public class DefaultSaltGenerator {

	public static String generate() {
		byte[] saltBytes = generateBytes();

		return Base64.encode(saltBytes);
	}

	public static byte[] generateBytes() {
		byte[] saltBytes = new byte[Long.BYTES];

		BigEndianCodec.putLong(saltBytes, 0, SecureRandomUtil.nextLong());

		return saltBytes;
	}

}