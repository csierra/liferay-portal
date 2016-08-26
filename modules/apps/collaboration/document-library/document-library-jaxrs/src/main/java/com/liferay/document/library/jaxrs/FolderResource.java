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

import com.liferay.document.library.jaxrs.provider.OrderByComparatorSelectorUtil;
import com.liferay.document.library.jaxrs.provider.OrderBySelector;
import com.liferay.document.library.jaxrs.provider.PageContainer;
import com.liferay.document.library.jaxrs.provider.Pagination;
import com.liferay.document.library.kernel.service.DLAppService;
import com.liferay.document.library.kernel.util.comparator.RepositoryModelTitleComparator;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.repository.Repository;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.FileShortcut;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.function.BiFunction;

/**
 * @author Carlos Sierra Andrés
 */
public class FolderResource {

	public FolderResource(
		DLAppService dlAppService, final Folder folder) {

		_dlAppService = dlAppService;
		_folderId = folder.getFolderId();

		_repositoryId = folder.getRepositoryId();
		_folderReprFunction =
			(uriInfo, contents) -> FolderRepr.fromFolder(
				folder, contents, uriInfo.getBaseUriBuilder());
	}

	public FolderResource(
		DLAppService dlAppService, final long groupId,
		final Repository repository) {

		_dlAppService = dlAppService;

		_repositoryId = repository.getRepositoryId();
		_folderId = 0;
		_folderReprFunction =
			(uriInfo, contents) -> FolderRepr.fromRepository(
				groupId, repository, contents,
				uriInfo.getBaseUriBuilder());
	}

	@POST
	public FileRepr addFile(
		@Context UriInfo uriInfo,
		@Multipart("metadata") FileRepr fileRepr,
		@Multipart("content") Attachment attachment,
		@Multipart(value = "changelog", required = false)
		String changelog)
		throws PortalException {

		if ((fileRepr == null) || (attachment == null)) {
			throw new WebApplicationException(
				Response.
					status(400).
					entity("Request is not properly built" +
						   "We expect a multipart/form-data request with" +
						   "metadata and content fields").build());
		}

		changelog = GetterUtil.getString(changelog);

		return FileRepr.fromFileEntry(
			_dlAppService.addFileEntry(
				_repositoryId, _folderId,
				fileRepr.getFileName(),
				attachment.getContentType().getType(), fileRepr.getTitle(),
				fileRepr.getDescription(), changelog,
				attachment.getObject(byte[].class), new ServiceContext()),
			uriInfo.getRequestUriBuilder().path("{id}"));
	}

	@POST
	@Path("/{fileName}")
	public FileRepr addFile(
			@Context HttpServletRequest httpServletRequest,
			@Context UriInfo uriInfo,
			byte[] content, @PathParam("fileName") String fileName,
			@QueryParam("changelog") String changelog,
			@QueryParam("title") String title,
			@QueryParam("description") String description)
		throws PortalException {

		if ((fileName == null) || (content == null)) {
			throw new WebApplicationException(
				Response.
					status(400).
					entity("Request is not properly built").build());
		}

		changelog = GetterUtil.getString(changelog);
		title = GetterUtil.getString(title, fileName);
		description = GetterUtil.getString(description, fileName);

		return FileRepr.fromFileEntry(
			_dlAppService.addFileEntry(
				_repositoryId, _folderId,
				fileName,
				httpServletRequest.getContentType(), title, description,
				changelog, content, new ServiceContext()),
			uriInfo.getRequestUriBuilder().path("{id}"));
	}

	@GET
	public PageContainer<FolderRepr> getFolder(
		@Context UriInfo uriInfo,
		@Context Pagination pagination,
		@Context OrderBySelector orderBySelector)
		throws PortalException {

		OrderByComparator<Object> orderByComparator =
			OrderByComparatorSelectorUtil.select(
				orderBySelector, RepositoryContentObject.comparators).
				orElse(new RepositoryModelTitleComparator<>());

		UriBuilder baseUriBuilder = uriInfo.getBaseUriBuilder().path("objects");

		List<RepositoryContentObject> repositoryContentObjects =
			ListUtil.toList(
				_dlAppService.getFoldersAndFileEntriesAndFileShortcuts(
					_repositoryId, _folderId, 0, true,
					pagination.getStartPosition(), pagination.getEndPosition(),
					orderByComparator),
				o -> toObjectRepository(o, baseUriBuilder.clone()));


		return pagination.createContainer(
			_folderReprFunction.apply(uriInfo, repositoryContentObjects),
			_dlAppService.getFoldersAndFileEntriesAndFileShortcutsCount(
				_repositoryId, _folderId, 0, true)
		);
	}

	protected RepositoryContentObject toObjectRepository(
		Object rco, UriBuilder uriBuilder) {

		if (rco instanceof FileEntry) {
			FileEntry fileEntry = (FileEntry) rco;

			String url = uriBuilder.path("files").
				path(Long.toString(fileEntry.getFileEntryId())).
				build().
				toString();

			return new RepositoryContentObject(
				fileEntry.getFileEntryId(), fileEntry.getTitle(), url,
				RepositoryContentObject.RepositoryContentType.FILE);

		}
		else if (rco instanceof Folder) {
			Folder folder = (Folder) rco;

			String url = uriBuilder.path("folders").
				path(Long.toString(folder.getFolderId())).
				build().
				toString();

			return new RepositoryContentObject(
				folder.getFolderId(), folder.getName(), url,
				RepositoryContentObject.RepositoryContentType.FOLDER);
		}
		else if (rco instanceof FileShortcut) {
			FileShortcut fileShortcut = (FileShortcut) rco;

			String url = uriBuilder.path("files").
				path(Long.toString(fileShortcut.getToFileEntryId())).
				build().
				toString();

			return new RepositoryContentObject(
				fileShortcut.getFileShortcutId(), fileShortcut.getToTitle(),
				url, RepositoryContentObject.RepositoryContentType.SHORTCUT);
		}
		else {
			throw new IllegalArgumentException(
				"Object must be an instance of FileEntry, Folder of FileShortcut");
		}
	}


	private final long _folderId;

	public BiFunction<UriInfo, List<RepositoryContentObject>, FolderRepr>
		_folderReprFunction;
	private final long _repositoryId;
	private DLAppService _dlAppService;

}
