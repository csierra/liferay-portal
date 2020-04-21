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

package com.liferay.portal.crypto.hash.internal.context.builder;

import com.liferay.portal.crypto.hash.context.builder.HashContextBuilder;
import com.liferay.portal.crypto.hash.context.builder.PepperContextBuilder;

import org.json.JSONObject;

/**
 * @author Arthur Chan
 */
public class PepperContextBuilderImpl
	extends HashContextBuilderImpl implements PepperContextBuilder {

	public PepperContextBuilderImpl(
		String hashProviderName, JSONObject hashProviderMeta) {

		super(hashProviderName, hashProviderMeta, null);
	}

	@Override
	public HashContextBuilder pepperProvider(String pepperProviderName) {
		if (pepperProviderName == null) {
			throw new IllegalArgumentException("pepper can not be null");
		}

		return new HashContextBuilderImpl(
			hashProviderName, hashProviderMeta, pepperProviderName);
	}

}