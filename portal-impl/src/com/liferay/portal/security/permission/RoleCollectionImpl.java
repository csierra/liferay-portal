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

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.security.permission.RoleCollection;
import com.liferay.portal.kernel.service.RoleLocalService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Raymond Augé
 */
@SuppressWarnings("serial")
public class RoleCollectionImpl implements RoleCollection {

	public RoleCollectionImpl(
		Collection<Role> roles, Collection<Role> forbiddenRoles,
		RoleLocalService roleLocalService) {

		_roles = new ArrayList<>(roles);
		_forbiddenRoles = forbiddenRoles;
		_roleLocalService = roleLocalService;
	}

	@Override
	public boolean addAll(Collection<Role> roles) {
		boolean changed = false;

		for (Role role : roles) {
			changed = changed | addRole(role);
		}

		return changed;
	}

	@Override
	public boolean addAll(long[] roleIds) throws PortalException {
		boolean changed = false;

		for (long roleId : roleIds) {
			changed = changed | addRoleId(roleId);
		}

		return changed;
	}

	@Override
	public boolean addRole(Role role) {
		if (role == null) {
			throw new IllegalArgumentException("Role can not be null");
		}

		if (_forbiddenRoles.contains(role)) {
			throw new IllegalArgumentException(
				"Role " + role + " can not be dynamically contributed");
		}

		return _roles.add(role);
	}

	@Override
	public boolean addRoleId(long roleId) throws PortalException {
		return _roles.add(_roleLocalService.getRole(roleId));
	}

	@Override
	public boolean removeIf(Predicate<Role> predicate) {
		return _roles.removeIf(predicate);
	}

	protected List<Role> getRoleList() {
		return _roles;
	}

	private final Collection<Role> _forbiddenRoles;
	private final RoleLocalService _roleLocalService;
	private final List<Role> _roles;

}