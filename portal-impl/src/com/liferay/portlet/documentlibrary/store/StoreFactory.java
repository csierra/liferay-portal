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

package com.liferay.portlet.documentlibrary.store;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ClassLoaderUtil;
import com.liferay.portal.kernel.util.ClassUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.spring.aop.MethodInterceptorInvocationHandler;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.registry.Filter;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceTracker;
import com.liferay.registry.ServiceTrackerCustomizer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * @author Brian Wing Shun Chan
 * @author Shuyang Zhou
 * @author Manuel de la Peña
 */
public class StoreFactory {

	public static StoreFactory getInstance() {
		if (_instance == null) {
			_instance = new StoreFactory();
		}

		return _instance;
	}

	public void checkProperties() {
		if (_warned) {
			return;
		}

		String dlHookImpl = PropsUtil.get("dl.hook.impl");

		if (Validator.isNull(dlHookImpl)) {
			_warned = true;

			return;
		}

		boolean found = false;

		for (String key : _stores.keySet()) {
			Store storeEntry = getStoreInstance(key);

			String className = storeEntry.getClass().getName();

			if (dlHookImpl.equals(className)) {
				PropsValues.DL_STORE_IMPL = className;

				found = true;

				break;
			}
		}

		if (!found) {
			PropsValues.DL_STORE_IMPL = dlHookImpl;
		}

		if (_log.isWarnEnabled()) {
			StringBundler sb = new StringBundler(13);

			sb.append("Liferay is configured with the legacy ");
			sb.append("property \"dl.hook.impl=");
			sb.append(dlHookImpl);
			sb.append("\" ");
			sb.append("in portal-ext.properties. Please reconfigure ");
			sb.append("to use the new property \"");
			sb.append(PropsKeys.DL_STORE_IMPL);
			sb.append("\". Liferay will ");
			sb.append("attempt to temporarily set \"");
			sb.append(PropsKeys.DL_STORE_IMPL);
			sb.append("=");
			sb.append(PropsValues.DL_STORE_IMPL);
			sb.append("\".");

			_log.warn(sb.toString());
		}

		_warned = true;
	}

	public Store getStoreInstance() {
		if (_store == null) {
			checkProperties();

			if (_log.isDebugEnabled()) {
				_log.debug("Instantiate " + PropsValues.DL_STORE_IMPL);
			}

			try {
				_store = _getStoreInstance();
			}
			catch (Exception e) {
				_log.error(e, e);
			}
		}

		if ((_store != null) && _log.isDebugEnabled()) {
			Class<?> clazz = _store.getClass();

			_log.debug("Return " + clazz.getName());
		}

		return _store;
	}

	public Store getStoreInstance(String key) {
		return _stores.get(key);
	}

	public String[] getStoreTypes() {
		Set<String> keySet = _stores.keySet();

		String[] storesTypes = new String[keySet.size()];

		int i = 0;

		for (String key : keySet) {
			Store store = getStoreInstance(key);

			storesTypes[i] = store.getType();

			i++;
		}

		return storesTypes;
	}

	public void setStoreInstance(Store store) {
		if (_log.isDebugEnabled()) {
			_log.debug("Set " + ClassUtil.getClassName(store));
		}

		_store = store;
	}

	private StoreFactory() {
		Registry registry = RegistryUtil.getRegistry();

		Filter filter = registry.getFilter(
			"(&(store.type=*)(objectClass=" + Store.class.getName() + "))");

		_serviceTracker = registry.trackServices(
			filter, new StoreServiceTrackerCustomizer());

		_serviceTracker.open();
	}

	private Store _getStoreInstance() throws Exception {
		if (_store == null) {
			Set<String> keySet = _stores.keySet();

			for (String key : keySet) {
				if (key.endsWith("FileSystemStore")) {
					_store = getStoreInstance(key);

					break;
				}
			}

			if (_store == null) {
				return _NULL_STORE;
			}
		}

		String storeType = _store.getType();

		if (!(storeType.endsWith("DBStore"))) {
			return _store;
		}

		DB db = DBFactoryUtil.getDB();

		String dbType = db.getType();

		if (dbType.equals(DB.TYPE_POSTGRESQL)) {
			ClassLoader classLoader = ClassLoaderUtil.getPortalClassLoader();

			MethodInterceptor transactionAdviceMethodInterceptor =
				(MethodInterceptor)PortalBeanLocatorUtil.locate(
					"transactionAdvice");

			MethodInterceptor tempFileMethodInterceptor =
				new TempFileMethodInterceptor();

			List<MethodInterceptor> methodInterceptors = Arrays.asList(
				transactionAdviceMethodInterceptor, tempFileMethodInterceptor);

			_store = (Store)ProxyUtil.newProxyInstance(
				classLoader, new Class<?>[] {Store.class},
				new MethodInterceptorInvocationHandler(
					_store, methodInterceptors));
		}

		return _store;
	}

	private static final Log _log = LogFactoryUtil.getLog(StoreFactory.class);

	private static final Store _NULL_STORE = null;
	private static Store _store;
	private static Map<String, Store> _stores = new ConcurrentHashMap<>();
	private static StoreFactory _instance;
	private static boolean _warned;

	private final ServiceTracker<Store, Store> _serviceTracker;

	private class StoreServiceTrackerCustomizer
		implements ServiceTrackerCustomizer<Store, Store> {

		@Override
		public Store addingService(ServiceReference<Store> serviceReference) {
			Registry registry = RegistryUtil.getRegistry();

			Store store = registry.getService(serviceReference);

			if (store == null) {
				return null;
			}

			String storeType = store.getType();

			if (_store == null || storeType.equals(PropsValues.DL_STORE_IMPL)) {
				_store = store;
			}

			_stores.put(storeType, store);

			return store;
		}

		@Override
		public void modifiedService(
			ServiceReference<Store> serviceReference, Store service) {

			if (service.getType().equals(_store.getType())) {
				_store = service;
			}
		}

		@Override
		public void removedService(
			ServiceReference<Store> serviceReference, Store service) {

			_stores.remove(service.getType());

			if (_store != null && _store.getType().equals(service.getType())) {
				_store = null;
			}
		}
	}

}