<%@ page
	import="com.liferay.frontend.taglib.clay.servlet.taglib.util.JSPCreationMenu" %>
<%@ page import="javax.portlet.PortletURL" %><%--
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

<%@include file="/admin/init.jsp"%>

<%

String displayStyle = ParamUtil.getString(request, "displayStyle", "list");

boolean orderByAsc = false;

String orderByType = ParamUtil.getString(request, "orderByType", "asc");

if (orderByType.equals("asc")) {
	orderByAsc = true;
}

OrderByComparator orderByComparator = OrderByComparatorFactoryUtil.create("OAuth2Application", "name", orderByType.equals("asc"));

int oAuth2ApplicationsCount = OAuth2ApplicationServiceUtil.getOAuth2ApplicationsCount(themeDisplay.getCompanyId());

PortletURL sortingURL = renderResponse.createRenderURL();

sortingURL.setParameter("displayStyle", displayStyle);
sortingURL.setParameter("orderByType", orderByAsc ? "desc" : "asc");
final PortletURL addOAuth2ApplicationURL = renderResponse.createRenderURL();

addOAuth2ApplicationURL.setParameter("mvcPath", "/admin/edit_application.jsp");
addOAuth2ApplicationURL.setParameter("redirect", currentURL);
%>

<aui:nav-bar cssClass="navbar-no-collapse" markupView="lexicon">
	<aui:nav collapsible="<%= false %>" cssClass="navbar-nav">
		<aui:nav-item label="oauth2-applications" selected="<%= true %>" />
	</aui:nav>
</aui:nav-bar>

<clay:management-toolbar
	creationMenu="<%=
		new JSPCreationMenu(pageContext) {
			{
				addPrimaryDropdownItem(dropdownItem -> dropdownItem.setHref(addOAuth2ApplicationURL.toString()));
			}
		}
	%>"
	disabled="<%= oAuth2ApplicationsCount == 0 %>"
	namespace="<%= renderResponse.getNamespace() %>"
	selectable="<%= false %>"
	showCreationMenu="<%= oAuth2AdminPortletDisplayContext.hasAddApplicationPermission() %>"
	showSearch="<%= false %>"
	sortingOrder="<%= orderByType %>"
	sortingURL="<%= sortingURL.toString() %>"
/>

<div class="closed container-fluid-1280">
	<liferay-ui:search-container
		emptyResultsMessage="no-applications-were-found"
		total="<%= oAuth2ApplicationsCount %>"
	>

	    <liferay-ui:search-container-results
	        results="<%= OAuth2ApplicationServiceUtil.getOAuth2Applications(themeDisplay.getCompanyId(), searchContainer.getStart(), searchContainer.getEnd(), orderByComparator) %>"
		/>
	
	    <liferay-ui:search-container-row
	        className="com.liferay.oauth2.provider.model.OAuth2Application"
			escapedModel="true"
			modelVar="oAuth2Application"
		>
			<%
				String thumbnailURL = StringPool.BLANK;

				try {
					if (oAuth2Application.getIconFileEntryId() > 0) {
						FileEntry fileEntry = DLAppLocalServiceUtil.getFileEntry(oAuth2Application.getIconFileEntryId());
						thumbnailURL = DLUtil.getThumbnailSrc(fileEntry, themeDisplay);
					}
				}
				catch (PortalException e) {
				}

			%>
			<liferay-ui:search-container-column-image
				name="icon"
				src="<%= thumbnailURL %>"
			/>

			<liferay-ui:search-container-column-text
				href="<%= addOAuth2ApplicationURL %>"
				property="name"
			/>
	        
	        <liferay-ui:search-container-column-text
				property="description"
			/>

			<liferay-ui:search-container-column-text
				name="granted-authorizations"
				value="<%= String.valueOf(OAuth2AuthorizationServiceUtil.getApplicationOAuth2AuthorizationsCount(oAuth2Application.getOAuth2ApplicationId())) %>"
			/>

			<liferay-ui:search-container-column-text
				name="scopes"
			>
				<%
					int scopeAliasesSize = 0;
					String scopeAliases = "";

					if (oAuth2Application.getOAuth2ApplicationScopeAliasesId() > 0) {
						OAuth2ApplicationScopeAliases
							oAuth2ApplicationScopeAliases =
							OAuth2ApplicationScopeAliasesLocalServiceUtil.getOAuth2ApplicationScopeAliases(
								oAuth2Application.getOAuth2ApplicationScopeAliasesId());

						List<String> scopeAliasesList = oAuth2ApplicationScopeAliases.getScopeAliasesList();

						scopeAliasesSize = scopeAliasesList.size();
						scopeAliases = oAuth2ApplicationScopeAliases.getScopeAliases();
					}

					out.println(String.valueOf(scopeAliasesSize));
				%>

				<liferay-ui:icon-help message="<%= HtmlUtil.escapeAttribute(scopeAliases) %>" />

			</liferay-ui:search-container-column-text>

	        <liferay-ui:search-container-column-jsp
	            align="right" 
	            path="/admin/application_actions.jsp"
			/>

	    </liferay-ui:search-container-row>

		<liferay-ui:search-iterator
			markupView="lexicon"
		/>

	</liferay-ui:search-container>
</div>