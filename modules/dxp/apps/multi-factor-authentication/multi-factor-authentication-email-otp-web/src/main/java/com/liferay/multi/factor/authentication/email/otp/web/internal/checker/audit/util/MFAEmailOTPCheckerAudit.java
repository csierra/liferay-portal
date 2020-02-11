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
import com.liferay.portal.kernel.audit.AuditException;
import com.liferay.portal.kernel.audit.AuditMessage;
import com.liferay.portal.kernel.audit.AuditRouter;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Arthur Chan
 */
@Component(service = MFAEmailOTPCheckerAudit.class)
public class MFAEmailOTPCheckerAudit {

	public AuditMessage buildIsNotVerifiedMessage(
		User user, String checkerClassName, String reason) {

		return new AuditMessage(
			MFAEmailOTPEventTypes.MFA_EMAIL_OTP_IS_NOT_VERIFIED,
			user.getCompanyId(), user.getUserId(), user.getFullName(),
			checkerClassName, String.valueOf(user.getPrimaryKey()), null,
			JSONUtil.put("reason", reason));
	}

	public AuditMessage buildIsVerifiedMessage(
		User user, String checkerClassName) {

		return new AuditMessage(
			MFAEmailOTPEventTypes.MFA_EMAIL_OTP_IS_VERIFIED,
			user.getCompanyId(), user.getUserId(), user.getFullName(),
			checkerClassName, String.valueOf(user.getPrimaryKey()), null, null);
	}

	public AuditMessage buildNonexistentUserAuditMessage(
		long companyId, long userId, String checkerClassName) {

		return new AuditMessage(
			MFAEmailOTPEventTypes.MFA_EMAIL_OTP_VERIFICATION_FAILURE, companyId,
			userId, "Nonexistent", checkerClassName, String.valueOf(userId),
			null, JSONUtil.put("reason", "Nonexistent User"));
	}

	public AuditMessage buildVerificationFailureMessage(
		User user, String checkerClassName, String reason) {

		return new AuditMessage(
			MFAEmailOTPEventTypes.MFA_EMAIL_OTP_VERIFICATION_FAILURE,
			user.getCompanyId(), user.getUserId(), user.getFullName(),
			checkerClassName, String.valueOf(user.getPrimaryKey()), null,
			JSONUtil.put("reason", reason));
	}

	public AuditMessage buildVerificationSuccessMessage(
		User user, String checkerClassName) {

		return new AuditMessage(
			MFAEmailOTPEventTypes.MFA_EMAIL_OTP_VERIFICATION_SUCCESS,
			user.getCompanyId(), user.getUserId(), user.getFullName(),
			checkerClassName, String.valueOf(user.getPrimaryKey()), null, null);
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

	private static final Log _log = LogFactoryUtil.getLog(
		MFAEmailOTPCheckerAudit.class);

	@Reference
	private AuditRouter _auditRouter;

}