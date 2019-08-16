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

package com.liferay.portal.security.credential.generator.bcrypt;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.security.credential.generator.spi.CredentialGenerator;
import com.liferay.portal.security.credential.model.AlgorithmEntry;
import com.liferay.portal.security.credential.model.CredentialMeta;
import com.liferay.portal.security.crypto.generator.salt.BCryptSaltGenerator;
import com.liferay.portal.security.crypto.generator.secret.BCryptSecretGenerator;

import org.osgi.service.component.annotations.Component;

/**
 * @author arthurchan35
 */
@Component(
	immediate = true, property = "encryption.algorithm=BCRYPT",
	service = CredentialGenerator.class
)
public class BCryptCredentialGenerator implements CredentialGenerator {

	@Override
	public String generateSalt(AlgorithmEntry algorithm) throws Exception {
		JSONObject meta = JSONFactoryUtil.createJSONObject(algorithm.getMeta());

		int rounds = meta.getInt("rounds");

		if (rounds < 1) {
			throw new Exception();
		}

		return BCryptSaltGenerator.generate(rounds);
	}

	@Override
	public String generateSecret(String password, CredentialMeta meta)
		throws Exception {

		return BCryptSecretGenerator.generate(password, meta.getSalt());
	}

}