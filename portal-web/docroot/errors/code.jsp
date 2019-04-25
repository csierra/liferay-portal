<%--
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
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %><%@
taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>

<%@ page isErrorPage="true" %>
<%@ page trimDirectiveWhitespaces="true" %>

<%@ page import="com.liferay.petra.string.CharPool" %><%@
page import="com.liferay.petra.string.StringPool" %><%@
page import="com.liferay.portal.kernel.language.LanguageUtil" %><%@
page import="com.liferay.portal.kernel.log.Log" %><%@
page import="com.liferay.portal.kernel.log.LogFactoryUtil" %><%@
page import="com.liferay.portal.kernel.servlet.taglib.DynamicIncludeUtil" %><%@
page import="com.liferay.portal.kernel.util.GetterUtil" %><%@
page import="com.liferay.portal.kernel.util.HtmlUtil" %><%@
page import="com.liferay.portal.kernel.util.JavaConstants" %><%@
page import="com.liferay.portal.kernel.util.StringUtil" %><%@
page import="com.liferay.portal.kernel.util.Validator" %>

<%@ page import="java.util.Comparator" %><%@
page import="java.util.Set" %><%@
page import="java.util.TreeSet" %>

<%

ErrorData errorData = pageContext.getErrorData();

int code = errorData.getStatusCode();
String msg = String.valueOf(request.getAttribute(JavaConstants.JAVAX_SERVLET_ERROR_MESSAGE));
String uri = errorData.getRequestURI();

if (_log.isWarnEnabled()) {
	_log.warn("{code=\"" + code + "\", msg=\"" + msg + "\", uri=" + uri + "}", exception);
}

String dynamicIncludeKey = _getDynamicIncludeKey(request.getHeader("Accept"));
%>

<c:choose>
	<c:when test="<%= !Validator.isBlank(dynamicIncludeKey) %>">
		<liferay-util:dynamic-include key="<%= dynamicIncludeKey %>" />
	</c:when>
	<c:otherwise>
		<%@ page contentType="text/html; charset=UTF-8" %>
		<html>
			<head>
				<title>Http Status <%= code %> - <%= LanguageUtil.get(request, "http-status-code[" + code + "]") %></title>
			</head>

			<body>
				<h1>Http Status <%= code %> - <%= LanguageUtil.get(request, "http-status-code[" + code + "]") %></h1>

				<p>
					<liferay-ui:message key="message" />: <%= HtmlUtil.escape(msg) %>
				</p>

				<p>
					<liferay-ui:message key="resource" />: <%= HtmlUtil.escape(uri) %>
				</p>
			</body>
		</html>
	</c:otherwise>
</c:choose>

<%!
private static Log _log = LogFactoryUtil.getLog("portal_web.docroot.errors.code_jsp");

private static String _extractWeight(String mediaRange) {
	int weightPos = mediaRange.indexOf(";q=");

	if (weightPos < 0) {
		weightPos = mediaRange.indexOf(";Q=");
	}

	if (weightPos < 0) {
		return null;
	}

	int endPos = mediaRange.indexOf(CharPool.SEMICOLON, weightPos + 1);
	if (endPos < 0) {
		endPos = mediaRange.length();
	}

	return mediaRange.substring(weightPos, endPos);
}

private static String _getDynamicIncludeKey(String accept) {
	Comparator<String> mediaRangesComparator =
		Comparator.<String>comparingDouble(
			mediaRange -> {
				String weightString = _extractWeight(mediaRange);
				if (weightString != null) {
					return GetterUtil.getDouble(weightString.substring(3), 1);
				}

				return 1;
			}
		).reversed(
		).thenComparing(
			mediaRange -> {
				int pos = mediaRange.indexOf(CharPool.SEMICOLON);
				if (pos > 0) {
					mediaRange = mediaRange.substring(0, pos);
				}

				pos = mediaRange.indexOf(CharPool.SLASH);
				if (pos > 0) {
					mediaRange = mediaRange.substring(0, pos);
				}

				return mediaRange.trim();
			}
		).thenComparing(
			mediaRange -> {
				int pos = mediaRange.indexOf(CharPool.SEMICOLON);
				if (pos > 0) {
					mediaRange = mediaRange.substring(0, pos);
				}

				pos = mediaRange.indexOf(CharPool.SLASH);
				if (pos > 0) {
					mediaRange = mediaRange.substring(pos);
				}

				return mediaRange.trim();
			}
		).thenComparing(
			mediaRange -> {
				mediaRange = _removeWeight(mediaRange);

				int pos = mediaRange.indexOf(CharPool.SEMICOLON);
				if (pos > 0) {
					mediaRange = mediaRange.substring(pos);
				}

				return mediaRange.trim();
			}
		);

	Set<String> mediaRangesSet = new TreeSet<>(mediaRangesComparator);

	if (Validator.isBlank(accept)) {
		return null;
	}
	else if (!accept.contains(StringPool.COMMA)) {
		mediaRangesSet.add(accept);
	}
	else {
		String[] mediaRanges = accept.split(StringPool.COMMA);

		for (String mediaRange : mediaRanges) {
			mediaRangesSet.add(mediaRange);
		}
	}

	String dynamicIncludeKeyPrefix = "/errors/code.jsp#";

	for (String mediaType : mediaRangesSet) {
		mediaType = _removeWeight(mediaType);

		String dynamicIncludeKey = dynamicIncludeKeyPrefix + mediaType.trim();

		if (DynamicIncludeUtil.hasDynamicInclude(dynamicIncludeKey)) {
			return dynamicIncludeKey;
		}
	}

	return null;
}

private static String _removeWeight(String mediaRange) {
	String weightString = _extractWeight(mediaRange);

	if (weightString != null) {
		return StringUtil.removeSubstring(mediaRange, weightString);
	}

	return mediaRange;
}
%>