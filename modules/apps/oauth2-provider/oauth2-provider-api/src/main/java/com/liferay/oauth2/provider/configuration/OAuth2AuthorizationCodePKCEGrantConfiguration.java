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

package com.liferay.oauth2.provider.configuration;

import aQute.bnd.annotation.metatype.Meta;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

@ExtendedObjectClassDefinition(
	category = "foundation", scope = ExtendedObjectClassDefinition.Scope.COMPANY
)
@Meta.OCD(
	id = "com.liferay.oauth2.provider.configuration.OAuth2AuthorizationCodePKCEGrantConfiguration",
	localization = "content/Language", name = "oauth2-authorization-code-pkce-grant-configuration-name"
)
public interface OAuth2AuthorizationCodePKCEGrantConfiguration {

	@Meta.AD(
		deflt = "true", id = "oauth2.authorization.code.pkce.grant.enabled",
		name = "oauth2-authorization-code-pkce-grant-enabled", required = false
	)
	public boolean enabled();

}