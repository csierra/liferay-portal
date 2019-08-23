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
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author Raymond Augé
 */
public class RoleCollectionImpl implements RoleCollection {

	public RoleCollectionImpl(
		Collection<Role> roles, List<String> forbiddenRoleNames,
		RoleLocalService roleLocalService) {

		_roles = new ArrayList<>(roles);
		_initialRoles = Collections.unmodifiableList(_roles);
		_forbiddenRoleNames = forbiddenRoleNames;
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
		return _addRole(_roleLocalService.getRole(roleId));
	}

	@Override
	public boolean removeIf(Predicate<Role> predicate) {
		return _roles.removeIf(predicate);
	}

	@Override
	public List<Role> getInitialRoles() {
		return _initialRoles;
	}

	protected List<Role> getRoleList() {
		return _roles;
	}

	@Override
	public boolean hasRoleId(long roleId) {
		Stream<Role> stream = _roles.stream();

		return stream.anyMatch(role -> role.getRoleId() == roleId);
	}

	private boolean _addRole(Role role) {
		if (_forbiddenRoleNames.contains(role.getName())) {
			throw new IllegalArgumentException(
				"Role " + role + " can not be dynamically contributed");
		}

		return _roles.add(role);
	}

	private final List<String> _forbiddenRoleNames;
	private final List<Role> _initialRoles;
	private final RoleLocalService _roleLocalService;
	private final List<Role> _roles;

}