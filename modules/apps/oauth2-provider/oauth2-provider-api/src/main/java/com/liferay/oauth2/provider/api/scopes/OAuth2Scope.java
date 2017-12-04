/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.oauth2.provider.api.scopes;

import java.io.Serializable;
import java.util.Locale;

/**
 * @author Carlos Sierra Andrés
 */
public interface OAuth2Scope {

	/**
	 *
	 * @return a unique identifier for the grant that has to be matched against
	 *  the different points in the API
	 */
	public String scopeUUID();

	/**
	 * @param locale
	 * @return human readable description of the scope for the given locale.
	 */
	public String getDescription(Locale locale);

}
