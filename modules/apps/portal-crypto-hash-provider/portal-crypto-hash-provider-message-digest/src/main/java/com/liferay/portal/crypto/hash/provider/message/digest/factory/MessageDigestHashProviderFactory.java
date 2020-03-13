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

package com.liferay.portal.crypto.hash.provider.message.digest.factory;

import com.liferay.portal.crypto.hash.provider.message.digest.MessageDigestHashProvider;
import com.liferay.portal.crypto.hash.provider.spi.HashProvider;
import com.liferay.portal.crypto.hash.provider.spi.factory.HashProviderFactory;

import org.json.JSONObject;

import org.osgi.service.component.annotations.Component;

/**
 * @author Arthur Chan
 */
@Component(
	immediate = true,
	property = {
		"crypto.hash.provider.name=MD2", "crypto.hash.provider.name=MD5",
		"crypto.hash.provider.name=SHA-1", "crypto.hash.provider.name=SHA-224",
		"crypto.hash.provider.name=SHA-256",
		"crypto.hash.provider.name=SHA-384", "crypto.hash.provider.name=SHA-512"
	},
	service = HashProviderFactory.class
)
public class MessageDigestHashProviderFactory implements HashProviderFactory {

	@Override
	public HashProvider create(String providerName, JSONObject providerMeta)
		throws Exception {

		return new MessageDigestHashProvider(providerName);
	}

}