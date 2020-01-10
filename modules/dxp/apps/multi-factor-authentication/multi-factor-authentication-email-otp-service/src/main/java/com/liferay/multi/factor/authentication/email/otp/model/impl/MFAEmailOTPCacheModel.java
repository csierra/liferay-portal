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

import com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP;
import com.liferay.petra.lang.HashUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.MVCCModel;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing MFAEmailOTP in entity cache.
 *
 * @author Arthur Chan
 * @generated
 */
public class MFAEmailOTPCacheModel
	implements CacheModel<MFAEmailOTP>, Externalizable, MVCCModel {

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof MFAEmailOTPCacheModel)) {
			return false;
		}

		MFAEmailOTPCacheModel mfaEmailOTPCacheModel =
			(MFAEmailOTPCacheModel)obj;

		if ((emailOTPId == mfaEmailOTPCacheModel.emailOTPId) &&
			(mvccVersion == mfaEmailOTPCacheModel.mvccVersion)) {

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = HashUtil.hash(0, emailOTPId);

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
		StringBundler sb = new StringBundler(25);

		sb.append("{mvccVersion=");
		sb.append(mvccVersion);
		sb.append(", emailOTPId=");
		sb.append(emailOTPId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", userId=");
		sb.append(userId);
		sb.append(", userName=");
		sb.append(userName);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", failedAttempts=");
		sb.append(failedAttempts);
		sb.append(", lastFailDate=");
		sb.append(lastFailDate);
		sb.append(", lastFailIP=");
		sb.append(lastFailIP);
		sb.append(", lastSuccessDate=");
		sb.append(lastSuccessDate);
		sb.append(", lastSuccessIP=");
		sb.append(lastSuccessIP);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public MFAEmailOTP toEntityModel() {
		MFAEmailOTPImpl mfaEmailOTPImpl = new MFAEmailOTPImpl();

		mfaEmailOTPImpl.setMvccVersion(mvccVersion);
		mfaEmailOTPImpl.setEmailOTPId(emailOTPId);
		mfaEmailOTPImpl.setCompanyId(companyId);
		mfaEmailOTPImpl.setUserId(userId);

		if (userName == null) {
			mfaEmailOTPImpl.setUserName("");
		}
		else {
			mfaEmailOTPImpl.setUserName(userName);
		}

		if (createDate == Long.MIN_VALUE) {
			mfaEmailOTPImpl.setCreateDate(null);
		}
		else {
			mfaEmailOTPImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			mfaEmailOTPImpl.setModifiedDate(null);
		}
		else {
			mfaEmailOTPImpl.setModifiedDate(new Date(modifiedDate));
		}

		mfaEmailOTPImpl.setFailedAttempts(failedAttempts);

		if (lastFailDate == Long.MIN_VALUE) {
			mfaEmailOTPImpl.setLastFailDate(null);
		}
		else {
			mfaEmailOTPImpl.setLastFailDate(new Date(lastFailDate));
		}

		if (lastFailIP == null) {
			mfaEmailOTPImpl.setLastFailIP("");
		}
		else {
			mfaEmailOTPImpl.setLastFailIP(lastFailIP);
		}

		if (lastSuccessDate == Long.MIN_VALUE) {
			mfaEmailOTPImpl.setLastSuccessDate(null);
		}
		else {
			mfaEmailOTPImpl.setLastSuccessDate(new Date(lastSuccessDate));
		}

		if (lastSuccessIP == null) {
			mfaEmailOTPImpl.setLastSuccessIP("");
		}
		else {
			mfaEmailOTPImpl.setLastSuccessIP(lastSuccessIP);
		}

		mfaEmailOTPImpl.resetOriginalValues();

		return mfaEmailOTPImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		mvccVersion = objectInput.readLong();

		emailOTPId = objectInput.readLong();

		companyId = objectInput.readLong();

		userId = objectInput.readLong();
		userName = objectInput.readUTF();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();

		failedAttempts = objectInput.readInt();
		lastFailDate = objectInput.readLong();
		lastFailIP = objectInput.readUTF();
		lastSuccessDate = objectInput.readLong();
		lastSuccessIP = objectInput.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		objectOutput.writeLong(mvccVersion);

		objectOutput.writeLong(emailOTPId);

		objectOutput.writeLong(companyId);

		objectOutput.writeLong(userId);

		if (userName == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(userName);
		}

		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);

		objectOutput.writeInt(failedAttempts);
		objectOutput.writeLong(lastFailDate);

		if (lastFailIP == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(lastFailIP);
		}

		objectOutput.writeLong(lastSuccessDate);

		if (lastSuccessIP == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(lastSuccessIP);
		}
	}

	public long mvccVersion;
	public long emailOTPId;
	public long companyId;
	public long userId;
	public String userName;
	public long createDate;
	public long modifiedDate;
	public int failedAttempts;
	public long lastFailDate;
	public String lastFailIP;
	public long lastSuccessDate;
	public String lastSuccessIP;

}