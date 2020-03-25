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
import com.liferay.portal.crypto.hash.generation.context.salt.SaltGenerationCommand;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;

/**
 * @author Arthur Chan
 */
public class HashGenerationContext extends HashContext {

	public static Builder newBuilder(
		String hashProviderName, JSONObject hashProviderMeta) {

		return new Builder(hashProviderName, hashProviderMeta);
	}

	public List<SaltGenerationCommand> getSaltGenerationCommands() {
		return _saltGenerationCommands;
	}

	public static class Builder extends HashContext.Builder {

		public HashGenerationContext build() {
			return new HashGenerationContext(
				hashProviderName, hashProviderMeta, pepper,
				_saltGenerationCommands);
		}

		public Builder saltGeneration(
			SaltGenerationCommand... saltGenerationCommands) {

			_saltGenerationCommands = Arrays.asList(saltGenerationCommands);

			return this;
		}

		private Builder(String hashProviderName, JSONObject hashProviderMeta) {
			super(hashProviderName, hashProviderMeta);
		}

		private List<SaltGenerationCommand> _saltGenerationCommands;

	}

	protected HashGenerationContext(
		String hashProviderName, JSONObject hashProviderMeta,
		Optional<byte[]> pepper,
		List<SaltGenerationCommand> saltGenerationCommands) {

		super(hashProviderName, hashProviderMeta, pepper);

		_saltGenerationCommands = saltGenerationCommands;
	}

	private List<SaltGenerationCommand> _saltGenerationCommands;

}