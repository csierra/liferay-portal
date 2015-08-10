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

package com.liferay.portal.upgrade.internal.core;

import com.liferay.portal.bean.BeanLocatorImpl;
import com.liferay.portal.kernel.bean.BeanLocator;
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.util.ClassLoaderUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.model.Release;

import com.liferay.portal.spring.context.ArrayApplicationContext;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.util.PropsValues;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * @author Migue Pastor
 */
@Component(
	immediate = true, property = "module.service.lifecycle=spring.initialized"
)
public class PortalCoreServices implements ModuleServiceLifecycle {

	@Activate
	protected void activate(BundleContext bundleContext) {
		System.out.println("Starting Portal core services ....");

		_configurableApplicationContext = wirePortalServices();

		_applicationContextServicePublisher =
			new ApplicationContextServicePublisher(
				_configurableApplicationContext, bundleContext);

		System.out.println("Registering Portal's beans as OSGi services ....");

		_applicationContextServicePublisher.register();

		System.out.println("Finished!");
	}

	@Deactivate
	protected void deactivate() {
		_applicationContextServicePublisher.unregister();

		_configurableApplicationContext.close();
	}

	@Reference(target = "(&(component.name=portal)(release.build.number=7.0.0.0))")
	protected void setRelease(Release release) {
	}

	protected ConfigurableApplicationContext wirePortalServices() {
		List<String> configLocations = ListUtil.fromArray(
			PropsUtil.getArray(
				com.liferay.portal.kernel.util.PropsKeys.SPRING_CONFIGS));

		ConfigurableApplicationContext configurableApplicationContext =
			new ArrayApplicationContext(
				PropsValues.SPRING_INFRASTRUCTURE_CONFIGS);

		configurableApplicationContext = new ClassPathXmlApplicationContext(
			configLocations.toArray(new String[configLocations.size()]),
			configurableApplicationContext);

		BeanLocator beanLocator = new BeanLocatorImpl(
			ClassLoaderUtil.getPortalClassLoader(),
			configurableApplicationContext);

		PortalBeanLocatorUtil.setBeanLocator(beanLocator);

		return configurableApplicationContext;
	}

	private ConfigurableApplicationContext _configurableApplicationContext;
	private ApplicationContextServicePublisher
		_applicationContextServicePublisher;

}