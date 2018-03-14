/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.oauth2.provider.rest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.jaxrs.runtime.JaxrsServiceRuntime;
import org.osgi.service.jaxrs.runtime.dto.ApplicationDTO;
import org.osgi.service.jaxrs.runtime.dto.FailedApplicationDTO;
import org.osgi.service.jaxrs.runtime.dto.RuntimeDTO;

@Component(
	immediate = true,
	property = {
		"osgi.command.function=list", "osgi.command.function=failed",
		"osgi.command.scope=jaxrs"
	},
	service = JAXRSCommands.class
)
public class JAXRSCommands {

	public void list() {
		RuntimeDTO runtimeDTO = _jaxrsServiceRuntime.getRuntimeDTO();

		ApplicationDTO[] applicationDTOs = runtimeDTO.applicationDTOs;

		for (ApplicationDTO applicationDTO : applicationDTOs) {
			System.out.println(applicationDTO);
		}
	}

	public void failed() {
		RuntimeDTO runtimeDTO = _jaxrsServiceRuntime.getRuntimeDTO();

		FailedApplicationDTO[] applicationDTOs = runtimeDTO.failedApplicationDTOs;

		for (FailedApplicationDTO applicationDTO : applicationDTOs) {
			System.out.println(applicationDTO);
		}
	}

	@Reference
	private JaxrsServiceRuntime _jaxrsServiceRuntime;
}
