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

package com.liferay.portal.kernel.security.permission;

/**
 * RoleContributors are invoked during permission checking allowing the
 * calculated roles to be altered dynamically. In order to support such dynamic
 * behavior RoleContributors are invoked without the benefit of external
 * caching. In this respect implementations must make every attempt to be as
 * efficient as possible or risk performance degradation.
 *
 * @author Raymond Augé
 */
public interface RoleContributor {

	/**
	 * Contribute to the collection of user roles.
	 *
	 * @param roleCollection the pre-calculated collection of roles
	 * @param userId the current userId
	 * @param groupId the current groupId
	 * @return modified, and sorted, array of roleIds
	 */
	public void contribute(
		RoleCollection roleCollection, long userId, long groupId);

}