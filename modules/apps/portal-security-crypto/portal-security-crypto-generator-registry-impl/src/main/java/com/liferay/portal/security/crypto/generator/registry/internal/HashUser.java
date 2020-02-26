/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

package com.liferay.portal.security.crypto.generator.registry.internal;

import com.liferay.portal.security.crypto.generator.registry.HashGeneratorFactoryRegistry;
import com.liferay.portal.security.crypto.generator.registry.HashRequest;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Carlos Sierra Andrés
 */
@Component
public class HashUser {

	private HashRequest.InputBuilder _inputBuilder;

	@Activate
	public void activate() {
		_inputBuilder = _inputBuilder.saltCommand(
			HashRequest.SaltCommand.generateDefaultSizeSalt()
		);

		doSomething(HashRequest.HashRequestBuilder.newBuilder());
	}

	public void doSomething(HashRequest.InputBuilder inputBuilder) {
		inputBuilder.input("input".getBytes());
	}

	@Reference
	HashRequest.SaltHashRequestBuilder _saltHashRequestBuilder;

	@Reference
	HashGeneratorFactoryRegistry.HashRequestProcessor _hashRequestProcessor;
}
