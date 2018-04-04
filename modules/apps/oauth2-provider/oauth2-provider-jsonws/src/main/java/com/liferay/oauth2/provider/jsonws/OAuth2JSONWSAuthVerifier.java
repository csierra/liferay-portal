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

import com.liferay.oauth2.provider.constants.OAuth2ProviderConstants;
import com.liferay.oauth2.provider.exception.NoSuchOAuth2AuthorizationException;
import com.liferay.oauth2.provider.model.OAuth2Application;
import com.liferay.oauth2.provider.model.OAuth2ApplicationScopeAliases;
import com.liferay.oauth2.provider.model.OAuth2Authorization;
import com.liferay.oauth2.provider.rest.spi.bearer.token.provider.BearerTokenProvider;
import com.liferay.oauth2.provider.scope.liferay.LiferayOAuth2Scope;
import com.liferay.oauth2.provider.scope.liferay.ScopeLocator;
import com.liferay.oauth2.provider.scope.liferay.ScopedServiceTrackerMap;
import com.liferay.oauth2.provider.scope.liferay.ScopedServiceTrackerMapFactory;
import com.liferay.oauth2.provider.service.OAuth2ApplicationLocalService;
import com.liferay.oauth2.provider.service.OAuth2ApplicationScopeAliasesLocalService;
import com.liferay.oauth2.provider.service.OAuth2AuthorizationLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.AccessControlContext;
import com.liferay.portal.kernel.security.auth.AuthException;
import com.liferay.portal.kernel.security.auth.verifier.AuthVerifier;
import com.liferay.portal.kernel.security.auth.verifier.AuthVerifierResult;
import com.liferay.portal.kernel.security.service.access.policy.ServiceAccessPolicyThreadLocal;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * @author Tomas Polesovsky
 */
@Component(
	immediate = true,
	property = {
		"auth.verifier.OAuth2JSONWSAuthVerifier.urls.includes=/api/jsonws/*"
	}
)
public class OAuth2JSONWSAuthVerifier implements AuthVerifier {

	@Activate
	public void activate(BundleContext bundleContext){
		_bundleContext = bundleContext;

		_scopedBearerTokenProvider = _scopedServiceTrackerMapFactory.create(
			bundleContext, BearerTokenProvider.class,
			"liferay.oauth2.client.id", () -> _defaultBearerTokenProvider);
	}

	@Override
	public String getAuthType() {
		return "OAuth2";
	}

	@Override
	public AuthVerifierResult verify(
			AccessControlContext accessControlContext, Properties properties)
		throws AuthException {

		AuthVerifierResult authVerifierResult = new AuthVerifierResult();

		try {
			BearerTokenProvider.AccessToken accessToken =
				getAccessToken(accessControlContext);

			if (accessToken == null) {
				return authVerifierResult;
			}

			long companyId = accessToken.getOAuth2Application().getCompanyId();

			BearerTokenProvider bearerTokenProvider =
				_scopedBearerTokenProvider.getService(
					companyId,
					accessToken.getOAuth2Application().getClientId());

			if (bearerTokenProvider == null) {
				return authVerifierResult;
			}

			if (!bearerTokenProvider.isValid(accessToken)) {
				return authVerifierResult;
			}

			Set<String> scopeNames = new HashSet<>();

			String auth2PortalJSONWSApplicationName =
				_oAuth2SAPEntryScopesPublisher.
					getOAuth2PortalJSONWSApplicationName();

			for (String accessTokenScope : accessToken.getScopes()) {
				Collection<LiferayOAuth2Scope> liferayOAuth2Scopes =
					_scopeFinderLocator.getLiferayOAuth2Scopes(
						companyId, accessTokenScope,
						auth2PortalJSONWSApplicationName);
				
				for (LiferayOAuth2Scope liferayOAuth2Scope :
					liferayOAuth2Scopes) {

					scopeNames.add(liferayOAuth2Scope.getScope());
				}				
			}

			List<SAPEntryScope> sapEntryScopes =
				_sapEntryScopeRegistry.getSAPEntryScopes(companyId);

			for (SAPEntryScope sapEntryScope : sapEntryScopes) {
				if (scopeNames.contains(sapEntryScope.getScopeName())) {
					ServiceAccessPolicyThreadLocal.
						addActiveServiceAccessPolicyName(
							sapEntryScope.getSapEntryName());
				}
			}

			authVerifierResult.getSettings().put(
				BearerTokenProvider.AccessToken.class.getName(), accessToken);

			authVerifierResult.setState(AuthVerifierResult.State.SUCCESS);
			authVerifierResult.setUserId(accessToken.getUserId());

			return authVerifierResult;
		}
		catch (Exception e) {
			if (_log.isDebugEnabled()) {
				_log.debug("OAuth2 Access Token validation failed", e);
			}

			return authVerifierResult;
		}
	}

	protected BearerTokenProvider.AccessToken getAccessToken(
			AccessControlContext accessControlContext)
		throws PortalException {

		HttpServletRequest request = accessControlContext.getRequest();

		String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (authorization == null) {
			return null;
		}

		String[] basicAuthParts = authorization.split(" ");

		if (basicAuthParts.length != 2) {
			return null;
		}

		String basicAuthPart = basicAuthParts[0];

		if (!"Bearer".equalsIgnoreCase(basicAuthPart)) {
			return null;
		}

		String token = basicAuthParts[1];

		if (Validator.isBlank(token)) {
			return null;
		}

		OAuth2Authorization oAuth2Authorization = null;
		try {
			oAuth2Authorization =
				_oAuth2AuthorizationLocalService.
					getOAuth2AuthorizationByAccessTokenContent(token);
		}
		catch (NoSuchOAuth2AuthorizationException e) {
			return null;
		}

		String accessTokenContent = oAuth2Authorization.getAccessTokenContent();

		if(OAuth2ProviderConstants.EXPIRED_TOKEN.equals(accessTokenContent)) {
			return null;
		}

		OAuth2Application oAuth2Application =
			_oAuth2ApplicationLocalService.getOAuth2Application(
				oAuth2Authorization.getOAuth2ApplicationId());

		long issuedAtSeconds =
			oAuth2Authorization.getAccessTokenCreateDate().getTime() / 1000;

		long expiresSeconds =
			oAuth2Authorization.getAccessTokenExpirationDate().getTime() / 1000;

		long lifeTime = expiresSeconds - issuedAtSeconds;

		List<String> scopeAliasesList = Collections.emptyList();

		if (oAuth2Application.getOAuth2ApplicationScopeAliasesId() > 0) {
			OAuth2ApplicationScopeAliases oAuth2ApplicationScopeAliases =
				_oAuth2ApplicationScopeAliasesLocalService.
					getOAuth2ApplicationScopeAliases(
						oAuth2Authorization.
							getOAuth2ApplicationScopeAliasesId());

			scopeAliasesList =
				oAuth2ApplicationScopeAliases.getScopeAliasesList();
		}

		BearerTokenProvider.AccessToken accessToken =
			new BearerTokenProvider.AccessToken(
				oAuth2Application,
				new ArrayList<>(),
				StringPool.BLANK,
				lifeTime,
				new HashMap<>(),
				StringPool.BLANK,
				StringPool.BLANK,
				issuedAtSeconds,
				StringPool.BLANK,
				StringPool.BLANK,
				new HashMap<>(),
				StringPool.BLANK,
				StringPool.BLANK,
				scopeAliasesList,
				accessTokenContent,
				"Bearer",
				oAuth2Authorization.getUserId(),
				oAuth2Authorization.getUserName());

		return accessToken;
	}

	private static Log _log = LogFactoryUtil.getLog(OAuth2JSONWSAuthVerifier.class);

	private BundleContext _bundleContext;

	@Reference(
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(name=default)"
	)
	private volatile BearerTokenProvider _defaultBearerTokenProvider;

	@Reference(
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	private volatile ScopeLocator _scopeFinderLocator;

	@Reference
	private OAuth2AuthorizationLocalService _oAuth2AuthorizationLocalService;

	@Reference
	private OAuth2ApplicationLocalService _oAuth2ApplicationLocalService;

	@Reference
	private OAuth2ApplicationScopeAliasesLocalService
		_oAuth2ApplicationScopeAliasesLocalService;

	@Reference
	private SAPEntryScopeRegistry _sapEntryScopeRegistry;

	@Reference
	private OAuth2SAPEntryScopesPublisher _oAuth2SAPEntryScopesPublisher;

	@Reference
	private ScopedServiceTrackerMapFactory _scopedServiceTrackerMapFactory;

	private ScopedServiceTrackerMap<BearerTokenProvider>
		_scopedBearerTokenProvider;

}
