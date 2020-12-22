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

<%@ include file="/polls/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect");

PollsQuestion question = (PollsQuestion)request.getAttribute(PollsWebKeys.POLLS_QUESTION);

UserInputString questionTitle = question.getTitle(locale);

UserInputString questionDescription = question.getDescription(locale);

List<PollsChoice> choices = PollsChoiceLocalServiceUtil.getChoices(question.getQuestionId());

boolean hasVoted = PollsUtil.hasVoted(request, question.getQuestionId());

boolean viewResults = ParamUtil.getBoolean(request, "viewResults");

if (viewResults && !PollsQuestionPermission.contains(permissionChecker, question, ActionKeys.UPDATE)) {
	viewResults = false;
}

renderResponse.setTitle(questionTitle.unsafeGetString());

portletDisplay.setShowBackIcon(true);
portletDisplay.setURLBack(redirect);
%>

<portlet:actionURL name="/polls/view_question" var="viewQuestionActionURL">
	<portlet:param name="mvcActionCommand" value="/polls/view_question" />
</portlet:actionURL>

<aui:form action="<%= viewQuestionActionURL %>" cssClass="container-fluid container-fluid-max-xl" method="post" name="fm">
	<portlet:renderURL var="viewQuestionRenderURL">
		<portlet:param name="mvcRenderCommandName" value="/polls/view_question" />
		<portlet:param name="redirect" value="<%= redirect %>" />
		<portlet:param name="questionId" value="<%= String.valueOf(question.getQuestionId()) %>" />
	</portlet:renderURL>

	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.VOTE %>" />
	<aui:input name="redirect" type="hidden" value="<%= viewQuestionRenderURL %>" />
	<aui:input name="questionId" type="hidden" value="<%= question.getQuestionId() %>" />

	<liferay-ui:error exception="<%= DuplicateVoteException.class %>" message="you-may-only-vote-once" />
	<liferay-ui:error exception="<%= NoSuchChoiceException.class %>" message="please-select-an-option" />

	<aui:fieldset-group markupView="lexicon">
		<aui:fieldset>
			<h2>
				<%= StringUtil.replace(questionDescription.toString(), CharPool.NEW_LINE, "<br />") %>
			</h2>

			<c:choose>
				<c:when test="<%= !viewResults && !question.isExpired() && !hasVoted && PollsQuestionPermission.contains(permissionChecker, question, ActionKeys.ADD_VOTE) %>">

					<%
					for (PollsChoice choice : choices) {
						UserInputString choiceDescription = choice.getDescription(locale);
					%>

						<aui:field-wrapper cssClass="radio">
							<aui:input label='<%= choice.getName() + ". " + choiceDescription.unsafeGetString() %>' name="choiceId" type="radio" value="<%= choice.getChoiceId() %>" />
						</aui:field-wrapper>

					<%
					}
					%>

					<c:if test="<%= PollsQuestionPermission.contains(permissionChecker, question, ActionKeys.UPDATE) %>">
						<portlet:renderURL var="viewResultsURL">
							<portlet:param name="mvcRenderCommandName" value="/polls/view_question" />
							<portlet:param name="redirect" value="<%= redirect %>" />
							<portlet:param name="questionId" value="<%= String.valueOf(question.getQuestionId()) %>" />
							<portlet:param name="viewResults" value="1" />
						</portlet:renderURL>

						<liferay-ui:icon
							icon="search"
							label="<%= true %>"
							markupView="lexicon"
							message="view-results"
							url="<%= viewResultsURL %>"
						/>
					</c:if>

					<aui:button-row>
						<aui:button type="submit" value="vote[action]" />

						<aui:button href="<%= redirect %>" type="cancel" />
					</aui:button-row>

					<%
					PortalUtil.addPortletBreadcrumbEntry(request, questionTitle.unsafeGetString(), currentURL);
					%>

				</c:when>
				<c:otherwise>
					<%@ include file="/polls/view_question_results.jspf" %>

					<portlet:renderURL var="viewQuestionURL">
						<portlet:param name="mvcRenderCommandName" value="/polls/view_question" />
						<portlet:param name="redirect" value="<%= redirect %>" />
						<portlet:param name="questionId" value="<%= String.valueOf(question.getQuestionId()) %>" />
					</portlet:renderURL>

					<div class="button-holder">
						<c:choose>
							<c:when test="<%= !question.isExpired() && !hasVoted && PollsQuestionPermission.contains(permissionChecker, question, ActionKeys.ADD_VOTE) %>">
								<aui:button href="<%= viewQuestionURL %>" value="back-to-vote" />
							</c:when>
						</c:choose>
					</div>

					<%
					PortalUtil.addPortletBreadcrumbEntry(request, questionTitle.unsafeGetString(), viewQuestionURL.toString());
					PortalUtil.addPortletBreadcrumbEntry(request, LanguageUtil.get(request, "results"), currentURL);
					%>

				</c:otherwise>
			</c:choose>
		</aui:fieldset>
	</aui:fieldset-group>
</aui:form>