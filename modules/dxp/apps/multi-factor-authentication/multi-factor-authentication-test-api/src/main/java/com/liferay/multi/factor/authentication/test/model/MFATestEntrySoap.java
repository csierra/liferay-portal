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

package com.liferay.multi.factor.authentication.test.model;

import com.liferay.portal.kernel.service.persistence.impl.UserInputString;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used by SOAP remote services.
 *
 * @author Arthur Chan
 * @deprecated As of Athanasius (7.3.x), with no direct replacement
 * @generated
 */
@Deprecated
public class MFATestEntrySoap implements Serializable {

	public static MFATestEntrySoap toSoapModel(MFATestEntry model) {
		MFATestEntrySoap soapModel = new MFATestEntrySoap();

		soapModel.setMvccVersion(model.getMvccVersion());
		soapModel.setMfaTestEntryId(model.getMfaTestEntryId());
		soapModel.setTestString(model.getTestString());
		soapModel.setTestUserInputString(model.getTestUserInputString());

		return soapModel;
	}

	public static MFATestEntrySoap[] toSoapModels(MFATestEntry[] models) {
		MFATestEntrySoap[] soapModels = new MFATestEntrySoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static MFATestEntrySoap[][] toSoapModels(MFATestEntry[][] models) {
		MFATestEntrySoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels = new MFATestEntrySoap[models.length][models[0].length];
		}
		else {
			soapModels = new MFATestEntrySoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static MFATestEntrySoap[] toSoapModels(List<MFATestEntry> models) {
		List<MFATestEntrySoap> soapModels = new ArrayList<MFATestEntrySoap>(
			models.size());

		for (MFATestEntry model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new MFATestEntrySoap[soapModels.size()]);
	}

	public MFATestEntrySoap() {
	}

	public long getPrimaryKey() {
		return _mfaTestEntryId;
	}

	public void setPrimaryKey(long pk) {
		setMfaTestEntryId(pk);
	}

	public long getMvccVersion() {
		return _mvccVersion;
	}

	public void setMvccVersion(long mvccVersion) {
		_mvccVersion = mvccVersion;
	}

	public long getMfaTestEntryId() {
		return _mfaTestEntryId;
	}

	public void setMfaTestEntryId(long mfaTestEntryId) {
		_mfaTestEntryId = mfaTestEntryId;
	}

	public String getTestString() {
		return _testString;
	}

	public void setTestString(String testString) {
		_testString = testString;
	}

	public UserInputString getTestUserInputString() {
		return _testUserInputString;
	}

	public void setTestUserInputString(UserInputString testUserInputString) {
		_testUserInputString = testUserInputString;
	}

	private long _mvccVersion;
	private long _mfaTestEntryId;
	private String _testString;
	private UserInputString _testUserInputString;

}