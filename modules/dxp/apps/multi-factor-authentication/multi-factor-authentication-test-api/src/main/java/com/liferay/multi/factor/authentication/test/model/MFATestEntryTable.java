/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.multi.factor.authentication.test.model;

import com.liferay.petra.sql.dsl.Column;
import com.liferay.petra.sql.dsl.base.BaseTable;
import com.liferay.portal.kernel.service.persistence.impl.UserInputString;

import java.sql.Types;

/**
 * The table class for the &quot;MFATestEntry&quot; database table.
 *
 * @author Arthur Chan
 * @see MFATestEntry
 * @generated
 */
public class MFATestEntryTable extends BaseTable<MFATestEntryTable> {

	public static final MFATestEntryTable INSTANCE = new MFATestEntryTable();

	public final Column<MFATestEntryTable, Long> mvccVersion = createColumn(
		"mvccVersion", Long.class, Types.BIGINT, Column.FLAG_NULLITY);
	public final Column<MFATestEntryTable, Long> mfaTestEntryId = createColumn(
		"mfaTestEntryId", Long.class, Types.BIGINT, Column.FLAG_PRIMARY);
	public final Column<MFATestEntryTable, String> testString = createColumn(
		"testString", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<MFATestEntryTable, UserInputString>
		testUserInputString = createColumn(
			"testUserInputString", UserInputString.class, Types.VARCHAR,
			Column.FLAG_DEFAULT);

	private MFATestEntryTable() {
		super("MFATestEntry", MFATestEntryTable::new);
	}

}