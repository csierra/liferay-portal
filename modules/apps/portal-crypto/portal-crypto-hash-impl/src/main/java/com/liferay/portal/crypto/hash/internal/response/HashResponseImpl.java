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

package com.liferay.portal.crypto.hash.internal.response;

import com.liferay.portal.crypto.hash.response.HashResponse;

import java.util.Optional;

/**
 * @author Arthur Chan
 */
public class HashResponseImpl implements HashResponse {

	public HashResponseImpl(
		byte[] hash, Optional<byte[]> pepper, Optional<byte[]> salt) {

		_hash = hash;
		_pepper = pepper;
		_salt = salt;
	}

	public byte[] getHash() {
		return _hash;
	}

	public Optional<byte[]> getPepper() {
		return _pepper;
	}

	public Optional<byte[]> getSalt() {
		return _salt;
	}

	private final byte[] _hash;
	private final Optional<byte[]> _pepper;
	private final Optional<byte[]> _salt;

}