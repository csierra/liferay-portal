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

package com.liferay.document.library.jaxrs.provider;

import com.liferay.portal.kernel.util.HttpUtil;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Carlos Sierra Andrés
 */
@Provider
public class PageContainerMessageBodyWriter implements MessageBodyWriter<PageContainer<?>> {

	@Override
	public boolean isWriteable(
		Class<?> type, Type genericType, Annotation[] annotations,
		MediaType mediaType) {

		if (!(PageContainer.class.isAssignableFrom(type))) {
			return false;
		}

		Class<?> targetClass = null;

		if (genericType instanceof ParameterizedType) {
			ParameterizedType parameterizedType =
				(ParameterizedType)genericType;

			Type targetType = parameterizedType.getActualTypeArguments()[0];

			if (targetType instanceof ParameterizedType) {
				ParameterizedType para = (ParameterizedType) targetType;

				targetClass = (Class<?>)para.getRawType();
				genericType = para;
			}
			else {
				targetClass = (Class<?>) targetType;
				genericType = targetType;
			}
		}

		if (targetClass == null) {
			return false;
		}

		return (_providers.getMessageBodyWriter(
			targetClass, genericType, annotations, mediaType) != null);
	}

	@Override
	public long getSize(
		PageContainer<?> pageContainer, Class<?> type, Type genericType,
		Annotation[] annotations, MediaType mediaType) {

		return -1;
	}

	@Override
	public void writeTo(
			PageContainer<?> pageContainer, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream)
		throws IOException, WebApplicationException {

		List<Object> linkHeader = new ArrayList<>();

		int currentPage = pageContainer.getCurrentPage();

		String requestURL =
			_httpServletRequest.getRequestURI() + "?" +
			_httpServletRequest.getQueryString();

		if (pageContainer.hasNext()) {
			String nextPageUrl = HttpUtil.setParameter(
				requestURL, "page", currentPage + 1);

			nextPageUrl = HttpUtil.setParameter(
				nextPageUrl, "per_page", pageContainer.getItemsPerPage());

			linkHeader.add(link(nextPageUrl, "next"));

			String lastPageUrl = HttpUtil.setParameter(
				requestURL, "page", pageContainer.getLastPage());

			lastPageUrl = HttpUtil.setParameter(
				lastPageUrl, "per_page", pageContainer.getItemsPerPage());

			linkHeader.add(link(lastPageUrl, "last"));
		}

		if (pageContainer.hasPrevious()) {
			String prevPageUrl = HttpUtil.setParameter(
				requestURL, "page", currentPage - 1);

			prevPageUrl = HttpUtil.setParameter(
				prevPageUrl, "per_page", pageContainer.getItemsPerPage());

			linkHeader.add(link(prevPageUrl, "prev"));

			String firstPageUrl = HttpUtil.setParameter(requestURL, "page", 1);

			firstPageUrl = HttpUtil.setParameter(
				firstPageUrl, "per_page", pageContainer.getItemsPerPage());

			linkHeader.add(link(firstPageUrl, "first"));
		}

		httpHeaders.addAll("Link", linkHeader);

		ParameterizedType parameterizedType = (ParameterizedType)genericType;

		Type targetType = parameterizedType.getActualTypeArguments()[0];

		Class<Object> targetClass = null;

		if (targetType instanceof ParameterizedType) {
			ParameterizedType para = (ParameterizedType) targetType;

			targetClass = (Class<Object>) para.getRawType();
			genericType = para;
		}
		else {
			targetClass = (Class<Object>)targetType;
			genericType = targetType;
		}

		@SuppressWarnings("rawtypes")
		MessageBodyWriter<Object> messageBodyWriter =
			_providers.getMessageBodyWriter(
				targetClass, genericType, annotations, mediaType);

		messageBodyWriter.writeTo(
			pageContainer.getContainer(), targetClass,
			genericType, annotations, mediaType, httpHeaders, entityStream);
	}

	private String link(String url, String rel) {
		url = url.replace("&", "&amp;");

		return "<link href=\"" + url + "\" rel=\"" + rel + "\" />";
	}

	@Context
	Providers _providers;

	@Context
	HttpServletRequest _httpServletRequest;
}