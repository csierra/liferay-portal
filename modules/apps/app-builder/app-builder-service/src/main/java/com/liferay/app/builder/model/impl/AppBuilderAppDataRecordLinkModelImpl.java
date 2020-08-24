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

package com.liferay.app.builder.model.impl;

import com.liferay.app.builder.model.AppBuilderAppDataRecordLink;
import com.liferay.app.builder.model.AppBuilderAppDataRecordLinkModel;
import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;

import java.io.Serializable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;

import java.sql.Types;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * The base model implementation for the AppBuilderAppDataRecordLink service. Represents a row in the &quot;AppBuilderAppDataRecordLink&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface <code>AppBuilderAppDataRecordLinkModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link AppBuilderAppDataRecordLinkImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AppBuilderAppDataRecordLinkImpl
 * @generated
 */
public class AppBuilderAppDataRecordLinkModelImpl
	extends BaseModelImpl<AppBuilderAppDataRecordLink>
	implements AppBuilderAppDataRecordLinkModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a app builder app data record link model instance should use the <code>AppBuilderAppDataRecordLink</code> interface instead.
	 */
	public static final String TABLE_NAME = "AppBuilderAppDataRecordLink";

	public static final Object[][] TABLE_COLUMNS = {
		{"appBuilderAppDataRecordLinkId", Types.BIGINT},
		{"groupId", Types.BIGINT}, {"companyId", Types.BIGINT},
		{"appBuilderAppId", Types.BIGINT},
		{"appBuilderAppVersionId", Types.BIGINT}, {"ddlRecordId", Types.BIGINT}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("appBuilderAppDataRecordLinkId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("groupId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("appBuilderAppId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("appBuilderAppVersionId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("ddlRecordId", Types.BIGINT);
	}

	public static final String TABLE_SQL_CREATE =
		"create table AppBuilderAppDataRecordLink (appBuilderAppDataRecordLinkId LONG not null primary key,groupId LONG,companyId LONG,appBuilderAppId LONG,appBuilderAppVersionId LONG,ddlRecordId LONG)";

	public static final String TABLE_SQL_DROP =
		"drop table AppBuilderAppDataRecordLink";

	public static final String ORDER_BY_JPQL =
		" ORDER BY appBuilderAppDataRecordLink.appBuilderAppDataRecordLinkId ASC";

	public static final String ORDER_BY_SQL =
		" ORDER BY AppBuilderAppDataRecordLink.appBuilderAppDataRecordLinkId ASC";

	public static final String DATA_SOURCE = "liferayDataSource";

	public static final String SESSION_FACTORY = "liferaySessionFactory";

	public static final String TX_MANAGER = "liferayTransactionManager";

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)
	 */
	@Deprecated
	public static final long APPBUILDERAPPID_COLUMN_BITMASK = 1L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)
	 */
	@Deprecated
	public static final long DDLRECORDID_COLUMN_BITMASK = 2L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *		#getColumnBitmask(String)
	 */
	@Deprecated
	public static final long APPBUILDERAPPDATARECORDLINKID_COLUMN_BITMASK = 4L;

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	public static void setEntityCacheEnabled(boolean entityCacheEnabled) {
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	public static void setFinderCacheEnabled(boolean finderCacheEnabled) {
	}

	public AppBuilderAppDataRecordLinkModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _appBuilderAppDataRecordLinkId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setAppBuilderAppDataRecordLinkId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _appBuilderAppDataRecordLinkId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return AppBuilderAppDataRecordLink.class;
	}

	@Override
	public String getModelClassName() {
		return AppBuilderAppDataRecordLink.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<AppBuilderAppDataRecordLink, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		for (Map.Entry<String, Function<AppBuilderAppDataRecordLink, Object>>
				entry : attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<AppBuilderAppDataRecordLink, Object>
				attributeGetterFunction = entry.getValue();

			attributes.put(
				attributeName,
				attributeGetterFunction.apply(
					(AppBuilderAppDataRecordLink)this));
		}

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<AppBuilderAppDataRecordLink, Object>>
			attributeSetterBiConsumers = getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<AppBuilderAppDataRecordLink, Object>
				attributeSetterBiConsumer = attributeSetterBiConsumers.get(
					attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(AppBuilderAppDataRecordLink)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<AppBuilderAppDataRecordLink, Object>>
		getAttributeGetterFunctions() {

		return _attributeGetterFunctions;
	}

	public Map<String, BiConsumer<AppBuilderAppDataRecordLink, Object>>
		getAttributeSetterBiConsumers() {

		return _attributeSetterBiConsumers;
	}

	private static Function<InvocationHandler, AppBuilderAppDataRecordLink>
		_getProxyProviderFunction() {

		Class<?> proxyClass = ProxyUtil.getProxyClass(
			AppBuilderAppDataRecordLink.class.getClassLoader(),
			AppBuilderAppDataRecordLink.class, ModelWrapper.class);

		try {
			Constructor<AppBuilderAppDataRecordLink> constructor =
				(Constructor<AppBuilderAppDataRecordLink>)
					proxyClass.getConstructor(InvocationHandler.class);

			return invocationHandler -> {
				try {
					return constructor.newInstance(invocationHandler);
				}
				catch (ReflectiveOperationException
							reflectiveOperationException) {

					throw new InternalError(reflectiveOperationException);
				}
			};
		}
		catch (NoSuchMethodException noSuchMethodException) {
			throw new InternalError(noSuchMethodException);
		}
	}

	private static final Map
		<String, Function<AppBuilderAppDataRecordLink, Object>>
			_attributeGetterFunctions;
	private static final Map
		<String, BiConsumer<AppBuilderAppDataRecordLink, Object>>
			_attributeSetterBiConsumers;

	static {
		Map<String, Function<AppBuilderAppDataRecordLink, Object>>
			attributeGetterFunctions =
				new LinkedHashMap
					<String, Function<AppBuilderAppDataRecordLink, Object>>();
		Map<String, BiConsumer<AppBuilderAppDataRecordLink, ?>>
			attributeSetterBiConsumers =
				new LinkedHashMap
					<String, BiConsumer<AppBuilderAppDataRecordLink, ?>>();

		attributeGetterFunctions.put(
			"appBuilderAppDataRecordLinkId",
			AppBuilderAppDataRecordLink::getAppBuilderAppDataRecordLinkId);
		attributeSetterBiConsumers.put(
			"appBuilderAppDataRecordLinkId",
			(BiConsumer<AppBuilderAppDataRecordLink, Long>)
				AppBuilderAppDataRecordLink::setAppBuilderAppDataRecordLinkId);
		attributeGetterFunctions.put(
			"groupId", AppBuilderAppDataRecordLink::getGroupId);
		attributeSetterBiConsumers.put(
			"groupId",
			(BiConsumer<AppBuilderAppDataRecordLink, Long>)
				AppBuilderAppDataRecordLink::setGroupId);
		attributeGetterFunctions.put(
			"companyId", AppBuilderAppDataRecordLink::getCompanyId);
		attributeSetterBiConsumers.put(
			"companyId",
			(BiConsumer<AppBuilderAppDataRecordLink, Long>)
				AppBuilderAppDataRecordLink::setCompanyId);
		attributeGetterFunctions.put(
			"appBuilderAppId", AppBuilderAppDataRecordLink::getAppBuilderAppId);
		attributeSetterBiConsumers.put(
			"appBuilderAppId",
			(BiConsumer<AppBuilderAppDataRecordLink, Long>)
				AppBuilderAppDataRecordLink::setAppBuilderAppId);
		attributeGetterFunctions.put(
			"appBuilderAppVersionId",
			AppBuilderAppDataRecordLink::getAppBuilderAppVersionId);
		attributeSetterBiConsumers.put(
			"appBuilderAppVersionId",
			(BiConsumer<AppBuilderAppDataRecordLink, Long>)
				AppBuilderAppDataRecordLink::setAppBuilderAppVersionId);
		attributeGetterFunctions.put(
			"ddlRecordId", AppBuilderAppDataRecordLink::getDdlRecordId);
		attributeSetterBiConsumers.put(
			"ddlRecordId",
			(BiConsumer<AppBuilderAppDataRecordLink, Long>)
				AppBuilderAppDataRecordLink::setDdlRecordId);

		_attributeGetterFunctions = Collections.unmodifiableMap(
			attributeGetterFunctions);
		_attributeSetterBiConsumers = Collections.unmodifiableMap(
			(Map)attributeSetterBiConsumers);
	}

	@Override
	public long getAppBuilderAppDataRecordLinkId() {
		return _appBuilderAppDataRecordLinkId;
	}

	@Override
	public void setAppBuilderAppDataRecordLinkId(
		long appBuilderAppDataRecordLinkId) {

		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_appBuilderAppDataRecordLinkId = appBuilderAppDataRecordLinkId;
	}

	@Override
	public long getGroupId() {
		return _groupId;
	}

	@Override
	public void setGroupId(long groupId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_groupId = groupId;
	}

	@Override
	public long getCompanyId() {
		return _companyId;
	}

	@Override
	public void setCompanyId(long companyId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_companyId = companyId;
	}

	@Override
	public long getAppBuilderAppId() {
		return _appBuilderAppId;
	}

	@Override
	public void setAppBuilderAppId(long appBuilderAppId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_appBuilderAppId = appBuilderAppId;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public long getOriginalAppBuilderAppId() {
		return GetterUtil.getLong(getColumnOriginalValue("appBuilderAppId"));
	}

	@Override
	public long getAppBuilderAppVersionId() {
		return _appBuilderAppVersionId;
	}

	@Override
	public void setAppBuilderAppVersionId(long appBuilderAppVersionId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_appBuilderAppVersionId = appBuilderAppVersionId;
	}

	@Override
	public long getDdlRecordId() {
		return _ddlRecordId;
	}

	@Override
	public void setDdlRecordId(long ddlRecordId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_ddlRecordId = ddlRecordId;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public long getOriginalDdlRecordId() {
		return GetterUtil.getLong(getColumnOriginalValue("ddlRecordId"));
	}

	public long getColumnBitmask() {
		if (_columnBitmask > 0) {
			return _columnBitmask;
		}

		if ((_columnOriginalValues == null) ||
			(_columnOriginalValues == Collections.EMPTY_MAP)) {

			return 0;
		}

		for (Map.Entry<String, Object> entry :
				_columnOriginalValues.entrySet()) {

			if (entry.getValue() != getColumnValue(entry.getKey())) {
				_columnBitmask |= _columnBitmasks.get(entry.getKey());
			}
		}

		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(
			getCompanyId(), AppBuilderAppDataRecordLink.class.getName(),
			getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public AppBuilderAppDataRecordLink toEscapedModel() {
		if (_escapedModel == null) {
			Function<InvocationHandler, AppBuilderAppDataRecordLink>
				escapedModelProxyProviderFunction =
					EscapedModelProxyProviderFunctionHolder.
						_escapedModelProxyProviderFunction;

			_escapedModel = escapedModelProxyProviderFunction.apply(
				new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		AppBuilderAppDataRecordLinkImpl appBuilderAppDataRecordLinkImpl =
			new AppBuilderAppDataRecordLinkImpl();

		appBuilderAppDataRecordLinkImpl.setAppBuilderAppDataRecordLinkId(
			getAppBuilderAppDataRecordLinkId());
		appBuilderAppDataRecordLinkImpl.setGroupId(getGroupId());
		appBuilderAppDataRecordLinkImpl.setCompanyId(getCompanyId());
		appBuilderAppDataRecordLinkImpl.setAppBuilderAppId(
			getAppBuilderAppId());
		appBuilderAppDataRecordLinkImpl.setAppBuilderAppVersionId(
			getAppBuilderAppVersionId());
		appBuilderAppDataRecordLinkImpl.setDdlRecordId(getDdlRecordId());

		appBuilderAppDataRecordLinkImpl.resetOriginalValues();

		return appBuilderAppDataRecordLinkImpl;
	}

	@Override
	public int compareTo(
		AppBuilderAppDataRecordLink appBuilderAppDataRecordLink) {

		long primaryKey = appBuilderAppDataRecordLink.getPrimaryKey();

		if (getPrimaryKey() < primaryKey) {
			return -1;
		}
		else if (getPrimaryKey() > primaryKey) {
			return 1;
		}
		else {
			return 0;
		}
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof AppBuilderAppDataRecordLink)) {
			return false;
		}

		AppBuilderAppDataRecordLink appBuilderAppDataRecordLink =
			(AppBuilderAppDataRecordLink)object;

		long primaryKey = appBuilderAppDataRecordLink.getPrimaryKey();

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

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	@Override
	public boolean isEntityCacheEnabled() {
		return true;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	@Override
	public boolean isFinderCacheEnabled() {
		return true;
	}

	@Override
	public void resetOriginalValues() {
		_columnOriginalValues = Collections.emptyMap();

		_columnBitmask = 0;
	}

	@Override
	public CacheModel<AppBuilderAppDataRecordLink> toCacheModel() {
		AppBuilderAppDataRecordLinkCacheModel
			appBuilderAppDataRecordLinkCacheModel =
				new AppBuilderAppDataRecordLinkCacheModel();

		appBuilderAppDataRecordLinkCacheModel.appBuilderAppDataRecordLinkId =
			getAppBuilderAppDataRecordLinkId();

		appBuilderAppDataRecordLinkCacheModel.groupId = getGroupId();

		appBuilderAppDataRecordLinkCacheModel.companyId = getCompanyId();

		appBuilderAppDataRecordLinkCacheModel.appBuilderAppId =
			getAppBuilderAppId();

		appBuilderAppDataRecordLinkCacheModel.appBuilderAppVersionId =
			getAppBuilderAppVersionId();

		appBuilderAppDataRecordLinkCacheModel.ddlRecordId = getDdlRecordId();

		return appBuilderAppDataRecordLinkCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<AppBuilderAppDataRecordLink, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			4 * attributeGetterFunctions.size() + 2);

		sb.append("{");

		for (Map.Entry<String, Function<AppBuilderAppDataRecordLink, Object>>
				entry : attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<AppBuilderAppDataRecordLink, Object>
				attributeGetterFunction = entry.getValue();

			sb.append(attributeName);
			sb.append("=");
			sb.append(
				attributeGetterFunction.apply(
					(AppBuilderAppDataRecordLink)this));
			sb.append(", ");
		}

		if (sb.index() > 1) {
			sb.setIndex(sb.index() - 1);
		}

		sb.append("}");

		return sb.toString();
	}

	@Override
	public String toXmlString() {
		Map<String, Function<AppBuilderAppDataRecordLink, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			5 * attributeGetterFunctions.size() + 4);

		sb.append("<model><model-name>");
		sb.append(getModelClassName());
		sb.append("</model-name>");

		for (Map.Entry<String, Function<AppBuilderAppDataRecordLink, Object>>
				entry : attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<AppBuilderAppDataRecordLink, Object>
				attributeGetterFunction = entry.getValue();

			sb.append("<column><column-name>");
			sb.append(attributeName);
			sb.append("</column-name><column-value><![CDATA[");
			sb.append(
				attributeGetterFunction.apply(
					(AppBuilderAppDataRecordLink)this));
			sb.append("]]></column-value></column>");
		}

		sb.append("</model>");

		return sb.toString();
	}

	private static class EscapedModelProxyProviderFunctionHolder {

		private static final Function
			<InvocationHandler, AppBuilderAppDataRecordLink>
				_escapedModelProxyProviderFunction =
					_getProxyProviderFunction();

	}

	private long _appBuilderAppDataRecordLinkId;
	private long _groupId;
	private long _companyId;
	private long _appBuilderAppId;
	private long _appBuilderAppVersionId;
	private long _ddlRecordId;

	public <T> T getColumnValue(String columnName) {
		Function<AppBuilderAppDataRecordLink, Object> function =
			_attributeGetterFunctions.get(columnName);

		if (function == null) {
			throw new IllegalArgumentException(
				"No attribute getter function found for " + columnName);
		}

		return (T)function.apply((AppBuilderAppDataRecordLink)this);
	}

	public <T> T getColumnOriginalValue(String columnName) {
		if (_columnOriginalValues == null) {
			return null;
		}

		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		return (T)_columnOriginalValues.get(columnName);
	}

	private void _setColumnOriginalValues() {
		_columnOriginalValues = new HashMap<String, Object>();

		_columnOriginalValues.put(
			"appBuilderAppDataRecordLinkId", _appBuilderAppDataRecordLinkId);
		_columnOriginalValues.put("groupId", _groupId);
		_columnOriginalValues.put("companyId", _companyId);
		_columnOriginalValues.put("appBuilderAppId", _appBuilderAppId);
		_columnOriginalValues.put(
			"appBuilderAppVersionId", _appBuilderAppVersionId);
		_columnOriginalValues.put("ddlRecordId", _ddlRecordId);
	}

	private transient Map<String, Object> _columnOriginalValues;

	public static long getColumnBitmask(String columnName) {
		return _columnBitmasks.get(columnName);
	}

	private static final Map<String, Long> _columnBitmasks;

	static {
		Map<String, Long> columnBitmasks = new HashMap<>();

		columnBitmasks.put("appBuilderAppDataRecordLinkId", 1L);

		columnBitmasks.put("groupId", 2L);

		columnBitmasks.put("companyId", 4L);

		columnBitmasks.put("appBuilderAppId", 8L);

		columnBitmasks.put("appBuilderAppVersionId", 16L);

		columnBitmasks.put("ddlRecordId", 32L);

		_columnBitmasks = Collections.unmodifiableMap(columnBitmasks);
	}

	private long _columnBitmask;
	private AppBuilderAppDataRecordLink _escapedModel;

}