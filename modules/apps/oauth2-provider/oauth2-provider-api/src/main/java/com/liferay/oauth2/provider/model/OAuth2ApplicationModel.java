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
import com.liferay.portal.kernel.model.AuditedModel;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.ShardedModel;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.Serializable;

import java.util.Date;

/**
 * The base model interface for the OAuth2Application service. Represents a row in the &quot;OAuth2Application&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.liferay.oauth2.provider.model.impl.OAuth2ApplicationModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.liferay.oauth2.provider.model.impl.OAuth2ApplicationImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see OAuth2Application
 * @see com.liferay.oauth2.provider.model.impl.OAuth2ApplicationImpl
 * @see com.liferay.oauth2.provider.model.impl.OAuth2ApplicationModelImpl
 * @generated
 */
@ProviderType
public interface OAuth2ApplicationModel extends AuditedModel,
	BaseModel<OAuth2Application>, ShardedModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a o auth2 application model instance should use the {@link OAuth2Application} interface instead.
	 */

	/**
	 * Returns the primary key of this o auth2 application.
	 *
	 * @return the primary key of this o auth2 application
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this o auth2 application.
	 *
	 * @param primaryKey the primary key of this o auth2 application
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the o auth2 application ID of this o auth2 application.
	 *
	 * @return the o auth2 application ID of this o auth2 application
	 */
	public long getOAuth2ApplicationId();

	/**
	 * Sets the o auth2 application ID of this o auth2 application.
	 *
	 * @param oAuth2ApplicationId the o auth2 application ID of this o auth2 application
	 */
	public void setOAuth2ApplicationId(long oAuth2ApplicationId);

	/**
	 * Returns the company ID of this o auth2 application.
	 *
	 * @return the company ID of this o auth2 application
	 */
	@Override
	public long getCompanyId();

	/**
	 * Sets the company ID of this o auth2 application.
	 *
	 * @param companyId the company ID of this o auth2 application
	 */
	@Override
	public void setCompanyId(long companyId);

	/**
	 * Returns the user ID of this o auth2 application.
	 *
	 * @return the user ID of this o auth2 application
	 */
	@Override
	public long getUserId();

	/**
	 * Sets the user ID of this o auth2 application.
	 *
	 * @param userId the user ID of this o auth2 application
	 */
	@Override
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this o auth2 application.
	 *
	 * @return the user uuid of this o auth2 application
	 */
	@Override
	public String getUserUuid();

	/**
	 * Sets the user uuid of this o auth2 application.
	 *
	 * @param userUuid the user uuid of this o auth2 application
	 */
	@Override
	public void setUserUuid(String userUuid);

	/**
	 * Returns the user name of this o auth2 application.
	 *
	 * @return the user name of this o auth2 application
	 */
	@AutoEscape
	@Override
	public String getUserName();

	/**
	 * Sets the user name of this o auth2 application.
	 *
	 * @param userName the user name of this o auth2 application
	 */
	@Override
	public void setUserName(String userName);

	/**
	 * Returns the create date of this o auth2 application.
	 *
	 * @return the create date of this o auth2 application
	 */
	@Override
	public Date getCreateDate();

	/**
	 * Sets the create date of this o auth2 application.
	 *
	 * @param createDate the create date of this o auth2 application
	 */
	@Override
	public void setCreateDate(Date createDate);

	/**
	 * Returns the modified date of this o auth2 application.
	 *
	 * @return the modified date of this o auth2 application
	 */
	@Override
	public Date getModifiedDate();

	/**
	 * Sets the modified date of this o auth2 application.
	 *
	 * @param modifiedDate the modified date of this o auth2 application
	 */
	@Override
	public void setModifiedDate(Date modifiedDate);

	/**
	 * Returns the client ID of this o auth2 application.
	 *
	 * @return the client ID of this o auth2 application
	 */
	@AutoEscape
	public String getClientId();

	/**
	 * Sets the client ID of this o auth2 application.
	 *
	 * @param clientId the client ID of this o auth2 application
	 */
	public void setClientId(String clientId);

	/**
	 * Returns the client secret of this o auth2 application.
	 *
	 * @return the client secret of this o auth2 application
	 */
	@AutoEscape
	public String getClientSecret();

	/**
	 * Sets the client secret of this o auth2 application.
	 *
	 * @param clientSecret the client secret of this o auth2 application
	 */
	public void setClientSecret(String clientSecret);

	/**
	 * Returns the redirect uri of this o auth2 application.
	 *
	 * @return the redirect uri of this o auth2 application
	 */
	@AutoEscape
	public String getRedirectUri();

	/**
	 * Sets the redirect uri of this o auth2 application.
	 *
	 * @param redirectUri the redirect uri of this o auth2 application
	 */
	public void setRedirectUri(String redirectUri);

	/**
	 * Returns the client confidential of this o auth2 application.
	 *
	 * @return the client confidential of this o auth2 application
	 */
	public Boolean getClientConfidential();

	/**
	 * Sets the client confidential of this o auth2 application.
	 *
	 * @param clientConfidential the client confidential of this o auth2 application
	 */
	public void setClientConfidential(Boolean clientConfidential);

	/**
	 * Returns the description of this o auth2 application.
	 *
	 * @return the description of this o auth2 application
	 */
	@AutoEscape
	public String getDescription();

	/**
	 * Sets the description of this o auth2 application.
	 *
	 * @param description the description of this o auth2 application
	 */
	public void setDescription(String description);

	/**
	 * Returns the name of this o auth2 application.
	 *
	 * @return the name of this o auth2 application
	 */
	@AutoEscape
	public String getName();

	/**
	 * Sets the name of this o auth2 application.
	 *
	 * @param name the name of this o auth2 application
	 */
	public void setName(String name);

	/**
	 * Returns the web url of this o auth2 application.
	 *
	 * @return the web url of this o auth2 application
	 */
	@AutoEscape
	public String getWebUrl();

	/**
	 * Sets the web url of this o auth2 application.
	 *
	 * @param webUrl the web url of this o auth2 application
	 */
	public void setWebUrl(String webUrl);

	/**
	 * Returns the scopes of this o auth2 application.
	 *
	 * @return the scopes of this o auth2 application
	 */
	@AutoEscape
	public String getScopes();

	/**
	 * Sets the scopes of this o auth2 application.
	 *
	 * @param scopes the scopes of this o auth2 application
	 */
	public void setScopes(String scopes);

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
	public int compareTo(OAuth2Application oAuth2Application);

	@Override
	public int hashCode();

	@Override
	public CacheModel<OAuth2Application> toCacheModel();

	@Override
	public OAuth2Application toEscapedModel();

	@Override
	public OAuth2Application toUnescapedModel();

	@Override
	public String toString();

	@Override
	public String toXmlString();
}