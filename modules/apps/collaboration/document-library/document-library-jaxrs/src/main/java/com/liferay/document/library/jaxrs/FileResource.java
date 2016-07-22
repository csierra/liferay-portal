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

package com.liferay.document.library.jaxrs;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Company;
import io.swagger.annotations.Api;
import org.osgi.service.component.annotations.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import java.util.Optional;

/**
 * @author Carlos Sierra Andrés
 */
@Api
@Path("/file")
@Component(immediate = true, service = FileResource.class)
public class FileResource {

	@GET
	@Produces("text/plain")
	public Optional<String> greet(
		@Context Company company, @QueryParam("suffix") String suffix) {

		try {
			return Optional.of(company.getName() + "-" + suffix);
		}
		catch (PortalException e) {
			return Optional.empty();
		}
	}

}
