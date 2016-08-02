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
import com.liferay.portal.kernel.model.Repository;
import com.liferay.portal.kernel.service.RepositoryService;
import io.swagger.annotations.Api;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Carlos Sierra Andrés
 */
@Api
@Component(immediate = true, service = RepositoryResource.class)
public class RepositoryResource {

	@GET
	public Repository getRepositories(
		@Context Company company, @Context Pagination pagination) {

		try {
			List<Repository> repositories =
				repositoryService.
					repositoryId, 0, 0, false, pagination.getStartPosition(),
					pagination.getEndPosition());

			int foldersAndFileEntriesAndFileShortcutsCount =
				dlAppService.getFoldersAndFileEntriesAndFileShortcutsCount(
					repositoryId, 0, 0, false);

			return pagination.createPage(
				foldersAndFileEntriesAndFileShortcuts.stream().map(RepositoryContentObject::createContentObject).map(
					repositoryContentObject -> {repositoryContentObject.setUrl(
					_request.getScheme() + "://" + _request.getServerName() +
					":" + _request.getServerPort() +
					_request.getContextPath() + "/document-library/"); return repositoryContentObject;}
			).collect(
					Collectors.toList()), foldersAndFileEntriesAndFileShortcutsCount);

		}
		catch (PortalException e) {
			e.printStackTrace();
		}

		return null;
	}

	@GET
	@Path("/folder/{repositoryId}/{folderId}")
	public Page<RepositoryContentObject> getFolderRepositoryContentObject(
		@Context Company company, @Context Pagination pagination, @PathParam("repositoryId") long repositoryId,
		@PathParam("folderId") long folderId) {

		try {
			List<Object> foldersAndFileEntriesAndFileShortcuts =
				dlAppService.getFoldersAndFileEntriesAndFileShortcuts(
					repositoryId, folderId, 0, false, pagination.getStartPosition(),
					pagination.getEndPosition());

			int foldersAndFileEntriesAndFileShortcutsCount =
				dlAppService.getFoldersAndFileEntriesAndFileShortcutsCount(
					repositoryId, folderId, 0, false);

			return pagination.createPage(
				foldersAndFileEntriesAndFileShortcuts.stream().map(RepositoryContentObject::createContentObject).map(
					repositoryContentObject -> {repositoryContentObject.setUrl(
					_request.getScheme() + "://" + _request.getServerName() +
					":" + _request.getServerPort() +
					_request.getContextPath() + "/document-library/"); return repositoryContentObject;}
			).collect(
					Collectors.toList()), foldersAndFileEntriesAndFileShortcutsCount);

		}
		catch (PortalException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Reference
	protected RepositoryService repositoryService;

	@Context
	protected HttpServletRequest _request;


}
