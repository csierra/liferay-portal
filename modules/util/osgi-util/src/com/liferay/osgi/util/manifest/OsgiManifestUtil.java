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
import java.io.OutputStream;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.osgi.framework.Constants;

/**
 * @author Carlos Sierra Andrés
 * @author Miguel Pastor
 */
public class OsgiManifestUtil implements Constants {

	public static OsgiManifestUtil create(Manifest manifest) {
		if (!isOSGiManifest(manifest)) {
			throw new RuntimeException("Not an OSGi manifest");
		}

		return new OsgiManifestUtil(manifest);
	}

	public void appendToAttribute(
		String key, String value, boolean allowDuplicates) {

		String currentValue = getAttribute(key);

		if (currentValue == null) {
			currentValue = value;
		}

		if (allowDuplicates) {
			currentValue = currentValue + ',' + value;
		}
		else {
			Set<String> values = new LinkedHashSet<String>(
				Arrays.asList(currentValue.split(",")));

			values.add(value);

			currentValue = _join(",", values);
		}

		Attributes attributes = _manifest.getMainAttributes();

		attributes.putValue(key, currentValue);
	}

	public void appendToClassPath(String path) {
		appendToAttribute(BUNDLE_CLASSPATH, path, false);
	}

	public void appendToImport(String pgk) {
		appendToAttribute(IMPORT_PACKAGE, pgk, false);
	}

	public String getAttribute(String key) {
		Attributes attributes = _manifest.getMainAttributes();

		return attributes.getValue(key);
	}

	public void putIfAbsent(String key, String value) {
		Attributes attributes = _manifest.getMainAttributes();

		if (!attributes.containsKey(key)) {
			attributes.putValue(key, value);
		}
	}

	public void writeTo(OutputStream outputStream) throws IOException {
		_manifest.write(outputStream);
	}

	private static boolean isOSGiManifest(Manifest manifest) {
		Attributes attributes = manifest.getMainAttributes();

		return attributes.containsKey(new Attributes.Name(BUNDLE_NAME));
	}

	private OsgiManifestUtil(Manifest manifest) {
		_manifest = manifest;
	}

	private String _join(String delim, Collection<String> collection) {
		StringBuilder sb = new StringBuilder(collection.size());

		for (String s : collection) {
			sb.append(s);
			sb.append(delim);
		}

		sb.deleteCharAt(sb.length() - 1);

		return sb.toString();
	}

	private Manifest _manifest;

}