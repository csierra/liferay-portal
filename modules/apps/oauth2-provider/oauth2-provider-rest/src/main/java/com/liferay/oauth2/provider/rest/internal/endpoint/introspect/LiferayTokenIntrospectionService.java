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

package com.liferay.oauth2.provider.rest.internal.endpoint.introspect;

import com.liferay.oauth2.provider.rest.internal.endpoint.constants.OAuth2ProviderRestEndpointConstants;
import com.liferay.oauth2.provider.rest.internal.endpoint.liferay.LiferayOAuthDataProvider;
import com.liferay.oauth2.provider.rest.spi.bearer.token.provider.BearerTokenProvider;
import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken;
import org.apache.cxf.rs.security.oauth2.common.UserSubject;
import org.apache.cxf.rs.security.oauth2.services.AbstractTokenService;
import org.apache.cxf.rs.security.oauth2.tokens.refresh.RefreshToken;
import org.apache.cxf.rs.security.oauth2.utils.OAuthConstants;
import org.apache.cxf.rs.security.oauth2.utils.OAuthUtils;

import javax.ws.rs.Consumes;
import javax.ws.rs.Encoded;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.Objects;

/**
 * @author Tomas Polesovsky
 */
@Path("introspect")
public class LiferayTokenIntrospectionService extends AbstractTokenService {

	public LiferayTokenIntrospectionService(
		LiferayOAuthDataProvider liferayOAuthDataProvider,
		boolean publicClientsEnabled) {

		_liferayOAuthDataProvider = liferayOAuthDataProvider;

		setCanSupportPublicClients(publicClientsEnabled);
		setDataProvider(liferayOAuthDataProvider);
	}

	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response getTokenIntrospection(
		@Encoded MultivaluedMap<String, String> params) {

		Client authenticatedClient = authenticateClientIfNeeded(params);

		String tokenId = params.getFirst(OAuthConstants.TOKEN_ID);
		String tokenTypeHint =
			params.getFirst(OAuthConstants.TOKEN_TYPE_HINT);

		if (tokenTypeHint == null) {
			ServerAccessToken accessToken =
				_liferayOAuthDataProvider.getAccessToken(tokenId);

			if (accessToken != null) {
				return handleAccessToken(authenticatedClient, accessToken);
			}

			RefreshToken refreshToken =
				_liferayOAuthDataProvider.getRefreshToken(tokenId);

			if (refreshToken != null) {
				return handleRefreshToken(
					authenticatedClient, refreshToken);
			}
		}

		else if (OAuthConstants.ACCESS_TOKEN.equals(tokenTypeHint)) {
			ServerAccessToken accessToken =
				_liferayOAuthDataProvider.getAccessToken(tokenId);

			if (accessToken != null) {
				return handleAccessToken(authenticatedClient, accessToken);
			}
		}

		else if (OAuthConstants.REFRESH_TOKEN.equals(tokenTypeHint)) {
			RefreshToken refreshToken =
				_liferayOAuthDataProvider.getRefreshToken(tokenId);

			if (refreshToken != null) {
				return handleRefreshToken(
					authenticatedClient, refreshToken);
			}
		}

		else {
			return createErrorResponseFromErrorCode(
				OAuthConstants.UNSUPPORTED_TOKEN_TYPE);
		}

		return Response.ok(new TokenIntrospection(false)).build();
	}

	protected Response handleAccessToken(
		Client authenticatedClient, ServerAccessToken cxfAccessToken) {

		if (!verifyClient(authenticatedClient, cxfAccessToken)) {
			return createErrorResponseFromErrorCode(
				OAuthConstants.UNAUTHORIZED_CLIENT);
		}

		if (!verifyCXFToken(cxfAccessToken)) {
			return Response.ok(new TokenIntrospection(false)).build();
		}

		BearerTokenProvider.AccessToken accessToken =
			_liferayOAuthDataProvider.fromCXFAccessToken(
				cxfAccessToken);

		BearerTokenProvider bearerTokenProvider =
			_liferayOAuthDataProvider.getBearerTokenProvider(
				accessToken.getOAuth2Application().getCompanyId(),
				accessToken.getOAuth2Application().getClientId());

		if (!bearerTokenProvider.isValid(accessToken)) {
			return Response.ok(new TokenIntrospection(false)).build();
		}

		TokenIntrospection tokenIntrospection =
			_buildTokenIntrospection(cxfAccessToken);

		return Response.ok(tokenIntrospection).build();
	}

	protected Response handleRefreshToken(
		Client authenticatedClient, RefreshToken cxfRefreshToken) {

		if (!verifyClient(authenticatedClient, cxfRefreshToken)) {
			return createErrorResponseFromErrorCode(
				OAuthConstants.UNAUTHORIZED_CLIENT);
		}

		if (!verifyCXFToken(cxfRefreshToken)) {
			return Response.ok(new TokenIntrospection(false)).build();
		}

		BearerTokenProvider.RefreshToken refreshToken =
			_liferayOAuthDataProvider.fromCXFRefreshToken(
				cxfRefreshToken);

		BearerTokenProvider bearerTokenProvider =
			_liferayOAuthDataProvider.getBearerTokenProvider(
				refreshToken.getOAuth2Application().getCompanyId(),
				refreshToken.getOAuth2Application().getClientId());

		if (!bearerTokenProvider.isValid(refreshToken)) {
			return Response.ok(new TokenIntrospection(false)).build();
		}

		TokenIntrospection tokenIntrospection =
			_buildTokenIntrospection(cxfRefreshToken);

		return Response.ok().entity(tokenIntrospection).build();
	}

	protected boolean verifyClient(
		Client authenticatedClient, ServerAccessToken serverAccessToken) {

		if (!_clientsMatch(
			authenticatedClient, serverAccessToken.getClient())) {

			return false;
		}

		Map<String, String> clientProperties =
			authenticatedClient.getProperties();

		if (!clientProperties.containsKey(
			OAuth2ProviderRestEndpointConstants.FEATURE_PREFIX +
			OAuth2ProviderRestEndpointConstants.TOKEN_INTROSPECTION_FEATURE)) {

			return false;
		}

		return true;
	}

	protected boolean verifyCXFToken(ServerAccessToken serverAccessToken) {
		if (OAuthUtils.isExpired(
			serverAccessToken.getIssuedAt(),
			serverAccessToken.getExpiresIn())) {

			return false;
		}

		return true;
	}

	private TokenIntrospection _buildTokenIntrospection(
		ServerAccessToken serverAccessToken) {

		TokenIntrospection tokenIntrospection =
			new TokenIntrospection(true);

		tokenIntrospection.setClientId(
			serverAccessToken.getClient().getClientId());

		if (!serverAccessToken.getScopes().isEmpty()) {
			tokenIntrospection.setScope(
				OAuthUtils.convertPermissionsToScope(
					serverAccessToken.getScopes()));
		}

		UserSubject userSubject = serverAccessToken.getSubject();

		if (userSubject != null) {
			tokenIntrospection.setUsername(
				serverAccessToken.getSubject().getLogin());

			if (userSubject.getId() != null) {
				tokenIntrospection.setSub(userSubject.getId());
			}
		}

		if (!serverAccessToken.getAudiences().isEmpty()) {
			tokenIntrospection.setAud(serverAccessToken.getAudiences());
		}

		if (serverAccessToken.getIssuer() != null) {
			tokenIntrospection.setIss(serverAccessToken.getIssuer());
		}

		tokenIntrospection.setIat(serverAccessToken.getIssuedAt());

		if (serverAccessToken.getExpiresIn() > 0) {
			tokenIntrospection.setExp(
				serverAccessToken.getIssuedAt() +
				serverAccessToken.getExpiresIn());
		}

		tokenIntrospection.setTokenType(serverAccessToken.getTokenType());

		tokenIntrospection.getExtensions().putAll(
			serverAccessToken.getExtraProperties());

		return tokenIntrospection;
	}

	private boolean _clientsMatch(Client client1, Client client2) {
		String client1Id = client1.getClientId();
		String client2Id = client2.getClientId();

		if (!Objects.equals(client1Id, client2Id)) {
			return false;
		}

		Map<String, String> properties = client1.getProperties();

		String companyId1 = properties.get(
			OAuth2ProviderRestEndpointConstants.COMPANY_ID);

		properties = client2.getProperties();

		String companyId2 = properties.get(
			OAuth2ProviderRestEndpointConstants.COMPANY_ID);

		if (!Objects.equals(companyId1, companyId2)) {
			return false;
		}

		return true;
	}

	private LiferayOAuthDataProvider _liferayOAuthDataProvider;
}
