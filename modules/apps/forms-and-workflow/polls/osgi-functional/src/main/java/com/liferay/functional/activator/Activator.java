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

import com.liferay.functional.osgi.OSGiOperation.OSGiResult;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.util.ResourceBundleLoader;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


import java.util.HashMap;

import static com.liferay.functional.osgi.OSGi.forEach;
import static com.liferay.functional.osgi.OSGi.forEachConfiguration;
import static com.liferay.functional.osgi.OSGi.forEver;
import static com.liferay.functional.osgi.OSGi.register;
import static com.liferay.functional.osgi.OSGi.runOsgi;
import static com.liferay.functional.osgi.OSGi.service;

/**
 * @author Carlos Sierra Andrés
 */
public class Activator implements BundleActivator {

	private OSGiResult<Void> _osgiResult;

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
				   ", _resourceBundleLoader=" + _resourceBundleLoader +
				   '}';
		}
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		System.out.println("Bundle Start");

		_osgiResult = runOsgi(bundleContext,
			service(CompanyLocalService.class).bind(cls ->
			forEachConfiguration(
				"com.liferay.portal.remote.soap.extender.configuration.SoapExtenderConfiguration",
				sec -> {
					String filter = "(servlet.context.name=" +
										  sec.get("soapDescriptorBuilderFilter").toString() +
										  ")";
					return forEver(service(
						ResourceBundleLoader.class, filter).bind(rbl ->
						register(
							Component.class, new Component(cls, rbl),
							new HashMap<>())

					));
				})),
			x -> System.out.println("Shopping web is gone"));
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		_osgiResult.close.accept(null);

		System.out.println("Bundle stop");
	}
}
