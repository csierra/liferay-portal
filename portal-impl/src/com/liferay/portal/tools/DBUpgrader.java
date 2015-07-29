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

package com.liferay.portal.tools;

import com.liferay.portal.bean.BeanLocatorImpl;
import com.liferay.portal.cache.key.HashCodeCacheKeyGenerator;
import com.liferay.portal.cache.key.SimpleCacheKeyGenerator;
import com.liferay.portal.dao.db.DBFactoryImpl;
import com.liferay.portal.dao.jdbc.DataSourceFactoryImpl;
import com.liferay.portal.dao.orm.common.SQLTransformer;
import com.liferay.portal.kernel.bean.BeanLocator;
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.cache.key.CacheKeyGenerator;
import com.liferay.portal.kernel.cache.key.CacheKeyGeneratorUtil;
import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.dao.jdbc.DataSourceFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.sql.SQLTransformerUtil;
import com.liferay.portal.kernel.upgrade.UpgradeException;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.upgrade.util.UpgradeProcessUtil;
;
import com.liferay.portal.kernel.util.ClassLoaderUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.ReleaseInfo;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.kernel.xml.UnsecureSAXReaderUtil;
import com.liferay.portal.model.ReleaseConstants;
import com.liferay.portal.security.lang.DoPrivilegedUtil;
import com.liferay.portal.security.xml.SecureXMLFactoryProviderImpl;
import com.liferay.portal.security.xml.SecureXMLFactoryProviderUtil;
import com.liferay.portal.spring.context.ArrayApplicationContext;
import com.liferay.portal.util.InitUtil;
import com.liferay.portal.util.PropsImpl;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.util.PropsValues;

import java.io.Console;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import com.liferay.portal.xml.SAXReaderImpl;
import com.liferay.portlet.documentlibrary.util.DLImpl;
import com.liferay.portlet.documentlibrary.util.DLUtil;
import com.liferay.util.log4j.Log4JUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractRefreshableApplicationContext;

/**
 * @author Michael C. Han
 * @author Brian Wing Shun Chan
 */
public class DBUpgrader {

	public static void main(String[] args) {
		DBUpgrader dbUpgrader = new DBUpgrader();

		try {
			// upgradeCorePortal();

			_startMinimalServices();

		} catch (Exception e) {
			Thread.currentThread().interrupt();
		}
	}

	public void upgrade(){
		// Check release

		int buildNumber = _getBuildNumber();

		if (buildNumber > ReleaseInfo.getParentBuildNumber()) {
			StringBundler sb = new StringBundler(6);

			sb.append("Attempting to deploy an older Liferay Portal version. ");
			sb.append("Current build version is ");
			sb.append(buildNumber);
			sb.append(" and attempting to deploy version ");
			sb.append(ReleaseInfo.getParentBuildNumber());
			sb.append(".");

			throw new IllegalStateException(sb.toString());
		}
		else if (buildNumber < ReleaseInfo.RELEASE_5_2_3_BUILD_NUMBER) {
			String msg = "You must first upgrade to Liferay Portal 5.2.3";

			System.out.println(msg);

			throw new RuntimeException(msg);
		}

		// Upgrade

		if (_log.isDebugEnabled()) {
			_log.debug("Update build " + buildNumber);
		}

		_checkPermissionAlgorithm();
		_checkReleaseState(_getReleaseState());

		try {
			_upgradeProcess(buildNumber);
		}
		catch (Exception e) {
			_updateReleaseState(ReleaseConstants.STATE_UPGRADE_FAILURE);

			throw new RuntimeException(e);
		}

		// Delete temporary images

		if (_log.isDebugEnabled()) {
			_log.debug("Delete temporary images");
		}

		// Temporarily disabled due to LPS-56383

		// _deleteTempImages();

		// Clear the caches only if the upgrade process was run

		if (_log.isDebugEnabled()) {
			_log.debug("Clear cache if upgrade process was run");
		}
	}

	protected static void upgradeCorePortal() {
		Console console = System.console();

		if (console == null) {
			System.err.println("No console is available.");

			System.exit(1);
		}

		console.format("*****************************************\n");
		console.format("Portal is about to start in Upgrade mode.\n");
		console.format("*****************************************\n");

		String upgrade = null;

		do {
			upgrade = console.readLine(
				"Do you want to upgrade Liferay's Portal Core? (yes/no):");
		} while (!upgrade.equals("yes") && !upgrade.equals("no"));

		if (upgrade.equals("no")) {
			console.format(
				"Upgrade the core portal is mandatory in order to be able " +
					"to upgrade all the modules");

			System.exit(1);
		}

		DataSourceFactoryUtil.setDataSourceFactory(
			DoPrivilegedUtil.wrap(new DataSourceFactoryImpl()));

		DBFactoryUtil.setDBFactory(DoPrivilegedUtil.wrap(new DBFactoryImpl()));

		PortalClassLoaderUtil.setClassLoader(
			ClassLoaderUtil.getContextClassLoader());

		ApplicationContext applicationContext = new ArrayApplicationContext(
			PropsValues.SPRING_INFRASTRUCTURE_CONFIGS);

		BeanLocator beanLocator = new BeanLocatorImpl(
			ClassLoaderUtil.getPortalClassLoader(), applicationContext);

		PortalBeanLocatorUtil.setBeanLocator(beanLocator);

		new SQLTransformerUtil(new SQLTransformer());

		com.liferay.portal.kernel.util.PropsUtil.setProps(new PropsImpl());

		PropsUtil.reload();

		SAXReaderImpl saxReader = new SAXReaderImpl();

		saxReader.setSecure(true);
		saxReader.setSecureXMLFactoryProvider(
			new SecureXMLFactoryProviderImpl());

		new UnsecureSAXReaderUtil().setSAXReader(saxReader);

		Log4JUtil.configureLog4J(ClassLoaderUtil.getPortalClassLoader());

		DBUpgrader dbUpgrader = new DBUpgrader();

		dbUpgrader.upgrade();

		((AbstractRefreshableApplicationContext)applicationContext).close();

		return;
	}
	protected static String[] getUpgradeProcessClassNames(String key) {

		// We would normally call PropsUtil#getArray(String) to return a String
		// array based on a comma delimited value. However, there is a bug with
		// Apache Commons Configuration where multi-line comma delimited values
		// do not interpolate properly (i.e. cannot be referenced by other
		// properties). The workaround to the bug is to escape commas with a
		// back slash. To get the configured String array, we have to call
		// PropsUtil#get(String) and manually split the value into a String
		// array instead of simply calling PropsUtil#getArray(String).

		return StringUtil.split(GetterUtil.getString(PropsUtil.get(key)));
	}

	private static void _checkPermissionAlgorithm() {
		long count = _getResourceCodesCount();

		if (count == 0) {
			return;
		}

		StringBundler sb = new StringBundler(8);

		sb.append("Permission conversion to algorithm 6 has not been ");
		sb.append("completed. Please complete the conversion prior to ");
		sb.append("starting the portal. The conversion process is ");
		sb.append("available in portal versions starting with ");
		sb.append(ReleaseInfo.RELEASE_5_2_3_BUILD_NUMBER);
		sb.append(" and prior to ");
		sb.append(ReleaseInfo.RELEASE_6_2_0_BUILD_NUMBER);
		sb.append(".");

		throw new IllegalStateException(sb.toString());
	}

	private static void _checkReleaseState(int state) {
		if (state == ReleaseConstants.STATE_GOOD) {
			return;
		}

		StringBundler sb = new StringBundler(6);

		sb.append("The database contains changes from a previous ");
		sb.append("upgrade attempt that failed. Please restore the old ");
		sb.append("database and file system and retry the upgrade. A ");
		sb.append("patch may be required if the upgrade failed due to a");
		sb.append(" bug or an unforeseen data permutation that resulted ");
		sb.append("from a corrupt database.");

		throw new IllegalStateException(sb.toString());
	}

	private static int _getBuildNumber() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DataAccess.getUpgradeOptimizedConnection();

			ps = con.prepareStatement(
				"select buildNumber from Release_ where releaseId = ?");

			ps.setLong(1, ReleaseConstants.DEFAULT_ID);

			rs = ps.executeQuery();

			if (rs.next()) {
				int buildNumber = rs.getInt("buildNumber");

				if (_log.isDebugEnabled()) {
					_log.debug("Build number " + buildNumber);
				}

				return buildNumber;
			}
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e.getMessage());
			}
		}
		finally {
			DataAccess.cleanUp(con, ps, rs);
		}

		throw new RuntimeException("Unable to retrieve the build number");
	}

	private static int _getReleaseState() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DataAccess.getUpgradeOptimizedConnection();

			ps = con.prepareStatement(
				"select state_ from Release_ where releaseId = ?");

			ps.setLong(1, ReleaseConstants.DEFAULT_ID);

			rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getInt("state_");
			}

			throw new IllegalArgumentException(
				"No Release exists with the primary key " +
					ReleaseConstants.DEFAULT_ID);
		}
		catch (SQLException sqle) {
			_log.error(sqle, sqle);

			throw new RuntimeException(sqle);
		} finally {
			DataAccess.cleanUp(con, ps, rs);
		}
	}

	private static long _getResourceCodesCount() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DataAccess.getUpgradeOptimizedConnection();

			ps = con.prepareStatement("select count(*) from ResourceCode");

			rs = ps.executeQuery();

			if (rs.next()) {
				int count = rs.getInt(1);

				return count;
			}

			return 0;
		}
		catch (Exception e) {
			return 0;
		}
		finally {
			DataAccess.cleanUp(con, ps, rs);
		}
	}

	private static List<String> _getMinimalSpringConfigs() {
		List<String> configLocations = new ArrayList<>();

		configLocations.add("META-INF/base-spring.xml");
		configLocations.add("META-INF/counter-spring.xml");
		configLocations.add("META-INF/minimal-upgrade-services-spring.xml");
		configLocations.add("META-INF/portlet-container-spring.xml");
		configLocations.add("META-INF/upgrade-spring.xml");
		configLocations.add("META-INF/util-spring.xml");

		return configLocations;
	}

	private static void _updateReleaseState(int state) {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = DataAccess.getUpgradeOptimizedConnection();

			ps = con.prepareStatement(
				"update Release_ set modifiedDate = ?, state_ = ? where " +
					"releaseId = ?");

			ps.setDate(1, new Date(System.currentTimeMillis()));
			ps.setInt(2, state);
			ps.setLong(3, ReleaseConstants.DEFAULT_ID);

			ps.executeUpdate();
		}
		catch (SQLException sqle) {
			_log.error(sqle, sqle);

			throw  new RuntimeException(sqle);
		} finally {
			DataAccess.cleanUp(con, ps);
		}
	}

	private static void _upgradeProcess(int buildNumber)
		throws UpgradeException {

		if (buildNumber == ReleaseInfo.getParentBuildNumber()) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"Skipping upgrade process from " + buildNumber +
						" to " + ReleaseInfo.getParentBuildNumber());
			}

			return;
		}

		String[] upgradeProcessClassNames = getUpgradeProcessClassNames(
			PropsKeys.UPGRADE_PROCESSES);

		if (upgradeProcessClassNames.length == 0) {
			upgradeProcessClassNames = getUpgradeProcessClassNames(
				PropsKeys.UPGRADE_PROCESSES + StringPool.PERIOD + buildNumber);

			if (upgradeProcessClassNames.length == 0) {
				if (_log.isInfoEnabled()) {
					_log.info(
						"Upgrading from " + buildNumber + " to " +
							ReleaseInfo.getParentBuildNumber() +
							" is not supported");
				}

				System.exit(0);
			}
		}

		List<UpgradeProcess> upgradeProcesses =
			UpgradeProcessUtil.initUpgradeProcesses(
				ClassLoaderUtil.getPortalClassLoader(),
				upgradeProcessClassNames);

		UpgradeProcessUtil.upgradeProcess(buildNumber, upgradeProcesses);
	}

	private static void _wireCacheKeyGenerators() {
		CacheKeyGeneratorUtil cacheKeyGeneratorUtil =
			new CacheKeyGeneratorUtil();

		Map<String, CacheKeyGenerator> cacheKeyGenerators = new HashMap<>();

		cacheKeyGenerators.put(
			"com.liferay.portal.kernel.dao.orm.FinderCache#BaseModel",
			new HashCodeCacheKeyGenerator());

		cacheKeyGeneratorUtil.setCacheKeyGenerators(cacheKeyGenerators);

		cacheKeyGeneratorUtil.setDefaultCacheKeyGenerator(
			new SimpleCacheKeyGenerator());
	}

	public static void _wireDLUtils() {
		new DLUtil().setDL(new DLImpl());
	}

	private static void _wireSaxReaders() {
		SAXReaderImpl saxReader = new SAXReaderImpl();

		saxReader.setSecure(true);
		saxReader.setSecureXMLFactoryProvider(
			new SecureXMLFactoryProviderImpl());

		UnsecureSAXReaderUtil unsecureSAXReaderUtil =
			new UnsecureSAXReaderUtil();

		unsecureSAXReaderUtil.setSAXReader(saxReader);

		SecureXMLFactoryProviderUtil secureXMLFactoryProviderUtil =
			new SecureXMLFactoryProviderUtil();

		secureXMLFactoryProviderUtil.setSecureXMLFactoryProvider(
			new SecureXMLFactoryProviderImpl());

		SAXReaderUtil saxReaderUtil = new SAXReaderUtil();

		saxReaderUtil.setSAXReader(saxReader);
	}

	private static void _startMinimalServices() {
		_wireCacheKeyGenerators();

		InitUtil.initWithSpring(_getMinimalSpringConfigs(), true);

		_wireDLUtils();

		_wireSaxReaders();

		// InitUtil.initWithSpring(true);

		PropsValues.SPRING_HIBERNATE_SESSION_DELEGATED = false;
	}

	private static final Log _log = LogFactoryUtil.getLog(DBUpgrader.class);

}