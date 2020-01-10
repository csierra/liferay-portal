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
import com.liferay.multi.factor.authentication.email.otp.web.internal.configuration.MFAEmailOTPConfiguration;
import com.liferay.multi.factor.authentication.email.otp.web.internal.constants.MFAEmailOTPWebKeys;
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
@Component(service = MFAEmailOTPChecker.class)
public class MFAEmailOTPChecker {

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
			MFAEmailOTPWebKeys.MFA_SEND_TO_EMAIL_ADDRESS,
			user.getEmailAddress());

		RequestDispatcher requestDispatcher =
			_servletContext.getRequestDispatcher("/verify_otp.jsp");

		try {
			MFAEmailOTPConfiguration mfaEmailOTPConfiguration =
				_getEmailOTPConfiguration(userId);

			httpServletRequest.setAttribute(
				MFAEmailOTPWebKeys.MFA_EMAIL_OTP_CONFIGURATION,
				mfaEmailOTPConfiguration);

			requestDispatcher.include(httpServletRequest, httpServletResponse);

			HttpServletRequest originalHttpServletRequest =
				_portal.getOriginalServletRequest(httpServletRequest);

			HttpSession httpSession = originalHttpServletRequest.getSession();

			httpSession.setAttribute(
				MFAEmailOTPWebKeys.MFA_OTP_PHASE, "verify");
			httpSession.setAttribute(MFAEmailOTPWebKeys.MFA_USER_ID, userId);
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

			MFAEmailOTPConfiguration mfaEmailOTPConfiguration =
				_getEmailOTPConfiguration(userId);

			if (isThrottlingEnabled(mfaEmailOTPConfiguration) &&
				_reachedFailedAttemptsAllowed(
					mfaEmailOTPConfiguration, mfaEmailOTPEntry)) {

				if (_isRetryTimedOut(
						mfaEmailOTPConfiguration, mfaEmailOTPEntry)) {

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
					MFAEmailOTPWebKeys.MFA_VALIDATED_AT,
					System.currentTimeMillis());
				httpSession.setAttribute(
					MFAEmailOTPWebKeys.MFA_VALIDATED_USER_ID, userId);

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
				MFAEmailOTPWebKeys.MFA_VALIDATED_AT);
			sessionPhishingProtectedAttributesList.add(
				MFAEmailOTPWebKeys.MFA_VALIDATED_USER_ID);

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
				MFAEmailOTPWebKeys.MFA_VALIDATED_AT);
			sessionPhishingProtectedAttributesList.remove(
				MFAEmailOTPWebKeys.MFA_VALIDATED_USER_ID);

			PropsValues.SESSION_PHISHING_PROTECTED_ATTRIBUTES =
				sessionPhishingProtectedAttributesList.toArray(new String[0]);
		}
	}

	protected boolean isThrottlingEnabled(
		MFAEmailOTPConfiguration emailOTPConfiguration) {

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
			MFAEmailOTPWebKeys.MFA_VALIDATED_USER_ID);

		if (userId != validatedUserId) {
			return false;
		}

		MFAEmailOTPConfiguration emailOTPConfiguration =
			_getEmailOTPConfiguration(userId);

		long validationExpirationTime =
			emailOTPConfiguration.validationExpirationTime();

		if (validationExpirationTime < 0) {
			return true;
		}

		long validatedAt = (long)httpSession.getAttribute(
			MFAEmailOTPWebKeys.MFA_VALIDATED_AT);

		if ((validatedAt + validationExpirationTime * 1000) >
				System.currentTimeMillis()) {

			return true;
		}

		return false;
	}

	private MFAEmailOTPConfiguration _getEmailOTPConfiguration(long userId) {
		User user = _userLocalService.fetchUser(userId);

		if (user == null) {
			throw new IllegalStateException(
				"Requested Email One-time password verification for a non " +
					"existent user id: " + userId);
		}

		try {
			return ConfigurationProviderUtil.getCompanyConfiguration(
				MFAEmailOTPConfiguration.class, user.getCompanyId());
		}
		catch (ConfigurationException ce) {
			throw new IllegalStateException(ce);
		}
	}

	private boolean _isRetryTimedOut(
		MFAEmailOTPConfiguration emailOTPConfiguration,
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
		MFAEmailOTPConfiguration emailOTPConfiguration,
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
			MFAEmailOTPWebKeys.MFA_OTP);

		if ((expectedOtp == null) || !expectedOtp.equals(otp)) {
			return false;
		}

		httpSession.removeAttribute(MFAEmailOTPWebKeys.MFA_OTP);
		httpSession.removeAttribute(MFAEmailOTPWebKeys.MFA_OTP_PHASE);
		httpSession.removeAttribute(MFAEmailOTPWebKeys.MFA_OTP_SET_AT);
		httpSession.removeAttribute(MFAEmailOTPWebKeys.MFA_USER_ID);

		return true;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		MFAEmailOTPChecker.class);

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