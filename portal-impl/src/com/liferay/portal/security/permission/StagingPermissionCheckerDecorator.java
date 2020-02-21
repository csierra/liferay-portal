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

package com.liferay.portal.security.permission;

import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerDecorator;
import com.liferay.portal.kernel.spring.osgi.OSGiBeanProperties;

/**
 * @author Carlos Sierra
 * @author Marta Medio
 */
@OSGiBeanProperties(
	property = "service.ranking:Integer=20",
	service = PermissionCheckerDecorator.class
)
public class StagingPermissionCheckerDecorator
	implements PermissionCheckerDecorator {

	@Override
	public PermissionChecker decorate(PermissionChecker permissionChecker) {
		return new StagingPermissionChecker(
			permissionChecker, this::getDecoratedPermissionChecker);
	}

	public PermissionChecker getDecoratedPermissionChecker() {
		return _decoratedPermissionChecker;
	}

	public void setDecoratedPermissionChecker(
		PermissionChecker decoratedPermissionChecker) {

		_decoratedPermissionChecker = decoratedPermissionChecker;
	}

	private PermissionChecker _decoratedPermissionChecker;

}