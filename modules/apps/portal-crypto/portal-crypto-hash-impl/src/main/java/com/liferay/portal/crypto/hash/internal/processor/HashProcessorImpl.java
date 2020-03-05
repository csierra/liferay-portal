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

package com.liferay.portal.crypto.hash.internal.processor;

import com.liferay.portal.crypto.hash.generation.context.HashGenerationContext;
import com.liferay.portal.crypto.hash.generation.context.salt.SaltGeneration;
import com.liferay.portal.crypto.hash.generation.response.HashGenerationResponse;
import com.liferay.portal.crypto.hash.provider.spi.HashProvider;
import com.liferay.portal.crypto.hash.provider.spi.factory.HashProviderFactory;
import com.liferay.portal.crypto.hash.internal.generation.context.salt.SaltGenerationImpl;
import com.liferay.portal.crypto.hash.internal.generation.response.HashGenerationResponseImpl;
import com.liferay.portal.crypto.hash.processor.HashProcessor;
import com.liferay.portal.crypto.hash.verification.context.HashVerificationContext;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Arthur Chan
 * @author Carlos Sierra Andrés
 */
@Component(service = HashProcessor.class)
public class HashProcessorImpl implements HashProcessor {

	@Override
	public HashGenerationResponse generate(
			byte[] input, HashGenerationContext hashGenerationContext)
		throws Exception {

		HashProvider hashProvider = _hashProviderFactory.create(
			hashGenerationContext.getHashProviderName(),
			hashGenerationContext.getHashProviderMeta());

		// process pepper

		Optional<byte[]> optionalPepper = hashGenerationContext.getPepper();

		optionalPepper.ifPresent(hashProvider::setPepper);

		// process salt

		Optional<Function<SaltGeneration, byte[]>> optionalFunction =
			hashGenerationContext.getSaltGenerationFunction();

		Optional<byte[]> optionalSalt = optionalFunction.map(
			sf -> sf.apply(new SaltGenerationImpl(hashProvider)));

		optionalSalt.ifPresent(hashProvider::setSalt);

		return new HashGenerationResponseImpl(
			optionalPepper, optionalSalt, hashProvider.hash(input));
	}

	@Override
	public boolean verify(
			byte[] input,
			List<HashVerificationContext> hashVerificationContexts, byte[] hash)
		throws Exception {

		for (HashVerificationContext hashVerificationContext :
				hashVerificationContexts) {

			HashProvider hashProvider = _hashProviderFactory.create(
				hashVerificationContext.getHashProviderName(),
				hashVerificationContext.getHashProviderMeta());

			// process pepper

			Optional<byte[]> optionalPepper =
				hashVerificationContext.getPepper();

			optionalPepper.ifPresent(hashProvider::setPepper);

			// process salt

			Optional<byte[]> optionalSalt = hashVerificationContext.getSalt();

			optionalSalt.ifPresent(hashProvider::setSalt);

			input = hashProvider.hash(input);
		}

		return _compare(input, hash);
	}

	// A comparison algorithm that prevents timing attack

	private boolean _compare(byte[] bytes1, byte[] bytes2) {
		int diff = bytes1.length ^ bytes2.length;

		for (int i = 0; (i < bytes1.length) && (i < bytes2.length); ++i) {
			diff |= bytes1[i] ^ bytes2[i];
		}

		if (diff == 0) {
			return true;
		}

		return false;
	}

	@Reference
	private HashProviderFactory _hashProviderFactory;

}