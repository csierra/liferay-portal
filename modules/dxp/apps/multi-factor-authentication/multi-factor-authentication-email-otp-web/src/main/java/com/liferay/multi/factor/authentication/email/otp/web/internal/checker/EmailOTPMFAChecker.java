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

package com.liferay.multi.factor.authentication.email.otp.web.internal.checker;

import com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP;
import com.liferay.multi.factor.authentication.email.otp.service.MFAEmailOTPLocalService;
import com.liferay.multi.factor.authentication.email.otp.web.internal.configuration.EmailOTPConfiguration;
import com.liferay.multi.factor.authentication.email.otp.web.internal.constants.MFAWebKeys;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProviderUtil;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.util.PropsValues;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
			MFAWebKeys.MFA_SEND_TO_EMAIL_ADDRESS, user.getEmailAddress());

		RequestDispatcher requestDispatcher =
			_servletContext.getRequestDispatcher("/verify_otp.jsp");

		try {
			EmailOTPConfiguration emailOTPConfiguration =
				_getEmailOTPConfiguration(userId);

			httpServletRequest.setAttribute(
				MFAWebKeys.MFA_EMAIL_OTP_CONFIGURATION, emailOTPConfiguration);

			requestDispatcher.include(httpServletRequest, httpServletResponse);

			HttpServletRequest originalHttpServletRequest =
				_portal.getOriginalServletRequest(httpServletRequest);

			HttpSession httpSession = originalHttpServletRequest.getSession();

			httpSession.setAttribute(MFAWebKeys.MFA_OTP_PHASE, "verify");
			httpSession.setAttribute(MFAWebKeys.MFA_USER_ID, userId);
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
			MFAEmailOTP mfaEmailOTPEntry =
				_mfaEmailOTPEntryLocalService.fetchEmailOTPByUserId(userId);

			if (mfaEmailOTPEntry == null) {
				mfaEmailOTPEntry = _mfaEmailOTPEntryLocalService.addEmailOTP(
					userId);
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
				httpSession.setAttribute(
					MFAWebKeys.MFA_VALIDATED_AT, System.currentTimeMillis());
				httpSession.setAttribute(
					MFAWebKeys.MFA_VALIDATED_USER_ID, userId);

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

			sessionPhishingProtectedAttributesList.add(
				MFAWebKeys.MFA_VALIDATED_AT);
			sessionPhishingProtectedAttributesList.add(
				MFAWebKeys.MFA_VALIDATED_USER_ID);

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

			sessionPhishingProtectedAttributesList.remove(
				MFAWebKeys.MFA_VALIDATED_AT);
			sessionPhishingProtectedAttributesList.remove(
				MFAWebKeys.MFA_VALIDATED_USER_ID);

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

		long validatedUserId = (long)httpSession.getAttribute(
			MFAWebKeys.MFA_VALIDATED_USER_ID);

		if (userId != validatedUserId) {
			return false;
		}

		EmailOTPConfiguration emailOTPConfiguration = _getEmailOTPConfiguration(
			userId);

		long validationExpirationTime =
			emailOTPConfiguration.validationExpirationTime();

		if (validationExpirationTime < 0) {
			return true;
		}

		long validatedAt = (long)httpSession.getAttribute(
			MFAWebKeys.MFA_VALIDATED_AT);

		if ((validatedAt + validationExpirationTime * 1000) >
				System.currentTimeMillis()) {

			return true;
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

	private boolean _isRetryTimedOut(
		EmailOTPConfiguration emailOTPConfiguration,
		MFAEmailOTP mfaEmailOTPEntry) {

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
		MFAEmailOTP mfaEmailOTPEntry) {

		int failedAttemptsAllowed =
			emailOTPConfiguration.failedAttemptsAllowed();

		if (mfaEmailOTPEntry.getFailedAttempts() >= failedAttemptsAllowed) {
			return true;
		}

		return false;
	}

	private boolean _verify(HttpSession httpSession, String otp) {
		String expectedOtp = (String)httpSession.getAttribute(
			MFAWebKeys.MFA_OTP);

		if ((expectedOtp == null) || !expectedOtp.equals(otp)) {
			return false;
		}

		httpSession.removeAttribute(MFAWebKeys.MFA_OTP);
		httpSession.removeAttribute(MFAWebKeys.MFA_OTP_PHASE);
		httpSession.removeAttribute(MFAWebKeys.MFA_OTP_SET_AT);
		httpSession.removeAttribute(MFAWebKeys.MFA_USER_ID);

		return true;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		EmailOTPMFAChecker.class);

	@Reference
	private MFAEmailOTPLocalService _mfaEmailOTPEntryLocalService;

	@Reference
	private Portal _portal;

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.multi.factor.authentication.checker.email.otp.web)"
	)
	private ServletContext _servletContext;

	@Reference
	private UserLocalService _userLocalService;

}