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

package com.liferay.portal.crypto.hash.context.builder;

import com.liferay.portal.crypto.hash.generation.context.HashGenerationContext;
import com.liferay.portal.crypto.hash.verification.context.HashVerificationContext;

import org.json.JSONObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * @author Carlos Sierra Andrés
 */
@ProviderType
public interface HashContextBuilder {

	public HashGenerationContext.PepperBuilder generationHashProvider(
			String hashProviderName)
		throws Exception;

	public HashGenerationContext.PepperBuilder generationHashProvider(
			String hashProviderName, JSONObject hashProviderMeta)
		throws Exception;

	public HashVerificationContext.PepperBuilder verificationHashProvider(
			String hashProviderName)
		throws Exception;

	public HashVerificationContext.PepperBuilder verificationHashProvider(
			String hashProviderName, JSONObject hashProviderMeta)
		throws Exception;

}