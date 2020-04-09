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

package com.liferay.portal.security.password.model.impl;

import com.liferay.petra.lang.HashUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.MVCCModel;
import com.liferay.portal.security.password.model.PasswordHashProvider;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing PasswordHashProvider in entity cache.
 *
 * @author Arthur Chan
 * @generated
 */
public class PasswordHashProviderCacheModel
	implements CacheModel<PasswordHashProvider>, Externalizable, MVCCModel {

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof PasswordHashProviderCacheModel)) {
			return false;
		}

		PasswordHashProviderCacheModel passwordHashProviderCacheModel =
			(PasswordHashProviderCacheModel)obj;

		if ((passwordHashProviderId ==
				passwordHashProviderCacheModel.passwordHashProviderId) &&
			(mvccVersion == passwordHashProviderCacheModel.mvccVersion)) {

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = HashUtil.hash(0, passwordHashProviderId);

		return HashUtil.hash(hashCode, mvccVersion);
	}

	@Override
	public long getMvccVersion() {
		return mvccVersion;
	}

	@Override
	public void setMvccVersion(long mvccVersion) {
		this.mvccVersion = mvccVersion;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(17);

		sb.append("{mvccVersion=");
		sb.append(mvccVersion);
		sb.append(", uuid=");
		sb.append(uuid);
		sb.append(", passwordHashProviderId=");
		sb.append(passwordHashProviderId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", hashProviderName=");
		sb.append(hashProviderName);
		sb.append(", hashProviderMeta=");
		sb.append(hashProviderMeta);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public PasswordHashProvider toEntityModel() {
		PasswordHashProviderImpl passwordHashProviderImpl =
			new PasswordHashProviderImpl();

		passwordHashProviderImpl.setMvccVersion(mvccVersion);

		if (uuid == null) {
			passwordHashProviderImpl.setUuid("");
		}
		else {
			passwordHashProviderImpl.setUuid(uuid);
		}

		passwordHashProviderImpl.setPasswordHashProviderId(
			passwordHashProviderId);
		passwordHashProviderImpl.setCompanyId(companyId);

		if (createDate == Long.MIN_VALUE) {
			passwordHashProviderImpl.setCreateDate(null);
		}
		else {
			passwordHashProviderImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			passwordHashProviderImpl.setModifiedDate(null);
		}
		else {
			passwordHashProviderImpl.setModifiedDate(new Date(modifiedDate));
		}

		if (hashProviderName == null) {
			passwordHashProviderImpl.setHashProviderName("");
		}
		else {
			passwordHashProviderImpl.setHashProviderName(hashProviderName);
		}

		if (hashProviderMeta == null) {
			passwordHashProviderImpl.setHashProviderMeta("");
		}
		else {
			passwordHashProviderImpl.setHashProviderMeta(hashProviderMeta);
		}

		passwordHashProviderImpl.resetOriginalValues();

		return passwordHashProviderImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		mvccVersion = objectInput.readLong();
		uuid = objectInput.readUTF();

		passwordHashProviderId = objectInput.readLong();

		companyId = objectInput.readLong();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();
		hashProviderName = objectInput.readUTF();
		hashProviderMeta = objectInput.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		objectOutput.writeLong(mvccVersion);

		if (uuid == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(uuid);
		}

		objectOutput.writeLong(passwordHashProviderId);

		objectOutput.writeLong(companyId);
		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);

		if (hashProviderName == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(hashProviderName);
		}

		if (hashProviderMeta == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(hashProviderMeta);
		}
	}

	public long mvccVersion;
	public String uuid;
	public long passwordHashProviderId;
	public long companyId;
	public long createDate;
	public long modifiedDate;
	public String hashProviderName;
	public String hashProviderMeta;

}