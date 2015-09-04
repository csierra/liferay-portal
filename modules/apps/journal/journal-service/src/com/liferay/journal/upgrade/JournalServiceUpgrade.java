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

package com.liferay.journal.upgrade;

import com.liferay.journal.upgrade.v1_0_0.UpgradeClassNames;
import com.liferay.journal.upgrade.v1_0_0.UpgradeJournal;
import com.liferay.journal.upgrade.v1_0_0.UpgradeJournalArticleType;
import com.liferay.journal.upgrade.v1_0_0.UpgradeJournalDisplayPreferences;
import com.liferay.journal.upgrade.v1_0_0.UpgradeLastPublishDate;
import com.liferay.journal.upgrade.v1_0_0.UpgradeSchema;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.kernel.dao.db.DBProcessContext;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.upgrade.UpgradeException;
import com.liferay.portal.kernel.upgrade.UpgradeStep;
import com.liferay.portal.upgrade.tools.UpgradeStepRegistrator;

import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Collection;

import org.osgi.service.component.annotations.Component;

/**
 * @author Eduardo Garcia
 */
@Component(immediate = true)
public class JournalServiceUpgrade implements UpgradeStepRegistrator {

	@Override
	public String getBundleSymbolicName() {
		return "com.liferay.journal.service";
	}

	@Override
	public String getFromVersion() {
		return "0.0.1";
	}

	@Override
	public String getToVersion() {
		return "1.0.0";
	}

	public Collection<UpgradeStep> getUpgradeSteps() {
		Collection<UpgradeStep> upgradeSteps = new ArrayList<>();

		upgradeSteps.add(new UpgradeSchema());

		upgradeSteps.add(new UpgradeClassNames());
		upgradeSteps.add(new UpgradeJournal());
		upgradeSteps.add(new UpgradeJournalArticleType());
		upgradeSteps.add(new UpgradeJournalDisplayPreferences());
		upgradeSteps.add(new UpgradeLastPublishDate());
		upgradeSteps.add(new UpgradeStep() {
			@Override
			public void upgrade(DBProcessContext dbProcessContext)
				throws UpgradeException {

				try {
					deleteTempImages();
				}
				catch (Exception e) {
					e.printStackTrace(
						new PrintWriter(
							dbProcessContext.getOutputStream(), true));
				}
			}
		});

		return upgradeSteps;
	}

	protected void deleteTempImages() throws Exception {
		if (_log.isDebugEnabled()) {
			_log.debug("Delete temporary images");
		}

		DB db = DBFactoryUtil.getDB();

		db.runSQL(
			"delete from Image where imageId IN (SELECT articleImageId FROM " +
				"JournalArticleImage where tempImage = TRUE)");

		db.runSQL("delete from JournalArticleImage where tempImage = TRUE");
	}

	private static final Log _log = LogFactoryUtil.getLog(
		JournalServiceUpgrade.class);

}