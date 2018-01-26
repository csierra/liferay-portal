<%@ page import="com.liferay.oauth2.provider.model.LiferayOAuth2Scope" %>
<%@ page import="static com.liferay.oauth2.provider.web.internal.constants.OAuth2AdminWebKeys.SCOPES" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.liferay.portal.kernel.util.HtmlUtil" %>
<%@ page import="java.util.Arrays" %><%--
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

<%
	String redirect = ParamUtil.getString(request, "redirect");

	long oAuth2ApplicationId = ParamUtil.getLong(request, "oAuth2ApplicationId", -1);

	OAuth2Application oAuth2Application = null;

	if (oAuth2ApplicationId > -1) {
		oAuth2Application = OAuth2ApplicationLocalServiceUtil.getOAuth2Application(oAuth2ApplicationId);
	}

	portletDisplay.setShowBackIcon(true);

	portletDisplay.setURLBack(redirect);

	renderResponse.setTitle(LanguageUtil.get(request, "assign-scopes"));

	Map<String, List<LiferayOAuth2Scope>> scopes = (Map<String, List<LiferayOAuth2Scope>>) request.getAttribute(SCOPES);

	List<String> assignedScopes = Arrays.asList(oAuth2Application.getScopes().split(" "));
%>

<div class="container-fluid-1280">
	<portlet:actionURL name="/admin/assign_scopes" var="updateScopesURL" />
	<aui:form action="<%= updateScopesURL %>" name="fm">

		<aui:fieldset-group markupView="lexicon">
			<aui:input type="hidden" name="oAuth2ApplicationId" value='<%= oAuth2ApplicationId %>' />

			<%
				for (Map.Entry<String, List<LiferayOAuth2Scope>> applicationScopes : scopes.entrySet()) {
					String applicationName = applicationScopes.getKey();

			%>
			<aui:fieldset label="<%= HtmlUtil.escape(applicationName) %>" localizeLabel="false">
				<%
					for (LiferayOAuth2Scope scope : applicationScopes.getValue()) {
						String externalIdentifier = scope.toString();
						boolean checked = assignedScopes.contains(externalIdentifier);
				%>
					<div>
						<aui:input checked="<%= checked %>" label="<%= HtmlUtil.escape(scope.getScope()) %>" localizeLabel="false" name="<%= "scope_" + externalIdentifier %>" type="checkbox" />
					</div>
				<%
					}
				%>
			</aui:fieldset>
			<%
				}
			%>
		</aui:fieldset-group>

		<aui:button-row>
			<aui:button cssClass="btn-lg" type="submit" />
			<aui:button cssClass="btn-lg" onClick="<%= portletDisplay.getURLBack() %>" type="cancel" />
		</aui:button-row>
	</aui:form>
</div>