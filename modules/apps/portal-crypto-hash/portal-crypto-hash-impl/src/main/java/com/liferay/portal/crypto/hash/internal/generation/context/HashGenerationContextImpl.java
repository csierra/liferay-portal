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
import com.liferay.portal.crypto.hash.internal.context.AbstractHashContext;

import org.json.JSONObject;

/**
 * @author Arthur Chan
 */
public class HashGenerationContextImpl
	extends AbstractHashContext implements HashGenerationContext {

	public SaltCommand[] getSaltCommands() {
		return _saltCommands;
	}

	public static class Builder
		extends AbstractHashContext.Builder implements PepperBuilder {

		public Builder(String hashProviderName, JSONObject hashProviderMeta) {
			super(hashProviderName, hashProviderMeta);

			_saltCommands = null;
		}

		public Builder(
			String hashProviderName, JSONObject hashProviderMeta, byte[] pepper,
			SaltCommand... saltCommands) {

			super(hashProviderName, hashProviderMeta, pepper);

			_saltCommands = saltCommands;
		}

		@Override
		public HashGenerationContext build() {
			return new HashGenerationContextImpl(
				hashProviderName, hashProviderMeta, pepper, _saltCommands);
		}

		@Override
		public SaltCommandBuilder pepper(byte[] pepper) {
			if (pepper == null) {
				throw new IllegalArgumentException("pepper can not be null");
			}

			return new Builder(
				hashProviderName, hashProviderMeta, pepper, _saltCommands);
		}

		@Override
		public HashGenerationContext.Builder saltCommand(
			SaltCommand... saltCommands) {

			if (saltCommands == null) {
				throw new IllegalArgumentException(
					"saltCommands can not be null");
			}

			return new Builder(
				hashProviderName, hashProviderMeta, pepper, saltCommands);
		}

		private final SaltCommand[] _saltCommands;

	}

	protected HashGenerationContextImpl(
		String hashProviderName, JSONObject hashProviderMeta, byte[] pepper,
		SaltCommand... saltCommands) {

		super(hashProviderName, hashProviderMeta, pepper);

		_saltCommands = saltCommands;
	}

	private final SaltCommand[] _saltCommands;

}