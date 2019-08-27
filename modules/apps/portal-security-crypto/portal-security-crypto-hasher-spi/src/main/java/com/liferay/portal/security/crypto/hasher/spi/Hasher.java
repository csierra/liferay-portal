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

package com.liferay.portal.security.crypto.hasher.spi;

import com.liferay.portal.kernel.json.JSONObject;

/**
 * @author arthurchan35
 */
public interface Hasher {

	public byte[] hash(
			byte[] toBeHashed, byte[] salt, byte[] pepper,
			JSONObject algorithmMeta)
		throws Exception;

	public byte[] hash(
			String toBeHashed, String salt, String pepper,
			JSONObject algorithmMeta)
		throws Exception;

}