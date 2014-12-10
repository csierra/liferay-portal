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

package com.liferay.portal.kernel;

import com.liferay.portal.model.Company;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupedModel;
import com.liferay.portal.model.User;


/**
 * @author Carlos Sierra Andrés
 */
public abstract class AuditableScope implements AutoCloseable {

	public abstract Company getCompany();
	public abstract DateContext getDateContext();
	public abstract Group getGroup();
	public abstract User getUser();

	@Override
	public void close() {

	}
	
	public void propagate(GroupedModel model) {

		DateContext dateContext = getDateContext();
		Group group = getGroup();
		User user = getUser();

		model.setCompanyId(group.getCompanyId());
		model.setCreateDate(dateContext.getCurrentDate());
		model.setGroupId(group.getGroupId());
		model.setModifiedDate(dateContext.getCurrentDate());
		model.setUserId(user.getUserId());
		model.setUserName(user.getFullName());
	}
}
