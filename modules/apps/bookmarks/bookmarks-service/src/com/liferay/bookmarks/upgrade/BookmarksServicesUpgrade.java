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

package com.liferay.bookmarks.upgrade;

import com.liferay.bookmarks.upgrade.v1_0_0.UpgradeClassNames;
import com.liferay.bookmarks.upgrade.v1_0_0.UpgradePortletId;
import com.liferay.bookmarks.upgrade.v1_0_0.UpgradePortletSettings;
import com.liferay.portal.kernel.settings.SettingsFactory;
import com.liferay.portal.kernel.upgrade.UpgradeException;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;

import java.util.ArrayList;
import java.util.List;

import com.liferay.portal.upgrade.api.Upgrade;
import com.liferay.portal.upgrade.constants.UpgradeWhiteboardConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Carlos Sierra Andrés
 * @author Miguel Pastor
 */
@Component(
	immediate = true,
	property = {
		UpgradeWhiteboardConstants.ALL_DATABASES_PROPERTY,
		UpgradeWhiteboardConstants.APPLICATION_NAME + "=bookmarks",
		UpgradeWhiteboardConstants.FROM + "=0.0.1",
		UpgradeWhiteboardConstants.TO + "=1.0.0"
	},
	service = Upgrade.class)
public class BookmarksServicesUpgrade implements Upgrade {

	@Reference(unbind = "-")
	protected void setSettingsFactory(SettingsFactory settingsFactory) {
		_settingsFactory = settingsFactory;
	}

	@Override
	public void upgrade(UpgradeContext upgradeContext) throws UpgradeException {
		List<UpgradeProcess> upgradeProcesses = new ArrayList<>();

		upgradeProcesses.add(new UpgradePortletId());

		upgradeProcesses.add(new UpgradeClassNames());
		upgradeProcesses.add(new UpgradePortletSettings(_settingsFactory));

		for (UpgradeProcess upgradeProcess : upgradeProcesses) {
			upgradeProcess.upgrade();
		}
	}

	private SettingsFactory _settingsFactory;
}
