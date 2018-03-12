<%@ page import="com.liferay.oauth2.provider.constants.GrantType" %>
<%@ page import="com.liferay.portal.kernel.util.StringUtil" %><%--
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

long oAuth2ApplicationId = ParamUtil.getLong(request, "oAuth2ApplicationId", -1);

OAuth2Application oAuth2Application = null;

if (oAuth2ApplicationId > -1) {
		oAuth2Application = OAuth2ApplicationServiceUtil.getOAuth2Application(oAuth2ApplicationId);
}

String headerTitle = (oAuth2Application == null) ? LanguageUtil.get(request, "add-o-auth2-application") : LanguageUtil.format(request, "edit-x", oAuth2Application.getName(), false);

renderResponse.setTitle(headerTitle);
%>

<div class="container-fluid-1280">
	<portlet:actionURL name='<%= oAuth2Application == null ? "updateOAuth2Application" : "updateOAuth2Application" %>' var="editOAuth2ApplicationURL" />

	<aui:form action="<%= editOAuth2ApplicationURL %>" name="fm">
		<liferay-ui:error exception="<%= DuplicateOAuth2ClientIdException.class %>" message="client-id-already-exists" />

		<aui:model-context bean="<%= oAuth2Application %>" model="<%= OAuth2Application.class %>" />

		<aui:fieldset-group markupView="lexicon">
			<aui:input type="hidden" name="oAuth2ApplicationId"
				value='<%= oAuth2Application == null ? "" : oAuth2Application.getOAuth2ApplicationId() %>'
			/>

			<aui:fieldset label="details">
				<aui:input name="name" required="true" />
				<aui:field-wrapper>
					<aui:input label="icon" name="icon" type="file" inlineField="true"/>
					<c:if test="<%= oAuth2Application != null && oAuth2Application.getIconFileEntryId() > 0%>">
						<%
							String thumbnailURL = StringPool.BLANK;

							try {
								FileEntry fileEntry = DLAppLocalServiceUtil.getFileEntry(oAuth2Application.getIconFileEntryId());
								thumbnailURL = DLUtil.getThumbnailSrc(fileEntry, themeDisplay);
							}
							catch (PortalException e) {
								// user has no longer access to the application
							}
						%>
						<img src="<%= thumbnailURL %>" width="64" />
					</c:if>
				</aui:field-wrapper>
				<aui:input name="description" type="textarea" />
				<aui:input name="homePageURL" />
				<aui:input name="privacyPolicyURL" />
				<aui:input name="clientConfidential" type="checkbox" value="<%= oAuth2Application == null ? true : oAuth2Application.getClientConfidential() %>"/>
				<aui:input name="clientId" required="true" />
				<aui:input name="clientSecret" />
				<aui:input label="redirect-uris" name="redirectURIs" helpMessage="redirect-uris-help" />
				<aui:fieldset label="allowed-grant-types">
					<%
						for (GrantType grantType : GrantType.values()) {
							String grantTypeName = grantType.name();
					%>
					<aui:input name="allowedGrantTypeNames" type="checkbox" label="<%= grantTypeName + "-grant" %>" value="<%= grantTypeName %>" checked="<%= oAuth2Application != null && oAuth2Application.getAllowedGrantTypesList().contains(grantType) %>" />
					<%
						}
					%>
				</aui:fieldset>
				<aui:fieldset label="supported-features">
					<%
						String[] oAuth2Features = StringUtil.split(portletPreferences.getValue("oAuth2Features", StringPool.BLANK));

						for (String oAuth2Feature : oAuth2Features) {
							String escapedOAuth2Feature = HtmlUtil.escapeAttribute(oAuth2Feature);
					%>
					<aui:input name="<%= "feature-" + escapedOAuth2Feature %>" type="checkbox" label="<%= escapedOAuth2Feature %>" value="true" checked="<%= oAuth2Application != null && oAuth2Application.getFeaturesList().contains(oAuth2Feature) %>" />
					<%
						}
					%>
				</aui:fieldset>
			</aui:fieldset>
		</aui:fieldset-group>

		<aui:button-row>
			<aui:button cssClass="btn-lg" type="submit" />
			<aui:button cssClass="btn-lg" href="<%= portletDisplay.getURLBack() %>" type="cancel" />
		</aui:button-row>
	</aui:form>
</div>