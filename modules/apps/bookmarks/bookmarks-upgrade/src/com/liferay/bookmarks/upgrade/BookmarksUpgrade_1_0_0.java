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

import com.liferay.bookmarks.upgrade.v1_0_0.UpgradeAdminPortlets;
import com.liferay.bookmarks.upgrade.v1_0_0.UpgradeClassNames;
import com.liferay.bookmarks.upgrade.v1_0_0.UpgradePortletId;
import com.liferay.bookmarks.upgrade.v1_0_0.UpgradePortletPreferences;
import com.liferay.bookmarks.upgrade.v1_0_0.UpgradePortletSettings;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.settings.SettingsFactory;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.upgrade.whiteboard.UpgradeWhiteboardConstants;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Miguel Pastor
 */
@Component(
	immediate = true,
	property = {
		UpgradeWhiteboardConstants.UPGRADE_WHITEBOARD_RELEASE_NAME + "=com.liferay.bookmarks.service",
		UpgradeWhiteboardConstants.UPGRADE_WHITEBOARD_RELEASE_PREVIOUS_VERSION + "=0",
		UpgradeWhiteboardConstants.UPGRADE_WHITEBOARD_RELEASE_VERSION + "=" + BookmarksUpgrade_1_0_0.BUILD_NUMBER
	},
	service = UpgradeProcess.class
)
public class BookmarksUpgrade_1_0_0 extends UpgradeProcess {

	public static final int BUILD_NUMBER = 1;

	@Override
	public int getThreshold() {
		return BUILD_NUMBER;
	}

	@Override
	protected void doUpgrade() throws PortalException {
		for (UpgradeProcess upgradeProcess : getUpgradeProcesses()) {
			upgradeProcess.upgrade();
		}
	}

	protected List<UpgradeProcess> getUpgradeProcesses() {
		List<UpgradeProcess> upgradeProcesses = new ArrayList<>();

		upgradeProcesses.add(new UpgradePortletId());

		upgradeProcesses.add(new UpgradeClassNames());
		upgradeProcesses.add(new UpgradePortletSettings(_settingsFactory));

		upgradeProcesses.add(new UpgradeAdminPortlets());
		upgradeProcesses.add(new UpgradePortletPreferences());

		return upgradeProcesses;
	}

	@Reference(unbind = "-")
	protected void setSettingsFactory(SettingsFactory settingsFactory) {
		_settingsFactory = settingsFactory;
	}

	private SettingsFactory _settingsFactory;

}