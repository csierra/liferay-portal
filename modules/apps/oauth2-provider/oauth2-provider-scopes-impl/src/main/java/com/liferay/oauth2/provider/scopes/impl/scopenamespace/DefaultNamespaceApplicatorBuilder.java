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
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import java.util.Map;

@Component(
	configurationPid = "com.liferay.oauth2.provider.scopes.impl.scopenamespace.DefaultNamespaceApplicatorBuilder",
	property = {
		"separator=" + StringPool.SLASH
	}
)
public class DefaultNamespaceApplicatorBuilder implements NamespaceApplicatorBuilder {

	private String _separator = StringPool.SLASH;

	@Activate
	protected void activate(Map<String, Object> properties) {
		Object separatorObject = properties.get("separator");

		if (Validator.isNotNull(separatorObject)) {
			_separator = separatorObject.toString();
		}
	}

	@Override
	public NamespaceApplicator build(String namespace) {
		return new DefaultNamespaceApplicator(namespace + _separator);
	}

	@Override
	public NamespaceApplicator build(String ... namespaces) {
		StringBundler sb = new StringBundler(namespaces.length * 2);

		for (String namespace : namespaces) {
			sb.append(namespace);
			sb.append(_separator);
		}

		return new DefaultNamespaceApplicator(sb.toString());
	}
}