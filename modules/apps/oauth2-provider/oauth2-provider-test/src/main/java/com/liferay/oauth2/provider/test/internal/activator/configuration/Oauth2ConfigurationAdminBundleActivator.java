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

package com.liferay.oauth2.provider.test.internal.activator.configuration;

import com.liferay.oauth2.provider.constants.GrantType;
import com.liferay.oauth2.provider.model.OAuth2Application;
import com.liferay.oauth2.provider.service.OAuth2ApplicationLocalService;
import com.liferay.oauth2.provider.test.internal.TestAnnotatedApplication;
import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.core.Application;

import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.endpoint.ServerRegistry;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.http.context.ServletContextHelper;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Carlos Sierra Andrés
 */
public class Oauth2ConfigurationAdminBundleActivator
	implements BundleActivator {

	private OAuth2Application _oAuth2ApplicationBefore;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		ServiceReference<ConfigurationAdmin> serviceReference =
			bundleContext.getServiceReference(ConfigurationAdmin.class);

		ServiceReference<OAuth2ApplicationLocalService>
			auth2ApplicationLocalServiceReference =
				bundleContext.getServiceReference(
					OAuth2ApplicationLocalService.class);

		try {
			ConfigurationAdmin configurationAdmin = bundleContext.getService(
				serviceReference);

			_oAuth2ApplicationLocalService = bundleContext.getService(
				auth2ApplicationLocalServiceReference);

			_cxfConfiguration = configurationAdmin.createFactoryConfiguration(
				"com.liferay.portal.remote.cxf.common.configuration." +
					"CXFEndpointPublisherConfiguration",
				null);

			Dictionary<String, Object> properties = new Hashtable<>();

			properties.put("contextPath", "/oauth2-test");
			properties.put(
				"authVerifierProperties",
				"auth.verifier.OAuth2RestAuthVerifier.urls.includes=*");

			_cxfConfiguration.update(properties);

			_restConfiguration = configurationAdmin.createFactoryConfiguration(
				"com.liferay.portal.remote.rest.extender.configuration." +
					"RestExtenderConfiguration",
				null);

			properties = new Hashtable<>();

			properties.put("contextPaths", new String[] {"/oauth2-test"});
			properties.put(
				"jaxRsApplicationFilterStrings",
				new String[] {"(oauth2.test.application=true)"});
			properties.put(
				"jaxRsProviderFilterStrings",
				new String[] {"(liferay.extension=OAuth2)"});

			_restConfiguration.update(properties);

			long defaultCompanyId = PortalUtil.getDefaultCompanyId();

			User user = UserTestUtil.getAdminUser(defaultCompanyId);

			_oAuth2ApplicationBefore =
				_oAuth2ApplicationLocalService.addOAuth2Application(
					defaultCompanyId, user.getUserId(), user.getLogin(),
					Collections.singletonList(GrantType.CLIENT_CREDENTIALS),
					"oauthTestApplicationBefore", 0,
					"oauthTestApplicationSecret", "test oauth application",
					Collections.singletonList("token-introspection"),
					"http://localhost:8080", 0, "test application",
					"http://localhost:8080",
					Collections.singletonList("http://localhost:8080"),
					Arrays.asList("everything", "everything.readonly"),
					new ServiceContext());

			properties = new Hashtable<>();

			properties.put("oauth2.test.application", true);
			properties.put("oauth2.scopechecker.type", "annotations");

			_serviceRegistration = bundleContext.registerService(
				Application.class, new TestAnnotatedApplication(), properties);

			ServiceTracker<Bus, Bus> serviceTracker =
				ServiceTrackerFactory.open(
					bundleContext,
					"(&(objectClass=" + Bus.class.getName() + ")(" +
						HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_PATH +
							"=/oauth2-test))");

			Bus bus = serviceTracker.waitForService(10000L);

			if (bus == null) {
				throw new IllegalStateException(
					"Bus was not registered within 10 seconds");
			}

			ServerRegistry serverRegistry = bus.getExtension(
				ServerRegistry.class);

			List<Server> servers = null;

			for (int i = 0; i <= 100; i++) {
				servers = serverRegistry.getServers();

				if (!servers.isEmpty()) {
					break;
				}

				Thread.sleep(100);
			}

			if (servers.isEmpty()) {
				_cleanUp(bundleContext);

				throw new IllegalStateException(
					"Endpoint was not registered within 10 seconds");
			}

			_oAuth2ApplicationAfter =
				_oAuth2ApplicationLocalService.addOAuth2Application(
					defaultCompanyId, user.getUserId(), user.getLogin(),
					Collections.singletonList(GrantType.CLIENT_CREDENTIALS),
					"oauthTestApplicationAfter", 0,
					"oauthTestApplicationSecret", "test oauth application",
					Collections.singletonList("token-introspection"),
					"http://localhost:8080", 0, "test application",
					"http://localhost:8080",
					Collections.singletonList("http://localhost:8080"),
					Arrays.asList("everything", "everything.readonly"),
					new ServiceContext());
		}
		catch (Exception e) {
			e.printStackTrace();

			bundleContext.ungetService(serviceReference);

			bundleContext.ungetService(auth2ApplicationLocalServiceReference);
		}
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		_cleanUp(bundleContext);
	}

	private void _cleanUp(BundleContext bundleContext) throws Exception {
		final CountDownLatch countDownLatch = new CountDownLatch(1);

		ServiceTracker<ServletContextHelper, ServletContextHelper>
			serviceTracker =
				new ServiceTracker<ServletContextHelper, ServletContextHelper>(
					bundleContext, ServletContextHelper.class, null) {

					@Override
					public void removedService(
						ServiceReference<ServletContextHelper> reference,
						ServletContextHelper service) {

						Object contextName = reference.getProperty(
							HttpWhiteboardConstants.
								HTTP_WHITEBOARD_CONTEXT_NAME);

						if (Objects.equals(contextName, "oauth2-test")) {
							countDownLatch.countDown();

							close();
						}
					}

				};

		serviceTracker.open();

		try {
			_serviceRegistration.unregister();
		}
		catch (Exception e) {
		}

		try {
			_restConfiguration.delete();
		}
		catch (Exception e) {
		}

		try {
			_cxfConfiguration.delete();
		}
		catch (Exception e) {
		}

		try {
			_oAuth2ApplicationLocalService.deleteOAuth2Application(
				_oAuth2ApplicationBefore.getOAuth2ApplicationId());
		}
		catch (Exception e) {
		}
		try {
			_oAuth2ApplicationLocalService.deleteOAuth2Application(
				_oAuth2ApplicationAfter.getOAuth2ApplicationId());
		}
		catch (Exception e) {
		}

		if (!countDownLatch.await(10, TimeUnit.SECONDS)) {
			throw new TimeoutException("Service unregister waiting timeout");
		}
	}

	private Configuration _cxfConfiguration;
	private OAuth2Application _oAuth2ApplicationAfter;
	private OAuth2ApplicationLocalService _oAuth2ApplicationLocalService;
	private Configuration _restConfiguration;
	private ServiceRegistration<Application> _serviceRegistration;

}