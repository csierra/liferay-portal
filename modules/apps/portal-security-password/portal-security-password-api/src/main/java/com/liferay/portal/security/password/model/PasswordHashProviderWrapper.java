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

import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.wrapper.BaseModelWrapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class is a wrapper for {@link PasswordHashProvider}.
 * </p>
 *
 * @author Arthur Chan
 * @see PasswordHashProvider
 * @generated
 */
public class PasswordHashProviderWrapper
	extends BaseModelWrapper<PasswordHashProvider>
	implements ModelWrapper<PasswordHashProvider>, PasswordHashProvider {

	public PasswordHashProviderWrapper(
		PasswordHashProvider passwordHashProvider) {

		super(passwordHashProvider);
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("mvccVersion", getMvccVersion());
		attributes.put("uuid", getUuid());
		attributes.put("passwordHashProviderId", getPasswordHashProviderId());
		attributes.put("companyId", getCompanyId());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("hashProviderName", getHashProviderName());
		attributes.put("hashProviderMeta", getHashProviderMeta());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long mvccVersion = (Long)attributes.get("mvccVersion");

		if (mvccVersion != null) {
			setMvccVersion(mvccVersion);
		}

		String uuid = (String)attributes.get("uuid");

		if (uuid != null) {
			setUuid(uuid);
		}

		Long passwordHashProviderId = (Long)attributes.get(
			"passwordHashProviderId");

		if (passwordHashProviderId != null) {
			setPasswordHashProviderId(passwordHashProviderId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		String hashProviderName = (String)attributes.get("hashProviderName");

		if (hashProviderName != null) {
			setHashProviderName(hashProviderName);
		}

		String hashProviderMeta = (String)attributes.get("hashProviderMeta");

		if (hashProviderMeta != null) {
			setHashProviderMeta(hashProviderMeta);
		}
	}

	/**
	 * Returns the company ID of this password hash provider.
	 *
	 * @return the company ID of this password hash provider
	 */
	@Override
	public long getCompanyId() {
		return model.getCompanyId();
	}

	/**
	 * Returns the create date of this password hash provider.
	 *
	 * @return the create date of this password hash provider
	 */
	@Override
	public Date getCreateDate() {
		return model.getCreateDate();
	}

	/**
	 * Returns the hash provider meta of this password hash provider.
	 *
	 * @return the hash provider meta of this password hash provider
	 */
	@Override
	public String getHashProviderMeta() {
		return model.getHashProviderMeta();
	}

	/**
	 * Returns the hash provider name of this password hash provider.
	 *
	 * @return the hash provider name of this password hash provider
	 */
	@Override
	public String getHashProviderName() {
		return model.getHashProviderName();
	}

	/**
	 * Returns the modified date of this password hash provider.
	 *
	 * @return the modified date of this password hash provider
	 */
	@Override
	public Date getModifiedDate() {
		return model.getModifiedDate();
	}

	/**
	 * Returns the mvcc version of this password hash provider.
	 *
	 * @return the mvcc version of this password hash provider
	 */
	@Override
	public long getMvccVersion() {
		return model.getMvccVersion();
	}

	/**
	 * Returns the password hash provider ID of this password hash provider.
	 *
	 * @return the password hash provider ID of this password hash provider
	 */
	@Override
	public long getPasswordHashProviderId() {
		return model.getPasswordHashProviderId();
	}

	/**
	 * Returns the primary key of this password hash provider.
	 *
	 * @return the primary key of this password hash provider
	 */
	@Override
	public long getPrimaryKey() {
		return model.getPrimaryKey();
	}

	/**
	 * Returns the uuid of this password hash provider.
	 *
	 * @return the uuid of this password hash provider
	 */
	@Override
	public String getUuid() {
		return model.getUuid();
	}

	@Override
	public void persist() {
		model.persist();
	}

	/**
	 * Sets the company ID of this password hash provider.
	 *
	 * @param companyId the company ID of this password hash provider
	 */
	@Override
	public void setCompanyId(long companyId) {
		model.setCompanyId(companyId);
	}

	/**
	 * Sets the create date of this password hash provider.
	 *
	 * @param createDate the create date of this password hash provider
	 */
	@Override
	public void setCreateDate(Date createDate) {
		model.setCreateDate(createDate);
	}

	/**
	 * Sets the hash provider meta of this password hash provider.
	 *
	 * @param hashProviderMeta the hash provider meta of this password hash provider
	 */
	@Override
	public void setHashProviderMeta(String hashProviderMeta) {
		model.setHashProviderMeta(hashProviderMeta);
	}

	/**
	 * Sets the hash provider name of this password hash provider.
	 *
	 * @param hashProviderName the hash provider name of this password hash provider
	 */
	@Override
	public void setHashProviderName(String hashProviderName) {
		model.setHashProviderName(hashProviderName);
	}

	/**
	 * Sets the modified date of this password hash provider.
	 *
	 * @param modifiedDate the modified date of this password hash provider
	 */
	@Override
	public void setModifiedDate(Date modifiedDate) {
		model.setModifiedDate(modifiedDate);
	}

	/**
	 * Sets the mvcc version of this password hash provider.
	 *
	 * @param mvccVersion the mvcc version of this password hash provider
	 */
	@Override
	public void setMvccVersion(long mvccVersion) {
		model.setMvccVersion(mvccVersion);
	}

	/**
	 * Sets the password hash provider ID of this password hash provider.
	 *
	 * @param passwordHashProviderId the password hash provider ID of this password hash provider
	 */
	@Override
	public void setPasswordHashProviderId(long passwordHashProviderId) {
		model.setPasswordHashProviderId(passwordHashProviderId);
	}

	/**
	 * Sets the primary key of this password hash provider.
	 *
	 * @param primaryKey the primary key of this password hash provider
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		model.setPrimaryKey(primaryKey);
	}

	/**
	 * Sets the uuid of this password hash provider.
	 *
	 * @param uuid the uuid of this password hash provider
	 */
	@Override
	public void setUuid(String uuid) {
		model.setUuid(uuid);
	}

	@Override
	public StagedModelType getStagedModelType() {
		return model.getStagedModelType();
	}

	@Override
	protected PasswordHashProviderWrapper wrap(
		PasswordHashProvider passwordHashProvider) {

		return new PasswordHashProviderWrapper(passwordHashProvider);
	}

}