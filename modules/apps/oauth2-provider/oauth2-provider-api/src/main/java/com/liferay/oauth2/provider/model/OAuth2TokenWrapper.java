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

import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.Serializable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * This class is a wrapper for {@link OAuth2Token}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see OAuth2Token
 * @generated
 */
@ProviderType
public class OAuth2TokenWrapper implements OAuth2Token,
	ModelWrapper<OAuth2Token> {
	public OAuth2TokenWrapper(OAuth2Token oAuth2Token) {
		_oAuth2Token = oAuth2Token;
	}

	@Override
	public Class<?> getModelClass() {
		return OAuth2Token.class;
	}

	@Override
	public String getModelClassName() {
		return OAuth2Token.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("oAuth2TokenId", getOAuth2TokenId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("lifeTime", getLifeTime());
		attributes.put("oAuth2ApplicationId", getOAuth2ApplicationId());
		attributes.put("oAuth2TokenType", getOAuth2TokenType());
		attributes.put("oAuth2RefreshTokenId", getOAuth2RefreshTokenId());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		String oAuth2TokenId = (String)attributes.get("oAuth2TokenId");

		if (oAuth2TokenId != null) {
			setOAuth2TokenId(oAuth2TokenId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}

		String userName = (String)attributes.get("userName");

		if (userName != null) {
			setUserName(userName);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Long lifeTime = (Long)attributes.get("lifeTime");

		if (lifeTime != null) {
			setLifeTime(lifeTime);
		}

		String oAuth2ApplicationId = (String)attributes.get(
				"oAuth2ApplicationId");

		if (oAuth2ApplicationId != null) {
			setOAuth2ApplicationId(oAuth2ApplicationId);
		}

		String oAuth2TokenType = (String)attributes.get("oAuth2TokenType");

		if (oAuth2TokenType != null) {
			setOAuth2TokenType(oAuth2TokenType);
		}

		String oAuth2RefreshTokenId = (String)attributes.get(
				"oAuth2RefreshTokenId");

		if (oAuth2RefreshTokenId != null) {
			setOAuth2RefreshTokenId(oAuth2RefreshTokenId);
		}
	}

	@Override
	public java.lang.Object clone() {
		return new OAuth2TokenWrapper((OAuth2Token)_oAuth2Token.clone());
	}

	@Override
	public int compareTo(OAuth2Token oAuth2Token) {
		return _oAuth2Token.compareTo(oAuth2Token);
	}

	/**
	* Returns the company ID of this o auth2 token.
	*
	* @return the company ID of this o auth2 token
	*/
	@Override
	public long getCompanyId() {
		return _oAuth2Token.getCompanyId();
	}

	/**
	* Returns the create date of this o auth2 token.
	*
	* @return the create date of this o auth2 token
	*/
	@Override
	public Date getCreateDate() {
		return _oAuth2Token.getCreateDate();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _oAuth2Token.getExpandoBridge();
	}

	/**
	* Returns the life time of this o auth2 token.
	*
	* @return the life time of this o auth2 token
	*/
	@Override
	public long getLifeTime() {
		return _oAuth2Token.getLifeTime();
	}

	/**
	* Returns the o auth2 application ID of this o auth2 token.
	*
	* @return the o auth2 application ID of this o auth2 token
	*/
	@Override
	public java.lang.String getOAuth2ApplicationId() {
		return _oAuth2Token.getOAuth2ApplicationId();
	}

	/**
	* Returns the o auth2 refresh token ID of this o auth2 token.
	*
	* @return the o auth2 refresh token ID of this o auth2 token
	*/
	@Override
	public java.lang.String getOAuth2RefreshTokenId() {
		return _oAuth2Token.getOAuth2RefreshTokenId();
	}

	/**
	* Returns the o auth2 token ID of this o auth2 token.
	*
	* @return the o auth2 token ID of this o auth2 token
	*/
	@Override
	public java.lang.String getOAuth2TokenId() {
		return _oAuth2Token.getOAuth2TokenId();
	}

	/**
	* Returns the o auth2 token type of this o auth2 token.
	*
	* @return the o auth2 token type of this o auth2 token
	*/
	@Override
	public java.lang.String getOAuth2TokenType() {
		return _oAuth2Token.getOAuth2TokenType();
	}

	/**
	* Returns the primary key of this o auth2 token.
	*
	* @return the primary key of this o auth2 token
	*/
	@Override
	public java.lang.String getPrimaryKey() {
		return _oAuth2Token.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _oAuth2Token.getPrimaryKeyObj();
	}

	/**
	* Returns the user ID of this o auth2 token.
	*
	* @return the user ID of this o auth2 token
	*/
	@Override
	public long getUserId() {
		return _oAuth2Token.getUserId();
	}

	/**
	* Returns the user name of this o auth2 token.
	*
	* @return the user name of this o auth2 token
	*/
	@Override
	public java.lang.String getUserName() {
		return _oAuth2Token.getUserName();
	}

	/**
	* Returns the user uuid of this o auth2 token.
	*
	* @return the user uuid of this o auth2 token
	*/
	@Override
	public java.lang.String getUserUuid() {
		return _oAuth2Token.getUserUuid();
	}

	@Override
	public int hashCode() {
		return _oAuth2Token.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _oAuth2Token.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _oAuth2Token.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _oAuth2Token.isNew();
	}

	@Override
	public void persist() {
		_oAuth2Token.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_oAuth2Token.setCachedModel(cachedModel);
	}

	/**
	* Sets the company ID of this o auth2 token.
	*
	* @param companyId the company ID of this o auth2 token
	*/
	@Override
	public void setCompanyId(long companyId) {
		_oAuth2Token.setCompanyId(companyId);
	}

	/**
	* Sets the create date of this o auth2 token.
	*
	* @param createDate the create date of this o auth2 token
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_oAuth2Token.setCreateDate(createDate);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_oAuth2Token.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_oAuth2Token.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_oAuth2Token.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the life time of this o auth2 token.
	*
	* @param lifeTime the life time of this o auth2 token
	*/
	@Override
	public void setLifeTime(long lifeTime) {
		_oAuth2Token.setLifeTime(lifeTime);
	}

	@Override
	public void setNew(boolean n) {
		_oAuth2Token.setNew(n);
	}

	/**
	* Sets the o auth2 application ID of this o auth2 token.
	*
	* @param oAuth2ApplicationId the o auth2 application ID of this o auth2 token
	*/
	@Override
	public void setOAuth2ApplicationId(java.lang.String oAuth2ApplicationId) {
		_oAuth2Token.setOAuth2ApplicationId(oAuth2ApplicationId);
	}

	/**
	* Sets the o auth2 refresh token ID of this o auth2 token.
	*
	* @param oAuth2RefreshTokenId the o auth2 refresh token ID of this o auth2 token
	*/
	@Override
	public void setOAuth2RefreshTokenId(java.lang.String oAuth2RefreshTokenId) {
		_oAuth2Token.setOAuth2RefreshTokenId(oAuth2RefreshTokenId);
	}

	/**
	* Sets the o auth2 token ID of this o auth2 token.
	*
	* @param oAuth2TokenId the o auth2 token ID of this o auth2 token
	*/
	@Override
	public void setOAuth2TokenId(java.lang.String oAuth2TokenId) {
		_oAuth2Token.setOAuth2TokenId(oAuth2TokenId);
	}

	/**
	* Sets the o auth2 token type of this o auth2 token.
	*
	* @param oAuth2TokenType the o auth2 token type of this o auth2 token
	*/
	@Override
	public void setOAuth2TokenType(java.lang.String oAuth2TokenType) {
		_oAuth2Token.setOAuth2TokenType(oAuth2TokenType);
	}

	/**
	* Sets the primary key of this o auth2 token.
	*
	* @param primaryKey the primary key of this o auth2 token
	*/
	@Override
	public void setPrimaryKey(java.lang.String primaryKey) {
		_oAuth2Token.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_oAuth2Token.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the user ID of this o auth2 token.
	*
	* @param userId the user ID of this o auth2 token
	*/
	@Override
	public void setUserId(long userId) {
		_oAuth2Token.setUserId(userId);
	}

	/**
	* Sets the user name of this o auth2 token.
	*
	* @param userName the user name of this o auth2 token
	*/
	@Override
	public void setUserName(java.lang.String userName) {
		_oAuth2Token.setUserName(userName);
	}

	/**
	* Sets the user uuid of this o auth2 token.
	*
	* @param userUuid the user uuid of this o auth2 token
	*/
	@Override
	public void setUserUuid(java.lang.String userUuid) {
		_oAuth2Token.setUserUuid(userUuid);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<OAuth2Token> toCacheModel() {
		return _oAuth2Token.toCacheModel();
	}

	@Override
	public OAuth2Token toEscapedModel() {
		return new OAuth2TokenWrapper(_oAuth2Token.toEscapedModel());
	}

	@Override
	public java.lang.String toString() {
		return _oAuth2Token.toString();
	}

	@Override
	public OAuth2Token toUnescapedModel() {
		return new OAuth2TokenWrapper(_oAuth2Token.toUnescapedModel());
	}

	@Override
	public java.lang.String toXmlString() {
		return _oAuth2Token.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof OAuth2TokenWrapper)) {
			return false;
		}

		OAuth2TokenWrapper oAuth2TokenWrapper = (OAuth2TokenWrapper)obj;

		if (Objects.equals(_oAuth2Token, oAuth2TokenWrapper._oAuth2Token)) {
			return true;
		}

		return false;
	}

	@Override
	public OAuth2Token getWrappedModel() {
		return _oAuth2Token;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _oAuth2Token.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _oAuth2Token.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_oAuth2Token.resetOriginalValues();
	}

	private final OAuth2Token _oAuth2Token;
}