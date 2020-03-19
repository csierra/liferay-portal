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

/**
 * @author Arthur Chan
 */
public abstract class BaseHashGenerator implements HashGenerator {

	@Override
	public byte[] getPepper() {
		return pepper;
	}

	@Override
	public byte[] getSalt() {
		return salt;
	}

	@Override
	public void setPepper(byte[] pepper) {
		if (pepper == null) {
			throw new IllegalArgumentException("pepper can not be null");
		}

		this.pepper = pepper;
	}

	@Override
	public void setSalt(byte[] salt) {
		if (salt == null) {
			throw new IllegalArgumentException("salt can not be null");
		}

		this.salt = salt;
	}

	protected byte[] pepper = new byte[0];
	protected byte[] salt = new byte[0];

}