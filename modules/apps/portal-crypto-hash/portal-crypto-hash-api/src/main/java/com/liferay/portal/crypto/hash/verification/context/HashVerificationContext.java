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

package com.liferay.portal.crypto.hash.verification.context;

import com.liferay.portal.crypto.hash.context.HashContext;

import java.util.Optional;

import org.osgi.annotation.versioning.ProviderType;

/**
 * @author Carlos Sierra Andrés
 */
@ProviderType
public interface HashVerificationContext extends HashContext {

	public Optional<byte[]> getSalt();

	public interface Builder {

		public HashVerificationContext build();

	}

	public interface PepperBuilder extends SaltBuilder {

		public SaltBuilder pepper(byte[] pepper);

	}

	public interface SaltBuilder extends Builder {

		public Builder salt(byte[] salt);

	}

}