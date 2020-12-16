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

import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.wrapper.BaseModelWrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class is a wrapper for {@link MFATestEntry}.
 * </p>
 *
 * @author Arthur Chan
 * @see MFATestEntry
 * @generated
 */
public class MFATestEntryWrapper
	extends BaseModelWrapper<MFATestEntry>
	implements MFATestEntry, ModelWrapper<MFATestEntry> {

	public MFATestEntryWrapper(MFATestEntry mfaTestEntry) {
		super(mfaTestEntry);
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("mvccVersion", getMvccVersion());
		attributes.put("mfaTestEntryId", getMfaTestEntryId());
		attributes.put("testString", getTestString());
		attributes.put("testUserInputString", getTestUserInputString());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long mvccVersion = (Long)attributes.get("mvccVersion");

		if (mvccVersion != null) {
			setMvccVersion(mvccVersion);
		}

		Long mfaTestEntryId = (Long)attributes.get("mfaTestEntryId");

		if (mfaTestEntryId != null) {
			setMfaTestEntryId(mfaTestEntryId);
		}

		String testString = (String)attributes.get("testString");

		if (testString != null) {
			setTestString(testString);
		}

		UserInputString testUserInputString = (UserInputString)attributes.get(
			"testUserInputString");

		if (testUserInputString != null) {
			setTestUserInputString(testUserInputString);
		}
	}

	/**
	 * Returns the mfa test entry ID of this mfa test entry.
	 *
	 * @return the mfa test entry ID of this mfa test entry
	 */
	@Override
	public long getMfaTestEntryId() {
		return model.getMfaTestEntryId();
	}

	/**
	 * Returns the mvcc version of this mfa test entry.
	 *
	 * @return the mvcc version of this mfa test entry
	 */
	@Override
	public long getMvccVersion() {
		return model.getMvccVersion();
	}

	/**
	 * Returns the primary key of this mfa test entry.
	 *
	 * @return the primary key of this mfa test entry
	 */
	@Override
	public long getPrimaryKey() {
		return model.getPrimaryKey();
	}

	/**
	 * Returns the test string of this mfa test entry.
	 *
	 * @return the test string of this mfa test entry
	 */
	@Override
	public String getTestString() {
		return model.getTestString();
	}

	/**
	 * Returns the test user input string of this mfa test entry.
	 *
	 * @return the test user input string of this mfa test entry
	 */
	@Override
	public com.liferay.portal.kernel.service.persistence.impl.UserInputString
		getTestUserInputString() {

		return model.getTestUserInputString();
	}

	@Override
	public void persist() {
		model.persist();
	}

	/**
	 * Sets the mfa test entry ID of this mfa test entry.
	 *
	 * @param mfaTestEntryId the mfa test entry ID of this mfa test entry
	 */
	@Override
	public void setMfaTestEntryId(long mfaTestEntryId) {
		model.setMfaTestEntryId(mfaTestEntryId);
	}

	/**
	 * Sets the mvcc version of this mfa test entry.
	 *
	 * @param mvccVersion the mvcc version of this mfa test entry
	 */
	@Override
	public void setMvccVersion(long mvccVersion) {
		model.setMvccVersion(mvccVersion);
	}

	/**
	 * Sets the primary key of this mfa test entry.
	 *
	 * @param primaryKey the primary key of this mfa test entry
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		model.setPrimaryKey(primaryKey);
	}

	/**
	 * Sets the test string of this mfa test entry.
	 *
	 * @param testString the test string of this mfa test entry
	 */
	@Override
	public void setTestString(String testString) {
		model.setTestString(testString);
	}

	/**
	 * Sets the test user input string of this mfa test entry.
	 *
	 * @param testUserInputString the test user input string of this mfa test entry
	 */
	@Override
	public void setTestUserInputString(
		com.liferay.portal.kernel.service.persistence.impl.UserInputString
			testUserInputString) {

		model.setTestUserInputString(testUserInputString);
	}

	@Override
	protected MFATestEntryWrapper wrap(MFATestEntry mfaTestEntry) {
		return new MFATestEntryWrapper(mfaTestEntry);
	}

}