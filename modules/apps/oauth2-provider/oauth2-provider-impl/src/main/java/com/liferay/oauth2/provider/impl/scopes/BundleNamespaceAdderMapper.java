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

package com.liferay.oauth2.provider.impl.scopes;

import com.liferay.oauth2.provider.api.scopes.NamespaceAdder;
import com.liferay.oauth2.provider.api.scopes.NamespaceAdderFactory;
import com.liferay.oauth2.provider.api.scopes.NamespaceAdderMapper;
import com.liferay.oauth2.provider.api.scopes.PropertyGetter;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.Version;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true)
public class BundleNamespaceAdderMapper implements NamespaceAdderMapper {

	private BundleContext _bundleContext;

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;
	}

	@Override
	public NamespaceAdder mapFrom(PropertyGetter propertyHolder) {
		long bundleId = Long.parseLong(
			propertyHolder.get("service.bundleid").toString());

		Bundle bundle = _bundleContext.getBundle(bundleId);

		NamespaceAdder bundleSymbolicNameAdder = _namespaceAdderFactory.create(
			bundle.getSymbolicName());

		Version bundleVersion = bundle.getVersion();

		NamespaceAdder namespaceAdder = bundleSymbolicNameAdder.append(
			_namespaceAdderFactory.create(bundleVersion.toString()));

		Object applicationNameObject = propertyHolder.get("osgi.jaxrs.name");
		String applicationName = applicationNameObject.toString();

		return namespaceAdder.append(
			_namespaceAdderFactory.create(applicationName));
	}

	@Reference
	NamespaceAdderFactory _namespaceAdderFactory;

}