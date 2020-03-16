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

package com.liferay.portal.crypto.hash.internal.processor.factory.registry;

import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.crypto.hash.processor.HashProcessor;
import com.liferay.portal.crypto.hash.processor.factory.registry.HashProcessorFactoryRegistry;
import com.liferay.portal.crypto.hash.processor.spi.factory.HashProcessorFactory;

import org.json.JSONObject;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Arthur Chan
 */
@Component(service = HashProcessorFactoryRegistry.class)
public class HashProcessorFactoryRegistryImpl
	implements HashProcessorFactoryRegistry {

	@Override
	public HashProcessor createHashProcessor(
			String processorName, JSONObject processorMeta)
		throws Exception {

		HashProcessorFactory hashProcessorFactory =
			_hashProcessorFactories.getService(processorName);

		if (hashProcessorFactory == null) {
			throw new IllegalArgumentException(
				"There is no processor for algorithm: " + processorName);
		}

		return hashProcessorFactory.create(processorName, processorMeta);
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_hashProcessorFactories = ServiceTrackerMapFactory.openSingleValueMap(
			bundleContext, HashProcessorFactory.class,
			"crypto.hash.processor.name");
	}

	@Deactivate
	protected void deactivate() {
		_hashProcessorFactories.close();
	}

	private ServiceTrackerMap<String, HashProcessorFactory>
		_hashProcessorFactories;

}