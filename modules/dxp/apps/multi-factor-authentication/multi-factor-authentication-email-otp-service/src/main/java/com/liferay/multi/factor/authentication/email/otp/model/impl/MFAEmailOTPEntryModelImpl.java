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

package com.liferay.multi.factor.authentication.email.otp.model.impl;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;
import com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTPEntry;
import com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTPEntryModel;
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
 * The base model implementation for the MFAEmailOTPEntry service. Represents a row in the &quot;MFAEmailOTPEntry&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface <code>MFAEmailOTPEntryModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link MFAEmailOTPEntryImpl}.
 * </p>
 *
 * @author Arthur Chan
 * @see MFAEmailOTPEntryImpl
 * @generated
 */
public class MFAEmailOTPEntryModelImpl
	extends BaseModelImpl<MFAEmailOTPEntry> implements MFAEmailOTPEntryModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a mfa email otp entry model instance should use the <code>MFAEmailOTPEntry</code> interface instead.
	 */
	public static final String TABLE_NAME = "MFAEmailOTPEntry";

	public static final Object[][] TABLE_COLUMNS = {
		{"mvccVersion", Types.BIGINT}, {"mfaEmailOTPEntryId", Types.BIGINT},
		{"companyId", Types.BIGINT}, {"userId", Types.BIGINT},
		{"userName", Types.VARCHAR}, {"createDate", Types.TIMESTAMP},
		{"modifiedDate", Types.TIMESTAMP}, {"failedAttempts", Types.INTEGER},
		{"lastFailDate", Types.TIMESTAMP}, {"lastFailIP", Types.VARCHAR},
		{"lastSuccessDate", Types.TIMESTAMP}, {"lastSuccessIP", Types.VARCHAR}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("mvccVersion", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("mfaEmailOTPEntryId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("modifiedDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("failedAttempts", Types.INTEGER);
		TABLE_COLUMNS_MAP.put("lastFailDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("lastFailIP", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("lastSuccessDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("lastSuccessIP", Types.VARCHAR);
	}

	public static final String TABLE_SQL_CREATE =
		"create table MFAEmailOTPEntry (mvccVersion LONG default 0 not null,mfaEmailOTPEntryId LONG not null primary key,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,failedAttempts INTEGER,lastFailDate DATE null,lastFailIP VARCHAR(75) null,lastSuccessDate DATE null,lastSuccessIP VARCHAR(75) null)";

	public static final String TABLE_SQL_DROP = "drop table MFAEmailOTPEntry";

	public static final String ORDER_BY_JPQL =
		" ORDER BY mfaEmailOTPEntry.mfaEmailOTPEntryId ASC";

	public static final String ORDER_BY_SQL =
		" ORDER BY MFAEmailOTPEntry.mfaEmailOTPEntryId ASC";

	public static final String DATA_SOURCE = "liferayDataSource";

	public static final String SESSION_FACTORY = "liferaySessionFactory";

	public static final String TX_MANAGER = "liferayTransactionManager";

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)
	 */
	@Deprecated
	public static final long USERID_COLUMN_BITMASK = 1L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *		#getColumnBitmask(String)
	 */
	@Deprecated
	public static final long MFAEMAILOTPENTRYID_COLUMN_BITMASK = 2L;

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

	public MFAEmailOTPEntryModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _mfaEmailOTPEntryId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setMfaEmailOTPEntryId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _mfaEmailOTPEntryId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return MFAEmailOTPEntry.class;
	}

	@Override
	public String getModelClassName() {
		return MFAEmailOTPEntry.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<MFAEmailOTPEntry, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		for (Map.Entry<String, Function<MFAEmailOTPEntry, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<MFAEmailOTPEntry, Object> attributeGetterFunction =
				entry.getValue();

			attributes.put(
				attributeName,
				attributeGetterFunction.apply((MFAEmailOTPEntry)this));
		}

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<MFAEmailOTPEntry, Object>>
			attributeSetterBiConsumers = getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<MFAEmailOTPEntry, Object> attributeSetterBiConsumer =
				attributeSetterBiConsumers.get(attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(MFAEmailOTPEntry)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<MFAEmailOTPEntry, Object>>
		getAttributeGetterFunctions() {

		return _attributeGetterFunctions;
	}

	public Map<String, BiConsumer<MFAEmailOTPEntry, Object>>
		getAttributeSetterBiConsumers() {

		return _attributeSetterBiConsumers;
	}

	private static Function<InvocationHandler, MFAEmailOTPEntry>
		_getProxyProviderFunction() {

		Class<?> proxyClass = ProxyUtil.getProxyClass(
			MFAEmailOTPEntry.class.getClassLoader(), MFAEmailOTPEntry.class,
			ModelWrapper.class);

		try {
			Constructor<MFAEmailOTPEntry> constructor =
				(Constructor<MFAEmailOTPEntry>)proxyClass.getConstructor(
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

	private static final Map<String, Function<MFAEmailOTPEntry, Object>>
		_attributeGetterFunctions;
	private static final Map<String, BiConsumer<MFAEmailOTPEntry, Object>>
		_attributeSetterBiConsumers;

	static {
		Map<String, Function<MFAEmailOTPEntry, Object>>
			attributeGetterFunctions =
				new LinkedHashMap<String, Function<MFAEmailOTPEntry, Object>>();
		Map<String, BiConsumer<MFAEmailOTPEntry, ?>>
			attributeSetterBiConsumers =
				new LinkedHashMap<String, BiConsumer<MFAEmailOTPEntry, ?>>();

		attributeGetterFunctions.put(
			"mvccVersion", MFAEmailOTPEntry::getMvccVersion);
		attributeSetterBiConsumers.put(
			"mvccVersion",
			(BiConsumer<MFAEmailOTPEntry, Long>)
				MFAEmailOTPEntry::setMvccVersion);
		attributeGetterFunctions.put(
			"mfaEmailOTPEntryId", MFAEmailOTPEntry::getMfaEmailOTPEntryId);
		attributeSetterBiConsumers.put(
			"mfaEmailOTPEntryId",
			(BiConsumer<MFAEmailOTPEntry, Long>)
				MFAEmailOTPEntry::setMfaEmailOTPEntryId);
		attributeGetterFunctions.put(
			"companyId", MFAEmailOTPEntry::getCompanyId);
		attributeSetterBiConsumers.put(
			"companyId",
			(BiConsumer<MFAEmailOTPEntry, Long>)MFAEmailOTPEntry::setCompanyId);
		attributeGetterFunctions.put("userId", MFAEmailOTPEntry::getUserId);
		attributeSetterBiConsumers.put(
			"userId",
			(BiConsumer<MFAEmailOTPEntry, Long>)MFAEmailOTPEntry::setUserId);
		attributeGetterFunctions.put("userName", MFAEmailOTPEntry::getUserName);
		attributeSetterBiConsumers.put(
			"userName",
			(BiConsumer<MFAEmailOTPEntry, String>)
				MFAEmailOTPEntry::setUserName);
		attributeGetterFunctions.put(
			"createDate", MFAEmailOTPEntry::getCreateDate);
		attributeSetterBiConsumers.put(
			"createDate",
			(BiConsumer<MFAEmailOTPEntry, Date>)
				MFAEmailOTPEntry::setCreateDate);
		attributeGetterFunctions.put(
			"modifiedDate", MFAEmailOTPEntry::getModifiedDate);
		attributeSetterBiConsumers.put(
			"modifiedDate",
			(BiConsumer<MFAEmailOTPEntry, Date>)
				MFAEmailOTPEntry::setModifiedDate);
		attributeGetterFunctions.put(
			"failedAttempts", MFAEmailOTPEntry::getFailedAttempts);
		attributeSetterBiConsumers.put(
			"failedAttempts",
			(BiConsumer<MFAEmailOTPEntry, Integer>)
				MFAEmailOTPEntry::setFailedAttempts);
		attributeGetterFunctions.put(
			"lastFailDate", MFAEmailOTPEntry::getLastFailDate);
		attributeSetterBiConsumers.put(
			"lastFailDate",
			(BiConsumer<MFAEmailOTPEntry, Date>)
				MFAEmailOTPEntry::setLastFailDate);
		attributeGetterFunctions.put(
			"lastFailIP", MFAEmailOTPEntry::getLastFailIP);
		attributeSetterBiConsumers.put(
			"lastFailIP",
			(BiConsumer<MFAEmailOTPEntry, String>)
				MFAEmailOTPEntry::setLastFailIP);
		attributeGetterFunctions.put(
			"lastSuccessDate", MFAEmailOTPEntry::getLastSuccessDate);
		attributeSetterBiConsumers.put(
			"lastSuccessDate",
			(BiConsumer<MFAEmailOTPEntry, Date>)
				MFAEmailOTPEntry::setLastSuccessDate);
		attributeGetterFunctions.put(
			"lastSuccessIP", MFAEmailOTPEntry::getLastSuccessIP);
		attributeSetterBiConsumers.put(
			"lastSuccessIP",
			(BiConsumer<MFAEmailOTPEntry, String>)
				MFAEmailOTPEntry::setLastSuccessIP);

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
	public long getMfaEmailOTPEntryId() {
		return _mfaEmailOTPEntryId;
	}

	@Override
	public void setMfaEmailOTPEntryId(long mfaEmailOTPEntryId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_mfaEmailOTPEntryId = mfaEmailOTPEntryId;
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
	public int getFailedAttempts() {
		return _failedAttempts;
	}

	@Override
	public void setFailedAttempts(int failedAttempts) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_failedAttempts = failedAttempts;
	}

	@Override
	public Date getLastFailDate() {
		return _lastFailDate;
	}

	@Override
	public void setLastFailDate(Date lastFailDate) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_lastFailDate = lastFailDate;
	}

	@Override
	public String getLastFailIP() {
		if (_lastFailIP == null) {
			return "";
		}
		else {
			return _lastFailIP;
		}
	}

	@Override
	public void setLastFailIP(String lastFailIP) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_lastFailIP = lastFailIP;
	}

	@Override
	public Date getLastSuccessDate() {
		return _lastSuccessDate;
	}

	@Override
	public void setLastSuccessDate(Date lastSuccessDate) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_lastSuccessDate = lastSuccessDate;
	}

	@Override
	public String getLastSuccessIP() {
		if (_lastSuccessIP == null) {
			return "";
		}
		else {
			return _lastSuccessIP;
		}
	}

	@Override
	public void setLastSuccessIP(String lastSuccessIP) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_lastSuccessIP = lastSuccessIP;
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
			getCompanyId(), MFAEmailOTPEntry.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public MFAEmailOTPEntry toEscapedModel() {
		if (_escapedModel == null) {
			Function<InvocationHandler, MFAEmailOTPEntry>
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
		MFAEmailOTPEntryImpl mfaEmailOTPEntryImpl = new MFAEmailOTPEntryImpl();

		mfaEmailOTPEntryImpl.setMvccVersion(getMvccVersion());
		mfaEmailOTPEntryImpl.setMfaEmailOTPEntryId(getMfaEmailOTPEntryId());
		mfaEmailOTPEntryImpl.setCompanyId(getCompanyId());
		mfaEmailOTPEntryImpl.setUserId(getUserId());
		mfaEmailOTPEntryImpl.setUserName(getUserName());
		mfaEmailOTPEntryImpl.setCreateDate(getCreateDate());
		mfaEmailOTPEntryImpl.setModifiedDate(getModifiedDate());
		mfaEmailOTPEntryImpl.setFailedAttempts(getFailedAttempts());
		mfaEmailOTPEntryImpl.setLastFailDate(getLastFailDate());
		mfaEmailOTPEntryImpl.setLastFailIP(getLastFailIP());
		mfaEmailOTPEntryImpl.setLastSuccessDate(getLastSuccessDate());
		mfaEmailOTPEntryImpl.setLastSuccessIP(getLastSuccessIP());

		mfaEmailOTPEntryImpl.resetOriginalValues();

		return mfaEmailOTPEntryImpl;
	}

	@Override
	public int compareTo(MFAEmailOTPEntry mfaEmailOTPEntry) {
		long primaryKey = mfaEmailOTPEntry.getPrimaryKey();

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

		if (!(object instanceof MFAEmailOTPEntry)) {
			return false;
		}

		MFAEmailOTPEntry mfaEmailOTPEntry = (MFAEmailOTPEntry)object;

		long primaryKey = mfaEmailOTPEntry.getPrimaryKey();

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
	public CacheModel<MFAEmailOTPEntry> toCacheModel() {
		MFAEmailOTPEntryCacheModel mfaEmailOTPEntryCacheModel =
			new MFAEmailOTPEntryCacheModel();

		mfaEmailOTPEntryCacheModel.mvccVersion = getMvccVersion();

		mfaEmailOTPEntryCacheModel.mfaEmailOTPEntryId = getMfaEmailOTPEntryId();

		mfaEmailOTPEntryCacheModel.companyId = getCompanyId();

		mfaEmailOTPEntryCacheModel.userId = getUserId();

		mfaEmailOTPEntryCacheModel.userName = getUserName();

		String userName = mfaEmailOTPEntryCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			mfaEmailOTPEntryCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			mfaEmailOTPEntryCacheModel.createDate = createDate.getTime();
		}
		else {
			mfaEmailOTPEntryCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			mfaEmailOTPEntryCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			mfaEmailOTPEntryCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		mfaEmailOTPEntryCacheModel.failedAttempts = getFailedAttempts();

		Date lastFailDate = getLastFailDate();

		if (lastFailDate != null) {
			mfaEmailOTPEntryCacheModel.lastFailDate = lastFailDate.getTime();
		}
		else {
			mfaEmailOTPEntryCacheModel.lastFailDate = Long.MIN_VALUE;
		}

		mfaEmailOTPEntryCacheModel.lastFailIP = getLastFailIP();

		String lastFailIP = mfaEmailOTPEntryCacheModel.lastFailIP;

		if ((lastFailIP != null) && (lastFailIP.length() == 0)) {
			mfaEmailOTPEntryCacheModel.lastFailIP = null;
		}

		Date lastSuccessDate = getLastSuccessDate();

		if (lastSuccessDate != null) {
			mfaEmailOTPEntryCacheModel.lastSuccessDate =
				lastSuccessDate.getTime();
		}
		else {
			mfaEmailOTPEntryCacheModel.lastSuccessDate = Long.MIN_VALUE;
		}

		mfaEmailOTPEntryCacheModel.lastSuccessIP = getLastSuccessIP();

		String lastSuccessIP = mfaEmailOTPEntryCacheModel.lastSuccessIP;

		if ((lastSuccessIP != null) && (lastSuccessIP.length() == 0)) {
			mfaEmailOTPEntryCacheModel.lastSuccessIP = null;
		}

		return mfaEmailOTPEntryCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<MFAEmailOTPEntry, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			4 * attributeGetterFunctions.size() + 2);

		sb.append("{");

		for (Map.Entry<String, Function<MFAEmailOTPEntry, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<MFAEmailOTPEntry, Object> attributeGetterFunction =
				entry.getValue();

			sb.append(attributeName);
			sb.append("=");
			sb.append(attributeGetterFunction.apply((MFAEmailOTPEntry)this));
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
		Map<String, Function<MFAEmailOTPEntry, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			5 * attributeGetterFunctions.size() + 4);

		sb.append("<model><model-name>");
		sb.append(getModelClassName());
		sb.append("</model-name>");

		for (Map.Entry<String, Function<MFAEmailOTPEntry, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<MFAEmailOTPEntry, Object> attributeGetterFunction =
				entry.getValue();

			sb.append("<column><column-name>");
			sb.append(attributeName);
			sb.append("</column-name><column-value><![CDATA[");
			sb.append(attributeGetterFunction.apply((MFAEmailOTPEntry)this));
			sb.append("]]></column-value></column>");
		}

		sb.append("</model>");

		return sb.toString();
	}

	private static class EscapedModelProxyProviderFunctionHolder {

		private static final Function<InvocationHandler, MFAEmailOTPEntry>
			_escapedModelProxyProviderFunction = _getProxyProviderFunction();

	}

	private long _mvccVersion;
	private long _mfaEmailOTPEntryId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private boolean _setModifiedDate;
	private int _failedAttempts;
	private Date _lastFailDate;
	private String _lastFailIP;
	private Date _lastSuccessDate;
	private String _lastSuccessIP;

	public <T> T getColumnValue(String columnName) {
		Function<MFAEmailOTPEntry, Object> function =
			_attributeGetterFunctions.get(columnName);

		if (function == null) {
			throw new IllegalArgumentException(
				"No attribute getter function found for " + columnName);
		}

		return (T)function.apply((MFAEmailOTPEntry)this);
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
		_columnOriginalValues.put("mfaEmailOTPEntryId", _mfaEmailOTPEntryId);
		_columnOriginalValues.put("companyId", _companyId);
		_columnOriginalValues.put("userId", _userId);
		_columnOriginalValues.put("userName", _userName);
		_columnOriginalValues.put("createDate", _createDate);
		_columnOriginalValues.put("modifiedDate", _modifiedDate);
		_columnOriginalValues.put("failedAttempts", _failedAttempts);
		_columnOriginalValues.put("lastFailDate", _lastFailDate);
		_columnOriginalValues.put("lastFailIP", _lastFailIP);
		_columnOriginalValues.put("lastSuccessDate", _lastSuccessDate);
		_columnOriginalValues.put("lastSuccessIP", _lastSuccessIP);
	}

	private transient Map<String, Object> _columnOriginalValues;

	public static long getColumnBitmask(String columnName) {
		return _columnBitmasks.get(columnName);
	}

	private static final Map<String, Long> _columnBitmasks;

	static {
		Map<String, Long> columnBitmasks = new HashMap<>();

		columnBitmasks.put("mvccVersion", 1L);

		columnBitmasks.put("mfaEmailOTPEntryId", 2L);

		columnBitmasks.put("companyId", 4L);

		columnBitmasks.put("userId", 8L);

		columnBitmasks.put("userName", 16L);

		columnBitmasks.put("createDate", 32L);

		columnBitmasks.put("modifiedDate", 64L);

		columnBitmasks.put("failedAttempts", 128L);

		columnBitmasks.put("lastFailDate", 256L);

		columnBitmasks.put("lastFailIP", 512L);

		columnBitmasks.put("lastSuccessDate", 1024L);

		columnBitmasks.put("lastSuccessIP", 2048L);

		_columnBitmasks = Collections.unmodifiableMap(columnBitmasks);
	}

	private long _columnBitmask;
	private MFAEmailOTPEntry _escapedModel;

}