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

package com.liferay.portal.security.password.model;

import com.liferay.petra.sql.dsl.Column;
import com.liferay.petra.sql.dsl.base.BaseTable;

import java.sql.Types;

import java.util.Date;

/**
 * The table class for the &quot;PasswordHashProvider&quot; database table.
 *
 * @author Arthur Chan
 * @see PasswordHashProvider
 * @generated
 */
public class PasswordHashProviderTable
	extends BaseTable<PasswordHashProviderTable> {

	public static final PasswordHashProviderTable INSTANCE =
		new PasswordHashProviderTable();

	public final Column<PasswordHashProviderTable, Long> mvccVersion =
		createColumn(
			"mvccVersion", Long.class, Types.BIGINT, Column.FLAG_NULLITY);
	public final Column<PasswordHashProviderTable, String> uuid = createColumn(
		"uuid_", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<PasswordHashProviderTable, Long>
		passwordHashProviderId = createColumn(
			"passwordHashProviderId", Long.class, Types.BIGINT,
			Column.FLAG_PRIMARY);
	public final Column<PasswordHashProviderTable, Long> companyId =
		createColumn(
			"companyId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<PasswordHashProviderTable, Date> createDate =
		createColumn(
			"createDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<PasswordHashProviderTable, String> hashProviderName =
		createColumn(
			"hashProviderName", String.class, Types.VARCHAR,
			Column.FLAG_DEFAULT);
	public final Column<PasswordHashProviderTable, String> hashProviderMeta =
		createColumn(
			"hashProviderMeta", String.class, Types.VARCHAR,
			Column.FLAG_DEFAULT);

	private PasswordHashProviderTable() {
		super("PasswordHashProvider", PasswordHashProviderTable::new);
	}

}