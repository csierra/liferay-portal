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

package com.liferay.portal.security.credential.generator.spi;

import com.liferay.portal.security.credential.model.AlgorithmEntry;
import com.liferay.portal.security.credential.model.CredentialMeta;

/**
 * @author arthurchan35
 */
public interface CredentialGenerator {

	public String generateSalt(AlgorithmEntry algorithm) throws Exception;

	/**
	 * Simple String comparison between two secrets is vunlerable to timing attack, use {@link CredentialEntry#timingAttackProofEquals(String)} when a comparison is needed
	 *
	 * @param Password the given password to to encrypted
	 * @param Meta the credential meta used to encrypt the given password
	 * @return The generated secret
	 */
	public String generateSecret(String password, CredentialMeta meta)
		throws Exception;

}