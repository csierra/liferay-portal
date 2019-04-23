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

<%@ page isErrorPage="true" %>
<%@ page trimDirectiveWhitespaces="true" %>

<%@ page import="com.liferay.portal.kernel.util.JavaConstants" %>
<%@ page import="com.liferay.portal.kernel.log.Log" %>
<%@ page import="com.liferay.portal.kernel.log.LogFactoryUtil" %>

<%
ErrorData errorData = pageContext.getErrorData();

int code = errorData.getStatusCode();
String msg = String.valueOf(request.getAttribute(JavaConstants.JAVAX_SERVLET_ERROR_MESSAGE));
String uri = errorData.getRequestURI();

if (_log.isWarnEnabled()) {
	_log.warn("{code=\"" + code + "\", msg=\"" + msg + "\", uri=" + uri + "}", exception);
}

String accept = request.getHeader("Accept");
%>

<c:choose>
	<c:when test='<%= (accept != null) && accept.contains("json") %>'>
		<%@ include file="code_json.jspf" %>
	</c:when>
	<c:otherwise>
		<%@ include file="code_html.jspf" %>
	</c:otherwise>
</c:choose>

<%!
private static Log _log = LogFactoryUtil.getLog("portal_web.docroot.errors.code_jsp");
%>