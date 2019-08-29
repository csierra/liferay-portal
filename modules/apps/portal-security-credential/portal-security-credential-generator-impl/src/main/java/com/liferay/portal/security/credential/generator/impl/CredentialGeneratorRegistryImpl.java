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

package com.liferay.portal.security.credential.generator.impl;

import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.security.credential.generator.registry.CredentialGeneratorRegistry;
import com.liferay.portal.security.credential.generator.spi.CredentialGenerator;
import com.liferay.portal.security.credential.model.AlgorithmEntry;
import com.liferay.portal.security.credential.model.CredentialMeta;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author arthurchan35
 */
@Component(immediate = true, service = CredentialGeneratorRegistry.class)
public class CredentialGeneratorRegistryImpl
	implements CredentialGeneratorRegistry {

	@Override
	public CredentialGenerator getCredentialGenerator(
		AlgorithmEntry algorithm) {

		return getCredentialGenerator(algorithm.getType());
	}

	@Override
	public CredentialGenerator getCredentialGenerator(CredentialMeta meta) {
		return getCredentialGenerator(meta.getAlgorithmEntry());
	}

	@Override
	public CredentialGenerator getCredentialGenerator(String name) {
		return _credentialGeneratorServiceTrackerMap.getService(name);
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_credentialGeneratorServiceTrackerMap =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext, CredentialGenerator.class,
				"encryption.algorithm");
	}

	@Deactivate
	protected void deactivate() {
		_credentialGeneratorServiceTrackerMap.close();
	}

	private ServiceTrackerMap<String, CredentialGenerator>
		_credentialGeneratorServiceTrackerMap;

}