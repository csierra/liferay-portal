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

package com.liferay.portal.security.password.model.impl;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;
import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.security.password.model.PasswordHashProvider;
import com.liferay.portal.security.password.model.PasswordHashProviderModel;
import com.liferay.portal.security.password.model.PasswordHashProviderSoap;

import java.io.Serializable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;

import java.sql.Types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * The base model implementation for the PasswordHashProvider service. Represents a row in the &quot;PasswordHashProvider&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface <code>PasswordHashProviderModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link PasswordHashProviderImpl}.
 * </p>
 *
 * @author Arthur Chan
 * @see PasswordHashProviderImpl
 * @generated
 */
@JSON(strict = true)
public class PasswordHashProviderModelImpl
	extends BaseModelImpl<PasswordHashProvider>
	implements PasswordHashProviderModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a password hash provider model instance should use the <code>PasswordHashProvider</code> interface instead.
	 */
	public static final String TABLE_NAME = "PasswordHashProvider";

	public static final Object[][] TABLE_COLUMNS = {
		{"mvccVersion", Types.BIGINT}, {"uuid_", Types.VARCHAR},
		{"passwordHashProviderId", Types.BIGINT}, {"companyId", Types.BIGINT},
		{"createDate", Types.TIMESTAMP}, {"modifiedDate", Types.TIMESTAMP},
		{"hashProviderName", Types.VARCHAR}, {"hashProviderMeta", Types.VARCHAR}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("mvccVersion", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("uuid_", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("passwordHashProviderId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("modifiedDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("hashProviderName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("hashProviderMeta", Types.VARCHAR);
	}

	public static final String TABLE_SQL_CREATE =
		"create table PasswordHashProvider (mvccVersion LONG default 0 not null,uuid_ VARCHAR(75) null,passwordHashProviderId LONG not null primary key,companyId LONG,createDate DATE null,modifiedDate DATE null,hashProviderName VARCHAR(75) null,hashProviderMeta VARCHAR(75) null)";

	public static final String TABLE_SQL_DROP =
		"drop table PasswordHashProvider";

	public static final String ORDER_BY_JPQL =
		" ORDER BY passwordHashProvider.passwordHashProviderId ASC";

	public static final String ORDER_BY_SQL =
		" ORDER BY PasswordHashProvider.passwordHashProviderId ASC";

	public static final String DATA_SOURCE = "liferayDataSource";

	public static final String SESSION_FACTORY = "liferaySessionFactory";

	public static final String TX_MANAGER = "liferayTransactionManager";

	public static final long COMPANYID_COLUMN_BITMASK = 1L;

	public static final long HASHPROVIDERMETA_COLUMN_BITMASK = 2L;

	public static final long HASHPROVIDERNAME_COLUMN_BITMASK = 4L;

	public static final long UUID_COLUMN_BITMASK = 8L;

	public static final long PASSWORDHASHPROVIDERID_COLUMN_BITMASK = 16L;

	public static void setEntityCacheEnabled(boolean entityCacheEnabled) {
		_entityCacheEnabled = entityCacheEnabled;
	}

	public static void setFinderCacheEnabled(boolean finderCacheEnabled) {
		_finderCacheEnabled = finderCacheEnabled;
	}

	/**
	 * Converts the soap model instance into a normal model instance.
	 *
	 * @param soapModel the soap model instance to convert
	 * @return the normal model instance
	 */
	public static PasswordHashProvider toModel(
		PasswordHashProviderSoap soapModel) {

		if (soapModel == null) {
			return null;
		}

		PasswordHashProvider model = new PasswordHashProviderImpl();

		model.setMvccVersion(soapModel.getMvccVersion());
		model.setUuid(soapModel.getUuid());
		model.setPasswordHashProviderId(soapModel.getPasswordHashProviderId());
		model.setCompanyId(soapModel.getCompanyId());
		model.setCreateDate(soapModel.getCreateDate());
		model.setModifiedDate(soapModel.getModifiedDate());
		model.setHashProviderName(soapModel.getHashProviderName());
		model.setHashProviderMeta(soapModel.getHashProviderMeta());

		return model;
	}

	/**
	 * Converts the soap model instances into normal model instances.
	 *
	 * @param soapModels the soap model instances to convert
	 * @return the normal model instances
	 */
	public static List<PasswordHashProvider> toModels(
		PasswordHashProviderSoap[] soapModels) {

		if (soapModels == null) {
			return null;
		}

		List<PasswordHashProvider> models = new ArrayList<PasswordHashProvider>(
			soapModels.length);

		for (PasswordHashProviderSoap soapModel : soapModels) {
			models.add(toModel(soapModel));
		}

		return models;
	}

	public PasswordHashProviderModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _passwordHashProviderId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setPasswordHashProviderId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _passwordHashProviderId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return PasswordHashProvider.class;
	}

	@Override
	public String getModelClassName() {
		return PasswordHashProvider.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<PasswordHashProvider, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		for (Map.Entry<String, Function<PasswordHashProvider, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<PasswordHashProvider, Object> attributeGetterFunction =
				entry.getValue();

			attributes.put(
				attributeName,
				attributeGetterFunction.apply((PasswordHashProvider)this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<PasswordHashProvider, Object>>
			attributeSetterBiConsumers = getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<PasswordHashProvider, Object> attributeSetterBiConsumer =
				attributeSetterBiConsumers.get(attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(PasswordHashProvider)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<PasswordHashProvider, Object>>
		getAttributeGetterFunctions() {

		return _attributeGetterFunctions;
	}

	public Map<String, BiConsumer<PasswordHashProvider, Object>>
		getAttributeSetterBiConsumers() {

		return _attributeSetterBiConsumers;
	}

	private static Function<InvocationHandler, PasswordHashProvider>
		_getProxyProviderFunction() {

		Class<?> proxyClass = ProxyUtil.getProxyClass(
			PasswordHashProvider.class.getClassLoader(),
			PasswordHashProvider.class, ModelWrapper.class);

		try {
			Constructor<PasswordHashProvider> constructor =
				(Constructor<PasswordHashProvider>)proxyClass.getConstructor(
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

	private static final Map<String, Function<PasswordHashProvider, Object>>
		_attributeGetterFunctions;
	private static final Map<String, BiConsumer<PasswordHashProvider, Object>>
		_attributeSetterBiConsumers;

	static {
		Map<String, Function<PasswordHashProvider, Object>>
			attributeGetterFunctions =
				new LinkedHashMap
					<String, Function<PasswordHashProvider, Object>>();
		Map<String, BiConsumer<PasswordHashProvider, ?>>
			attributeSetterBiConsumers =
				new LinkedHashMap
					<String, BiConsumer<PasswordHashProvider, ?>>();

		attributeGetterFunctions.put(
			"mvccVersion", PasswordHashProvider::getMvccVersion);
		attributeSetterBiConsumers.put(
			"mvccVersion",
			(BiConsumer<PasswordHashProvider, Long>)
				PasswordHashProvider::setMvccVersion);
		attributeGetterFunctions.put("uuid", PasswordHashProvider::getUuid);
		attributeSetterBiConsumers.put(
			"uuid",
			(BiConsumer<PasswordHashProvider, String>)
				PasswordHashProvider::setUuid);
		attributeGetterFunctions.put(
			"passwordHashProviderId",
			PasswordHashProvider::getPasswordHashProviderId);
		attributeSetterBiConsumers.put(
			"passwordHashProviderId",
			(BiConsumer<PasswordHashProvider, Long>)
				PasswordHashProvider::setPasswordHashProviderId);
		attributeGetterFunctions.put(
			"companyId", PasswordHashProvider::getCompanyId);
		attributeSetterBiConsumers.put(
			"companyId",
			(BiConsumer<PasswordHashProvider, Long>)
				PasswordHashProvider::setCompanyId);
		attributeGetterFunctions.put(
			"createDate", PasswordHashProvider::getCreateDate);
		attributeSetterBiConsumers.put(
			"createDate",
			(BiConsumer<PasswordHashProvider, Date>)
				PasswordHashProvider::setCreateDate);
		attributeGetterFunctions.put(
			"modifiedDate", PasswordHashProvider::getModifiedDate);
		attributeSetterBiConsumers.put(
			"modifiedDate",
			(BiConsumer<PasswordHashProvider, Date>)
				PasswordHashProvider::setModifiedDate);
		attributeGetterFunctions.put(
			"hashProviderName", PasswordHashProvider::getHashProviderName);
		attributeSetterBiConsumers.put(
			"hashProviderName",
			(BiConsumer<PasswordHashProvider, String>)
				PasswordHashProvider::setHashProviderName);
		attributeGetterFunctions.put(
			"hashProviderMeta", PasswordHashProvider::getHashProviderMeta);
		attributeSetterBiConsumers.put(
			"hashProviderMeta",
			(BiConsumer<PasswordHashProvider, String>)
				PasswordHashProvider::setHashProviderMeta);

		_attributeGetterFunctions = Collections.unmodifiableMap(
			attributeGetterFunctions);
		_attributeSetterBiConsumers = Collections.unmodifiableMap(
			(Map)attributeSetterBiConsumers);
	}

	@JSON
	@Override
	public long getMvccVersion() {
		return _mvccVersion;
	}

	@Override
	public void setMvccVersion(long mvccVersion) {
		_mvccVersion = mvccVersion;
	}

	@JSON
	@Override
	public String getUuid() {
		if (_uuid == null) {
			return "";
		}
		else {
			return _uuid;
		}
	}

	@Override
	public void setUuid(String uuid) {
		_columnBitmask |= UUID_COLUMN_BITMASK;

		if (_originalUuid == null) {
			_originalUuid = _uuid;
		}

		_uuid = uuid;
	}

	public String getOriginalUuid() {
		return GetterUtil.getString(_originalUuid);
	}

	@JSON
	@Override
	public long getPasswordHashProviderId() {
		return _passwordHashProviderId;
	}

	@Override
	public void setPasswordHashProviderId(long passwordHashProviderId) {
		_passwordHashProviderId = passwordHashProviderId;
	}

	@JSON
	@Override
	public long getCompanyId() {
		return _companyId;
	}

	@Override
	public void setCompanyId(long companyId) {
		_columnBitmask |= COMPANYID_COLUMN_BITMASK;

		if (!_setOriginalCompanyId) {
			_setOriginalCompanyId = true;

			_originalCompanyId = _companyId;
		}

		_companyId = companyId;
	}

	public long getOriginalCompanyId() {
		return _originalCompanyId;
	}

	@JSON
	@Override
	public Date getCreateDate() {
		return _createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	@JSON
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

		_modifiedDate = modifiedDate;
	}

	@JSON
	@Override
	public String getHashProviderName() {
		if (_hashProviderName == null) {
			return "";
		}
		else {
			return _hashProviderName;
		}
	}

	@Override
	public void setHashProviderName(String hashProviderName) {
		_columnBitmask |= HASHPROVIDERNAME_COLUMN_BITMASK;

		if (_originalHashProviderName == null) {
			_originalHashProviderName = _hashProviderName;
		}

		_hashProviderName = hashProviderName;
	}

	public String getOriginalHashProviderName() {
		return GetterUtil.getString(_originalHashProviderName);
	}

	@JSON
	@Override
	public String getHashProviderMeta() {
		if (_hashProviderMeta == null) {
			return "";
		}
		else {
			return _hashProviderMeta;
		}
	}

	@Override
	public void setHashProviderMeta(String hashProviderMeta) {
		_columnBitmask |= HASHPROVIDERMETA_COLUMN_BITMASK;

		if (_originalHashProviderMeta == null) {
			_originalHashProviderMeta = _hashProviderMeta;
		}

		_hashProviderMeta = hashProviderMeta;
	}

	public String getOriginalHashProviderMeta() {
		return GetterUtil.getString(_originalHashProviderMeta);
	}

	@Override
	public StagedModelType getStagedModelType() {
		return new StagedModelType(
			PortalUtil.getClassNameId(PasswordHashProvider.class.getName()));
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(
			getCompanyId(), PasswordHashProvider.class.getName(),
			getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public PasswordHashProvider toEscapedModel() {
		if (_escapedModel == null) {
			Function<InvocationHandler, PasswordHashProvider>
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
		PasswordHashProviderImpl passwordHashProviderImpl =
			new PasswordHashProviderImpl();

		passwordHashProviderImpl.setMvccVersion(getMvccVersion());
		passwordHashProviderImpl.setUuid(getUuid());
		passwordHashProviderImpl.setPasswordHashProviderId(
			getPasswordHashProviderId());
		passwordHashProviderImpl.setCompanyId(getCompanyId());
		passwordHashProviderImpl.setCreateDate(getCreateDate());
		passwordHashProviderImpl.setModifiedDate(getModifiedDate());
		passwordHashProviderImpl.setHashProviderName(getHashProviderName());
		passwordHashProviderImpl.setHashProviderMeta(getHashProviderMeta());

		passwordHashProviderImpl.resetOriginalValues();

		return passwordHashProviderImpl;
	}

	@Override
	public int compareTo(PasswordHashProvider passwordHashProvider) {
		long primaryKey = passwordHashProvider.getPrimaryKey();

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
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof PasswordHashProvider)) {
			return false;
		}

		PasswordHashProvider passwordHashProvider = (PasswordHashProvider)obj;

		long primaryKey = passwordHashProvider.getPrimaryKey();

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
		return _entityCacheEnabled;
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _finderCacheEnabled;
	}

	@Override
	public void resetOriginalValues() {
		PasswordHashProviderModelImpl passwordHashProviderModelImpl = this;

		passwordHashProviderModelImpl._originalUuid =
			passwordHashProviderModelImpl._uuid;

		passwordHashProviderModelImpl._originalCompanyId =
			passwordHashProviderModelImpl._companyId;

		passwordHashProviderModelImpl._setOriginalCompanyId = false;

		passwordHashProviderModelImpl._setModifiedDate = false;

		passwordHashProviderModelImpl._originalHashProviderName =
			passwordHashProviderModelImpl._hashProviderName;

		passwordHashProviderModelImpl._originalHashProviderMeta =
			passwordHashProviderModelImpl._hashProviderMeta;

		passwordHashProviderModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<PasswordHashProvider> toCacheModel() {
		PasswordHashProviderCacheModel passwordHashProviderCacheModel =
			new PasswordHashProviderCacheModel();

		passwordHashProviderCacheModel.mvccVersion = getMvccVersion();

		passwordHashProviderCacheModel.uuid = getUuid();

		String uuid = passwordHashProviderCacheModel.uuid;

		if ((uuid != null) && (uuid.length() == 0)) {
			passwordHashProviderCacheModel.uuid = null;
		}

		passwordHashProviderCacheModel.passwordHashProviderId =
			getPasswordHashProviderId();

		passwordHashProviderCacheModel.companyId = getCompanyId();

		Date createDate = getCreateDate();

		if (createDate != null) {
			passwordHashProviderCacheModel.createDate = createDate.getTime();
		}
		else {
			passwordHashProviderCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			passwordHashProviderCacheModel.modifiedDate =
				modifiedDate.getTime();
		}
		else {
			passwordHashProviderCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		passwordHashProviderCacheModel.hashProviderName = getHashProviderName();

		String hashProviderName =
			passwordHashProviderCacheModel.hashProviderName;

		if ((hashProviderName != null) && (hashProviderName.length() == 0)) {
			passwordHashProviderCacheModel.hashProviderName = null;
		}

		passwordHashProviderCacheModel.hashProviderMeta = getHashProviderMeta();

		String hashProviderMeta =
			passwordHashProviderCacheModel.hashProviderMeta;

		if ((hashProviderMeta != null) && (hashProviderMeta.length() == 0)) {
			passwordHashProviderCacheModel.hashProviderMeta = null;
		}

		return passwordHashProviderCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<PasswordHashProvider, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			4 * attributeGetterFunctions.size() + 2);

		sb.append("{");

		for (Map.Entry<String, Function<PasswordHashProvider, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<PasswordHashProvider, Object> attributeGetterFunction =
				entry.getValue();

			sb.append(attributeName);
			sb.append("=");
			sb.append(
				attributeGetterFunction.apply((PasswordHashProvider)this));
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
		Map<String, Function<PasswordHashProvider, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			5 * attributeGetterFunctions.size() + 4);

		sb.append("<model><model-name>");
		sb.append(getModelClassName());
		sb.append("</model-name>");

		for (Map.Entry<String, Function<PasswordHashProvider, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<PasswordHashProvider, Object> attributeGetterFunction =
				entry.getValue();

			sb.append("<column><column-name>");
			sb.append(attributeName);
			sb.append("</column-name><column-value><![CDATA[");
			sb.append(
				attributeGetterFunction.apply((PasswordHashProvider)this));
			sb.append("]]></column-value></column>");
		}

		sb.append("</model>");

		return sb.toString();
	}

	private static class EscapedModelProxyProviderFunctionHolder {

		private static final Function<InvocationHandler, PasswordHashProvider>
			_escapedModelProxyProviderFunction = _getProxyProviderFunction();

	}

	private static boolean _entityCacheEnabled;
	private static boolean _finderCacheEnabled;

	private long _mvccVersion;
	private String _uuid;
	private String _originalUuid;
	private long _passwordHashProviderId;
	private long _companyId;
	private long _originalCompanyId;
	private boolean _setOriginalCompanyId;
	private Date _createDate;
	private Date _modifiedDate;
	private boolean _setModifiedDate;
	private String _hashProviderName;
	private String _originalHashProviderName;
	private String _hashProviderMeta;
	private String _originalHashProviderMeta;
	private long _columnBitmask;
	private PasswordHashProvider _escapedModel;

}