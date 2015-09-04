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

package com.liferay.portal.lock.upgrade;

import com.liferay.portal.kernel.upgrade.UpgradeStep;
import com.liferay.portal.lock.upgrade.v1_0_0.UpgradeLock;
import com.liferay.portal.upgrade.tools.UpgradeStepRegistrator;

import java.util.Collection;
import java.util.Collections;

import org.osgi.service.component.annotations.Component;

/**
 * @author Miguel Pastor
 */
@Component(immediate = true)
public class LockServiceUpgrade implements UpgradeStepRegistrator {

	@Override
	public String getBundleSymbolicName() {
		return "com.liferay.portal.lock.service";
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
		return Collections.<UpgradeStep>singleton(new UpgradeLock());
	}

}