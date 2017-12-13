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
import com.liferay.portal.kernel.model.Company;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Reference;

public class DefaultServiceReferenceNamespaceAdderFactory
	implements NamespaceAdderFactory<ServiceReference<?>> {

	@Reference
	NamespaceAdderFactory<String> _stringNamespaceAdderFactory;

	@Override
	public NamespaceAdder create(ServiceReference<?> serviceReference) {
		return _stringNamespaceAdderFactory.create(
			serviceReference.getProperty("osgi.jaxrs.name").toString());
	}

}
