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
import com.liferay.portal.crypto.hash.generation.context.salt.SaltCommand;

import org.json.JSONObject;

/**
 * @author Carlos Sierra Andrés
 * @author Arthur Chan
 */
public class HashGenerationContext extends HashContext {

	public static Builder newBuilder() {
		return new Builder(null, (SaltCommand[])null);
	}

	public SaltCommand[] getSaltCommands() {
		return _saltCommands;
	}

	public static class Builder implements PepperBuilder {

		@Override
		public HashGenerationContext hashProvider(String hashProviderName) {
			return new HashGenerationContext(
				hashProviderName, null, _pepper, _saltCommands);
		}

		@Override
		public HashGenerationContext hashProvider(
			String hashProviderName, JSONObject hashProviderMeta) {

			return new HashGenerationContext(
				hashProviderName, hashProviderMeta, _pepper, _saltCommands);
		}

		@Override
		public SaltCommandBuilder pepper(byte[] pepper) {
			if (pepper == null) {
				throw new IllegalArgumentException("pepper can not be null");
			}

			return new Builder(pepper, _saltCommands);
		}

		@Override
		public Builder saltCommand(SaltCommand... saltCommands) {
			if (saltCommands == null) {
				throw new IllegalArgumentException(
					"saltCommands can not be null");
			}

			return new Builder(_pepper, saltCommands);
		}

		private Builder(byte[] pepper, SaltCommand... saltCommands) {
			_pepper = pepper;
			_saltCommands = saltCommands;
		}

		private final byte[] _pepper;
		private final SaltCommand[] _saltCommands;

	}

	public interface HashProviderBuilder {

		public HashGenerationContext input(byte[] input);

	}

	public interface PepperBuilder extends SaltCommandBuilder {

		public SaltCommandBuilder pepper(byte[] pepper);

	}

	public interface SaltCommandBuilder extends HashProviderBuilder {

		public HashProviderBuilder saltCommand(SaltCommand... saltCommands);

	}

	protected HashGenerationContext(
		String hashProviderName, JSONObject hashProviderMeta, byte[] pepper,
		SaltCommand... saltCommands) {

		super(hashProviderName, hashProviderMeta, pepper);

		_saltCommands = saltCommands;
	}

	private final SaltCommand[] _saltCommands;

}