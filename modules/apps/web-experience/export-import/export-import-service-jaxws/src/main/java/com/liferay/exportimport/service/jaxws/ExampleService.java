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

package com.liferay.exportimport.service.jaxws;

import com.liferay.exportimport.kernel.service.StagingService;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Carlos Sierra Andrés
 */
@Component(
	immediate = true, property = {"staging.jax.ws.service=true"},
	service = ExampleService.class
)
@WebService
public class ExampleService {

	@WebMethod
	public String myCall() {
		return _stagingService.getOSGiServiceIdentifier();
	}

	@Reference(unbind = "-")
	protected void setStagingService(StagingService stagingService) {
		_stagingService = stagingService;
	}

	private StagingService _stagingService;

}