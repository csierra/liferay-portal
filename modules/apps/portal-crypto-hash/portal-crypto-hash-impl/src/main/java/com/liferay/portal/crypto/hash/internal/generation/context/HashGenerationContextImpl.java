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

package com.liferay.portal.crypto.hash.internal.generation.context;

import com.liferay.portal.crypto.hash.generation.context.HashGenerationContext;
import com.liferay.portal.crypto.hash.generation.context.salt.SaltCommand;

import java.util.Optional;

import org.json.JSONObject;

/**
 * @author Arthur Chan
 */
public class HashGenerationContextImpl implements HashGenerationContext {

	public HashGenerationContextImpl(
		String hashProviderName, JSONObject hashProviderMeta,
		SaltCommand... saltCommands) {

		_hashProviderName = hashProviderName;
		_hashProviderMeta = Optional.ofNullable(hashProviderMeta);

		_saltCommands = saltCommands;
	}

	@Override
	public Optional<JSONObject> getHashProviderMeta() {
		return _hashProviderMeta;
	}

	@Override
	public String getHashProviderName() {
		return _hashProviderName;
	}

	public SaltCommand[] getSaltCommands() {
		return _saltCommands;
	}

	public static class BuilderImpl implements HashGenerationContext.Builder {

		public BuilderImpl(
			String hashProviderName, JSONObject hashProviderMeta) {

			_hashProviderName = hashProviderName;
			_hashProviderMeta = hashProviderMeta;
		}

		@Override
		public HashGenerationContext build(SaltCommand... saltCommands) {
			if (saltCommands == null) {
				throw new IllegalArgumentException(
					"saltCommands can not be null, remove argument if no" +
						"saltCommand is providered");
			}

			return new HashGenerationContextImpl(
				_hashProviderName, _hashProviderMeta, saltCommands);
		}

		private JSONObject _hashProviderMeta;
		private String _hashProviderName;

	}

	private final Optional<JSONObject> _hashProviderMeta;
	private final String _hashProviderName;
	private final SaltCommand[] _saltCommands;

}