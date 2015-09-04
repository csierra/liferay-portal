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

package com.liferay.dynamic.data.mapping.upgrade;

import com.liferay.dynamic.data.mapping.upgrade.v1_0_0.UpgradeClassNames;
import com.liferay.dynamic.data.mapping.upgrade.v1_0_0.UpgradeDynamicDataMapping;
import com.liferay.dynamic.data.mapping.upgrade.v1_0_0.UpgradeLastPublishDate;
import com.liferay.dynamic.data.mapping.upgrade.v1_0_0.UpgradeSchema;
import com.liferay.portal.kernel.upgrade.UpgradeStep;
import com.liferay.portal.upgrade.tools.UpgradeStepRegistrator;

import java.util.ArrayList;
import java.util.Collection;

import org.osgi.service.component.annotations.Component;

/**
 * @author Marcellus Tavares
 */
@Component(immediate = true)
public class DDMServiceUpgrade implements UpgradeStepRegistrator {

	@Override
	public String getBundleSymbolicName() {
		return "com.liferay.dynamic.data.mapping.service";
	}

	@Override
	public String getFromVersion() {
		return "0.0.1";
	}

	@Override
	public String getToVersion() {
		return "1.0.0";
	}

	@Override
	public Collection<UpgradeStep> getUpgradeSteps() {
		Collection<UpgradeStep> upgradeSteps = new ArrayList<>();

		upgradeSteps.add(new UpgradeSchema());
		upgradeSteps.add(new UpgradeClassNames());
		upgradeSteps.add(new UpgradeDynamicDataMapping());
		upgradeSteps.add(new UpgradeLastPublishDate());

		return upgradeSteps;
	}
}