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

package com.liferay.portal.crypto.hash.internal.request.command.salt;

import com.liferay.portal.crypto.hash.request.salt.SaltGenerator;

import org.osgi.service.component.annotations.Component;

/**
 * @author Carlos Sierra Andrés
 */
@Component(service = SaltGenerator.class)
public class SaltGeneratorImpl implements SaltGenerator {

	@Override
	public SaltCommand firstAvailableSaltCommand(SaltCommand... saltCommands) {
		return new FirstAvailableSaltCommand(saltCommands);
	}

	@Override
	public SaltCommand generateDefaultSizeSalt() {
		return new GenerateDefaultSizeSaltCommand();
	}

	@Override
	public SaltCommand generateVariableSizeSalt(int size) {
		return new GenerateVariableSizeSaltCommand(size);
	}

	@Override
	public SaltCommand useSaltCommand(byte[] saltBytes) {
		return new UseSaltCommand(saltBytes);
	}

}