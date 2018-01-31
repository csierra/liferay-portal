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
<%@ include file="init.jsp" %>
<%
String redirect = ParamUtil.getString(request, "redirect");

long oAuth2ApplicationId = ParamUtil.getLong(request, "oAuth2ApplicationId", -1);

OAuth2Application oAuth2Application = null;

if (oAuth2ApplicationId > -1) {
	oAuth2Application = OAuth2ApplicationServiceUtil.getOAuth2Application(oAuth2ApplicationId);
}

portletDisplay.setShowBackIcon(true);

portletDisplay.setURLBack(redirect);

renderResponse.setTitle(LanguageUtil.get(request, "assign-scopes"));

Map<String, List<LiferayOAuth2Scope>> scopes = (Map<String, List<LiferayOAuth2Scope>>) request.getAttribute(OAuth2AdminWebKeys.SCOPES);

List<String> assignedScopes = oAuth2Application.getScopesList();
%>

<div class="container-fluid-1280">
	<portlet:actionURL name="/admin/assign_scopes" var="updateScopesURL" />
	<aui:form action="<%= updateScopesURL %>" name="fm">

		<aui:fieldset-group markupView="lexicon">
			<aui:input type="hidden" name="oAuth2ApplicationId" value='<%= oAuth2ApplicationId %>' />

			<%
				for (Map.Entry<String, List<LiferayOAuth2Scope>> aliasedScopes : scopes.entrySet()) {
					String alias = aliasedScopes.getKey();
			%>
				<div>
					<aui:input checked="<%= assignedScopes.contains(alias) %>" label="<%= HtmlUtil.escape(alias) %>" localizeLabel="false" name="<%= "scope_" + alias %>" type="checkbox" />
				</div>
			<%
					List<LiferayOAuth2Scope> value = aliasedScopes.getValue();
					if (value.size() > 1) {
						%>
						<div>
							<ul>
								<%
								for (LiferayOAuth2Scope internalScope : value) {
									%>
										<li><%=internalScope.getApplicationName()%> -> <%=internalScope.getInternalScope()%></li>
									<%
								}
								%>
							</ul>
						</div>
						<%
					}

				}
			%>
		</aui:fieldset-group>

		<aui:button-row>
			<aui:button cssClass="btn-lg" type="submit" />
			<aui:button cssClass="btn-lg" onClick="<%= portletDisplay.getURLBack() %>" type="cancel" />
		</aui:button-row>
	</aui:form>
</div>