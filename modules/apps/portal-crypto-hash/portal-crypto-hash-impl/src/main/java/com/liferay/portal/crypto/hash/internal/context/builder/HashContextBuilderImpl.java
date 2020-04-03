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

import com.liferay.portal.crypto.hash.context.builder.HashContextBuilder;
import com.liferay.portal.crypto.hash.generation.context.HashGenerationContext;
import com.liferay.portal.crypto.hash.internal.generation.context.HashGenerationContextImpl;
import com.liferay.portal.crypto.hash.internal.verification.context.HashVerificationContextImpl;
import com.liferay.portal.crypto.hash.verification.context.HashVerificationContext;

import org.json.JSONObject;

/**
 * @author Arthur Chan
 */
public class HashContextBuilderImpl implements HashContextBuilder {

	public HashContextBuilderImpl(
		String hashProviderName, JSONObject hashProviderMeta) {

		_hashProviderName = hashProviderName;
		_hashProviderMeta = hashProviderMeta;
	}

	@Override
	public HashGenerationContext.PepperBuilder generationHashProvider() {
		return new HashGenerationContextImpl.Builder(
			_hashProviderName, _hashProviderMeta);
	}

	@Override
	public HashVerificationContext.PepperBuilder verificationHashProvider() {
		return new HashVerificationContextImpl.Builder(
			_hashProviderName, _hashProviderMeta);
	}

	private final JSONObject _hashProviderMeta;
	private final String _hashProviderName;

}