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

package com.liferay.portal.crypto.hash.processor.message.digest.factory;

import com.liferay.portal.crypto.hash.processor.HashProcessor;
import com.liferay.portal.crypto.hash.processor.message.digest.MessageDigestHashProcessor;
import com.liferay.portal.crypto.hash.processor.spi.factory.HashProcessorFactory;

import org.json.JSONObject;

import org.osgi.service.component.annotations.Component;

/**
 * @author Arthur Chan
 */
@Component(
	immediate = true,
	property = {
		"crypto.hash.processor.name=MD2", "crypto.hash.processor.name=MD5",
		"crypto.hash.processor.name=SHA-1",
		"crypto.hash.processor.name=SHA-224",
		"crypto.hash.processor.name=SHA-256",
		"crypto.hash.processor.name=SHA-384",
		"crypto.hash.processor.name=SHA-512"
	},
	service = HashProcessorFactory.class
)
public class MessageDigestHashGeneratorFactory implements HashProcessorFactory {

	@Override
	public HashProcessor create(String generatorName, JSONObject generatorMeta)
		throws Exception {

		return new MessageDigestHashProcessor(generatorName);
	}

}