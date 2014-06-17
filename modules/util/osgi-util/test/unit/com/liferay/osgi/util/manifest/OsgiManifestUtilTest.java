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

package com.liferay.osgi.util.manifest;

import java.io.IOException;
import java.io.InputStream;

import java.util.jar.Manifest;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.osgi.framework.Constants;

/**
 * @author Miguel Pastor
 * @author Carlos Sierra Andrés
 */
public class OsgiManifestUtilTest implements Constants {

	@Before
	public void setUp() {
		_osgiManifestUtil = _createManifestUtil();
	}

	@After
	public void tearDown() {
		_osgiManifestUtil = null;
	}

	@Test
	public void testAddNonExistingAttribute() {
		_osgiManifestUtil.putIfAbsent(DYNAMICIMPORT_PACKAGE, "foo.*");

		Assert.assertEquals(
			"foo.*", _osgiManifestUtil.getAttribute(DYNAMICIMPORT_PACKAGE));
	}

	@Test
	public void testAppendToClasspath() {
		_osgiManifestUtil.appendToClassPath("lib/b.jar");

		Assert.assertEquals(
			".,lib/a.jar,lib/b.jar",
			_osgiManifestUtil.getAttribute(BUNDLE_CLASSPATH));
	}

	@Test
	public void testAppendToImport() {
		_osgiManifestUtil.appendToImport("com.liferay.one.more.import");

		Assert.assertEquals(
			"com.liferay.other.module.c,com.liferay.one.more.import",
			_osgiManifestUtil.getAttribute(IMPORT_PACKAGE));
	}

	private OsgiManifestUtil _createManifestUtil() {
		Class<?> clazz = getClass();

		InputStream inputStream = clazz.getResourceAsStream(
			"dependencies/MANIFEST.MF");

		Manifest manifest = null;

		try {
			manifest = new Manifest(inputStream);
		}
		catch (IOException ioe) {
			Assert.fail("Unexpected error creating the MANIFEST test file");
		}

		return OsgiManifestUtil.create(manifest);
	}

	private OsgiManifestUtil _osgiManifestUtil;

}