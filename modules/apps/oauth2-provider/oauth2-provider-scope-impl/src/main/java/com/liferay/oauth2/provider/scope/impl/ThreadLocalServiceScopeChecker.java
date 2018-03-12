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

package com.liferay.oauth2.provider.scope.impl;

import com.liferay.oauth2.provider.model.OAuth2ScopeGrant;
import com.liferay.oauth2.provider.scope.ScopeChecker;
import com.liferay.oauth2.provider.scope.liferay.ScopeContext;
import com.liferay.oauth2.provider.service.OAuth2ScopeGrantLocalService;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.Collection;

import org.osgi.framework.Bundle;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Carlos Sierra Andrés
 */
@Component(service = {ScopeChecker.class, ScopeContext.class})
public class ThreadLocalServiceScopeChecker
	implements ScopeChecker, ScopeContext {

	ThreadLocal<Long> _companyIdThreadLocal = ThreadLocal.withInitial(() -> 0L);
	ThreadLocal<String> _bundleSymbolicName = ThreadLocal.withInitial(
		() -> StringPool.BLANK);
	ThreadLocal<String> _applicationName = ThreadLocal.withInitial(
		() -> StringPool.BLANK);
	ThreadLocal<String> _accessToken = ThreadLocal.withInitial(
		() -> StringPool.BLANK);

	@Override
	public boolean checkAllScopes(String... scopes) {
		if (Validator.isNull(scopes)) {
			throw new IllegalArgumentException("Scopes can't be null");
		}

		Collection<OAuth2ScopeGrant> oAuth2ScopeGrants = new ArrayList<>(
			_oAuth2ScopeGrantLocalService.findByA_BSN_C_T(
				_applicationName.get(), _bundleSymbolicName.get(),
				_companyIdThreadLocal.get(), _accessToken.get()));

		if (scopes.length > oAuth2ScopeGrants.size()) {
			return false;
		}

		for (String scope : scopes) {
			if (Validator.isNull(scope)) {
				throw new IllegalArgumentException("Scope can't be null");
			}

			boolean found = oAuth2ScopeGrants.removeIf(
				o -> scope.equals(o.getOAuth2ScopeName()));

			if (!found) {
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean checkAnyScope(String... scopes) {
		if (Validator.isNull(scopes)) {
			throw new IllegalArgumentException("Scopes can't be null");
		}

		Collection<OAuth2ScopeGrant> oAuth2ScopeGrants =
			_oAuth2ScopeGrantLocalService.findByA_BSN_C_T(
				_applicationName.get(), _bundleSymbolicName.get(),
				_companyIdThreadLocal.get(), _accessToken.get());

		for (String scope : scopes) {
			if (Validator.isNull(scope)) {
				throw new IllegalArgumentException("Scope can't be null");
			}

			for (OAuth2ScopeGrant oAuth2ScopeGrant : oAuth2ScopeGrants) {
				if (scope.equals(oAuth2ScopeGrant.getOAuth2ScopeName())) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public boolean checkScope(String scope) {
		if (Validator.isNull(scope)) {
			throw new IllegalArgumentException("Scope can't be null");
		}

		Collection<OAuth2ScopeGrant> oAuth2ScopeGrants =
			_oAuth2ScopeGrantLocalService.findByA_BSN_C_T(
				_applicationName.get(), _bundleSymbolicName.get(),
				_companyIdThreadLocal.get(), _accessToken.get());

		return oAuth2ScopeGrants.stream().anyMatch(
			o -> scope.equals(o.getOAuth2ScopeName()));
	}

	@Override
	public void clear() {
		_applicationName.remove();
		_bundleSymbolicName.remove();
		_companyIdThreadLocal.remove();
		_accessToken.remove();
	}

	@Override
	public void setAccessToken(String accessToken) {
		_accessToken.set(accessToken);
	}

	@Override
	public void setApplicationName(String applicationName) {
		_applicationName.set(applicationName);
	}

	@Override
	public void setBundle(Bundle bundle) {
		_bundleSymbolicName.set(bundle.getSymbolicName());
	}

	@Override
	public void setCompanyId(long companyId) {
		_companyIdThreadLocal.set(companyId);
	}

	@Reference
	OAuth2ScopeGrantLocalService _oAuth2ScopeGrantLocalService;

}