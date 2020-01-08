/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.multi.factor.authentication.checker.email.otp.web.internal.checker;

import com.liferay.multi.factor.authentication.checker.email.otp.model.MFAEmailOTPEntry;
import com.liferay.multi.factor.authentication.checker.email.otp.service.MFAEmailOTPEntryLocalService;
import com.liferay.multi.factor.authentication.checker.email.otp.web.internal.configuration.EmailOTPConfiguration;
import com.liferay.multi.factor.authentication.checker.email.otp.web.internal.constants.MFAEmailOTPWebKeys;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProviderUtil;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.util.PropsValues;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Arthur Chan
 */
@Component(service = EmailOTPMFAChecker.class)
public class EmailOTPMFAChecker {

	public void includeBrowserVerification(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, long userId)
		throws IOException {

		User user = _userLocalService.fetchUser(userId);

		if (user == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Requested Email One-time password verification for a " +
						"non existent user id: " + userId);
			}

			return;
		}

		httpServletRequest.setAttribute(
			MFAEmailOTPWebKeys.SEND_TO_EMAIL_ADDRESS, user.getEmailAddress());

		RequestDispatcher requestDispatcher =
			_servletContext.getRequestDispatcher("/verify_otp.jsp");

		try {
			EmailOTPConfiguration emailOTPConfiguration =
				_getEmailOTPConfiguration(userId);

			httpServletRequest.setAttribute(
				MFAEmailOTPWebKeys.EMAIL_OTP_CONFIGURATION,
				emailOTPConfiguration);

			requestDispatcher.include(httpServletRequest, httpServletResponse);

			HttpServletRequest originalHttpServletRequest =
				_portal.getOriginalServletRequest(httpServletRequest);

			HttpSession httpSession = originalHttpServletRequest.getSession();

			httpSession.setAttribute(MFAEmailOTPWebKeys.OTP_PHASE, "verify");
			httpSession.setAttribute(MFAEmailOTPWebKeys.USER_ID, userId);
		}
		catch (ServletException se) {
			throw new IOException(
				"Unable to include /verify_otp.jsp: " + se, se);
		}
	}

	public boolean isBrowserVerified(
		HttpServletRequest httpServletRequest, long userId) {

		HttpServletRequest originalHttpServletRequest =
			_portal.getOriginalServletRequest(httpServletRequest);

		HttpSession httpSession = originalHttpServletRequest.getSession(false);

		if (isVerified(httpSession, userId)) {
			return true;
		}

		return false;
	}

	public boolean verifyBrowserRequest(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse, long userId) {

		User user = _userLocalService.fetchUser(userId);

		if (user == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Requested Email One-time password verification for a " +
						"non existent user id: " + userId);
			}

			return false;
		}

		HttpServletRequest originalHttpServletRequest =
			_portal.getOriginalServletRequest(httpServletRequest);

		HttpSession httpSession = originalHttpServletRequest.getSession();

		try {
			MFAEmailOTPEntry mfaEmailOTPEntry =
				_mfaEmailOTPEntryLocalService.fetchMFAEmailOTPEntryByUserId(
					userId);

			if (mfaEmailOTPEntry == null) {
				mfaEmailOTPEntry =
					_mfaEmailOTPEntryLocalService.addMFAEmailOTPEntry(userId);
			}

			EmailOTPConfiguration emailOTPConfiguration =
				_getEmailOTPConfiguration(userId);

			if (isThrottlingEnabled(emailOTPConfiguration) &&
				_reachedFailedAttemptsAllowed(
					emailOTPConfiguration, mfaEmailOTPEntry)) {

				if (_isRetryTimedOut(emailOTPConfiguration, mfaEmailOTPEntry)) {
					_mfaEmailOTPEntryLocalService.resetFailedAttempts(userId);
				}
				else {
					return false;
				}
			}

			String otp = ParamUtil.getString(httpServletRequest, "otp");

			boolean verified = _verify(httpSession, otp);

			String remoteAddr = originalHttpServletRequest.getRemoteAddr();

			if (verified) {
				Map<String, Object> validatedMap = _getValidatedMap(
					httpSession);

				if (validatedMap == null) {
					validatedMap = new HashMap<>(2);

					httpSession.setAttribute(_VALIDATED, validatedMap);
				}

				validatedMap.put(MFAEmailOTPWebKeys.USER_ID, userId);
				validatedMap.put(
					MFAEmailOTPWebKeys.VALIDATED_AT,
					System.currentTimeMillis());

				_mfaEmailOTPEntryLocalService.updateAttempts(
					userId, remoteAddr, true);

				return true;
			}

			_mfaEmailOTPEntryLocalService.updateAttempts(
				userId, remoteAddr, false);

			return false;
		}
		catch (Exception e) {
			_log.error(e.getMessage(), e);

			return false;
		}
	}

	@Activate
	protected void activate(Map<String, Object> properties) {
		if (PropsValues.SESSION_ENABLE_PHISHING_PROTECTION) {
			List<String> sessionPhishingProtectedAttributesList =
				new ArrayList<>(
					Arrays.asList(
						PropsValues.SESSION_PHISHING_PROTECTED_ATTRIBUTES));

			sessionPhishingProtectedAttributesList.add(_VALIDATED);

			PropsValues.SESSION_PHISHING_PROTECTED_ATTRIBUTES =
				sessionPhishingProtectedAttributesList.toArray(new String[0]);
		}
	}

	@Deactivate
	protected void deactivate() {
		if (PropsValues.SESSION_ENABLE_PHISHING_PROTECTION) {
			List<String> sessionPhishingProtectedAttributesList =
				new ArrayList<>(
					Arrays.asList(
						PropsValues.SESSION_PHISHING_PROTECTED_ATTRIBUTES));

			sessionPhishingProtectedAttributesList.remove(_VALIDATED);

			PropsValues.SESSION_PHISHING_PROTECTED_ATTRIBUTES =
				sessionPhishingProtectedAttributesList.toArray(new String[0]);
		}
	}

	protected boolean isThrottlingEnabled(
		EmailOTPConfiguration emailOTPConfiguration) {

		long retryTimeout = emailOTPConfiguration.retryTimeout();

		int failedAttemptsAllowed =
			emailOTPConfiguration.failedAttemptsAllowed();

		if ((retryTimeout < 0) || (failedAttemptsAllowed < 0)) {
			return false;
		}

		return true;
	}

	protected boolean isVerified(HttpSession httpSession, long userId) {
		if (httpSession == null) {
			return false;
		}

		Map<String, Object> validatedMap = _getValidatedMap(httpSession);

		if (validatedMap != null) {
			if (userId != MapUtil.getLong(
					validatedMap, MFAEmailOTPWebKeys.USER_ID)) {

				return false;
			}

			EmailOTPConfiguration emailOTPConfiguration =
				_getEmailOTPConfiguration(userId);

			long validationExpirationTime =
				emailOTPConfiguration.validationExpirationTime();

			if (validationExpirationTime < 0) {
				return true;
			}

			long validatedAt = MapUtil.getLong(
				validatedMap, MFAEmailOTPWebKeys.VALIDATED_AT);

			if ((validatedAt + validationExpirationTime * 1000) >
					System.currentTimeMillis()) {

				return true;
			}
		}

		return false;
	}

	private EmailOTPConfiguration _getEmailOTPConfiguration(long userId) {
		User user = _userLocalService.fetchUser(userId);

		if (user == null) {
			throw new IllegalStateException(
				"Requested Email One-time password verification for a non " +
					"existent user id: " + userId);
		}

		try {
			return ConfigurationProviderUtil.getCompanyConfiguration(
				EmailOTPConfiguration.class, user.getCompanyId());
		}
		catch (ConfigurationException ce) {
			throw new IllegalStateException(ce);
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> _getValidatedMap(HttpSession httpSession) {
		return (Map<String, Object>)httpSession.getAttribute(_VALIDATED);
	}

	private boolean _isRetryTimedOut(
		EmailOTPConfiguration emailOTPConfiguration,
		MFAEmailOTPEntry mfaEmailOTPEntry) {

		Date lastFailDate = mfaEmailOTPEntry.getLastFailDate();
		long retryTimeout = emailOTPConfiguration.retryTimeout();

		if ((lastFailDate.getTime() + retryTimeout) >
				System.currentTimeMillis()) {

			return false;
		}

		return true;
	}

	private boolean _reachedFailedAttemptsAllowed(
		EmailOTPConfiguration emailOTPConfiguration,
		MFAEmailOTPEntry mfaEmailOTPEntry) {

		int failedAttemptsAllowed =
			emailOTPConfiguration.failedAttemptsAllowed();

		if (mfaEmailOTPEntry.getFailedAttempts() >= failedAttemptsAllowed) {
			return true;
		}

		return false;
	}

	private boolean _verify(HttpSession httpSession, String otp) {
		String expectedOtp = (String)httpSession.getAttribute(
			MFAEmailOTPWebKeys.OTP);

		if ((expectedOtp == null) || !expectedOtp.equals(otp)) {
			return false;
		}

		httpSession.removeAttribute(MFAEmailOTPWebKeys.OTP);
		httpSession.removeAttribute(MFAEmailOTPWebKeys.OTP_PHASE);
		httpSession.removeAttribute(MFAEmailOTPWebKeys.OTP_SET_AT);
		httpSession.removeAttribute(MFAEmailOTPWebKeys.USER_ID);

		return true;
	}

	private static final String _VALIDATED =
		EmailOTPMFAChecker.class.getName() + "#VALIDATED";

	private static final Log _log = LogFactoryUtil.getLog(
		EmailOTPMFAChecker.class);

	@Reference
	private MFAEmailOTPEntryLocalService _mfaEmailOTPEntryLocalService;

	@Reference
	private Portal _portal;

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.multi.factor.authentication.checker.email.otp.web)"
	)
	private ServletContext _servletContext;

	@Reference
	private UserLocalService _userLocalService;

}