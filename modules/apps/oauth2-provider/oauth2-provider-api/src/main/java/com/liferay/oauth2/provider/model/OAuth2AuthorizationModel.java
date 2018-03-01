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

package com.liferay.oauth2.provider.model;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.StringPool;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Tomas Polesovsky
 */
public class OAuth2AuthorizationModel implements BaseModel<OAuth2Authorization> {

	@Override
	public Object clone() {
		return null;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return null;
	}

	@Override
	public Class<?> getModelClass() {
		return null;
	}

	@Override
	public String getModelClassName() {
		return null;
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		return null;
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return null;
	}

	@Override
	public boolean isCachedModel() {
		return false;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return false;
	}

	@Override
	public boolean isEscapedModel() {
		return false;
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return false;
	}

	@Override
	public boolean isNew() {
		return false;
	}

	@Override
	public void resetOriginalValues() {

	}

	@Override
	public void setCachedModel(boolean cachedModel) {

	}

	@Override
	public void setExpandoBridgeAttributes(
		BaseModel<?> baseModel) {

	}

	@Override
	public void setExpandoBridgeAttributes(
		ExpandoBridge expandoBridge) {

	}

	@Override
	public void setExpandoBridgeAttributes(
		ServiceContext serviceContext) {

	}

	@Override
	public void setModelAttributes(
		Map<String, Object> attributes) {

	}

	@Override
	public void setNew(boolean n) {

	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {

	}

	@Override
	public CacheModel<OAuth2Authorization> toCacheModel() {
		return null;
	}

	@Override
	public OAuth2Authorization toEscapedModel() {
		return null;
	}

	@Override
	public OAuth2Authorization toUnescapedModel() {
		return null;
	}

	@Override
	public String toXmlString() {
		return toString();
	}

	@Override
	public int compareTo(OAuth2Authorization o) {
		return 0;
	}
}

