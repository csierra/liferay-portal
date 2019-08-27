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

package com.liferay.portal.security.crypto.registry;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.security.crypto.encryptor.spi.Encryptor;
import com.liferay.portal.security.crypto.hasher.spi.Hasher;
import com.liferay.portal.security.crypto.key.generator.spi.PasswordBasedKeyGenerator;

/**
 * @author arthurchan35
 */
@ProviderType
public interface CryptoGeneratorRegistry {

	public Encryptor getEncryptor(String algorithmName);

	public Hasher getHasher(String algorithmName);

	public PasswordBasedKeyGenerator getPasswordBasedKeyGenerator(
		String algorithmName);

}