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

package com.liferay.portal.crypto.hash.request.processor.internal.factory;

import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.crypto.hash.generator.HashGenerator;
import com.liferay.portal.crypto.hash.generator.factory.HashGeneratorFactory;
import com.liferay.portal.crypto.hash.request.processor.HashRequestProcessor;
import com.liferay.portal.crypto.hash.request.processor.factory.HashRequestProcessorFactory;
import com.liferay.portal.crypto.hash.request.processor.internal.HashRequestProcessorImpl;

import org.json.JSONObject;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Arthur Chan
 */
@Component(service = HashRequestProcessorFactory.class)
public class HashRequestProcessorFactoryImpl
	implements HashRequestProcessorFactory {

	@Override
	public HashRequestProcessor createHashRequestProcessor(
			String processorName, JSONObject processorMeta)
		throws Exception {

		return new HashRequestProcessorImpl(
			_createHashGenerator(processorName, processorMeta));
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_hashGeneratorFactories = ServiceTrackerMapFactory.openSingleValueMap(
			bundleContext, HashGeneratorFactory.class,
			"crypto.hash.generator.name");
	}

	@Deactivate
	protected void deactivate() {
		_hashGeneratorFactories.close();
	}

	private HashGenerator _createHashGenerator(
			String generatorName, JSONObject generatorMeta)
		throws Exception {

		HashGeneratorFactory hashGeneratorFactory =
			_hashGeneratorFactories.getService(generatorName);

		if (hashGeneratorFactory == null) {
			throw new IllegalArgumentException(
				"There is no generator for algorithm: " + generatorName);
		}

		return hashGeneratorFactory.create(generatorName, generatorMeta);
	}

	private ServiceTrackerMap<String, HashGeneratorFactory>
		_hashGeneratorFactories;

}