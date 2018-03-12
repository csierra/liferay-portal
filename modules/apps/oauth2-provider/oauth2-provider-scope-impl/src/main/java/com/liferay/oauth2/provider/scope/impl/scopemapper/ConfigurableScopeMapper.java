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

package com.liferay.oauth2.provider.scope.impl.scopemapper;

import com.liferay.oauth2.provider.scope.spi.scope.mapper.ScopeMapper;
import com.liferay.portal.kernel.util.GetterUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;

/**
 * @author Stian Sigvartsen
 */
@Component(
	configurationPid = "com.liferay.oauth2.provider.configuration.ConfigurableScopeMapper",
	configurationPolicy = ConfigurationPolicy.REQUIRE, immediate = true
)
public class ConfigurableScopeMapper implements ScopeMapper {

	public ConfigurableScopeMapper() {
		this(new String[0], false);
	}

	public ConfigurableScopeMapper(String[] mappings, boolean passthrough) {
		_init(mappings, passthrough);
	}

	@Override
	public Set<String> map(String scope) {
		Set<String> mappedScopes = _map.get(scope);

		Set<String> result = new HashSet<>();

		if (mappedScopes != null) {
			result.addAll(mappedScopes);
		}
		else {
			result.add(scope);
		}

		if (_passtrough) {
			result.add(scope);
		}

		return result;
	}

	@Activate
	protected void activate(Map<String, Object> properties) {
		Object mappings = properties.get("mapping");

		if ((mappings == null) || !(mappings instanceof String[])) {
			return;
		}

		boolean passthrough = GetterUtil.getBoolean(
			properties.get("passthrough"));

		_init((String[])mappings, passthrough);
	}

	private void _init(String[] mappings, boolean passthrough) {
		_passtrough = passthrough;

		for (String mapping : mappings) {
			String[] mappingParts = mapping.split("=");

			for (String mappingInput : mappingParts[0].split(",")) {
				if (mappingParts.length < 2) {
					continue;
				}

				Set<String> mappingOutput = _map.computeIfAbsent(
					mappingInput, __ -> new HashSet<>());

				Collections.addAll(mappingOutput, mappingParts[1].split(","));
			}
		}
	}

	private Map<String, Set<String>> _map = new HashMap<>();
	private boolean _passtrough;

}