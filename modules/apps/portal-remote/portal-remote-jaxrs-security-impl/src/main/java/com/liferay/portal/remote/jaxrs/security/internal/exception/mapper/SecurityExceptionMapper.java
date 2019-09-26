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

package com.liferay.portal.remote.jaxrs.security.internal.exception.mapper;

import com.liferay.portal.remote.jaxrs.security.internal.entity.ForbiddenEntity;

import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Providers;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;

/**
 * @author Carlos Sierra Andrés
 */
@Component(
	property = {
		JaxrsWhiteboardConstants.JAX_RS_APPLICATION_SELECT + "=(!(liferay.access.security.exception.mapper=false))",
		JaxrsWhiteboardConstants.JAX_RS_EXTENSION + "=true",
		JaxrsWhiteboardConstants.JAX_RS_NAME + "=Liferay.Security.Exception.Mapper"
	},
	scope = ServiceScope.PROTOTYPE, service = ExceptionMapper.class
)
public class SecurityExceptionMapper
	implements ExceptionMapper<SecurityException> {

	@Override
	public Response toResponse(SecurityException exception) {
		MediaType mediaType = null;

		List<MediaType> mediaTypes = _httpHeaders.getAcceptableMediaTypes();

		for (MediaType currentMediaType : mediaTypes) {
			MessageBodyWriter<ForbiddenEntity> messageBodyWriter =
				_providers.getMessageBodyWriter(
					ForbiddenEntity.class, null, null, currentMediaType);

			if (messageBodyWriter != null) {
				mediaType = currentMediaType;

				break;
			}
		}

		if (mediaType == null) {
			mediaType = MediaType.APPLICATION_XML_TYPE;
		}

		return Response.status(
			Response.Status.FORBIDDEN
		).entity(
			new ForbiddenEntity(exception)
		).type(
			mediaType
		).build();
	}

	@Context
	private HttpHeaders _httpHeaders;

	@Context
	private Providers _providers;

}