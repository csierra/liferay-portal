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

package com.liferay.multi.factor.authentication.checker.email.otp.model.impl;

import com.liferay.multi.factor.authentication.checker.email.otp.model.EmailOTPEntry;
import com.liferay.multi.factor.authentication.checker.email.otp.service.EmailOTPEntryLocalServiceUtil;

/**
 * The extended model base implementation for the EmailOTPEntry service. Represents a row in the &quot;MFAEmailOTPEntry&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This class exists only as a container for the default extended model level methods generated by ServiceBuilder. Helper methods and all application logic should be put in {@link EmailOTPEntryImpl}.
 * </p>
 *
 * @author arthurchan35
 * @see EmailOTPEntryImpl
 * @see EmailOTPEntry
 * @generated
 */
public abstract class EmailOTPEntryBaseImpl
	extends EmailOTPEntryModelImpl implements EmailOTPEntry {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a email otp entry model instance should use the <code>EmailOTPEntry</code> interface instead.
	 */
	@Override
	public void persist() {
		if (this.isNew()) {
			EmailOTPEntryLocalServiceUtil.addEmailOTPEntry(this);
		}
		else {
			EmailOTPEntryLocalServiceUtil.updateEmailOTPEntry(this);
		}
	}

}