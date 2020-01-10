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

package com.liferay.multi.factor.authentication.email.otp.model;

import com.liferay.portal.kernel.annotation.ImplementationClassName;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.util.Accessor;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The extended model interface for the MFAEmailOTP service. Represents a row in the &quot;MFAEmailOTP&quot; database table, with each column mapped to a property of this class.
 *
 * @author Arthur Chan
 * @see MFAEmailOTPModel
 * @generated
 */
@ImplementationClassName(
	"com.liferay.multi.factor.authentication.email.otp.model.impl.MFAEmailOTPImpl"
)
@ProviderType
public interface MFAEmailOTP extends MFAEmailOTPModel, PersistedModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add methods to <code>com.liferay.multi.factor.authentication.email.otp.model.impl.MFAEmailOTPImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static final Accessor<MFAEmailOTP, Long> EMAIL_OTP_ID_ACCESSOR =
		new Accessor<MFAEmailOTP, Long>() {

			@Override
			public Long get(MFAEmailOTP mfaEmailOTP) {
				return mfaEmailOTP.getEmailOTPId();
			}

			@Override
			public Class<Long> getAttributeClass() {
				return Long.class;
			}

			@Override
			public Class<MFAEmailOTP> getTypeClass() {
				return MFAEmailOTP.class;
			}

		};

}