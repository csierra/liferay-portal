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

package com.liferay.portal.crypto.hash.internal.context;

import com.liferay.portal.crypto.hash.context.HashContext;

import java.util.Optional;

import org.json.JSONObject;

/**
 * @author Arthur Chan
 */
public abstract class AbstractHashContext implements HashContext {

	public JSONObject getHashProviderMeta() {
		return hashProviderMeta;
	}

	public String getHashProviderName() {
		return hashProviderName;
	}

	public Optional<byte[]> getPepper() {
		return pepper;
	}

	public abstract static class Builder {

		protected Builder(String hashProviderName) {
			this.hashProviderName = hashProviderName;
			hashProviderMeta = null;
			pepper = null;
		}

		protected Builder(
			String hashProviderName, JSONObject hashProviderMeta) {

			this.hashProviderName = hashProviderName;
			this.hashProviderMeta = hashProviderMeta;
			pepper = null;
		}

		protected Builder(
			String hashProviderName, JSONObject hashProviderMeta,
			byte[] pepper) {

			this.hashProviderName = hashProviderName;
			this.hashProviderMeta = hashProviderMeta;
			this.pepper = pepper;
		}

		protected final JSONObject hashProviderMeta;
		protected final String hashProviderName;
		protected final byte[] pepper;

	}

	protected AbstractHashContext(
		String hashProviderName, JSONObject hashProviderMeta, byte[] pepper) {

		this.hashProviderName = hashProviderName;
		this.hashProviderMeta = hashProviderMeta;
		this.pepper = Optional.ofNullable(pepper);
	}

	protected final JSONObject hashProviderMeta;
	protected final String hashProviderName;
	protected final Optional<byte[]> pepper;

}