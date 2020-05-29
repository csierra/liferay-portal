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

package com.liferay.portal.security.auth.verifier.internal.basic.auth.header;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.security.auth.AccessControlContext;
import com.liferay.portal.kernel.security.auth.AuthException;
import com.liferay.portal.kernel.security.auth.http.HttpAuthManagerUtil;
import com.liferay.portal.kernel.security.auth.http.HttpAuthorizationHeader;
import com.liferay.portal.kernel.security.auth.verifier.AuthVerifier;
import com.liferay.portal.kernel.security.auth.verifier.AuthVerifierResult;
import com.liferay.portal.kernel.security.auto.login.AutoLogin;
import com.liferay.portal.kernel.security.auto.login.AutoLoginException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.security.configuration.BasicAuthHeaderSupportConfiguration;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Tomas Polesovsky
 */
public class BasicAuthHeaderAuthVerifier implements AuthVerifier {

	public BasicAuthHeaderAuthVerifier(
		AutoLogin autoLogin, ConfigurationProvider configurationProvider,
		Portal portal) {

		_autoLogin = autoLogin;
		_configurationProvider = configurationProvider;
		_portal = portal;
	}

	@Override
	public String getAuthType() {
		return HttpServletRequest.BASIC_AUTH;
	}

	@Override
	public AuthVerifierResult verify(
			AccessControlContext accessControlContext, Properties properties)
		throws AuthException {

		AuthVerifierResult authVerifierResult = new AuthVerifierResult();

		if (!_isEnabled(accessControlContext)) {
			return authVerifierResult;
		}

		try {
			String[] credentials = _autoLogin.login(
				accessControlContext.getRequest(),
				accessControlContext.getResponse());

			if (credentials != null) {
				authVerifierResult.setPassword(credentials[1]);
				authVerifierResult.setPasswordBasedAuthentication(true);
				authVerifierResult.setState(AuthVerifierResult.State.SUCCESS);
				authVerifierResult.setUserId(Long.valueOf(credentials[0]));
			}
			else if (isBasicAuth(accessControlContext, properties)) {
				return generateChallenge(accessControlContext);
			}

			return authVerifierResult;
		}
		catch (AutoLoginException autoLoginException) {
			if (isBasicAuth(accessControlContext, properties)) {
				if (_log.isDebugEnabled()) {
					_log.debug(autoLoginException, autoLoginException);
				}

				return generateChallenge(accessControlContext);
			}

			throw new AuthException(autoLoginException);
		}
	}

	protected AuthVerifierResult generateChallenge(
		AccessControlContext accessControlContext) {

		HttpAuthorizationHeader httpAuthorizationHeader =
			new HttpAuthorizationHeader(HttpAuthorizationHeader.SCHEME_BASIC);

		HttpAuthManagerUtil.generateChallenge(
			accessControlContext.getRequest(),
			accessControlContext.getResponse(), httpAuthorizationHeader);

		AuthVerifierResult authVerifierResult = new AuthVerifierResult();

		authVerifierResult.setState(
			AuthVerifierResult.State.INVALID_CREDENTIALS);

		return authVerifierResult;
	}

	protected boolean isBasicAuth(
		AccessControlContext accessControlContext, Properties properties) {

		boolean basicAuth = MapUtil.getBoolean(
			accessControlContext.getSettings(), "basic_auth");

		if (!basicAuth) {
			basicAuth = GetterUtil.getBoolean(
				properties.getProperty("basic_auth"));
		}

		return basicAuth;
	}

	private boolean _isEnabled(AccessControlContext accessControlContext) {
		try {
			BasicAuthHeaderSupportConfiguration
				basicAuthHeaderSupportConfiguration =
					_configurationProvider.getCompanyConfiguration(
						BasicAuthHeaderSupportConfiguration.class,
						_portal.getCompanyId(
							accessControlContext.getRequest()));

			return basicAuthHeaderSupportConfiguration.enabled();
		}
		catch (ConfigurationException configurationException) {
			return false;
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BasicAuthHeaderAuthVerifier.class);

	private final AutoLogin _autoLogin;
	private final ConfigurationProvider _configurationProvider;
	private final Portal _portal;

}