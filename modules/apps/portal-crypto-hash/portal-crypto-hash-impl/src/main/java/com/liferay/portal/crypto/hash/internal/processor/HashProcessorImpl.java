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
import com.liferay.portal.crypto.hash.builder.HashBuilder;
import com.liferay.portal.crypto.hash.builder.HashProviderBuilder;
import com.liferay.portal.crypto.hash.generation.context.HashGenerationContext;
import com.liferay.portal.crypto.hash.generation.context.salt.SaltCommand;
import com.liferay.portal.crypto.hash.generation.response.HashGenerationResponse;
import com.liferay.portal.crypto.hash.header.HashHeader;
import com.liferay.portal.crypto.hash.internal.context.builder.HashProviderBuilderImpl;
import com.liferay.portal.crypto.hash.internal.generation.response.HashGenerationResponseImpl;
import com.liferay.portal.crypto.hash.internal.verification.context.HashHeaderImpl;
import com.liferay.portal.crypto.hash.processor.HashProcessor;
import com.liferay.portal.crypto.hash.provider.spi.HashProvider;
import com.liferay.portal.crypto.hash.provider.spi.factory.HashProviderFactory;
import com.liferay.portal.crypto.hash.provider.spi.salt.VariableSizeSaltProvider;

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
	public HashProviderBuilder createHashBuilder(String hashProviderName) {
		Set<String> availableHashProviderNames =
			getAvailableHashProviderNames();

		if (!availableHashProviderNames.contains(hashProviderName)) {
			throw new IllegalArgumentException(
				"There is no provider of name: " + hashProviderName);
		}

		return new HashProviderBuilderImpl(hashProviderName, null);
	}

	@Override
	public HashHeader createHashHeader(String serializedHashHeader)
		throws Exception {

		JSONObject jsonObject = new JSONObject(serializedHashHeader);

		HashProviderBuilder hashProviderBuilder = createHashBuilder(
			jsonObject.getString("hashProviderName"));

		HashBuilder hashBuilder = hashProviderBuilder;

		if (jsonObject.has("hashProviderMeta")) {
			hashBuilder = hashProviderBuilder.hashProviderMeta(
				jsonObject.getJSONObject("hashProviderMeta"));
		}

		if (jsonObject.has("pepperId") && jsonObject.has("salt")) {
			return hashBuilder.buildHashHeader(
				jsonObject.getString("pepperId"),
				(byte[])jsonObject.get("salt"));
		}

		if (jsonObject.has("pepperId")) {
			return hashBuilder.buildHashHeader(
				jsonObject.getString("pepperId"));
		}

		if (jsonObject.has("salt")) {
			return hashBuilder.buildHashHeader((byte[])jsonObject.get("salt"));
		}

		return hashBuilder.buildHashHeader();
	}

	@Override
	public HashGenerationResponse generate(
			byte[] input, HashGenerationContext hashGenerationContext)
		throws Exception {

		String hashProviderName = hashGenerationContext.getHashProviderName();
		Optional<JSONObject> optionalHashProviderMeta =
			hashGenerationContext.getHashProviderMeta();

		HashProvider hashProvider = _createHashProvider(
			hashProviderName, optionalHashProviderMeta);

		// process pepper

		byte[] pepper = null;

		String pepperId = "";

		//pepperId = _pepperProvider.getCurrentPepperId();
		//pepper = _pepperProvider.getPepper(pepperId);

		if (pepper != null) {
			hashProvider.setPepper(pepper);
		}

		// process salt

		Optional<byte[]> optionalSalt = _processSaltCommands(
			hashProvider, hashGenerationContext.getSaltCommands());

		optionalSalt.ifPresent(hashProvider::setSalt);

		return new HashGenerationResponseImpl(
			new HashHeaderImpl(
				hashProviderName,
				optionalHashProviderMeta.orElse(new JSONObject()), pepperId,
				optionalSalt.orElse(new byte[0])),
			hashProvider.hash(input));
	}

	@Override
	public Set<String> getAvailableHashProviderNames() {
		return _hashProviderFactories.keySet();
	}

	@Override
	public boolean verify(byte[] input, byte[] hash, HashHeader... hashHeaders)
		throws Exception {

		for (HashHeader hashHeader : hashHeaders) {
			HashProvider hashProvider = _createHashProvider(
				hashHeader.getHashProviderName(),
				hashHeader.getHashProviderMeta());

			// process pepper

			byte[] pepper = null;

			String pepperId = "";

			//pepperId = _pepperProvider.getCurrentPepperId();
			//pepper = _pepperProvider.getPepper(pepperId);

			if (pepper != null) {
				hashProvider.setPepper(pepper);
			}

			// process salt

			Optional<byte[]> optionalSalt = hashHeader.getSalt();

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
			String providerName, Optional<JSONObject> providerMeta)
		throws Exception {

		HashProviderFactory hashProviderFactory =
			_hashProviderFactories.getService(providerName);

		if (hashProviderFactory == null) {
			throw new IllegalArgumentException(
				"There is no provider of the name: " + providerName);
		}

		return hashProviderFactory.create(
			providerName, providerMeta.orElse(new JSONObject()));
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