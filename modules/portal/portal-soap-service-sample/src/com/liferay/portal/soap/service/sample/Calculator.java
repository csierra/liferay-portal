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

package com.liferay.portal.soap.service.sample;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * @author Carlos Sierra Andrés
 */
@WebService(wsdlLocation = "classpath:/META-INF/resources/calculator.wsdl")
public interface Calculator {

	@WebMethod(operationName = "calculate")
	public int calcula(int a, int b);

	public ComplicatedThing getComplicatedThing();

	public int notExportedInWSDL();

}