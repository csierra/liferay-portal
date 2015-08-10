/*
 * *
 *  * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *  *
 *  * This library is free software; you can redistribute it and/or modify it under
 *  * the terms of the GNU Lesser General Public License as published by the Free
 *  * Software Foundation; either version 2.1 of the License, or (at your option)
 *  * any later version.
 *  *
 *  * This library is distributed in the hope that it will be useful, but WITHOUT
 *  * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 *  * details.
 *
 */

package com.liferay.portal.upgrade.internal.upgrade;

import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.upgrade.UpgradeMVCC;
import com.liferay.portal.upgrade.v7_0_0.util.ReleaseTable;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Brian Wing Shun Chan
 */
public class UpgradeRelease extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		addVersionColumn();

		transformBuildNumber();
	}

	protected void addVersionColumn() throws Exception {
		Connection connection = DataAccess.getUpgradeOptimizedConnection();

		DatabaseMetaData databaseMetaData = connection.getMetaData();

		UpgradeMVCC upgradeMVCC = new UpgradeMVCC();

		upgradeMVCC.upgradeMVCC(databaseMetaData, "Release_");
	}

	protected void transformBuildNumber() throws Exception {
		try {
			runSQL("alter_column_type Release_ buildNumber STRING");
		}
		catch (SQLException sqle) {
			upgradeTable(
				ReleaseTable.TABLE_NAME, ReleaseTable.TABLE_COLUMNS,
				ReleaseTable.TABLE_SQL_CREATE,
				ReleaseTable.TABLE_SQL_ADD_INDEXES);
		}

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DataAccess.getUpgradeOptimizedConnection();

			ps = con.prepareStatement(
				"select distinct buildNumber from Release_");

			rs = ps.executeQuery();

			while (rs.next()) {
				updateToNewFormat(rs.getString("buildNumber"));
			}
		}
		finally {
			DataAccess.cleanUp(con, ps, rs);
		}
	}

	protected void updateToNewFormat(String oldFormattedVersion)
		throws SQLException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DataAccess.getUpgradeOptimizedConnection();

			ps = con.prepareStatement(
				"update Release_ set buildNumber = ? where buildNumber = ?");

			char[] chars = oldFormattedVersion.toCharArray();

			StringBundler sb = new StringBundler(2 * chars.length);

			int i = 0;

			for (;i < chars.length - 1; i++) {
				sb.append(chars[i]);
				sb.append(".");
			}

			sb.append(chars[i]);

			ps.setString(1, sb.toString());
			ps.setString(2, oldFormattedVersion);

			ps.execute();
		}
		finally {
			DataAccess.cleanUp(con, ps, rs);
		}
	}

}