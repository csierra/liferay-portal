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

import com.liferay.portal.crypto.hash.header.HashHeader;

import java.util.Optional;

import org.json.JSONObject;

/**
 * @author Arthur Chan
 */
public class HashHeaderImpl implements HashHeader {

	public HashHeaderImpl(
		String hashProviderName, JSONObject hashProviderMeta, String pepperId,
		byte[] salt) {

		_hashProviderName = hashProviderName;
		_hashProviderMeta = Optional.ofNullable(hashProviderMeta);

		_pepperId = Optional.ofNullable(pepperId);

		_salt = Optional.ofNullable(salt);
	}

	@Override
	public Optional<JSONObject> getHashProviderMeta() {
		return _hashProviderMeta;
	}

	@Override
	public String getHashProviderName() {
		return _hashProviderName;
	}

	@Override
	public Optional<String> getPepperId() {
		return _pepperId;
	}

	public Optional<byte[]> getSalt() {
		return _salt;
	}

	private final Optional<JSONObject> _hashProviderMeta;
	private final String _hashProviderName;
	private final Optional<String> _pepperId;
	private final Optional<byte[]> _salt;

}