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

package com.liferay.oauth2.provider.scopes.impl.scopematcher;

import com.liferay.oauth2.provider.scopes.spi.NamespaceApplicator;
import com.liferay.oauth2.provider.scopes.spi.ScopeMatcher;
import com.liferay.oauth2.provider.scopes.spi.ScopeMatcherFactory;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import java.util.Map;

@Component(
	configurationPid = "com.liferay.oauth2.provider.scopes.impl.scopematcher.HierarchicalScopeMatcherFactory",
	property = {
		"default=true",
		"type=chunks",
		"separator=" + StringPool.PERIOD
	}
)
public class HierarchicalScopeMatcherFactory implements ScopeMatcherFactory {

	private String _separator = StringPool.PERIOD;

	@Activate
	protected void activate(Map<String, Object> properties) {
		Object separator = properties.get("separator");

		_separator = separator.toString();

		if (Validator.isNull(_separator)) {
			throw new IllegalArgumentException(
				"separator property can't be null");
		}
	}

	@Override
	public ScopeMatcher createScopeMatcher(String scopesAlias) {
		String[] scopesAliasParts = StringUtil.split(scopesAlias, _separator);

		if (scopesAliasParts.length == 0) {
			return ScopeMatcher.NONE;
		}

		return new HierarchicalScopeMatcher(scopesAlias, scopesAliasParts);
	}

	private class HierarchicalScopeMatcher implements ScopeMatcher {
		private String _scopesAlias;
		private final String[] _scopesAliasParts;

		private HierarchicalScopeMatcher(String scopesAlias, String[] scopesAliasParts) {
			_scopesAlias = scopesAlias;
			_scopesAliasParts = scopesAliasParts;
		}

		@Override
		public boolean match(String scopesAlias) {
			String[] scopesAliasParts = StringUtil.split(scopesAlias, _separator);

			if (scopesAliasParts.length < _scopesAliasParts.length) {
				return false;
			}

			for (int i = 0; i < _scopesAliasParts.length; i++) {
				String thisScopesAliasPart = _scopesAliasParts[i];

				if (thisScopesAliasPart.equals(scopesAliasParts[i])) {
					continue;
				}

				return false;
			}

			return true;
		}

		@Override
		public ScopeMatcher withNamespaceApplicator(NamespaceApplicator namespaceApplicator) {
			String namespace = namespaceApplicator.applyNamespace(StringPool.BLANK);

			if (!_scopesAlias.startsWith(namespace)) {
				return ScopeMatcher.NONE;
			}

			return createScopeMatcher(_scopesAlias.substring(namespace.length()));
		}
	}

}
