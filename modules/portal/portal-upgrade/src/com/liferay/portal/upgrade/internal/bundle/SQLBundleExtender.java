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

package com.liferay.portal.upgrade.internal.bundle;

import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.upgrade.UpgradeException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.upgrade.api.Upgrade;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;

import java.net.URL;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * @author Carlos Sierra Andrés
 */
@Component
public class SQLBundleExtender {

	private BundleTracker<Bundle> _bundleTracker;

	@Activate
	public void activate(final BundleContext bundleContext) {
		_bundleTracker = new BundleTracker<>(
			bundleContext, Bundle.ACTIVE,
			new BundleTrackerCustomizer<Bundle>() {

				@Override
				public Bundle addingBundle(Bundle bundle, BundleEvent event) {
					Dictionary<String, String> headers = bundle.getHeaders();

					String SqlUpgrades = headers.get("SQL-Upgrades");

					if (SqlUpgrades != null) {
						Enumeration<URL> entries = bundle.findEntries(
							SqlUpgrades, "*.sql", true);

						while (entries.hasMoreElements()) {
							final URL url = entries.nextElement();

							String entryPath =
								url.getPath().substring(
									SqlUpgrades.length() + 1);

							String[] tokens = entryPath.split(StringPool.SLASH);

							String transition = tokens[0];

							String[] transitionTokens = transition.split("-");

							String from = transitionTokens[0];
							String to = transitionTokens[1];

							String database = tokens[1];

							Dictionary<String, Object> properties =
								new Hashtable<>();

							properties.put("database", database);
							properties.put("from", from);
							properties.put("to", to);
							properties.put("application.name", "bookmarks");

							bundle.getBundleContext().registerService(
								Upgrade.class, new Upgrade() {
									@Override
									public void upgrade(
											UpgradeContext upgradeContext) 
										throws UpgradeException {

										try {
											String sql = StringUtil.read(
												url.openStream());

											DB db = upgradeContext.getDBFactory().getDB();

											db.runSQLTemplateString(sql, true, true);
										}
										catch (Exception e) {
											throw new UpgradeException(e);
										}
									}
								}, properties);

						}
					}

					return bundle;
				}

				@Override
				public void modifiedBundle(
					Bundle bundle, BundleEvent event, Bundle object) {

				}

				@Override
				public void removedBundle(
					Bundle bundle, BundleEvent event, Bundle object) {

				}
			});

		_bundleTracker.open();
	}

	@Deactivate
	public void deactivate() {
		_bundleTracker.close();
	}
}
