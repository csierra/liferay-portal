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

package com.liferay.portal.security.password.hash.provider.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Arthur Chan
 */
@ExtendedObjectClassDefinition(
	category = "password", scope = ExtendedObjectClassDefinition.Scope.SYSTEM
)
@Meta.OCD(
	id = "com.liferay.portal.security.password.hash.provider.configuration.PasswordHashProviderConfiguration",
	localization = "content/Language",
	name = "password-hash-provider-configuration-name"
)
public interface PasswordHashProviderConfiguration {

	/**
	 * Name of hash provider that will be used to hash passwords
	 */
	@Meta.AD(deflt = "PBKDF2", name = "hash-provider-name")
	public String hashProviderName();

	@Meta.AD(
		deflt = "{\"prfName\":\"HmacSHA1\"\\,\"keySize\":160\\,\"rounds\":128000}",
		name = "hash-provider-meta-json"
	)
	public String hashProviderMetaJSON();

}