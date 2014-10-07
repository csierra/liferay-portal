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

package com.liferay.typeclasses;

import aQute.bnd.annotation.metatype.Meta;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.service.component.annotations.Component;

import java.util.Dictionary;

/**
 * @author Carlos Sierra Andrés
 */
@Component(immediate = true)
@Meta.OCD(factory = true, id = "com.liferay.managedservice")
public class TypeClassesRegistryFactory
	implements ManagedServiceFactory {

	@Override
	public String getName() {
		return null;
	}

	@Override
	public void updated(String pid, Dictionary<String, ?> properties)
		throws ConfigurationException {

	}

	@Override
	public void deleted(String pid) {

	}
}
