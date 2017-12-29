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

import aQute.bnd.annotation.ProviderType;

import com.liferay.expando.kernel.model.ExpandoBridge;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.ShardedModel;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.Serializable;

import java.util.Date;

/**
 * The base model interface for the OAuth2Token service. Represents a row in the &quot;OAuth2Token&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.liferay.oauth2.provider.model.impl.OAuth2TokenModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.liferay.oauth2.provider.model.impl.OAuth2TokenImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see OAuth2Token
 * @see com.liferay.oauth2.provider.model.impl.OAuth2TokenImpl
 * @see com.liferay.oauth2.provider.model.impl.OAuth2TokenModelImpl
 * @generated
 */
@ProviderType
public interface OAuth2TokenModel extends BaseModel<OAuth2Token>, ShardedModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a o auth2 token model instance should use the {@link OAuth2Token} interface instead.
	 */

	/**
	 * Returns the primary key of this o auth2 token.
	 *
	 * @return the primary key of this o auth2 token
	 */
	public String getPrimaryKey();

	/**
	 * Sets the primary key of this o auth2 token.
	 *
	 * @param primaryKey the primary key of this o auth2 token
	 */
	public void setPrimaryKey(String primaryKey);

	/**
	 * Returns the o auth2 token ID of this o auth2 token.
	 *
	 * @return the o auth2 token ID of this o auth2 token
	 */
	@AutoEscape
	public String getOAuth2TokenId();

	/**
	 * Sets the o auth2 token ID of this o auth2 token.
	 *
	 * @param oAuth2TokenId the o auth2 token ID of this o auth2 token
	 */
	public void setOAuth2TokenId(String oAuth2TokenId);

	/**
	 * Returns the company ID of this o auth2 token.
	 *
	 * @return the company ID of this o auth2 token
	 */
	@Override
	public long getCompanyId();

	/**
	 * Sets the company ID of this o auth2 token.
	 *
	 * @param companyId the company ID of this o auth2 token
	 */
	@Override
	public void setCompanyId(long companyId);

	/**
	 * Returns the create date of this o auth2 token.
	 *
	 * @return the create date of this o auth2 token
	 */
	public Date getCreateDate();

	/**
	 * Sets the create date of this o auth2 token.
	 *
	 * @param createDate the create date of this o auth2 token
	 */
	public void setCreateDate(Date createDate);

	/**
	 * Returns the o auth2 application ID of this o auth2 token.
	 *
	 * @return the o auth2 application ID of this o auth2 token
	 */
	@AutoEscape
	public String getOAuth2ApplicationId();

	/**
	 * Sets the o auth2 application ID of this o auth2 token.
	 *
	 * @param oAuth2ApplicationId the o auth2 application ID of this o auth2 token
	 */
	public void setOAuth2ApplicationId(String oAuth2ApplicationId);

	@Override
	public boolean isNew();

	@Override
	public void setNew(boolean n);

	@Override
	public boolean isCachedModel();

	@Override
	public void setCachedModel(boolean cachedModel);

	@Override
	public boolean isEscapedModel();

	@Override
	public Serializable getPrimaryKeyObj();

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj);

	@Override
	public ExpandoBridge getExpandoBridge();

	@Override
	public void setExpandoBridgeAttributes(BaseModel<?> baseModel);

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge);

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext);

	@Override
	public Object clone();

	@Override
	public int compareTo(OAuth2Token oAuth2Token);

	@Override
	public int hashCode();

	@Override
	public CacheModel<OAuth2Token> toCacheModel();

	@Override
	public OAuth2Token toEscapedModel();

	@Override
	public OAuth2Token toUnescapedModel();

	@Override
	public String toString();

	@Override
	public String toXmlString();
}