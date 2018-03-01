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

<%@ include file="/admin/init.jsp" %>

<%

String redirect = ParamUtil.getString(request, "redirect");

portletDisplay.setShowBackIcon(true);
portletDisplay.setURLBack(redirect);

long oAuth2ApplicationId = ParamUtil.getLong(request, "oAuth2ApplicationId", 0);

OAuth2Application oAuth2Application = OAuth2ApplicationServiceUtil.getOAuth2Application(oAuth2ApplicationId);

renderResponse.setTitle(LanguageUtil.format(request, "x-authorizations", new String[]{oAuth2Application.getName()}));

if (!oAuth2AdminPortletDisplayContext.hasViewGrantedAuthorizationsPermission()) {
	return;
}
%>

<div class="container-fluid-1280">
	<liferay-portlet:renderURL varImpl="applicationAuthorizationsURL">
		<portlet:param name="mvcPath" value="/admin/application_authorizations.jsp" />
		<portlet:param name="oAuth2ApplicationId" value="<%= String.valueOf(oAuth2ApplicationId) %>" />
		<portlet:param name="redirect" value="<%= redirect %>" />
	</liferay-portlet:renderURL>

	<liferay-ui:search-container
		iteratorURL="<%= applicationAuthorizationsURL %>"
		total="<%= OAuth2AuthorizationLocalServiceUtil.countByApplicationId(themeDisplay.getCompanyId(), oAuth2ApplicationId) %>" >

		<liferay-ui:search-container-results
			results="<%= OAuth2AuthorizationLocalServiceUtil.findByApplicationId(themeDisplay.getCompanyId(), oAuth2ApplicationId, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator()) %>"/>

		<liferay-ui:search-container-row
			className="com.liferay.oauth2.provider.model.OAuth2Authorization" modelVar="oAuth2Authorization">

			<liferay-ui:search-container-column-text
				property="userId" />

			<liferay-ui:search-container-column-text
				property="userName" />

			<liferay-ui:search-container-column-date
				property="createDate" />

			<liferay-ui:search-container-column-text
				property="accessTokenExpiresDate" />

			<liferay-ui:search-container-column-text
				property="refreshTokenExpiresDate" />

			<liferay-ui:search-container-column-text
				property="remoteIPInfo" />

			<liferay-ui:search-container-column-text
				name="scopes">

				<%= String.valueOf(oAuth2Authorization.getScopesList().size()) %>

				<liferay-ui:icon-help message="<%= HtmlUtil.escapeAttribute(oAuth2Authorization.getScopes()) %>" />

			</liferay-ui:search-container-column-text>

		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator
			markupView="lexicon"
			searchContainer="<%= searchContainer %>"/>

	</liferay-ui:search-container>

</div>

