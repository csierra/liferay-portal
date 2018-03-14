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
 * The extended model implementation for the OAuth2Token service. Represents a row in the &quot;OAuth2Token&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * Helper methods and all application logic should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.liferay.oauth2.provider.model.OAuth2Token} interface.
 * </p>
 *
 * @author Brian Wing Shun Chan
 */
@ProviderType
public class OAuth2TokenImpl extends OAuth2TokenBaseImpl {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. All methods that expect a o auth2 token model instance should use the {@link com.liferay.oauth2.provider.model.OAuth2Token} interface instead.
	 */
	public OAuth2TokenImpl() {
	}

	@Override
	public List<String> getScopesList() {
		return Arrays.asList(StringUtil.split(getScopes(), StringPool.SPACE));
	}

	@Override
	public void setScopesList(List<String> scopesList) {
		String scopes = StringUtil.merge(scopesList, StringPool.SPACE);

		setScopes(scopes);
	}

}