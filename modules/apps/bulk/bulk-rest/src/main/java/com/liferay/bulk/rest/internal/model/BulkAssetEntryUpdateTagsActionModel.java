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

package com.liferay.bulk.rest.internal.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Adolfo Pérez
 */
@XmlRootElement
public class BulkAssetEntryUpdateTagsActionModel {

	@XmlElement
	public boolean getAppend() {
		return _append;
	}

	public long getFolderId() {
		return _folderId;
	}

	@XmlTransient
	public Map<String, String[]> getParameterMap() {
		String[] values = _selection.toArray(new String[_selection.size()]);

		if (getRepositoryId() == 0) {
			return Collections.singletonMap("rowIdsFileEntry", values);
		}

		Map<String, String[]> parameterMap = new HashMap<>(2);

		parameterMap.put("folderId", new String[] {String.valueOf(_folderId)});
		parameterMap.put(
			"repositoryId", new String[] {String.valueOf(_repositoryId)});
		parameterMap.put("rowIdsFileEntry", values);
		parameterMap.put(
			"selectAll", new String[] {Boolean.toString(_selectAll)});

		return parameterMap;
	}

	@XmlElement
	public long getRepositoryId() {
		return _repositoryId;
	}

	@XmlElement
	public List<String> getSelection() {
		return _selection;
	}

	@XmlElement
	public String[] getToAddTagNames() {
		return _toAddTagNames;
	}

	@XmlElement
	public String[] getToRemoveTagNames() {
		return _toRemoveTagNames;
	}

	public boolean isSelectAll() {
		return _selectAll;
	}

	public void setAppend(boolean append) {
		_append = append;
	}

	public void setFolderId(long folderId) {
		_folderId = folderId;
	}

	public void setRepositoryId(long repositoryId) {
		_repositoryId = repositoryId;
	}

	public void setSelectAll(boolean selectAll) {
		_selectAll = selectAll;
	}

	public void setSelection(List<String> selection) {
		_selection = selection;
	}

	public void setToAddTagNames(String[] toAddTagNames) {
		_toAddTagNames = toAddTagNames;
	}

	public void setToRemoveTagNames(String[] toRemoveTagNames) {
		_toRemoveTagNames = toRemoveTagNames;
	}

	private boolean _append;
	private long _folderId;
	private long _repositoryId;
	private boolean _selectAll;
	private List<String> _selection;
	private String[] _toAddTagNames;
	private String[] _toRemoveTagNames;

}