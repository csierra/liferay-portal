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

package com.liferay.portal.crypto.hash.internal.generation.response;

import com.liferay.portal.crypto.hash.generation.response.HashGenerationResponse;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Arthur Chan
 */
public class HashGenerationResponseImpl implements HashGenerationResponse {

	public HashGenerationResponseImpl(Optional<byte[]> salt, byte[] hash) {
		_salt = salt;
		_hash = hash;
	}

	public byte[] getHash() {
		return Arrays.copyOf(_hash, _hash.length);
	}

	public Optional<byte[]> getSalt() {
		return _salt;
	}

	private final byte[] _hash;
	private final Optional<byte[]> _salt;

}