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

package com.liferay.multi.factor.authentication.test.model.impl;

import com.liferay.multi.factor.authentication.test.model.MFATestEntry;
import com.liferay.petra.lang.HashUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.MVCCModel;
import com.liferay.portal.kernel.service.persistence.impl.UserInputString;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * The cache model class for representing MFATestEntry in entity cache.
 *
 * @author Arthur Chan
 * @generated
 */
public class MFATestEntryCacheModel
	implements CacheModel<MFATestEntry>, Externalizable, MVCCModel {

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof MFATestEntryCacheModel)) {
			return false;
		}

		MFATestEntryCacheModel mfaTestEntryCacheModel =
			(MFATestEntryCacheModel)object;

		if ((mfaTestEntryId == mfaTestEntryCacheModel.mfaTestEntryId) &&
			(mvccVersion == mfaTestEntryCacheModel.mvccVersion)) {

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = HashUtil.hash(0, mfaTestEntryId);

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
		StringBundler sb = new StringBundler(9);

		sb.append("{mvccVersion=");
		sb.append(mvccVersion);
		sb.append(", mfaTestEntryId=");
		sb.append(mfaTestEntryId);
		sb.append(", testString=");
		sb.append(testString);
		sb.append(", testUserInputString=");
		sb.append(testUserInputString);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public MFATestEntry toEntityModel() {
		MFATestEntryImpl mfaTestEntryImpl = new MFATestEntryImpl();

		mfaTestEntryImpl.setMvccVersion(mvccVersion);
		mfaTestEntryImpl.setMfaTestEntryId(mfaTestEntryId);

		if (testString == null) {
			mfaTestEntryImpl.setTestString("");
		}
		else {
			mfaTestEntryImpl.setTestString(testString);
		}

		mfaTestEntryImpl.setTestUserInputString(testUserInputString);

		mfaTestEntryImpl.resetOriginalValues();

		return mfaTestEntryImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput)
		throws ClassNotFoundException, IOException {

		mvccVersion = objectInput.readLong();

		mfaTestEntryId = objectInput.readLong();
		testString = objectInput.readUTF();
		testUserInputString = (UserInputString)objectInput.readObject();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		objectOutput.writeLong(mvccVersion);

		objectOutput.writeLong(mfaTestEntryId);

		if (testString == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(testString);
		}

		objectOutput.writeObject(testUserInputString);
	}

	public long mvccVersion;
	public long mfaTestEntryId;
	public String testString;
	public UserInputString testUserInputString;

}