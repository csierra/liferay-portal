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
import com.liferay.portal.model.User;

/**
 * @author Carlos Sierra Andrés
 */
public abstract class ServiceScope implements AutoCloseable {

	public static class Provided extends ServiceScope {

		@Override
		public Company getCompany() {
			return null;
		}

		@Override
		public Group getGroup() {
			return null;
		}

		@Override
		public User getUser() {
			return null;
		}
	}

	public abstract Company getCompany();
	public abstract Group getGroup();
	public abstract User getUser();

	@Override
	public void close() {

	}
}
