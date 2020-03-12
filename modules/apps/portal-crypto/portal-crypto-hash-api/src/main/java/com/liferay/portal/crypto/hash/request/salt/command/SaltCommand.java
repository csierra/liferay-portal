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

package com.liferay.portal.crypto.hash.request.salt.command;

/**
 * @author Carlos Sierra Andrés
 */
public interface SaltCommand {

	/**
	 * A command to HashProcessor to use the given salt
	 *
	 * @param salt the given salt
	 * @return the given salt
	 */
	public static byte[] useSalt(byte[] salt) {
		return salt;
	}

	/**
	 * A command to HashProcessor to use a new salt generated from underlying DefaultSizeSaltGenerator
	 *
	 * @return a default size salt or null if failed
	 */
	public byte[] generateDefaultSizeSalt();

	/**
	 * A command to HashProcessor to use a new salt generated from underlying VariableSizeSaltGenerator
	 *
	 * @param size the size of generated salt
	 * @return A variable size salt or null if failed
	 */
	public byte[] generateVariableSizeSalt(int size);

}