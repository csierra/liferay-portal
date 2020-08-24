/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.app.builder.workflow.model.impl;

import com.liferay.app.builder.workflow.model.AppBuilderWorkflowTaskLink;
import com.liferay.app.builder.workflow.model.AppBuilderWorkflowTaskLinkModel;
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
 * The base model implementation for the AppBuilderWorkflowTaskLink service. Represents a row in the &quot;AppBuilderWorkflowTaskLink&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface <code>AppBuilderWorkflowTaskLinkModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link AppBuilderWorkflowTaskLinkImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AppBuilderWorkflowTaskLinkImpl
 * @generated
 */
public class AppBuilderWorkflowTaskLinkModelImpl
	extends BaseModelImpl<AppBuilderWorkflowTaskLink>
	implements AppBuilderWorkflowTaskLinkModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a app builder workflow task link model instance should use the <code>AppBuilderWorkflowTaskLink</code> interface instead.
	 */
	public static final String TABLE_NAME = "AppBuilderWorkflowTaskLink";

	public static final Object[][] TABLE_COLUMNS = {
		{"mvccVersion", Types.BIGINT},
		{"appBuilderWorkflowTaskLinkId", Types.BIGINT},
		{"companyId", Types.BIGINT}, {"appBuilderAppId", Types.BIGINT},
		{"appBuilderAppVersionId", Types.BIGINT},
		{"ddmStructureLayoutId", Types.BIGINT}, {"readOnly", Types.BOOLEAN},
		{"workflowTaskName", Types.VARCHAR}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("mvccVersion", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("appBuilderWorkflowTaskLinkId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("appBuilderAppId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("appBuilderAppVersionId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("ddmStructureLayoutId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("readOnly", Types.BOOLEAN);
		TABLE_COLUMNS_MAP.put("workflowTaskName", Types.VARCHAR);
	}

	public static final String TABLE_SQL_CREATE =
		"create table AppBuilderWorkflowTaskLink (mvccVersion LONG default 0 not null,appBuilderWorkflowTaskLinkId LONG not null primary key,companyId LONG,appBuilderAppId LONG,appBuilderAppVersionId LONG,ddmStructureLayoutId LONG,readOnly BOOLEAN,workflowTaskName VARCHAR(75) null)";

	public static final String TABLE_SQL_DROP =
		"drop table AppBuilderWorkflowTaskLink";

	public static final String ORDER_BY_JPQL =
		" ORDER BY appBuilderWorkflowTaskLink.appBuilderWorkflowTaskLinkId ASC";

	public static final String ORDER_BY_SQL =
		" ORDER BY AppBuilderWorkflowTaskLink.appBuilderWorkflowTaskLinkId ASC";

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
	public static final long APPBUILDERAPPVERSIONID_COLUMN_BITMASK = 2L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)
	 */
	@Deprecated
	public static final long DDMSTRUCTURELAYOUTID_COLUMN_BITMASK = 4L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)
	 */
	@Deprecated
	public static final long WORKFLOWTASKNAME_COLUMN_BITMASK = 8L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *		#getColumnBitmask(String)
	 */
	@Deprecated
	public static final long APPBUILDERWORKFLOWTASKLINKID_COLUMN_BITMASK = 16L;

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

	public AppBuilderWorkflowTaskLinkModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _appBuilderWorkflowTaskLinkId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setAppBuilderWorkflowTaskLinkId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _appBuilderWorkflowTaskLinkId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return AppBuilderWorkflowTaskLink.class;
	}

	@Override
	public String getModelClassName() {
		return AppBuilderWorkflowTaskLink.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<AppBuilderWorkflowTaskLink, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		for (Map.Entry<String, Function<AppBuilderWorkflowTaskLink, Object>>
				entry : attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<AppBuilderWorkflowTaskLink, Object>
				attributeGetterFunction = entry.getValue();

			attributes.put(
				attributeName,
				attributeGetterFunction.apply(
					(AppBuilderWorkflowTaskLink)this));
		}

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<AppBuilderWorkflowTaskLink, Object>>
			attributeSetterBiConsumers = getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<AppBuilderWorkflowTaskLink, Object>
				attributeSetterBiConsumer = attributeSetterBiConsumers.get(
					attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(AppBuilderWorkflowTaskLink)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<AppBuilderWorkflowTaskLink, Object>>
		getAttributeGetterFunctions() {

		return _attributeGetterFunctions;
	}

	public Map<String, BiConsumer<AppBuilderWorkflowTaskLink, Object>>
		getAttributeSetterBiConsumers() {

		return _attributeSetterBiConsumers;
	}

	private static Function<InvocationHandler, AppBuilderWorkflowTaskLink>
		_getProxyProviderFunction() {

		Class<?> proxyClass = ProxyUtil.getProxyClass(
			AppBuilderWorkflowTaskLink.class.getClassLoader(),
			AppBuilderWorkflowTaskLink.class, ModelWrapper.class);

		try {
			Constructor<AppBuilderWorkflowTaskLink> constructor =
				(Constructor<AppBuilderWorkflowTaskLink>)
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
		<String, Function<AppBuilderWorkflowTaskLink, Object>>
			_attributeGetterFunctions;
	private static final Map
		<String, BiConsumer<AppBuilderWorkflowTaskLink, Object>>
			_attributeSetterBiConsumers;

	static {
		Map<String, Function<AppBuilderWorkflowTaskLink, Object>>
			attributeGetterFunctions =
				new LinkedHashMap
					<String, Function<AppBuilderWorkflowTaskLink, Object>>();
		Map<String, BiConsumer<AppBuilderWorkflowTaskLink, ?>>
			attributeSetterBiConsumers =
				new LinkedHashMap
					<String, BiConsumer<AppBuilderWorkflowTaskLink, ?>>();

		attributeGetterFunctions.put(
			"mvccVersion", AppBuilderWorkflowTaskLink::getMvccVersion);
		attributeSetterBiConsumers.put(
			"mvccVersion",
			(BiConsumer<AppBuilderWorkflowTaskLink, Long>)
				AppBuilderWorkflowTaskLink::setMvccVersion);
		attributeGetterFunctions.put(
			"appBuilderWorkflowTaskLinkId",
			AppBuilderWorkflowTaskLink::getAppBuilderWorkflowTaskLinkId);
		attributeSetterBiConsumers.put(
			"appBuilderWorkflowTaskLinkId",
			(BiConsumer<AppBuilderWorkflowTaskLink, Long>)
				AppBuilderWorkflowTaskLink::setAppBuilderWorkflowTaskLinkId);
		attributeGetterFunctions.put(
			"companyId", AppBuilderWorkflowTaskLink::getCompanyId);
		attributeSetterBiConsumers.put(
			"companyId",
			(BiConsumer<AppBuilderWorkflowTaskLink, Long>)
				AppBuilderWorkflowTaskLink::setCompanyId);
		attributeGetterFunctions.put(
			"appBuilderAppId", AppBuilderWorkflowTaskLink::getAppBuilderAppId);
		attributeSetterBiConsumers.put(
			"appBuilderAppId",
			(BiConsumer<AppBuilderWorkflowTaskLink, Long>)
				AppBuilderWorkflowTaskLink::setAppBuilderAppId);
		attributeGetterFunctions.put(
			"appBuilderAppVersionId",
			AppBuilderWorkflowTaskLink::getAppBuilderAppVersionId);
		attributeSetterBiConsumers.put(
			"appBuilderAppVersionId",
			(BiConsumer<AppBuilderWorkflowTaskLink, Long>)
				AppBuilderWorkflowTaskLink::setAppBuilderAppVersionId);
		attributeGetterFunctions.put(
			"ddmStructureLayoutId",
			AppBuilderWorkflowTaskLink::getDdmStructureLayoutId);
		attributeSetterBiConsumers.put(
			"ddmStructureLayoutId",
			(BiConsumer<AppBuilderWorkflowTaskLink, Long>)
				AppBuilderWorkflowTaskLink::setDdmStructureLayoutId);
		attributeGetterFunctions.put(
			"readOnly", AppBuilderWorkflowTaskLink::getReadOnly);
		attributeSetterBiConsumers.put(
			"readOnly",
			(BiConsumer<AppBuilderWorkflowTaskLink, Boolean>)
				AppBuilderWorkflowTaskLink::setReadOnly);
		attributeGetterFunctions.put(
			"workflowTaskName",
			AppBuilderWorkflowTaskLink::getWorkflowTaskName);
		attributeSetterBiConsumers.put(
			"workflowTaskName",
			(BiConsumer<AppBuilderWorkflowTaskLink, String>)
				AppBuilderWorkflowTaskLink::setWorkflowTaskName);

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
	public long getAppBuilderWorkflowTaskLinkId() {
		return _appBuilderWorkflowTaskLinkId;
	}

	@Override
	public void setAppBuilderWorkflowTaskLinkId(
		long appBuilderWorkflowTaskLinkId) {

		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_appBuilderWorkflowTaskLinkId = appBuilderWorkflowTaskLinkId;
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

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public long getOriginalAppBuilderAppVersionId() {
		return GetterUtil.getLong(
			getColumnOriginalValue("appBuilderAppVersionId"));
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

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public long getOriginalDdmStructureLayoutId() {
		return GetterUtil.getLong(
			getColumnOriginalValue("ddmStructureLayoutId"));
	}

	@Override
	public boolean getReadOnly() {
		return _readOnly;
	}

	@Override
	public boolean isReadOnly() {
		return _readOnly;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_readOnly = readOnly;
	}

	@Override
	public String getWorkflowTaskName() {
		if (_workflowTaskName == null) {
			return "";
		}
		else {
			return _workflowTaskName;
		}
	}

	@Override
	public void setWorkflowTaskName(String workflowTaskName) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_workflowTaskName = workflowTaskName;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public String getOriginalWorkflowTaskName() {
		return getColumnOriginalValue("workflowTaskName");
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
			getCompanyId(), AppBuilderWorkflowTaskLink.class.getName(),
			getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public AppBuilderWorkflowTaskLink toEscapedModel() {
		if (_escapedModel == null) {
			Function<InvocationHandler, AppBuilderWorkflowTaskLink>
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
		AppBuilderWorkflowTaskLinkImpl appBuilderWorkflowTaskLinkImpl =
			new AppBuilderWorkflowTaskLinkImpl();

		appBuilderWorkflowTaskLinkImpl.setMvccVersion(getMvccVersion());
		appBuilderWorkflowTaskLinkImpl.setAppBuilderWorkflowTaskLinkId(
			getAppBuilderWorkflowTaskLinkId());
		appBuilderWorkflowTaskLinkImpl.setCompanyId(getCompanyId());
		appBuilderWorkflowTaskLinkImpl.setAppBuilderAppId(getAppBuilderAppId());
		appBuilderWorkflowTaskLinkImpl.setAppBuilderAppVersionId(
			getAppBuilderAppVersionId());
		appBuilderWorkflowTaskLinkImpl.setDdmStructureLayoutId(
			getDdmStructureLayoutId());
		appBuilderWorkflowTaskLinkImpl.setReadOnly(isReadOnly());
		appBuilderWorkflowTaskLinkImpl.setWorkflowTaskName(
			getWorkflowTaskName());

		appBuilderWorkflowTaskLinkImpl.resetOriginalValues();

		return appBuilderWorkflowTaskLinkImpl;
	}

	@Override
	public int compareTo(
		AppBuilderWorkflowTaskLink appBuilderWorkflowTaskLink) {

		int value = 0;

		if (getAppBuilderWorkflowTaskLinkId() <
				appBuilderWorkflowTaskLink.getAppBuilderWorkflowTaskLinkId()) {

			value = -1;
		}
		else if (getAppBuilderWorkflowTaskLinkId() >
					appBuilderWorkflowTaskLink.
						getAppBuilderWorkflowTaskLinkId()) {

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

		if (!(object instanceof AppBuilderWorkflowTaskLink)) {
			return false;
		}

		AppBuilderWorkflowTaskLink appBuilderWorkflowTaskLink =
			(AppBuilderWorkflowTaskLink)object;

		long primaryKey = appBuilderWorkflowTaskLink.getPrimaryKey();

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
	public CacheModel<AppBuilderWorkflowTaskLink> toCacheModel() {
		AppBuilderWorkflowTaskLinkCacheModel
			appBuilderWorkflowTaskLinkCacheModel =
				new AppBuilderWorkflowTaskLinkCacheModel();

		appBuilderWorkflowTaskLinkCacheModel.mvccVersion = getMvccVersion();

		appBuilderWorkflowTaskLinkCacheModel.appBuilderWorkflowTaskLinkId =
			getAppBuilderWorkflowTaskLinkId();

		appBuilderWorkflowTaskLinkCacheModel.companyId = getCompanyId();

		appBuilderWorkflowTaskLinkCacheModel.appBuilderAppId =
			getAppBuilderAppId();

		appBuilderWorkflowTaskLinkCacheModel.appBuilderAppVersionId =
			getAppBuilderAppVersionId();

		appBuilderWorkflowTaskLinkCacheModel.ddmStructureLayoutId =
			getDdmStructureLayoutId();

		appBuilderWorkflowTaskLinkCacheModel.readOnly = isReadOnly();

		appBuilderWorkflowTaskLinkCacheModel.workflowTaskName =
			getWorkflowTaskName();

		String workflowTaskName =
			appBuilderWorkflowTaskLinkCacheModel.workflowTaskName;

		if ((workflowTaskName != null) && (workflowTaskName.length() == 0)) {
			appBuilderWorkflowTaskLinkCacheModel.workflowTaskName = null;
		}

		return appBuilderWorkflowTaskLinkCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<AppBuilderWorkflowTaskLink, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			4 * attributeGetterFunctions.size() + 2);

		sb.append("{");

		for (Map.Entry<String, Function<AppBuilderWorkflowTaskLink, Object>>
				entry : attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<AppBuilderWorkflowTaskLink, Object>
				attributeGetterFunction = entry.getValue();

			sb.append(attributeName);
			sb.append("=");
			sb.append(
				attributeGetterFunction.apply(
					(AppBuilderWorkflowTaskLink)this));
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
		Map<String, Function<AppBuilderWorkflowTaskLink, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			5 * attributeGetterFunctions.size() + 4);

		sb.append("<model><model-name>");
		sb.append(getModelClassName());
		sb.append("</model-name>");

		for (Map.Entry<String, Function<AppBuilderWorkflowTaskLink, Object>>
				entry : attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<AppBuilderWorkflowTaskLink, Object>
				attributeGetterFunction = entry.getValue();

			sb.append("<column><column-name>");
			sb.append(attributeName);
			sb.append("</column-name><column-value><![CDATA[");
			sb.append(
				attributeGetterFunction.apply(
					(AppBuilderWorkflowTaskLink)this));
			sb.append("]]></column-value></column>");
		}

		sb.append("</model>");

		return sb.toString();
	}

	private static class EscapedModelProxyProviderFunctionHolder {

		private static final Function
			<InvocationHandler, AppBuilderWorkflowTaskLink>
				_escapedModelProxyProviderFunction =
					_getProxyProviderFunction();

	}

	private long _mvccVersion;
	private long _appBuilderWorkflowTaskLinkId;
	private long _companyId;
	private long _appBuilderAppId;
	private long _appBuilderAppVersionId;
	private long _ddmStructureLayoutId;
	private boolean _readOnly;
	private String _workflowTaskName;

	public <T> T getColumnValue(String columnName) {
		Function<AppBuilderWorkflowTaskLink, Object> function =
			_attributeGetterFunctions.get(columnName);

		if (function == null) {
			throw new IllegalArgumentException(
				"No attribute getter function found for " + columnName);
		}

		return (T)function.apply((AppBuilderWorkflowTaskLink)this);
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
			"appBuilderWorkflowTaskLinkId", _appBuilderWorkflowTaskLinkId);
		_columnOriginalValues.put("companyId", _companyId);
		_columnOriginalValues.put("appBuilderAppId", _appBuilderAppId);
		_columnOriginalValues.put(
			"appBuilderAppVersionId", _appBuilderAppVersionId);
		_columnOriginalValues.put(
			"ddmStructureLayoutId", _ddmStructureLayoutId);
		_columnOriginalValues.put("readOnly", _readOnly);
		_columnOriginalValues.put("workflowTaskName", _workflowTaskName);
	}

	private transient Map<String, Object> _columnOriginalValues;

	public static long getColumnBitmask(String columnName) {
		return _columnBitmasks.get(columnName);
	}

	private static final Map<String, Long> _columnBitmasks;

	static {
		Map<String, Long> columnBitmasks = new HashMap<>();

		columnBitmasks.put("mvccVersion", 1L);

		columnBitmasks.put("appBuilderWorkflowTaskLinkId", 2L);

		columnBitmasks.put("companyId", 4L);

		columnBitmasks.put("appBuilderAppId", 8L);

		columnBitmasks.put("appBuilderAppVersionId", 16L);

		columnBitmasks.put("ddmStructureLayoutId", 32L);

		columnBitmasks.put("readOnly", 64L);

		columnBitmasks.put("workflowTaskName", 128L);

		_columnBitmasks = Collections.unmodifiableMap(columnBitmasks);
	}

	private long _columnBitmask;
	private AppBuilderWorkflowTaskLink _escapedModel;

}