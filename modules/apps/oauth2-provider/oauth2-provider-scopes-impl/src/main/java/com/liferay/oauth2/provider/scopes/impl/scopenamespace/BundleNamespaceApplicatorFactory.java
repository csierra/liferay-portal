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

package com.liferay.oauth2.provider.scopes.impl.scopenamespace;

import com.liferay.oauth2.provider.scopes.spi.NamespaceApplicator;
import com.liferay.oauth2.provider.scopes.spi.NamespaceApplicatorBuilder;
import com.liferay.oauth2.provider.scopes.spi.NamespaceApplicatorFactory;
import com.liferay.oauth2.provider.scopes.spi.PropertyGetter;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Version;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component(immediate = true, property = "default=true")
public class BundleNamespaceApplicatorFactory implements NamespaceApplicatorFactory {

	private List<String> _excludedScopes = new ArrayList<>();

	@Override
	public NamespaceApplicator mapFrom(PropertyGetter propertyGetter) {
		long bundleId = Long.parseLong(
			propertyGetter.get("service.bundleid").toString());

		Bundle bundle = _bundleContext.getBundle(bundleId);

		Object applicationNameObject = propertyGetter.get("osgi.jaxrs.name");

		String applicationName = applicationNameObject.toString();

		NamespaceApplicator namespaceApplicator = _namespaceApplicatorBuilder.build(
			bundle.getSymbolicName(), applicationName);

		return (target) -> {
			if (_excludedScopes.contains(target)) {
				return target;
			}

			return namespaceApplicator.applyNamespace(target);
		};
	}

	@Activate
	protected void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		_bundleContext = bundleContext;

		String excludedScopesProperty = MapUtil.getString(
			properties, "excluded.scopes");

		_excludedScopes.addAll(Arrays.asList(
			excludedScopesProperty.split(StringPool.COMMA)));

		_excludedScopes.removeIf(Validator::isBlank);
	}

	private BundleContext _bundleContext;

	@Reference
	NamespaceApplicatorBuilder _namespaceApplicatorBuilder;

}