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

package com.liferay.portal.crypto.hash.request.command.salt;

import org.osgi.annotation.versioning.ProviderType;

/**
 * @author Carlos Sierra Andrés
 */
@ProviderType
public interface SaltCommandVisitor<T> {

	public T visit(FirstAvailableSaltCommand command) throws Exception;

	public T visit(GenerateDefaultSizeSaltCommand command) throws Exception;

	public T visit(GenerateVariableSizeSaltCommand command) throws Exception;

	public T visit(UseSaltCommand command) throws Exception;

}