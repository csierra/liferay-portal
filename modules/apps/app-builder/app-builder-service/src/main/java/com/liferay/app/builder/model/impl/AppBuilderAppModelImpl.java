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

import com.liferay.app.builder.model.AppBuilderApp;
import com.liferay.app.builder.model.AppBuilderAppModel;
import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;
import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.LocaleException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.Serializable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;

import java.sql.Types;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * The base model implementation for the AppBuilderApp service. Represents a row in the &quot;AppBuilderApp&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface <code>AppBuilderAppModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link AppBuilderAppImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AppBuilderAppImpl
 * @generated
 */
public class AppBuilderAppModelImpl
	extends BaseModelImpl<AppBuilderApp> implements AppBuilderAppModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a app builder app model instance should use the <code>AppBuilderApp</code> interface instead.
	 */
	public static final String TABLE_NAME = "AppBuilderApp";

	public static final Object[][] TABLE_COLUMNS = {
		{"uuid_", Types.VARCHAR}, {"appBuilderAppId", Types.BIGINT},
		{"groupId", Types.BIGINT}, {"companyId", Types.BIGINT},
		{"userId", Types.BIGINT}, {"userName", Types.VARCHAR},
		{"createDate", Types.TIMESTAMP}, {"modifiedDate", Types.TIMESTAMP},
		{"active_", Types.BOOLEAN}, {"ddlRecordSetId", Types.BIGINT},
		{"ddmStructureId", Types.BIGINT},
		{"ddmStructureLayoutId", Types.BIGINT},
		{"deDataListViewId", Types.BIGINT}, {"name", Types.VARCHAR},
		{"scope", Types.VARCHAR}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("uuid_", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("appBuilderAppId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("groupId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("modifiedDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("active_", Types.BOOLEAN);
		TABLE_COLUMNS_MAP.put("ddlRecordSetId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("ddmStructureId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("ddmStructureLayoutId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("deDataListViewId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("name", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("scope", Types.VARCHAR);
	}

	public static final String TABLE_SQL_CREATE =
		"create table AppBuilderApp (uuid_ VARCHAR(75) null,appBuilderAppId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,active_ BOOLEAN,ddlRecordSetId LONG,ddmStructureId LONG,ddmStructureLayoutId LONG,deDataListViewId LONG,name STRING null,scope VARCHAR(75) null)";

	public static final String TABLE_SQL_DROP = "drop table AppBuilderApp";

	public static final String ORDER_BY_JPQL =
		" ORDER BY appBuilderApp.appBuilderAppId ASC";

	public static final String ORDER_BY_SQL =
		" ORDER BY AppBuilderApp.appBuilderAppId ASC";

	public static final String DATA_SOURCE = "liferayDataSource";

	public static final String SESSION_FACTORY = "liferaySessionFactory";

	public static final String TX_MANAGER = "liferayTransactionManager";

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)
	 */
	@Deprecated
	public static final long ACTIVE_COLUMN_BITMASK = 1L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)
	 */
	@Deprecated
	public static final long COMPANYID_COLUMN_BITMASK = 2L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)
	 */
	@Deprecated
	public static final long DDMSTRUCTUREID_COLUMN_BITMASK = 4L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)
	 */
	@Deprecated
	public static final long GROUPID_COLUMN_BITMASK = 8L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)
	 */
	@Deprecated
	public static final long SCOPE_COLUMN_BITMASK = 16L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)
	 */
	@Deprecated
	public static final long UUID_COLUMN_BITMASK = 32L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *		#getColumnBitmask(String)
	 */
	@Deprecated
	public static final long APPBUILDERAPPID_COLUMN_BITMASK = 64L;

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

	public AppBuilderAppModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _appBuilderAppId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setAppBuilderAppId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _appBuilderAppId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return AppBuilderApp.class;
	}

	@Override
	public String getModelClassName() {
		return AppBuilderApp.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<AppBuilderApp, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		for (Map.Entry<String, Function<AppBuilderApp, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<AppBuilderApp, Object> attributeGetterFunction =
				entry.getValue();

			attributes.put(
				attributeName,
				attributeGetterFunction.apply((AppBuilderApp)this));
		}

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<AppBuilderApp, Object>>
			attributeSetterBiConsumers = getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<AppBuilderApp, Object> attributeSetterBiConsumer =
				attributeSetterBiConsumers.get(attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(AppBuilderApp)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<AppBuilderApp, Object>>
		getAttributeGetterFunctions() {

		return _attributeGetterFunctions;
	}

	public Map<String, BiConsumer<AppBuilderApp, Object>>
		getAttributeSetterBiConsumers() {

		return _attributeSetterBiConsumers;
	}

	private static Function<InvocationHandler, AppBuilderApp>
		_getProxyProviderFunction() {

		Class<?> proxyClass = ProxyUtil.getProxyClass(
			AppBuilderApp.class.getClassLoader(), AppBuilderApp.class,
			ModelWrapper.class);

		try {
			Constructor<AppBuilderApp> constructor =
				(Constructor<AppBuilderApp>)proxyClass.getConstructor(
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

	private static final Map<String, Function<AppBuilderApp, Object>>
		_attributeGetterFunctions;
	private static final Map<String, BiConsumer<AppBuilderApp, Object>>
		_attributeSetterBiConsumers;

	static {
		Map<String, Function<AppBuilderApp, Object>> attributeGetterFunctions =
			new LinkedHashMap<String, Function<AppBuilderApp, Object>>();
		Map<String, BiConsumer<AppBuilderApp, ?>> attributeSetterBiConsumers =
			new LinkedHashMap<String, BiConsumer<AppBuilderApp, ?>>();

		attributeGetterFunctions.put("uuid", AppBuilderApp::getUuid);
		attributeSetterBiConsumers.put(
			"uuid", (BiConsumer<AppBuilderApp, String>)AppBuilderApp::setUuid);
		attributeGetterFunctions.put(
			"appBuilderAppId", AppBuilderApp::getAppBuilderAppId);
		attributeSetterBiConsumers.put(
			"appBuilderAppId",
			(BiConsumer<AppBuilderApp, Long>)AppBuilderApp::setAppBuilderAppId);
		attributeGetterFunctions.put("groupId", AppBuilderApp::getGroupId);
		attributeSetterBiConsumers.put(
			"groupId",
			(BiConsumer<AppBuilderApp, Long>)AppBuilderApp::setGroupId);
		attributeGetterFunctions.put("companyId", AppBuilderApp::getCompanyId);
		attributeSetterBiConsumers.put(
			"companyId",
			(BiConsumer<AppBuilderApp, Long>)AppBuilderApp::setCompanyId);
		attributeGetterFunctions.put("userId", AppBuilderApp::getUserId);
		attributeSetterBiConsumers.put(
			"userId",
			(BiConsumer<AppBuilderApp, Long>)AppBuilderApp::setUserId);
		attributeGetterFunctions.put("userName", AppBuilderApp::getUserName);
		attributeSetterBiConsumers.put(
			"userName",
			(BiConsumer<AppBuilderApp, String>)AppBuilderApp::setUserName);
		attributeGetterFunctions.put(
			"createDate", AppBuilderApp::getCreateDate);
		attributeSetterBiConsumers.put(
			"createDate",
			(BiConsumer<AppBuilderApp, Date>)AppBuilderApp::setCreateDate);
		attributeGetterFunctions.put(
			"modifiedDate", AppBuilderApp::getModifiedDate);
		attributeSetterBiConsumers.put(
			"modifiedDate",
			(BiConsumer<AppBuilderApp, Date>)AppBuilderApp::setModifiedDate);
		attributeGetterFunctions.put("active", AppBuilderApp::getActive);
		attributeSetterBiConsumers.put(
			"active",
			(BiConsumer<AppBuilderApp, Boolean>)AppBuilderApp::setActive);
		attributeGetterFunctions.put(
			"ddlRecordSetId", AppBuilderApp::getDdlRecordSetId);
		attributeSetterBiConsumers.put(
			"ddlRecordSetId",
			(BiConsumer<AppBuilderApp, Long>)AppBuilderApp::setDdlRecordSetId);
		attributeGetterFunctions.put(
			"ddmStructureId", AppBuilderApp::getDdmStructureId);
		attributeSetterBiConsumers.put(
			"ddmStructureId",
			(BiConsumer<AppBuilderApp, Long>)AppBuilderApp::setDdmStructureId);
		attributeGetterFunctions.put(
			"ddmStructureLayoutId", AppBuilderApp::getDdmStructureLayoutId);
		attributeSetterBiConsumers.put(
			"ddmStructureLayoutId",
			(BiConsumer<AppBuilderApp, Long>)
				AppBuilderApp::setDdmStructureLayoutId);
		attributeGetterFunctions.put(
			"deDataListViewId", AppBuilderApp::getDeDataListViewId);
		attributeSetterBiConsumers.put(
			"deDataListViewId",
			(BiConsumer<AppBuilderApp, Long>)
				AppBuilderApp::setDeDataListViewId);
		attributeGetterFunctions.put("name", AppBuilderApp::getName);
		attributeSetterBiConsumers.put(
			"name", (BiConsumer<AppBuilderApp, String>)AppBuilderApp::setName);
		attributeGetterFunctions.put("scope", AppBuilderApp::getScope);
		attributeSetterBiConsumers.put(
			"scope",
			(BiConsumer<AppBuilderApp, String>)AppBuilderApp::setScope);

		_attributeGetterFunctions = Collections.unmodifiableMap(
			attributeGetterFunctions);
		_attributeSetterBiConsumers = Collections.unmodifiableMap(
			(Map)attributeSetterBiConsumers);
	}

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
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_uuid = uuid;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public String getOriginalUuid() {
		return getColumnOriginalValue("uuid_");
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

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public long getOriginalGroupId() {
		return GetterUtil.getLong(getColumnOriginalValue("groupId"));
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
	public boolean getActive() {
		return _active;
	}

	@Override
	public boolean isActive() {
		return _active;
	}

	@Override
	public void setActive(boolean active) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_active = active;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public boolean getOriginalActive() {
		return GetterUtil.getBoolean(getColumnOriginalValue("active_"));
	}

	@Override
	public long getDdlRecordSetId() {
		return _ddlRecordSetId;
	}

	@Override
	public void setDdlRecordSetId(long ddlRecordSetId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_ddlRecordSetId = ddlRecordSetId;
	}

	@Override
	public long getDdmStructureId() {
		return _ddmStructureId;
	}

	@Override
	public void setDdmStructureId(long ddmStructureId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_ddmStructureId = ddmStructureId;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public long getOriginalDdmStructureId() {
		return GetterUtil.getLong(getColumnOriginalValue("ddmStructureId"));
	}

	@Override
	public long getDdmStructureLayoutId() {
		return _ddmStructureLayoutId;
	}

	@Override
	public void setDdmStructureLayoutId(long ddmStructureLayoutId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_ddmStructureLayoutId = ddmStructureLayoutId;
	}

	@Override
	public long getDeDataListViewId() {
		return _deDataListViewId;
	}

	@Override
	public void setDeDataListViewId(long deDataListViewId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_deDataListViewId = deDataListViewId;
	}

	@Override
	public String getName() {
		if (_name == null) {
			return "";
		}
		else {
			return _name;
		}
	}

	@Override
	public String getName(Locale locale) {
		String languageId = LocaleUtil.toLanguageId(locale);

		return getName(languageId);
	}

	@Override
	public String getName(Locale locale, boolean useDefault) {
		String languageId = LocaleUtil.toLanguageId(locale);

		return getName(languageId, useDefault);
	}

	@Override
	public String getName(String languageId) {
		return LocalizationUtil.getLocalization(getName(), languageId);
	}

	@Override
	public String getName(String languageId, boolean useDefault) {
		return LocalizationUtil.getLocalization(
			getName(), languageId, useDefault);
	}

	@Override
	public String getNameCurrentLanguageId() {
		return _nameCurrentLanguageId;
	}

	@JSON
	@Override
	public String getNameCurrentValue() {
		Locale locale = getLocale(_nameCurrentLanguageId);

		return getName(locale);
	}

	@Override
	public Map<Locale, String> getNameMap() {
		return LocalizationUtil.getLocalizationMap(getName());
	}

	@Override
	public void setName(String name) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_name = name;
	}

	@Override
	public void setName(String name, Locale locale) {
		setName(name, locale, LocaleUtil.getSiteDefault());
	}

	@Override
	public void setName(String name, Locale locale, Locale defaultLocale) {
		String languageId = LocaleUtil.toLanguageId(locale);
		String defaultLanguageId = LocaleUtil.toLanguageId(defaultLocale);

		if (Validator.isNotNull(name)) {
			setName(
				LocalizationUtil.updateLocalization(
					getName(), "Name", name, languageId, defaultLanguageId));
		}
		else {
			setName(
				LocalizationUtil.removeLocalization(
					getName(), "Name", languageId));
		}
	}

	@Override
	public void setNameCurrentLanguageId(String languageId) {
		_nameCurrentLanguageId = languageId;
	}

	@Override
	public void setNameMap(Map<Locale, String> nameMap) {
		setNameMap(nameMap, LocaleUtil.getSiteDefault());
	}

	@Override
	public void setNameMap(Map<Locale, String> nameMap, Locale defaultLocale) {
		if (nameMap == null) {
			return;
		}

		setName(
			LocalizationUtil.updateLocalization(
				nameMap, getName(), "Name",
				LocaleUtil.toLanguageId(defaultLocale)));
	}

	@Override
	public String getScope() {
		if (_scope == null) {
			return "";
		}
		else {
			return _scope;
		}
	}

	@Override
	public void setScope(String scope) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_scope = scope;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public String getOriginalScope() {
		return getColumnOriginalValue("scope");
	}

	@Override
	public StagedModelType getStagedModelType() {
		return new StagedModelType(
			PortalUtil.getClassNameId(AppBuilderApp.class.getName()));
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
			getCompanyId(), AppBuilderApp.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public String[] getAvailableLanguageIds() {
		Set<String> availableLanguageIds = new TreeSet<String>();

		Map<Locale, String> nameMap = getNameMap();

		for (Map.Entry<Locale, String> entry : nameMap.entrySet()) {
			Locale locale = entry.getKey();
			String value = entry.getValue();

			if (Validator.isNotNull(value)) {
				availableLanguageIds.add(LocaleUtil.toLanguageId(locale));
			}
		}

		return availableLanguageIds.toArray(
			new String[availableLanguageIds.size()]);
	}

	@Override
	public String getDefaultLanguageId() {
		String xml = getName();

		if (xml == null) {
			return "";
		}

		Locale defaultLocale = LocaleUtil.getSiteDefault();

		return LocalizationUtil.getDefaultLanguageId(xml, defaultLocale);
	}

	@Override
	public void prepareLocalizedFieldsForImport() throws LocaleException {
		Locale defaultLocale = LocaleUtil.fromLanguageId(
			getDefaultLanguageId());

		Locale[] availableLocales = LocaleUtil.fromLanguageIds(
			getAvailableLanguageIds());

		Locale defaultImportLocale = LocalizationUtil.getDefaultImportLocale(
			AppBuilderApp.class.getName(), getPrimaryKey(), defaultLocale,
			availableLocales);

		prepareLocalizedFieldsForImport(defaultImportLocale);
	}

	@Override
	@SuppressWarnings("unused")
	public void prepareLocalizedFieldsForImport(Locale defaultImportLocale)
		throws LocaleException {

		Locale defaultLocale = LocaleUtil.getSiteDefault();

		String modelDefaultLanguageId = getDefaultLanguageId();

		String name = getName(defaultLocale);

		if (Validator.isNull(name)) {
			setName(getName(modelDefaultLanguageId), defaultLocale);
		}
		else {
			setName(getName(defaultLocale), defaultLocale, defaultLocale);
		}
	}

	@Override
	public AppBuilderApp toEscapedModel() {
		if (_escapedModel == null) {
			Function<InvocationHandler, AppBuilderApp>
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
		AppBuilderAppImpl appBuilderAppImpl = new AppBuilderAppImpl();

		appBuilderAppImpl.setUuid(getUuid());
		appBuilderAppImpl.setAppBuilderAppId(getAppBuilderAppId());
		appBuilderAppImpl.setGroupId(getGroupId());
		appBuilderAppImpl.setCompanyId(getCompanyId());
		appBuilderAppImpl.setUserId(getUserId());
		appBuilderAppImpl.setUserName(getUserName());
		appBuilderAppImpl.setCreateDate(getCreateDate());
		appBuilderAppImpl.setModifiedDate(getModifiedDate());
		appBuilderAppImpl.setActive(isActive());
		appBuilderAppImpl.setDdlRecordSetId(getDdlRecordSetId());
		appBuilderAppImpl.setDdmStructureId(getDdmStructureId());
		appBuilderAppImpl.setDdmStructureLayoutId(getDdmStructureLayoutId());
		appBuilderAppImpl.setDeDataListViewId(getDeDataListViewId());
		appBuilderAppImpl.setName(getName());
		appBuilderAppImpl.setScope(getScope());

		appBuilderAppImpl.resetOriginalValues();

		return appBuilderAppImpl;
	}

	@Override
	public int compareTo(AppBuilderApp appBuilderApp) {
		long primaryKey = appBuilderApp.getPrimaryKey();

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

		if (!(object instanceof AppBuilderApp)) {
			return false;
		}

		AppBuilderApp appBuilderApp = (AppBuilderApp)object;

		long primaryKey = appBuilderApp.getPrimaryKey();

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
	public CacheModel<AppBuilderApp> toCacheModel() {
		AppBuilderAppCacheModel appBuilderAppCacheModel =
			new AppBuilderAppCacheModel();

		appBuilderAppCacheModel.uuid = getUuid();

		String uuid = appBuilderAppCacheModel.uuid;

		if ((uuid != null) && (uuid.length() == 0)) {
			appBuilderAppCacheModel.uuid = null;
		}

		appBuilderAppCacheModel.appBuilderAppId = getAppBuilderAppId();

		appBuilderAppCacheModel.groupId = getGroupId();

		appBuilderAppCacheModel.companyId = getCompanyId();

		appBuilderAppCacheModel.userId = getUserId();

		appBuilderAppCacheModel.userName = getUserName();

		String userName = appBuilderAppCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			appBuilderAppCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			appBuilderAppCacheModel.createDate = createDate.getTime();
		}
		else {
			appBuilderAppCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			appBuilderAppCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			appBuilderAppCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		appBuilderAppCacheModel.active = isActive();

		appBuilderAppCacheModel.ddlRecordSetId = getDdlRecordSetId();

		appBuilderAppCacheModel.ddmStructureId = getDdmStructureId();

		appBuilderAppCacheModel.ddmStructureLayoutId =
			getDdmStructureLayoutId();

		appBuilderAppCacheModel.deDataListViewId = getDeDataListViewId();

		appBuilderAppCacheModel.name = getName();

		String name = appBuilderAppCacheModel.name;

		if ((name != null) && (name.length() == 0)) {
			appBuilderAppCacheModel.name = null;
		}

		appBuilderAppCacheModel.scope = getScope();

		String scope = appBuilderAppCacheModel.scope;

		if ((scope != null) && (scope.length() == 0)) {
			appBuilderAppCacheModel.scope = null;
		}

		return appBuilderAppCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<AppBuilderApp, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			4 * attributeGetterFunctions.size() + 2);

		sb.append("{");

		for (Map.Entry<String, Function<AppBuilderApp, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<AppBuilderApp, Object> attributeGetterFunction =
				entry.getValue();

			sb.append(attributeName);
			sb.append("=");
			sb.append(attributeGetterFunction.apply((AppBuilderApp)this));
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
		Map<String, Function<AppBuilderApp, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			5 * attributeGetterFunctions.size() + 4);

		sb.append("<model><model-name>");
		sb.append(getModelClassName());
		sb.append("</model-name>");

		for (Map.Entry<String, Function<AppBuilderApp, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<AppBuilderApp, Object> attributeGetterFunction =
				entry.getValue();

			sb.append("<column><column-name>");
			sb.append(attributeName);
			sb.append("</column-name><column-value><![CDATA[");
			sb.append(attributeGetterFunction.apply((AppBuilderApp)this));
			sb.append("]]></column-value></column>");
		}

		sb.append("</model>");

		return sb.toString();
	}

	private static class EscapedModelProxyProviderFunctionHolder {

		private static final Function<InvocationHandler, AppBuilderApp>
			_escapedModelProxyProviderFunction = _getProxyProviderFunction();

	}

	private String _uuid;
	private long _appBuilderAppId;
	private long _groupId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private boolean _setModifiedDate;
	private boolean _active;
	private long _ddlRecordSetId;
	private long _ddmStructureId;
	private long _ddmStructureLayoutId;
	private long _deDataListViewId;
	private String _name;
	private String _nameCurrentLanguageId;
	private String _scope;

	public <T> T getColumnValue(String columnName) {
		columnName = _attributeNames.getOrDefault(columnName, columnName);

		Function<AppBuilderApp, Object> function =
			_attributeGetterFunctions.get(columnName);

		if (function == null) {
			throw new IllegalArgumentException(
				"No attribute getter function found for " + columnName);
		}

		return (T)function.apply((AppBuilderApp)this);
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

		_columnOriginalValues.put("uuid_", _uuid);
		_columnOriginalValues.put("appBuilderAppId", _appBuilderAppId);
		_columnOriginalValues.put("groupId", _groupId);
		_columnOriginalValues.put("companyId", _companyId);
		_columnOriginalValues.put("userId", _userId);
		_columnOriginalValues.put("userName", _userName);
		_columnOriginalValues.put("createDate", _createDate);
		_columnOriginalValues.put("modifiedDate", _modifiedDate);
		_columnOriginalValues.put("active_", _active);
		_columnOriginalValues.put("ddlRecordSetId", _ddlRecordSetId);
		_columnOriginalValues.put("ddmStructureId", _ddmStructureId);
		_columnOriginalValues.put(
			"ddmStructureLayoutId", _ddmStructureLayoutId);
		_columnOriginalValues.put("deDataListViewId", _deDataListViewId);
		_columnOriginalValues.put("name", _name);
		_columnOriginalValues.put("scope", _scope);
	}

	private static final Map<String, String> _attributeNames;

	static {
		Map<String, String> attributeNames = new HashMap<>();

		attributeNames.put("uuid_", "uuid");
		attributeNames.put("active_", "active");

		_attributeNames = Collections.unmodifiableMap(attributeNames);
	}

	private transient Map<String, Object> _columnOriginalValues;

	public static long getColumnBitmask(String columnName) {
		return _columnBitmasks.get(columnName);
	}

	private static final Map<String, Long> _columnBitmasks;

	static {
		Map<String, Long> columnBitmasks = new HashMap<>();

		columnBitmasks.put("uuid_", 1L);

		columnBitmasks.put("appBuilderAppId", 2L);

		columnBitmasks.put("groupId", 4L);

		columnBitmasks.put("companyId", 8L);

		columnBitmasks.put("userId", 16L);

		columnBitmasks.put("userName", 32L);

		columnBitmasks.put("createDate", 64L);

		columnBitmasks.put("modifiedDate", 128L);

		columnBitmasks.put("active_", 256L);

		columnBitmasks.put("ddlRecordSetId", 512L);

		columnBitmasks.put("ddmStructureId", 1024L);

		columnBitmasks.put("ddmStructureLayoutId", 2048L);

		columnBitmasks.put("deDataListViewId", 4096L);

		columnBitmasks.put("name", 8192L);

		columnBitmasks.put("scope", 16384L);

		_columnBitmasks = Collections.unmodifiableMap(columnBitmasks);
	}

	private long _columnBitmask;
	private AppBuilderApp _escapedModel;

}