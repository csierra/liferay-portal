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
public class OAuth2ApplicationSoap implements Serializable {
	public static OAuth2ApplicationSoap toSoapModel(OAuth2Application model) {
		OAuth2ApplicationSoap soapModel = new OAuth2ApplicationSoap();

		soapModel.setOAuth2ApplicationId(model.getOAuth2ApplicationId());
		soapModel.setCompanyId(model.getCompanyId());
		soapModel.setUserId(model.getUserId());
		soapModel.setUserName(model.getUserName());
		soapModel.setCreateDate(model.getCreateDate());
		soapModel.setModifiedDate(model.getModifiedDate());
		soapModel.setApplicationSecret(model.getApplicationSecret());
		soapModel.setConfidential(model.getConfidential());
		soapModel.setDescription(model.getDescription());
		soapModel.setName(model.getName());
		soapModel.setWebUrl(model.getWebUrl());

		return soapModel;
	}

	public static OAuth2ApplicationSoap[] toSoapModels(
		OAuth2Application[] models) {
		OAuth2ApplicationSoap[] soapModels = new OAuth2ApplicationSoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static OAuth2ApplicationSoap[][] toSoapModels(
		OAuth2Application[][] models) {
		OAuth2ApplicationSoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels = new OAuth2ApplicationSoap[models.length][models[0].length];
		}
		else {
			soapModels = new OAuth2ApplicationSoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static OAuth2ApplicationSoap[] toSoapModels(
		List<OAuth2Application> models) {
		List<OAuth2ApplicationSoap> soapModels = new ArrayList<OAuth2ApplicationSoap>(models.size());

		for (OAuth2Application model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new OAuth2ApplicationSoap[soapModels.size()]);
	}

	public OAuth2ApplicationSoap() {
	}

	public String getPrimaryKey() {
		return _oAuth2ApplicationId;
	}

	public void setPrimaryKey(String pk) {
		setOAuth2ApplicationId(pk);
	}

	public String getOAuth2ApplicationId() {
		return _oAuth2ApplicationId;
	}

	public void setOAuth2ApplicationId(String oAuth2ApplicationId) {
		_oAuth2ApplicationId = oAuth2ApplicationId;
	}

	public long getCompanyId() {
		return _companyId;
	}

	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	public long getUserId() {
		return _userId;
	}

	public void setUserId(long userId) {
		_userId = userId;
	}

	public String getUserName() {
		return _userName;
	}

	public void setUserName(String userName) {
		_userName = userName;
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

	public String getApplicationSecret() {
		return _applicationSecret;
	}

	public void setApplicationSecret(String applicationSecret) {
		_applicationSecret = applicationSecret;
	}

	public Boolean getConfidential() {
		return _confidential;
	}

	public void setConfidential(Boolean confidential) {
		_confidential = confidential;
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		_description = description;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public String getWebUrl() {
		return _webUrl;
	}

	public void setWebUrl(String webUrl) {
		_webUrl = webUrl;
	}

	private String _oAuth2ApplicationId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private String _applicationSecret;
	private Boolean _confidential;
	private String _description;
	private String _name;
	private String _webUrl;
}