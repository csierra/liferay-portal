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

package com.liferay.portal.crypto.hash.generation.context;

import com.liferay.portal.crypto.hash.context.HashContext;
import com.liferay.portal.crypto.hash.generation.context.salt.SaltGeneration;

import java.util.Optional;
import java.util.function.Function;

import org.json.JSONObject;

/**
 * @author Arthur Chan
 */
public class HashGenerationContext extends HashContext {

	public Optional<Function<SaltGeneration, byte[]>>
		getSaltGenerationFunction() {

		return _saltGenerationFunction;
	}

	public Builder newBuilder(
		String hashProviderName, JSONObject hashProviderMeta) {

		return new Builder(hashProviderName, hashProviderMeta);
	}

	public static class Builder extends HashContext.Builder {

		public HashGenerationContext build() {
			return new HashGenerationContext(
				hashProviderName, hashProviderMeta, pepper,
				_saltGenerationFunction);
		}

		public Builder salt(
			Function<SaltGeneration, byte[]> saltGenerationFunction) {

			_saltGenerationFunction = Optional.ofNullable(
				saltGenerationFunction);

			return this;
		}

		private Builder(String hashProviderName, JSONObject hashProviderMeta) {
			super(hashProviderName, hashProviderMeta);
		}

		private Optional<Function<SaltGeneration, byte[]>>
			_saltGenerationFunction;

	}

	protected HashGenerationContext(
		String hashProviderName, JSONObject hashProviderMeta,
		Optional<byte[]> pepper,
		Optional<Function<SaltGeneration, byte[]>> saltGenerationFunction) {

		super(hashProviderName, hashProviderMeta, pepper);

		_saltGenerationFunction = saltGenerationFunction;
	}

	private Optional<Function<SaltGeneration, byte[]>> _saltGenerationFunction;

}