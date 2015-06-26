/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.upgrade.api;

import com.liferay.portal.kernel.dao.db.DBFactory;
import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.upgrade.UpgradeException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Carlos Sierra Andrés
 */
public interface Upgrade {

	public void upgrade(UpgradeContext upgradeContext) throws UpgradeException;

	public class UpgradeContext {
		public DBFactory getDBFactory() {
			return DBFactoryUtil.getDBFactory();
		}

		public Connection getDataAccess() {
			try {
				return DataAccess.getConnection();
			}
			catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}

		public Connection getUpgradeOptimizedConnection() {
			try {
				return DataAccess.getUpgradeOptimizedConnection();
			}
			catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}


	}



}
