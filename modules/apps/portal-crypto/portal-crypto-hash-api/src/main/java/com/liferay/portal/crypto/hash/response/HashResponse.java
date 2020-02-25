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

package com.liferay.portal.crypto.hash.response;

import java.util.Optional;

/**
 * @author Carlos Sierra Andrés
 * @author Arthur Chan
 */
public class HashResponse {

	public byte[] getHash() {
		return _hash;
	}

	public byte[] getSalt() {
		if (_salt.isPresent()) {
			return _salt.get();
		}

		return null;
	}

	public static class Builder {

		public Builder(byte[] hash) {
			_hash = hash;
		}

		public HashResponse build() {
			return new HashResponse(this);
		}

		public void setSalt(byte[] salt) {
			_salt = Optional.ofNullable(salt);
		}

		private byte[] _hash;
		private Optional<byte[]> _salt;

	}

	private HashResponse(Builder builder) {
		_hash = builder._hash;
		_salt = builder._salt;
	}

	private byte[] _hash;
	private Optional<byte[]> _salt;

}