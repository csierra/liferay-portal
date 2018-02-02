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

package com.liferay.oauth2.provider.model.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.oauth2.provider.model.OAuth2Token;
import com.liferay.oauth2.provider.model.OAuth2TokenModel;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;

import java.io.Serializable;

import java.sql.Types;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * The base model implementation for the OAuth2Token service. Represents a row in the &quot;OAuth2Token&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link OAuth2TokenModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link OAuth2TokenImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see OAuth2TokenImpl
 * @see OAuth2Token
 * @see OAuth2TokenModel
 * @generated
 */
@ProviderType
public class OAuth2TokenModelImpl extends BaseModelImpl<OAuth2Token>
	implements OAuth2TokenModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a o auth2 token model instance should use the {@link OAuth2Token} interface instead.
	 */
	public static final String TABLE_NAME = "OAuth2Token";
	public static final Object[][] TABLE_COLUMNS = {
			{ "oAuth2TokenId", Types.VARCHAR },
			{ "companyId", Types.BIGINT },
			{ "userId", Types.BIGINT },
			{ "userName", Types.VARCHAR },
			{ "createDate", Types.TIMESTAMP },
			{ "lifeTime", Types.BIGINT },
			{ "oAuth2ApplicationId", Types.BIGINT },
			{ "oAuth2TokenType", Types.VARCHAR },
			{ "oAuth2RefreshTokenId", Types.VARCHAR },
			{ "scopes", Types.CLOB }
		};
	public static final Map<String, Integer> TABLE_COLUMNS_MAP = new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("oAuth2TokenId", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("lifeTime", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("oAuth2ApplicationId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("oAuth2TokenType", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("oAuth2RefreshTokenId", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("scopes", Types.CLOB);
	}

	public static final String TABLE_SQL_CREATE = "create table OAuth2Token (oAuth2TokenId VARCHAR(75) not null primary key,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,lifeTime LONG,oAuth2ApplicationId LONG,oAuth2TokenType VARCHAR(75) null,oAuth2RefreshTokenId VARCHAR(75) null,scopes TEXT null)";
	public static final String TABLE_SQL_DROP = "drop table OAuth2Token";
	public static final String ORDER_BY_JPQL = " ORDER BY oAuth2Token.oAuth2TokenId ASC";
	public static final String ORDER_BY_SQL = " ORDER BY OAuth2Token.oAuth2TokenId ASC";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.oauth2.provider.service.util.ServiceProps.get(
				"value.object.entity.cache.enabled.com.liferay.oauth2.provider.model.OAuth2Token"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.oauth2.provider.service.util.ServiceProps.get(
				"value.object.finder.cache.enabled.com.liferay.oauth2.provider.model.OAuth2Token"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.oauth2.provider.service.util.ServiceProps.get(
				"value.object.column.bitmask.enabled.com.liferay.oauth2.provider.model.OAuth2Token"),
			true);
	public static final long OAUTH2APPLICATIONID_COLUMN_BITMASK = 1L;
	public static final long OAUTH2REFRESHTOKENID_COLUMN_BITMASK = 2L;
	public static final long USERNAME_COLUMN_BITMASK = 4L;
	public static final long OAUTH2TOKENID_COLUMN_BITMASK = 8L;
	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.oauth2.provider.service.util.ServiceProps.get(
				"lock.expiration.time.com.liferay.oauth2.provider.model.OAuth2Token"));

	public OAuth2TokenModelImpl() {
	}

	@Override
	public String getPrimaryKey() {
		return _oAuth2TokenId;
	}

	@Override
	public void setPrimaryKey(String primaryKey) {
		setOAuth2TokenId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _oAuth2TokenId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey((String)primaryKeyObj);
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
		attributes.put("scopes", getScopes());

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

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

		Long oAuth2ApplicationId = (Long)attributes.get("oAuth2ApplicationId");

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

		String scopes = (String)attributes.get("scopes");

		if (scopes != null) {
			setScopes(scopes);
		}
	}

	@Override
	public String getOAuth2TokenId() {
		if (_oAuth2TokenId == null) {
			return "";
		}
		else {
			return _oAuth2TokenId;
		}
	}

	@Override
	public void setOAuth2TokenId(String oAuth2TokenId) {
		_oAuth2TokenId = oAuth2TokenId;
	}

	@Override
	public long getCompanyId() {
		return _companyId;
	}

	@Override
	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	@Override
	public long getUserId() {
		return _userId;
	}

	@Override
	public void setUserId(long userId) {
		_userId = userId;
	}

	@Override
	public String getUserUuid() {
		try {
			User user = UserLocalServiceUtil.getUserById(getUserId());

			return user.getUuid();
		}
		catch (PortalException pe) {
			return "";
		}
	}

	@Override
	public void setUserUuid(String userUuid) {
	}

	@Override
	public String getUserName() {
		if (_userName == null) {
			return "";
		}
		else {
			return _userName;
		}
	}

	@Override
	public void setUserName(String userName) {
		_columnBitmask |= USERNAME_COLUMN_BITMASK;

		if (_originalUserName == null) {
			_originalUserName = _userName;
		}

		_userName = userName;
	}

	public String getOriginalUserName() {
		return GetterUtil.getString(_originalUserName);
	}

	@Override
	public Date getCreateDate() {
		return _createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	@Override
	public long getLifeTime() {
		return _lifeTime;
	}

	@Override
	public void setLifeTime(long lifeTime) {
		_lifeTime = lifeTime;
	}

	@Override
	public long getOAuth2ApplicationId() {
		return _oAuth2ApplicationId;
	}

	@Override
	public void setOAuth2ApplicationId(long oAuth2ApplicationId) {
		_columnBitmask |= OAUTH2APPLICATIONID_COLUMN_BITMASK;

		if (!_setOriginalOAuth2ApplicationId) {
			_setOriginalOAuth2ApplicationId = true;

			_originalOAuth2ApplicationId = _oAuth2ApplicationId;
		}

		_oAuth2ApplicationId = oAuth2ApplicationId;
	}

	public long getOriginalOAuth2ApplicationId() {
		return _originalOAuth2ApplicationId;
	}

	@Override
	public String getOAuth2TokenType() {
		if (_oAuth2TokenType == null) {
			return "";
		}
		else {
			return _oAuth2TokenType;
		}
	}

	@Override
	public void setOAuth2TokenType(String oAuth2TokenType) {
		_oAuth2TokenType = oAuth2TokenType;
	}

	@Override
	public String getOAuth2RefreshTokenId() {
		if (_oAuth2RefreshTokenId == null) {
			return "";
		}
		else {
			return _oAuth2RefreshTokenId;
		}
	}

	@Override
	public void setOAuth2RefreshTokenId(String oAuth2RefreshTokenId) {
		_columnBitmask |= OAUTH2REFRESHTOKENID_COLUMN_BITMASK;

		if (_originalOAuth2RefreshTokenId == null) {
			_originalOAuth2RefreshTokenId = _oAuth2RefreshTokenId;
		}

		_oAuth2RefreshTokenId = oAuth2RefreshTokenId;
	}

	public String getOriginalOAuth2RefreshTokenId() {
		return GetterUtil.getString(_originalOAuth2RefreshTokenId);
	}

	@Override
	public String getScopes() {
		if (_scopes == null) {
			return "";
		}
		else {
			return _scopes;
		}
	}

	@Override
	public void setScopes(String scopes) {
		_scopes = scopes;
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public OAuth2Token toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (OAuth2Token)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelInterfaces, new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		OAuth2TokenImpl oAuth2TokenImpl = new OAuth2TokenImpl();

		oAuth2TokenImpl.setOAuth2TokenId(getOAuth2TokenId());
		oAuth2TokenImpl.setCompanyId(getCompanyId());
		oAuth2TokenImpl.setUserId(getUserId());
		oAuth2TokenImpl.setUserName(getUserName());
		oAuth2TokenImpl.setCreateDate(getCreateDate());
		oAuth2TokenImpl.setLifeTime(getLifeTime());
		oAuth2TokenImpl.setOAuth2ApplicationId(getOAuth2ApplicationId());
		oAuth2TokenImpl.setOAuth2TokenType(getOAuth2TokenType());
		oAuth2TokenImpl.setOAuth2RefreshTokenId(getOAuth2RefreshTokenId());
		oAuth2TokenImpl.setScopes(getScopes());

		oAuth2TokenImpl.resetOriginalValues();

		return oAuth2TokenImpl;
	}

	@Override
	public int compareTo(OAuth2Token oAuth2Token) {
		String primaryKey = oAuth2Token.getPrimaryKey();

		return getPrimaryKey().compareTo(primaryKey);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof OAuth2Token)) {
			return false;
		}

		OAuth2Token oAuth2Token = (OAuth2Token)obj;

		String primaryKey = oAuth2Token.getPrimaryKey();

		if (getPrimaryKey().equals(primaryKey)) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return getPrimaryKey().hashCode();
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return ENTITY_CACHE_ENABLED;
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return FINDER_CACHE_ENABLED;
	}

	@Override
	public void resetOriginalValues() {
		OAuth2TokenModelImpl oAuth2TokenModelImpl = this;

		oAuth2TokenModelImpl._originalUserName = oAuth2TokenModelImpl._userName;

		oAuth2TokenModelImpl._originalOAuth2ApplicationId = oAuth2TokenModelImpl._oAuth2ApplicationId;

		oAuth2TokenModelImpl._setOriginalOAuth2ApplicationId = false;

		oAuth2TokenModelImpl._originalOAuth2RefreshTokenId = oAuth2TokenModelImpl._oAuth2RefreshTokenId;

		oAuth2TokenModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<OAuth2Token> toCacheModel() {
		OAuth2TokenCacheModel oAuth2TokenCacheModel = new OAuth2TokenCacheModel();

		oAuth2TokenCacheModel.oAuth2TokenId = getOAuth2TokenId();

		String oAuth2TokenId = oAuth2TokenCacheModel.oAuth2TokenId;

		if ((oAuth2TokenId != null) && (oAuth2TokenId.length() == 0)) {
			oAuth2TokenCacheModel.oAuth2TokenId = null;
		}

		oAuth2TokenCacheModel.companyId = getCompanyId();

		oAuth2TokenCacheModel.userId = getUserId();

		oAuth2TokenCacheModel.userName = getUserName();

		String userName = oAuth2TokenCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			oAuth2TokenCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			oAuth2TokenCacheModel.createDate = createDate.getTime();
		}
		else {
			oAuth2TokenCacheModel.createDate = Long.MIN_VALUE;
		}

		oAuth2TokenCacheModel.lifeTime = getLifeTime();

		oAuth2TokenCacheModel.oAuth2ApplicationId = getOAuth2ApplicationId();

		oAuth2TokenCacheModel.oAuth2TokenType = getOAuth2TokenType();

		String oAuth2TokenType = oAuth2TokenCacheModel.oAuth2TokenType;

		if ((oAuth2TokenType != null) && (oAuth2TokenType.length() == 0)) {
			oAuth2TokenCacheModel.oAuth2TokenType = null;
		}

		oAuth2TokenCacheModel.oAuth2RefreshTokenId = getOAuth2RefreshTokenId();

		String oAuth2RefreshTokenId = oAuth2TokenCacheModel.oAuth2RefreshTokenId;

		if ((oAuth2RefreshTokenId != null) &&
				(oAuth2RefreshTokenId.length() == 0)) {
			oAuth2TokenCacheModel.oAuth2RefreshTokenId = null;
		}

		oAuth2TokenCacheModel.scopes = getScopes();

		String scopes = oAuth2TokenCacheModel.scopes;

		if ((scopes != null) && (scopes.length() == 0)) {
			oAuth2TokenCacheModel.scopes = null;
		}

		return oAuth2TokenCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(21);

		sb.append("{oAuth2TokenId=");
		sb.append(getOAuth2TokenId());
		sb.append(", companyId=");
		sb.append(getCompanyId());
		sb.append(", userId=");
		sb.append(getUserId());
		sb.append(", userName=");
		sb.append(getUserName());
		sb.append(", createDate=");
		sb.append(getCreateDate());
		sb.append(", lifeTime=");
		sb.append(getLifeTime());
		sb.append(", oAuth2ApplicationId=");
		sb.append(getOAuth2ApplicationId());
		sb.append(", oAuth2TokenType=");
		sb.append(getOAuth2TokenType());
		sb.append(", oAuth2RefreshTokenId=");
		sb.append(getOAuth2RefreshTokenId());
		sb.append(", scopes=");
		sb.append(getScopes());
		sb.append("}");

		return sb.toString();
	}

	@Override
	public String toXmlString() {
		StringBundler sb = new StringBundler(34);

		sb.append("<model><model-name>");
		sb.append("com.liferay.oauth2.provider.model.OAuth2Token");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>oAuth2TokenId</column-name><column-value><![CDATA[");
		sb.append(getOAuth2TokenId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>companyId</column-name><column-value><![CDATA[");
		sb.append(getCompanyId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>userId</column-name><column-value><![CDATA[");
		sb.append(getUserId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>userName</column-name><column-value><![CDATA[");
		sb.append(getUserName());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>createDate</column-name><column-value><![CDATA[");
		sb.append(getCreateDate());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>lifeTime</column-name><column-value><![CDATA[");
		sb.append(getLifeTime());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>oAuth2ApplicationId</column-name><column-value><![CDATA[");
		sb.append(getOAuth2ApplicationId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>oAuth2TokenType</column-name><column-value><![CDATA[");
		sb.append(getOAuth2TokenType());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>oAuth2RefreshTokenId</column-name><column-value><![CDATA[");
		sb.append(getOAuth2RefreshTokenId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>scopes</column-name><column-value><![CDATA[");
		sb.append(getScopes());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static final ClassLoader _classLoader = OAuth2Token.class.getClassLoader();
	private static final Class<?>[] _escapedModelInterfaces = new Class[] {
			OAuth2Token.class
		};
	private String _oAuth2TokenId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private String _originalUserName;
	private Date _createDate;
	private long _lifeTime;
	private long _oAuth2ApplicationId;
	private long _originalOAuth2ApplicationId;
	private boolean _setOriginalOAuth2ApplicationId;
	private String _oAuth2TokenType;
	private String _oAuth2RefreshTokenId;
	private String _originalOAuth2RefreshTokenId;
	private String _scopes;
	private long _columnBitmask;
	private OAuth2Token _escapedModel;
}