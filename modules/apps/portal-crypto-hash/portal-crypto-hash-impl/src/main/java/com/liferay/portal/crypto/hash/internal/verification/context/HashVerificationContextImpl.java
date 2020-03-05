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

package com.liferay.portal.crypto.hash.internal.verification.context;

import com.liferay.portal.crypto.hash.internal.context.AbstractHashContext;
import com.liferay.portal.crypto.hash.verification.context.HashVerificationContext;

import java.util.Optional;

import org.json.JSONObject;

/**
 * @author Arthur Chan
 */
public class HashVerificationContextImpl
	extends AbstractHashContext implements HashVerificationContext {

	public Optional<byte[]> getSalt() {
		return _salt;
	}

	public static class Builder
		extends AbstractHashContext.Builder implements PepperBuilder {

		public Builder(String hashProviderName) {
			super(hashProviderName);

			_salt = null;
		}

		public Builder(String hashProviderName, JSONObject hashProviderMeta) {
			super(hashProviderName, hashProviderMeta);

			_salt = null;
		}

		public Builder(
			String hashProviderName, JSONObject hashProviderMeta, byte[] pepper,
			byte[] salt) {

			super(hashProviderName, hashProviderMeta, pepper);

			_salt = salt;
		}

		@Override
		public HashVerificationContext build() {
			return new HashVerificationContextImpl(
				hashProviderName, hashProviderMeta, pepper, _salt);
		}

		@Override
		public SaltBuilder pepper(byte[] pepper) {
			if (pepper == null) {
				throw new IllegalArgumentException("pepper can not be null");
			}

			return new Builder(
				hashProviderName, hashProviderMeta, pepper, _salt);
		}

		@Override
		public HashVerificationContext.Builder salt(byte[] salt) {
			if (salt == null) {
				throw new IllegalArgumentException("salt can not be null");
			}

			return new Builder(
				hashProviderName, hashProviderMeta, pepper, salt);
		}

		private final byte[] _salt;

	}

	protected HashVerificationContextImpl(
		String hashProviderName, JSONObject hashProviderMeta, byte[] pepper,
		byte[] salt) {

		super(hashProviderName, hashProviderMeta, pepper);

		_salt = Optional.ofNullable(salt);
	}

	private final Optional<byte[]> _salt;

}