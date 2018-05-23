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

long oAuth2ApplicationId = ParamUtil.getLong(request, "oAuth2ApplicationId");

OAuth2Application oAuth2Application = null;

try {
	oAuth2Application = OAuth2ApplicationServiceUtil.getOAuth2Application(oAuth2ApplicationId);
}
catch (PrincipalException e) {
%>

	<liferay-ui:error-principal />

<%
	return;
}

portletDisplay.setShowBackIcon(true);

portletDisplay.setURLBack(redirect);

renderResponse.setTitle(LanguageUtil.get(request, "assign-scopes"));

AssignScopesModel assignScopesModel = (AssignScopesModel)request.getAttribute(OAuth2AdminWebKeys.ASSIGN_SCOPES_MODEL);

List<String> assignedScopes = Collections.emptyList();

if (oAuth2Application.getOAuth2ApplicationScopeAliasesId() > 0) {
	OAuth2ApplicationScopeAliases oAuth2ApplicationScopeAliases = OAuth2ApplicationScopeAliasesLocalServiceUtil.getOAuth2ApplicationScopeAliases(oAuth2Application.getOAuth2ApplicationScopeAliasesId());

	assignedScopes = oAuth2ApplicationScopeAliases.getScopeAliasesList();
}
%>

<div class="container-fluid container-fluid-max-xl container-view">
	<div class="row">
		<div class="col-lg-12">
			<portlet:actionURL name="/admin/assign_scopes" var="updateScopesURL" />

			<aui:form action="<%= updateScopesURL %>" name="fm">
				<aui:input name="oAuth2ApplicationId" type="hidden" value="<%= oAuth2ApplicationId %>" />

				<div class="sheet">
					<ul class="nav nav-underline" role="tablist">
						<li class="nav-item">
							<a aria-controls="<portlet:namespace />navResourceScopes" aria-expanded="true" class="active nav-link" data-toggle="tab" href="#<portlet:namespace />navResourceScopes" id="<portlet:namespace />navResourceScopesTab" role="tab">Resource scopes</a>
						</li>
						<li class="nav-item">
							<a aria-controls="<portlet:namespace />navGlobalScopes" class="nav-link" data-toggle="tab" href="#<portlet:namespace />navGlobalScopes" id="<portlet:namespace />navGlobalScopesTab" role="tab">Global scopes</a>
						</li>
					</ul>

					<div class="tab-content">
						<div aria-labelledby="navResourceScopesTab" class="active fade show tab-pane" id="<portlet:namespace />navResourceScopes" role="tabpanel">
							<%@ include file="/admin/assign_scopes_tab1.jspf" %>
						</div>

						<div aria-labelledby="navGlobalScopesTab" class="fade tab-pane" id="<portlet:namespace />navGlobalScopes" role="tabpanel">
							<%@ include file="/admin/assign_scopes_tab2.jspf" %>
						</div>
					</div>

					<aui:button-row>
						<aui:button cssClass="btn-lg" type="submit" />
						<aui:button cssClass="btn-lg" href="<%= portletDisplay.getURLBack() %>" type="cancel" />
					</aui:button-row>
				</div>
			</aui:form>
		</div>
	</div>
</div>

<aui:script use="node,aui-modal,event-outside">

	var appsAccordion = A.one('#appsAccordion');

	var modal = new A.Modal(
		{
			centered: true,
			visible: false,
			zIndex: Liferay.zIndex.OVERLAY,
			modal: true,
			width: 1000,
			bodyContent: '<div id="<portlet:namespace />modalBody"/>',
			headerContent: 'Choose one of the following global scopes that include this resource scope...'
		}
	).render();

	var handle;

	appsAccordion.delegate(
		'click',
		function(event) {

			event.stopPropagation();

			if (handle) {
				handle.detach();
				handle = null;
			}

			var currentTarget = event.currentTarget;

			if (currentTarget.attr("name")) {
				return true;
			}

			var scopeAliases = currentTarget.attr("data-slave").split(" ");

			$('#<portlet:namespace />globalAccordion .panel').hide();
			for (var i = 0; i < scopeAliases.length; i++) {
				$('#<portlet:namespace />globalAccordion #<portlet:namespace />' + $.escapeSelector(scopeAliases[i]) + '.panel').show();
			}

			$('#<portlet:namespace />globalAccordion').appendTo('#<portlet:namespace />modalBody');

			var boundingBox = modal.get('boundingBox');

			handle = boundingBox.once(
				'clickoutside',
				function() {
					modal.hide();
					$('#<portlet:namespace />globalAccordion .panel').show();
					$('#<portlet:namespace />globalAccordion').appendTo('#<portlet:namespace />navGlobalScopes');
				},
				modal);

			modal.show();

			event.preventDefault();

			return false;
		},
		'input[data-slave], a[data-slave]'
	);

	window.<portlet:namespace />recalculateDependants = function(checkboxElement) {

		var checkbox = $(checkboxElement);

		var value = checkbox.val();

		return $('input[data-slave]').filter(
			function() {
				return $.inArray(value, $(this).attr("data-slave").split(" ")) >= 0
			}).each(function() {

				var slave = $(this);

				var scopeAliases = slave.attr("data-slave").split(" ");

				var logicalOR = checkbox.prop('checked');

				for (var i = 0; i < scopeAliases.length; i++) {

					logicalOR = logicalOR || $('input[value="' + scopeAliases[i] + '"]:checked').length > 0;

					if (logicalOR) {
						slave.prop('checked', true);
						slave.prop('disabled', true);
						return;
					}
				}
				slave.prop('checked', false);

				if (slave.prop('name')) {
					slave.prop('disabled', false);
				}
			});
	}

	window.<portlet:namespace />recalculateAll = function() {

		var scopeAliasesCheckboxes = $('input[name="<portlet:namespace />scopeAliases"]');

		for (var i = 0; i < scopeAliasesCheckboxes.length; i++) {
			<portlet:namespace />recalculateDependants(scopeAliasesCheckboxes[i]);
		}
	}

	<portlet:namespace />recalculateAll();

</aui:script>