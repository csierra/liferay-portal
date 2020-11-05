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

<%@ include file="/form_navigator/init.jsp" %>

<%
FormNavigatorDisplayContext formNavigatorDisplayContext = new FormNavigatorDisplayContext(request);

List<FormNavigatorEntry<Object>> formNavigatorEntries = (List<FormNavigatorEntry<Object>>)request.getAttribute(FormNavigatorWebKeys.FORM_NAVIGATOR_ENTRIES);
%>

<liferay-frontend:fieldset-group>

	<%
	final FormNavigatorEntry<Object> formNavigatorEntry = formNavigatorEntries.get(0);

	String sectionId = namespace + formNavigatorDisplayContext.getSectionId(formNavigatorEntry.getKey());

	String errorSection = null;
	%>

	<!-- Begin fragment <%= sectionId %> -->

	<liferay-frontend:fieldset
		collapsible="<%= formNavigatorEntries.size() > 1 %>"
		cssClass="<%= formNavigatorDisplayContext.getFieldSetCssClass() %>"
		id="<%= formNavigatorDisplayContext.getSectionId(formNavigatorEntry.getKey()) %>"
		label="<%= (formNavigatorEntries.size() > 1) ? formNavigatorEntry.getLabel(locale) : StringPool.BLANK %>"
	>

		<%
		PortalIncludeUtil.include(pageContext, formNavigatorEntry::include);

		errorSection = (String)request.getAttribute(WebKeys.ERROR_SECTION);

		if (Objects.equals(formNavigatorEntry.getKey(), errorSection)) {
			request.setAttribute(WebKeys.ERROR_SECTION, null);
		}
		%>

	</liferay-frontend:fieldset>

	<!-- End fragment <%= sectionId %> -->

	<%
	for (FormNavigatorEntry<Object> curFormNavigatorEntry : formNavigatorEntries) {
		sectionId = namespace + formNavigatorDisplayContext.getSectionId(curFormNavigatorEntry.getKey());
	%>

		<!-- Begin fragment <%= sectionId %> -->

		<liferay-frontend:fieldset
			collapsed="<%= true %>"
			collapsible="<%= true %>"
			cssClass="<%= formNavigatorDisplayContext.getFieldSetCssClass() %>"
			id="<%= formNavigatorDisplayContext.getSectionId(curFormNavigatorEntry.getKey()) %>"
			label="<%= curFormNavigatorEntry.getLabel(locale) %>"
		>

			<%
			PortalIncludeUtil.include(pageContext, curFormNavigatorEntry::include);
			%>

		</liferay-frontend:fieldset>

		<!-- End fragment <%= sectionId %> -->

	<%
		String curErrorSection = (String)request.getAttribute(WebKeys.ERROR_SECTION);

		if (Objects.equals(formNavigatorDisplayContext.getSectionId(curFormNavigatorEntry.getKey()), formNavigatorDisplayContext.getSectionId(curErrorSection))) {
			errorSection = curErrorSection;

			request.setAttribute(WebKeys.ERROR_SECTION, null);
		}
	}
	%>

	<%
	if (Validator.isNotNull(errorSection)) {
		String currentTab = (String)request.getAttribute(FormNavigatorWebKeys.CURRENT_TAB);

		request.setAttribute(FormNavigatorWebKeys.ERROR_TAB, currentTab);
	%>

		<aui:script sandbox="<%= true %>">
			var focusField;

			var sectionContent = document.querySelector(
				'#<%= formNavigatorDisplayContext.getSectionId(errorSection) %>Content'
			);

			<%
			String focusField = (String)request.getAttribute("liferay-ui:error:focusField");
			%>

			<c:choose>
				<c:when test="<%= Validator.isNotNull(focusField) %>">
					focusField = sectionContent.querySelector(
						'#<portlet:namespace /><%= focusField %>'
					);
				</c:when>
				<c:otherwise>
					focusField = sectionContent.querySelector('input:not([type="hidden"]).field');
				</c:otherwise>
			</c:choose>

			Liferay.once('<portlet:namespace />formReady', function (event) {
				if (!sectionContent.classList.contains('show')) {
					if (focusField) {
						Liferay.on('liferay.collapse.shown', function (event) {
							var panelId = event.panel.getAttribute('id');

							if (panelId === sectionContent.getAttribute('id')) {
								Liferay.Util.focusFormField(focusField);
							}
						});
					}

					Liferay.CollapseProvider.show({panel: sectionContent});
				}
				else if (focusField) {
					Liferay.Util.focusFormField(focusField);
				}
			});
		</aui:script>

	<%
	}
	%>

</liferay-frontend:fieldset-group>