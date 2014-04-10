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

import aQute.bnd.osgi.Annotation;
import aQute.bnd.osgi.ClassDataCollector;
import aQute.bnd.osgi.Descriptors;

/**
 * @author Carlos Sierra Andrés
 */
public class PortletAnnotationDataCollector extends ClassDataCollector {

	@Override
	public void annotation(Annotation annotation) {
		_name = (String) annotation.get("name");
	}

	@Override
	public void classBegin(int access, Descriptors.TypeRef className) {
		this._className = className.getFQN();
	}

	public String getClassName() {
		return _className;
	}

	public String getName() {
		return _name;
	}

	private String _className;
	private String _name;
}