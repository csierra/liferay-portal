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

package com.liferay.portal.upgrade;

import com.liferay.portal.kernel.dao.db.DB;

/**
 * @author Miguel Pastor
 */
public interface ModuleUpgradeManager {

	public void execute(UpgradeContext upgradeContext);

	public void register(UpgradeRegistry registry);

	public static interface UpgradeRegistry {
		public void registerStep(String from, String to);
	}

	public class UpgradeContext {
		public UpgradeContext(
			String componentName, String from, String to, DB db) {

			_componentName = componentName;
			_from = from;
			_to = to;
			_db = db;
		}

		public String getComponentName() {
			return _componentName;
		}

		@Override
		public String toString() {
			return "UpgradeContext{" +
				"_componentName='" + _componentName + '\'' +
				", _db=" + _db +
				", _from='" + _from + '\'' +
				", _to='" + _to + '\'' +
				'}';
		}

		private String _componentName;
		private DB _db;
		private String _from;
		private String _to;
	}

}