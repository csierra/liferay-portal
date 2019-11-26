/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.service.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.pwd.PasswordHelperUtil;
import com.liferay.portal.service.base.PasswordTrackerLocalServiceBaseImpl;

/**
 * @author Brian Wing Shun Chan
 * @author Scott Lee
 * @deprecated As of Mueller (7.2.x), replaced by {@link
 *             PasswordHelperUtil}
 */
@Deprecated
public class PasswordTrackerLocalServiceImpl
	extends PasswordTrackerLocalServiceBaseImpl {

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	@Override
	public void deletePasswordTrackers(long userId) {
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             PasswordHelperUtil#compare(long, String, boolean)}
	 */
	@Deprecated
	@Override
	public boolean isSameAsCurrentPassword(long userId, String newClearTextPwd)
		throws PortalException {

		return PasswordHelperUtil.compare(userId, newClearTextPwd, false);
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             PasswordHelperUtil#compareHistory(long, String, boolean)}
	 */
	@Deprecated
	@Override
	public boolean isValidPassword(long userId, String newClearTextPwd)
		throws PortalException {

		return !PasswordHelperUtil.compareHistory(
			userId, newClearTextPwd, false);
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	@Override
	public void trackPassword(long userId, String encPassword)
		throws PortalException {
	}

}