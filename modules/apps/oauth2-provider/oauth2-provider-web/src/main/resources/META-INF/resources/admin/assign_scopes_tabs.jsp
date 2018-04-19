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

<ul class="nav nav-underline" role="tablist">
	<li class="nav-item">
		<a aria-controls="navResourceScopes" aria-expanded="true" class="active nav-link" data-toggle="tab" href="#navResourceScopes" id="navResourceScopesTab" role="tab">Resource scopes</a>
	</li>
	<li class="nav-item">
		<a aria-controls="navGlobalScopes" class="nav-link" data-toggle="tab" href="#navGlobalScopes" id="navGlobalScopesTab" role="tab">Global scopes</a>
	</li>
</ul>

<div class="tab-content">
	<div aria-labelledby="navResourceScopesTab" class="active fade show tab-pane" id="navResourceScopes" role="tabpanel">
		<%@ include file="/admin/assign_scopes_tab1.jsp" %>
	</div>

	<div aria-labelledby="navGlobalScopesTab" class="fade tab-pane" id="navGlobalScopes" role="tabpanel">
		<%@ include file="/admin/assign_scopes_tab2.jsp" %>
	</div>
</div>