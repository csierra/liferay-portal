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
import com.liferay.oauth2.provider.model.OAuth2Application;
import com.liferay.oauth2.provider.model.OAuth2ApplicationScopeAliases;
import com.liferay.oauth2.provider.model.OAuth2Authorization;
import com.liferay.oauth2.provider.rest.spi.bearer.token.provider.BearerTokenProvider;
import com.liferay.oauth2.provider.rest.spi.bearer.token.provider.BearerTokenProviderAccessor;
import com.liferay.oauth2.provider.scope.liferay.LiferayOAuth2Scope;
import com.liferay.oauth2.provider.scope.liferay.ScopeLocator;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Tomas Polesovsky
 */
@Component(
	immediate = true,
	property =
		{"auth.verifier.OAuth2JSONWSAuthVerifier.urls.includes=/api/jsonws/*"}
)
public class OAuth2JSONWSAuthVerifier implements AuthVerifier {

	@Activate
	public void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;
	}

	@Override
	public String getAuthType() {
		return _OAUTH2;
	}

	@Override
	public AuthVerifierResult verify(
			AccessControlContext accessControlContext, Properties properties)
		throws AuthException {

		AuthVerifierResult authVerifierResult = new AuthVerifierResult();

		try {
			BearerTokenProvider.AccessToken accessToken = getAccessToken(
				accessControlContext);

			if (accessToken == null) {
				return authVerifierResult;
			}

			OAuth2Application oAuth2Application =
				accessToken.getOAuth2Application();

			long companyId = oAuth2Application.getCompanyId();

			BearerTokenProvider bearerTokenProvider =
				_scopedBearerTokenProviderAccessor.getBearerTokenProvider(
					companyId, oAuth2Application.getClientId());

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

			Map<String, Object> settings = authVerifierResult.getSettings();

			settings.put(
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

		HttpServletRequest httpServletRequest =
			accessControlContext.getRequest();

		String authorization = httpServletRequest.getHeader(
			HttpHeaders.AUTHORIZATION);

		if (Validator.isBlank(authorization)) {
			return null;
		}

		String[] authorizationParts = authorization.split("\\s");

		String scheme = authorizationParts[0];

		if (!StringUtil.equalsIgnoreCase(scheme, _BEARER)) {
			return null;
		}

		String token = authorizationParts[1];

		if (Validator.isBlank(token)) {
			return null;
		}

		OAuth2Authorization oAuth2Authorization =
			_oAuth2AuthorizationLocalService.
				fetchOAuth2AuthorizationByAccessTokenContent(token);

		if (oAuth2Authorization == null) {
			return null;
		}

		String accessTokenContent = oAuth2Authorization.getAccessTokenContent();

		if (OAuth2ProviderConstants.EXPIRED_TOKEN.equals(accessTokenContent)) {
			return null;
		}

		OAuth2Application oAuth2Application =
			_oAuth2ApplicationLocalService.getOAuth2Application(
				oAuth2Authorization.getOAuth2ApplicationId());

		Date createDate = oAuth2Authorization.getAccessTokenCreateDate();
		Date expirationDate =
			oAuth2Authorization.getAccessTokenExpirationDate();

		long expiresIn =
			(expirationDate.getTime() - createDate.getTime()) / 1000;

		long issuedAt = createDate.getTime() / 1000;

		List<String> scopeAliasesList = Collections.emptyList();

		long oAuth2ApplicationScopeAliasesId =
			oAuth2Authorization.getOAuth2ApplicationScopeAliasesId();

		if (oAuth2ApplicationScopeAliasesId > 0) {
			OAuth2ApplicationScopeAliases oAuth2ApplicationScopeAliases =
				_oAuth2ApplicationScopeAliasesLocalService.
					getOAuth2ApplicationScopeAliases(
						oAuth2ApplicationScopeAliasesId);

			scopeAliasesList =
				oAuth2ApplicationScopeAliases.getScopeAliasesList();
		}

		BearerTokenProvider.AccessToken accessToken =
			new BearerTokenProvider.AccessToken(
				oAuth2Application, new ArrayList<>(), StringPool.BLANK,
				expiresIn, new HashMap<>(), StringPool.BLANK, StringPool.BLANK,
				issuedAt, StringPool.BLANK, StringPool.BLANK, new HashMap<>(),
				StringPool.BLANK, StringPool.BLANK, scopeAliasesList,
				accessTokenContent, _BEARER, oAuth2Authorization.getUserId(),
				oAuth2Authorization.getUserName());

		return accessToken;
	}

	private static final String _BEARER = "Bearer";

	private static final String _OAUTH2 = "OAuth2";

	private static final Log _log = LogFactoryUtil.getLog(
		OAuth2JSONWSAuthVerifier.class);

	private BundleContext _bundleContext;

	@Reference
	private OAuth2ApplicationLocalService _oAuth2ApplicationLocalService;

	@Reference
	private OAuth2ApplicationScopeAliasesLocalService
		_oAuth2ApplicationScopeAliasesLocalService;

	@Reference
	private OAuth2AuthorizationLocalService _oAuth2AuthorizationLocalService;

	@Reference
	private OAuth2SAPEntryScopesPublisher _oAuth2SAPEntryScopesPublisher;

	@Reference
	private SAPEntryScopeRegistry _sapEntryScopeRegistry;

	@Reference
	private BearerTokenProviderAccessor _scopedBearerTokenProviderAccessor;

	@Reference
	private ScopedServiceTrackerMapFactory _scopedServiceTrackerMapFactory;

	@Reference(
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	private volatile ScopeLocator _scopeFinderLocator;

}