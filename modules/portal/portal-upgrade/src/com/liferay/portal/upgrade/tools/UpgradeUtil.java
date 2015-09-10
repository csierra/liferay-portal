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

package com.liferay.portal.upgrade.tools;

import com.liferay.portal.kernel.upgrade.UpgradeStep;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.upgrade.internal.UpgradeInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * @author Carlos Sierra Andrés
 */
public class UpgradeUtil {

	public static List<ServiceRegistration<UpgradeStep>> register(
		BundleContext bundleContext, String upgradeBundleSymbolicName,
		String upgradeFromSchemaVersion, String upgradeToSchemaVersion,
		Collection<UpgradeStep> upgradeSteps) {

		UpgradeStep[] upgradeStepsArray = new UpgradeStep[upgradeSteps.size()];

		return register(
			bundleContext, upgradeBundleSymbolicName, upgradeFromSchemaVersion,
			upgradeToSchemaVersion, upgradeSteps.toArray(upgradeStepsArray));
	}

	public static List<ServiceRegistration<UpgradeStep>> register(
		BundleContext bundleContext, String upgradeBundleSymbolicName,
		String upgradeFromSchemaVersion, String upgradeToSchemaVersion,
		UpgradeStep ... upgradeSteps) {

		List<UpgradeInfo> upgradeInfos = buildUpgradeInfos(
			upgradeFromSchemaVersion, upgradeToSchemaVersion, upgradeSteps);

		List<ServiceRegistration<UpgradeStep>> serviceRegistrations =
			new ArrayList<>();

		for (UpgradeInfo upgradeInfo : upgradeInfos) {
			ServiceRegistration<UpgradeStep> upgradeStep = _registerUpgradeStep(
				bundleContext, upgradeBundleSymbolicName, upgradeInfo);

			serviceRegistrations.add(upgradeStep);
		}

		return serviceRegistrations;
	}

	protected static List<UpgradeInfo> buildUpgradeInfos(
		String upgradeFromVersion, String upgradeToVersion,
		UpgradeStep... upgradeSteps) {

		if (ArrayUtil.isEmpty(upgradeSteps)) {
			return Collections.emptyList();
		}

		List<UpgradeInfo> upgradeInfos = new ArrayList<>();

		String from = upgradeFromVersion;

		int upgradeStepsLength = upgradeSteps.length;

		for (int i = 0; i < upgradeStepsLength - 1; i++) {
			UpgradeStep upgradeStep = upgradeSteps[i];

			String to =
				upgradeToVersion + "-" + "step" + (i - upgradeStepsLength + 1);

			upgradeInfos.add(new UpgradeInfo(from, to, upgradeStep));

			from = to;
		}

		UpgradeInfo upgradeInfo = new UpgradeInfo(
			from, upgradeToVersion, upgradeSteps[upgradeStepsLength - 1]);

		upgradeInfos.add(upgradeInfo);

		return upgradeInfos;
	}

	private static ServiceRegistration<UpgradeStep> _registerUpgradeStep(
		BundleContext bundleContext, String upgradeBundleSymbolicName,
		UpgradeInfo upgradeInfo) {

		Dictionary<String, Object> properties;
		properties = new Hashtable<>();

		properties.put("upgrade.db.type", "any");
		properties.put(
			"upgrade.from.version", upgradeInfo.getFromSchemaVersionString());
		properties.put(
			"upgrade.to.version", upgradeInfo.getToSchemaVersionString());
		properties.put(
			"upgrade.bundle.symbolic.name", upgradeBundleSymbolicName);

		return bundleContext.registerService(
			UpgradeStep.class, upgradeInfo.getUpgradeStep(), properties);
	}

}