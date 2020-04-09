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

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.MVCCModel;
import com.liferay.portal.kernel.model.ShardedModel;
import com.liferay.portal.kernel.model.StagedModel;

import java.util.Date;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The base model interface for the PasswordHashProvider service. Represents a row in the &quot;PasswordHashProvider&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation <code>com.liferay.portal.security.password.model.impl.PasswordHashProviderModelImpl</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in <code>com.liferay.portal.security.password.model.impl.PasswordHashProviderImpl</code>.
 * </p>
 *
 * @author Arthur Chan
 * @see PasswordHashProvider
 * @generated
 */
@ProviderType
public interface PasswordHashProviderModel
	extends BaseModel<PasswordHashProvider>, MVCCModel, ShardedModel,
			StagedModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a password hash provider model instance should use the {@link PasswordHashProvider} interface instead.
	 */

	/**
	 * Returns the primary key of this password hash provider.
	 *
	 * @return the primary key of this password hash provider
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this password hash provider.
	 *
	 * @param primaryKey the primary key of this password hash provider
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the mvcc version of this password hash provider.
	 *
	 * @return the mvcc version of this password hash provider
	 */
	@Override
	public long getMvccVersion();

	/**
	 * Sets the mvcc version of this password hash provider.
	 *
	 * @param mvccVersion the mvcc version of this password hash provider
	 */
	@Override
	public void setMvccVersion(long mvccVersion);

	/**
	 * Returns the uuid of this password hash provider.
	 *
	 * @return the uuid of this password hash provider
	 */
	@AutoEscape
	@Override
	public String getUuid();

	/**
	 * Sets the uuid of this password hash provider.
	 *
	 * @param uuid the uuid of this password hash provider
	 */
	@Override
	public void setUuid(String uuid);

	/**
	 * Returns the password hash provider ID of this password hash provider.
	 *
	 * @return the password hash provider ID of this password hash provider
	 */
	public long getPasswordHashProviderId();

	/**
	 * Sets the password hash provider ID of this password hash provider.
	 *
	 * @param passwordHashProviderId the password hash provider ID of this password hash provider
	 */
	public void setPasswordHashProviderId(long passwordHashProviderId);

	/**
	 * Returns the company ID of this password hash provider.
	 *
	 * @return the company ID of this password hash provider
	 */
	@Override
	public long getCompanyId();

	/**
	 * Sets the company ID of this password hash provider.
	 *
	 * @param companyId the company ID of this password hash provider
	 */
	@Override
	public void setCompanyId(long companyId);

	/**
	 * Returns the create date of this password hash provider.
	 *
	 * @return the create date of this password hash provider
	 */
	@Override
	public Date getCreateDate();

	/**
	 * Sets the create date of this password hash provider.
	 *
	 * @param createDate the create date of this password hash provider
	 */
	@Override
	public void setCreateDate(Date createDate);

	/**
	 * Returns the modified date of this password hash provider.
	 *
	 * @return the modified date of this password hash provider
	 */
	@Override
	public Date getModifiedDate();

	/**
	 * Sets the modified date of this password hash provider.
	 *
	 * @param modifiedDate the modified date of this password hash provider
	 */
	@Override
	public void setModifiedDate(Date modifiedDate);

	/**
	 * Returns the hash provider name of this password hash provider.
	 *
	 * @return the hash provider name of this password hash provider
	 */
	@AutoEscape
	public String getHashProviderName();

	/**
	 * Sets the hash provider name of this password hash provider.
	 *
	 * @param hashProviderName the hash provider name of this password hash provider
	 */
	public void setHashProviderName(String hashProviderName);

	/**
	 * Returns the hash provider meta of this password hash provider.
	 *
	 * @return the hash provider meta of this password hash provider
	 */
	@AutoEscape
	public String getHashProviderMeta();

	/**
	 * Sets the hash provider meta of this password hash provider.
	 *
	 * @param hashProviderMeta the hash provider meta of this password hash provider
	 */
	public void setHashProviderMeta(String hashProviderMeta);

}