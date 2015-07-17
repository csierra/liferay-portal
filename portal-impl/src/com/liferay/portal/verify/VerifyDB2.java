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

package com.liferay.portal.verify;

import com.liferay.portal.DatabaseProcessContext;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.spring.osgi.OSGiBeanProperties;
import com.liferay.portal.kernel.util.Consumer;
import com.liferay.portal.kernel.util.StringBundler;

import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Igor Beslic
 */
@OSGiBeanProperties(property = {"verifier.name=DB2"}, service = Verifier.class)
public class VerifyDB2 extends VerifyProcess implements Verifier {

	protected void doWithResultSet(Consumer<ResultSet> consumer)
		throws SQLException {

		DB db = DBFactoryUtil.getDB();

		String dbType = db.getType();

		if (!dbType.equals(DB.TYPE_DB2)) {
			return;
		}

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DataAccess.getUpgradeOptimizedConnection();

			StringBundler sb = new StringBundler(4);

			sb.append("select tbname, name, coltype, length from ");
			sb.append("sysibm.syscolumns where tbcreator = (select distinct ");
			sb.append("current schema from sysibm.sysschemata) AND coltype = ");
			sb.append("'VARCHAR' and length = 500");

			ps = con.prepareStatement(sb.toString());

			rs = ps.executeQuery();

			consumer.accept(rs);
		}
		finally {
			DataAccess.cleanUp(con, ps, rs);
		}
	}

	@Override
	protected void doVerify() throws Exception {
		doWithResultSet(new Consumer<ResultSet>() {

			@Override
			public void accept(ResultSet rs) {
				try {
					while (rs.next()) {
						String tableName = rs.getString(1);

						if (!isPortalTableName(tableName)) {
							continue;
						}

						String columnName = rs.getString(2);

						runSQL(
							"alter table " + tableName + " alter column " +
								columnName + " set data type varchar(600)");
					}
				}
				catch (Exception e) {
					throw new RuntimeException(e);
				}
			}

		});
	}

	@Override
	public void verify(DatabaseProcessContext verifyContext) {
		OutputStream outputStream = verifyContext.getOutputStream();

		final PrintStream out = new PrintStream(outputStream);

		try {
			doWithResultSet(new Consumer<ResultSet>() {

				@Override
				public void accept(ResultSet rs) {
					out.println("Columns to alter:");

					try {
						while (rs.next()) {
							String tableName = rs.getString(1);

							if (!isPortalTableName(tableName)) {
								continue;
							}

							String columnName = rs.getString(2);

							out.println(
								"Column: " + columnName + "; Table: " +
									tableName);
						}
					}
					catch (Exception e) {
						throw new RuntimeException(e);
					}
				}

			});
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}