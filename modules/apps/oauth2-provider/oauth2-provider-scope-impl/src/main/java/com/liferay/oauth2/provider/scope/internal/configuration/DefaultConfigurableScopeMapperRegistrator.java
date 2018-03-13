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

import com.liferay.oauth2.provider.configuration.DefaultConfigurableScopeMapperRegistratorConfiguration;
import com.liferay.oauth2.provider.scope.impl.scopemapper.ConfigurableScopeMapper;
import com.liferay.oauth2.provider.scope.spi.scope.mapper.ScopeMapper;
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
	configurationPid = "com.liferay.oauth2.provider.configuration.DefaultConfigurableScopeMapperRegistratorConfiguration",
	immediate = true
)
public class DefaultConfigurableScopeMapperRegistrator {

	@Activate
	protected void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		boolean enabled = GetterUtil.getBoolean(
			properties.get("enabled"), true);

		Class<DefaultConfigurableScopeMapperRegistratorConfiguration> clazz =
			DefaultConfigurableScopeMapperRegistratorConfiguration.class;

		if (enabled) {
			DefaultConfigurableScopeMapperRegistratorConfiguration
				configuration = ConfigurableUtil.createConfigurable(
					clazz, properties);

			Hashtable<String, Object> scopeMapperProperties = new Hashtable<>();

			scopeMapperProperties.put("default", true);

			_defaultScopeMapperServiceRegistration =
				bundleContext.registerService(
					ScopeMapper.class,
					new ConfigurableScopeMapper(
						configuration.mapping(), configuration.passthrough()),
					scopeMapperProperties);
		}
	}

	@Deactivate
	protected void deactivate() {
		if (_defaultScopeMapperServiceRegistration != null) {
			_defaultScopeMapperServiceRegistration.unregister();
		}
	}

	private ServiceRegistration<ScopeMapper>
		_defaultScopeMapperServiceRegistration;

}