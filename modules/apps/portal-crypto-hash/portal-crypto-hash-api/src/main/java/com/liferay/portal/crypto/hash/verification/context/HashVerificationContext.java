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

package com.liferay.portal.crypto.hash.verification.context;

import com.liferay.portal.crypto.hash.context.HashContext;
import com.liferay.portal.crypto.hash.generation.context.HashGenerationContext;

import java.util.Optional;

import org.json.JSONObject;

/**
 * @author Carlos Sierra Andrés
 * @author Arthur Chan
 */
public class HashVerificationContext extends HashContext {

	public static Builder newBuilder(String hashProviderName) {
		return new Builder(hashProviderName, null, null, null);
	}

	public static Builder newtBuilder(String hashProviderName, JSONObject hashProviderMeta) {
		return new Builder(hashProviderName, hashProviderMeta, null, null);
	}

	public Optional<byte[]> getSalt() {
		return _salt;
	}

	public static class Builder implements PepperBuilder {

		@Override
		public HashVerificationContext build() {
			return new HashVerificationContext(_hashProviderName, _hashProviderMeta, _pepper, _salt);
		}

		@Override
		public SaltBuilder pepper(byte[] pepper) {
			if (pepper == null) {
				throw new IllegalArgumentException("pepper can not be null");
			}

			return new Builder(_hashProviderName, _hashProviderMeta, pepper, _salt);
		}

		@Override
		public ContextBuilder salt(byte[] salt) {
			if (salt == null) {
				throw new IllegalArgumentException("salt can not be null");
			}

			return new Builder(_hashProviderName, _hashProviderMeta, _pepper, salt);
		}

		private Builder(String hashProviderName, JSONObject hashProviderMeta, byte[] pepper, byte[] salt) {
			_hashProviderName = hashProviderName;
			_hashProviderMeta = hashProviderMeta;
			_pepper = pepper;
			_salt = salt;
		}

		private final String _hashProviderName;
		private final JSONObject _hashProviderMeta;
		private final byte[] _pepper;
		private final byte[] _salt;

	}

	public interface PepperBuilder extends SaltBuilder {

		public SaltBuilder pepper(byte[] pepper);

	}

	public interface SaltBuilder extends ContextBuilder {

		public ContextBuilder salt(byte[] salt);

	}

	public interface ContextBuilder {

		public HashVerificationContext build();

	}

	protected HashVerificationContext(
		String hashProviderName, JSONObject hashProviderMeta, byte[] pepper,
		byte[] salt) {

		super(hashProviderName, hashProviderMeta, pepper);

		_salt = Optional.ofNullable(salt);
	}

	private final Optional<byte[]> _salt;

}