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

package com.liferay.portal.workflow.kaleo.model.impl;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.workflow.kaleo.model.KaleoNotificationRecipient;
import com.liferay.portal.workflow.kaleo.model.KaleoNotificationRecipientModel;

import java.io.Serializable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;

import java.sql.Types;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * The base model implementation for the KaleoNotificationRecipient service. Represents a row in the &quot;KaleoNotificationRecipient&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface <code>KaleoNotificationRecipientModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link KaleoNotificationRecipientImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see KaleoNotificationRecipientImpl
 * @generated
 */
public class KaleoNotificationRecipientModelImpl
	extends BaseModelImpl<KaleoNotificationRecipient>
	implements KaleoNotificationRecipientModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a kaleo notification recipient model instance should use the <code>KaleoNotificationRecipient</code> interface instead.
	 */
	public static final String TABLE_NAME = "KaleoNotificationRecipient";

	public static final Object[][] TABLE_COLUMNS = {
		{"mvccVersion", Types.BIGINT},
		{"kaleoNotificationRecipientId", Types.BIGINT},
		{"groupId", Types.BIGINT}, {"companyId", Types.BIGINT},
		{"userId", Types.BIGINT}, {"userName", Types.VARCHAR},
		{"createDate", Types.TIMESTAMP}, {"modifiedDate", Types.TIMESTAMP},
		{"kaleoDefinitionId", Types.BIGINT},
		{"kaleoDefinitionVersionId", Types.BIGINT},
		{"kaleoNotificationId", Types.BIGINT},
		{"recipientClassName", Types.VARCHAR},
		{"recipientClassPK", Types.BIGINT},
		{"recipientRoleType", Types.INTEGER}, {"recipientScript", Types.CLOB},
		{"recipientScriptLanguage", Types.VARCHAR},
		{"recipientScriptContexts", Types.VARCHAR}, {"address", Types.VARCHAR},
		{"notificationReceptionType", Types.VARCHAR}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("mvccVersion", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("kaleoNotificationRecipientId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("groupId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("modifiedDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("kaleoDefinitionId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("kaleoDefinitionVersionId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("kaleoNotificationId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("recipientClassName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("recipientClassPK", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("recipientRoleType", Types.INTEGER);
		TABLE_COLUMNS_MAP.put("recipientScript", Types.CLOB);
		TABLE_COLUMNS_MAP.put("recipientScriptLanguage", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("recipientScriptContexts", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("address", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("notificationReceptionType", Types.VARCHAR);
	}

	public static final String TABLE_SQL_CREATE =
		"create table KaleoNotificationRecipient (mvccVersion LONG default 0 not null,kaleoNotificationRecipientId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,userName VARCHAR(200) null,createDate DATE null,modifiedDate DATE null,kaleoDefinitionId LONG,kaleoDefinitionVersionId LONG,kaleoNotificationId LONG,recipientClassName VARCHAR(200) null,recipientClassPK LONG,recipientRoleType INTEGER,recipientScript TEXT null,recipientScriptLanguage VARCHAR(75) null,recipientScriptContexts STRING null,address VARCHAR(255) null,notificationReceptionType VARCHAR(3) null)";

	public static final String TABLE_SQL_DROP =
		"drop table KaleoNotificationRecipient";

	public static final String ORDER_BY_JPQL =
		" ORDER BY kaleoNotificationRecipient.kaleoNotificationRecipientId ASC";

	public static final String ORDER_BY_SQL =
		" ORDER BY KaleoNotificationRecipient.kaleoNotificationRecipientId ASC";

	public static final String DATA_SOURCE = "liferayDataSource";

	public static final String SESSION_FACTORY = "liferaySessionFactory";

	public static final String TX_MANAGER = "liferayTransactionManager";

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)
	 */
	@Deprecated
	public static final long COMPANYID_COLUMN_BITMASK = 1L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)
	 */
	@Deprecated
	public static final long KALEODEFINITIONVERSIONID_COLUMN_BITMASK = 2L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)
	 */
	@Deprecated
	public static final long KALEONOTIFICATIONID_COLUMN_BITMASK = 4L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *		#getColumnBitmask(String)
	 */
	@Deprecated
	public static final long KALEONOTIFICATIONRECIPIENTID_COLUMN_BITMASK = 8L;

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

	public KaleoNotificationRecipientModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _kaleoNotificationRecipientId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setKaleoNotificationRecipientId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _kaleoNotificationRecipientId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return KaleoNotificationRecipient.class;
	}

	@Override
	public String getModelClassName() {
		return KaleoNotificationRecipient.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<KaleoNotificationRecipient, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		for (Map.Entry<String, Function<KaleoNotificationRecipient, Object>>
				entry : attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<KaleoNotificationRecipient, Object>
				attributeGetterFunction = entry.getValue();

			attributes.put(
				attributeName,
				attributeGetterFunction.apply(
					(KaleoNotificationRecipient)this));
		}

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<KaleoNotificationRecipient, Object>>
			attributeSetterBiConsumers = getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<KaleoNotificationRecipient, Object>
				attributeSetterBiConsumer = attributeSetterBiConsumers.get(
					attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(KaleoNotificationRecipient)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<KaleoNotificationRecipient, Object>>
		getAttributeGetterFunctions() {

		return _attributeGetterFunctions;
	}

	public Map<String, BiConsumer<KaleoNotificationRecipient, Object>>
		getAttributeSetterBiConsumers() {

		return _attributeSetterBiConsumers;
	}

	private static Function<InvocationHandler, KaleoNotificationRecipient>
		_getProxyProviderFunction() {

		Class<?> proxyClass = ProxyUtil.getProxyClass(
			KaleoNotificationRecipient.class.getClassLoader(),
			KaleoNotificationRecipient.class, ModelWrapper.class);

		try {
			Constructor<KaleoNotificationRecipient> constructor =
				(Constructor<KaleoNotificationRecipient>)
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
		<String, Function<KaleoNotificationRecipient, Object>>
			_attributeGetterFunctions;
	private static final Map
		<String, BiConsumer<KaleoNotificationRecipient, Object>>
			_attributeSetterBiConsumers;

	static {
		Map<String, Function<KaleoNotificationRecipient, Object>>
			attributeGetterFunctions =
				new LinkedHashMap
					<String, Function<KaleoNotificationRecipient, Object>>();
		Map<String, BiConsumer<KaleoNotificationRecipient, ?>>
			attributeSetterBiConsumers =
				new LinkedHashMap
					<String, BiConsumer<KaleoNotificationRecipient, ?>>();

		attributeGetterFunctions.put(
			"mvccVersion", KaleoNotificationRecipient::getMvccVersion);
		attributeSetterBiConsumers.put(
			"mvccVersion",
			(BiConsumer<KaleoNotificationRecipient, Long>)
				KaleoNotificationRecipient::setMvccVersion);
		attributeGetterFunctions.put(
			"kaleoNotificationRecipientId",
			KaleoNotificationRecipient::getKaleoNotificationRecipientId);
		attributeSetterBiConsumers.put(
			"kaleoNotificationRecipientId",
			(BiConsumer<KaleoNotificationRecipient, Long>)
				KaleoNotificationRecipient::setKaleoNotificationRecipientId);
		attributeGetterFunctions.put(
			"groupId", KaleoNotificationRecipient::getGroupId);
		attributeSetterBiConsumers.put(
			"groupId",
			(BiConsumer<KaleoNotificationRecipient, Long>)
				KaleoNotificationRecipient::setGroupId);
		attributeGetterFunctions.put(
			"companyId", KaleoNotificationRecipient::getCompanyId);
		attributeSetterBiConsumers.put(
			"companyId",
			(BiConsumer<KaleoNotificationRecipient, Long>)
				KaleoNotificationRecipient::setCompanyId);
		attributeGetterFunctions.put(
			"userId", KaleoNotificationRecipient::getUserId);
		attributeSetterBiConsumers.put(
			"userId",
			(BiConsumer<KaleoNotificationRecipient, Long>)
				KaleoNotificationRecipient::setUserId);
		attributeGetterFunctions.put(
			"userName", KaleoNotificationRecipient::getUserName);
		attributeSetterBiConsumers.put(
			"userName",
			(BiConsumer<KaleoNotificationRecipient, String>)
				KaleoNotificationRecipient::setUserName);
		attributeGetterFunctions.put(
			"createDate", KaleoNotificationRecipient::getCreateDate);
		attributeSetterBiConsumers.put(
			"createDate",
			(BiConsumer<KaleoNotificationRecipient, Date>)
				KaleoNotificationRecipient::setCreateDate);
		attributeGetterFunctions.put(
			"modifiedDate", KaleoNotificationRecipient::getModifiedDate);
		attributeSetterBiConsumers.put(
			"modifiedDate",
			(BiConsumer<KaleoNotificationRecipient, Date>)
				KaleoNotificationRecipient::setModifiedDate);
		attributeGetterFunctions.put(
			"kaleoDefinitionId",
			KaleoNotificationRecipient::getKaleoDefinitionId);
		attributeSetterBiConsumers.put(
			"kaleoDefinitionId",
			(BiConsumer<KaleoNotificationRecipient, Long>)
				KaleoNotificationRecipient::setKaleoDefinitionId);
		attributeGetterFunctions.put(
			"kaleoDefinitionVersionId",
			KaleoNotificationRecipient::getKaleoDefinitionVersionId);
		attributeSetterBiConsumers.put(
			"kaleoDefinitionVersionId",
			(BiConsumer<KaleoNotificationRecipient, Long>)
				KaleoNotificationRecipient::setKaleoDefinitionVersionId);
		attributeGetterFunctions.put(
			"kaleoNotificationId",
			KaleoNotificationRecipient::getKaleoNotificationId);
		attributeSetterBiConsumers.put(
			"kaleoNotificationId",
			(BiConsumer<KaleoNotificationRecipient, Long>)
				KaleoNotificationRecipient::setKaleoNotificationId);
		attributeGetterFunctions.put(
			"recipientClassName",
			KaleoNotificationRecipient::getRecipientClassName);
		attributeSetterBiConsumers.put(
			"recipientClassName",
			(BiConsumer<KaleoNotificationRecipient, String>)
				KaleoNotificationRecipient::setRecipientClassName);
		attributeGetterFunctions.put(
			"recipientClassPK",
			KaleoNotificationRecipient::getRecipientClassPK);
		attributeSetterBiConsumers.put(
			"recipientClassPK",
			(BiConsumer<KaleoNotificationRecipient, Long>)
				KaleoNotificationRecipient::setRecipientClassPK);
		attributeGetterFunctions.put(
			"recipientRoleType",
			KaleoNotificationRecipient::getRecipientRoleType);
		attributeSetterBiConsumers.put(
			"recipientRoleType",
			(BiConsumer<KaleoNotificationRecipient, Integer>)
				KaleoNotificationRecipient::setRecipientRoleType);
		attributeGetterFunctions.put(
			"recipientScript", KaleoNotificationRecipient::getRecipientScript);
		attributeSetterBiConsumers.put(
			"recipientScript",
			(BiConsumer<KaleoNotificationRecipient, String>)
				KaleoNotificationRecipient::setRecipientScript);
		attributeGetterFunctions.put(
			"recipientScriptLanguage",
			KaleoNotificationRecipient::getRecipientScriptLanguage);
		attributeSetterBiConsumers.put(
			"recipientScriptLanguage",
			(BiConsumer<KaleoNotificationRecipient, String>)
				KaleoNotificationRecipient::setRecipientScriptLanguage);
		attributeGetterFunctions.put(
			"recipientScriptContexts",
			KaleoNotificationRecipient::getRecipientScriptContexts);
		attributeSetterBiConsumers.put(
			"recipientScriptContexts",
			(BiConsumer<KaleoNotificationRecipient, String>)
				KaleoNotificationRecipient::setRecipientScriptContexts);
		attributeGetterFunctions.put(
			"address", KaleoNotificationRecipient::getAddress);
		attributeSetterBiConsumers.put(
			"address",
			(BiConsumer<KaleoNotificationRecipient, String>)
				KaleoNotificationRecipient::setAddress);
		attributeGetterFunctions.put(
			"notificationReceptionType",
			KaleoNotificationRecipient::getNotificationReceptionType);
		attributeSetterBiConsumers.put(
			"notificationReceptionType",
			(BiConsumer<KaleoNotificationRecipient, String>)
				KaleoNotificationRecipient::setNotificationReceptionType);

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
	public long getKaleoNotificationRecipientId() {
		return _kaleoNotificationRecipientId;
	}

	@Override
	public void setKaleoNotificationRecipientId(
		long kaleoNotificationRecipientId) {

		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_kaleoNotificationRecipientId = kaleoNotificationRecipientId;
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

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public long getOriginalCompanyId() {
		return GetterUtil.getLong(getColumnOriginalValue("companyId"));
	}

	@Override
	public long getUserId() {
		return _userId;
	}

	@Override
	public void setUserId(long userId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_userId = userId;
	}

	@Override
	public String getUserUuid() {
		try {
			User user = UserLocalServiceUtil.getUserById(getUserId());

			return user.getUuid();
		}
		catch (PortalException portalException) {
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
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_userName = userName;
	}

	@Override
	public Date getCreateDate() {
		return _createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_createDate = createDate;
	}

	@Override
	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public boolean hasSetModifiedDate() {
		return _setModifiedDate;
	}

	@Override
	public void setModifiedDate(Date modifiedDate) {
		_setModifiedDate = true;

		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_modifiedDate = modifiedDate;
	}

	@Override
	public long getKaleoDefinitionId() {
		return _kaleoDefinitionId;
	}

	@Override
	public void setKaleoDefinitionId(long kaleoDefinitionId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_kaleoDefinitionId = kaleoDefinitionId;
	}

	@Override
	public long getKaleoDefinitionVersionId() {
		return _kaleoDefinitionVersionId;
	}

	@Override
	public void setKaleoDefinitionVersionId(long kaleoDefinitionVersionId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_kaleoDefinitionVersionId = kaleoDefinitionVersionId;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public long getOriginalKaleoDefinitionVersionId() {
		return GetterUtil.getLong(
			getColumnOriginalValue("kaleoDefinitionVersionId"));
	}

	@Override
	public long getKaleoNotificationId() {
		return _kaleoNotificationId;
	}

	@Override
	public void setKaleoNotificationId(long kaleoNotificationId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_kaleoNotificationId = kaleoNotificationId;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public long getOriginalKaleoNotificationId() {
		return GetterUtil.getLong(
			getColumnOriginalValue("kaleoNotificationId"));
	}

	@Override
	public String getRecipientClassName() {
		if (_recipientClassName == null) {
			return "";
		}
		else {
			return _recipientClassName;
		}
	}

	@Override
	public void setRecipientClassName(String recipientClassName) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_recipientClassName = recipientClassName;
	}

	@Override
	public long getRecipientClassPK() {
		return _recipientClassPK;
	}

	@Override
	public void setRecipientClassPK(long recipientClassPK) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_recipientClassPK = recipientClassPK;
	}

	@Override
	public int getRecipientRoleType() {
		return _recipientRoleType;
	}

	@Override
	public void setRecipientRoleType(int recipientRoleType) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_recipientRoleType = recipientRoleType;
	}

	@Override
	public String getRecipientScript() {
		if (_recipientScript == null) {
			return "";
		}
		else {
			return _recipientScript;
		}
	}

	@Override
	public void setRecipientScript(String recipientScript) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_recipientScript = recipientScript;
	}

	@Override
	public String getRecipientScriptLanguage() {
		if (_recipientScriptLanguage == null) {
			return "";
		}
		else {
			return _recipientScriptLanguage;
		}
	}

	@Override
	public void setRecipientScriptLanguage(String recipientScriptLanguage) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_recipientScriptLanguage = recipientScriptLanguage;
	}

	@Override
	public String getRecipientScriptContexts() {
		if (_recipientScriptContexts == null) {
			return "";
		}
		else {
			return _recipientScriptContexts;
		}
	}

	@Override
	public void setRecipientScriptContexts(String recipientScriptContexts) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_recipientScriptContexts = recipientScriptContexts;
	}

	@Override
	public String getAddress() {
		if (_address == null) {
			return "";
		}
		else {
			return _address;
		}
	}

	@Override
	public void setAddress(String address) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_address = address;
	}

	@Override
	public String getNotificationReceptionType() {
		if (_notificationReceptionType == null) {
			return "";
		}
		else {
			return _notificationReceptionType;
		}
	}

	@Override
	public void setNotificationReceptionType(String notificationReceptionType) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_notificationReceptionType = notificationReceptionType;
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
			getCompanyId(), KaleoNotificationRecipient.class.getName(),
			getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public KaleoNotificationRecipient toEscapedModel() {
		if (_escapedModel == null) {
			Function<InvocationHandler, KaleoNotificationRecipient>
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
		KaleoNotificationRecipientImpl kaleoNotificationRecipientImpl =
			new KaleoNotificationRecipientImpl();

		kaleoNotificationRecipientImpl.setMvccVersion(getMvccVersion());
		kaleoNotificationRecipientImpl.setKaleoNotificationRecipientId(
			getKaleoNotificationRecipientId());
		kaleoNotificationRecipientImpl.setGroupId(getGroupId());
		kaleoNotificationRecipientImpl.setCompanyId(getCompanyId());
		kaleoNotificationRecipientImpl.setUserId(getUserId());
		kaleoNotificationRecipientImpl.setUserName(getUserName());
		kaleoNotificationRecipientImpl.setCreateDate(getCreateDate());
		kaleoNotificationRecipientImpl.setModifiedDate(getModifiedDate());
		kaleoNotificationRecipientImpl.setKaleoDefinitionId(
			getKaleoDefinitionId());
		kaleoNotificationRecipientImpl.setKaleoDefinitionVersionId(
			getKaleoDefinitionVersionId());
		kaleoNotificationRecipientImpl.setKaleoNotificationId(
			getKaleoNotificationId());
		kaleoNotificationRecipientImpl.setRecipientClassName(
			getRecipientClassName());
		kaleoNotificationRecipientImpl.setRecipientClassPK(
			getRecipientClassPK());
		kaleoNotificationRecipientImpl.setRecipientRoleType(
			getRecipientRoleType());
		kaleoNotificationRecipientImpl.setRecipientScript(getRecipientScript());
		kaleoNotificationRecipientImpl.setRecipientScriptLanguage(
			getRecipientScriptLanguage());
		kaleoNotificationRecipientImpl.setRecipientScriptContexts(
			getRecipientScriptContexts());
		kaleoNotificationRecipientImpl.setAddress(getAddress());
		kaleoNotificationRecipientImpl.setNotificationReceptionType(
			getNotificationReceptionType());

		kaleoNotificationRecipientImpl.resetOriginalValues();

		return kaleoNotificationRecipientImpl;
	}

	@Override
	public int compareTo(
		KaleoNotificationRecipient kaleoNotificationRecipient) {

		int value = 0;

		if (getKaleoNotificationRecipientId() <
				kaleoNotificationRecipient.getKaleoNotificationRecipientId()) {

			value = -1;
		}
		else if (getKaleoNotificationRecipientId() >
					kaleoNotificationRecipient.
						getKaleoNotificationRecipientId()) {

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
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof KaleoNotificationRecipient)) {
			return false;
		}

		KaleoNotificationRecipient kaleoNotificationRecipient =
			(KaleoNotificationRecipient)object;

		long primaryKey = kaleoNotificationRecipient.getPrimaryKey();

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

		_setModifiedDate = false;

		_columnBitmask = 0;
	}

	@Override
	public CacheModel<KaleoNotificationRecipient> toCacheModel() {
		KaleoNotificationRecipientCacheModel
			kaleoNotificationRecipientCacheModel =
				new KaleoNotificationRecipientCacheModel();

		kaleoNotificationRecipientCacheModel.mvccVersion = getMvccVersion();

		kaleoNotificationRecipientCacheModel.kaleoNotificationRecipientId =
			getKaleoNotificationRecipientId();

		kaleoNotificationRecipientCacheModel.groupId = getGroupId();

		kaleoNotificationRecipientCacheModel.companyId = getCompanyId();

		kaleoNotificationRecipientCacheModel.userId = getUserId();

		kaleoNotificationRecipientCacheModel.userName = getUserName();

		String userName = kaleoNotificationRecipientCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			kaleoNotificationRecipientCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			kaleoNotificationRecipientCacheModel.createDate =
				createDate.getTime();
		}
		else {
			kaleoNotificationRecipientCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			kaleoNotificationRecipientCacheModel.modifiedDate =
				modifiedDate.getTime();
		}
		else {
			kaleoNotificationRecipientCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		kaleoNotificationRecipientCacheModel.kaleoDefinitionId =
			getKaleoDefinitionId();

		kaleoNotificationRecipientCacheModel.kaleoDefinitionVersionId =
			getKaleoDefinitionVersionId();

		kaleoNotificationRecipientCacheModel.kaleoNotificationId =
			getKaleoNotificationId();

		kaleoNotificationRecipientCacheModel.recipientClassName =
			getRecipientClassName();

		String recipientClassName =
			kaleoNotificationRecipientCacheModel.recipientClassName;

		if ((recipientClassName != null) &&
			(recipientClassName.length() == 0)) {

			kaleoNotificationRecipientCacheModel.recipientClassName = null;
		}

		kaleoNotificationRecipientCacheModel.recipientClassPK =
			getRecipientClassPK();

		kaleoNotificationRecipientCacheModel.recipientRoleType =
			getRecipientRoleType();

		kaleoNotificationRecipientCacheModel.recipientScript =
			getRecipientScript();

		String recipientScript =
			kaleoNotificationRecipientCacheModel.recipientScript;

		if ((recipientScript != null) && (recipientScript.length() == 0)) {
			kaleoNotificationRecipientCacheModel.recipientScript = null;
		}

		kaleoNotificationRecipientCacheModel.recipientScriptLanguage =
			getRecipientScriptLanguage();

		String recipientScriptLanguage =
			kaleoNotificationRecipientCacheModel.recipientScriptLanguage;

		if ((recipientScriptLanguage != null) &&
			(recipientScriptLanguage.length() == 0)) {

			kaleoNotificationRecipientCacheModel.recipientScriptLanguage = null;
		}

		kaleoNotificationRecipientCacheModel.recipientScriptContexts =
			getRecipientScriptContexts();

		String recipientScriptContexts =
			kaleoNotificationRecipientCacheModel.recipientScriptContexts;

		if ((recipientScriptContexts != null) &&
			(recipientScriptContexts.length() == 0)) {

			kaleoNotificationRecipientCacheModel.recipientScriptContexts = null;
		}

		kaleoNotificationRecipientCacheModel.address = getAddress();

		String address = kaleoNotificationRecipientCacheModel.address;

		if ((address != null) && (address.length() == 0)) {
			kaleoNotificationRecipientCacheModel.address = null;
		}

		kaleoNotificationRecipientCacheModel.notificationReceptionType =
			getNotificationReceptionType();

		String notificationReceptionType =
			kaleoNotificationRecipientCacheModel.notificationReceptionType;

		if ((notificationReceptionType != null) &&
			(notificationReceptionType.length() == 0)) {

			kaleoNotificationRecipientCacheModel.notificationReceptionType =
				null;
		}

		return kaleoNotificationRecipientCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<KaleoNotificationRecipient, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			4 * attributeGetterFunctions.size() + 2);

		sb.append("{");

		for (Map.Entry<String, Function<KaleoNotificationRecipient, Object>>
				entry : attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<KaleoNotificationRecipient, Object>
				attributeGetterFunction = entry.getValue();

			sb.append(attributeName);
			sb.append("=");
			sb.append(
				attributeGetterFunction.apply(
					(KaleoNotificationRecipient)this));
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
		Map<String, Function<KaleoNotificationRecipient, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			5 * attributeGetterFunctions.size() + 4);

		sb.append("<model><model-name>");
		sb.append(getModelClassName());
		sb.append("</model-name>");

		for (Map.Entry<String, Function<KaleoNotificationRecipient, Object>>
				entry : attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<KaleoNotificationRecipient, Object>
				attributeGetterFunction = entry.getValue();

			sb.append("<column><column-name>");
			sb.append(attributeName);
			sb.append("</column-name><column-value><![CDATA[");
			sb.append(
				attributeGetterFunction.apply(
					(KaleoNotificationRecipient)this));
			sb.append("]]></column-value></column>");
		}

		sb.append("</model>");

		return sb.toString();
	}

	private static class EscapedModelProxyProviderFunctionHolder {

		private static final Function
			<InvocationHandler, KaleoNotificationRecipient>
				_escapedModelProxyProviderFunction =
					_getProxyProviderFunction();

	}

	private long _mvccVersion;
	private long _kaleoNotificationRecipientId;
	private long _groupId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private boolean _setModifiedDate;
	private long _kaleoDefinitionId;
	private long _kaleoDefinitionVersionId;
	private long _kaleoNotificationId;
	private String _recipientClassName;
	private long _recipientClassPK;
	private int _recipientRoleType;
	private String _recipientScript;
	private String _recipientScriptLanguage;
	private String _recipientScriptContexts;
	private String _address;
	private String _notificationReceptionType;

	public <T> T getColumnValue(String columnName) {
		Function<KaleoNotificationRecipient, Object> function =
			_attributeGetterFunctions.get(columnName);

		if (function == null) {
			throw new IllegalArgumentException(
				"No attribute getter function found for " + columnName);
		}

		return (T)function.apply((KaleoNotificationRecipient)this);
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
			"kaleoNotificationRecipientId", _kaleoNotificationRecipientId);
		_columnOriginalValues.put("groupId", _groupId);
		_columnOriginalValues.put("companyId", _companyId);
		_columnOriginalValues.put("userId", _userId);
		_columnOriginalValues.put("userName", _userName);
		_columnOriginalValues.put("createDate", _createDate);
		_columnOriginalValues.put("modifiedDate", _modifiedDate);
		_columnOriginalValues.put("kaleoDefinitionId", _kaleoDefinitionId);
		_columnOriginalValues.put(
			"kaleoDefinitionVersionId", _kaleoDefinitionVersionId);
		_columnOriginalValues.put("kaleoNotificationId", _kaleoNotificationId);
		_columnOriginalValues.put("recipientClassName", _recipientClassName);
		_columnOriginalValues.put("recipientClassPK", _recipientClassPK);
		_columnOriginalValues.put("recipientRoleType", _recipientRoleType);
		_columnOriginalValues.put("recipientScript", _recipientScript);
		_columnOriginalValues.put(
			"recipientScriptLanguage", _recipientScriptLanguage);
		_columnOriginalValues.put(
			"recipientScriptContexts", _recipientScriptContexts);
		_columnOriginalValues.put("address", _address);
		_columnOriginalValues.put(
			"notificationReceptionType", _notificationReceptionType);
	}

	private transient Map<String, Object> _columnOriginalValues;

	public static long getColumnBitmask(String columnName) {
		return _columnBitmasks.get(columnName);
	}

	private static final Map<String, Long> _columnBitmasks;

	static {
		Map<String, Long> columnBitmasks = new HashMap<>();

		columnBitmasks.put("mvccVersion", 1L);

		columnBitmasks.put("kaleoNotificationRecipientId", 2L);

		columnBitmasks.put("groupId", 4L);

		columnBitmasks.put("companyId", 8L);

		columnBitmasks.put("userId", 16L);

		columnBitmasks.put("userName", 32L);

		columnBitmasks.put("createDate", 64L);

		columnBitmasks.put("modifiedDate", 128L);

		columnBitmasks.put("kaleoDefinitionId", 256L);

		columnBitmasks.put("kaleoDefinitionVersionId", 512L);

		columnBitmasks.put("kaleoNotificationId", 1024L);

		columnBitmasks.put("recipientClassName", 2048L);

		columnBitmasks.put("recipientClassPK", 4096L);

		columnBitmasks.put("recipientRoleType", 8192L);

		columnBitmasks.put("recipientScript", 16384L);

		columnBitmasks.put("recipientScriptLanguage", 32768L);

		columnBitmasks.put("recipientScriptContexts", 65536L);

		columnBitmasks.put("address", 131072L);

		columnBitmasks.put("notificationReceptionType", 262144L);

		_columnBitmasks = Collections.unmodifiableMap(columnBitmasks);
	}

	private long _columnBitmask;
	private KaleoNotificationRecipient _escapedModel;

}