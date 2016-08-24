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

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Carlos Sierra Andrés
 */
@XmlRootElement
public class GroupRepr {
	private long _id;
	private String _name;

	public String getFriendlyUrl() {
		return _friendlyUrl;
	}

	public void setFriendlyUrl(String friendlyUrl) {
		_friendlyUrl = friendlyUrl;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public int getType() {
		return _type;
	}

	public void setType(int type) {
		_type = type;
	}

	private String _friendlyUrl;
	private int _type;
	private String _url;

	public GroupRepr() {
	}

	public GroupRepr(long id, String url, String name, String friendlyUrl, int type) {
		_id = id;
		_url = url;
		_name = name;
		_friendlyUrl = friendlyUrl;
		_type = type;
	}

	public long getId() {
		return _id;
	}

	public void setId(long id) {
		this._id = id;
	}

	public String getUrl() {
		return _url;
	}

	public void setUrl(String url) {
		this._url = url;
	}

}
