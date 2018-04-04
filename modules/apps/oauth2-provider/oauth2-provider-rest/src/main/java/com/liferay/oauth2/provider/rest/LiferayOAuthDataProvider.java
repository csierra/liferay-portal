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

package com.liferay.oauth2.provider.rest;

import com.liferay.oauth2.provider.configuration.OAuth2ProviderConfiguration;
import com.liferay.oauth2.provider.constants.GrantType;
import com.liferay.oauth2.provider.model.OAuth2Application;
import com.liferay.oauth2.provider.model.OAuth2ApplicationScopeAliases;
import com.liferay.oauth2.provider.model.OAuth2Authorization;
import com.liferay.oauth2.provider.rest.spi.bearer.token.provider.BearerTokenProvider;
import com.liferay.oauth2.provider.rest.spi.bearer.token.provider.BearerTokenProvider.AccessToken;
import com.liferay.oauth2.provider.scope.liferay.LiferayOAuth2Scope;
import com.liferay.oauth2.provider.scope.liferay.ScopeLocator;
import com.liferay.oauth2.provider.scope.liferay.ScopedServiceTrackerMap;
import com.liferay.oauth2.provider.scope.liferay.ScopedServiceTrackerMapFactory;
import com.liferay.oauth2.provider.service.OAuth2ApplicationLocalService;
import com.liferay.oauth2.provider.service.OAuth2ApplicationScopeAliasesLocalService;
import com.liferay.oauth2.provider.service.OAuth2AuthorizationLocalService;
import com.liferay.oauth2.provider.service.OAuth2ScopeGrantLocalService;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.cache.MultiVMPool;
import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.TransactionConfig;
import com.liferay.portal.kernel.transaction.TransactionInvokerUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import org.apache.cxf.rs.security.oauth2.common.AccessTokenRegistration;
import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.common.OAuthPermission;
import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken;
import org.apache.cxf.rs.security.oauth2.common.UserSubject;
import org.apache.cxf.rs.security.oauth2.grants.code.AbstractAuthorizationCodeDataProvider;
import org.apache.cxf.rs.security.oauth2.grants.code.AuthorizationCodeRegistration;
import org.apache.cxf.rs.security.oauth2.grants.code.ServerAuthorizationCodeGrant;
import org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException;
import org.apache.cxf.rs.security.oauth2.tokens.bearer.BearerAccessToken;
import org.apache.cxf.rs.security.oauth2.tokens.refresh.RefreshToken;
import org.apache.cxf.rs.security.oauth2.utils.OAuthConstants;
import org.apache.cxf.rs.security.oauth2.utils.OAuthUtils;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component(
	service = LiferayOAuthDataProvider.class,
	configurationPid = "com.liferay.oauth2.provider.configuration.OAuth2ProviderConfiguration"
)
public class LiferayOAuthDataProvider extends AbstractAuthorizationCodeDataProvider {

	public static final String CLIENT_REMOTE_ADDR_PROPERTY =
		"CLIENT_REMOTE_ADDR_ADDRESS";
	public static final String CLIENT_REMOTE_HOST_PROPERTY =
		"CLIENT_REMOTE_HOST";
	public static final String FEATURE_PREFIX_PROPERTY = "FEATURE-";
	public static final String FEATURES_PROPERTY = "FEATURES";

	private PortalCache<String, ServerAuthorizationCodeGrant>
		_codeGrantsPortalCache;

	private ScopedServiceTrackerMap<BearerTokenProvider>
		_scopedBearerTokenProvider;

	public LiferayOAuthDataProvider() {
		setInvisibleToClientScopes(
			Collections.singletonList(OAuthConstants.REFRESH_TOKEN_SCOPE));
	}

	@Activate
	@SuppressWarnings("unchecked")
	protected void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		_codeGrantsPortalCache = (PortalCache<String, ServerAuthorizationCodeGrant>)
			_multiVMPool.getPortalCache("oauth2-provider-code-grants");
		_oAuth2Configuration =
			ConfigurableUtil.createConfigurable(
				OAuth2ProviderConfiguration.class, properties);
		_codeGrantsPortalCache = (PortalCache<String, ServerAuthorizationCodeGrant>)
			_multiVMPool.getPortalCache("oauth2-provider-code-grants");

		_scopedBearerTokenProvider = _scopedServiceTrackerMapFactory.create(
			bundleContext, BearerTokenProvider.class,
			"liferay.oauth2.client.id", () -> _defaultBearerTokenProvider);
	}

	@Override
	public ServerAuthorizationCodeGrant createCodeGrant(
			AuthorizationCodeRegistration reg)
		throws OAuthServiceException {

		ServerAuthorizationCodeGrant authorizationCodeGrant =
			super.createCodeGrant(reg);

		_codeGrantsPortalCache.put(
			authorizationCodeGrant.getCode(),
			authorizationCodeGrant,
			Math.toIntExact(authorizationCodeGrant.getExpiresIn()));

		return authorizationCodeGrant;
	}

	@Override
	public ServerAccessToken createAccessToken(AccessTokenRegistration reg)
		throws OAuthServiceException {

		List<String> approvedScope = new ArrayList<>(
			reg.getApprovedScope());

		if (approvedScope.isEmpty()) {
			approvedScope.addAll(reg.getClient().getRegisteredScopes());
		}

		reg.setApprovedScope(approvedScope);

		if (!OAuthConstants.CLIENT_CREDENTIALS_GRANT.equals(
			reg.getGrantType())) {

			approvedScope.add(OAuthConstants.REFRESH_TOKEN_SCOPE);
		}

		return super.createAccessToken(reg);
	}

	@Override
	protected ServerAccessToken doCreateAccessToken(
		AccessTokenRegistration accessTokenRegistration) {

		ServerAccessToken serverAccessToken = super.doCreateAccessToken(
			accessTokenRegistration);

		BearerTokenProvider.AccessToken accessToken = fromCXFAccessToken(
			serverAccessToken);

		BearerTokenProvider tokenProvider = getBearerTokenProvider(
			accessToken.getOAuth2Application().getCompanyId(),
			accessToken.getOAuth2Application().getClientId());

		tokenProvider.onBeforeCreate(accessToken);

		serverAccessToken.setAudiences(accessToken.getAudiences());
		serverAccessToken.setClientCodeVerifier(
			accessToken.getClientCodeVerifier());
		serverAccessToken.setExpiresIn(accessToken.getExpiresIn());
		serverAccessToken.setExtraProperties(accessToken.getExtraProperties());
		serverAccessToken.setGrantCode(accessToken.getGrantCode());
		serverAccessToken.setGrantType(accessToken.getGrantType());
		serverAccessToken.setIssuedAt(accessToken.getIssuedAt());
		serverAccessToken.setIssuer(accessToken.getIssuer());
		serverAccessToken.setNonce(accessToken.getNonce());
		serverAccessToken.setParameters(accessToken.getParameters());
		serverAccessToken.setRefreshToken(accessToken.getRefreshToken());
		serverAccessToken.setResponseType(accessToken.getResponseType());
		serverAccessToken.setScopes(
			convertScopeToPermissions(
				serverAccessToken.getClient(), accessToken.getScopes()));
		serverAccessToken.setTokenKey(accessToken.getTokenKey());
		serverAccessToken.setTokenType(accessToken.getTokenType());
		serverAccessToken.getSubject().setId(
			String.valueOf(accessToken.getUserId()));
		serverAccessToken.getSubject().setLogin(accessToken.getUserName());

		return serverAccessToken;
	}

	protected BearerTokenProvider getBearerTokenProvider(
		long companyId, String clientId) {

		return _scopedBearerTokenProvider.getService(companyId, clientId);
	}

	protected BearerTokenProvider.AccessToken fromCXFAccessToken(
		ServerAccessToken serverAccessToken) {

		OAuth2Application oAuth2Application =
			resolveOAuth2Application(serverAccessToken.getClient());

		return new AccessToken(
			oAuth2Application,
			serverAccessToken.getAudiences(),
			serverAccessToken.getClientCodeVerifier(),
			serverAccessToken.getExpiresIn(),
			serverAccessToken.getExtraProperties(),
			serverAccessToken.getGrantCode(),
			serverAccessToken.getGrantType(),
			serverAccessToken.getIssuedAt(),
			serverAccessToken.getIssuer(),
			serverAccessToken.getNonce(),
			serverAccessToken.getParameters(),
			serverAccessToken.getRefreshToken(),
			serverAccessToken.getResponseType(),
			OAuthUtils.convertPermissionsToScopeList(
				serverAccessToken.getScopes()),
			serverAccessToken.getTokenKey(),
			serverAccessToken.getTokenType(),
			Long.parseLong(serverAccessToken.getSubject().getId()),
			serverAccessToken.getSubject().getLogin());
	}

	@Override
	public ServerAccessToken refreshAccessToken(
			Client client, String refreshTokenKey,
			List<String> restrictedScopes)
		throws OAuthServiceException {

		RefreshToken cxfRefreshToken = getRefreshToken(refreshTokenKey);

		BearerTokenProvider.RefreshToken refreshToken =
			fromCXFRefreshToken(cxfRefreshToken);

		OAuth2Application oAuth2Application = resolveOAuth2Application(client);

		BearerTokenProvider tokenProvider =
			getBearerTokenProvider(
				oAuth2Application.getCompanyId(),
				oAuth2Application.getClientId());

		if (!tokenProvider.isValid(refreshToken)) {
			throw new OAuthServiceException(OAuthConstants.ACCESS_DENIED);
		}

		OAuth2Authorization oAuth2Authorization =
			_oAuth2AuthorizationLocalService.
				fetchOAuth2AuthorizationByRefreshTokenContent(refreshTokenKey);

		if (oAuth2Authorization == null) {
			throw new OAuthServiceException(OAuthConstants.ACCESS_DENIED);
		}

		try {
			OAuth2ApplicationScopeAliases oAuth2ApplicationScopeAliases =
				_oAuth2ApplicationScopeAliasesLocalService.
					getOAuth2ApplicationScopeAliases(
						oAuth2Authorization.
							getOAuth2ApplicationScopeAliasesId());

			restrictedScopes =
				oAuth2ApplicationScopeAliases.getScopeAliasesList();
		}
		catch (PortalException pe) {
			_log.error(
				"Unable to find associated application scope aliases" ,pe);

			throw new OAuthServiceException(pe);
		}

		return super.refreshAccessToken(
			client, refreshTokenKey, restrictedScopes);
	}

	@Override
	protected RefreshToken doCreateNewRefreshToken(
		ServerAccessToken serverAccessToken) {

		RefreshToken cxfRefreshToken = super.doCreateNewRefreshToken(
			serverAccessToken);

		BearerTokenProvider.RefreshToken refreshToken =
			fromCXFRefreshToken(cxfRefreshToken);

		BearerTokenProvider tokenProvider = getBearerTokenProvider(
			refreshToken.getOAuth2Application().getCompanyId(),
			refreshToken.getOAuth2Application().getClientId());

		tokenProvider.onBeforeCreate(refreshToken);

		cxfRefreshToken.setAudiences(refreshToken.getAudiences());
		cxfRefreshToken.setClientCodeVerifier(refreshToken.getClientCodeVerifier());
		cxfRefreshToken.setExpiresIn(refreshToken.getExpiresIn());
		cxfRefreshToken.setGrantType(refreshToken.getGrantType());
		cxfRefreshToken.setIssuedAt(refreshToken.getIssuedAt());
		cxfRefreshToken.setScopes(
			convertScopeToPermissions(
				serverAccessToken.getClient(), refreshToken.getScopes()));
		cxfRefreshToken.setTokenKey(refreshToken.getTokenKey());
		cxfRefreshToken.setTokenType(refreshToken.getTokenType());
		cxfRefreshToken.getSubject().setId(
			String.valueOf(refreshToken.getUserId()));
		cxfRefreshToken.getSubject().setLogin(refreshToken.getUserName());

		return cxfRefreshToken;
	}

	protected BearerTokenProvider.RefreshToken fromCXFRefreshToken(
		RefreshToken cxfRefreshToken) {

		OAuth2Application oAuth2Application =
			resolveOAuth2Application(cxfRefreshToken.getClient());

		return new BearerTokenProvider.RefreshToken(
			oAuth2Application,
			cxfRefreshToken.getAudiences(),
			cxfRefreshToken.getClientCodeVerifier(),
			cxfRefreshToken.getExpiresIn(),
			cxfRefreshToken.getGrantType(),
			cxfRefreshToken.getIssuedAt(),
			OAuthUtils.convertPermissionsToScopeList(
				cxfRefreshToken.getScopes()),
			cxfRefreshToken.getTokenKey(),
			cxfRefreshToken.getTokenType(),
			Long.parseLong(cxfRefreshToken.getSubject().getId()),
			cxfRefreshToken.getSubject().getLogin());
	}

	@Override
	protected ServerAccessToken doRefreshAccessToken(
		Client client, RefreshToken oldRefreshToken,
		List<String> restrictedScopes) {

		ServerAccessToken serverAccessToken =
			super.doRefreshAccessToken(
				client, oldRefreshToken, restrictedScopes);

		BearerTokenProvider.AccessToken accessToken = fromCXFAccessToken(
			serverAccessToken);

		BearerTokenProvider tokenProvider = getBearerTokenProvider(
			accessToken.getOAuth2Application().getCompanyId(),
			accessToken.getOAuth2Application().getClientId());

		tokenProvider.onBeforeCreate(accessToken);

		serverAccessToken.setAudiences(accessToken.getAudiences());
		serverAccessToken.setClientCodeVerifier(
			accessToken.getClientCodeVerifier());
		serverAccessToken.setExpiresIn(accessToken.getExpiresIn());
		serverAccessToken.setExtraProperties(accessToken.getExtraProperties());
		serverAccessToken.setGrantCode(accessToken.getGrantCode());
		serverAccessToken.setGrantType(accessToken.getGrantType());
		serverAccessToken.setIssuedAt(accessToken.getIssuedAt());
		serverAccessToken.setIssuer(accessToken.getIssuer());
		serverAccessToken.setNonce(accessToken.getNonce());
		serverAccessToken.setParameters(accessToken.getParameters());
		serverAccessToken.setRefreshToken(accessToken.getRefreshToken());
		serverAccessToken.setResponseType(accessToken.getResponseType());
		serverAccessToken.setScopes(
			convertScopeToPermissions(
				serverAccessToken.getClient(), accessToken.getScopes()));
		serverAccessToken.setTokenKey(accessToken.getTokenKey());
		serverAccessToken.setTokenType(accessToken.getTokenType());
		serverAccessToken.getSubject().setId(
			String.valueOf(accessToken.getUserId()));
		serverAccessToken.getSubject().setLogin(accessToken.getUserName());

		return serverAccessToken;
	}

	@Override
	public ServerAuthorizationCodeGrant removeCodeGrant(String code)
		throws OAuthServiceException {

		if (code == null) {
			return null;
		}

		ServerAuthorizationCodeGrant serverAuthorizationCodeGrant =
			_codeGrantsPortalCache.get(code);

		_codeGrantsPortalCache.remove(code);

		return serverAuthorizationCodeGrant;
	}

	public  ServerAuthorizationCodeGrant getCodeGrant(String code) {
		if (code == null) {
			return null;
		}

		return _codeGrantsPortalCache.get(code);
	}

	@Override
	public List<ServerAuthorizationCodeGrant> getCodeGrants(
			Client client, UserSubject subject)
		throws OAuthServiceException {

		List<String> keys = _codeGrantsPortalCache.getKeys();

		List<ServerAuthorizationCodeGrant> authorizationCodeGrants =
			new ArrayList<>();

		for (String key : keys) {
			ServerAuthorizationCodeGrant serverAuthorizationCodeGrant =
				_codeGrantsPortalCache.get(key);

			if (serverAuthorizationCodeGrant == null) {
				continue;
			}

			if (client.equals(serverAuthorizationCodeGrant.getClient()) &&
				subject.equals(serverAuthorizationCodeGrant.getSubject())) {

				authorizationCodeGrants.add(serverAuthorizationCodeGrant);
			}
		}

		return authorizationCodeGrants;
	}

	@Override
	protected void saveAccessToken(ServerAccessToken serverToken) {
		try {
			_invokeTransactionally(
				() -> _transactionalSaveAccessToken(serverToken));
		}
		catch (Throwable throwable) {
			throw new OAuthServiceException(throwable);
		}
	}

	private void _transactionalSaveAccessToken(ServerAccessToken token) {
		Client client = token.getClient();

		Date createDate = fromCXFTime(token.getIssuedAt());
		Date expirationDate =
			fromCXFTime(token.getIssuedAt() + token.getExpiresIn());

		Map<String, String> clientProperties = client.getProperties();

		String remoteAddr = clientProperties.get(CLIENT_REMOTE_ADDR_PROPERTY);
		String remoteHost = clientProperties.get(CLIENT_REMOTE_HOST_PROPERTY);

		String remoteIPInfo = remoteAddr + ", " + remoteHost;

		if (token.getRefreshToken() != null) {
			OAuth2Authorization oAuth2Authorization =
				_oAuth2AuthorizationLocalService.
					fetchOAuth2AuthorizationByRefreshTokenContent(
						token.getRefreshToken());

			oAuth2Authorization.setAccessTokenContent(token.getTokenKey());
			oAuth2Authorization.setAccessTokenCreateDate(createDate);
			oAuth2Authorization.setAccessTokenExpirationDate(createDate);

			_oAuth2AuthorizationLocalService.updateOAuth2Authorization(
				oAuth2Authorization);

			return;
		}

		OAuth2Application oAuth2Application = resolveOAuth2Application(client);

		long companyId = oAuth2Application.getCompanyId();

		UserSubject subject = token.getSubject();

		long userId = 0;
		String userName = StringPool.BLANK;

		if (subject != null) {
			try {
				userId = Long.parseLong(subject.getId());

				User user = _userLocalService.getUser(userId);

				userName = user.getFullName();
			}
			catch (Exception e) {
				_log.error("Unable to load user " + subject.getId(), e);
				throw new RuntimeException(e);
			}
		}

		OAuth2Authorization oAuth2Authorization =
			_oAuth2AuthorizationLocalService.addOAuth2Authorization(companyId,
				userId, userName, oAuth2Application.getOAuth2ApplicationId(),
				oAuth2Application.getOAuth2ApplicationScopeAliasesId(),
				token.getTokenKey(), createDate, expirationDate, remoteIPInfo,
				null, null, null);

		List<String> scopeList =
			OAuthUtils.convertPermissionsToScopeList(token.getScopes());

		Set<LiferayOAuth2Scope> liferayScopes = new HashSet<>();

		for (String scope : scopeList) {
			liferayScopes.addAll(
				_scopeFinderLocator.getLiferayOAuth2Scopes(companyId, scope));
		}

		try {
			_oAuth2ScopeGrantLocalService.grantScopesToAuthorization(
				oAuth2Authorization.getOAuth2AuthorizationId(), liferayScopes);
		}
		catch (PortalException e) {
			_log.error(
				"Unable to find authorization " + oAuth2Authorization);

			throw new OAuthServiceException(
				"Unable to grant scope for token", e);
		}
	}

	@Override
	protected void saveRefreshToken(RefreshToken refreshToken) {
		Date createDate = fromCXFTime(refreshToken.getIssuedAt());
		Date expirationDate =
			fromCXFTime(refreshToken.getIssuedAt() + refreshToken.getExpiresIn());

		List<String> accessTokens = refreshToken.getAccessTokens();

		if ((accessTokens != null) && !accessTokens.isEmpty()) {
			String accessTokenContent = accessTokens.get(0);

			OAuth2Authorization oAuth2Authorization =
				_oAuth2AuthorizationLocalService.
					fetchOAuth2AuthorizationByAccessTokenContent(
						accessTokenContent);

			oAuth2Authorization.setRefreshTokenContent(refreshToken.getTokenKey());
			oAuth2Authorization.setRefreshTokenCreateDate(createDate);
			oAuth2Authorization.setRefreshTokenExpirationDate(expirationDate);

			_oAuth2AuthorizationLocalService.updateOAuth2Authorization(
				oAuth2Authorization);

			return;
		}

		throw new OAuthServiceException("Unable to find granted authorization");
	}

	@Override
	protected void doRevokeAccessToken(ServerAccessToken accessToken) {
		OAuth2Authorization oAuth2Authorization =
			_oAuth2AuthorizationLocalService.
				fetchOAuth2AuthorizationByAccessTokenContent(
					accessToken.getTokenKey());

		if (oAuth2Authorization == null) {
			return;
		}

		oAuth2Authorization.setAccessTokenContent(null);

		_oAuth2AuthorizationLocalService.updateOAuth2Authorization(
			oAuth2Authorization);
	}

	@Override
	protected void doRevokeRefreshToken(RefreshToken refreshToken) {
		OAuth2Authorization oAuth2Authorization =
			_oAuth2AuthorizationLocalService.
				fetchOAuth2AuthorizationByRefreshTokenContent(
					refreshToken.getTokenKey());

		if (oAuth2Authorization == null) {
			return;
		}

		oAuth2Authorization.setRefreshTokenContent(null);

		_oAuth2AuthorizationLocalService.updateOAuth2Authorization(
			oAuth2Authorization);
	}

	@Override
	protected RefreshToken getRefreshToken(String refreshTokenKey) {
		try {
			OAuth2Authorization oAuth2Authorization =
				_oAuth2AuthorizationLocalService.
					fetchOAuth2AuthorizationByRefreshTokenContent(
						refreshTokenKey);

			if (oAuth2Authorization == null ){
				// audit: trying to use expired token or brute-force token

				return null;
			}

			OAuth2Application oAuth2Application =
				_oAuth2ApplicationLocalService.getOAuth2Application(
					oAuth2Authorization.getOAuth2ApplicationId());

			long issuedAt = toCXFTime(
				oAuth2Authorization.getRefreshTokenCreateDate());

			long expires =  toCXFTime(
				oAuth2Authorization.getRefreshTokenCreateDate());

			long lifetime = expires - issuedAt;

			RefreshToken refreshToken = new RefreshToken(
				populateClient(oAuth2Application), refreshTokenKey, lifetime,
				issuedAt);

			refreshToken.setSubject(
				populateUserSubject(
					oAuth2Authorization.getCompanyId(),
					oAuth2Authorization.getUserId(),
					oAuth2Authorization.getUserName()));

			List<String> accessTokens = Collections.singletonList(
				oAuth2Authorization.getAccessTokenContent());

			refreshToken.setAccessTokens(accessTokens);

			OAuth2ApplicationScopeAliases oAuth2ApplicationScopeAliases =
				_oAuth2ApplicationScopeAliasesLocalService.
					getOAuth2ApplicationScopeAliases(
						oAuth2Authorization.
							getOAuth2ApplicationScopeAliasesId());

			refreshToken.setScopes(
				convertScopeToPermissions(
					refreshToken.getClient(),
					oAuth2ApplicationScopeAliases.getScopeAliasesList()));

			refreshToken.getExtraProperties().put(
				"companyId", Long.toString(oAuth2Authorization.getCompanyId()));

			return refreshToken;
		}
		catch (PortalException e) {
			throw new OAuthServiceException(e);
		}
	}

	@Override
	protected void doRemoveClient(Client c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setClient(Client client) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Client> getClients(UserSubject resourceOwner) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Client getClient(String clientId) {
		long companyId = CompanyThreadLocal.getCompanyId();

		OAuth2Application oAuth2Application =
			_oAuth2ApplicationLocalService.fetchOAuth2Application(
				companyId, clientId);

		if (oAuth2Application == null) {
			//audit: trying non-existing or removed clientId

			return null;
		}

		getMessageContext().put(OAuthConstants.CLIENT_ID, clientId);

		return populateClient(oAuth2Application);
	}

	@Override
	public ServerAccessToken getAccessToken(String accessToken)
		throws OAuthServiceException {

		OAuth2Authorization oAuth2Authorization =
			_oAuth2AuthorizationLocalService.
				fetchOAuth2AuthorizationByAccessTokenContent(accessToken);

		if (oAuth2Authorization == null) {
			// audit: trying to use expired token or brute-force token

			return null;
		}

		try {
			return populateAccessToken(oAuth2Authorization);
		}
		catch (PortalException pe) {
			_log.error("Unable to populate access token", pe);

			throw new OAuthServiceException(pe);
		}
	}

	protected Client populateClient(OAuth2Application oAuth2Application) {
		String clientSecret = oAuth2Application.getClientSecret();

		if (Validator.isBlank(clientSecret)) {
			clientSecret = null;
		}

		Client client = new Client(
			oAuth2Application.getClientId(),
			clientSecret,
			!Validator.isBlank(clientSecret),
			oAuth2Application.getName(),
			oAuth2Application.getHomePageURL());

		List<GrantType> allowedGrantTypes =
			oAuth2Application.getAllowedGrantTypesList();

		List<String> clientGrantTypes = client.getAllowedGrantTypes();

		for (GrantType allowedGrantType : allowedGrantTypes) {
			if (_oAuth2Configuration.allowAuthorizationCodeGrant() &&
					(allowedGrantType == GrantType.AUTHORIZATION_CODE)) {

				clientGrantTypes.add(OAuthConstants.AUTHORIZATION_CODE_GRANT);
			}
			else if (_oAuth2Configuration.allowAuthorizationCodePKCEGrant() &&
					 (allowedGrantType == GrantType.AUTHORIZATION_CODE_PKCE)) {

				clientGrantTypes.add(OAuthConstants.AUTHORIZATION_CODE_GRANT);
				clientGrantTypes.add(
					LiferayAuthorizationCodeGrantHandlerRegistrator.
						AUTHORIZATION_CODE_PKCE_GRANT);
			}
			else if (_oAuth2Configuration.allowClientCredentialsGrant() &&
					 (allowedGrantType == GrantType.CLIENT_CREDENTIALS)) {

				clientGrantTypes.add(OAuthConstants.CLIENT_CREDENTIALS_GRANT);
			}
			else if (
				_oAuth2Configuration.
					allowResourceOwnerPasswordCredentialsGrant() &&
				(allowedGrantType == GrantType.RESOURCE_OWNER_PASSWORD)) {

				clientGrantTypes.add(OAuthConstants.RESOURCE_OWNER_GRANT);
			}
			else if (_oAuth2Configuration.allowRefreshTokenGrant() &&
					 (allowedGrantType == GrantType.REFRESH_TOKEN)) {

				clientGrantTypes.add(OAuthConstants.REFRESH_TOKEN_GRANT);
			} else {
				if (_log.isDebugEnabled()) {
					_log.debug(
						"Unknown or disabled grant type " + allowedGrantType);
				}
			}
		}

		// CXF considers no allowed grant types as allow all!

		if (clientGrantTypes.isEmpty()) {
			clientGrantTypes.add(StringPool.BLANK);
		}

		client.setApplicationDescription(
			oAuth2Application.getDescription());

		if (oAuth2Application.getOAuth2ApplicationScopeAliasesId() > 0) {
			try {
				OAuth2ApplicationScopeAliases oAuth2ApplicationScopeAliases =
					_oAuth2ApplicationScopeAliasesLocalService.
						getOAuth2ApplicationScopeAliases(
							oAuth2Application.
								getOAuth2ApplicationScopeAliasesId());

				client.setRegisteredScopes(
						oAuth2ApplicationScopeAliases.getScopeAliasesList());
			}
			catch (PortalException pe) {
				_log.error(
					"Unable to find associated application scope aliases" ,pe);

				throw new OAuthServiceException(pe);
			}
		}

		client.setRedirectUris(oAuth2Application.getRedirectURIsList());

		client.setSubject(populateUserSubject(
			oAuth2Application.getCompanyId(), oAuth2Application.getUserId(),
			oAuth2Application.getUserName()));

		long companyId = oAuth2Application.getCompanyId();

		Map<String, String> clientProperties = client.getProperties();

		clientProperties.put("companyId", Long.toString(companyId));

		clientProperties.put(
			FEATURES_PROPERTY, oAuth2Application.getFeatures());

		for (String feature : oAuth2Application.getFeaturesList()) {
			clientProperties.put(FEATURE_PREFIX_PROPERTY + feature, feature);
		}

		return client;
	}

	protected ServerAccessToken populateAccessToken(
		OAuth2Authorization oAuth2Authorization) throws PortalException {

		OAuth2Application oAuth2Application =
			_oAuth2ApplicationLocalService.fetchOAuth2Application(
				oAuth2Authorization.getOAuth2ApplicationId());

		if (oAuth2Application == null) {
			throw new SystemException(
				"Inconsistent DB state! No application found for " +
					"authorization " + oAuth2Authorization);
		}

		Client client = getClient(oAuth2Application.getClientId());

		long issuedAt = toCXFTime(
			oAuth2Authorization.getAccessTokenCreateDate());

		long expires = toCXFTime(
			oAuth2Authorization.getAccessTokenExpirationDate());

		long lifetime = expires - issuedAt;

		BearerAccessToken bearerAccessToken = new BearerAccessToken(
			client, oAuth2Authorization.getAccessTokenContent(), lifetime,
			issuedAt);

		bearerAccessToken.setSubject(
			populateUserSubject(
				oAuth2Authorization.getCompanyId(), oAuth2Authorization.getUserId(),
				oAuth2Authorization.getUserName()));

		OAuth2ApplicationScopeAliases oAuth2ApplicationScopeAliases =
			_oAuth2ApplicationScopeAliasesLocalService.
				getOAuth2ApplicationScopeAliases(
					oAuth2Authorization.
						getOAuth2ApplicationScopeAliasesId());

		List<OAuthPermission> permissions =
			convertScopeToPermissions(
				client,
				oAuth2ApplicationScopeAliases.getScopeAliasesList());
		
		bearerAccessToken.setScopes(permissions);

		bearerAccessToken.getExtraProperties().put(
			"companyId", Long.toString(oAuth2Authorization.getCompanyId()));

		return bearerAccessToken;
	}

	protected UserSubject populateUserSubject(
		long companyId, long userId, String userName) {

		UserSubject userSubject = new UserSubject(
			userName, Long.toString(userId));

		userSubject.getProperties().put("companyId", Long.toString(companyId));

		return userSubject;
	}

	protected OAuth2Application resolveOAuth2Application(Client client) {
		long companyId = Long.parseLong(
			client.getProperties().get("companyId"));

		OAuth2Application oAuth2Application =
			_oAuth2ApplicationLocalService.fetchOAuth2Application(
				companyId, client.getClientId());

		if (oAuth2Application == null) {
			// audit: possible attack on client_id

			return null;
		}

		return oAuth2Application;
	}

	@Override
	public List<ServerAccessToken> getAccessTokens(
			Client client, UserSubject subject)
		throws OAuthServiceException {

		return null;
	}

	@Override
	public List<RefreshToken> getRefreshTokens(
			Client client, UserSubject subject)
		throws OAuthServiceException {

		return null;
	}

	@Override
	public List<OAuthPermission> convertScopeToPermissions(
		Client client, List<String> requestedScopes) {

		List<String> invisibleToClientScopes = getInvisibleToClientScopes();

		List<OAuthPermission> permissions = new ArrayList<>();

		for (String requestedScope : requestedScopes) {
			OAuthPermission oAuthPermission = new OAuthPermission(
				requestedScope);

			if (invisibleToClientScopes.contains(
				oAuthPermission.getPermission())) {

				oAuthPermission.setInvisibleToClient(true);
			}

			permissions.add(oAuthPermission);
		}

		return permissions;
	}

	protected Date fromCXFTime(long issuedAt) {
		return new Date(issuedAt * 1000);
	}

	protected long toCXFTime(Date dateCreated) {
		return dateCreated.getTime() / 1000;
	}

	private static Log _log =
		LogFactoryUtil.getLog(LiferayOAuthDataProvider.class);

	private static void _invokeTransactionally(Runnable runnable)
		throws Throwable{

		TransactionInvokerUtil.invoke(
			TransactionConfig.Factory.create(
				Propagation.REQUIRED, new Class[]{Exception.class}),
			() -> { runnable.run(); return null;});
	}

	@Reference
	private MultiVMPool _multiVMPool;

	@Reference
	private OAuth2AuthorizationLocalService _oAuth2AuthorizationLocalService;

	@Reference
	private OAuth2ApplicationLocalService _oAuth2ApplicationLocalService;

	@Reference
	private OAuth2ScopeGrantLocalService _oAuth2ScopeGrantLocalService;

	@Reference
	private OAuth2ApplicationScopeAliasesLocalService
		_oAuth2ApplicationScopeAliasesLocalService;

	@Reference
	private UserLocalService _userLocalService;

	@Reference
	private ScopeLocator _scopeFinderLocator;

	@Reference
	private ConfigurationProvider _configurationProvider;
	
	private OAuth2ProviderConfiguration _oAuth2Configuration;

	@Reference(
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(name=default)"
	)
	private BearerTokenProvider _defaultBearerTokenProvider;

	@Reference
	ScopedServiceTrackerMapFactory _scopedServiceTrackerMapFactory;
}
