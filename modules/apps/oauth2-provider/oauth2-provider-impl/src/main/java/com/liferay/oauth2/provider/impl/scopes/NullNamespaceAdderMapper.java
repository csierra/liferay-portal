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

package com.liferay.oauth2.provider.impl.scopes;

import com.liferay.oauth2.provider.api.scopes.NamespaceAdder;
import com.liferay.oauth2.provider.api.scopes.NamespaceAdderMapper;
import com.liferay.oauth2.provider.api.scopes.PropertyGetter;
import org.osgi.service.component.annotations.Component;

@Component(immediate = true)
public class NullNamespaceAdderMapper implements NamespaceAdderMapper {

	@Override
	public NamespaceAdder mapFrom(PropertyGetter propertyHolder) {
		return NamespaceAdder.NULL_ADDER;
	}

}