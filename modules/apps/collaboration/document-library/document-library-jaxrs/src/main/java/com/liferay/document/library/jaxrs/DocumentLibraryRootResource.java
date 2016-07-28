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
import com.liferay.portal.kernel.repository.Repository;
import com.liferay.portal.kernel.repository.RepositoryProvider;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.service.GroupService;
import com.liferay.portal.kernel.service.RepositoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Carlos Sierra Andrés
 */
@Api
@Path("/")
@Component(immediate = true, service = DocumentLibraryRootResource.class)
public class DocumentLibraryRootResource {

	@GET
	@ApiOperation(
		value = "List groups", responseContainer = "List",
		response = GroupRepr.class
	)
	public Page<Group> listGroups(
			@Context Company company, @Context Pagination pagination)
		throws PortalException {

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

	@Path("/{groupId}")
	public DocumentLibraryGroupResource getGroupResource(
		@PathParam("groupId") long groupId) {

		return new DocumentLibraryGroupResource(groupId);
	}

	@Path("/objects/files/{fileId}")
	public FileRestResource getFileResource(@PathParam("fileId") long fileId)
		throws PortalException {

		return new FileRestResource(dlAppService.getFileEntry(fileId));
	}

	@Path("/objects/folders/{folderId}")
	public FolderResource getFolderResource(
			@PathParam("folderId") long folderId)
		throws PortalException {

		return new FolderResource(dlAppService.getFolder(folderId));
	}

	@Reference
	protected DLAppService dlAppService;

	@Reference
	protected GroupService _groupService;

	@Reference
	protected RepositoryProvider _repositoryProvider;

	@Reference
	protected RepositoryService _repositoryService;

	@Context
	protected HttpServletRequest _request;

	public class FileRestResource {
		private FileEntry _fileEntry;

		public FileRestResource(FileEntry fileEntry) {
			_fileEntry = fileEntry;
		}

		@GET
		public FileRepr getFileEntry() {
			return FileRepr.fromFileEntry(_fileEntry, _request);
		}

		@GET
		@Path("/content")
		public Response getFileEntryContent() throws PortalException {
			return Response.
				status(200).
				type(_fileEntry.getMimeType()).
				entity(_fileEntry.getContentStream()).
				build();
		}
	}

	public class FolderResource {

		private final long _repositoryId;
		private final long _folderId;
		public Function<List<RepositoryContentObject>, FolderRepr>
			_folderReprFunction;

		public FolderResource(final Folder folder) {
			_repositoryId = folder.getRepositoryId();
			_folderId = folder.getFolderId();
			_folderReprFunction =
				(contents) -> FolderRepr.fromFolder(folder, contents);
		}

		public FolderResource(final Repository repository) {
			_repositoryId = repository.getRepositoryId();
			_folderId = 0;
			_folderReprFunction =
				(contents) -> FolderRepr.fromRepository(repository, contents);
		}

		@GET
		public FolderRepr getFolder()
			throws PortalException {

			return _folderReprFunction.apply(
				dlAppService.getFoldersAndFileEntriesAndFileShortcuts(
				_repositoryId, _folderId, 0,
				true, -1, -1).
				stream().
				map(RepositoryContentObject::createContentObject).
				collect(Collectors.toList()));
		}
	}

	/**
	 * @author Carlos Sierra Andrés
	 */
	public class DocumentLibraryGroupResource {

		private long _groupId;

		public DocumentLibraryGroupResource(long groupId) {
			_groupId = groupId;
		}

		@GET
		@Path("/")
		public List<RepositoryRepr> getDefaultRepositoryHandler()
			throws PortalException {

			return _repositoryProvider.getGroupRepositories(_groupId).stream().
				map(r -> RepositoryRepr.fromRepository(r, _request)).
				collect(Collectors.toList());
		}

		@Path("/{repositoryId}")
		public FolderResource getRepositoryHandler(
				@PathParam("repositoryId") long repositoryId)
			throws PortalException {

			return new FolderResource(
				_repositoryProvider.getRepository(repositoryId));
		}

	}
}
