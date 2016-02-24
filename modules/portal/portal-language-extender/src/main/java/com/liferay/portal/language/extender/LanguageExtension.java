/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p/>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.language.extender;

import com.liferay.portal.kernel.util.CacheResourceBundleLoader;
import com.liferay.portal.kernel.util.ResourceBundleLoader;
import com.liferay.portal.kernel.util.ResourceBundleUtil;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.felix.utils.extender.Extension;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.wiring.BundleCapability;
import org.osgi.framework.wiring.BundleWiring;

/**
 * @author Carlos Sierra Andrés
 */
class LanguageExtension implements Extension {
	private final Bundle _bundle;
	private final List<BundleCapability> _capabilities;
	private List<ServiceRegistration<ResourceBundleLoader>>
		_serviceRegistrations;

	public LanguageExtension(
		Bundle bundle, List<BundleCapability> capabilities) {

		_bundle = bundle;
		_capabilities = capabilities;
	}

	@Override
	public void start() throws Exception {
		BundleContext bundleContext = _bundle.getBundleContext();
		BundleWiring bundleWiring = _bundle.adapt(BundleWiring.class);

		_serviceRegistrations = new ArrayList<>();

		for (BundleCapability capability : _capabilities) {
			Map<String, Object> attributes = capability.getAttributes();

			Object baseName = attributes.get("baseName");

			if (baseName instanceof String) {
				ResourceBundleLoader resourceBundleLoader =
					new CacheResourceBundleLoader(
						ResourceBundleUtil.getResourceBundleLoader(
							(String)baseName, bundleWiring.getClassLoader()));

				Dictionary<String, Object> properties = new Hashtable<>(
					attributes);

				properties.put(
					"bundle.symbolic.name", _bundle.getSymbolicName());
				properties.put(
					"service.ranking", Integer.MIN_VALUE);

				bundleContext.registerService(
					ResourceBundleLoader.class, resourceBundleLoader,
					properties);
			}
		}
	}

	@Override
	public void destroy() throws Exception {
		for (ServiceRegistration<ResourceBundleLoader> serviceRegistration :
				_serviceRegistrations) {

			serviceRegistration.unregister();
		}
	}

}