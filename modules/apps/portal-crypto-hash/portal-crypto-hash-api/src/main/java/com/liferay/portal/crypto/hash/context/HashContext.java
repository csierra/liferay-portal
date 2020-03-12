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

package com.liferay.portal.crypto.hash.context;

import java.util.Optional;

import org.json.JSONObject;

/**
 * @author Carlos Sierra Andrés
 * @author Arthur Chan
 */
public abstract class HashContext {

	public JSONObject getHashProviderMeta() {
		return _hashProviderMeta;
	}

	public String getHashProviderName() {
		return _hashProviderName;
	}

	public Optional<byte[]> getPepper() {
		return _pepper;
	}

	protected HashContext(
		String hashProviderName, JSONObject hashProviderMeta, byte[] pepper) {

		_hashProviderName = hashProviderName;
		_hashProviderMeta = hashProviderMeta;
		_pepper = Optional.ofNullable(pepper);
	}

	private final JSONObject _hashProviderMeta;
	private final String _hashProviderName;
	private final Optional<byte[]> _pepper;

}