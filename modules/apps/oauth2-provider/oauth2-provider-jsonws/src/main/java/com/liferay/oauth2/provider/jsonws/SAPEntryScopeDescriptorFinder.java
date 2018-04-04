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

package com.liferay.oauth2.provider.jsonws;

import com.liferay.oauth2.provider.scope.spi.scope.descriptor.ScopeDescriptor;
import com.liferay.oauth2.provider.scope.spi.scope.finder.ScopeFinder;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

/**
 * @author Tomas Polesovsky
 */
public class SAPEntryScopeDescriptorFinder
	implements ScopeDescriptor, ScopeFinder {

	public SAPEntryScopeDescriptorFinder(List<SAPEntryScope> sapEntryScopes) {
		_sapEntryScopes = new ArrayList(sapEntryScopes);
	}

	@Override
	public Collection<String> findScopes() {
		Collection<String> scopes = new HashSet<>(_sapEntryScopes.size());

		for (SAPEntryScope sapEntryScope : _sapEntryScopes) {
			scopes.add(sapEntryScope.getScopeName());
		}

		return scopes;
	}

	@Override
	public String describeScope(String scope, Locale locale) {
		for (SAPEntryScope sapEntryScope : _sapEntryScopes) {
			if (sapEntryScope.getScopeName().equals(scope)) {
				String languageId = LocaleUtil.toLanguageId(locale);

				return LocalizationUtil.getLocalization(
					sapEntryScope.getTitle(), languageId);
			}
		}

		if (_log.isWarnEnabled()) {
			_log.warn("Unable to locate SAPEntry scope " + scope);
		}

		return scope;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SAPEntryScopeDescriptorFinder.class);

	private final List<SAPEntryScope> _sapEntryScopes;

}
