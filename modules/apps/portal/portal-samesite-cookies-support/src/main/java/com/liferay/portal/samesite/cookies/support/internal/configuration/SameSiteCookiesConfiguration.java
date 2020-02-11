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

package com.liferay.portal.samesite.cookies.support.internal.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Tomas Polesovsky
 */
@ExtendedObjectClassDefinition(category = "security-tools")
@Meta.OCD(
	id = "com.liferay.portal.samesite.cookies.support.internal.configuration.SameSiteCookiesConfiguration",
	localization = "content/Language",
	name = "samesite-cookies-configuration-name"
)
public interface SameSiteCookiesConfiguration {

	@Meta.AD(deflt = "true", name = "enabled", required = false)
	public boolean enabled();

	@Meta.AD(
		deflt = "true", description = "apply-to-session-cookie-description",
		name = "apply-to-session-cookie", required = false
	)
	public boolean applyToSessionCookie();

	@Meta.AD(
		deflt = "", description = "cookie-names-description",
		name = "cookie-names", required = false
	)
	public String[] cookieNames();

	@Meta.AD(
		deflt = "JSESSIONID", description = "session-cookie-name-description",
		name = "session-cookie-name", required = false
	)
	public String sessionCookieName();

}