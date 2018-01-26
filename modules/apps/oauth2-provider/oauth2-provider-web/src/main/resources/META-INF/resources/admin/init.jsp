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
<%@ include file="../init.jsp" %>
<%@page import="com.liferay.oauth2.provider.exception.DuplicateOAuth2ClientIdException" %><%@
page import="com.liferay.oauth2.provider.model.LiferayOAuth2Scope" %><%@
page import="com.liferay.oauth2.provider.model.OAuth2Application" %><%@
page import="com.liferay.oauth2.provider.service.OAuth2ApplicationLocalServiceUtil" %><%@
page import="com.liferay.oauth2.provider.web.internal.constants.OAuth2AdminWebKeys" %><%@
page import="com.liferay.portal.kernel.dao.search.ResultRow" %><%@
page import="com.liferay.portal.kernel.language.LanguageUtil" %><%@
page import="com.liferay.portal.kernel.util.HtmlUtil" %><%@
page import="com.liferay.portal.kernel.util.ParamUtil" %><%@
page import="com.liferay.portal.kernel.util.StringPool" %><%@
page import="java.util.List" %><%@
page import="java.util.Map" %>
