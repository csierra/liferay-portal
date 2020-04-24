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

import com.liferay.portal.crypto.hash.flavor.HashFlavor;
import com.liferay.portal.crypto.hash.internal.flavor.HashFlavorImpl;
import com.liferay.portal.crypto.hash.verification.context.HashVerificationContext;

import java.util.Optional;

import org.json.JSONObject;

/**
 * @author Arthur Chan
 */
public class HashVerificationContextImpl implements HashVerificationContext {

	public HashVerificationContextImpl(
		String hashProviderName, JSONObject hashProviderMeta,
		HashFlavor hashFlavor) {

		_hashProviderName = hashProviderName;
		_hashProviderMeta = Optional.ofNullable(hashProviderMeta);

		_hashFlavor = hashFlavor;
	}

	@Override
	public HashFlavor getHashFlavor() {
		return _hashFlavor;
	}

	@Override
	public Optional<JSONObject> getHashProviderMeta() {
		return _hashProviderMeta;
	}

	@Override
	public String getHashProviderName() {
		return _hashProviderName;
	}

	public static class BuilderImpl implements HashVerificationContext.Builder {

		public BuilderImpl(
			String hashProviderName, JSONObject hashProviderMeta) {

			this(hashProviderName, hashProviderMeta, null, null);
		}

		public BuilderImpl(
			String hashProviderName, JSONObject hashProviderMeta,
			String pepperId, byte[] salt) {

			_hashProviderName = hashProviderName;
			_hashProviderMeta = hashProviderMeta;
			_pepperId = pepperId;
			_salt = salt;
		}

		@Override
		public HashVerificationContext build() {
			HashFlavor hashFlavor = new HashFlavorImpl(_pepperId, _salt);

			return new HashVerificationContextImpl(
				_hashProviderName, _hashProviderMeta, hashFlavor);
		}

		@Override
		public HashVerificationContext build(String serializedHashFlavor) {
			JSONObject jsonObject = new JSONObject(serializedHashFlavor);

			HashFlavor hashFlavor = new HashFlavorImpl(
				jsonObject.getString("pepperId"),
				(byte[])jsonObject.get("salt"));

			return new HashVerificationContextImpl(
				_hashProviderName, _hashProviderMeta, hashFlavor);
		}

		@Override
		public SaltedBuilder pepper(String pepperId) {
			if (pepperId == null) {
				throw new IllegalArgumentException(
					"use other overloaded method if no pepperId is provided");
			}

			return new BuilderImpl(
				_hashProviderName, _hashProviderMeta, pepperId, null);
		}

		@Override
		public Builder salt(byte[] salt) {
			if (salt == null) {
				throw new IllegalArgumentException(
					"use other overloaded method if no salt is provided");
			}

			return new BuilderImpl(
				_hashProviderName, _hashProviderMeta, null, salt);
		}

		private final JSONObject _hashProviderMeta;
		private final String _hashProviderName;
		private final String _pepperId;
		private final byte[] _salt;

	}

	private final HashFlavor _hashFlavor;
	private final Optional<JSONObject> _hashProviderMeta;
	private final String _hashProviderName;

}