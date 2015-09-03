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

package com.liferay.portlet.asset.model.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.User;
import com.liferay.portal.model.impl.BaseModelImpl;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;

import com.liferay.portlet.asset.model.AssetLink;
import com.liferay.portlet.asset.model.AssetLinkModel;
import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.util.ExpandoBridgeFactoryUtil;

import java.io.Serializable;

import java.sql.Types;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * The base model implementation for the AssetLink service. Represents a row in the &quot;AssetLink&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link AssetLinkModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link AssetLinkImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AssetLinkImpl
 * @see AssetLink
 * @see AssetLinkModel
 * @generated
 */
@ProviderType
public class AssetLinkModelImpl extends BaseModelImpl<AssetLink>
	implements AssetLinkModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a asset link model instance should use the {@link AssetLink} interface instead.
	 */
	public static final String TABLE_NAME = "AssetLink";
	public static final Object[][] TABLE_COLUMNS = {
			{ "linkId", Types.BIGINT },
			{ "userId", Types.BIGINT },
			{ "userName", Types.VARCHAR },
			{ "createDate", Types.TIMESTAMP },
			{ "entryId1", Types.BIGINT },
			{ "entryId2", Types.BIGINT },
			{ "type_", Types.INTEGER },
			{ "weight", Types.INTEGER },
			{ "companyId", Types.BIGINT }
		};
	public static final Map<String, Integer> TABLE_COLUMNS_MAP = new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("linkId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("entryId1", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("entryId2", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("type_", Types.INTEGER);
		TABLE_COLUMNS_MAP.put("weight", Types.INTEGER);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
	}

	public static final String TABLE_SQL_CREATE = "create table AssetLink (linkId LONG not null primary key,userId LONG,userName VARCHAR(75) null,createDate DATE null,entryId1 LONG,entryId2 LONG,type_ INTEGER,weight INTEGER,companyId LONG)";
	public static final String TABLE_SQL_DROP = "drop table AssetLink";
	public static final String ORDER_BY_JPQL = " ORDER BY assetLink.weight ASC";
	public static final String ORDER_BY_SQL = " ORDER BY AssetLink.weight ASC";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.entity.cache.enabled.com.liferay.portlet.asset.model.AssetLink"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.finder.cache.enabled.com.liferay.portlet.asset.model.AssetLink"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
				"value.object.column.bitmask.enabled.com.liferay.portlet.asset.model.AssetLink"),
			true);
	public static final long ENTRYID1_COLUMN_BITMASK = 1L;
	public static final long ENTRYID2_COLUMN_BITMASK = 2L;
	public static final long TYPE_COLUMN_BITMASK = 4L;
	public static final long WEIGHT_COLUMN_BITMASK = 8L;
	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.portal.util.PropsUtil.get(
				"lock.expiration.time.com.liferay.portlet.asset.model.AssetLink"));

	public AssetLinkModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _linkId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setLinkId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _linkId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return AssetLink.class;
	}

	@Override
	public String getModelClassName() {
		return AssetLink.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("linkId", getLinkId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("entryId1", getEntryId1());
		attributes.put("entryId2", getEntryId2());
		attributes.put("type", getType());
		attributes.put("weight", getWeight());
		attributes.put("companyId", getCompanyId());

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long linkId = (Long)attributes.get("linkId");

		if (linkId != null) {
			setLinkId(linkId);
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

		Long entryId1 = (Long)attributes.get("entryId1");

		if (entryId1 != null) {
			setEntryId1(entryId1);
		}

		Long entryId2 = (Long)attributes.get("entryId2");

		if (entryId2 != null) {
			setEntryId2(entryId2);
		}

		Integer type = (Integer)attributes.get("type");

		if (type != null) {
			setType(type);
		}

		Integer weight = (Integer)attributes.get("weight");

		if (weight != null) {
			setWeight(weight);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}
	}

	@Override
	public long getLinkId() {
		return _linkId;
	}

	@Override
	public void setLinkId(long linkId) {
		_linkId = linkId;
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
			return StringPool.BLANK;
		}
	}

	@Override
	public void setUserUuid(String userUuid) {
	}

	@Override
	public String getUserName() {
		if (_userName == null) {
			return StringPool.BLANK;
		}
		else {
			return _userName;
		}
	}

	@Override
	public void setUserName(String userName) {
		_userName = userName;
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
	public long getEntryId1() {
		return _entryId1;
	}

	@Override
	public void setEntryId1(long entryId1) {
		_columnBitmask |= ENTRYID1_COLUMN_BITMASK;

		if (!_setOriginalEntryId1) {
			_setOriginalEntryId1 = true;

			_originalEntryId1 = _entryId1;
		}

		_entryId1 = entryId1;
	}

	public long getOriginalEntryId1() {
		return _originalEntryId1;
	}

	@Override
	public long getEntryId2() {
		return _entryId2;
	}

	@Override
	public void setEntryId2(long entryId2) {
		_columnBitmask |= ENTRYID2_COLUMN_BITMASK;

		if (!_setOriginalEntryId2) {
			_setOriginalEntryId2 = true;

			_originalEntryId2 = _entryId2;
		}

		_entryId2 = entryId2;
	}

	public long getOriginalEntryId2() {
		return _originalEntryId2;
	}

	@Override
	public int getType() {
		return _type;
	}

	@Override
	public void setType(int type) {
		_columnBitmask |= TYPE_COLUMN_BITMASK;

		if (!_setOriginalType) {
			_setOriginalType = true;

			_originalType = _type;
		}

		_type = type;
	}

	public int getOriginalType() {
		return _originalType;
	}

	@Override
	public int getWeight() {
		return _weight;
	}

	@Override
	public void setWeight(int weight) {
		_columnBitmask = -1L;

		_weight = weight;
	}

	@Override
	public long getCompanyId() {
		return _companyId;
	}

	@Override
	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(getCompanyId(),
			AssetLink.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public AssetLink toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (AssetLink)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelInterfaces, new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		AssetLinkImpl assetLinkImpl = new AssetLinkImpl();

		assetLinkImpl.setLinkId(getLinkId());
		assetLinkImpl.setUserId(getUserId());
		assetLinkImpl.setUserName(getUserName());
		assetLinkImpl.setCreateDate(getCreateDate());
		assetLinkImpl.setEntryId1(getEntryId1());
		assetLinkImpl.setEntryId2(getEntryId2());
		assetLinkImpl.setType(getType());
		assetLinkImpl.setWeight(getWeight());
		assetLinkImpl.setCompanyId(getCompanyId());

		assetLinkImpl.resetOriginalValues();

		return assetLinkImpl;
	}

	@Override
	public int compareTo(AssetLink assetLink) {
		int value = 0;

		if (getWeight() < assetLink.getWeight()) {
			value = -1;
		}
		else if (getWeight() > assetLink.getWeight()) {
			value = 1;
		}
		else {
			value = 0;
		}

		if (value != 0) {
			return value;
		}

		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof AssetLink)) {
			return false;
		}

		AssetLink assetLink = (AssetLink)obj;

		long primaryKey = assetLink.getPrimaryKey();

		if (getPrimaryKey() == primaryKey) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (int)getPrimaryKey();
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
		AssetLinkModelImpl assetLinkModelImpl = this;

		assetLinkModelImpl._originalEntryId1 = assetLinkModelImpl._entryId1;

		assetLinkModelImpl._setOriginalEntryId1 = false;

		assetLinkModelImpl._originalEntryId2 = assetLinkModelImpl._entryId2;

		assetLinkModelImpl._setOriginalEntryId2 = false;

		assetLinkModelImpl._originalType = assetLinkModelImpl._type;

		assetLinkModelImpl._setOriginalType = false;

		assetLinkModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<AssetLink> toCacheModel() {
		AssetLinkCacheModel assetLinkCacheModel = new AssetLinkCacheModel();

		assetLinkCacheModel.linkId = getLinkId();

		assetLinkCacheModel.userId = getUserId();

		assetLinkCacheModel.userName = getUserName();

		String userName = assetLinkCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			assetLinkCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			assetLinkCacheModel.createDate = createDate.getTime();
		}
		else {
			assetLinkCacheModel.createDate = Long.MIN_VALUE;
		}

		assetLinkCacheModel.entryId1 = getEntryId1();

		assetLinkCacheModel.entryId2 = getEntryId2();

		assetLinkCacheModel.type = getType();

		assetLinkCacheModel.weight = getWeight();

		assetLinkCacheModel.companyId = getCompanyId();

		return assetLinkCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(19);

		sb.append("{linkId=");
		sb.append(getLinkId());
		sb.append(", userId=");
		sb.append(getUserId());
		sb.append(", userName=");
		sb.append(getUserName());
		sb.append(", createDate=");
		sb.append(getCreateDate());
		sb.append(", entryId1=");
		sb.append(getEntryId1());
		sb.append(", entryId2=");
		sb.append(getEntryId2());
		sb.append(", type=");
		sb.append(getType());
		sb.append(", weight=");
		sb.append(getWeight());
		sb.append(", companyId=");
		sb.append(getCompanyId());
		sb.append("}");

		return sb.toString();
	}

	@Override
	public String toXmlString() {
		StringBundler sb = new StringBundler(31);

		sb.append("<model><model-name>");
		sb.append("com.liferay.portlet.asset.model.AssetLink");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>linkId</column-name><column-value><![CDATA[");
		sb.append(getLinkId());
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
			"<column><column-name>entryId1</column-name><column-value><![CDATA[");
		sb.append(getEntryId1());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>entryId2</column-name><column-value><![CDATA[");
		sb.append(getEntryId2());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>type</column-name><column-value><![CDATA[");
		sb.append(getType());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>weight</column-name><column-value><![CDATA[");
		sb.append(getWeight());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>companyId</column-name><column-value><![CDATA[");
		sb.append(getCompanyId());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static final ClassLoader _classLoader = AssetLink.class.getClassLoader();
	private static final Class<?>[] _escapedModelInterfaces = new Class[] {
			AssetLink.class
		};
	private long _linkId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private long _entryId1;
	private long _originalEntryId1;
	private boolean _setOriginalEntryId1;
	private long _entryId2;
	private long _originalEntryId2;
	private boolean _setOriginalEntryId2;
	private int _type;
	private int _originalType;
	private boolean _setOriginalType;
	private int _weight;
	private long _companyId;
	private long _columnBitmask;
	private AssetLink _escapedModel;
}