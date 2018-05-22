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

<%
request.setAttribute("application_authorizations.jsp-oAuth2Application", oAuth2Application);

renderResponse.setTitle(LanguageUtil.format(request, "x-authorizations", new String[] {oAuth2Application.getName()}));

if (!oAuth2AdminPortletDisplayContext.hasViewGrantedAuthorizationsPermission()) {
	return;
}

String orderByCol = ParamUtil.getString(request, "orderByCol", "createDate");
String orderByType = ParamUtil.getString(request, "orderByType", "desc");
%>

<liferay-portlet:renderURL varImpl="portletURL">
	<portlet:param name="appTab" value="authorizations" />
	<portlet:param name="mvcPath" value="/admin/edit_application.jsp" />
	<portlet:param name="oAuth2ApplicationId" value="<%= String.valueOf(oAuth2ApplicationId) %>" />
	<portlet:param name="redirect" value="<%= redirect %>" />
</liferay-portlet:renderURL>

<liferay-frontend:management-bar>
	<liferay-frontend:management-bar-buttons>
		<liferay-frontend:management-bar-display-buttons
			displayViews='<%= new String[] {"list"} %>'
			portletURL="<%= PortletURLUtil.clone(portletURL, liferayPortletResponse) %>"
			selectedDisplayStyle="list"
		/>
	</liferay-frontend:management-bar-buttons>

	<liferay-frontend:management-bar-filters>
		<liferay-frontend:management-bar-navigation
			navigationKeys='<%= new String[] {"all"} %>'
			portletURL="<%= PortletURLUtil.clone(portletURL, liferayPortletResponse) %>"
		/>

		<liferay-frontend:management-bar-sort
			orderByCol="<%= orderByCol %>"
			orderByType="<%= orderByType %>"
			orderColumns='<%= new String[] {"createDate"} %>'
			portletURL="<%= PortletURLUtil.clone(portletURL, liferayPortletResponse) %>"
		/>
	</liferay-frontend:management-bar-filters>
</liferay-frontend:management-bar>

<div class="container-fluid-1280">
	<liferay-ui:search-container
		emptyResultsMessage="no-devices-were-found"
		iteratorURL="<%= portletURL %>"
		total="<%= OAuth2AuthorizationServiceUtil.getApplicationOAuth2AuthorizationsCount(oAuth2ApplicationId) %>"
	>

		<%
		OrderByComparator orderByComparator = null;

		if (orderByCol.equals("createDate")) {
			orderByComparator = OrderByComparatorFactoryUtil.create("OAuth2Authorization", "createDate", orderByType.equals("desc"));
		}
		%>

		<liferay-ui:search-container-results
			results="<%= OAuth2AuthorizationServiceUtil.getApplicationOAuth2Authorizations(oAuth2ApplicationId, searchContainer.getStart(), searchContainer.getEnd(), orderByComparator) %>"
		/>

		<liferay-ui:search-container-row
			className="com.liferay.oauth2.provider.model.OAuth2Authorization"
			escapedModel="<%= true %>"
			modelVar="oAuth2Authorization"
		>
			<liferay-ui:search-container-column-text
				property="userId"
			/>

			<liferay-ui:search-container-column-text
				property="userName"
			/>

			<liferay-ui:search-container-column-date
				property="createDate"
			/>

			<liferay-ui:search-container-column-date
				property="accessTokenExpirationDate"
			/>

			<liferay-ui:search-container-column-date
				property="refreshTokenExpirationDate"
			/>

			<liferay-ui:search-container-column-text
				property="remoteIPInfo"
			/>

			<liferay-ui:search-container-column-jsp
				align="right"
				path="/admin/application_authorization_actions.jsp"
			/>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator
			markupView="lexicon"
			searchContainer="<%= searchContainer %>"
		/>
	</liferay-ui:search-container>
</div>