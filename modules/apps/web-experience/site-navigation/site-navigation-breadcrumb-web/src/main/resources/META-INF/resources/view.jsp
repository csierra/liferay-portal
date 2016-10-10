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

<%@ include file="/init.jsp" %>

<%
Map<String, Object> context = new HashMap<>();

context.put("visible", true);
%>

<soy:template-renderer
	context="<%= context %>"
	templateNamespace="Modal.render"
/>

<liferay-ui:breadcrumb
	ddmTemplateGroupId="<%= siteNavigationBreadcrumbDisplayContext.getDisplayStyleGroupId() %>"
	ddmTemplateKey="<%= siteNavigationBreadcrumbDisplayContext.getDDMTemplateKey() %>"
	showCurrentGroup="<%= siteNavigationBreadcrumbDisplayContext.isShowCurrentGroup() %>"
	showGuestGroup="<%= siteNavigationBreadcrumbDisplayContext.isShowGuestGroup() %>"
	showLayout="<%= siteNavigationBreadcrumbDisplayContext.isShowLayout() %>"
	showParentGroups="<%= siteNavigationBreadcrumbDisplayContext.isShowParentGroups() %>"
	showPortletBreadcrumb="<%= siteNavigationBreadcrumbDisplayContext.isShowPortletBreadcrumb() %>"
/>