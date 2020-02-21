/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

package com.liferay.portal.kernel.security.permission;

import com.liferay.portal.kernel.security.permission.contributor.RoleContributor;

/**
 * @author Carlos Sierra Andrés
 * @author Marta Medio
 */
public interface PermissionCheckerDecorator {

	public PermissionChecker decorate(PermissionChecker permissionChecker);

	public default PermissionChecker decorate(
		PermissionChecker permissionChecker,
		RoleContributor[] roleContributors) {

		return decorate(permissionChecker);
	}

	public void setDecoratedPermissionChecker(
		PermissionChecker permissionChecker);

}