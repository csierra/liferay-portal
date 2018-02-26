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

package com.liferay.oauth2.provider.scope.impl.prefixhandler;

import com.liferay.oauth2.provider.scope.spi.prefixhandler.PrefixHandler;
import com.liferay.oauth2.provider.scope.spi.prefixhandler.PrefixHandlerFactory;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component(
	immediate = true, 
	property = {
		"default=true", 
		"separator=" + StringPool.SLASH
	}
)
public class BundleNamespacePrefixHandlerFactory implements
	PrefixHandlerFactory {
	
	private List<String> _excludedScopes = new ArrayList<>();
	private String _separator = StringPool.SLASH;
	
	@Override
	public PrefixHandler create(Function<String,Object> serviceProperties) {
		long bundleId = Long.parseLong(
			serviceProperties.apply("service.bundleid").toString());

		Bundle bundle = _bundleContext.getBundle(bundleId);

		Object applicationNameObject = serviceProperties.apply("osgi.jaxrs.name");

		String applicationName = applicationNameObject.toString();

		PrefixHandler prefixHandler = create(
			bundle.getSymbolicName(), applicationName);

		return (target) -> {
			if (_excludedScopes.contains(target)) {
				return target;
			}

			return prefixHandler.addPrefix(target);
		};
	}

	@Activate
	protected void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		_bundleContext = bundleContext;

		String excludedScopesProperty = MapUtil.getString(
			properties, "excluded.scope");

		_excludedScopes.addAll(Arrays.asList(
			excludedScopesProperty.split(StringPool.COMMA)));

		_excludedScopes.removeIf(Validator::isBlank);
		
		Object separatorObject = properties.get("separator");

		if (Validator.isNotNull(separatorObject)) {
			_separator = separatorObject.toString();
		}		
	}

	private BundleContext _bundleContext;

	public PrefixHandler create(String ... prefixes) {
		StringBundler sb = new StringBundler(prefixes.length * 2);

		for (String namespace : prefixes) {
			sb.append(namespace);
			sb.append(_separator);
		}

		return (target) -> sb.toString() + target;
	}

}