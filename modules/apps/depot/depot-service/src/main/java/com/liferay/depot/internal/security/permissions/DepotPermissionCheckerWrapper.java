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

package com.liferay.depot.internal.security.permissions;

import com.liferay.depot.constants.DepotRolesConstants;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerWrapper;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.UserGroupRoleLocalService;
import com.liferay.portal.security.permission.PermissionCacheUtil;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Cristina González
 */
public class DepotPermissionCheckerWrapper extends PermissionCheckerWrapper {

	public DepotPermissionCheckerWrapper(
		PermissionChecker permissionChecker,
		GroupLocalService groupLocalService, RoleLocalService roleLocalService,
		UserGroupRoleLocalService userGroupRoleLocalService) {

		super(permissionChecker);

		_groupLocalService = groupLocalService;
		_roleLocalService = roleLocalService;
		_userGroupRoleLocalService = userGroupRoleLocalService;
	}

	@Override
	public PermissionChecker clone() {
		return new DepotPermissionCheckerWrapper(
			delegatePermissionChecker.clone(), _groupLocalService,
			_roleLocalService, _userGroupRoleLocalService);
	}

	@Override
	public boolean isGroupAdmin(long groupId) {
		if (!permissionChecker.isSignedIn()) {
			return false;
		}

		if (delegatePermissionChecker.isGroupAdmin(groupId)) {
			return true;
		}

		if (groupId <= 0) {
			return false;
		}

		try {
			return _getOrAddToPermissionCache(
				_groupLocalService.fetchGroup(groupId), this::_isGroupAdmin,
				DepotRolesConstants.DEPOT_ADMINISTRATOR);
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			return false;
		}
	}

	@Override
	public boolean isGroupMember(long groupId) {
		if (!permissionChecker.isSignedIn()) {
			return false;
		}

		if (groupId <= 0) {
			return false;
		}

		try {
			return _isGroupMember(_groupLocalService.getGroup(groupId));
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			return false;
		}
	}

	@Override
	public boolean isGroupOwner(long groupId) {
		if (!permissionChecker.isSignedIn()) {
			return false;
		}

		if (delegatePermissionChecker.isGroupOwner(groupId)) {
			return true;
		}

		if (groupId <= 0) {
			return false;
		}

		try {
			return _getOrAddToPermissionCache(
				_groupLocalService.getGroup(groupId), this::_isGroupOwner,
				DepotRolesConstants.DEPOT_OWNER);
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			return false;
		}
	}

	private boolean _getOrAddToPermissionCache(
			Group group,
			UnsafeFunction<Group, Boolean, Exception> unsafeFunction,
			String roleName)
		throws Exception {

		if (group == null) {
			return false;
		}

		Boolean value = PermissionCacheUtil.getUserPrimaryKeyRole(
			getUserId(), group.getGroupId(), roleName);

		try {
			if (value == null) {
				value = unsafeFunction.apply(group);

				PermissionCacheUtil.putUserPrimaryKeyRole(
					getUserId(), group.getGroupId(), roleName, value);
			}
		}
		catch (Exception exception) {
			PermissionCacheUtil.removeUserPrimaryKeyRole(
				getUserId(), group.getGroupId(), roleName);

			throw exception;
		}

		return value;
	}

	private boolean _isGroupAdmin(Group group) throws Exception {
		if (Objects.equals(group.getType(), GroupConstants.TYPE_DEPOT)) {
			if (_userGroupRoleLocalService.hasUserGroupRole(
					getUserId(), group.getGroupId(),
					DepotRolesConstants.DEPOT_ADMINISTRATOR, true) ||
				_userGroupRoleLocalService.hasUserGroupRole(
					getUserId(), group.getGroupId(),
					DepotRolesConstants.DEPOT_OWNER, true)) {

				return true;
			}

			Group parentGroup = group;

			while (!parentGroup.isRoot()) {
				parentGroup = parentGroup.getParentGroup();

				if (permissionChecker.hasPermission(
						parentGroup, Group.class.getName(),
						String.valueOf(parentGroup.getGroupId()),
						ActionKeys.MANAGE_SUBGROUPS)) {

					return true;
				}
			}
		}

		return false;
	}

	private boolean _isGroupMember(Group group) throws Exception {
		long[] roleIds = getRoleIds(getUserId(), group.getGroupId());

		Role role = _roleLocalService.getRole(
			group.getCompanyId(), DepotRolesConstants.DEPOT_MEMBER);

		if (Arrays.binarySearch(roleIds, role.getRoleId()) >= 0) {
			return true;
		}

		return delegatePermissionChecker.isGroupMember(group.getGroupId());
	}

	private boolean _isGroupOwner(Group group) throws PortalException {
		if (Objects.equals(group.getType(), GroupConstants.TYPE_DEPOT) &&
			_userGroupRoleLocalService.hasUserGroupRole(
				getUserId(), group.getGroupId(),
				DepotRolesConstants.DEPOT_OWNER, true)) {

			return true;
		}

		return false;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DepotPermissionCheckerWrapper.class);

	private final GroupLocalService _groupLocalService;
	private final RoleLocalService _roleLocalService;
	private final UserGroupRoleLocalService _userGroupRoleLocalService;

}