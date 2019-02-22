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

package com.liferay.oauth2.provider.jsonws.internal.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Tomas Polesovsky
 */
@ExtendedObjectClassDefinition(
	category = "oauth2", scope = ExtendedObjectClassDefinition.Scope.SYSTEM,
	factoryInstanceLabelAttribute = "osgi.jaxrs.name"
)
@Meta.OCD(
	id = "com.liferay.oauth2.provider.jsonws.internal.configuration.OAuth2JSONWSApplication",
	localization = "content/Language", name = "oauth2-jsonws-application-name",
	factory = true
)
public interface OAuth2JSONWSApplication {

	@Meta.AD(
		description = "osgi-jaxrs-name-description", id = "osgi.jaxrs.name",
		name = "osgi-jaxrs-name"
	)
	public String osgiJaxrsName();

	@Meta.AD(
		description = "oauth2-republished-scopes-description",
		id = "oauth2.republished.scopes", name = "oauth2.republished.scopes"
	)
	public String[] oauth2RepublishedScopes();

}