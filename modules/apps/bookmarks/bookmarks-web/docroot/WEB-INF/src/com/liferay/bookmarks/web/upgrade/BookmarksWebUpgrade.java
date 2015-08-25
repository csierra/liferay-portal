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

package com.liferay.bookmarks.web.upgrade;

import com.liferay.bookmarks.web.upgrade.v1_0_0.UpgradeAdminPortlets;
import com.liferay.bookmarks.web.upgrade.v1_0_0.UpgradePortletPreferences;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;

import com.liferay.portal.upgrade.constants.UpgradeWhiteboardConstants;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Miguel Pastor
 */
@Component(
	immediate = true,
	property = {
		UpgradeWhiteboardConstants.DATABASES_ALL_PROPERTY,
		UpgradeWhiteboardConstants.APPLICATION_NAME + "=com.liferay.bookmarks.web",
		UpgradeWhiteboardConstants.FROM + "=-1.-1.-1.-1",
		UpgradeWhiteboardConstants.TO + "=1.0.0.0"
	},
	service = UpgradeProcess.class
)
public class BookmarksWebUpgrade extends UpgradeProcess {

	@Reference(target = ModuleServiceLifecycle.SPRING_INITIALIZED, unbind = "-")
	protected void setModuleServiceLifecycle(
		ModuleServiceLifecycle moduleServiceLifecycle) {
	}

	@Override
	protected void doUpgrade() throws PortalException {
		UpgradeProcess upgradeProcess = new UpgradeAdminPortlets();

		upgradeProcess.upgrade();

		upgradeProcess = new UpgradePortletPreferences();

		upgradeProcess.upgrade();
	}

}