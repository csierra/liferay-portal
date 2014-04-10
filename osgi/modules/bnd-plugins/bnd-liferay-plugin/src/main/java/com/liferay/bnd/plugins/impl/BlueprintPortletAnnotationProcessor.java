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

package com.liferay.bnd.plugins.impl;

import aQute.bnd.osgi.Analyzer;
import aQute.bnd.osgi.Jar;

import aQute.service.reporter.Reporter;
import com.liferay.bnd.plugins.PortletAnnotationDataCollector;
import com.liferay.bnd.plugins.PortletAnnotationProcessor;
import com.liferay.bnd.plugins.PortletResourceWriter;

import java.io.IOException;

import java.util.Collection;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * @author Carlos Sierra Andrés
 */
public class BlueprintPortletAnnotationProcessor
	implements PortletAnnotationProcessor {

	@Override
	public void process(
		Collection<PortletAnnotationDataCollector> annotationDataCollectors,
		Analyzer analyzer) {

		PortletResourceWriter resourceWriter = null;
		try {
			resourceWriter = getPortletResourceWriter();
		} catch (Exception e) {
			_reporter.exception(e, "Could not get PortletResourceWriter");
		}

		resourceWriter.setAnnotationDataCollectors(annotationDataCollectors);

		Jar jar = analyzer.getJar();

		String blueprintFileLocation = _getBlueprintFileLocation();

		jar.putResource(blueprintFileLocation, resourceWriter);

		Manifest manifest = new Manifest();

		Attributes mainAttributes = manifest.getMainAttributes();

		mainAttributes.putValue("Spring-Context", blueprintFileLocation);

		try {
			analyzer.mergeManifest(manifest);
		}
		catch (IOException e) {
			_reporter.exception(e, "Could not merge manifest");
		}
	}

	public void setProperties(Map<String, String> properties) throws Exception {
		_properties = properties;
	}

	@Override
	public void setReporter(Reporter reporter) {
		_reporter = reporter;
	}

	private String _getBlueprintFileLocation() {
		String path = _properties.get("portlets-blueprint-file-path");

		if (path != null) {
			return path;
		}

		return "/META-INF/spring/portlets.xml";
	}

	private PortletResourceWriter getPortletResourceWriter()
		throws ClassNotFoundException, IllegalAccessException,
		InstantiationException {

		String portletResourceWriterClassName = _properties.get(
			"Portlet-Resource-Writer");

		if (portletResourceWriterClassName == null) {
			return new BlueprintPortletResourceWriter();
		}

		Class<PortletResourceWriter> portletResourceWriterClass =
			(Class<PortletResourceWriter>)Class.forName(
				portletResourceWriterClassName);

		return portletResourceWriterClass.newInstance();
	}

	private Map<String, String> _properties;
	private Reporter _reporter;

}