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

<%@ include file="/connected_applications/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect");

portletDisplay.setShowBackIcon(true);
portletDisplay.setURLBack(redirect);

OAuth2Application oAuth2Application = oAuth2ConnectionApplicationsPortletDisplayContext.getOAuth2Application();

renderResponse.setTitle(oAuth2Application.getName());

AuthorizationModel authorizationModel = oAuth2ConnectionApplicationsPortletDisplayContext.getAuthorizationModel();
%>

<div class="container-fluid-1280 view-application">
	<portlet:actionURL name="revokeOAuth2Authorization" var="revokeOAuth2AuthorizationURL" />

	<aui:form action="<%= revokeOAuth2AuthorizationURL %>" method="post" name="fm">
		<aui:input name="oAuth2AuthorizationId" type="hidden" value='<%= ParamUtil.getString(request, "oAuth2AuthorizationId") %>' />
		<aui:input name="redirect" type="hidden" value="<%= redirect %>" />

		<aui:fieldset-group markupView="lexicon">
			<div class="panel-body">
				<liferay-ui:icon
					cssClass="app-icon"
					src="<%= oAuth2ConnectionApplicationsPortletDisplayContext.getThumbnailURL() %>"
				/>

				<h1 class="name">
					<%= HtmlUtil.escape(oAuth2Application.getName()) %>
				</h1>

				<p class="description text-truncate">
					<%= HtmlUtil.escape(oAuth2Application.getDescription()) %>
				</p>

				<c:if test="<%= !Validator.isBlank(oAuth2Application.getHomePageURL()) || !Validator.isBlank(oAuth2Application.getPrivacyPolicyURL()) %>">
					<p class="application-information text-truncate">
						<span><liferay-ui:message key="application-information" /></span>:

						<c:if test="<%= !Validator.isBlank(oAuth2Application.getHomePageURL()) %>">
							<aui:a href="<%= HtmlUtil.escapeJSLink(oAuth2Application.getHomePageURL()) %>" label="website" target="_blank" />
						</c:if>

						<c:if test="<%= !Validator.isBlank(oAuth2Application.getPrivacyPolicyURL()) %>">
							<c:if test="<%= !Validator.isBlank(oAuth2Application.getHomePageURL()) %>">
								<%= StringUtil.toLowerCase(LanguageUtil.get(request, "and")) %>
							</c:if>

							<aui:a href="<%= HtmlUtil.escapeJSLink(oAuth2Application.getPrivacyPolicyURL()) %>" label="privacy-policy" target="_blank" />
						</c:if>
					</p>
				</c:if>

				<h4 class="permissions">
					<liferay-ui:message key="permissions" />:
				</h4>

				<ul class="list-group">

					<%
					for (String applicationName : authorizationModel.getApplicationNames()) {
						String applicationScopeDescription = StringUtil.merge(authorizationModel.getApplicationScopeDescription(applicationName), ", ");
					%>

						<li class="list-group-item list-group-item-flex">
							<div class="autofit-col">
								<clay:icon
									symbol="check"
								/>
							</div>

							<div class="autofit-col autofit-col-expand">
								<h4 class="list-group-title text-truncate"><%= HtmlUtil.escape(authorizationModel.getApplicationDescription(applicationName)) %></h4>
								<p class="list-group-subtitle text-truncate"><%= applicationScopeDescription %></p>
							</div>
						</li>

					<%
					}
					%>

				</ul>

				<h4 class="activity">
					<liferay-ui:message key="activity" />
				</h4>

				<p class="last-access text-truncate">
					<span><liferay-ui:message key="last-access" /></span>:
				</p>

				<p class="authorization text-truncate">
					<span><liferay-ui:message key="authorization" /></span>:
				</p>

				<p class="buttons">
					<aui:button cssClass="remove-access" id="removeAccess" value="remove-access" />
					<aui:button href="<%= PortalUtil.escapeRedirect(redirect) %>" value="cancel" />
				</p>

				<aui:script>
					$('#<portlet:namespace />removeAccess').on(
						'click',
						function() {
							if (confirm('<%= UnicodeLanguageUtil.format(request, "x-will-no-longer-have-access-to-your-account-removed-access-cannot-be-recovered", new String[] {oAuth2Application.getName()}) %>')) {
								document.<portlet:namespace/>fm.submit();
							}
						}
					);
				</aui:script>
			</div>
		</aui:fieldset-group>
	</aui:form>
</div>