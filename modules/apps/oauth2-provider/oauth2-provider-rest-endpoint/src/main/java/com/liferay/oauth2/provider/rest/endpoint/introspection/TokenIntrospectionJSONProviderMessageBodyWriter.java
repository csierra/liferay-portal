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

package com.liferay.oauth2.provider.rest.endpoint.introspection;

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import org.apache.cxf.rs.security.oauth2.utils.OAuthConstants;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Tomas Polesovsky
 */
@Provider
@Produces("application/json")
public class TokenIntrospectionJSONProviderMessageBodyWriter
	implements
	MessageBodyWriter<TokenIntrospection> {

	@Override
	public boolean isWriteable(
		Class<?> type, Type genericType, Annotation[] annotations,
		MediaType mediaType) {

		return TokenIntrospection.class.isAssignableFrom(type);
	}

	@Override
	public long getSize(
		TokenIntrospection tokenIntrospection, Class<?> type,
		Type genericType, Annotation[] annotations, MediaType mediaType) {

		return -1;
	}

	@Override
	public void writeTo(
		TokenIntrospection tokenIntrospection, Class<?> type,
		Type genericType, Annotation[] annotations, MediaType mediaType,
		MultivaluedMap<String, Object> httpHeaders,
		OutputStream entityStream)
		throws IOException, WebApplicationException {

		StringBundler sb = new StringBundler();
		sb.append("{");

		append(sb, "active", tokenIntrospection.isActive(), false);

		if (tokenIntrospection.isActive()) {
			if (tokenIntrospection.getAud() != null) {
				List<String> audience = new ArrayList<>(
					tokenIntrospection.getAud());

				audience.removeIf(String::isEmpty);

				if (!audience.isEmpty()) {
					if (audience.size() == 1) {
						append(sb, "aud", audience.get(0));
					}
					else {
						append(sb, "aud", audience);
					}
				}
			}

			append(
				sb, OAuthConstants.CLIENT_ID,
				tokenIntrospection.getClientId());

			append(sb, "exp", tokenIntrospection.getExp());
			append(sb, "iat", tokenIntrospection.getIat());
			append(sb, "iss", tokenIntrospection.getIss());
			append(sb, "jti", tokenIntrospection.getJti());
			append(sb, "nbf", tokenIntrospection.getNbf());
			append(sb, OAuthConstants.SCOPE, tokenIntrospection.getScope());
			append(sb, "sub", tokenIntrospection.getSub());
			append(
				sb, OAuthConstants.ACCESS_TOKEN_TYPE,
				tokenIntrospection.getTokenType());

			append(sb, "username", tokenIntrospection.getUsername());

			Map<String, String> extensions =
				tokenIntrospection.getExtensions();

			if ((extensions != null) && !extensions.isEmpty()) {
				for (Map.Entry<String, String> extension :
					extensions.entrySet()) {

					append(sb, extension.getKey(), extension.getValue());
				}
			}
		}

		sb.append("}");

		String result = sb.toString();

		entityStream.write(result.getBytes(StandardCharsets.UTF_8));

		entityStream.flush();
	}

	protected void append(
		StringBundler sb, String key, Long value) {

		if (value == null) {
			return;
		}

		sb.append(",");

		append(sb, key, value, false);
	}

	protected void append(
		StringBundler sb, String key, String value) {

		if (value == null) {
			return;
		}

		sb.append(",");

		append(sb, key, value, true);
	}

	protected void append(
		StringBundler sb, String key, List<String> value) {

		StringBundler arraySB = new StringBundler(
			(value.size() * 2 - 1) + 2);

		arraySB.append("[");

		for (int i = 0; i < value.size(); i++) {
			if (i > 0) {
				arraySB.append(",");
			}
			appendValue(arraySB, value.get(i), true);
		}

		arraySB.append("]");

		sb.append(",");

		append(sb, key, arraySB.toString(), false);
	}

	protected void append(
		StringBundler sb, String key, Object value, boolean quote) {

		sb.append("\"");
		sb.append(key);
		sb.append("\":");

		appendValue(sb, value, quote);
	}

	protected void appendValue(
		StringBundler sb, Object value, boolean quote) {
		if (quote) {
			sb.append("\"");

			String stringValue = StringUtil.replace(
				String.valueOf(value), new String[]{"\\", "\""},
				new String[]{"\\\\", "\\\""});

			sb.append(stringValue);
			sb.append("\"");
		}
		else {
			sb.append(value);
		}
	}
}
