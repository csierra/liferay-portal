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

package com.liferay.depot.model.impl;

import com.liferay.depot.model.DepotAppCustomization;
import com.liferay.depot.model.DepotAppCustomizationModel;
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
 * The base model implementation for the DepotAppCustomization service. Represents a row in the &quot;DepotAppCustomization&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface <code>DepotAppCustomizationModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link DepotAppCustomizationImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see DepotAppCustomizationImpl
 * @generated
 */
public class DepotAppCustomizationModelImpl
	extends BaseModelImpl<DepotAppCustomization>
	implements DepotAppCustomizationModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a depot app customization model instance should use the <code>DepotAppCustomization</code> interface instead.
	 */
	public static final String TABLE_NAME = "DepotAppCustomization";

	public static final Object[][] TABLE_COLUMNS = {
		{"mvccVersion", Types.BIGINT},
		{"depotAppCustomizationId", Types.BIGINT}, {"companyId", Types.BIGINT},
		{"depotEntryId", Types.BIGINT}, {"enabled", Types.BOOLEAN},
		{"portletId", Types.VARCHAR}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("mvccVersion", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("depotAppCustomizationId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("depotEntryId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("enabled", Types.BOOLEAN);
		TABLE_COLUMNS_MAP.put("portletId", Types.VARCHAR);
	}

	public static final String TABLE_SQL_CREATE =
		"create table DepotAppCustomization (mvccVersion LONG default 0 not null,depotAppCustomizationId LONG not null primary key,companyId LONG,depotEntryId LONG,enabled BOOLEAN,portletId VARCHAR(75) null)";

	public static final String TABLE_SQL_DROP =
		"drop table DepotAppCustomization";

	public static final String ORDER_BY_JPQL =
		" ORDER BY depotAppCustomization.depotAppCustomizationId ASC";

	public static final String ORDER_BY_SQL =
		" ORDER BY DepotAppCustomization.depotAppCustomizationId ASC";

	public static final String DATA_SOURCE = "liferayDataSource";

	public static final String SESSION_FACTORY = "liferaySessionFactory";

	public static final String TX_MANAGER = "liferayTransactionManager";

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)
	 */
	@Deprecated
	public static final long DEPOTENTRYID_COLUMN_BITMASK = 1L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)
	 */
	@Deprecated
	public static final long ENABLED_COLUMN_BITMASK = 2L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)
	 */
	@Deprecated
	public static final long PORTLETID_COLUMN_BITMASK = 4L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *		#getColumnBitmask(String)
	 */
	@Deprecated
	public static final long DEPOTAPPCUSTOMIZATIONID_COLUMN_BITMASK = 8L;

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

	public DepotAppCustomizationModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _depotAppCustomizationId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setDepotAppCustomizationId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _depotAppCustomizationId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return DepotAppCustomization.class;
	}

	@Override
	public String getModelClassName() {
		return DepotAppCustomization.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<DepotAppCustomization, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		for (Map.Entry<String, Function<DepotAppCustomization, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<DepotAppCustomization, Object> attributeGetterFunction =
				entry.getValue();

			attributes.put(
				attributeName,
				attributeGetterFunction.apply((DepotAppCustomization)this));
		}

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<DepotAppCustomization, Object>>
			attributeSetterBiConsumers = getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<DepotAppCustomization, Object>
				attributeSetterBiConsumer = attributeSetterBiConsumers.get(
					attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(DepotAppCustomization)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<DepotAppCustomization, Object>>
		getAttributeGetterFunctions() {

		return _attributeGetterFunctions;
	}

	public Map<String, BiConsumer<DepotAppCustomization, Object>>
		getAttributeSetterBiConsumers() {

		return _attributeSetterBiConsumers;
	}

	private static Function<InvocationHandler, DepotAppCustomization>
		_getProxyProviderFunction() {

		Class<?> proxyClass = ProxyUtil.getProxyClass(
			DepotAppCustomization.class.getClassLoader(),
			DepotAppCustomization.class, ModelWrapper.class);

		try {
			Constructor<DepotAppCustomization> constructor =
				(Constructor<DepotAppCustomization>)proxyClass.getConstructor(
					InvocationHandler.class);

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

	private static final Map<String, Function<DepotAppCustomization, Object>>
		_attributeGetterFunctions;
	private static final Map<String, BiConsumer<DepotAppCustomization, Object>>
		_attributeSetterBiConsumers;

	static {
		Map<String, Function<DepotAppCustomization, Object>>
			attributeGetterFunctions =
				new LinkedHashMap
					<String, Function<DepotAppCustomization, Object>>();
		Map<String, BiConsumer<DepotAppCustomization, ?>>
			attributeSetterBiConsumers =
				new LinkedHashMap
					<String, BiConsumer<DepotAppCustomization, ?>>();

		attributeGetterFunctions.put(
			"mvccVersion", DepotAppCustomization::getMvccVersion);
		attributeSetterBiConsumers.put(
			"mvccVersion",
			(BiConsumer<DepotAppCustomization, Long>)
				DepotAppCustomization::setMvccVersion);
		attributeGetterFunctions.put(
			"depotAppCustomizationId",
			DepotAppCustomization::getDepotAppCustomizationId);
		attributeSetterBiConsumers.put(
			"depotAppCustomizationId",
			(BiConsumer<DepotAppCustomization, Long>)
				DepotAppCustomization::setDepotAppCustomizationId);
		attributeGetterFunctions.put(
			"companyId", DepotAppCustomization::getCompanyId);
		attributeSetterBiConsumers.put(
			"companyId",
			(BiConsumer<DepotAppCustomization, Long>)
				DepotAppCustomization::setCompanyId);
		attributeGetterFunctions.put(
			"depotEntryId", DepotAppCustomization::getDepotEntryId);
		attributeSetterBiConsumers.put(
			"depotEntryId",
			(BiConsumer<DepotAppCustomization, Long>)
				DepotAppCustomization::setDepotEntryId);
		attributeGetterFunctions.put(
			"enabled", DepotAppCustomization::getEnabled);
		attributeSetterBiConsumers.put(
			"enabled",
			(BiConsumer<DepotAppCustomization, Boolean>)
				DepotAppCustomization::setEnabled);
		attributeGetterFunctions.put(
			"portletId", DepotAppCustomization::getPortletId);
		attributeSetterBiConsumers.put(
			"portletId",
			(BiConsumer<DepotAppCustomization, String>)
				DepotAppCustomization::setPortletId);

		_attributeGetterFunctions = Collections.unmodifiableMap(
			attributeGetterFunctions);
		_attributeSetterBiConsumers = Collections.unmodifiableMap(
			(Map)attributeSetterBiConsumers);
	}

	@Override
	public long getMvccVersion() {
		return _mvccVersion;
	}

	@Override
	public void setMvccVersion(long mvccVersion) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_mvccVersion = mvccVersion;
	}

	@Override
	public long getDepotAppCustomizationId() {
		return _depotAppCustomizationId;
	}

	@Override
	public void setDepotAppCustomizationId(long depotAppCustomizationId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_depotAppCustomizationId = depotAppCustomizationId;
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
	public long getDepotEntryId() {
		return _depotEntryId;
	}

	@Override
	public void setDepotEntryId(long depotEntryId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_depotEntryId = depotEntryId;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public long getOriginalDepotEntryId() {
		return GetterUtil.getLong(getColumnOriginalValue("depotEntryId"));
	}

	@Override
	public boolean getEnabled() {
		return _enabled;
	}

	@Override
	public boolean isEnabled() {
		return _enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_enabled = enabled;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public boolean getOriginalEnabled() {
		return GetterUtil.getBoolean(getColumnOriginalValue("enabled"));
	}

	@Override
	public String getPortletId() {
		if (_portletId == null) {
			return "";
		}
		else {
			return _portletId;
		}
	}

	@Override
	public void setPortletId(String portletId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_portletId = portletId;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public String getOriginalPortletId() {
		return getColumnOriginalValue("portletId");
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
			getCompanyId(), DepotAppCustomization.class.getName(),
			getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public DepotAppCustomization toEscapedModel() {
		if (_escapedModel == null) {
			Function<InvocationHandler, DepotAppCustomization>
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
		DepotAppCustomizationImpl depotAppCustomizationImpl =
			new DepotAppCustomizationImpl();

		depotAppCustomizationImpl.setMvccVersion(getMvccVersion());
		depotAppCustomizationImpl.setDepotAppCustomizationId(
			getDepotAppCustomizationId());
		depotAppCustomizationImpl.setCompanyId(getCompanyId());
		depotAppCustomizationImpl.setDepotEntryId(getDepotEntryId());
		depotAppCustomizationImpl.setEnabled(isEnabled());
		depotAppCustomizationImpl.setPortletId(getPortletId());

		depotAppCustomizationImpl.resetOriginalValues();

		return depotAppCustomizationImpl;
	}

	@Override
	public int compareTo(DepotAppCustomization depotAppCustomization) {
		long primaryKey = depotAppCustomization.getPrimaryKey();

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

		if (!(object instanceof DepotAppCustomization)) {
			return false;
		}

		DepotAppCustomization depotAppCustomization =
			(DepotAppCustomization)object;

		long primaryKey = depotAppCustomization.getPrimaryKey();

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
	public CacheModel<DepotAppCustomization> toCacheModel() {
		DepotAppCustomizationCacheModel depotAppCustomizationCacheModel =
			new DepotAppCustomizationCacheModel();

		depotAppCustomizationCacheModel.mvccVersion = getMvccVersion();

		depotAppCustomizationCacheModel.depotAppCustomizationId =
			getDepotAppCustomizationId();

		depotAppCustomizationCacheModel.companyId = getCompanyId();

		depotAppCustomizationCacheModel.depotEntryId = getDepotEntryId();

		depotAppCustomizationCacheModel.enabled = isEnabled();

		depotAppCustomizationCacheModel.portletId = getPortletId();

		String portletId = depotAppCustomizationCacheModel.portletId;

		if ((portletId != null) && (portletId.length() == 0)) {
			depotAppCustomizationCacheModel.portletId = null;
		}

		return depotAppCustomizationCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<DepotAppCustomization, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			4 * attributeGetterFunctions.size() + 2);

		sb.append("{");

		for (Map.Entry<String, Function<DepotAppCustomization, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<DepotAppCustomization, Object> attributeGetterFunction =
				entry.getValue();

			sb.append(attributeName);
			sb.append("=");
			sb.append(
				attributeGetterFunction.apply((DepotAppCustomization)this));
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
		Map<String, Function<DepotAppCustomization, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			5 * attributeGetterFunctions.size() + 4);

		sb.append("<model><model-name>");
		sb.append(getModelClassName());
		sb.append("</model-name>");

		for (Map.Entry<String, Function<DepotAppCustomization, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<DepotAppCustomization, Object> attributeGetterFunction =
				entry.getValue();

			sb.append("<column><column-name>");
			sb.append(attributeName);
			sb.append("</column-name><column-value><![CDATA[");
			sb.append(
				attributeGetterFunction.apply((DepotAppCustomization)this));
			sb.append("]]></column-value></column>");
		}

		sb.append("</model>");

		return sb.toString();
	}

	private static class EscapedModelProxyProviderFunctionHolder {

		private static final Function<InvocationHandler, DepotAppCustomization>
			_escapedModelProxyProviderFunction = _getProxyProviderFunction();

	}

	private long _mvccVersion;
	private long _depotAppCustomizationId;
	private long _companyId;
	private long _depotEntryId;
	private boolean _enabled;
	private String _portletId;

	public <T> T getColumnValue(String columnName) {
		Function<DepotAppCustomization, Object> function =
			_attributeGetterFunctions.get(columnName);

		if (function == null) {
			throw new IllegalArgumentException(
				"No attribute getter function found for " + columnName);
		}

		return (T)function.apply((DepotAppCustomization)this);
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

		_columnOriginalValues.put("mvccVersion", _mvccVersion);
		_columnOriginalValues.put(
			"depotAppCustomizationId", _depotAppCustomizationId);
		_columnOriginalValues.put("companyId", _companyId);
		_columnOriginalValues.put("depotEntryId", _depotEntryId);
		_columnOriginalValues.put("enabled", _enabled);
		_columnOriginalValues.put("portletId", _portletId);
	}

	private transient Map<String, Object> _columnOriginalValues;

	public static long getColumnBitmask(String columnName) {
		return _columnBitmasks.get(columnName);
	}

	private static final Map<String, Long> _columnBitmasks;

	static {
		Map<String, Long> columnBitmasks = new HashMap<>();

		columnBitmasks.put("mvccVersion", 1L);

		columnBitmasks.put("depotAppCustomizationId", 2L);

		columnBitmasks.put("companyId", 4L);

		columnBitmasks.put("depotEntryId", 8L);

		columnBitmasks.put("enabled", 16L);

		columnBitmasks.put("portletId", 32L);

		_columnBitmasks = Collections.unmodifiableMap(columnBitmasks);
	}

	private long _columnBitmask;
	private DepotAppCustomization _escapedModel;

}