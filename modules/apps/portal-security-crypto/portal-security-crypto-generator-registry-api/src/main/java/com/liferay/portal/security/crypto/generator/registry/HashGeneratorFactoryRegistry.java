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

package com.liferay.portal.security.crypto.generator.registry;

import java.util.Set;

import com.liferay.portal.security.crypto.generator.registry.HashRequest.HashRequestBuilder;
import com.liferay.portal.security.crypto.generator.registry.HashRequest.SaltCommand;
import org.json.JSONObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * @author Arthur Chan
 */
@ProviderType
public interface HashGeneratorFactoryRegistry {

	public HashRequestProcessor getHashRequestProcessor(
			String generatorName, JSONObject generatorMeta)
		throws Exception;

	public Set<String> getRegisteredHashGeneratorFactoryNames();

	interface HashRequestProcessor {
		public HashResponse process(HashRequest hashRequest);
	}

	public static void main(String[] args) {
		HashGeneratorFactoryRegistry hashGeneratorFactoryRegistry =
			new HashGeneratorFactoryRegistry() {
			@Override
			public HashRequestProcessor getHashRequestProcessor(
				String generatorName, JSONObject generatorMeta)
				throws Exception {
				return null;
			}

			@Override
			public Set<String> getRegisteredHashGeneratorFactoryNames() {
				return null;
			}
		};

		final HashRequestProcessor hashRequestProcessor =
			hashGeneratorFactoryRegistry.getHashRequestProcessor(
				"MD5", new JSONObject());

		hashRequestProcessor.process(
			HashRequestBuilder.pepper(
				"pepper".getBytes()
			).saltCommand(
				SaltCommand.oneOf(
					SaltCommand.generateVariableSizeSalt(32),
					SaltCommand.generateDefaultSizeSalt()
				)
			).input(
				"password".getBytes()
			)
		);

		hashRequestProcessor.process(
			HashRequestBuilder.pepper(
				"pepper".getBytes()
			).saltCommand(
				SaltCommand.useSalt("storedSalt".getBytes())
			).input(
				"password".getBytes()
			)
		);

	}
}