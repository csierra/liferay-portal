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

package com.liferay.sharepoint.rest.oauth2.model.impl;

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
import com.liferay.sharepoint.rest.oauth2.model.SharepointOAuth2TokenEntry;
import com.liferay.sharepoint.rest.oauth2.model.SharepointOAuth2TokenEntryModel;

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
 * The base model implementation for the SharepointOAuth2TokenEntry service. Represents a row in the &quot;SharepointOAuth2TokenEntry&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface <code>SharepointOAuth2TokenEntryModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link SharepointOAuth2TokenEntryImpl}.
 * </p>
 *
 * @author Adolfo Pérez
 * @see SharepointOAuth2TokenEntryImpl
 * @generated
 */
public class SharepointOAuth2TokenEntryModelImpl
	extends BaseModelImpl<SharepointOAuth2TokenEntry>
	implements SharepointOAuth2TokenEntryModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a sharepoint o auth2 token entry model instance should use the <code>SharepointOAuth2TokenEntry</code> interface instead.
	 */
	public static final String TABLE_NAME = "SharepointOAuth2TokenEntry";

	public static final Object[][] TABLE_COLUMNS = {
		{"sharepointOAuth2TokenEntryId", Types.BIGINT},
		{"companyId", Types.BIGINT}, {"userId", Types.BIGINT},
		{"userName", Types.VARCHAR}, {"createDate", Types.TIMESTAMP},
		{"accessToken", Types.CLOB}, {"configurationPid", Types.VARCHAR},
		{"expirationDate", Types.TIMESTAMP}, {"refreshToken", Types.CLOB}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("sharepointOAuth2TokenEntryId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("accessToken", Types.CLOB);
		TABLE_COLUMNS_MAP.put("configurationPid", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("expirationDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("refreshToken", Types.CLOB);
	}

	public static final String TABLE_SQL_CREATE =
		"create table SharepointOAuth2TokenEntry (sharepointOAuth2TokenEntryId LONG not null primary key,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,accessToken TEXT null,configurationPid VARCHAR(75) null,expirationDate DATE null,refreshToken TEXT null)";

	public static final String TABLE_SQL_DROP =
		"drop table SharepointOAuth2TokenEntry";

	public static final String ORDER_BY_JPQL =
		" ORDER BY sharepointOAuth2TokenEntry.sharepointOAuth2TokenEntryId ASC";

	public static final String ORDER_BY_SQL =
		" ORDER BY SharepointOAuth2TokenEntry.sharepointOAuth2TokenEntryId ASC";

	public static final String DATA_SOURCE = "liferayDataSource";

	public static final String SESSION_FACTORY = "liferaySessionFactory";

	public static final String TX_MANAGER = "liferayTransactionManager";

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)
	 */
	@Deprecated
	public static final long CONFIGURATIONPID_COLUMN_BITMASK = 1L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)
	 */
	@Deprecated
	public static final long USERID_COLUMN_BITMASK = 2L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *		#getColumnBitmask(String)
	 */
	@Deprecated
	public static final long SHAREPOINTOAUTH2TOKENENTRYID_COLUMN_BITMASK = 4L;

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

	public SharepointOAuth2TokenEntryModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _sharepointOAuth2TokenEntryId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setSharepointOAuth2TokenEntryId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _sharepointOAuth2TokenEntryId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return SharepointOAuth2TokenEntry.class;
	}

	@Override
	public String getModelClassName() {
		return SharepointOAuth2TokenEntry.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<SharepointOAuth2TokenEntry, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		for (Map.Entry<String, Function<SharepointOAuth2TokenEntry, Object>>
				entry : attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<SharepointOAuth2TokenEntry, Object>
				attributeGetterFunction = entry.getValue();

			attributes.put(
				attributeName,
				attributeGetterFunction.apply(
					(SharepointOAuth2TokenEntry)this));
		}

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<SharepointOAuth2TokenEntry, Object>>
			attributeSetterBiConsumers = getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<SharepointOAuth2TokenEntry, Object>
				attributeSetterBiConsumer = attributeSetterBiConsumers.get(
					attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(SharepointOAuth2TokenEntry)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<SharepointOAuth2TokenEntry, Object>>
		getAttributeGetterFunctions() {

		return _attributeGetterFunctions;
	}

	public Map<String, BiConsumer<SharepointOAuth2TokenEntry, Object>>
		getAttributeSetterBiConsumers() {

		return _attributeSetterBiConsumers;
	}

	private static Function<InvocationHandler, SharepointOAuth2TokenEntry>
		_getProxyProviderFunction() {

		Class<?> proxyClass = ProxyUtil.getProxyClass(
			SharepointOAuth2TokenEntry.class.getClassLoader(),
			SharepointOAuth2TokenEntry.class, ModelWrapper.class);

		try {
			Constructor<SharepointOAuth2TokenEntry> constructor =
				(Constructor<SharepointOAuth2TokenEntry>)
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
		<String, Function<SharepointOAuth2TokenEntry, Object>>
			_attributeGetterFunctions;
	private static final Map
		<String, BiConsumer<SharepointOAuth2TokenEntry, Object>>
			_attributeSetterBiConsumers;

	static {
		Map<String, Function<SharepointOAuth2TokenEntry, Object>>
			attributeGetterFunctions =
				new LinkedHashMap
					<String, Function<SharepointOAuth2TokenEntry, Object>>();
		Map<String, BiConsumer<SharepointOAuth2TokenEntry, ?>>
			attributeSetterBiConsumers =
				new LinkedHashMap
					<String, BiConsumer<SharepointOAuth2TokenEntry, ?>>();

		attributeGetterFunctions.put(
			"sharepointOAuth2TokenEntryId",
			SharepointOAuth2TokenEntry::getSharepointOAuth2TokenEntryId);
		attributeSetterBiConsumers.put(
			"sharepointOAuth2TokenEntryId",
			(BiConsumer<SharepointOAuth2TokenEntry, Long>)
				SharepointOAuth2TokenEntry::setSharepointOAuth2TokenEntryId);
		attributeGetterFunctions.put(
			"companyId", SharepointOAuth2TokenEntry::getCompanyId);
		attributeSetterBiConsumers.put(
			"companyId",
			(BiConsumer<SharepointOAuth2TokenEntry, Long>)
				SharepointOAuth2TokenEntry::setCompanyId);
		attributeGetterFunctions.put(
			"userId", SharepointOAuth2TokenEntry::getUserId);
		attributeSetterBiConsumers.put(
			"userId",
			(BiConsumer<SharepointOAuth2TokenEntry, Long>)
				SharepointOAuth2TokenEntry::setUserId);
		attributeGetterFunctions.put(
			"userName", SharepointOAuth2TokenEntry::getUserName);
		attributeSetterBiConsumers.put(
			"userName",
			(BiConsumer<SharepointOAuth2TokenEntry, String>)
				SharepointOAuth2TokenEntry::setUserName);
		attributeGetterFunctions.put(
			"createDate", SharepointOAuth2TokenEntry::getCreateDate);
		attributeSetterBiConsumers.put(
			"createDate",
			(BiConsumer<SharepointOAuth2TokenEntry, Date>)
				SharepointOAuth2TokenEntry::setCreateDate);
		attributeGetterFunctions.put(
			"accessToken", SharepointOAuth2TokenEntry::getAccessToken);
		attributeSetterBiConsumers.put(
			"accessToken",
			(BiConsumer<SharepointOAuth2TokenEntry, String>)
				SharepointOAuth2TokenEntry::setAccessToken);
		attributeGetterFunctions.put(
			"configurationPid",
			SharepointOAuth2TokenEntry::getConfigurationPid);
		attributeSetterBiConsumers.put(
			"configurationPid",
			(BiConsumer<SharepointOAuth2TokenEntry, String>)
				SharepointOAuth2TokenEntry::setConfigurationPid);
		attributeGetterFunctions.put(
			"expirationDate", SharepointOAuth2TokenEntry::getExpirationDate);
		attributeSetterBiConsumers.put(
			"expirationDate",
			(BiConsumer<SharepointOAuth2TokenEntry, Date>)
				SharepointOAuth2TokenEntry::setExpirationDate);
		attributeGetterFunctions.put(
			"refreshToken", SharepointOAuth2TokenEntry::getRefreshToken);
		attributeSetterBiConsumers.put(
			"refreshToken",
			(BiConsumer<SharepointOAuth2TokenEntry, String>)
				SharepointOAuth2TokenEntry::setRefreshToken);

		_attributeGetterFunctions = Collections.unmodifiableMap(
			attributeGetterFunctions);
		_attributeSetterBiConsumers = Collections.unmodifiableMap(
			(Map)attributeSetterBiConsumers);
	}

	@Override
	public long getSharepointOAuth2TokenEntryId() {
		return _sharepointOAuth2TokenEntryId;
	}

	@Override
	public void setSharepointOAuth2TokenEntryId(
		long sharepointOAuth2TokenEntryId) {

		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_sharepointOAuth2TokenEntryId = sharepointOAuth2TokenEntryId;
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
	public String getAccessToken() {
		if (_accessToken == null) {
			return "";
		}
		else {
			return _accessToken;
		}
	}

	@Override
	public void setAccessToken(String accessToken) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_accessToken = accessToken;
	}

	@Override
	public String getConfigurationPid() {
		if (_configurationPid == null) {
			return "";
		}
		else {
			return _configurationPid;
		}
	}

	@Override
	public void setConfigurationPid(String configurationPid) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_configurationPid = configurationPid;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public String getOriginalConfigurationPid() {
		return getColumnOriginalValue("configurationPid");
	}

	@Override
	public Date getExpirationDate() {
		return _expirationDate;
	}

	@Override
	public void setExpirationDate(Date expirationDate) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_expirationDate = expirationDate;
	}

	@Override
	public String getRefreshToken() {
		if (_refreshToken == null) {
			return "";
		}
		else {
			return _refreshToken;
		}
	}

	@Override
	public void setRefreshToken(String refreshToken) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_refreshToken = refreshToken;
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
			getCompanyId(), SharepointOAuth2TokenEntry.class.getName(),
			getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public SharepointOAuth2TokenEntry toEscapedModel() {
		if (_escapedModel == null) {
			Function<InvocationHandler, SharepointOAuth2TokenEntry>
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
		SharepointOAuth2TokenEntryImpl sharepointOAuth2TokenEntryImpl =
			new SharepointOAuth2TokenEntryImpl();

		sharepointOAuth2TokenEntryImpl.setSharepointOAuth2TokenEntryId(
			getSharepointOAuth2TokenEntryId());
		sharepointOAuth2TokenEntryImpl.setCompanyId(getCompanyId());
		sharepointOAuth2TokenEntryImpl.setUserId(getUserId());
		sharepointOAuth2TokenEntryImpl.setUserName(getUserName());
		sharepointOAuth2TokenEntryImpl.setCreateDate(getCreateDate());
		sharepointOAuth2TokenEntryImpl.setAccessToken(getAccessToken());
		sharepointOAuth2TokenEntryImpl.setConfigurationPid(
			getConfigurationPid());
		sharepointOAuth2TokenEntryImpl.setExpirationDate(getExpirationDate());
		sharepointOAuth2TokenEntryImpl.setRefreshToken(getRefreshToken());

		sharepointOAuth2TokenEntryImpl.resetOriginalValues();

		return sharepointOAuth2TokenEntryImpl;
	}

	@Override
	public int compareTo(
		SharepointOAuth2TokenEntry sharepointOAuth2TokenEntry) {

		long primaryKey = sharepointOAuth2TokenEntry.getPrimaryKey();

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

		if (!(object instanceof SharepointOAuth2TokenEntry)) {
			return false;
		}

		SharepointOAuth2TokenEntry sharepointOAuth2TokenEntry =
			(SharepointOAuth2TokenEntry)object;

		long primaryKey = sharepointOAuth2TokenEntry.getPrimaryKey();

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
	public CacheModel<SharepointOAuth2TokenEntry> toCacheModel() {
		SharepointOAuth2TokenEntryCacheModel
			sharepointOAuth2TokenEntryCacheModel =
				new SharepointOAuth2TokenEntryCacheModel();

		sharepointOAuth2TokenEntryCacheModel.sharepointOAuth2TokenEntryId =
			getSharepointOAuth2TokenEntryId();

		sharepointOAuth2TokenEntryCacheModel.companyId = getCompanyId();

		sharepointOAuth2TokenEntryCacheModel.userId = getUserId();

		sharepointOAuth2TokenEntryCacheModel.userName = getUserName();

		String userName = sharepointOAuth2TokenEntryCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			sharepointOAuth2TokenEntryCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			sharepointOAuth2TokenEntryCacheModel.createDate =
				createDate.getTime();
		}
		else {
			sharepointOAuth2TokenEntryCacheModel.createDate = Long.MIN_VALUE;
		}

		sharepointOAuth2TokenEntryCacheModel.accessToken = getAccessToken();

		String accessToken = sharepointOAuth2TokenEntryCacheModel.accessToken;

		if ((accessToken != null) && (accessToken.length() == 0)) {
			sharepointOAuth2TokenEntryCacheModel.accessToken = null;
		}

		sharepointOAuth2TokenEntryCacheModel.configurationPid =
			getConfigurationPid();

		String configurationPid =
			sharepointOAuth2TokenEntryCacheModel.configurationPid;

		if ((configurationPid != null) && (configurationPid.length() == 0)) {
			sharepointOAuth2TokenEntryCacheModel.configurationPid = null;
		}

		Date expirationDate = getExpirationDate();

		if (expirationDate != null) {
			sharepointOAuth2TokenEntryCacheModel.expirationDate =
				expirationDate.getTime();
		}
		else {
			sharepointOAuth2TokenEntryCacheModel.expirationDate =
				Long.MIN_VALUE;
		}

		sharepointOAuth2TokenEntryCacheModel.refreshToken = getRefreshToken();

		String refreshToken = sharepointOAuth2TokenEntryCacheModel.refreshToken;

		if ((refreshToken != null) && (refreshToken.length() == 0)) {
			sharepointOAuth2TokenEntryCacheModel.refreshToken = null;
		}

		return sharepointOAuth2TokenEntryCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<SharepointOAuth2TokenEntry, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			4 * attributeGetterFunctions.size() + 2);

		sb.append("{");

		for (Map.Entry<String, Function<SharepointOAuth2TokenEntry, Object>>
				entry : attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<SharepointOAuth2TokenEntry, Object>
				attributeGetterFunction = entry.getValue();

			sb.append(attributeName);
			sb.append("=");
			sb.append(
				attributeGetterFunction.apply(
					(SharepointOAuth2TokenEntry)this));
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
		Map<String, Function<SharepointOAuth2TokenEntry, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			5 * attributeGetterFunctions.size() + 4);

		sb.append("<model><model-name>");
		sb.append(getModelClassName());
		sb.append("</model-name>");

		for (Map.Entry<String, Function<SharepointOAuth2TokenEntry, Object>>
				entry : attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<SharepointOAuth2TokenEntry, Object>
				attributeGetterFunction = entry.getValue();

			sb.append("<column><column-name>");
			sb.append(attributeName);
			sb.append("</column-name><column-value><![CDATA[");
			sb.append(
				attributeGetterFunction.apply(
					(SharepointOAuth2TokenEntry)this));
			sb.append("]]></column-value></column>");
		}

		sb.append("</model>");

		return sb.toString();
	}

	private static class EscapedModelProxyProviderFunctionHolder {

		private static final Function
			<InvocationHandler, SharepointOAuth2TokenEntry>
				_escapedModelProxyProviderFunction =
					_getProxyProviderFunction();

	}

	private long _sharepointOAuth2TokenEntryId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private String _accessToken;
	private String _configurationPid;
	private Date _expirationDate;
	private String _refreshToken;

	public <T> T getColumnValue(String columnName) {
		Function<SharepointOAuth2TokenEntry, Object> function =
			_attributeGetterFunctions.get(columnName);

		if (function == null) {
			throw new IllegalArgumentException(
				"No attribute getter function found for " + columnName);
		}

		return (T)function.apply((SharepointOAuth2TokenEntry)this);
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
			"sharepointOAuth2TokenEntryId", _sharepointOAuth2TokenEntryId);
		_columnOriginalValues.put("companyId", _companyId);
		_columnOriginalValues.put("userId", _userId);
		_columnOriginalValues.put("userName", _userName);
		_columnOriginalValues.put("createDate", _createDate);
		_columnOriginalValues.put("accessToken", _accessToken);
		_columnOriginalValues.put("configurationPid", _configurationPid);
		_columnOriginalValues.put("expirationDate", _expirationDate);
		_columnOriginalValues.put("refreshToken", _refreshToken);
	}

	private transient Map<String, Object> _columnOriginalValues;

	public static long getColumnBitmask(String columnName) {
		return _columnBitmasks.get(columnName);
	}

	private static final Map<String, Long> _columnBitmasks;

	static {
		Map<String, Long> columnBitmasks = new HashMap<>();

		columnBitmasks.put("sharepointOAuth2TokenEntryId", 1L);

		columnBitmasks.put("companyId", 2L);

		columnBitmasks.put("userId", 4L);

		columnBitmasks.put("userName", 8L);

		columnBitmasks.put("createDate", 16L);

		columnBitmasks.put("accessToken", 32L);

		columnBitmasks.put("configurationPid", 64L);

		columnBitmasks.put("expirationDate", 128L);

		columnBitmasks.put("refreshToken", 256L);

		_columnBitmasks = Collections.unmodifiableMap(columnBitmasks);
	}

	private long _columnBitmask;
	private SharepointOAuth2TokenEntry _escapedModel;

}