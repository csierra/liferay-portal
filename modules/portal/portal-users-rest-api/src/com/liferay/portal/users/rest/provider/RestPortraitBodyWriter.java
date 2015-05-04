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

package com.liferay.portal.users.rest.provider;

import com.liferay.portal.users.rest.api.model.RestPortrait;

import java.io.IOException;
import java.io.OutputStream;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

/**
 * @author Carlos Sierra Andrés
 */
@Provider
public class RestPortraitBodyWriter implements MessageBodyWriter<RestPortrait> {

	@Override
	public long getSize(
		RestPortrait restPortrait, Class<?> type, Type genericType,
		Annotation[] annotations, MediaType mediaType) {

		return -1;
	}

	@Override
	public boolean isWriteable(
		Class<?> type, Type genericType, Annotation[] annotations,
		MediaType mediaType) {

		return RestPortrait.class.isAssignableFrom(RestPortrait.class);
	}

	@Override
	public void writeTo(
			RestPortrait restPortrait, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream)
		throws IOException, WebApplicationException {

		entityStream.write(
			("<link href=\"portrait/" + restPortrait.getPortraitId() + "\" />").getBytes(Charset.forName("UTF8")));
	}

}