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

package com.liferay.portal.jsonwebservice;

import com.liferay.portal.util.PropsValues;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Carlos Sierra Andrés
 */
public class JSONWebServiceSecurityContext {

	public boolean isCustomInstantiationAllowed(Class<?> type) {
		return _allowedCustomInstantiationClassNames.contains(type.getName());
	}

	private final Set<String> _allowedCustomInstantiationClassNames =
		new HashSet<>(
			Arrays.asList(
				PropsValues.
					JSON_WEB_SERVICE_ALLOWED_CUSTOM_INSTANTIATION_CLASSES));

}