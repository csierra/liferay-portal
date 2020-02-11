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

package com.liferay.multi.factor.authentication.email.otp.web.internal.checker.audit.util;

import com.liferay.multi.factor.authentication.email.otp.web.internal.constants.MFAEmailOTPEventTypes;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.audit.AuditException;
import com.liferay.portal.kernel.audit.AuditMessage;
import com.liferay.portal.kernel.audit.AuditRouter;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.UserLocalService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Arthur Chan
 */
@Component(service = MFAEmailOTPCheckerAudit.class)
public class MFAEmailOTPCheckerAudit {

	public AuditMessage buildNotVerifiedMessage(
		long userId, String checkerClassName, String reason) {

		return _getAuditMessage(
			MFAEmailOTPEventTypes.MFA_EMAIL_OTP_NOT_VERIFIED, userId,
			checkerClassName, JSONUtil.put("reason", reason));
	}

	public AuditMessage buildVerificationFailureMessage(
		long userId, String checkerClassName, String reason) {

		return _getAuditMessage(
			MFAEmailOTPEventTypes.MFA_EMAIL_OTP_VERIFICATION_FAILURE, userId,
			checkerClassName, JSONUtil.put("reason", reason));
	}

	public AuditMessage buildVerificationSuccessMessage(
		long userId, String checkerClassName) {

		return _getAuditMessage(
			MFAEmailOTPEventTypes.MFA_EMAIL_OTP_VERIFICATION_SUCCESS, userId,
			checkerClassName, null);
	}

	public AuditMessage buildVerifiedMessage(
		long userId, String checkerClassName) {

		return _getAuditMessage(
			MFAEmailOTPEventTypes.MFA_EMAIL_OTP_VERIFIED, userId,
			checkerClassName, null);
	}

	public void routeAuditMessage(AuditMessage auditMessage) {
		try {
			_auditRouter.route(auditMessage);
		}
		catch (AuditException auditException) {
			if (_log.isWarnEnabled()) {
				_log.warn("Unable to route audit message", auditException);
			}
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception, exception);
			}
		}
	}

	private AuditMessage _getAuditMessage(
		String eventType, long userId, String checkerClassName,
		JSONObject reason) {

		User user = _userLocalService.fetchUser(userId);

		if (user == null) {
			return new AuditMessage(
				eventType, CompanyThreadLocal.getCompanyId(), userId,
				StringPool.BLANK, checkerClassName, String.valueOf(userId),
				null, JSONUtil.put("reason", "No Such User " + userId));
		}

		return new AuditMessage(
			eventType, user.getCompanyId(), user.getUserId(),
			user.getFullName(), checkerClassName,
			String.valueOf(user.getPrimaryKey()), null, reason);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		MFAEmailOTPCheckerAudit.class);

	@Reference
	private AuditRouter _auditRouter;

	@Reference
	private UserLocalService _userLocalService;

}