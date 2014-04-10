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

import aQute.bnd.osgi.WriteResource;
import com.liferay.bnd.plugins.PortletAnnotationDataCollector;
import com.liferay.bnd.plugins.PortletResourceWriter;

import java.io.IOException;
import java.io.OutputStream;

import java.util.Collection;

/**
 * @author Carlos Sierra Andrés
 */
public class BlueprintPortletResourceWriter extends WriteResource
	implements PortletResourceWriter {

	public Collection<PortletAnnotationDataCollector>
		getAnnotationDataCollectors() {

		return _annotationDataCollectors;
	}

	@Override
	public long lastModified() {
		return _lastModified;
	}

	@Override
	public void setAnnotationDataCollectors(
		Collection<PortletAnnotationDataCollector> annotationDataCollectors) {

		_annotationDataCollectors = annotationDataCollectors;
		_lastModified = System.currentTimeMillis();
	}

	@Override
	public void write(OutputStream out) throws Exception, IOException {
		StringBuilder sb = new StringBuilder();

		sb.append("<?xml version=\"1.0\"?>");
		sb.append(LINE_SEPARATOR);

		sb.append(
			"<beans xmlns=\"http://www.springframework.org/schema/" +
				"beans\" xmlns:xsi=\"http://www.w3.org/2001/" +
				"XMLSchema-instance\" xmlns:osgi=" +
				"\"http://www.springframework.org/schema/osgi\" " +
				"default-destroy-method=\"destroy\" " +
				"default-init-method=\"afterPropertiesSet\" " +
				"xsi:schemaLocation=\"http://www.springframework.org/" +
				"schema/beans http://www.springframework.org/schema/" +
				"beans/spring-beans-3.0.xsd " +
				"http://www.springframework.org/schema/osgi" +
				" http://www.springframework.org/schema/osgi/" +
				"spring-osgi.xsd\">");
		sb.append(LINE_SEPARATOR);

		for (PortletAnnotationDataCollector annotationDataCollector :
			_annotationDataCollectors) {

			String portetName = annotationDataCollector.getName();
			String className = annotationDataCollector.getClassName();

			sb.append(
				"\t<bean id=\"" + portetName +
					"\" class=\""+ className +"\" />");
			sb.append(LINE_SEPARATOR);

			sb.append(
				"\t<osgi:service ref=\""+ portetName +
					"\" interface=\"javax.portlet.Portlet\" />");
			sb.append(LINE_SEPARATOR);
		}

		sb.append("</beans>");
		sb.append(LINE_SEPARATOR);

		out.write(sb.toString().getBytes("UTF-8"));
	}

	private static String LINE_SEPARATOR = System.getProperty("line.separator");

	private Collection<PortletAnnotationDataCollector>
		_annotationDataCollectors;
	private long _lastModified;

}