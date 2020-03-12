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

	public abstract Builder newBuilder(
		String hashProviderName, JSONObject hashProviderMeta);

	public abstract static class Builder {

		public abstract HashContext build();

		public Builder pepper(byte[] pepper) {
			if (pepper == null) {
				throw new IllegalArgumentException("pepper can not be null");
			}

			this.pepper = Optional.ofNullable(pepper);

			return this;
		}

		protected Builder(
			String hashProviderName, JSONObject hashProviderMeta) {

			this.hashProviderName = hashProviderName;

			this.hashProviderMeta = hashProviderMeta;
		}

		protected JSONObject hashProviderMeta;
		protected String hashProviderName;
		protected Optional<byte[]> pepper;

	}

	protected HashContext(
		String hashProviderName, JSONObject hashProviderMeta,
		Optional<byte[]> pepper) {

		_hashProviderName = hashProviderName;
		_hashProviderMeta = hashProviderMeta;
		_pepper = pepper;
	}

	private final JSONObject _hashProviderMeta;
	private final String _hashProviderName;
	private final Optional<byte[]> _pepper;

}