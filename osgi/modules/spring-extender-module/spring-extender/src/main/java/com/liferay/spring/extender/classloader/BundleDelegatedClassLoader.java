/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.spring.extender.classloader;

import com.liferay.portal.kernel.spring.util.SpringFactoryUtil;
import com.liferay.portal.spring.aop.ChainableMethodAdviceInjectorCollector;
import org.eclipse.gemini.blueprint.extender.OsgiBeanFactoryPostProcessor;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanIsAbstractException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;

/**
 * @author Miguel Pastor
 */
public class BundleDelegatedClassLoader extends ClassLoader {

	public BundleDelegatedClassLoader(Bundle bundle) {
		if (bundle == null) {
			throw new IllegalArgumentException("A valid bundle is required!");
		}

		_bundle = bundle;
	}

	@Override
	protected URL findResource(String name) {
		return _bundle.getResource(name);
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		return _bundle.loadClass(name);
	}

	@Override
	protected Enumeration<URL> findResources(String name) throws IOException {
		return _bundle.getResources(name);
	}

	@Override
	public URL getResource(String name) {
		return _bundle.getResource(name);
	}

	@Override
	public Enumeration<URL> getResources(String name) throws IOException {
		return _bundle.getResources(name);
	}

	protected Class<?> loadClass(String name, boolean resolve)
		throws ClassNotFoundException {

		Class<?> clazz = findClass(name);

		if (resolve) {
			resolveClass(clazz);
		}

		return clazz;
	}

	private final Bundle _bundle;

}