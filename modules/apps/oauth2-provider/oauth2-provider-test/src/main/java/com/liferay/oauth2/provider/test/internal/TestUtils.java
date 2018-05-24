/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.liferay.oauth2.provider.test.internal;

import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.cm.ManagedServiceFactory;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class TestUtils {

	public static boolean isIncluded(
		Dictionary<String, ?> properties1, Dictionary<String, ?> properties2) {

		if (properties1.size() > properties2.size()) {
			return false;
		}

		Enumeration<String> keys = properties1.keys();

		while (keys.hasMoreElements()) {
			String key = keys.nextElement();

			if (!Objects.deepEquals(properties1.get(key), properties2.get(key))) {
				return false;
			}
		}

		return true;
	}

	public static Configuration createFactoryConfiguration(
		BundleContext bundleContext, String factoryPid,
		Dictionary<String, Object> properties) {

		CountDownLatch countDownLatch = new CountDownLatch(1);

		Dictionary<String, Object> registrationProperties =
			new HashMapDictionary<>();

		registrationProperties.put(Constants.SERVICE_PID, factoryPid);

		ServiceRegistration<ManagedServiceFactory> serviceRegistration =
			bundleContext.registerService(
				ManagedServiceFactory.class,
				new ManagedServiceFactory() {
					@Override
					public String getName() {
						return
							"Test managedservicefactory for pid " + factoryPid;
					}

					@Override
					public void updated(
							String pid, Dictionary incomingProperties) {

						if (Validator.isNull(incomingProperties)) {
							return;
						}

						if (isIncluded(properties, incomingProperties)) {
							countDownLatch.countDown();
						}
					}

					@Override
					public void deleted(String pid) {

					}
				},
				registrationProperties);

		try {
			ServiceReference<ConfigurationAdmin> serviceReference =
				bundleContext.getServiceReference(ConfigurationAdmin.class);

			ConfigurationAdmin configurationAdmin =
				bundleContext.getService(serviceReference);

			Configuration configuration = null;

			try {
				configuration =
					configurationAdmin.createFactoryConfiguration(
						factoryPid, StringPool.QUESTION);

				configuration.update(properties);

				countDownLatch.await(10, TimeUnit.SECONDS);

				return configuration;
			}
			catch (IOException ioe) {
				throw new RuntimeException(ioe);
			}
			catch (InterruptedException ie) {
				try {
					configuration.delete();
				}
				catch (IOException ioe) {
					throw new RuntimeException(ioe);
				}

				throw new RuntimeException(ie);
			}
			finally {
				bundleContext.ungetService(serviceReference);
			}
		}
		finally {
			serviceRegistration.unregister();
		}
	}

	public static Configuration configurationExists(
			BundleContext bundleContext, String pid)
		throws IOException, InvalidSyntaxException {

		ServiceReference<ConfigurationAdmin> serviceReference =
			bundleContext.getServiceReference(ConfigurationAdmin.class);

		ConfigurationAdmin configurationAdmin =
			bundleContext.getService(serviceReference);

		try {
			Configuration[] configurations =
				configurationAdmin.listConfigurations(
					"(" + Constants.SERVICE_PID + "=" + pid + ")");

			if (ArrayUtil.isEmpty(configurations)) {
				return null;
			}

			return configurations[0];
		}
		finally {
			bundleContext.ungetService(serviceReference);
		}
	}

	public static Configuration updateConfiguration(
		BundleContext bundleContext, String pid,
		Dictionary<String, ?> properties) {

		CountDownLatch countDownLatch = new CountDownLatch(1);

		Hashtable<String, Object> registrationProperties = new Hashtable<>();

		registrationProperties.put(Constants.SERVICE_PID, pid);

		ServiceRegistration<ManagedService> serviceRegistration =
			bundleContext.registerService(
				ManagedService.class, incomingProperties -> {
					if (Validator.isNull(incomingProperties)) {
						return;
					}

					if (isIncluded(properties, incomingProperties)) {
						countDownLatch.countDown();
					}
				},
				registrationProperties);
		try {
			ServiceReference<ConfigurationAdmin> serviceReference =
				bundleContext.getServiceReference(ConfigurationAdmin.class);

			ConfigurationAdmin configurationAdmin =
				bundleContext.getService(serviceReference);

			Configuration configuration = null;

			try {
				configuration = configurationAdmin.getConfiguration(
					pid, StringPool.QUESTION);

				configuration.update(properties);

				countDownLatch.await(10, TimeUnit.SECONDS);

				return configuration;
			}
			catch (IOException ioe1) {
				throw new RuntimeException(ioe1);
			}
			catch (InterruptedException ie) {
				try {
					configuration.delete();
				}
				catch (IOException ioe2) {
					throw new RuntimeException(ioe2);
				}

				throw new RuntimeException(ie);
			}
			finally {
				bundleContext.ungetService(serviceReference);
			}
		}
		finally {
			serviceRegistration.unregister();
		}
	}

	public static void deleteConfiguration(
		BundleContext bundleContext, String pid) {

		CountDownLatch countDownLatch = new CountDownLatch(1);

		Hashtable<String, Object> registrationProperties = new Hashtable<>();

		registrationProperties.put(Constants.SERVICE_PID, pid);

		ServiceRegistration<ManagedService> serviceRegistration =
			bundleContext.registerService(
				ManagedService.class,
				incomingProperties -> {
					if (Validator.isNull(incomingProperties)) {
						countDownLatch.countDown();
					}
				},
				registrationProperties);

		try {
			ServiceReference<ConfigurationAdmin> serviceReference =
				bundleContext.getServiceReference(ConfigurationAdmin.class);

			ConfigurationAdmin configurationAdmin =
				bundleContext.getService(serviceReference);

			try {
				Configuration configuration =
					configurationAdmin.getConfiguration(
						pid, StringPool.QUESTION);

				configuration.delete();

				countDownLatch.await(30, TimeUnit.SECONDS);
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
			finally {
				bundleContext.ungetService(serviceReference);
			}
		}
		finally {
			serviceRegistration.unregister();
		}
	}
}
