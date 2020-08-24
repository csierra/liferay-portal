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

package com.liferay.saml.persistence.model.impl;

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
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.saml.persistence.model.SamlIdpSsoSession;
import com.liferay.saml.persistence.model.SamlIdpSsoSessionModel;

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
 * The base model implementation for the SamlIdpSsoSession service. Represents a row in the &quot;SamlIdpSsoSession&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface <code>SamlIdpSsoSessionModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link SamlIdpSsoSessionImpl}.
 * </p>
 *
 * @author Mika Koivisto
 * @see SamlIdpSsoSessionImpl
 * @generated
 */
public class SamlIdpSsoSessionModelImpl
	extends BaseModelImpl<SamlIdpSsoSession> implements SamlIdpSsoSessionModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a saml idp sso session model instance should use the <code>SamlIdpSsoSession</code> interface instead.
	 */
	public static final String TABLE_NAME = "SamlIdpSsoSession";

	public static final Object[][] TABLE_COLUMNS = {
		{"samlIdpSsoSessionId", Types.BIGINT}, {"companyId", Types.BIGINT},
		{"userId", Types.BIGINT}, {"userName", Types.VARCHAR},
		{"createDate", Types.TIMESTAMP}, {"modifiedDate", Types.TIMESTAMP},
		{"samlIdpSsoSessionKey", Types.VARCHAR}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("samlIdpSsoSessionId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("modifiedDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("samlIdpSsoSessionKey", Types.VARCHAR);
	}

	public static final String TABLE_SQL_CREATE =
		"create table SamlIdpSsoSession (samlIdpSsoSessionId LONG not null primary key,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,samlIdpSsoSessionKey VARCHAR(75) null)";

	public static final String TABLE_SQL_DROP = "drop table SamlIdpSsoSession";

	public static final String ORDER_BY_JPQL =
		" ORDER BY samlIdpSsoSession.samlIdpSsoSessionId ASC";

	public static final String ORDER_BY_SQL =
		" ORDER BY SamlIdpSsoSession.samlIdpSsoSessionId ASC";

	public static final String DATA_SOURCE = "liferayDataSource";

	public static final String SESSION_FACTORY = "liferaySessionFactory";

	public static final String TX_MANAGER = "liferayTransactionManager";

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)
	 */
	@Deprecated
	public static final long CREATEDATE_COLUMN_BITMASK = 1L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)
	 */
	@Deprecated
	public static final long SAMLIDPSSOSESSIONKEY_COLUMN_BITMASK = 2L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *		#getColumnBitmask(String)
	 */
	@Deprecated
	public static final long SAMLIDPSSOSESSIONID_COLUMN_BITMASK = 4L;

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

	public SamlIdpSsoSessionModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _samlIdpSsoSessionId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setSamlIdpSsoSessionId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _samlIdpSsoSessionId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return SamlIdpSsoSession.class;
	}

	@Override
	public String getModelClassName() {
		return SamlIdpSsoSession.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<SamlIdpSsoSession, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		for (Map.Entry<String, Function<SamlIdpSsoSession, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<SamlIdpSsoSession, Object> attributeGetterFunction =
				entry.getValue();

			attributes.put(
				attributeName,
				attributeGetterFunction.apply((SamlIdpSsoSession)this));
		}

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<SamlIdpSsoSession, Object>>
			attributeSetterBiConsumers = getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<SamlIdpSsoSession, Object> attributeSetterBiConsumer =
				attributeSetterBiConsumers.get(attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(SamlIdpSsoSession)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<SamlIdpSsoSession, Object>>
		getAttributeGetterFunctions() {

		return _attributeGetterFunctions;
	}

	public Map<String, BiConsumer<SamlIdpSsoSession, Object>>
		getAttributeSetterBiConsumers() {

		return _attributeSetterBiConsumers;
	}

	private static Function<InvocationHandler, SamlIdpSsoSession>
		_getProxyProviderFunction() {

		Class<?> proxyClass = ProxyUtil.getProxyClass(
			SamlIdpSsoSession.class.getClassLoader(), SamlIdpSsoSession.class,
			ModelWrapper.class);

		try {
			Constructor<SamlIdpSsoSession> constructor =
				(Constructor<SamlIdpSsoSession>)proxyClass.getConstructor(
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

	private static final Map<String, Function<SamlIdpSsoSession, Object>>
		_attributeGetterFunctions;
	private static final Map<String, BiConsumer<SamlIdpSsoSession, Object>>
		_attributeSetterBiConsumers;

	static {
		Map<String, Function<SamlIdpSsoSession, Object>>
			attributeGetterFunctions =
				new LinkedHashMap
					<String, Function<SamlIdpSsoSession, Object>>();
		Map<String, BiConsumer<SamlIdpSsoSession, ?>>
			attributeSetterBiConsumers =
				new LinkedHashMap<String, BiConsumer<SamlIdpSsoSession, ?>>();

		attributeGetterFunctions.put(
			"samlIdpSsoSessionId", SamlIdpSsoSession::getSamlIdpSsoSessionId);
		attributeSetterBiConsumers.put(
			"samlIdpSsoSessionId",
			(BiConsumer<SamlIdpSsoSession, Long>)
				SamlIdpSsoSession::setSamlIdpSsoSessionId);
		attributeGetterFunctions.put(
			"companyId", SamlIdpSsoSession::getCompanyId);
		attributeSetterBiConsumers.put(
			"companyId",
			(BiConsumer<SamlIdpSsoSession, Long>)
				SamlIdpSsoSession::setCompanyId);
		attributeGetterFunctions.put("userId", SamlIdpSsoSession::getUserId);
		attributeSetterBiConsumers.put(
			"userId",
			(BiConsumer<SamlIdpSsoSession, Long>)SamlIdpSsoSession::setUserId);
		attributeGetterFunctions.put(
			"userName", SamlIdpSsoSession::getUserName);
		attributeSetterBiConsumers.put(
			"userName",
			(BiConsumer<SamlIdpSsoSession, String>)
				SamlIdpSsoSession::setUserName);
		attributeGetterFunctions.put(
			"createDate", SamlIdpSsoSession::getCreateDate);
		attributeSetterBiConsumers.put(
			"createDate",
			(BiConsumer<SamlIdpSsoSession, Date>)
				SamlIdpSsoSession::setCreateDate);
		attributeGetterFunctions.put(
			"modifiedDate", SamlIdpSsoSession::getModifiedDate);
		attributeSetterBiConsumers.put(
			"modifiedDate",
			(BiConsumer<SamlIdpSsoSession, Date>)
				SamlIdpSsoSession::setModifiedDate);
		attributeGetterFunctions.put(
			"samlIdpSsoSessionKey", SamlIdpSsoSession::getSamlIdpSsoSessionKey);
		attributeSetterBiConsumers.put(
			"samlIdpSsoSessionKey",
			(BiConsumer<SamlIdpSsoSession, String>)
				SamlIdpSsoSession::setSamlIdpSsoSessionKey);

		_attributeGetterFunctions = Collections.unmodifiableMap(
			attributeGetterFunctions);
		_attributeSetterBiConsumers = Collections.unmodifiableMap(
			(Map)attributeSetterBiConsumers);
	}

	@Override
	public long getSamlIdpSsoSessionId() {
		return _samlIdpSsoSessionId;
	}

	@Override
	public void setSamlIdpSsoSessionId(long samlIdpSsoSessionId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_samlIdpSsoSessionId = samlIdpSsoSessionId;
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

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public Date getOriginalCreateDate() {
		return getColumnOriginalValue("createDate");
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
	public String getSamlIdpSsoSessionKey() {
		if (_samlIdpSsoSessionKey == null) {
			return "";
		}
		else {
			return _samlIdpSsoSessionKey;
		}
	}

	@Override
	public void setSamlIdpSsoSessionKey(String samlIdpSsoSessionKey) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_samlIdpSsoSessionKey = samlIdpSsoSessionKey;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public String getOriginalSamlIdpSsoSessionKey() {
		return getColumnOriginalValue("samlIdpSsoSessionKey");
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
			getCompanyId(), SamlIdpSsoSession.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public SamlIdpSsoSession toEscapedModel() {
		if (_escapedModel == null) {
			Function<InvocationHandler, SamlIdpSsoSession>
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
		SamlIdpSsoSessionImpl samlIdpSsoSessionImpl =
			new SamlIdpSsoSessionImpl();

		samlIdpSsoSessionImpl.setSamlIdpSsoSessionId(getSamlIdpSsoSessionId());
		samlIdpSsoSessionImpl.setCompanyId(getCompanyId());
		samlIdpSsoSessionImpl.setUserId(getUserId());
		samlIdpSsoSessionImpl.setUserName(getUserName());
		samlIdpSsoSessionImpl.setCreateDate(getCreateDate());
		samlIdpSsoSessionImpl.setModifiedDate(getModifiedDate());
		samlIdpSsoSessionImpl.setSamlIdpSsoSessionKey(
			getSamlIdpSsoSessionKey());

		samlIdpSsoSessionImpl.resetOriginalValues();

		return samlIdpSsoSessionImpl;
	}

	@Override
	public int compareTo(SamlIdpSsoSession samlIdpSsoSession) {
		long primaryKey = samlIdpSsoSession.getPrimaryKey();

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

		if (!(object instanceof SamlIdpSsoSession)) {
			return false;
		}

		SamlIdpSsoSession samlIdpSsoSession = (SamlIdpSsoSession)object;

		long primaryKey = samlIdpSsoSession.getPrimaryKey();

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
	public CacheModel<SamlIdpSsoSession> toCacheModel() {
		SamlIdpSsoSessionCacheModel samlIdpSsoSessionCacheModel =
			new SamlIdpSsoSessionCacheModel();

		samlIdpSsoSessionCacheModel.samlIdpSsoSessionId =
			getSamlIdpSsoSessionId();

		samlIdpSsoSessionCacheModel.companyId = getCompanyId();

		samlIdpSsoSessionCacheModel.userId = getUserId();

		samlIdpSsoSessionCacheModel.userName = getUserName();

		String userName = samlIdpSsoSessionCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			samlIdpSsoSessionCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			samlIdpSsoSessionCacheModel.createDate = createDate.getTime();
		}
		else {
			samlIdpSsoSessionCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			samlIdpSsoSessionCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			samlIdpSsoSessionCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		samlIdpSsoSessionCacheModel.samlIdpSsoSessionKey =
			getSamlIdpSsoSessionKey();

		String samlIdpSsoSessionKey =
			samlIdpSsoSessionCacheModel.samlIdpSsoSessionKey;

		if ((samlIdpSsoSessionKey != null) &&
			(samlIdpSsoSessionKey.length() == 0)) {

			samlIdpSsoSessionCacheModel.samlIdpSsoSessionKey = null;
		}

		return samlIdpSsoSessionCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<SamlIdpSsoSession, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			4 * attributeGetterFunctions.size() + 2);

		sb.append("{");

		for (Map.Entry<String, Function<SamlIdpSsoSession, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<SamlIdpSsoSession, Object> attributeGetterFunction =
				entry.getValue();

			sb.append(attributeName);
			sb.append("=");
			sb.append(attributeGetterFunction.apply((SamlIdpSsoSession)this));
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
		Map<String, Function<SamlIdpSsoSession, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			5 * attributeGetterFunctions.size() + 4);

		sb.append("<model><model-name>");
		sb.append(getModelClassName());
		sb.append("</model-name>");

		for (Map.Entry<String, Function<SamlIdpSsoSession, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<SamlIdpSsoSession, Object> attributeGetterFunction =
				entry.getValue();

			sb.append("<column><column-name>");
			sb.append(attributeName);
			sb.append("</column-name><column-value><![CDATA[");
			sb.append(attributeGetterFunction.apply((SamlIdpSsoSession)this));
			sb.append("]]></column-value></column>");
		}

		sb.append("</model>");

		return sb.toString();
	}

	private static class EscapedModelProxyProviderFunctionHolder {

		private static final Function<InvocationHandler, SamlIdpSsoSession>
			_escapedModelProxyProviderFunction = _getProxyProviderFunction();

	}

	private long _samlIdpSsoSessionId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private boolean _setModifiedDate;
	private String _samlIdpSsoSessionKey;

	public <T> T getColumnValue(String columnName) {
		Function<SamlIdpSsoSession, Object> function =
			_attributeGetterFunctions.get(columnName);

		if (function == null) {
			throw new IllegalArgumentException(
				"No attribute getter function found for " + columnName);
		}

		return (T)function.apply((SamlIdpSsoSession)this);
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

		_columnOriginalValues.put("samlIdpSsoSessionId", _samlIdpSsoSessionId);
		_columnOriginalValues.put("companyId", _companyId);
		_columnOriginalValues.put("userId", _userId);
		_columnOriginalValues.put("userName", _userName);
		_columnOriginalValues.put("createDate", _createDate);
		_columnOriginalValues.put("modifiedDate", _modifiedDate);
		_columnOriginalValues.put(
			"samlIdpSsoSessionKey", _samlIdpSsoSessionKey);
	}

	private transient Map<String, Object> _columnOriginalValues;

	public static long getColumnBitmask(String columnName) {
		return _columnBitmasks.get(columnName);
	}

	private static final Map<String, Long> _columnBitmasks;

	static {
		Map<String, Long> columnBitmasks = new HashMap<>();

		columnBitmasks.put("samlIdpSsoSessionId", 1L);

		columnBitmasks.put("companyId", 2L);

		columnBitmasks.put("userId", 4L);

		columnBitmasks.put("userName", 8L);

		columnBitmasks.put("createDate", 16L);

		columnBitmasks.put("modifiedDate", 32L);

		columnBitmasks.put("samlIdpSsoSessionKey", 64L);

		_columnBitmasks = Collections.unmodifiableMap(columnBitmasks);
	}

	private long _columnBitmask;
	private SamlIdpSsoSession _escapedModel;

}