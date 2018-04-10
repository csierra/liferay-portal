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

package com.liferay.oauth2.provider.jsonws;

/**
 * @author Tomas Polesovsky
 */
public class SAPEntryScope {

	public SAPEntryScope(
		long sapEntryId, String sapEntryName, String scopeName, String title) {

		_sapEntryId = sapEntryId;
		_sapEntryName = sapEntryName;
		_scopeName = scopeName;
		_title = title;
	}

	public long getSapEntryId() {
		return _sapEntryId;
	}

	public String getSapEntryName() {
		return _sapEntryName;
	}

	public String getScopeName() {
		return _scopeName;
	}

	public String getTitle() {
		return _title;
	}

	public void setSapEntryId(long sapEntryId) {
		_sapEntryId = sapEntryId;
	}

	public void setSapEntryName(String sapEntryName) {
		_sapEntryName = sapEntryName;
	}

	public void setScopeName(String scopeName) {
		_scopeName = scopeName;
	}

	public void setTitle(String title) {
		_title = title;
	}

	private long _sapEntryId;
	private String _sapEntryName;
	private String _scopeName;
	private String _title;

}