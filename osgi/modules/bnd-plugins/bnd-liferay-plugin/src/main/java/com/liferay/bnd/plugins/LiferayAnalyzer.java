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

package com.liferay.bnd.plugins;

import aQute.bnd.osgi.Analyzer;
import aQute.bnd.osgi.Clazz;
import aQute.bnd.service.AnalyzerPlugin;
import aQute.bnd.service.Plugin;

import aQute.service.reporter.Reporter;

import com.liferay.annotations.Portlet;
import com.liferay.bnd.plugins.impl.BlueprintPortletAnnotationProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Carlos Sierra Andrés
 */
public class LiferayAnalyzer implements Plugin, AnalyzerPlugin {

	@Override
	public boolean analyzeJar(Analyzer analyzer) throws Exception {

		final Collection<Clazz> annotatedPortlets = analyzer.getClasses(
			"", Clazz.QUERY.ANNOTATED.toString(), Portlet.class.getName(),
			Clazz.QUERY.NAMED.toString(), "*");

		if (annotatedPortlets.size() == 0) {
			return false;
		}

		final List<PortletAnnotationDataCollector> annotationDataCollectors =
			new ArrayList<PortletAnnotationDataCollector>();

		for (Clazz annotatedPortlet : annotatedPortlets) {
			PortletAnnotationDataCollector padc =
				new PortletAnnotationDataCollector();

			try {
				annotatedPortlet.parseClassFileWithCollector(padc);
			} catch (Exception e) {
				e.printStackTrace();
			}

			annotationDataCollectors.add(padc);
		}

		PortletAnnotationProcessor portletAnnotationProcessor =
			_getPortletAnnotationProcessor();

		portletAnnotationProcessor.process(annotationDataCollectors, analyzer);

		return false;
	}

	@Override
	public void setProperties(Map<String, String> properties) throws Exception {
		_properties = properties;
	}

	@Override
	public void setReporter(Reporter processor) {
		_reporter = processor;
	} private void _init(PortletAnnotationProcessor processor) throws Exception {
		processor.setProperties(_properties);
		processor.setReporter(_reporter);
	}

	private PortletAnnotationProcessor _getPortletAnnotationProcessor()
		throws Exception {

		String annotationProcessorClassName = _properties.get(
			"Annotation-Processor");

		PortletAnnotationProcessor portletAnnotationProcessor;

		if (annotationProcessorClassName == null) {
			portletAnnotationProcessor = new BlueprintPortletAnnotationProcessor();
		}
		else {
			Class<PortletAnnotationProcessor> annotationProcessorClass =
				(Class<PortletAnnotationProcessor>)Class.forName(
					annotationProcessorClassName);

			portletAnnotationProcessor = annotationProcessorClass.newInstance();
		}

		_init(portletAnnotationProcessor);

		return portletAnnotationProcessor;
	}

	private Map<String, String> _properties;
	private Reporter _reporter;

}