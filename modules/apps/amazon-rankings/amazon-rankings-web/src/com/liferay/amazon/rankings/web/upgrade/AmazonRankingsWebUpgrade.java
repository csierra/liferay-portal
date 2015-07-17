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

package com.liferay.amazon.rankings.web.upgrade;

import com.liferay.amazon.rankings.web.constants.AmazonRankingsPortletKeys;

import com.liferay.portal.DatabaseProcessContext;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.upgrade.UpgradeException;
import com.liferay.portal.upgrade.api.Upgrade;
import com.liferay.portal.upgrade.constants.UpgradeWhiteboardConstants;
import com.liferay.portal.upgrade.util.UpgradePortletId;


import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 * @author Raymond Augé
 * @author Peter Fellwock
 */
@Component(
	immediate = true,
	property = {
		UpgradeWhiteboardConstants.DATABASES_ALL_PROPERTY,
		UpgradeWhiteboardConstants.APPLICATION_NAME + "=amazon-rankings-web",
		UpgradeWhiteboardConstants.FROM + "=0.0.0",
		UpgradeWhiteboardConstants.TO + "=1.0.0"
	},
	service = Upgrade.class)
public class AmazonRankingsWebUpgrade implements Upgrade {

	public void upgrade(DatabaseProcessContext databaseProcessContext) 
		throws UpgradeException {

		UpgradePortletId upgradePortletId = new UpgradePortletId() {

			@Override
			protected String[][] getRenamePortletIdsArray() {
				return new String[][] {
					new String[] {
						"67", AmazonRankingsPortletKeys.AMAZON_RANKINGS
					}
				};
			}

		};

		upgradePortletId.upgrade();
	}

}