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

package com.liferay.portal.security.password.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is used by SOAP remote services, specifically {@link com.liferay.portal.security.password.service.http.PasswordHashProviderServiceSoap}.
 *
 * @author Arthur Chan
 * @generated
 */
public class PasswordHashProviderSoap implements Serializable {

	public static PasswordHashProviderSoap toSoapModel(
		PasswordHashProvider model) {

		PasswordHashProviderSoap soapModel = new PasswordHashProviderSoap();

		soapModel.setMvccVersion(model.getMvccVersion());
		soapModel.setUuid(model.getUuid());
		soapModel.setPasswordHashProviderId(model.getPasswordHashProviderId());
		soapModel.setCompanyId(model.getCompanyId());
		soapModel.setCreateDate(model.getCreateDate());
		soapModel.setModifiedDate(model.getModifiedDate());
		soapModel.setHashProviderName(model.getHashProviderName());
		soapModel.setHashProviderMeta(model.getHashProviderMeta());

		return soapModel;
	}

	public static PasswordHashProviderSoap[] toSoapModels(
		PasswordHashProvider[] models) {

		PasswordHashProviderSoap[] soapModels =
			new PasswordHashProviderSoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static PasswordHashProviderSoap[][] toSoapModels(
		PasswordHashProvider[][] models) {

		PasswordHashProviderSoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels =
				new PasswordHashProviderSoap[models.length][models[0].length];
		}
		else {
			soapModels = new PasswordHashProviderSoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static PasswordHashProviderSoap[] toSoapModels(
		List<PasswordHashProvider> models) {

		List<PasswordHashProviderSoap> soapModels =
			new ArrayList<PasswordHashProviderSoap>(models.size());

		for (PasswordHashProvider model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(
			new PasswordHashProviderSoap[soapModels.size()]);
	}

	public PasswordHashProviderSoap() {
	}

	public long getPrimaryKey() {
		return _passwordHashProviderId;
	}

	public void setPrimaryKey(long pk) {
		setPasswordHashProviderId(pk);
	}

	public long getMvccVersion() {
		return _mvccVersion;
	}

	public void setMvccVersion(long mvccVersion) {
		_mvccVersion = mvccVersion;
	}

	public String getUuid() {
		return _uuid;
	}

	public void setUuid(String uuid) {
		_uuid = uuid;
	}

	public long getPasswordHashProviderId() {
		return _passwordHashProviderId;
	}

	public void setPasswordHashProviderId(long passwordHashProviderId) {
		_passwordHashProviderId = passwordHashProviderId;
	}

	public long getCompanyId() {
		return _companyId;
	}

	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	public Date getCreateDate() {
		return _createDate;
	}

	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		_modifiedDate = modifiedDate;
	}

	public String getHashProviderName() {
		return _hashProviderName;
	}

	public void setHashProviderName(String hashProviderName) {
		_hashProviderName = hashProviderName;
	}

	public String getHashProviderMeta() {
		return _hashProviderMeta;
	}

	public void setHashProviderMeta(String hashProviderMeta) {
		_hashProviderMeta = hashProviderMeta;
	}

	private long _mvccVersion;
	private String _uuid;
	private long _passwordHashProviderId;
	private long _companyId;
	private Date _createDate;
	private Date _modifiedDate;
	private String _hashProviderName;
	private String _hashProviderMeta;

}