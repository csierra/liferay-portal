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

package com.liferay.portal.upgrade.v7_0_0;

import com.liferay.portal.*;
import com.liferay.portal.kernel.dao.db.*;
import com.liferay.portal.kernel.spring.osgi.*;
import com.liferay.portal.kernel.upgrade.*;
import com.liferay.portal.upgrade.UpgradeMVCC;

import javax.naming.*;
import java.io.*;
import java.sql.*;

/**
 * @author Julio Camarero
 */
@OSGiBeanProperties(
	property = {
		"application.name=portal", "database=ALL", "from=6.2.0", "to=7.0.0"
	} ,
	service = Upgrade.class
)
public class UpgradeSchema extends UpgradeProcess implements Upgrade {

	@Override
	protected void doUpgrade() throws Exception {
		runSQLTemplate("update-6.2.0-7.0.0.sql", false);

		upgrade(UpgradeMVCC.class);
	}

	@Override
	public void upgrade(DatabaseProcessContext databaseProcessContext)
		throws UpgradeException {

		DatabaseContext databaseContext =
			databaseProcessContext.getDatabaseContext();

		DBFactory dbFactory = databaseContext.getDBFactory();

		DB db = dbFactory.getDB();

		try {
			db.runSQLTemplate("update-6.2.0-7.0.0.sql", true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}