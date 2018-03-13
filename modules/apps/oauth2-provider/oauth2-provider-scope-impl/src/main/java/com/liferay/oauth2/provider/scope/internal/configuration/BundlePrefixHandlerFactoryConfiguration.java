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
import com.liferay.portal.kernel.util.StringPool;

/**
 * @author Tomas Polesovsky
 */
@ExtendedObjectClassDefinition(
	category = "foundation", factoryInstanceLabelAttribute = "configurationName"
)
@Meta.OCD(
	id = "com.liferay.oauth2.provider.scope.internal.configuration.BundlePrefixHandlerFactoryConfiguration",
	factory = true,
	localization = "content/Language", name = "oauth2-BundlePrefixHandlerFactoryConfiguration-name"
)
public interface BundlePrefixHandlerFactoryConfiguration {

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
	public boolean defaultSystemPrefixHandlerFactory();

	@Meta.AD(
		deflt = "true",
		required = false
	)
	public boolean includeBundleSymbolicName();

	@Meta.AD(
		deflt = "", id = "exclued.scopes",
		required = false
	)
	public String[] excludedScopes();

	@Meta.AD(
		deflt = "osgi.jaxrs.name",
		required = false
	)
	public String[] serviceProperties();

	@Meta.AD(
		deflt = StringPool.SLASH, required = false
	)
	public String separator();

}
