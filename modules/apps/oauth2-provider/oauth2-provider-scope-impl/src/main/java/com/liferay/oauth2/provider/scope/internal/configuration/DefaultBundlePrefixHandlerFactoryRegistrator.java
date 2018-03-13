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

package com.liferay.oauth2.provider.scope.internal.configuration;

import com.liferay.oauth2.provider.configuration.DefaultBundlePrefixHandlerFactoryRegistratorConfiguration;
import com.liferay.oauth2.provider.scope.impl.prefixhandler.BundleNamespacePrefixHandlerFactory;
import com.liferay.oauth2.provider.scope.spi.prefix.handler.PrefixHandlerFactory;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.util.GetterUtil;

import java.util.Hashtable;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Stian Sigvartsen
 */
@Component(
	configurationPid = "com.liferay.oauth2.provider.configuration.DefaultBundlePrefixHandlerFactoryRegistrator",
	immediate = true
)
public class DefaultBundlePrefixHandlerFactoryRegistrator {

	@Activate
	protected void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		boolean enabled = GetterUtil.getBoolean(
			properties.get("enabled"), true);

		if (enabled) {
			DefaultBundlePrefixHandlerFactoryRegistratorConfiguration
				configuration = ConfigurableUtil.createConfigurable(
				DefaultBundlePrefixHandlerFactoryRegistratorConfiguration.class,
				properties);

			Hashtable<String, Object> prefixHandlerFactoryProperties =
				new Hashtable<>();

			prefixHandlerFactoryProperties.put("default", true);
			prefixHandlerFactoryProperties.put("service.ranking", 0);

			_defaultPrefixHandlerFactoryServiceRegistration =
				bundleContext.registerService(
					PrefixHandlerFactory.class,
					new BundleNamespacePrefixHandlerFactory(
						bundleContext,
						configuration.includeBundleSymbolicName(),
						configuration.serviceProperty(),
						configuration.excludedScope(),
						configuration.separator()),
					prefixHandlerFactoryProperties);
		}
	}

	@Deactivate
	protected void deactivate() {
		if (_defaultPrefixHandlerFactoryServiceRegistration != null) {
			_defaultPrefixHandlerFactoryServiceRegistration.unregister();
		}
	}

	private ServiceRegistration<PrefixHandlerFactory>
		_defaultPrefixHandlerFactoryServiceRegistration;

}