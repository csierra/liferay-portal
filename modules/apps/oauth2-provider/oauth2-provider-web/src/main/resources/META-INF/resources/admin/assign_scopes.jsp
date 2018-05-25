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

<%
AssignScopesModel assignScopesModel = (AssignScopesModel)request.getAttribute(OAuth2ProviderWebKeys.ASSIGN_SCOPES_MODEL);

List<String> assignedScopes = Collections.emptyList();

if (oAuth2Application.getOAuth2ApplicationScopeAliasesId() > 0) {
	OAuth2ApplicationScopeAliases oAuth2ApplicationScopeAliases = OAuth2ApplicationScopeAliasesLocalServiceUtil.getOAuth2ApplicationScopeAliases(oAuth2Application.getOAuth2ApplicationScopeAliasesId());

	assignedScopes = oAuth2ApplicationScopeAliases.getScopeAliasesList();
}
%>

<div class="container-fluid container-fluid-max-xl container-view">
	<div class="row">
		<div class="col-lg-12">
			<portlet:actionURL name="/admin/assign_scopes" var="assignScopesURL">				
				<portlet:param name="appTab" value="scopes" />
				<portlet:param name="mvcRenderCommandName" value="/admin/assign_scopes" />
				<portlet:param name="oAuth2ApplicationId" value='<%= String.valueOf(oAuth2ApplicationId) %>' />
				<portlet:param name="uRLBack" value='<%= redirect %>'/>
			</portlet:actionURL>

			<aui:form action="<%= assignScopesURL %>" name="fm">
				<div class="sheet">
					<ul class="nav nav-underline" id="<portlet:namespace />navScopeTypes" role="tablist" style="display: none;">
						<li class="nav-item">
							<a aria-controls="<portlet:namespace />navResourceScopes" aria-expanded="true" class="active nav-link" data-toggle="tab" href="#<portlet:namespace />navResourceScopes" id="<portlet:namespace />navResourceScopesTab" role="tab">
								<liferay-ui:message key="resource-scopes" />
							</a>
						</li>
						<li class="nav-item">
							<a aria-controls="<portlet:namespace />navGlobalScopes" class="nav-link" data-toggle="tab" href="#<portlet:namespace />navGlobalScopes" id="<portlet:namespace />navGlobalScopesTab" role="tab">
								<liferay-ui:message key="global-scopes" />
							</a>
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
						<aui:button cssClass="btn-lg" href="<%= PortalUtil.escapeRedirect(redirect) %>" type="cancel" />
					</aui:button-row>
				</div>
			</aui:form>
		</div>
	</div>
</div>

<aui:script use="node,aui-modal,event-outside">

	if (A.all('#<portlet:namespace />navGlobalScopes .panel').size() > 0) {
		A.one('#<portlet:namespace />navScopeTypes').show();
	}

	var appsAccordion = A.one('#appsAccordion');

	var modal = new A.Modal(
		{
			centered: true,
			cssClass: 'assign-scopes-modal',
			visible: false,
			zIndex: Liferay.zIndex.OVERLAY,
			modal: true,
			width: 1000,
			bodyContent: '<div id="<portlet:namespace />modalBody"/>',
			headerContent: '<%= UnicodeLanguageUtil.get(request, "choose-one-of-the-following-global-scopes-that-include-this-resource-scope") %>'
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