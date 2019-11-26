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

import java.util.Date;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The base model interface for the PasswordMeta service. Represents a row in the &quot;PasswordMeta&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation <code>com.liferay.portal.security.password.model.impl.PasswordMetaModelImpl</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in <code>com.liferay.portal.security.password.model.impl.PasswordMetaImpl</code>.
 * </p>
 *
 * @author arthurchan35
 * @see PasswordMeta
 * @generated
 */
@ProviderType
public interface PasswordMetaModel extends BaseModel<PasswordMeta>, MVCCModel {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a password meta model instance should use the {@link PasswordMeta} interface instead.
	 */

	/**
	 * Returns the primary key of this password meta.
	 *
	 * @return the primary key of this password meta
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this password meta.
	 *
	 * @param primaryKey the primary key of this password meta
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the mvcc version of this password meta.
	 *
	 * @return the mvcc version of this password meta
	 */
	@Override
	public long getMvccVersion();

	/**
	 * Sets the mvcc version of this password meta.
	 *
	 * @param mvccVersion the mvcc version of this password meta
	 */
	@Override
	public void setMvccVersion(long mvccVersion);

	/**
	 * Returns the uuid of this password meta.
	 *
	 * @return the uuid of this password meta
	 */
	@AutoEscape
	public String getUuid();

	/**
	 * Sets the uuid of this password meta.
	 *
	 * @param uuid the uuid of this password meta
	 */
	public void setUuid(String uuid);

	/**
	 * Returns the meta ID of this password meta.
	 *
	 * @return the meta ID of this password meta
	 */
	public long getMetaId();

	/**
	 * Sets the meta ID of this password meta.
	 *
	 * @param metaId the meta ID of this password meta
	 */
	public void setMetaId(long metaId);

	/**
	 * Returns the create date of this password meta.
	 *
	 * @return the create date of this password meta
	 */
	public Date getCreateDate();

	/**
	 * Sets the create date of this password meta.
	 *
	 * @param createDate the create date of this password meta
	 */
	public void setCreateDate(Date createDate);

	/**
	 * Returns the modified date of this password meta.
	 *
	 * @return the modified date of this password meta
	 */
	public Date getModifiedDate();

	/**
	 * Sets the modified date of this password meta.
	 *
	 * @param modifiedDate the modified date of this password meta
	 */
	public void setModifiedDate(Date modifiedDate);

	/**
	 * Returns the password entry ID of this password meta.
	 *
	 * @return the password entry ID of this password meta
	 */
	public long getPasswordEntryId();

	/**
	 * Sets the password entry ID of this password meta.
	 *
	 * @param passwordEntryId the password entry ID of this password meta
	 */
	public void setPasswordEntryId(long passwordEntryId);

	/**
	 * Returns the hash algorithm entry ID of this password meta.
	 *
	 * @return the hash algorithm entry ID of this password meta
	 */
	public long getHashAlgorithmEntryId();

	/**
	 * Sets the hash algorithm entry ID of this password meta.
	 *
	 * @param hashAlgorithmEntryId the hash algorithm entry ID of this password meta
	 */
	public void setHashAlgorithmEntryId(long hashAlgorithmEntryId);

	/**
	 * Returns the salt of this password meta.
	 *
	 * @return the salt of this password meta
	 */
	@AutoEscape
	public String getSalt();

	/**
	 * Sets the salt of this password meta.
	 *
	 * @param salt the salt of this password meta
	 */
	public void setSalt(String salt);

}