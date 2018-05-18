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

long oAuth2ApplicationId = ParamUtil.getLong(request, "oAuth2ApplicationId", -1);

OAuth2Application oAuth2Application = null;

if (oAuth2ApplicationId > -1) {
		oAuth2Application = OAuth2ApplicationServiceUtil.getOAuth2Application(oAuth2ApplicationId);
}

String headerTitle = (oAuth2Application == null) ? LanguageUtil.get(request, "add-o-auth2-application") : LanguageUtil.format(request, "edit-x", oAuth2Application.getName(), false);

renderResponse.setTitle(headerTitle);
%>

<portlet:actionURL name='<%= oAuth2Application == null ? "updateOAuth2Application" : "updateOAuth2Application" %>' var="editOAuth2ApplicationURL" />

<aui:form action="<%= editOAuth2ApplicationURL %>" name="fm">
	<div class="container-fluid container-fluid-max-xl container-view">
		<div class="sheet">
			<div class="row">
				<div class="col-lg-12">
					<liferay-ui:error exception="<%= DuplicateOAuth2ApplicationClientIdException.class %>" focusField="clientId" message="client-id-already-exists" />
					<liferay-ui:error exception="<%= OAuth2ApplicationHomePageURLException.class %>" focusField="homePageURL" message="home-page-url-is-invalid" />
					<liferay-ui:error exception="<%= OAuth2ApplicationHomePageURLSchemeException.class %>" focusField="homePageURL" message="home-page-url-scheme-is-invalid" />
					<liferay-ui:error exception="<%= OAuth2ApplicationNameException.class %>" focusField="name" message="missing-application-name" />
					<liferay-ui:error exception="<%= OAuth2ApplicationPrivacyPolicyURLException.class %>" focusField="privacyPolicyURL" message="privacy-policy-url-is-invalid" />

					<liferay-ui:error exception="<%= OAuth2ApplicationPrivacyPolicyURLSchemeException.class %>" focusField="privacyPolicyURL">
						<liferay-ui:message arguments="<%= HtmlUtil.escape(((OAuth2ApplicationPrivacyPolicyURLSchemeException)errorException).getMessage()) %>" key="privacy-policy-url-scheme-is-invalid" />
					</liferay-ui:error>

					<liferay-ui:error exception="<%= OAuth2ApplicationRedirectURIException.class %>" focusField="redirectURIs">
						<liferay-ui:message arguments="<%= HtmlUtil.escape(((OAuth2ApplicationRedirectURIException)errorException).getMessage()) %>" key="redirect-uri-x-is-invalid" />
					</liferay-ui:error>

					<liferay-ui:error exception="<%= OAuth2ApplicationRedirectURIMissingException.class %>" focusField="redirectURIs">
						<liferay-ui:message arguments="<%= HtmlUtil.escape(((OAuth2ApplicationRedirectURIMissingException)errorException).getMessage()) %>" key="redirect-uri-is-missing-for-grant-type-x" />
					</liferay-ui:error>

					<liferay-ui:error exception="<%= OAuth2ApplicationRedirectURIFragmentException.class %>" focusField="redirectURIs">
						<liferay-ui:message arguments="<%= HtmlUtil.escape(((OAuth2ApplicationRedirectURIFragmentException)errorException).getMessage()) %>" key="redirect-uri-x-fragment-is-invalid" />
					</liferay-ui:error>

					<liferay-ui:error exception="<%= OAuth2ApplicationRedirectURIPathException.class %>" focusField="redirectURIs">
						<liferay-ui:message arguments="<%= HtmlUtil.escape(((OAuth2ApplicationRedirectURIPathException)errorException).getMessage()) %>" key="redirect-uri-x-path-is-invalid" />
					</liferay-ui:error>

					<liferay-ui:error exception="<%= OAuth2ApplicationRedirectURISchemeException.class %>" focusField="redirectURIs">
						<liferay-ui:message arguments="<%= HtmlUtil.escape(((OAuth2ApplicationRedirectURISchemeException)errorException).getMessage()) %>" key="redirect-uri-x-scheme-is-invalid" />
					</liferay-ui:error>

					<liferay-ui:error exception="<%= OAuth2ApplicationClientGrantTypeException.class %>">
						<liferay-ui:message arguments="<%= HtmlUtil.escape(((OAuth2ApplicationClientGrantTypeException)errorException).getMessage()) %>" key="grant-type-x-is-unsupported-for-this-client-type" />
					</liferay-ui:error>

					<aui:model-context bean="<%= oAuth2Application %>" model="<%= OAuth2Application.class %>" />

					<aui:input name="oAuth2ApplicationId" type="hidden" value='<%= oAuth2Application == null ? "" : oAuth2Application.getOAuth2ApplicationId() %>' />

					<aui:fieldset>
						<aui:input name="clientId" required="<%= true %>" />
						<aui:input name="clientSecret" />
					</aui:fieldset>
				</div>
			</div>

			<div class="row">
				<div class="col-lg-9">
					<aui:fieldset>
						<aui:input name="name" required="<%= true %>" />

						<aui:input name="homePageURL" />

						<aui:input name="description" type="textarea" />

						<aui:input helpMessage="redirect-uris-help" label="redirect-uris" name="redirectURIs" />

						<aui:input name="privacyPolicyURL" />

						<aui:select name="clientProfile">
							<aui:option label="client-profile-0" selected="<%= oAuth2Application == null ? true : oAuth2Application.getClientProfile() == 0 %>" value="0" />
						</aui:select>

						<aui:fieldset label="allowed-grant-types">

							<%
							List<GrantType> oAuth2Grants = oAuth2AdminPortletDisplayContext.getOAuth2Grants(portletPreferences);

							for (GrantType grantType : oAuth2Grants) {
								String grantTypeName = grantType.name();
							%>

								<aui:input checked="<%= oAuth2Application != null && oAuth2Application.getAllowedGrantTypesList().contains(grantType) %>" label="<%= grantTypeName %>" name='<%= "grant-" + grantTypeName %>' type="checkbox" />

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

								<aui:input checked="<%= oAuth2Application != null && oAuth2Application.getFeaturesList().contains(oAuth2Feature) %>" label="<%= escapedOAuth2Feature %>" name='<%= "feature-" + escapedOAuth2Feature %>' type="checkbox" />

							<%
							}
							%>

						</aui:fieldset>
					</aui:fieldset>

				</div>

				<div class="col-lg-3">
					<aui:fieldset label="icon">
						<c:if test="<%= oAuth2Application != null %>">
							<%
								String thumbnailURL = StringPool.BLANK;
								if (oAuth2Application.getIconFileEntryId() > 0) {
									try {
										FileEntry fileEntry =
											DLAppLocalServiceUtil.getFileEntry(
												oAuth2Application.getIconFileEntryId());
										thumbnailURL =
											DLUtil.getThumbnailSrc(fileEntry,
												themeDisplay);
									}
									catch (PortalException e) {

										// user has no longer access to the application

									}
								}
							%>
							<c:choose>
								<c:when test="<%= oAuth2AdminPortletDisplayContext.hasUpdatePermission(oAuth2Application) %>">
									<liferay-ui:logo-selector
										currentLogoURL="<%= thumbnailURL %>"
										defaultLogo="<%= oAuth2Application.getIconFileEntryId() == 0 %>"
										tempImageFileName="<%= String.valueOf(oAuth2Application.getClientId()) %>"
									/>
								</c:when>
								<c:otherwise>
									<img alt="<liferay-ui:message escapeAttribute="<%= true %>" key="portrait" />" src="<%= thumbnailURL %>" />
								</c:otherwise>
							</c:choose>
						</c:if>
					</aui:fieldset>
				</div>
			</div>

			<div class="row">
				<div class="col-lg-12">
					<aui:button-row>
						<aui:button cssClass="btn-lg" type="submit" />
						<aui:button cssClass="btn-lg" href="<%= portletDisplay.getURLBack() %>" type="cancel" />
					</aui:button-row>
				</div>
			</div>
		</div>
	</div>
</aui:form>