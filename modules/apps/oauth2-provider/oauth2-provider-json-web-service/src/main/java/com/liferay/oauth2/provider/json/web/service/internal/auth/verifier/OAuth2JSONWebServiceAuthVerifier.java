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

package com.liferay.oauth2.provider.json.web.service.internal.auth.verifier;

import com.liferay.oauth2.provider.constants.OAuth2ProviderConstants;
import com.liferay.oauth2.provider.json.web.service.internal.constants.OAuth2JSONWebServiceConstants;
import com.liferay.oauth2.provider.json.web.service.internal.service.access.policy.scope.SAPEntryScope;
import com.liferay.oauth2.provider.json.web.service.internal.service.access.policy.scope.SAPEntryScopeDescriptorFinderRegistrator;
import com.liferay.oauth2.provider.model.OAuth2Application;
import com.liferay.oauth2.provider.model.OAuth2ApplicationScopeAliases;
import com.liferay.oauth2.provider.model.OAuth2Authorization;
import com.liferay.oauth2.provider.rest.spi.bearer.token.provider.BearerTokenProvider;
import com.liferay.oauth2.provider.rest.spi.bearer.token.provider.BearerTokenProviderAccessor;
import com.liferay.oauth2.provider.scope.liferay.LiferayOAuth2Scope;
import com.liferay.oauth2.provider.scope.liferay.ScopeLocator;
import com.liferay.oauth2.provider.service.OAuth2ApplicationLocalService;
import com.liferay.oauth2.provider.service.OAuth2ApplicationScopeAliasesLocalService;
import com.liferay.oauth2.provider.service.OAuth2AuthorizationLocalService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.AccessControlContext;
import com.liferay.portal.kernel.security.auth.AuthException;
import com.liferay.portal.kernel.security.auth.verifier.AuthVerifier;
import com.liferay.portal.kernel.security.auth.verifier.AuthVerifierResult;
import com.liferay.portal.kernel.security.service.access.policy.ServiceAccessPolicyThreadLocal;
import com.liferay.portal.kernel.servlet.HttpHeaders;
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

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Tomas Polesovsky
 */
@Component(
	property = "auth.verifier.OAuth2JSONWebServiceAuthVerifier.urls.includes=/api/jsonws/*"
)
public class OAuth2JSONWebServiceAuthVerifier implements AuthVerifier {

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
			BearerTokenProvider.AccessToken bearerAccessToken = getAccessToken(
				accessControlContext);

			if (bearerAccessToken == null) {
				return authVerifierResult;
			}

			OAuth2Application oAuth2Application =
				bearerAccessToken.getOAuth2Application();

			long companyId = oAuth2Application.getCompanyId();

			BearerTokenProvider bearerTokenProvider =
				_bearerTokenProviderAccessor.getBearerTokenProvider(
					companyId, oAuth2Application.getClientId());

			if (bearerTokenProvider == null) {
				return authVerifierResult;
			}

			if (!bearerTokenProvider.isValid(bearerAccessToken)) {
				return authVerifierResult;
			}

			Set<String> scopes = new HashSet<>();

			for (String scope : bearerAccessToken.getScopes()) {
				Collection<LiferayOAuth2Scope> liferayOAuth2Scopes =
					_scopeLocator.getLiferayOAuth2Scopes(
						companyId, scope,
						OAuth2JSONWebServiceConstants.
							LIFERAY_JSON_WEB_SERVICES);

				for (LiferayOAuth2Scope liferayOAuth2Scope :
						liferayOAuth2Scopes) {

					scopes.add(liferayOAuth2Scope.getScope());
				}
			}

			List<SAPEntryScope> sapEntryScopes =
				_sapEntryScopeDescriptorFinderRegistrator.
					getRegisteredSAPEntryScopes(companyId);

			for (SAPEntryScope sapEntryScope : sapEntryScopes) {
				if (scopes.contains(sapEntryScope.getScope())) {
					ServiceAccessPolicyThreadLocal.
						addActiveServiceAccessPolicyName(
							sapEntryScope.getSapEntryName());
				}
			}

			Map<String, Object> settings = authVerifierResult.getSettings();

			settings.put(
				BearerTokenProvider.AccessToken.class.getName(),
				bearerAccessToken);

			authVerifierResult.setState(AuthVerifierResult.State.SUCCESS);
			authVerifierResult.setUserId(bearerAccessToken.getUserId());

			return authVerifierResult;
		}
		catch (Exception e) {
			if (_log.isDebugEnabled()) {
				_log.debug("Unable to verify OAuth2 access token", e);
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

		String accessTokenContent = authorizationParts[1];

		if (Validator.isBlank(accessTokenContent)) {
			return null;
		}

		OAuth2Authorization oAuth2Authorization =
			_oAuth2AuthorizationLocalService.
				fetchOAuth2AuthorizationByAccessTokenContent(
					accessTokenContent);

		if (oAuth2Authorization == null) {
			return null;
		}

		accessTokenContent = oAuth2Authorization.getAccessTokenContent();

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

	private static final Log _log = LogFactoryUtil.getLog(
		OAuth2JSONWebServiceAuthVerifier.class);

	@Reference(
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	private volatile BearerTokenProviderAccessor _bearerTokenProviderAccessor;

	@Reference
	private OAuth2ApplicationLocalService _oAuth2ApplicationLocalService;

	@Reference
	private OAuth2ApplicationScopeAliasesLocalService
		_oAuth2ApplicationScopeAliasesLocalService;

	@Reference
	private OAuth2AuthorizationLocalService _oAuth2AuthorizationLocalService;

	@Reference
	private SAPEntryScopeDescriptorFinderRegistrator
		_sapEntryScopeDescriptorFinderRegistrator;

	@Reference
	private ScopeLocator _scopeLocator;

}