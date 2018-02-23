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

import com.liferay.oauth2.provider.scope.spi.ScopeDescriptor;
import com.liferay.oauth2.provider.scope.spi.ScopeFinder;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Tomas Polesovsky
 */
public class SAPEntryScopeDescriptorFinder
	implements ScopeDescriptor, ScopeFinder {

	public SAPEntryScopeDescriptorFinder(
		long companyId,
		SAPEntryScopeRegistry sapEntryScopeRegistry) {

		_companyId = companyId;
		_sapEntryScopeRegistry = sapEntryScopeRegistry;
	}

	@Override
	public Map<String, Set<String>> findScopes() {
		List<SAPEntryScope> _sapEntryScopes =
			_sapEntryScopeRegistry.getSAPEntryScopes(_companyId);

		Collection<String> names = new HashSet<>(_sapEntryScopes.size());

		for (SAPEntryScope sapEntryScope : _sapEntryScopes) {
			names.add(sapEntryScope.getScopeName());
		}

		Stream<String> stream = names.stream();

		return stream.collect(
			Collectors.toMap(Function.identity(), Collections::singleton)
		);
	}

	@Override
	public String describeScope(String scope, Locale locale) {
		List<SAPEntryScope> _sapEntryScopes =
			_sapEntryScopeRegistry.getSAPEntryScopes(_companyId);

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

	private long _companyId;
	private SAPEntryScopeRegistry _sapEntryScopeRegistry;

}
