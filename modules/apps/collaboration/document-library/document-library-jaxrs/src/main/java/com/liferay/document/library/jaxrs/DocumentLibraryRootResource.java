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
import com.liferay.document.library.jaxrs.provider.VariantUtil;
import com.liferay.document.library.kernel.service.DLAppService;
import com.liferay.document.library.kernel.util.comparator.RepositoryModelTitleComparator;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.repository.Repository;
import com.liferay.portal.kernel.repository.RepositoryProvider;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.FileShortcut;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.service.GroupService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.comparator.GroupFriendlyURLComparator;
import com.liferay.portal.kernel.util.comparator.GroupIdComparator;
import com.liferay.portal.kernel.util.comparator.GroupNameComparator;
import com.liferay.portal.kernel.util.comparator.GroupTypeComparator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Variant;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
	public PageContainer<List<GroupRepr>> listGroups(
			@Context Request request, @Context Company company,
			@Context Pagination pagination,
			@Context OrderBySelector orderBySelector)
		throws PortalException {

		Variant.VariantListBuilder variantListBuilder =
			Variant.VariantListBuilder.newInstance();

		variantListBuilder =
			variantListBuilder.languages(
				LanguageUtil.getAvailableLocales().toArray(new Locale[0])).
				add();

		List<Variant> variants = variantListBuilder.build();

		Locale locale =
			VariantUtil.safeSelectVariant(request, variants).
				map(Variant::getLanguage).
				orElse(LocaleUtil.getDefault());

		OrderByComparator<Group> groupOrderByComparator =
			OrderByComparatorSelectorUtil.select(
				orderBySelector,
				new HashMap<String, Function<Boolean, OrderByComparator<Group>>>() {{
					put("name", GroupNameComparator::new);
					put("id", GroupIdComparator::new);
					put("type", GroupTypeComparator::new);
					put("url", GroupFriendlyURLComparator::new);
				}}).
				orElseGet(GroupIdComparator::new);

		List<Group> userSitesGroups =
			_groupService.getUserSitesGroups();

		int maxSize =
			pagination.getEndPosition() - pagination.getStartPosition();

		UriBuilder uriBuilder = _uriInfo.getBaseUriBuilder().path("{groupId}");

		return pagination.createContainer(
			userSitesGroups.
				stream().
				skip(pagination.getStartPosition()).
				limit(maxSize).
				sorted(groupOrderByComparator).
				map(group ->
					new GroupRepr(
						group.getGroupId(),
						uriBuilder.build(group.getGroupId()).toString(),
						group.getName(locale, true), group.getFriendlyURL(),
						group.getType())).
				collect(Collectors.toList()),
			userSitesGroups.size());
	}

	@Path("/{groupId}")
	public DocumentLibraryGroupResource getGroupResource(
		@PathParam("groupId") long groupId) {

		return new DocumentLibraryGroupResource(groupId);
	}

	@Path("/objects/files/{fileId}")
	public FileResource getFileResource(@PathParam("fileId") long fileId)
		throws PortalException {

		return new FileResource(dlAppService.getFileEntry(fileId));
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

	@Context
	protected HttpServletRequest _request;

	@Context
	protected UriInfo _uriInfo;

	public class FileResource {
		private FileEntry _fileEntry;

		public FileResource(FileEntry fileEntry) {
			_fileEntry = fileEntry;
		}

		@GET
		@Path("/")
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

		@PUT
		@Consumes("multipart/form-data")
		public FileRepr updateMetadata(
			@Multipart("metadata") FileRepr fileRepr,
			@Multipart("content") Attachment attachment,
			@Multipart(value = "changelog", required = false) String changelog,
			@Multipart(value = "majorVersion", required = false)
				boolean majorVersion)
			throws PortalException {

			if ((fileRepr == null) || (attachment == null)) {
				throw new WebApplicationException(
					Response.
						status(400).
						entity("Request is not properly encoded").
						build()
				);
			}

			changelog = GetterUtil.getString(changelog);

			return FileRepr.fromFileEntry(
				dlAppService.updateFileEntry(
					_fileEntry.getFileEntryId(), fileRepr.getFileName(),
					attachment.getContentType().getType(), fileRepr.getTitle(),
					fileRepr.getDescription(), changelog, majorVersion,
					attachment.getObject(byte[].class), new ServiceContext()),
				_request);
		}

		@PUT
		@Consumes({"application/json", "application/xml"})
		public FileRepr updateMetadata(
				FileRepr fileRepr, @QueryParam("changelog") String changelog,
				@QueryParam("majorVersion") boolean majorVersion)
			throws PortalException {

			return FileRepr.fromFileEntry(
				dlAppService.updateFileEntry(
					_fileEntry.getFileEntryId(), fileRepr.getFileName(),
					_fileEntry.getMimeType(), fileRepr.getTitle(),
					fileRepr.getDescription(), changelog, majorVersion,
					_fileEntry.getContentStream(), _fileEntry.getSize(),
					new ServiceContext()),
				_request);
		}

		@PUT
		@Path("/content")
		public FileRepr updateFile(
				byte[] bytes,
				@QueryParam("changelog") String changelog,
				@QueryParam("majorVersion") boolean majorVersion)
			throws PortalException {

			changelog = GetterUtil.get(changelog, StringPool.BLANK);

			return FileRepr.fromFileEntry(
				dlAppService.updateFileEntry(
					_fileEntry.getFileEntryId(), _fileEntry.getFileName(),
					_request.getContentType(), _fileEntry.getTitle(),
					_fileEntry.getDescription(), changelog, majorVersion,
					bytes, new ServiceContext()),
				_request);
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
				(contents) -> FolderRepr.fromFolder(
					folder, contents, _uriInfo.getBaseUriBuilder());
		}

		public FolderResource(final long groupId, final Repository repository) {
			_repositoryId = repository.getRepositoryId();
			_folderId = 0;
			_folderReprFunction =
				(contents) -> FolderRepr.fromRepository(
					groupId, repository, contents,
					_uriInfo.getBaseUriBuilder());
		}

		@GET
		public PageContainer<FolderRepr> getFolder(
				@Context Pagination pagination,
				@Context OrderBySelector orderBySelector)
			throws PortalException {

			OrderByComparator<Object> orderByComparator =
				OrderByComparatorSelectorUtil.select(
					orderBySelector, RepositoryContentObject.comparators).
				orElse(new RepositoryModelTitleComparator<>());

			List<RepositoryContentObject> repositoryContentObjects =
				dlAppService.getFoldersAndFileEntriesAndFileShortcuts(
					_repositoryId, _folderId, 0, true,
					pagination.getStartPosition(), pagination.getEndPosition(),
					orderByComparator).
				stream().
					map(DocumentLibraryRootResource.this::toObjectRepository).
					collect(Collectors.toList());

			return pagination.createContainer(
				_folderReprFunction.apply(
					repositoryContentObjects),
				dlAppService.getFoldersAndFileEntriesAndFileShortcutsCount(
					_repositoryId, _folderId, 0, true)
			);
		}

		@POST
		public FileRepr addFile(
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
				dlAppService.addFileEntry(
					_repositoryId, _folderId,
					fileRepr.getFileName(),
					attachment.getContentType().getType(), fileRepr.getTitle(),
					fileRepr.getDescription(), changelog,
					attachment.getObject(byte[].class), new ServiceContext()),
				_request);
		}

		@POST
		@Path("/{fileName}")
		public FileRepr addFile(
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
				dlAppService.addFileEntry(
					_repositoryId, _folderId,
					fileName,
					_request.getContentType(), title, description, changelog,
					content, new ServiceContext()),
				_request);
		}

	}

	protected RepositoryContentObject toObjectRepository(Object rco) {
		UriBuilder uriBuilder =
			_uriInfo.getBaseUriBuilder().path("objects");

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
				map(r -> RepositoryRepr.fromRepository(
					r, _uriInfo.getAbsolutePathBuilder().path("{id}"))).
				collect(Collectors.toList());
		}

		@Path("/{repositoryId}")
		public FolderResource getRepositoryHandler(
				@PathParam("repositoryId") long repositoryId)
			throws PortalException {

			return new FolderResource(
				_groupId, _repositoryProvider.getRepository(repositoryId));
		}

	}

}
