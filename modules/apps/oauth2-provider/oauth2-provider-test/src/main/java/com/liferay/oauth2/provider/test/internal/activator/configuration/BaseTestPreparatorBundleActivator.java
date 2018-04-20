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
import com.liferay.oauth2.provider.scope.spi.prefix.handler.PrefixHandler;
import com.liferay.oauth2.provider.scope.spi.prefix.handler.PrefixHandlerFactory;
import com.liferay.oauth2.provider.scope.spi.scope.mapper.ScopeMapper;
import com.liferay.oauth2.provider.service.OAuth2ApplicationLocalService;
import com.liferay.oauth2.provider.test.internal.TestUtils;
import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.core.Application;

import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
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
public abstract class BaseTestPreparatorBundleActivator implements BundleActivator {

	private BundleContext _bundleContext;

	public OAuth2Application createOauth2Application(
		final long companyId, User user, String clientId)
		throws PortalException {

		return createOauth2Application(
			companyId, user, clientId,
			Arrays.asList(
				GrantType.CLIENT_CREDENTIALS,
				GrantType.RESOURCE_OWNER_PASSWORD),
			Arrays.asList("everything", "everything.readonly"));
	}

	public User addUser(Company company) throws Exception {
		User user = UserTestUtil.addUser(company);

		_autoCloseables.add(
			() -> UserLocalServiceUtil.deleteUser(user.getUserId()));

		return user;
	}

	public User addAdminUser(Company company) throws Exception {
		User user = UserTestUtil.addCompanyAdminUser(company);

		_autoCloseables.add(
			() -> UserLocalServiceUtil.deleteUser(user.getUserId()));

		return user;
	}

	public OAuth2Application createOauth2Application(
		final long companyId, User user, String clientId,
		List<GrantType> availableCredentials, List<String> availableScopes)
		throws PortalException {

		ServiceReference<OAuth2ApplicationLocalService> serviceReference =
			_bundleContext.getServiceReference(
				OAuth2ApplicationLocalService.class);

		_oAuth2ApplicationLocalService = _bundleContext.getService(
			serviceReference);

		final OAuth2Application oAuth2Application;

		try {
			oAuth2Application =
				_oAuth2ApplicationLocalService.addOAuth2Application(
					companyId, user.getUserId(), user.getLogin(),
					availableCredentials, clientId, 0,
					"oauthTestApplicationSecret", "test oauth application",
					Collections.singletonList("token-introspection"),
					"http://localhost:8080", 0, "test application",
					"http://localhost:8080",
					Collections.singletonList("http://localhost:8080"),
					availableScopes, new ServiceContext());

			_autoCloseables.add(
				() -> _oAuth2ApplicationLocalService.deleteOAuth2Application(
					oAuth2Application.getOAuth2ApplicationId()));

			return oAuth2Application;
		}
		finally {
			_bundleContext.ungetService(serviceReference);
		}

	}

	public Company createCompany(String hostName) throws PortalException {
		Company company = CompanyLocalServiceUtil.addCompany(
			hostName, hostName + ".xyz", hostName + ".xyz", false, 0, true);

		_autoCloseables.add(
			() -> CompanyLocalServiceUtil.deleteCompany(
				company.getCompanyId()));

		return company;
	}

	public ServiceRegistration<Application> registerJaxRsApplication(
		Application application, Dictionary<String, Object> properties)
		throws InterruptedException {

		if (properties == null || properties.isEmpty()) {
			properties = new Hashtable<>();
		}

		properties.put("oauth2.test.application", true);

		ServiceRegistration<Application> serviceRegistration =
			_bundleContext.registerService(
				Application.class, application, properties);

		ServiceTracker<Bus, Bus> serviceTracker =
			ServiceTrackerFactory.open(
				_bundleContext,
				"(&(objectClass=" + Bus.class.getName() + ")(" +
				HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_PATH +
				"=/oauth2-test))");

		Bus bus;

		try {
			bus = serviceTracker.waitForService(10000L);
		}
		finally {
			serviceTracker.close();
		}

		serviceTracker.close();

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
			throw new IllegalStateException(
				"Endpoint was not registered within 10 seconds");
		}

		_autoCloseables.add(serviceRegistration::unregister);

		return serviceRegistration;
	}

	public ServiceRegistration<Object> registerJsonWebService(
		String name, String path, Object service,
		Dictionary<String, Object> properties) {

		if (properties == null || properties.isEmpty()) {
			properties = new Hashtable<>();
		}

		properties.put("json.web.service.context.name", name);
		properties.put("json.web.service.context.path", path);

		ServiceRegistration<Object> serviceRegistration =
			_bundleContext.registerService(
				Object.class, service, properties);

		_autoCloseables.add(serviceRegistration::unregister);

		return serviceRegistration;
	}

	protected OAuth2Application createOauth2Application(
		long companyId, User user, String clientId,
		List<String> availableScopes) throws PortalException {

		return createOauth2Application(
			companyId, user, clientId,
			Arrays.asList(
				GrantType.CLIENT_CREDENTIALS,
				GrantType.RESOURCE_OWNER_PASSWORD),
			availableScopes);
	}

	protected ServiceRegistration<PrefixHandlerFactory> registerPrefixHandler(
		PrefixHandler prefixHandler,
		Hashtable<String, Object> prefixHandlerProperties) {

		ServiceRegistration<PrefixHandlerFactory> serviceRegistration =
			_bundleContext.registerService(
				PrefixHandlerFactory.class, __ -> prefixHandler,
				prefixHandlerProperties);

		_autoCloseables.add(serviceRegistration::unregister);

		return serviceRegistration;
	}

	protected ServiceRegistration<ScopeMapper> registerScopeMapper(
		ScopeMapper scopeMapper,
		Hashtable<String, Object> scopeMapperProperties) {

		ServiceRegistration<ScopeMapper> serviceRegistration =
			_bundleContext.registerService(
				ScopeMapper.class, scopeMapper, scopeMapperProperties);

		_autoCloseables.add(serviceRegistration::unregister);

		return serviceRegistration;
	}

	protected Configuration createConfigurationFactory(
		String factoryPid, Dictionary<String, Object> properties) {

		Configuration factoryConfiguration =
			TestUtils.createFactoryConfiguration(
				_bundleContext, factoryPid, properties);

		_autoCloseables.add(factoryConfiguration::delete);

		return factoryConfiguration;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		_bundleContext = bundleContext;

		_autoCloseables = new ArrayList<>();

		ServiceReference<ConfigurationAdmin> serviceReference =
			bundleContext.getServiceReference(ConfigurationAdmin.class);

		try {
			Dictionary<String, Object> properties = new Hashtable<>();

			properties.put("contextPath", "/oauth2-test");
			properties.put(
				"authVerifierProperties",
				"auth.verifier.OAuth2RestAuthVerifier.urls.includes=*");

			_cxfConfiguration = TestUtils.createFactoryConfiguration(
				bundleContext,
				"com.liferay.portal.remote.cxf.common.configuration." +
					"CXFEndpointPublisherConfiguration",
				properties);

			properties = new Hashtable<>();

			properties.put("contextPaths", new String[] {"/oauth2-test"});
			properties.put(
				"jaxRsApplicationFilterStrings",
				new String[] {"(oauth2.test.application=true)"});
			properties.put(
				"jaxRsProviderFilterStrings",
				new String[] {"(liferay.extension=OAuth2)"});

			_restConfiguration = TestUtils.createFactoryConfiguration(
				bundleContext,
				"com.liferay.portal.remote.rest.extender.configuration." +
				   "RestExtenderConfiguration",
				properties);

			prepareTest();
		}
		catch (Exception e) {
			e.printStackTrace();

			bundleContext.ungetService(serviceReference);

			_cleanUp(bundleContext);
		}
	}

	protected abstract void prepareTest() throws Exception;

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		_cleanUp(bundleContext);
	}

	private void _cleanUp(BundleContext bundleContext) throws Exception {
		final CountDownLatch countDownLatch = new CountDownLatch(1);

		ListIterator<AutoCloseable> listIterator = _autoCloseables.listIterator(
			_autoCloseables.size());

		while (listIterator.hasPrevious()) {
			AutoCloseable previous = listIterator.previous();

			try {
				previous.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

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
			_restConfiguration.delete();
		}
		catch (Exception e) {
		}

		try {
			_cxfConfiguration.delete();
		}
		catch (Exception e) {
		}

		if (!countDownLatch.await(10, TimeUnit.SECONDS)) {
			throw new TimeoutException("Service unregister waiting timeout");
		}
	}

	private ArrayList<AutoCloseable> _autoCloseables;
	private Configuration _cxfConfiguration;
	private OAuth2ApplicationLocalService _oAuth2ApplicationLocalService;
	private Configuration _restConfiguration;

}