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

package com.liferay.portal.security.crypto.registry.impl;

import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.security.crypto.encryptor.spi.Encryptor;
import com.liferay.portal.security.crypto.hasher.spi.Hasher;
import com.liferay.portal.security.crypto.key.generator.spi.PasswordBasedKeyGenerator;
import com.liferay.portal.security.crypto.registry.CryptoGeneratorRegistry;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author arthurchan35
 */
@Component(immediate = true, service = CryptoGeneratorRegistry.class)
public class CryptoGeneratorRegistryImpl implements CryptoGeneratorRegistry {

	@Override
	public Encryptor getEncryptor(String algorithmName) {
		return _encryptorServiceTrackerMap.getService(algorithmName);
	}

	@Override
	public Hasher getHasher(String algorithmName) {
		return _hasherServiceTrackerMap.getService(algorithmName);
	}

	@Override
	public PasswordBasedKeyGenerator getPasswordBasedKeyGenerator(
		String algorithmName) {

		return _passwordBasedKeyGeneratorServiceTrackerMap.getService(
			algorithmName);
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_encryptorServiceTrackerMap =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext, Encryptor.class, "crypto.encryption.algorithm");

		_hasherServiceTrackerMap = ServiceTrackerMapFactory.openSingleValueMap(
			bundleContext, Hasher.class, "crypto.hashing.algorithm");

		_passwordBasedKeyGeneratorServiceTrackerMap =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext, PasswordBasedKeyGenerator.class,
				"crypto.key.generation.algorithm");
	}

	@Deactivate
	protected void deactivate() {
		_encryptorServiceTrackerMap.close();
		_hasherServiceTrackerMap.close();
		_passwordBasedKeyGeneratorServiceTrackerMap.close();
	}

	private ServiceTrackerMap<String, Encryptor> _encryptorServiceTrackerMap;
	private ServiceTrackerMap<String, Hasher> _hasherServiceTrackerMap;
	private ServiceTrackerMap<String, PasswordBasedKeyGenerator>
		_passwordBasedKeyGeneratorServiceTrackerMap;

}