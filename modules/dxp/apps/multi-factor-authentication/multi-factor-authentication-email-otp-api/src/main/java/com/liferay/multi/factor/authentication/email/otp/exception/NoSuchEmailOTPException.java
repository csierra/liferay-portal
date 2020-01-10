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

package com.liferay.multi.factor.authentication.email.otp.exception;

import com.liferay.portal.kernel.exception.NoSuchModelException;

/**
 * @author Arthur Chan
 */
public class NoSuchEmailOTPException extends NoSuchModelException {

	public NoSuchEmailOTPException() {
	}

	public NoSuchEmailOTPException(String msg) {
		super(msg);
	}

	public NoSuchEmailOTPException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public NoSuchEmailOTPException(Throwable cause) {
		super(cause);
	}

}