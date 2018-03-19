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

package com.liferay.oauth2.provider.model.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.Arrays;
import java.util.List;

/**
 * The extended model implementation for the OAuth2RefreshToken service. Represents a row in the &quot;OAuth2RefreshToken&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * Helper methods and all application logic should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.liferay.oauth2.provider.model.OAuth2RefreshToken} interface.
 * </p>
 *
 * @author Brian Wing Shun Chan
 */
@ProviderType
public class OAuth2RefreshTokenImpl extends OAuth2RefreshTokenBaseImpl {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. All methods that expect a o auth2 refresh token model instance should use the {@link com.liferay.oauth2.provider.model.OAuth2RefreshToken} interface instead.
	 */
	public OAuth2RefreshTokenImpl() {
	}

	@Override
	public List<String> getScopeAliasesList() {
		return Arrays.asList(
			StringUtil.split(getScopeAliases(), StringPool.SPACE));
	}

	@Override
	public void setScopeAliasesList(List<String> scopeAliasesList) {
		String scopeAliases = StringUtil.merge(
			scopeAliasesList, StringPool.SPACE);

		setScopeAliases(scopeAliases);
	}

}