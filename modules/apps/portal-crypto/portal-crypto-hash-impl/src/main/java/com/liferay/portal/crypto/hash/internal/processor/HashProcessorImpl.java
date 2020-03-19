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

import com.liferay.portal.crypto.hash.generator.spi.HashGenerator;
import com.liferay.portal.crypto.hash.internal.request.salt.command.SaltProviderImpl;
import com.liferay.portal.crypto.hash.internal.response.HashResponseImpl;
import com.liferay.portal.crypto.hash.processor.HashProcessor;
import com.liferay.portal.crypto.hash.request.HashRequest;
import com.liferay.portal.crypto.hash.request.salt.command.SaltProvider;
import com.liferay.portal.crypto.hash.response.HashResponse;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author Arthur Chan
 * @author Carlos Sierra Andrés
 */
public class HashProcessorImpl implements HashProcessor {

	public HashProcessorImpl(HashGenerator hashGenerator) {
		_hashGenerator = hashGenerator;
	}

	@Override
	public HashResponse process(HashRequest hashRequest) throws Exception {
		_processPepper(hashRequest);
		_processSalt(hashRequest);

		return new HashResponseImpl(
			Optional.ofNullable(_hashGenerator.getPepper()),
			Optional.ofNullable(_hashGenerator.getSalt()),
			_hashGenerator.hash(hashRequest.getInput()));
	}

	private void _processPepper(HashRequest hashRequest) {
		Optional<byte[]> optionalPepper = hashRequest.getPepper();

		optionalPepper.ifPresent(_hashGenerator::setPepper);
	}

	private void _processSalt(HashRequest hashRequest) {
		Optional<Function<SaltProvider, byte[]>> optionalFunction =
			hashRequest.getSaltFunction();

		optionalFunction.map(
			sf -> sf.apply(new SaltProviderImpl(_hashGenerator))
		).ifPresent(
			_hashGenerator::setSalt
		);
	}

	private final HashGenerator _hashGenerator;

}