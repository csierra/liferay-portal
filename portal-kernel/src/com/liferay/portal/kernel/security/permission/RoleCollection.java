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

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Role;

import java.util.List;
import java.util.function.Predicate;

/**
 * RoleCollection is used as the first argument to {@link
 * RoleContributor#contribute(RoleCollection, long, long)}. It holds the managed
 * collection of roles starting with the <em>initial</em> set calculated from
 * persisted role assignment and role inheritance.
 *
 * @author Carlos Sierra Andrés
 * @author Raymond Augé
 */
public interface RoleCollection {

	/**
	 * Add an array of roleIds to the collection.
	 *
	 * @param  roleIds array of roleIds to add to the collection
	 * @return true if all rolesIds were added to the collection
	 * @throws PortalException if any roleId results in a failed role lookup
	 */
	public boolean addAll(long[] roleIds) throws PortalException;

	/**
	 * Add a roleId to the collection.
	 *
	 * @param  roleId to add to the collection
	 * @return true if the roleId was added to the collection
	 * @throws PortalException
	 */
	public boolean addRoleId(long roleId) throws PortalException;

	/**
	 * Get the initial set of roles calculated from persisted assignment and
	 * inheritance.
	 *
	 * @return the initial set of roles calculated from persisted assignment
	 *         and inheritance
	 */
	public List<Role> getInitialRoles();

	/**
	 * Check if a Role is already in the collection by roleId.
	 *
	 * @param  roleId the roleId to check
	 * @return true of the Role is in the collection
	 */
	public boolean hasRoleId(long roleId);

	/**
	 * Remove Roles matching a predicate from the collection.
	 *
	 * @param  predicate used to determine if any Roles should be removed from
	 *         the collection
	 * @return true if the removal was successful
	 */
	public boolean removeIf(Predicate<Role> predicate);

}