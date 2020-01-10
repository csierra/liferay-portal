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

package com.liferay.multi.factor.authentication.email.otp.service.impl;

import com.liferay.multi.factor.authentication.email.otp.exception.DuplicateEmailOTPException;
import com.liferay.multi.factor.authentication.email.otp.exception.NoSuchEmailOTPException;
import com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP;
import com.liferay.multi.factor.authentication.email.otp.service.base.MFAEmailOTPLocalServiceBaseImpl;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;

import java.util.Date;

import org.osgi.service.component.annotations.Component;

/**
 * The implementation of the mfa email otp local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>com.liferay.multi.factor.authentication.email.otp.service.MFAEmailOTPLocalService</code> interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Arthur Chan
 * @see MFAEmailOTPLocalServiceBaseImpl
 */
@Component(
	property = "model.class.name=com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP",
	service = AopService.class
)
public class MFAEmailOTPLocalServiceImpl
	extends MFAEmailOTPLocalServiceBaseImpl {

	public MFAEmailOTP addEmailOTP(long userId) throws PortalException {
		MFAEmailOTP mfaEmailOTP = mfaEmailOTPPersistence.fetchByUserId(userId);

		if (mfaEmailOTP != null) {
			throw new DuplicateEmailOTPException("User ID " + userId);
		}

		mfaEmailOTP = mfaEmailOTPPersistence.create(
			counterLocalService.increment());

		User user = userLocalService.getUserById(userId);

		mfaEmailOTP.setCompanyId(user.getCompanyId());
		mfaEmailOTP.setUserId(user.getUserId());
		mfaEmailOTP.setUserName(user.getFullName());

		mfaEmailOTP.setCreateDate(new Date());
		mfaEmailOTP.setModifiedDate(new Date());

		return mfaEmailOTPPersistence.update(mfaEmailOTP);
	}

	public MFAEmailOTP fetchEmailOTPByUserId(long userId) {
		return mfaEmailOTPPersistence.fetchByUserId(userId);
	}

	public MFAEmailOTP resetFailedAttempts(long userId) throws PortalException {
		MFAEmailOTP mfaEmailOTP = mfaEmailOTPPersistence.fetchByUserId(userId);

		if (mfaEmailOTP == null) {
			throw new NoSuchEmailOTPException("User ID " + userId);
		}

		mfaEmailOTP.setFailedAttempts(0);
		mfaEmailOTP.setLastFailDate(null);
		mfaEmailOTP.setLastFailIP(null);

		return mfaEmailOTPPersistence.update(mfaEmailOTP);
	}

	public MFAEmailOTP updateAttempts(long userId, String ip, boolean success)
		throws PortalException {

		MFAEmailOTP mfaEmailOTP = mfaEmailOTPPersistence.fetchByUserId(userId);

		if (mfaEmailOTP == null) {
			throw new NoSuchEmailOTPException("User ID " + userId);
		}

		if (success) {
			mfaEmailOTP.setFailedAttempts(0);
			mfaEmailOTP.setLastFailDate(null);
			mfaEmailOTP.setLastFailIP(null);
			mfaEmailOTP.setLastSuccessDate(new Date());
			mfaEmailOTP.setLastSuccessIP(ip);
		}
		else {
			mfaEmailOTP.setFailedAttempts(mfaEmailOTP.getFailedAttempts() + 1);
			mfaEmailOTP.setLastFailDate(new Date());
			mfaEmailOTP.setLastFailIP(ip);
		}

		return mfaEmailOTPPersistence.update(mfaEmailOTP);
	}

}