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

package com.liferay.portal.crypto.hash.generation.context;

import com.liferay.portal.crypto.hash.context.HashContext;
import com.liferay.portal.crypto.hash.generation.context.salt.SaltCommand;

import org.osgi.annotation.versioning.ProviderType;

/**
 * @author Carlos Sierra Andrés
 */
@ProviderType
public interface HashGenerationContext extends HashContext {

	public SaltCommand[] getSaltCommands();

	public interface Builder {

		public HashGenerationContext build();

	}

	public interface PepperBuilder extends SaltCommandBuilder {

		public SaltCommandBuilder pepper(byte[] pepper);

	}

	public interface SaltCommandBuilder extends Builder {

		public Builder saltCommand(SaltCommand... saltCommands);

	}

}