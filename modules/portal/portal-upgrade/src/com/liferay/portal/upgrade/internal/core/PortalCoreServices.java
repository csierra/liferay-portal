/*
 * *
 *  * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *  *
 *  * This library is free software; you can redistribute it and/or modify it under
 *  * the terms of the GNU Lesser General Public License as published by the Free
 *  * Software Foundation; either version 2.1 of the License, or (at your option)
 *  * any later version.
 *  *
 *  * This library is distributed in the hope that it will be useful, but WITHOUT
 *  * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 *  * details.
 *
 */

package com.liferay.portal.upgrade.internal.core;

import com.liferay.portal.bean.BeanLocatorImpl;
import com.liferay.portal.kernel.bean.BeanLocator;
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.util.ClassLoaderUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.SystemProperties;
import com.liferay.portal.model.Release;

import com.liferay.portal.module.framework.ModuleFrameworkUtilAdapter;
import com.liferay.portal.spring.context.ArrayApplicationContext;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.util.PropsValues;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * @author Migue Pastor
 */
@Component(immediate = true, service = PortalCoreServices.class)
public class PortalCoreServices {

	@Activate
	protected void activate() {
		System.out.println("-------------------");
		System.out.println("Register the full Application Context and publish a Servlet Context");
		System.out.println("-------------------");
	}

	@Deactivate
	protected void deactivate() {
		System.out.println("-------------------");
		System.out.println("Unregister the full Application Context and unregister the Servlet Context");
		System.out.println("-------------------");
	}

	@Reference(target = "(&(component.name=portal)(release.build.number=7.0.0.0))")
	protected void setRelease(Release release) {
	}

	public synchronized void startPortalServices() {
		List<String> configLocations = ListUtil.fromArray(
			PropsUtil.getArray(
				com.liferay.portal.kernel.util.PropsKeys.SPRING_CONFIGS));

		ApplicationContext applicationContext = new ArrayApplicationContext(
			PropsValues.SPRING_INFRASTRUCTURE_CONFIGS);

		applicationContext = new ClassPathXmlApplicationContext(
			configLocations.toArray(new String[configLocations.size()]),
			applicationContext);

		BeanLocator beanLocator = new BeanLocatorImpl(
			ClassLoaderUtil.getPortalClassLoader(), applicationContext);

		PortalBeanLocatorUtil.setBeanLocator(beanLocator);

		ModuleFrameworkUtilAdapter.registerContext(applicationContext);
	}
}