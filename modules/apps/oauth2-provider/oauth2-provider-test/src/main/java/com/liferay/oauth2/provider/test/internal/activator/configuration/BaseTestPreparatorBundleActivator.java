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
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.Configuration;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import javax.ws.rs.core.Application;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Dictionary;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Carlos Sierra Andrés
 */
public abstract class BaseTestPreparatorBundleActivator implements BundleActivator {

	protected BundleContext _bundleContext;

	public OAuth2Application createOAuth2Application(
			long companyId, User user, String clientId)
		throws PortalException {

		return createOAuth2Application(
			companyId, user, clientId,
			Arrays.asList(
				GrantType.CLIENT_CREDENTIALS,
				GrantType.RESOURCE_OWNER_PASSWORD),
			Arrays.asList("everything", "everything.readonly"));
	}

	public User addUser(Company company) throws Exception {
		User user = UserTestUtil.addUser(company);

		autoCloseables.add(
			() -> UserLocalServiceUtil.deleteUser(user.getUserId()));

		return user;
	}

	public User addAdminUser(Company company) throws Exception {
		User user = UserTestUtil.addCompanyAdminUser(company);

		autoCloseables.add(
			() -> UserLocalServiceUtil.deleteUser(user.getUserId()));

		return user;
	}

	public OAuth2Application createOAuth2Application(
			long companyId, User user, String clientId,
			List<GrantType> availableGrants, List<String> availableScopes)
		throws PortalException {

		return createOAuth2Application(
			companyId, user, clientId, "oauthTestApplicationSecret",
			availableGrants, availableScopes);
	}

	public OAuth2Application createOAuth2Application(
			long companyId, User user, String clientId, String clientSecret,
			List<GrantType> availableGrants, List<String> availableScopes)
		throws PortalException {

		ServiceReference<OAuth2ApplicationLocalService> serviceReference =
			_bundleContext.getServiceReference(
				OAuth2ApplicationLocalService.class);

		_oAuth2ApplicationLocalService = _bundleContext.getService(
			serviceReference);

		try {
			OAuth2Application oAuth2Application =
				_oAuth2ApplicationLocalService.addOAuth2Application(
					companyId, user.getUserId(), user.getLogin(),
					availableGrants, clientId, 0, clientSecret, 
					"test oauth application", 
					Collections.singletonList("token_introspection"),
					"http://localhost:8080", 0, "test application",
					"http://localhost:8080",
					Collections.singletonList("http://localhost:8080"),
					availableScopes, new ServiceContext());

			autoCloseables.add(
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

		autoCloseables.add(
			() -> CompanyLocalServiceUtil.deleteCompany(
				company.getCompanyId()));

		return company;
	}

	public ServiceRegistration<Application> registerJaxRsApplication(
			Application application, String path,
			Dictionary<String, Object> properties) {

		if (properties == null || properties.isEmpty()) {
			properties = new HashMapDictionary<>();
		}

		properties.put("oauth2.test.application", "true");
		properties.put(
			"osgi.jaxrs.extension.select", "(liferay.extension=OAuth2)");
		properties.put("osgi.jaxrs.application.base", "/oauth2-test/" + path);

		ServiceRegistration<Application> serviceRegistration =
			_bundleContext.registerService(
				Application.class, application, properties);

		autoCloseables.add(serviceRegistration::unregister);

		return serviceRegistration;
	}

	protected OAuth2Application createOAuth2Application(
			long companyId, User user, String clientId,
			List<String> availableScopes)
		throws PortalException {

		return createOAuth2Application(
			companyId, user, clientId,
			Arrays.asList(
				GrantType.CLIENT_CREDENTIALS,
				GrantType.RESOURCE_OWNER_PASSWORD),
			availableScopes);
	}

	protected ServiceRegistration<PrefixHandlerFactory> registerPrefixHandler(
		PrefixHandler prefixHandler, Dictionary<String, Object> properties) {

		ServiceRegistration<PrefixHandlerFactory> serviceRegistration =
			_bundleContext.registerService(
				PrefixHandlerFactory.class, key -> prefixHandler,
				properties);

		autoCloseables.add(serviceRegistration::unregister);

		return serviceRegistration;
	}

	protected ServiceRegistration<ScopeMapper> registerScopeMapper(
		ScopeMapper scopeMapper, Dictionary<String, Object> properties) {

		ServiceRegistration<ScopeMapper> serviceRegistration =
			_bundleContext.registerService(
				ScopeMapper.class, scopeMapper, properties);

		autoCloseables.add(serviceRegistration::unregister);

		return serviceRegistration;
	}

	protected Configuration createConfigurationFactory(
		String factoryPid, Dictionary<String, Object> properties) {

		Configuration factoryConfiguration =
			TestUtils.createFactoryConfiguration(
				_bundleContext, factoryPid, properties);

		autoCloseables.add(factoryConfiguration::delete);

		return factoryConfiguration;
	}

	@Override
	public void start(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		autoCloseables = new ArrayList<>();

		try {
			prepareTest();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected abstract void prepareTest() throws Exception;

	@Override
	public void stop(BundleContext bundleContext) {
		ListIterator<AutoCloseable> listIterator = autoCloseables.listIterator(
			autoCloseables.size());

		while (listIterator.hasPrevious()) {
			AutoCloseable previous = listIterator.previous();

			try {
				previous.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected ArrayList<AutoCloseable> autoCloseables;
	private OAuth2ApplicationLocalService _oAuth2ApplicationLocalService;

	protected Runnable updateOrCreateConfiguration(
			String pid, Dictionary<String, ?> properties) {

		Configuration configuration = null;

		try {
			configuration = TestUtils.configurationExists(_bundleContext, pid);
		}
		catch (Exception e) {
			e.printStackTrace();

			return () -> {};
		}

		if (configuration == null) {
			TestUtils.updateConfiguration(_bundleContext, pid, properties);

			return () -> TestUtils.deleteConfiguration(_bundleContext, pid);
		}
		else {
			Dictionary<String, Object> oldProperties =
				configuration.getProperties();

			TestUtils.updateConfiguration(_bundleContext, pid, properties);

			return () -> TestUtils.updateConfiguration(
				_bundleContext, pid, oldProperties);
		}
	}

	protected void waitForFramework(Runnable runnable)
		throws InvalidSyntaxException {

		CountDownLatch countDownLatch = new CountDownLatch(52);

		String filter =
			"(&(component.name=com.liferay.oauth2.provider.rest.internal." +
				"endpoint.OAuth2EndpointApplication)(objectClass=org.apache." +
					"aries.jax.rs.whiteboard.internal.cxf." +
						"CxfJaxrsServiceRegistrator))";

		ServiceTracker<?, ?> tracker =
			new ServiceTracker<>(
				_bundleContext,
				_bundleContext.createFilter(filter),
				new ServiceTrackerCustomizer<Object, Object>() {
					@Override
					public Object addingService(
						ServiceReference<Object> reference) {

						countDownLatch.countDown();

						return reference;
					}

					@Override
					public void modifiedService(
						ServiceReference<Object> reference,
						Object service) {

					}

					@Override
					public void removedService(
						ServiceReference<Object> reference,
						Object service) {

						countDownLatch.countDown();
					}
				});

		tracker.open();

		runnable.run();

		try {
			countDownLatch.await(30, TimeUnit.SECONDS);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		finally {
			tracker.close();
		}
	}
}
