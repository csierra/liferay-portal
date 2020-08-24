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
import com.liferay.portal.workflow.kaleo.model.KaleoInstance;
import com.liferay.portal.workflow.kaleo.model.KaleoInstanceModel;

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
 * The base model implementation for the KaleoInstance service. Represents a row in the &quot;KaleoInstance&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface <code>KaleoInstanceModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link KaleoInstanceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see KaleoInstanceImpl
 * @generated
 */
public class KaleoInstanceModelImpl
	extends BaseModelImpl<KaleoInstance> implements KaleoInstanceModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a kaleo instance model instance should use the <code>KaleoInstance</code> interface instead.
	 */
	public static final String TABLE_NAME = "KaleoInstance";

	public static final Object[][] TABLE_COLUMNS = {
		{"mvccVersion", Types.BIGINT}, {"kaleoInstanceId", Types.BIGINT},
		{"groupId", Types.BIGINT}, {"companyId", Types.BIGINT},
		{"userId", Types.BIGINT}, {"userName", Types.VARCHAR},
		{"createDate", Types.TIMESTAMP}, {"modifiedDate", Types.TIMESTAMP},
		{"kaleoDefinitionId", Types.BIGINT},
		{"kaleoDefinitionVersionId", Types.BIGINT},
		{"kaleoDefinitionName", Types.VARCHAR},
		{"kaleoDefinitionVersion", Types.INTEGER},
		{"rootKaleoInstanceTokenId", Types.BIGINT},
		{"className", Types.VARCHAR}, {"classPK", Types.BIGINT},
		{"completed", Types.BOOLEAN}, {"completionDate", Types.TIMESTAMP},
		{"workflowContext", Types.CLOB}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("mvccVersion", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("kaleoInstanceId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("groupId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("modifiedDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("kaleoDefinitionId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("kaleoDefinitionVersionId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("kaleoDefinitionName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("kaleoDefinitionVersion", Types.INTEGER);
		TABLE_COLUMNS_MAP.put("rootKaleoInstanceTokenId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("className", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("classPK", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("completed", Types.BOOLEAN);
		TABLE_COLUMNS_MAP.put("completionDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("workflowContext", Types.CLOB);
	}

	public static final String TABLE_SQL_CREATE =
		"create table KaleoInstance (mvccVersion LONG default 0 not null,kaleoInstanceId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,userName VARCHAR(200) null,createDate DATE null,modifiedDate DATE null,kaleoDefinitionId LONG,kaleoDefinitionVersionId LONG,kaleoDefinitionName VARCHAR(200) null,kaleoDefinitionVersion INTEGER,rootKaleoInstanceTokenId LONG,className VARCHAR(200) null,classPK LONG,completed BOOLEAN,completionDate DATE null,workflowContext TEXT null)";

	public static final String TABLE_SQL_DROP = "drop table KaleoInstance";

	public static final String ORDER_BY_JPQL =
		" ORDER BY kaleoInstance.kaleoInstanceId ASC";

	public static final String ORDER_BY_SQL =
		" ORDER BY KaleoInstance.kaleoInstanceId ASC";

	public static final String DATA_SOURCE = "liferayDataSource";

	public static final String SESSION_FACTORY = "liferaySessionFactory";

	public static final String TX_MANAGER = "liferayTransactionManager";

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)
	 */
	@Deprecated
	public static final long CLASSNAME_COLUMN_BITMASK = 1L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)
	 */
	@Deprecated
	public static final long CLASSPK_COLUMN_BITMASK = 2L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)
	 */
	@Deprecated
	public static final long COMPANYID_COLUMN_BITMASK = 4L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)
	 */
	@Deprecated
	public static final long COMPLETED_COLUMN_BITMASK = 8L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)
	 */
	@Deprecated
	public static final long COMPLETIONDATE_COLUMN_BITMASK = 16L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)
	 */
	@Deprecated
	public static final long KALEODEFINITIONID_COLUMN_BITMASK = 32L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)
	 */
	@Deprecated
	public static final long KALEODEFINITIONNAME_COLUMN_BITMASK = 64L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)
	 */
	@Deprecated
	public static final long KALEODEFINITIONVERSION_COLUMN_BITMASK = 128L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)
	 */
	@Deprecated
	public static final long KALEODEFINITIONVERSIONID_COLUMN_BITMASK = 256L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)
	 */
	@Deprecated
	public static final long KALEOINSTANCEID_COLUMN_BITMASK = 512L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)
	 */
	@Deprecated
	public static final long USERID_COLUMN_BITMASK = 1024L;

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

	public KaleoInstanceModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _kaleoInstanceId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setKaleoInstanceId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _kaleoInstanceId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return KaleoInstance.class;
	}

	@Override
	public String getModelClassName() {
		return KaleoInstance.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<KaleoInstance, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		for (Map.Entry<String, Function<KaleoInstance, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<KaleoInstance, Object> attributeGetterFunction =
				entry.getValue();

			attributes.put(
				attributeName,
				attributeGetterFunction.apply((KaleoInstance)this));
		}

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<KaleoInstance, Object>>
			attributeSetterBiConsumers = getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<KaleoInstance, Object> attributeSetterBiConsumer =
				attributeSetterBiConsumers.get(attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(KaleoInstance)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<KaleoInstance, Object>>
		getAttributeGetterFunctions() {

		return _attributeGetterFunctions;
	}

	public Map<String, BiConsumer<KaleoInstance, Object>>
		getAttributeSetterBiConsumers() {

		return _attributeSetterBiConsumers;
	}

	private static Function<InvocationHandler, KaleoInstance>
		_getProxyProviderFunction() {

		Class<?> proxyClass = ProxyUtil.getProxyClass(
			KaleoInstance.class.getClassLoader(), KaleoInstance.class,
			ModelWrapper.class);

		try {
			Constructor<KaleoInstance> constructor =
				(Constructor<KaleoInstance>)proxyClass.getConstructor(
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

	private static final Map<String, Function<KaleoInstance, Object>>
		_attributeGetterFunctions;
	private static final Map<String, BiConsumer<KaleoInstance, Object>>
		_attributeSetterBiConsumers;

	static {
		Map<String, Function<KaleoInstance, Object>> attributeGetterFunctions =
			new LinkedHashMap<String, Function<KaleoInstance, Object>>();
		Map<String, BiConsumer<KaleoInstance, ?>> attributeSetterBiConsumers =
			new LinkedHashMap<String, BiConsumer<KaleoInstance, ?>>();

		attributeGetterFunctions.put(
			"mvccVersion", KaleoInstance::getMvccVersion);
		attributeSetterBiConsumers.put(
			"mvccVersion",
			(BiConsumer<KaleoInstance, Long>)KaleoInstance::setMvccVersion);
		attributeGetterFunctions.put(
			"kaleoInstanceId", KaleoInstance::getKaleoInstanceId);
		attributeSetterBiConsumers.put(
			"kaleoInstanceId",
			(BiConsumer<KaleoInstance, Long>)KaleoInstance::setKaleoInstanceId);
		attributeGetterFunctions.put("groupId", KaleoInstance::getGroupId);
		attributeSetterBiConsumers.put(
			"groupId",
			(BiConsumer<KaleoInstance, Long>)KaleoInstance::setGroupId);
		attributeGetterFunctions.put("companyId", KaleoInstance::getCompanyId);
		attributeSetterBiConsumers.put(
			"companyId",
			(BiConsumer<KaleoInstance, Long>)KaleoInstance::setCompanyId);
		attributeGetterFunctions.put("userId", KaleoInstance::getUserId);
		attributeSetterBiConsumers.put(
			"userId",
			(BiConsumer<KaleoInstance, Long>)KaleoInstance::setUserId);
		attributeGetterFunctions.put("userName", KaleoInstance::getUserName);
		attributeSetterBiConsumers.put(
			"userName",
			(BiConsumer<KaleoInstance, String>)KaleoInstance::setUserName);
		attributeGetterFunctions.put(
			"createDate", KaleoInstance::getCreateDate);
		attributeSetterBiConsumers.put(
			"createDate",
			(BiConsumer<KaleoInstance, Date>)KaleoInstance::setCreateDate);
		attributeGetterFunctions.put(
			"modifiedDate", KaleoInstance::getModifiedDate);
		attributeSetterBiConsumers.put(
			"modifiedDate",
			(BiConsumer<KaleoInstance, Date>)KaleoInstance::setModifiedDate);
		attributeGetterFunctions.put(
			"kaleoDefinitionId", KaleoInstance::getKaleoDefinitionId);
		attributeSetterBiConsumers.put(
			"kaleoDefinitionId",
			(BiConsumer<KaleoInstance, Long>)
				KaleoInstance::setKaleoDefinitionId);
		attributeGetterFunctions.put(
			"kaleoDefinitionVersionId",
			KaleoInstance::getKaleoDefinitionVersionId);
		attributeSetterBiConsumers.put(
			"kaleoDefinitionVersionId",
			(BiConsumer<KaleoInstance, Long>)
				KaleoInstance::setKaleoDefinitionVersionId);
		attributeGetterFunctions.put(
			"kaleoDefinitionName", KaleoInstance::getKaleoDefinitionName);
		attributeSetterBiConsumers.put(
			"kaleoDefinitionName",
			(BiConsumer<KaleoInstance, String>)
				KaleoInstance::setKaleoDefinitionName);
		attributeGetterFunctions.put(
			"kaleoDefinitionVersion", KaleoInstance::getKaleoDefinitionVersion);
		attributeSetterBiConsumers.put(
			"kaleoDefinitionVersion",
			(BiConsumer<KaleoInstance, Integer>)
				KaleoInstance::setKaleoDefinitionVersion);
		attributeGetterFunctions.put(
			"rootKaleoInstanceTokenId",
			KaleoInstance::getRootKaleoInstanceTokenId);
		attributeSetterBiConsumers.put(
			"rootKaleoInstanceTokenId",
			(BiConsumer<KaleoInstance, Long>)
				KaleoInstance::setRootKaleoInstanceTokenId);
		attributeGetterFunctions.put("className", KaleoInstance::getClassName);
		attributeSetterBiConsumers.put(
			"className",
			(BiConsumer<KaleoInstance, String>)KaleoInstance::setClassName);
		attributeGetterFunctions.put("classPK", KaleoInstance::getClassPK);
		attributeSetterBiConsumers.put(
			"classPK",
			(BiConsumer<KaleoInstance, Long>)KaleoInstance::setClassPK);
		attributeGetterFunctions.put("completed", KaleoInstance::getCompleted);
		attributeSetterBiConsumers.put(
			"completed",
			(BiConsumer<KaleoInstance, Boolean>)KaleoInstance::setCompleted);
		attributeGetterFunctions.put(
			"completionDate", KaleoInstance::getCompletionDate);
		attributeSetterBiConsumers.put(
			"completionDate",
			(BiConsumer<KaleoInstance, Date>)KaleoInstance::setCompletionDate);
		attributeGetterFunctions.put(
			"workflowContext", KaleoInstance::getWorkflowContext);
		attributeSetterBiConsumers.put(
			"workflowContext",
			(BiConsumer<KaleoInstance, String>)
				KaleoInstance::setWorkflowContext);

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
	public long getKaleoInstanceId() {
		return _kaleoInstanceId;
	}

	@Override
	public void setKaleoInstanceId(long kaleoInstanceId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_kaleoInstanceId = kaleoInstanceId;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public long getOriginalKaleoInstanceId() {
		return GetterUtil.getLong(getColumnOriginalValue("kaleoInstanceId"));
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

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public long getOriginalUserId() {
		return GetterUtil.getLong(getColumnOriginalValue("userId"));
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

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public long getOriginalKaleoDefinitionId() {
		return GetterUtil.getLong(getColumnOriginalValue("kaleoDefinitionId"));
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
	public String getKaleoDefinitionName() {
		if (_kaleoDefinitionName == null) {
			return "";
		}
		else {
			return _kaleoDefinitionName;
		}
	}

	@Override
	public void setKaleoDefinitionName(String kaleoDefinitionName) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_kaleoDefinitionName = kaleoDefinitionName;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public String getOriginalKaleoDefinitionName() {
		return getColumnOriginalValue("kaleoDefinitionName");
	}

	@Override
	public int getKaleoDefinitionVersion() {
		return _kaleoDefinitionVersion;
	}

	@Override
	public void setKaleoDefinitionVersion(int kaleoDefinitionVersion) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_kaleoDefinitionVersion = kaleoDefinitionVersion;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public int getOriginalKaleoDefinitionVersion() {
		return GetterUtil.getInteger(
			getColumnOriginalValue("kaleoDefinitionVersion"));
	}

	@Override
	public long getRootKaleoInstanceTokenId() {
		return _rootKaleoInstanceTokenId;
	}

	@Override
	public void setRootKaleoInstanceTokenId(long rootKaleoInstanceTokenId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_rootKaleoInstanceTokenId = rootKaleoInstanceTokenId;
	}

	@Override
	public String getClassName() {
		if (_className == null) {
			return "";
		}
		else {
			return _className;
		}
	}

	@Override
	public void setClassName(String className) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_className = className;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public String getOriginalClassName() {
		return getColumnOriginalValue("className");
	}

	@Override
	public long getClassPK() {
		return _classPK;
	}

	@Override
	public void setClassPK(long classPK) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_classPK = classPK;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public long getOriginalClassPK() {
		return GetterUtil.getLong(getColumnOriginalValue("classPK"));
	}

	@Override
	public boolean getCompleted() {
		return _completed;
	}

	@Override
	public boolean isCompleted() {
		return _completed;
	}

	@Override
	public void setCompleted(boolean completed) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_completed = completed;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public boolean getOriginalCompleted() {
		return GetterUtil.getBoolean(getColumnOriginalValue("completed"));
	}

	@Override
	public Date getCompletionDate() {
		return _completionDate;
	}

	@Override
	public void setCompletionDate(Date completionDate) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_completionDate = completionDate;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public Date getOriginalCompletionDate() {
		return getColumnOriginalValue("completionDate");
	}

	@Override
	public String getWorkflowContext() {
		if (_workflowContext == null) {
			return "";
		}
		else {
			return _workflowContext;
		}
	}

	@Override
	public void setWorkflowContext(String workflowContext) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_workflowContext = workflowContext;
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
			getCompanyId(), KaleoInstance.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public KaleoInstance toEscapedModel() {
		if (_escapedModel == null) {
			Function<InvocationHandler, KaleoInstance>
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
		KaleoInstanceImpl kaleoInstanceImpl = new KaleoInstanceImpl();

		kaleoInstanceImpl.setMvccVersion(getMvccVersion());
		kaleoInstanceImpl.setKaleoInstanceId(getKaleoInstanceId());
		kaleoInstanceImpl.setGroupId(getGroupId());
		kaleoInstanceImpl.setCompanyId(getCompanyId());
		kaleoInstanceImpl.setUserId(getUserId());
		kaleoInstanceImpl.setUserName(getUserName());
		kaleoInstanceImpl.setCreateDate(getCreateDate());
		kaleoInstanceImpl.setModifiedDate(getModifiedDate());
		kaleoInstanceImpl.setKaleoDefinitionId(getKaleoDefinitionId());
		kaleoInstanceImpl.setKaleoDefinitionVersionId(
			getKaleoDefinitionVersionId());
		kaleoInstanceImpl.setKaleoDefinitionName(getKaleoDefinitionName());
		kaleoInstanceImpl.setKaleoDefinitionVersion(
			getKaleoDefinitionVersion());
		kaleoInstanceImpl.setRootKaleoInstanceTokenId(
			getRootKaleoInstanceTokenId());
		kaleoInstanceImpl.setClassName(getClassName());
		kaleoInstanceImpl.setClassPK(getClassPK());
		kaleoInstanceImpl.setCompleted(isCompleted());
		kaleoInstanceImpl.setCompletionDate(getCompletionDate());
		kaleoInstanceImpl.setWorkflowContext(getWorkflowContext());

		kaleoInstanceImpl.resetOriginalValues();

		return kaleoInstanceImpl;
	}

	@Override
	public int compareTo(KaleoInstance kaleoInstance) {
		int value = 0;

		if (getKaleoInstanceId() < kaleoInstance.getKaleoInstanceId()) {
			value = -1;
		}
		else if (getKaleoInstanceId() > kaleoInstance.getKaleoInstanceId()) {
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

		if (!(object instanceof KaleoInstance)) {
			return false;
		}

		KaleoInstance kaleoInstance = (KaleoInstance)object;

		long primaryKey = kaleoInstance.getPrimaryKey();

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
	public CacheModel<KaleoInstance> toCacheModel() {
		KaleoInstanceCacheModel kaleoInstanceCacheModel =
			new KaleoInstanceCacheModel();

		kaleoInstanceCacheModel.mvccVersion = getMvccVersion();

		kaleoInstanceCacheModel.kaleoInstanceId = getKaleoInstanceId();

		kaleoInstanceCacheModel.groupId = getGroupId();

		kaleoInstanceCacheModel.companyId = getCompanyId();

		kaleoInstanceCacheModel.userId = getUserId();

		kaleoInstanceCacheModel.userName = getUserName();

		String userName = kaleoInstanceCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			kaleoInstanceCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			kaleoInstanceCacheModel.createDate = createDate.getTime();
		}
		else {
			kaleoInstanceCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			kaleoInstanceCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			kaleoInstanceCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		kaleoInstanceCacheModel.kaleoDefinitionId = getKaleoDefinitionId();

		kaleoInstanceCacheModel.kaleoDefinitionVersionId =
			getKaleoDefinitionVersionId();

		kaleoInstanceCacheModel.kaleoDefinitionName = getKaleoDefinitionName();

		String kaleoDefinitionName =
			kaleoInstanceCacheModel.kaleoDefinitionName;

		if ((kaleoDefinitionName != null) &&
			(kaleoDefinitionName.length() == 0)) {

			kaleoInstanceCacheModel.kaleoDefinitionName = null;
		}

		kaleoInstanceCacheModel.kaleoDefinitionVersion =
			getKaleoDefinitionVersion();

		kaleoInstanceCacheModel.rootKaleoInstanceTokenId =
			getRootKaleoInstanceTokenId();

		kaleoInstanceCacheModel.className = getClassName();

		String className = kaleoInstanceCacheModel.className;

		if ((className != null) && (className.length() == 0)) {
			kaleoInstanceCacheModel.className = null;
		}

		kaleoInstanceCacheModel.classPK = getClassPK();

		kaleoInstanceCacheModel.completed = isCompleted();

		Date completionDate = getCompletionDate();

		if (completionDate != null) {
			kaleoInstanceCacheModel.completionDate = completionDate.getTime();
		}
		else {
			kaleoInstanceCacheModel.completionDate = Long.MIN_VALUE;
		}

		kaleoInstanceCacheModel.workflowContext = getWorkflowContext();

		String workflowContext = kaleoInstanceCacheModel.workflowContext;

		if ((workflowContext != null) && (workflowContext.length() == 0)) {
			kaleoInstanceCacheModel.workflowContext = null;
		}

		return kaleoInstanceCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<KaleoInstance, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			4 * attributeGetterFunctions.size() + 2);

		sb.append("{");

		for (Map.Entry<String, Function<KaleoInstance, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<KaleoInstance, Object> attributeGetterFunction =
				entry.getValue();

			sb.append(attributeName);
			sb.append("=");
			sb.append(attributeGetterFunction.apply((KaleoInstance)this));
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
		Map<String, Function<KaleoInstance, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			5 * attributeGetterFunctions.size() + 4);

		sb.append("<model><model-name>");
		sb.append(getModelClassName());
		sb.append("</model-name>");

		for (Map.Entry<String, Function<KaleoInstance, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<KaleoInstance, Object> attributeGetterFunction =
				entry.getValue();

			sb.append("<column><column-name>");
			sb.append(attributeName);
			sb.append("</column-name><column-value><![CDATA[");
			sb.append(attributeGetterFunction.apply((KaleoInstance)this));
			sb.append("]]></column-value></column>");
		}

		sb.append("</model>");

		return sb.toString();
	}

	private static class EscapedModelProxyProviderFunctionHolder {

		private static final Function<InvocationHandler, KaleoInstance>
			_escapedModelProxyProviderFunction = _getProxyProviderFunction();

	}

	private long _mvccVersion;
	private long _kaleoInstanceId;
	private long _groupId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private boolean _setModifiedDate;
	private long _kaleoDefinitionId;
	private long _kaleoDefinitionVersionId;
	private String _kaleoDefinitionName;
	private int _kaleoDefinitionVersion;
	private long _rootKaleoInstanceTokenId;
	private String _className;
	private long _classPK;
	private boolean _completed;
	private Date _completionDate;
	private String _workflowContext;

	public <T> T getColumnValue(String columnName) {
		Function<KaleoInstance, Object> function =
			_attributeGetterFunctions.get(columnName);

		if (function == null) {
			throw new IllegalArgumentException(
				"No attribute getter function found for " + columnName);
		}

		return (T)function.apply((KaleoInstance)this);
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
		_columnOriginalValues.put("kaleoInstanceId", _kaleoInstanceId);
		_columnOriginalValues.put("groupId", _groupId);
		_columnOriginalValues.put("companyId", _companyId);
		_columnOriginalValues.put("userId", _userId);
		_columnOriginalValues.put("userName", _userName);
		_columnOriginalValues.put("createDate", _createDate);
		_columnOriginalValues.put("modifiedDate", _modifiedDate);
		_columnOriginalValues.put("kaleoDefinitionId", _kaleoDefinitionId);
		_columnOriginalValues.put(
			"kaleoDefinitionVersionId", _kaleoDefinitionVersionId);
		_columnOriginalValues.put("kaleoDefinitionName", _kaleoDefinitionName);
		_columnOriginalValues.put(
			"kaleoDefinitionVersion", _kaleoDefinitionVersion);
		_columnOriginalValues.put(
			"rootKaleoInstanceTokenId", _rootKaleoInstanceTokenId);
		_columnOriginalValues.put("className", _className);
		_columnOriginalValues.put("classPK", _classPK);
		_columnOriginalValues.put("completed", _completed);
		_columnOriginalValues.put("completionDate", _completionDate);
		_columnOriginalValues.put("workflowContext", _workflowContext);
	}

	private transient Map<String, Object> _columnOriginalValues;

	public static long getColumnBitmask(String columnName) {
		return _columnBitmasks.get(columnName);
	}

	private static final Map<String, Long> _columnBitmasks;

	static {
		Map<String, Long> columnBitmasks = new HashMap<>();

		columnBitmasks.put("mvccVersion", 1L);

		columnBitmasks.put("kaleoInstanceId", 2L);

		columnBitmasks.put("groupId", 4L);

		columnBitmasks.put("companyId", 8L);

		columnBitmasks.put("userId", 16L);

		columnBitmasks.put("userName", 32L);

		columnBitmasks.put("createDate", 64L);

		columnBitmasks.put("modifiedDate", 128L);

		columnBitmasks.put("kaleoDefinitionId", 256L);

		columnBitmasks.put("kaleoDefinitionVersionId", 512L);

		columnBitmasks.put("kaleoDefinitionName", 1024L);

		columnBitmasks.put("kaleoDefinitionVersion", 2048L);

		columnBitmasks.put("rootKaleoInstanceTokenId", 4096L);

		columnBitmasks.put("className", 8192L);

		columnBitmasks.put("classPK", 16384L);

		columnBitmasks.put("completed", 32768L);

		columnBitmasks.put("completionDate", 65536L);

		columnBitmasks.put("workflowContext", 131072L);

		_columnBitmasks = Collections.unmodifiableMap(columnBitmasks);
	}

	private long _columnBitmask;
	private KaleoInstance _escapedModel;

}