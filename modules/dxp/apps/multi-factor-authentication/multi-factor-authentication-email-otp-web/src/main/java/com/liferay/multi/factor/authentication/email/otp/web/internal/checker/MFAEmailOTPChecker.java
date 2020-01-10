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

import com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTPEntry;
import com.liferay.multi.factor.authentication.email.otp.service.MFAEmailOTPEntryLocalService;
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
			MFAEmailOTPWebKeys.MFA_EMAIL_OTP_SEND_TO_ADDRESS,
			user.getEmailAddress());

		RequestDispatcher requestDispatcher =
			_servletContext.getRequestDispatcher("/verify_otp.jsp");

		try {
			MFAEmailOTPConfiguration mfaEmailOTPConfiguration =
				_getMFAEmailOTPConfiguration(userId);

			httpServletRequest.setAttribute(
				MFAEmailOTPWebKeys.MFA_EMAIL_OTP_CONFIGURATION,
				mfaEmailOTPConfiguration);

			requestDispatcher.include(httpServletRequest, httpServletResponse);

			HttpServletRequest originalHttpServletRequest =
				_portal.getOriginalServletRequest(httpServletRequest);

			HttpSession httpSession = originalHttpServletRequest.getSession();

			httpSession.setAttribute(
				MFAEmailOTPWebKeys.MFA_EMAIL_OTP_PHASE, "verify");
			httpSession.setAttribute(
				MFAEmailOTPWebKeys.MFA_EMAIL_OTP_USER_ID, userId);
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

			MFAEmailOTPConfiguration mfaEmailOTPConfiguration =
				_getMFAEmailOTPConfiguration(userId);

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
					MFAEmailOTPWebKeys.MFA_EMAIL_OTP_VALIDATED_AT,
					System.currentTimeMillis());
				httpSession.setAttribute(
					MFAEmailOTPWebKeys.MFA_EMAIL_OTP_VALIDATED_USER_ID, userId);

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
				MFAEmailOTPWebKeys.MFA_EMAIL_OTP_VALIDATED_AT);
			sessionPhishingProtectedAttributesList.add(
				MFAEmailOTPWebKeys.MFA_EMAIL_OTP_VALIDATED_USER_ID);

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
				MFAEmailOTPWebKeys.MFA_EMAIL_OTP_VALIDATED_AT);
			sessionPhishingProtectedAttributesList.remove(
				MFAEmailOTPWebKeys.MFA_EMAIL_OTP_VALIDATED_USER_ID);

			PropsValues.SESSION_PHISHING_PROTECTED_ATTRIBUTES =
				sessionPhishingProtectedAttributesList.toArray(new String[0]);
		}
	}

	protected boolean isThrottlingEnabled(
		MFAEmailOTPConfiguration mfaEmailOTPConfiguration) {

		long retryTimeout = mfaEmailOTPConfiguration.retryTimeout();

		int failedAttemptsAllowed =
			mfaEmailOTPConfiguration.failedAttemptsAllowed();

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
			MFAEmailOTPWebKeys.MFA_EMAIL_OTP_VALIDATED_USER_ID);

		if (userId != validatedUserId) {
			return false;
		}

		MFAEmailOTPConfiguration mfaEmailOTPConfiguration =
			_getMFAEmailOTPConfiguration(userId);

		long validationExpirationTime =
			mfaEmailOTPConfiguration.validationExpirationTime();

		if (validationExpirationTime < 0) {
			return true;
		}

		long validatedAt = (long)httpSession.getAttribute(
			MFAEmailOTPWebKeys.MFA_EMAIL_OTP_VALIDATED_AT);

		if ((validatedAt + validationExpirationTime * 1000) >
				System.currentTimeMillis()) {

			return true;
		}

		return false;
	}

	private MFAEmailOTPConfiguration _getMFAEmailOTPConfiguration(long userId) {
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
		MFAEmailOTPConfiguration mfaEmailOTPConfiguration,
		MFAEmailOTPEntry mfaEmailOTPEntry) {

		Date lastFailDate = mfaEmailOTPEntry.getLastFailDate();
		long retryTimeout = mfaEmailOTPConfiguration.retryTimeout();

		if ((lastFailDate.getTime() + retryTimeout) >
				System.currentTimeMillis()) {

			return false;
		}

		return true;
	}

	private boolean _reachedFailedAttemptsAllowed(
		MFAEmailOTPConfiguration mfaEmailOTPConfiguration,
		MFAEmailOTPEntry mfaEmailOTPEntry) {

		int failedAttemptsAllowed =
			mfaEmailOTPConfiguration.failedAttemptsAllowed();

		if (mfaEmailOTPEntry.getFailedAttempts() >= failedAttemptsAllowed) {
			return true;
		}

		return false;
	}

	private boolean _verify(HttpSession httpSession, String otp) {
		String expectedOtp = (String)httpSession.getAttribute(
			MFAEmailOTPWebKeys.MFA_EMAIL_OTP);

		if ((expectedOtp == null) || !expectedOtp.equals(otp)) {
			return false;
		}

		httpSession.removeAttribute(MFAEmailOTPWebKeys.MFA_EMAIL_OTP);
		httpSession.removeAttribute(MFAEmailOTPWebKeys.MFA_EMAIL_OTP_PHASE);
		httpSession.removeAttribute(MFAEmailOTPWebKeys.MFA_EMAIL_OTP_SET_AT);
		httpSession.removeAttribute(MFAEmailOTPWebKeys.MFA_EMAIL_OTP_USER_ID);

		return true;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		MFAEmailOTPChecker.class);

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