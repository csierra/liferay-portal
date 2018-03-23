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

package com.liferay.oauth2.provider.service.persistence.impl;

import com.liferay.oauth2.provider.model.OAuth2Authorization;
import com.liferay.oauth2.provider.service.persistence.OAuth2AuthorizationPersistence;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;

import java.lang.reflect.Field;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Brian Wing Shun Chan
 * @generated
 */
public class OAuth2AuthorizationFinderBaseImpl extends BasePersistenceImpl<OAuth2Authorization> {
	public OAuth2AuthorizationFinderBaseImpl() {
		setModelClass(OAuth2Authorization.class);

		try {
			Field field = BasePersistenceImpl.class.getDeclaredField(
					"_dbColumnNames");

			field.setAccessible(true);

			Map<String, String> dbColumnNames = new HashMap<String, String>();

			dbColumnNames.put("oAuth2ApplicationScopeAliasesId",
				"oA2AScopeAliasesId");

			field.set(this, dbColumnNames);
		}
		catch (Exception e) {
			if (_log.isDebugEnabled()) {
				_log.debug(e, e);
			}
		}
	}

	@Override
	public Set<String> getBadColumnNames() {
		return getOAuth2AuthorizationPersistence().getBadColumnNames();
	}

	/**
	 * Returns the o auth2 authorization persistence.
	 *
	 * @return the o auth2 authorization persistence
	 */
	public OAuth2AuthorizationPersistence getOAuth2AuthorizationPersistence() {
		return oAuth2AuthorizationPersistence;
	}

	/**
	 * Sets the o auth2 authorization persistence.
	 *
	 * @param oAuth2AuthorizationPersistence the o auth2 authorization persistence
	 */
	public void setOAuth2AuthorizationPersistence(
		OAuth2AuthorizationPersistence oAuth2AuthorizationPersistence) {
		this.oAuth2AuthorizationPersistence = oAuth2AuthorizationPersistence;
	}

	@BeanReference(type = OAuth2AuthorizationPersistence.class)
	protected OAuth2AuthorizationPersistence oAuth2AuthorizationPersistence;
	private static final Log _log = LogFactoryUtil.getLog(OAuth2AuthorizationFinderBaseImpl.class);
}