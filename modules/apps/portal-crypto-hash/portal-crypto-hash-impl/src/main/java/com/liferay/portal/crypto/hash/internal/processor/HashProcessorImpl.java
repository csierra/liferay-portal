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

import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.crypto.hash.context.builder.HashContextBuilder;
import com.liferay.portal.crypto.hash.generation.context.HashGenerationContext;
import com.liferay.portal.crypto.hash.generation.context.salt.SaltCommand;
import com.liferay.portal.crypto.hash.generation.response.HashGenerationResponse;
import com.liferay.portal.crypto.hash.internal.context.builder.HashContextBuilderImpl;
import com.liferay.portal.crypto.hash.internal.generation.response.HashGenerationResponseImpl;
import com.liferay.portal.crypto.hash.processor.HashProcessor;
import com.liferay.portal.crypto.hash.provider.spi.HashProvider;
import com.liferay.portal.crypto.hash.provider.spi.factory.HashProviderFactory;
import com.liferay.portal.crypto.hash.provider.spi.salt.VariableSizeSaltProvider;
import com.liferay.portal.crypto.hash.verification.context.HashVerificationContext;

import java.util.Optional;
import java.util.Set;

import org.json.JSONObject;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Arthur Chan
 * @author Carlos Sierra Andrés
 */
@Component(service = HashProcessor.class)
public class HashProcessorImpl implements HashProcessor {

	@Override
	public HashContextBuilder createHashContextBuilder(
		String hashProviderName) {

		return createHashContextBuilder(hashProviderName, null);
	}

	@Override
	public HashContextBuilder createHashContextBuilder(
		String hashProviderName, JSONObject hashProviderMeta) {

		Set<String> availableHashProviderNames =
			getAvailableHashProviderNames();

		if (!availableHashProviderNames.contains(hashProviderName)) {
			throw new IllegalArgumentException(
				"There is no provider of name: " + hashProviderName);
		}

		return new HashContextBuilderImpl(hashProviderName, hashProviderMeta);
	}

	@Override
	public HashGenerationResponse generate(
			byte[] input, HashGenerationContext hashGenerationContext)
		throws Exception {

		HashProvider hashProvider = _createHashProvider(
			hashGenerationContext.getHashProviderName(),
			hashGenerationContext.getHashProviderMeta());

		// process pepper

		Optional<byte[]> optionalPepper = hashGenerationContext.getPepper();

		optionalPepper.ifPresent(hashProvider::setPepper);

		// process salt

		Optional<byte[]> optionalSalt = _processSaltCommands(
			hashProvider, hashGenerationContext.getSaltCommands());

		optionalSalt.ifPresent(hashProvider::setSalt);

		return new HashGenerationResponseImpl(
			optionalSalt, hashProvider.hash(input));
	}

	@Override
	public Set<String> getAvailableHashProviderNames() {
		return _hashProviderFactories.keySet();
	}

	@Override
	public boolean verify(
			byte[] input, byte[] hash,
			HashVerificationContext... hashVerificationContexts)
		throws Exception {

		for (HashVerificationContext hashVerificationContext :
				hashVerificationContexts) {

			HashProvider hashProvider = _createHashProvider(
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

	@Activate
	protected void activate(BundleContext bundleContext) {
		_hashProviderFactories = ServiceTrackerMapFactory.openSingleValueMap(
			bundleContext, HashProviderFactory.class,
			"crypto.hash.provider.name");
	}

	@Deactivate
	protected void deactivate() {
		_hashProviderFactories.close();
	}

	/**
	 * A comparison algorithm that prevents timing attack
	 *
	 * @param bytes1 the input bytes
	 * @param bytes2 the expected bytes
	 * @return true if two given arrays of bytes are the same, otherwise false
	 */
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

	private HashProvider _createHashProvider(
			String providerName, JSONObject providerMeta)
		throws Exception {

		HashProviderFactory hashProviderFactory =
			_hashProviderFactories.getService(providerName);

		if (hashProviderFactory == null) {
			throw new IllegalArgumentException(
				"There is no provider of the name: " + providerName);
		}

		return hashProviderFactory.create(providerName, providerMeta);
	}

	private Optional<byte[]> _processSaltCommands(
		HashProvider hashProvider, SaltCommand... saltCommands) {

		if ((saltCommands == null) || (saltCommands.length < 1)) {
			return Optional.empty();
		}

		for (SaltCommand saltCommand : saltCommands) {
			if (saltCommand instanceof SaltCommand.VariableSizeSaltCommand) {
				SaltCommand.VariableSizeSaltCommand variableSizeSaltCommand =
					(SaltCommand.VariableSizeSaltCommand)saltCommand;

				if (hashProvider instanceof VariableSizeSaltProvider) {
					VariableSizeSaltProvider variableSizeSaltProvider =
						(VariableSizeSaltProvider)hashProvider;

					return Optional.of(
						variableSizeSaltProvider.generateSalt(
							variableSizeSaltCommand.getSaltSize()));
				}
			}
			else {
				return Optional.of(hashProvider.generateSalt());
			}
		}

		throw new UnsupportedOperationException();
	}

	private ServiceTrackerMap<String, HashProviderFactory>
		_hashProviderFactories;

}