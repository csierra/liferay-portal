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

package com.liferay.oauth2.provider.scope.internal.configuration;

import aQute.bnd.annotation.metatype.Meta;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

@ExtendedObjectClassDefinition(
	category = "foundation", factoryInstanceLabelAttribute = "configurationName"
)
@Meta.OCD(
	id = "com.liferay.oauth2.provider.scope.internal.configuration.ConfigurableScopeMapperConfiguration",
	factory = true, localization = "content/Language",
	name = "oauth2-default-configurable-scopemapper-configuration-name"
)
public interface ConfigurableScopeMapperConfiguration {

	@Meta.AD(
		deflt = "",
		required = false
	)
	public String configurationName();

	@Meta.AD(
		deflt = "",
		required = false
	)
	public String companyId();

	@Meta.AD(
		deflt = "false", id="default",
		required = false
	)
	public boolean defaultSystemScopeMapper();

	@Meta.AD(
		deflt = "GET\\,HEAD\\,OPTIONS\\=everything.readonly," +
			"PUT\\,POST\\,PATCH\\,DELETE=everything\\,everything.writeonly",
		required = false
	)
	public String[] mapping();

	@Meta.AD(
		deflt = "false", required = false
	)
	public boolean passthrough();
}