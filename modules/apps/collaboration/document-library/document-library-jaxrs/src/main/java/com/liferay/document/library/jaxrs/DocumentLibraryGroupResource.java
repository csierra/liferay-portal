/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.document.library.jaxrs;

import com.liferay.document.library.jaxrs.provider.Page;
import com.liferay.document.library.jaxrs.provider.Pagination;
import com.liferay.document.library.kernel.service.DLAppService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Carlos Sierra Andrés
 */
@Api
@Path("/")
@Component(immediate = true, service = DocumentLibraryGroupResource.class)
public class DocumentLibraryGroupResource {

	@Path("/")
	public ListGroupResource listGroups() {
		return new ListGroupResource();
	}

	@Path("/groups")
	public ListGroupResource alsoListGroups() {
		return new ListGroupResource();
	}

	@Path("/groups/{groupId}")
	public DocumentLibraryRepositoryResource getGroupResource(
		@PathParam("groupId") long groupId) {

		return new DocumentLibraryRepositoryResource(
			groupId, dlAppService, _request);
	}

	public class ListGroupResource {
		@GET
		@ApiOperation(
			value = "List groups", responseContainer = "List",
			response = GroupRepr.class
		)
		public Page<Group> listGroups(
			@Context Company company, @Context Pagination pagination) {
			try {
				List<Group> userSitesGroups =
					_groupService.getUserSitesGroups();

				int maxSize =
					pagination.getEndPosition() - pagination.getStartPosition();

				return pagination.createPage(
					userSitesGroups.
						stream().
						skip(pagination.getStartPosition()).
						limit(maxSize).
						collect(Collectors.toList()),
					userSitesGroups.size());
			}
			catch (PortalException e) {
				throw new WebApplicationException(Response.status(500).build());
			}
		}
	}

	@Reference
	protected DLAppService dlAppService;

	@Reference
	protected GroupService _groupService;

	@Context
	protected HttpServletRequest _request;


}
