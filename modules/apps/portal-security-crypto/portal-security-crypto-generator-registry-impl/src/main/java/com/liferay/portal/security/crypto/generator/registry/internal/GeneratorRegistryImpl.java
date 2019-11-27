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

package com.liferay.portal.security.crypto.generator.registry.internal;

import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.security.crypto.generator.registry.GeneratorRegistry;
import com.liferay.portal.security.crypto.generator.spi.HashGenerator;
import com.liferay.portal.security.crypto.generator.spi.factory.HashGeneratorFactory;

import org.json.JSONObject;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Arthur Chan
 */
@Component(service = GeneratorRegistry.class)
public class GeneratorRegistryImpl implements GeneratorRegistry {

	@Override
	public HashGenerator getHashGenerator(
			String algorithmName, JSONObject generatorMeta)
		throws Exception {

		HashGeneratorFactory hashGeneratorFactory =
			_hashGeneratorFactories.getService(algorithmName);

		return hashGeneratorFactory.create(algorithmName, generatorMeta);
	}

	@Override
	public HashGenerator getHashGenerator(
			String algorithmName, String generatorMeta)
		throws Exception {

		HashGeneratorFactory hashGeneratorFactory =
			_hashGeneratorFactories.getService(algorithmName);

		return hashGeneratorFactory.create(algorithmName, generatorMeta);
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_hashGeneratorFactories = ServiceTrackerMapFactory.openSingleValueMap(
			bundleContext, HashGeneratorFactory.class,
			"crypto.hash.generator.factory");
	}

	@Deactivate
	protected void deactivate() {
		_hashGeneratorFactories.close();
	}

	private ServiceTrackerMap<String, HashGeneratorFactory>
		_hashGeneratorFactories;

}