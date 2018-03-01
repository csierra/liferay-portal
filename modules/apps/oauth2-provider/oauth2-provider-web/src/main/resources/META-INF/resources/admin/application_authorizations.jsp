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
%>

<div class="container-fluid-1280">
	<liferay-ui:header title="<%= LanguageUtil.format(request, "x-authorizations", new String[]{oAuth2Application.getName()}) %>" />

	<liferay-ui:tabs cssClass="navbar-no-collapse panel panel-default" names="by-access-token,by-refresh-token" refresh="<%= false %>" type="tabs nav-tabs-default ">
		<liferay-ui:section>
			<liferay-ui:search-container>
				<liferay-ui:search-container-results
					results="<%= new ArrayList(OAuth2TokenLocalServiceUtil.findByApplicationId(
						oAuth2ApplicationId, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator())) %>" />

				<liferay-ui:search-container-row
					className="com.liferay.oauth2.provider.model.OAuth2Token" modelVar="oAuth2Token">

					<liferay-ui:search-container-column-text property="userId" />

					<liferay-ui:search-container-column-text property="userName" />

					<liferay-ui:search-container-column-date property="createDate" />

					<liferay-ui:search-container-column-text property="lifeTime" />

					<liferay-ui:search-container-column-text property="remoteIPInfo" />

					<liferay-ui:search-container-column-text name="scopes">

						<%= String.valueOf(oAuth2Token.getScopesList().size()) %>

						<liferay-ui:icon-help message="<%= HtmlUtil.escapeAttribute(oAuth2Token.getScopes()) %>" />

					</liferay-ui:search-container-column-text>

				</liferay-ui:search-container-row>

				<liferay-ui:search-iterator
					markupView="lexicon"
					searchContainer="<%= searchContainer %>"/>
			</liferay-ui:search-container>
		</liferay-ui:section>
		<liferay-ui:section>
			<liferay-ui:search-container>
				<liferay-ui:search-container-results
					results="<%= new ArrayList(OAuth2RefreshTokenLocalServiceUtil.findByApplication(
						oAuth2ApplicationId, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator())) %>" />

				<liferay-ui:search-container-row
					className="com.liferay.oauth2.provider.model.OAuth2RefreshToken" modelVar="oAuth2RefreshToken">

					<liferay-ui:search-container-column-text property="userId" />

					<liferay-ui:search-container-column-text property="userName" />

					<liferay-ui:search-container-column-date property="createDate" />

					<liferay-ui:search-container-column-text property="lifeTime" />

					<liferay-ui:search-container-column-text property="remoteIPInfo" />

					<liferay-ui:search-container-column-text name="scopes">

						<%= String.valueOf(oAuth2RefreshToken.getScopesList().size()) %>

						<liferay-ui:icon-help message="<%= HtmlUtil.escapeAttribute(oAuth2RefreshToken.getScopes()) %>" />

					</liferay-ui:search-container-column-text>
				</liferay-ui:search-container-row>

				<liferay-ui:search-iterator
					markupView="lexicon"
					searchContainer="<%= searchContainer %>"/>
			</liferay-ui:search-container>
		</liferay-ui:section>
	</liferay-ui:tabs>
</div>

