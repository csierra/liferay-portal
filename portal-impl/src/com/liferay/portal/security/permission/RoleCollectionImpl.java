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
import com.liferay.portal.kernel.util.ListUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author Raymond Augé
 */
public class RoleCollectionImpl implements RoleCollection {

	public RoleCollectionImpl(
		Collection<Role> roles, RoleLocalService roleLocalService) {

		_roles = new ArrayList<>(roles);
		_initialRoles = new ArrayList<>(roles);
		_roleLocalService = roleLocalService;
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
	public boolean addRoleId(long roleId) throws PortalException {
		return _roles.add(_roleLocalService.getRole(roleId));
	}

	@Override
	public List<Role> getInitialRoles() {
		return ListUtil.toList(_initialRoles, role -> (Role)role.clone());
	}

	@Override
	public boolean hasRoleId(long roleId) {
		Stream<Role> stream = _roles.stream();

		if (stream.anyMatch(role -> role.getRoleId() == roleId)) {
			return true;
		}

		return false;
	}

	@Override
	public boolean removeIf(Predicate<Role> predicate) {
		return _roles.removeIf(role -> predicate.test((Role)role.clone()));
	}

	protected List<Role> getRoleList() {
		return _roles;
	}

	private final List<Role> _initialRoles;
	private final RoleLocalService _roleLocalService;
	private final List<Role> _roles;

}