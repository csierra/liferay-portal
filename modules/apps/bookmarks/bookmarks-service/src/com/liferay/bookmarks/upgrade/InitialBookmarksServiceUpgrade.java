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

import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBFactory;
import com.liferay.portal.kernel.upgrade.UpgradeException;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.service.configuration.ServiceComponentConfiguration;
import com.liferay.portal.spring.extender.loader.ModuleResourceLoader;
import com.liferay.portal.upgrade.api.Upgrade;
import com.liferay.portal.upgrade.constants.UpgradeWhiteboardConstants;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 * @author Carlos Sierra Andrés
 */
@Component(
	immediate = true,
	property = {
		UpgradeWhiteboardConstants.APPLICATION_NAME + "=bookmarks",
		UpgradeWhiteboardConstants.FROM + "=0.0.0",
		UpgradeWhiteboardConstants.TO + "=1.0.0",
	},
	service = Upgrade.class)
public class InitialBookmarksServiceUpgrade implements Upgrade {

	private Bundle _bundle;

	@Activate
	public void activate(BundleContext bundleContext) {
		_bundle = bundleContext.getBundle();
	}

	@Override
	public void upgrade(UpgradeContext upgradeContext) throws UpgradeException {
		ServiceComponentConfiguration loader = new ModuleResourceLoader(
			_bundle);

		try {
			String indexes = StringUtil.read(loader.getSQLIndexesInputStream());
			String tables = StringUtil.read(loader.getSQLTablesInputStream());

			DBFactory dbFactory = upgradeContext.getDBFactory();

			DB db = dbFactory.getDB();

			db.runSQLTemplateString(tables, true, true);
			db.runSQLTemplateString(indexes, true, true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
