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

package com.liferay.portal.upgrade.v6_1_0;

import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.upgrade.UpgradeMVCC;
import com.liferay.portal.upgrade.UpgradeSchemaAPI_7_0_0;

/**
 * @author Brian Wing Shun Chan
 */
public class UpgradeSchema extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		runSQLTemplate("update-6.0.6-6.1.0.sql", false);

		upgrade(UpgradeMVCC.class);
		upgrade(UpgradeSchemaAPI_7_0_0.class);
	}

}