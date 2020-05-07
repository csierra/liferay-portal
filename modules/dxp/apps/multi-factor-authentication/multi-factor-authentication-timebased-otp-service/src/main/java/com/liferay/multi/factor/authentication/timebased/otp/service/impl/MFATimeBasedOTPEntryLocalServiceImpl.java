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

package com.liferay.multi.factor.authentication.timebased.otp.service.impl;

import com.liferay.multi.factor.authentication.timebased.otp.exception.NoSuchEntryException;
import com.liferay.multi.factor.authentication.timebased.otp.model.MFATimeBasedOTPEntry;
import com.liferay.multi.factor.authentication.timebased.otp.service.base.MFATimeBasedOTPEntryLocalServiceBaseImpl;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;

import java.util.Date;

import org.osgi.service.component.annotations.Component;

/**
 * The implementation of the mfa timebased otp entry local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>com.liferay.multi.factor.authentication.timebased.otp.service.MFATimeBasedOTPEntryLocalService</code> interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Arthur Chan
 * @see MFATimeBasedOTPEntryLocalServiceBaseImpl
 */
@Component(
	property = "model.class.name=com.liferay.multi.factor.authentication.timebased.otp.model.MFATimeBasedOTPEntry",
	service = AopService.class
)
public class MFATimeBasedOTPEntryLocalServiceImpl
	extends MFATimeBasedOTPEntryLocalServiceBaseImpl {

	public MFATimeBasedOTPEntry addTimeBasedOTPEntry(
			String sharedSecret, long userId)
		throws PortalException {

		MFATimeBasedOTPEntry mfaTimeBasedOTPEntry =
			mfaTimeBasedOTPEntryPersistence.fetchByUserId(userId);

		if (mfaTimeBasedOTPEntry != null) {
			throw new IllegalArgumentException(
				"There is already one TimeBased One-Time password for user " +
					userId);
		}

		User user = userLocalService.getUserById(userId);

		long entryId = counterLocalService.increment();

		mfaTimeBasedOTPEntry = mfaTimeBasedOTPEntryPersistence.create(entryId);

		mfaTimeBasedOTPEntry.setCompanyId(user.getCompanyId());
		mfaTimeBasedOTPEntry.setUserId(userId);
		mfaTimeBasedOTPEntry.setUserName(user.getFullName());
		mfaTimeBasedOTPEntry.setCreateDate(new Date());
		mfaTimeBasedOTPEntry.setSharedSecret(sharedSecret);

		return mfaTimeBasedOTPEntryPersistence.update(mfaTimeBasedOTPEntry);
	}

	public MFATimeBasedOTPEntry fetchMFATimeBasedOTPEntryByUserId(long userId) {
		return mfaTimeBasedOTPEntryPersistence.fetchByUserId(userId);
	}

	public MFATimeBasedOTPEntry resetFailedAttempts(long userId)
		throws PortalException {

		MFATimeBasedOTPEntry mfaTimeBasedOTPEntry =
			mfaTimeBasedOTPEntryPersistence.fetchByUserId(userId);

		if (mfaTimeBasedOTPEntry == null) {
			throw new NoSuchEntryException("User ID " + userId);
		}

		mfaTimeBasedOTPEntry.setFailedAttempts(0);
		mfaTimeBasedOTPEntry.setLastFailDate(null);
		mfaTimeBasedOTPEntry.setLastFailIP(null);

		return mfaTimeBasedOTPEntryPersistence.update(mfaTimeBasedOTPEntry);
	}

	public MFATimeBasedOTPEntry updateAttempts(
			long userId, String ip, boolean success)
		throws PortalException {

		MFATimeBasedOTPEntry mfaTimeBasedOTPEntry =
			mfaTimeBasedOTPEntryPersistence.fetchByUserId(userId);

		if (mfaTimeBasedOTPEntry == null) {
			throw new NoSuchEntryException("User ID " + userId);
		}

		if (success) {
			mfaTimeBasedOTPEntry.setFailedAttempts(0);
			mfaTimeBasedOTPEntry.setLastFailDate(null);
			mfaTimeBasedOTPEntry.setLastFailIP(null);
			mfaTimeBasedOTPEntry.setLastSuccessDate(new Date());
			mfaTimeBasedOTPEntry.setLastSuccessIP(ip);
		}
		else {
			mfaTimeBasedOTPEntry.setFailedAttempts(
				mfaTimeBasedOTPEntry.getFailedAttempts() + 1);
			mfaTimeBasedOTPEntry.setLastFailDate(new Date());
			mfaTimeBasedOTPEntry.setLastFailIP(ip);
		}

		return mfaTimeBasedOTPEntryPersistence.update(mfaTimeBasedOTPEntry);
	}

}