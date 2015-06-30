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
import com.liferay.portal.spring.extender.loader.ModuleResourceLoader;
import com.liferay.portal.upgrade.constants.UpgradeWhiteboardConstants;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * @author Carlos Sierra Andrés
 */
public class InitialServiceUpgrade implements Upgrade {

	public InitialServiceUpgrade(ModuleResourceLoader loader) {
		_loader = loader;
	}

	@Override
	public void upgrade(UpgradeContext upgradeContext) throws UpgradeException {
		try {
			String indexes = StringUtil.read(
				_loader.getSQLIndexesInputStream());
			String sequences = StringUtil.read(
				_loader.getSQLSequencesInputStream());
			String tables = StringUtil.read(_loader.getSQLTablesInputStream());

			DBFactory dbFactory = upgradeContext.getDBFactory();

			DB db = dbFactory.getDB();

			db.runSQLTemplateString(tables, true, true);

			db.runSQLTemplateString(indexes, true, true);
			db.runSQLTemplateString(sequences, true, true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ServiceRegistration<Upgrade> registerForApplication(
		BundleContext bundleContext, String applicationName, String to) {

		Dictionary<String, Object> properties = new Hashtable<>();

		properties.put(
			UpgradeWhiteboardConstants.APPLICATION_NAME, applicationName);
		properties.put(UpgradeWhiteboardConstants.FROM, "0.0.0");
		properties.put(UpgradeWhiteboardConstants.TO, to);

		ModuleResourceLoader loader = new ModuleResourceLoader(
			bundleContext.getBundle());

		return bundleContext.registerService(
			Upgrade.class, new InitialServiceUpgrade(loader), properties);
	}

	private ModuleResourceLoader _loader;

}
