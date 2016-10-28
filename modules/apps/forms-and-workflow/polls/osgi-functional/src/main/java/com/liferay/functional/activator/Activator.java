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

package com.liferay.functional.activator;

import com.liferay.functional.Applicative;
import com.liferay.functional.osgi.OSGi;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.util.ResourceBundleLoader;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import java.util.HashMap;
import java.util.concurrent.CompletionStage;

/**
 * @author Carlos Sierra Andrés
 */
public class Activator implements BundleActivator {

	static class Component {
		private CompanyLocalService _companyLocalService;
		private ResourceBundleLoader _resourceBundleLoader;

		public Component(
			CompanyLocalService companyLocalService,
			ResourceBundleLoader resourceBundleLoader) {

			_companyLocalService = companyLocalService;
			_resourceBundleLoader = resourceBundleLoader;
		}

		@Override
		public String toString() {
			return "Component{" +
				   "_companyLocalService=" + _companyLocalService +
				   ", _groupLocalService=" + _resourceBundleLoader +
				   '}';
		}
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		System.out.println("Bundle Start");

		OSGi<ServiceRegistration<Component>> program =
			((OSGi<Component>) Applicative.lift(
				Component::new,
				OSGi.service(CompanyLocalService.class),
				OSGi.service(
					ResourceBundleLoader.class,
					"(bundle.symbolic.name=com.liferay.shopping.web)"))).
				bind(c -> OSGi.register(Component.class, c, new HashMap<>()));

		CompletionStage<ServiceRegistration<Component>> future = OSGi.runOsgi(
			bundleContext, program, x -> System.out.println("CLEANING: " + x));

//		OSGi<ServiceRegistration<Component>> registration =
//			componentOSGi.bind(x -> OSGi.registerService(x));
//
//		CompletionStage<Component> future = OSGi.runOsgi(
//			bundleContext, componentOSGi, x -> x.unregister());

		future.thenAccept(x -> System.out.println("GOT: " + x));
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		System.out.println("Bundle stop");
	}
}
