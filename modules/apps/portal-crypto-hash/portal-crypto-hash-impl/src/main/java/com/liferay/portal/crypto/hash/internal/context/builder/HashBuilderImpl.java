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

package com.liferay.portal.crypto.hash.internal.context.builder;

import com.liferay.portal.crypto.hash.builder.HashBuilder;
import com.liferay.portal.crypto.hash.generation.context.HashGenerationContext;
import com.liferay.portal.crypto.hash.generation.context.salt.SaltCommand;
import com.liferay.portal.crypto.hash.header.HashHeader;
import com.liferay.portal.crypto.hash.internal.generation.context.HashGenerationContextImpl;
import com.liferay.portal.crypto.hash.internal.verification.context.HashHeaderImpl;

import org.json.JSONObject;

/**
 * @author Arthur Chan
 */
public class HashBuilderImpl implements HashBuilder {

	public HashBuilderImpl(
		String hashProviderName, JSONObject hashProviderMeta) {

		this.hashProviderName = hashProviderName;
		this.hashProviderMeta = hashProviderMeta;
	}

	@Override
	public HashGenerationContext buildHashGenerationContext(
		SaltCommand... saltCommands) {

		if (saltCommands == null) {
			throw new IllegalArgumentException(
				"saltCommands can not be null, remove argument if no" +
					"saltCommand is providered");
		}

		return new HashGenerationContextImpl(
			hashProviderName, hashProviderMeta, saltCommands);
	}

	@Override
	public HashHeader buildHashHeader() {
		return new HashHeaderImpl(
			hashProviderName, hashProviderMeta, null, null);
	}

	@Override
	public HashHeader buildHashHeader(byte[] salt) {
		if (salt == null) {
			throw new IllegalArgumentException(
				"use other overloaded method if no salt is provided");
		}

		return new HashHeaderImpl(
			hashProviderName, hashProviderMeta, null, salt);
	}

	@Override
	public HashHeader buildHashHeader(String pepperId) {
		if (pepperId == null) {
			throw new IllegalArgumentException(
				"use other overloaded method if no pepperId is provided");
		}

		return new HashHeaderImpl(
			hashProviderName, hashProviderMeta, pepperId, null);
	}

	@Override
	public HashHeader buildHashHeader(String pepperId, byte[] salt) {
		if (pepperId == null) {
			throw new IllegalArgumentException(
				"use other overloaded method if no pepperId is provided");
		}

		if (salt == null) {
			throw new IllegalArgumentException(
				"use other overloaded method if no salt is provided");
		}

		return new HashHeaderImpl(
			hashProviderName, hashProviderMeta, pepperId, salt);
	}

	protected final JSONObject hashProviderMeta;
	protected final String hashProviderName;

}