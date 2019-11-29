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

import aQute.bnd.annotation.ProviderType;

/**
 * @author Arthur Chan
 */
@ProviderType
public interface HashGenerator extends Generator {

	public HashGenerator withPepper(byte[] pepper) throws Exception;

	public default HashGenerator withPepper(String pepper) throws Exception {
		if (pepper == null) {
			throw new IllegalArgumentException("pepper can not be null");
		}

		return withPepper(pepper.getBytes());
	}

	public HashGenerator withSalt(byte[] salt) throws Exception;

	public default HashGenerator withSalt(String salt) throws Exception {
		if (salt == null) {
			throw new IllegalArgumentException("salt can not be null");
		}

		return withSalt(salt.getBytes());
	}

	public String generateSalt() throws Exception;

	public byte[] hash(byte[] toBeHashed) throws Exception;

	public default byte[] hash(String toBeHashed) throws Exception {
		if (toBeHashed == null) {
			throw new IllegalArgumentException("toBeHashed can not be null");
		}

		return hash(toBeHashed.getBytes());
	}

}