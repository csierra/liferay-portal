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

							<div aria-orientation="vertical" class="panel-group" id="appsAccordion" role="tablist">

			<%
				Map<String, String> applicationNamesDescriptions = assignScopesModel.getApplicationNamesDescriptions();

				List<Map.Entry<String, String>> applicationsNamesDescriptionsList =
					new ArrayList<>(applicationNamesDescriptions.entrySet());

				applicationsNamesDescriptionsList.sort(
					new Comparator<Map.Entry<String, String>>() {

						@Override
						public int compare(Map.Entry<String, String> entry1, Map.Entry<String, String> entry2) {
							return entry1.getValue().compareTo(entry2.getValue());
						}});

				for (Map.Entry<String, String> applicationNameDescriptionEntry : applicationsNamesDescriptionsList) {
					String applicationName = applicationNameDescriptionEntry.getKey();

					String applicationDescription = applicationNameDescriptionEntry.getValue();
					%>

								<div class="panel panel-secondary">
									<a aria-controls="appsAccordion<%= applicationName %>" aria-expanded="false" class="collapse-icon collapsed panel-header panel-header-link" data-parent="#appsAccordion" data-toggle="collapse" href="#appsAccordion<%= applicationName %>" id="appsAccordionHeading<%= applicationName %>" role="tab">
										<span class="panel-title"><%= HtmlUtil.escape(applicationDescription) %></span>
										<span class="collapse-icon-closed">
											<svg aria-hidden="true" class="lexicon-icon lexicon-icon-angle-right">
												<use xlink:href="/vendor/lexicon/icons.svg#angle-right" />
											</svg>
										</span>
										<span class="collapse-icon-open">
											<svg aria-hidden="true" class="lexicon-icon lexicon-icon-angle-down">
												<use xlink:href="/vendor/lexicon/icons.svg#angle-down" />
											</svg>
										</span>
									</a>

									<div aria-labelledby="appsAccordionHeading<%= applicationName %>" class="collapse panel-collapse" id="appsAccordion<%= applicationName %>" role="tabpanel">
										<div class="panel-body">
											<%@ include file="/admin/assign_scopes_tab1_app_scopes.jsp" %>
										</div>
									</div>
								</div>

					<%
				}
			%>

							</div>