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

package com.liferay.portal.upgrade.api;

import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBFactory;
import com.liferay.portal.kernel.upgrade.UpgradeException;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.service.configuration.ServiceComponentConfiguration;
import com.liferay.portal.spring.extender.loader.ModuleResourceLoader;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * @author Carlos Sierra Andrés
 */
public class InitialServiceUpgrade implements Upgrade {

	private Bundle _bundle;

	public void activate(BundleContext bundleContext) {
		_bundle = bundleContext.getBundle();
	}

	@Override
	public void upgrade(UpgradeContext upgradeContext) throws UpgradeException {
		ServiceComponentConfiguration loader = new ModuleResourceLoader(
			_bundle);

		try {
			String indexes = StringUtil.read(loader.getSQLIndexesInputStream());
			String sequences = StringUtil.read(
				loader.getSQLSequencesInputStream());
			String tables = StringUtil.read(loader.getSQLTablesInputStream());

			DBFactory dbFactory = upgradeContext.getDBFactory();

			DB db = dbFactory.getDB();

			db.runSQLTemplateString(tables, true, true);
			db.runSQLTemplateString(sequences, true, true);
			db.runSQLTemplateString(indexes, true, true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
