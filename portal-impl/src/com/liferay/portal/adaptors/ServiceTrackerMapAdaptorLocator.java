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

package com.liferay.portal.adaptors;

import com.liferay.portal.kernel.adaptors.Adaptor;
import com.liferay.portal.kernel.adaptors.AdaptorLocator;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.collections.ServiceReferenceMapper;
import com.liferay.registry.collections.ServiceTrackerCollections;
import com.liferay.registry.collections.ServiceTrackerMap;
import java8.util.Optional;

import java.lang.reflect.Type;

/**
 * @author Carlos Sierra Andrés
 */
public class ServiceTrackerMapAdaptorLocator implements AdaptorLocator {


	@Override
	public <R, T> Optional<Adaptor<R, T>> locate(Class<R> from, Class<T> to) {
		return Optional.empty();
	}
	
	private static ServiceTrackerMap<String, Adaptor> _adaptorsMap =
		ServiceTrackerCollections.singleValueMap(
			Adaptor.class, null, 
			new ServiceReferenceMapper<String, Adaptor>() {
				
				@Override
				public void map(
					ServiceReference<Adaptor> serviceReference, 
					Emitter<String> emitter) {

					Registry registry = RegistryUtil.getRegistry();

					Adaptor adaptor = registry.getService(serviceReference);

					Type[] genericInterfaces =
						adaptor.getClass().getGenericInterfaces();

					emitter.emit(
						genericInterfaces[0].toString() + "->" +
							genericInterfaces[1].toString());
				}
			});
}
