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
	category = "foundation", factoryInstanceLabelAttribute = "configuration.name"
)
@Meta.OCD(
	id = "com.liferay.oauth2.provider.scope.internal.configuration.BundlePrefixHandlerFactoryConfiguration",
	factory = true,
	localization = "content/Language", name = "oauth2-bundle-prefix-handler-factory-configuration-name"
)
public interface BundlePrefixHandlerFactoryConfiguration {

	@Meta.AD(
		deflt = "", id = "configuration.name",
		name = "configuration-name",
		description = "configuration-name-description", required = false
	)
	public String configurationName();

	@Meta.AD(
		deflt = "", id = "companyId", name = "company-id",
		description = "company-id-description", required = false
	)
	public String companyId();

	@Meta.AD(
		deflt = "false", id = "default",
		name = "default-system-prefixhandlerfactory",
		description = "default-system-prefixhandlerfactory-description",
		required = false
	)
	public boolean defaultSystemPrefixHandlerFactory();

	@Meta.AD(
		deflt = "true", id = "include.bundle.symbolic.name",
		name = "include-bundle-symbolic-name", description = "include-bundle-symbolic-name-description", required = false
	)
	public boolean includeBundleSymbolicName();

	@Meta.AD(
		deflt = "", id = "excluded.scopes", name = "excluded-scopes",
		description = "excluded-scopes-description", required = false
	)
	public String[] excludedScopes();

	@Meta.AD(
		deflt = "osgi.jaxrs.name", id = "service.properties",
		name = "service-properties",
		description = "service-properties-description", required = false
	)
	public String[] serviceProperties();

	@Meta.AD(
		deflt = StringPool.SLASH, id="separator", name="separator",
		description = "separator-description", required = false
	)
	public String separator();

}
