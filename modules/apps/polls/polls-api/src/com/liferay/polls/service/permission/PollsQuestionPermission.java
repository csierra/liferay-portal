/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.polls.service.permission;

import com.liferay.polls.model.PollsQuestion;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.security.permission.BaseModelPermissionChecker;
import com.liferay.portal.security.permission.PermissionChecker;

/**
 * @author Carlos Sierra Andrés
 */
public interface PollsQuestionPermission extends BaseModelPermissionChecker {

	void check(
			PermissionChecker permissionChecker, long questionId,
			String actionId)
		throws PortalException;

	void check(
			PermissionChecker permissionChecker, PollsQuestion question,
			String actionId)
		throws PortalException;

	boolean contains(
			PermissionChecker permissionChecker, long questionId,
			String actionId)
		throws PortalException;

	boolean contains(
		PermissionChecker permissionChecker, PollsQuestion question,
		String actionId);

	void checkBaseModel(
			PermissionChecker permissionChecker, long groupId, long primaryKey,
			String actionId)
		throws PortalException;

}
