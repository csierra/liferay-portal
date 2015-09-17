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
import java.util.Arrays;
import java.util.Collection;

import org.osgi.service.component.annotations.Component;

/**
 * @author Marcellus Tavares
 */
@Component(immediate = true)
public class DDMServiceUpgrade implements UpgradeStepRegistrator {

	@Override
	public void register(Registry registry) {
		registry.register(
			"com.liferay.dynamic.data.mapping.service", "0.0.1", "1.0.0",
			Arrays.<UpgradeStep>asList(
				new UpgradeSchema(),
				new UpgradeClassNames(),
				new UpgradeDynamicDataMapping(),
				new UpgradeLastPublishDate()
			));
	}
}