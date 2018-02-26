/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.oauth2.provider.scope.spi.impl.scopemapper;

import com.liferay.oauth2.provider.configuration.DefaultScopeMapperConfiguration;
import com.liferay.oauth2.provider.scope.spi.scopemapper.ScopeMapper;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component(
	immediate = true,
	configurationPid = "com.liferay.oauth2.provider.configuration.DefaultScopeMapperConfiguration",
	property = {
		"default=true",
		"companyId=0"
	}
)
public class DefaultScopeMapper implements ScopeMapper {

	private Map<String,Set<String>> _map = new HashMap<>();
	
	@Activate
	protected void activate(Map<String, Object> properties) {
		
		DefaultScopeMapperConfiguration configuration = 
			ConfigurableUtil.createConfigurable(
				DefaultScopeMapperConfiguration.class, properties);
		
		_passtrough = configuration.passthrough();

		String[] mappings = configuration.mapping();
		
		if (mappings == null) {
			return;
		} 
		
		for (String mapping : mappings) {
			String[] mappingParts = mapping.split("=");
			for (String mappingInput : mappingParts[0].split(",")) {
				
				if (mappingParts.length < 2) {
					continue;
				}
				
				Set<String> mappingOutput = 
					_map.computeIfAbsent(mappingInput, __ -> new HashSet<>());
				
				mappingOutput.addAll(Arrays.asList(mappingParts[1].split(",")));
			}		
		}
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
	
	private boolean _passtrough;	
}
