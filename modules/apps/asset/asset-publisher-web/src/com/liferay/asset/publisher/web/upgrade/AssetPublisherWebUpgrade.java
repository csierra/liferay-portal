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

package com.liferay.asset.publisher.web.upgrade;

import com.liferay.asset.publisher.web.constants.AssetPublisherPortletKeys;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.upgrade.util.UpgradePortletId;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 * @author Eudaldo Alonso
 */
@Component(immediate = true, service = AssetPublisherWebUpgrade.class)
public class AssetPublisherWebUpgrade {

	@Activate
	protected void upgrade() throws PortalException {
		UpgradePortletId upgradePortletId = new UpgradePortletId() {

			@Override
			protected String[][] getRenamePortletIdsArray() {
				return new String[][] {
					new String[] {
						"101", AssetPublisherPortletKeys.ASSET_PUBLISHER
					},
					new String[] {
						"173", AssetPublisherPortletKeys.RECENT_CONTENT
					},
					new String[] {
						"175", AssetPublisherPortletKeys.RELATED_ASSETS
					},
					new String[] {
						"193", AssetPublisherPortletKeys.MOST_VIEWED_ASSETS
					},
					new String[] {
						"194", AssetPublisherPortletKeys.HIGHEST_RATED_ASSETS
					}
				};
			}

		};
	}

}