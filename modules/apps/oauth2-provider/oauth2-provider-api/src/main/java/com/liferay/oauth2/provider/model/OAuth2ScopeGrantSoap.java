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

package com.liferay.oauth2.provider.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.oauth2.provider.service.persistence.OAuth2ScopeGrantPK;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is used by SOAP remote services.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@ProviderType
public class OAuth2ScopeGrantSoap implements Serializable {
	public static OAuth2ScopeGrantSoap toSoapModel(OAuth2ScopeGrant model) {
		OAuth2ScopeGrantSoap soapModel = new OAuth2ScopeGrantSoap();

		soapModel.setApplicationName(model.getApplicationName());
		soapModel.setBundleSymbolicName(model.getBundleSymbolicName());
		soapModel.setBundleVersion(model.getBundleVersion());
		soapModel.setOAuth2ScopeName(model.getOAuth2ScopeName());
		soapModel.setOAuth2TokenId(model.getOAuth2TokenId());
		soapModel.setCompanyId(model.getCompanyId());
		soapModel.setCreateDate(model.getCreateDate());

		return soapModel;
	}

	public static OAuth2ScopeGrantSoap[] toSoapModels(OAuth2ScopeGrant[] models) {
		OAuth2ScopeGrantSoap[] soapModels = new OAuth2ScopeGrantSoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static OAuth2ScopeGrantSoap[][] toSoapModels(
		OAuth2ScopeGrant[][] models) {
		OAuth2ScopeGrantSoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels = new OAuth2ScopeGrantSoap[models.length][models[0].length];
		}
		else {
			soapModels = new OAuth2ScopeGrantSoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static OAuth2ScopeGrantSoap[] toSoapModels(
		List<OAuth2ScopeGrant> models) {
		List<OAuth2ScopeGrantSoap> soapModels = new ArrayList<OAuth2ScopeGrantSoap>(models.size());

		for (OAuth2ScopeGrant model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new OAuth2ScopeGrantSoap[soapModels.size()]);
	}

	public OAuth2ScopeGrantSoap() {
	}

	public OAuth2ScopeGrantPK getPrimaryKey() {
		return new OAuth2ScopeGrantPK(_applicationName, _bundleSymbolicName,
			_bundleVersion, _oAuth2ScopeName, _oAuth2TokenId);
	}

	public void setPrimaryKey(OAuth2ScopeGrantPK pk) {
		setApplicationName(pk.applicationName);
		setBundleSymbolicName(pk.bundleSymbolicName);
		setBundleVersion(pk.bundleVersion);
		setOAuth2ScopeName(pk.oAuth2ScopeName);
		setOAuth2TokenId(pk.oAuth2TokenId);
	}

	public String getApplicationName() {
		return _applicationName;
	}

	public void setApplicationName(String applicationName) {
		_applicationName = applicationName;
	}

	public String getBundleSymbolicName() {
		return _bundleSymbolicName;
	}

	public void setBundleSymbolicName(String bundleSymbolicName) {
		_bundleSymbolicName = bundleSymbolicName;
	}

	public String getBundleVersion() {
		return _bundleVersion;
	}

	public void setBundleVersion(String bundleVersion) {
		_bundleVersion = bundleVersion;
	}

	public String getOAuth2ScopeName() {
		return _oAuth2ScopeName;
	}

	public void setOAuth2ScopeName(String oAuth2ScopeName) {
		_oAuth2ScopeName = oAuth2ScopeName;
	}

	public String getOAuth2TokenId() {
		return _oAuth2TokenId;
	}

	public void setOAuth2TokenId(String oAuth2TokenId) {
		_oAuth2TokenId = oAuth2TokenId;
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

	private String _applicationName;
	private String _bundleSymbolicName;
	private String _bundleVersion;
	private String _oAuth2ScopeName;
	private String _oAuth2TokenId;
	private long _companyId;
	private Date _createDate;
}