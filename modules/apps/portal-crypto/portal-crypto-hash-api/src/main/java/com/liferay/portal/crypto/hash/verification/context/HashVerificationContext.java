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

package com.liferay.portal.crypto.hash.verification.context;

import com.liferay.portal.crypto.hash.context.HashContext;

import java.util.Optional;

import org.json.JSONObject;

/**
 * @author Arthur Chan
 */
public class HashVerificationContext extends HashContext {

	public static Builder newBuilder(
		String hashProviderName, JSONObject hashProviderMeta) {

		return new Builder(hashProviderName, hashProviderMeta);
	}

	public Optional<byte[]> getSalt() {
		return _salt;
	}

	public static class Builder extends HashContext.Builder {

		public HashVerificationContext build() {
			return new HashVerificationContext(
				hashProviderName, hashProviderMeta, pepper, _salt);
		}

		public Builder salt(byte[] salt) {
			_salt = Optional.ofNullable(salt);

			return this;
		}

		private Builder(String hashProviderName, JSONObject hashProviderMeta) {
			super(hashProviderName, hashProviderMeta);
		}

		private Optional<byte[]> _salt;

	}

	protected HashVerificationContext(
		String hashProviderName, JSONObject hashProviderMeta,
		Optional<byte[]> pepper, Optional<byte[]> salt) {

		super(hashProviderName, hashProviderMeta, pepper);

		_salt = salt;
	}

	private Optional<byte[]> _salt;

}