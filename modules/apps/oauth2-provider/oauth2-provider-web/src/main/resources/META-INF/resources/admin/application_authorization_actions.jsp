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

OAuth2Application oAuth2Application = (OAuth2Application)request.getAttribute("application_authorizations.jsp-oAuth2Application");

ResultRow row = (ResultRow)request.getAttribute("SEARCH_CONTAINER_RESULT_ROW");

OAuth2Authorization oAuth2Authorization = (OAuth2Authorization)row.getObject();

long oAuth2TokenId = oAuth2Authorization.getoAuth2TokenId();
long oAuth2RefreshTokenId = oAuth2Authorization.getoAuth2RefreshTokenId();
%>

<c:if test="<%= oAuth2AdminPortletDisplayContext.hasRevokeTokenPermission(oAuth2Application) %>">

	<liferay-ui:icon-menu
		direction="left-side"
		icon="<%= StringPool.BLANK %>"
		markupView="lexicon"
		message="<%= StringPool.BLANK %>"
		showWhenSingleIcon="<%= true %>"
	>
		<c:if test="<%= oAuth2TokenId > 0 && oAuth2RefreshTokenId > 0 %>">
			<portlet:actionURL name="revokeAuthorizationTokens" var="revokeURL">
				<portlet:param name="oAuth2TokenId" value="<%= String.valueOf(oAuth2TokenId) %>" />
				<portlet:param name="oAuth2RefreshTokenId" value="<%= String.valueOf(oAuth2RefreshTokenId) %>" />
				<portlet:param name="redirect" value="<%= currentURL %>" />
			</portlet:actionURL>

			<liferay-ui:icon
				message="revoke-authorization"
				url="<%= revokeURL.toString() %>" />

			<div class="separator"><!-- --></div>
		</c:if>

		<c:if test="<%= oAuth2TokenId > 0 %>">
			<portlet:actionURL name="deleteAccessToken" var="deleteTokenURL">
				<portlet:param name="oAuth2TokenId" value="<%= String.valueOf(oAuth2TokenId) %>" />
				<portlet:param name="redirect" value="<%= currentURL %>" />
			</portlet:actionURL>

			<liferay-ui:icon
				message="delete-access-token"
				url="<%= deleteTokenURL.toString() %>" />
		</c:if>

		<c:if test="<%= oAuth2RefreshTokenId > 0 %>">
			<portlet:actionURL name="deleteRefreshToken" var="deleteTokenURL">
				<portlet:param name="oAuth2RefreshTokenId" value="<%= String.valueOf(oAuth2RefreshTokenId) %>" />
				<portlet:param name="redirect" value="<%= currentURL %>" />
			</portlet:actionURL>

			<liferay-ui:icon
				message="delete-refresh-token"
				url="<%= deleteTokenURL.toString() %>" />
		</c:if>
	</liferay-ui:icon-menu>
</c:if>