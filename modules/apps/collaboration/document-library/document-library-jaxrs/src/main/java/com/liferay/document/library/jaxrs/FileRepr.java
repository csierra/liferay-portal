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

import com.liferay.portal.kernel.repository.model.FileEntry;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Carlos Sierra Andrés
 */
@XmlRootElement
public class FileRepr {

	private long _id;
	private String _description;
	private String _fileName;

	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		_description = description;
	}

	public String getFileName() {
		return _fileName;
	}

	public void setFileName(String fileName) {
		_fileName = fileName;
	}

	public String getUrl() {
		return _url;
	}

	public void setUrl(String url) {
		_url = url;
	}

	public String getTitle() {
		return _title;
	}

	public void setTitle(String title) {
		_title = title;
	}

	public long getUuid() {
		return _id;
	}

	public void setUuid(long uuid) {
		_id = uuid;
	}

	private String _title;
	private String _url;

	public FileRepr() {
	}

	public FileRepr(
		long id, String description, String fileName, String title,
		String url) {

		_id = id;
		_description = description;
		_fileName = fileName;
		_title = title;
		_url = url;
	}

	public static FileRepr fromFileEntry(
		FileEntry fileEntry, HttpServletRequest request) {

		return new FileRepr(
			fileEntry.getFileEntryId(),
			fileEntry.getDescription(),
			fileEntry.getFileName(),
			fileEntry.getTitle(),
			request.getRequestURI() + "/" + fileEntry.getUuid());
	}
}
