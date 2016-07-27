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
public class DocumentLibraryRepositoryResource {
	private long _groupId;
	private DLAppService _dlAppService;
	private HttpServletRequest _request;

	public DocumentLibraryRepositoryResource(
		long groupId, DLAppService dlAppService,
		HttpServletRequest request) {

		_groupId = groupId;
		_dlAppService = dlAppService;
		_request = request;
	}

	@Path("/")
	public RepositoryHandler getDefaultRepositoryHandler() {
		return new RepositoryHandler(_groupId);
	}

	@Path("/{repositoryId}")
	public RepositoryHandler getRepositoryHandler(
		@PathParam("repositoryId") long repositoryId) {

		return new RepositoryHandler(repositoryId);
	}

	public class RepositoryHandler {
		long _repositoryId;

		public RepositoryHandler(long repositoryId) {
			_repositoryId = repositoryId;
		}

		@GET
		public Page<RepositoryContentObject> getRootRepositoryContentObject(
			@Context Company company, @Context Pagination pagination) {

			try {
				List<Object> foldersAndFileEntriesAndFileShortcuts =
					_dlAppService.getFoldersAndFileEntriesAndFileShortcuts(
						_repositoryId, 0, 0, false, pagination.getStartPosition(),
						pagination.getEndPosition());

				int foldersAndFileEntriesAndFileShortcutsCount =
					_dlAppService.getFoldersAndFileEntriesAndFileShortcutsCount(
						_repositoryId, 0, 0, false);

				List<RepositoryContentObject> items =
					foldersAndFileEntriesAndFileShortcuts.stream().map(
						RepositoryContentObject::createContentObject).map(
						repositoryContentObject -> {
							repositoryContentObject.setUrl(
								_request.getScheme() + "://" +
								_request.getServerName() +
								":" +
								_request.getServerPort() +
								_request.getContextPath() +
								"/api/objects/" +
								repositoryContentObject.getUuid());
							return repositoryContentObject;
						}
					).collect(Collectors.toList());

				return pagination.createPage(
					items, foldersAndFileEntriesAndFileShortcutsCount);

			}
			catch (PortalException e) {
				e.printStackTrace();
			}

			return null;
		}
	}

}
