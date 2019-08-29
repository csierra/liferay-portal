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

package com.liferay.portal.security.credential.generator.registry;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.security.credential.generator.spi.CredentialGenerator;
import com.liferay.portal.security.credential.model.AlgorithmEntry;
import com.liferay.portal.security.credential.model.CredentialMeta;

/**
 * @author arthurchan35
 */
@ProviderType
public interface CredentialGeneratorRegistry {

	public CredentialGenerator getCredentialGenerator(AlgorithmEntry algorithm);

	public CredentialGenerator getCredentialGenerator(CredentialMeta meta);

	public CredentialGenerator getCredentialGenerator(String name);

}