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

package com.liferay.bookmarks.upgrade;


import com.liferay.portal.upgrade.ModuleUpgradeManager;
import org.osgi.service.component.annotations.Component;

/**
 * @author Miguel Pastor
 */
@Component(property = "component.name=bookmarks")
public class BookmarksUpgradeManager implements ModuleUpgradeManager {

	@Override
	public void execute(UpgradeContext upgradeContext) {
		System.out.println(upgradeContext);
	}

	@Override
	public void register(UpgradeRegistry registry) {
		registry.registerStep("0", "1.0");
		registry.registerStep("1.0", "1.1");
	}

}