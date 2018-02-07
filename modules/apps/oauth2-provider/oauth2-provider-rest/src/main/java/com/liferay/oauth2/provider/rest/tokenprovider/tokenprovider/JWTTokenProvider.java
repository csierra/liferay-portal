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

package com.liferay.oauth2.provider.rest.tokenprovider.tokenprovider;

import com.liferay.oauth2.provider.rest.LiferayOAuthDataProvider;
import com.liferay.oauth2.provider.scopes.spi.TokenProvider;
import org.apache.cxf.rs.security.oauth2.utils.OAuthUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Map;

@Component(
	immediate = true,
	property = {
		"token.type=bearer",
		"token.format=jwt",
		"name=jwt",
	}
)
public class JWTTokenProvider implements TokenProvider {

	@Override
	public String createTokenString(TokenRequest tokenRequest) {

	}

	@Override
	public boolean validateToken(
		String tokenString, GrantedTokenRequest grantedTokenRequest) {

		return !OAuthUtils.isExpired(
			grantedTokenRequest.getIssuedAt(),
			grantedTokenRequest.getLifeTime());
	}

	@Activate
	protected void activate(Map<String, Object> properties) {

	}

	@Reference
	private LiferayOAuthDataProvider _liferayOAuthDataProvider;
}
