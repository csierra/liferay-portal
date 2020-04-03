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
import com.liferay.portal.crypto.hash.processor.HashProcessor;
import com.liferay.portal.crypto.hash.verification.context.HashVerificationContext;

import java.util.Set;

import org.json.JSONObject;

/**
 * @author Arthur Chan
 */
public class HashContextBuilderImpl implements HashContextBuilder {

	public HashContextBuilderImpl(HashProcessor hashProcessor) {
		_hashProcessor = hashProcessor;
	}

	@Override
	public HashGenerationContext.PepperBuilder generationHashProvider(
			String hashProviderName)
		throws Exception {

		Set<String> availableHashProviderNames =
			_hashProcessor.getAvailableHashProviderNames();

		if (!availableHashProviderNames.contains(hashProviderName)) {
			throw new IllegalArgumentException(
				"There is no provider of name: " + hashProviderName);
		}

		return new HashGenerationContextImpl.Builder(hashProviderName);
	}

	@Override
	public HashGenerationContext.PepperBuilder generationHashProvider(
			String hashProviderName, JSONObject hashProviderMeta)
		throws Exception {

		Set<String> availableHashProviderNames =
			_hashProcessor.getAvailableHashProviderNames();

		if (!availableHashProviderNames.contains(hashProviderName)) {
			throw new IllegalArgumentException(
				"There is no provider of name: " + hashProviderName);
		}

		return new HashGenerationContextImpl.Builder(
			hashProviderName, hashProviderMeta);
	}

	@Override
	public HashVerificationContext.PepperBuilder verificationHashProvider(
			String hashProviderName)
		throws Exception {

		Set<String> availableHashProviderNames =
			_hashProcessor.getAvailableHashProviderNames();

		if (!availableHashProviderNames.contains(hashProviderName)) {
			throw new IllegalArgumentException(
				"There is no provider of name: " + hashProviderName);
		}

		return new HashVerificationContextImpl.Builder(hashProviderName);
	}

	@Override
	public HashVerificationContext.PepperBuilder verificationHashProvider(
			String hashProviderName, JSONObject hashProviderMeta)
		throws Exception {

		Set<String> availableHashProviderNames =
			_hashProcessor.getAvailableHashProviderNames();

		if (!availableHashProviderNames.contains(hashProviderName)) {
			throw new IllegalArgumentException(
				"There is no provider of name: " + hashProviderName);
		}

		return new HashVerificationContextImpl.Builder(
			hashProviderName, hashProviderMeta);
	}

	private final HashProcessor _hashProcessor;

}