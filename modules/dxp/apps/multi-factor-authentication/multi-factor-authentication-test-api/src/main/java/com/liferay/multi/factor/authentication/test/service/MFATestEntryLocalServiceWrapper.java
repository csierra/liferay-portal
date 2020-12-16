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

package com.liferay.multi.factor.authentication.test.service;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link MFATestEntryLocalService}.
 *
 * @author Arthur Chan
 * @see MFATestEntryLocalService
 * @generated
 */
public class MFATestEntryLocalServiceWrapper
	implements MFATestEntryLocalService,
			   ServiceWrapper<MFATestEntryLocalService> {

	public MFATestEntryLocalServiceWrapper(
		MFATestEntryLocalService mfaTestEntryLocalService) {

		_mfaTestEntryLocalService = mfaTestEntryLocalService;
	}

	/**
	 * Adds the mfa test entry to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect MFATestEntryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param mfaTestEntry the mfa test entry
	 * @return the mfa test entry that was added
	 */
	@Override
	public com.liferay.multi.factor.authentication.test.model.MFATestEntry
		addMFATestEntry(
			com.liferay.multi.factor.authentication.test.model.MFATestEntry
				mfaTestEntry) {

		return _mfaTestEntryLocalService.addMFATestEntry(mfaTestEntry);
	}

	/**
	 * Creates a new mfa test entry with the primary key. Does not add the mfa test entry to the database.
	 *
	 * @param mfaTestEntryId the primary key for the new mfa test entry
	 * @return the new mfa test entry
	 */
	@Override
	public com.liferay.multi.factor.authentication.test.model.MFATestEntry
		createMFATestEntry(long mfaTestEntryId) {

		return _mfaTestEntryLocalService.createMFATestEntry(mfaTestEntryId);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel createPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _mfaTestEntryLocalService.createPersistedModel(primaryKeyObj);
	}

	/**
	 * Deletes the mfa test entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect MFATestEntryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param mfaTestEntryId the primary key of the mfa test entry
	 * @return the mfa test entry that was removed
	 * @throws PortalException if a mfa test entry with the primary key could not be found
	 */
	@Override
	public com.liferay.multi.factor.authentication.test.model.MFATestEntry
			deleteMFATestEntry(long mfaTestEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _mfaTestEntryLocalService.deleteMFATestEntry(mfaTestEntryId);
	}

	/**
	 * Deletes the mfa test entry from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect MFATestEntryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param mfaTestEntry the mfa test entry
	 * @return the mfa test entry that was removed
	 */
	@Override
	public com.liferay.multi.factor.authentication.test.model.MFATestEntry
		deleteMFATestEntry(
			com.liferay.multi.factor.authentication.test.model.MFATestEntry
				mfaTestEntry) {

		return _mfaTestEntryLocalService.deleteMFATestEntry(mfaTestEntry);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
			com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _mfaTestEntryLocalService.deletePersistedModel(persistedModel);
	}

	@Override
	public <T> T dslQuery(com.liferay.petra.sql.dsl.query.DSLQuery dslQuery) {
		return _mfaTestEntryLocalService.dslQuery(dslQuery);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _mfaTestEntryLocalService.dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _mfaTestEntryLocalService.dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.multi.factor.authentication.test.model.impl.MFATestEntryModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return _mfaTestEntryLocalService.dynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.multi.factor.authentication.test.model.impl.MFATestEntryModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return _mfaTestEntryLocalService.dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _mfaTestEntryLocalService.dynamicQueryCount(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return _mfaTestEntryLocalService.dynamicQueryCount(
			dynamicQuery, projection);
	}

	@Override
	public com.liferay.multi.factor.authentication.test.model.MFATestEntry
		fetchMFATestEntry(long mfaTestEntryId) {

		return _mfaTestEntryLocalService.fetchMFATestEntry(mfaTestEntryId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return _mfaTestEntryLocalService.getActionableDynamicQuery();
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		return _mfaTestEntryLocalService.getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns a range of all the mfa test entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.multi.factor.authentication.test.model.impl.MFATestEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of mfa test entries
	 * @param end the upper bound of the range of mfa test entries (not inclusive)
	 * @return the range of mfa test entries
	 */
	@Override
	public java.util.List
		<com.liferay.multi.factor.authentication.test.model.MFATestEntry>
			getMFATestEntries(int start, int end) {

		return _mfaTestEntryLocalService.getMFATestEntries(start, end);
	}

	/**
	 * Returns the number of mfa test entries.
	 *
	 * @return the number of mfa test entries
	 */
	@Override
	public int getMFATestEntriesCount() {
		return _mfaTestEntryLocalService.getMFATestEntriesCount();
	}

	/**
	 * Returns the mfa test entry with the primary key.
	 *
	 * @param mfaTestEntryId the primary key of the mfa test entry
	 * @return the mfa test entry
	 * @throws PortalException if a mfa test entry with the primary key could not be found
	 */
	@Override
	public com.liferay.multi.factor.authentication.test.model.MFATestEntry
			getMFATestEntry(long mfaTestEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _mfaTestEntryLocalService.getMFATestEntry(mfaTestEntryId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _mfaTestEntryLocalService.getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _mfaTestEntryLocalService.getPersistedModel(primaryKeyObj);
	}

	/**
	 * Updates the mfa test entry in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect MFATestEntryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param mfaTestEntry the mfa test entry
	 * @return the mfa test entry that was updated
	 */
	@Override
	public com.liferay.multi.factor.authentication.test.model.MFATestEntry
		updateMFATestEntry(
			com.liferay.multi.factor.authentication.test.model.MFATestEntry
				mfaTestEntry) {

		return _mfaTestEntryLocalService.updateMFATestEntry(mfaTestEntry);
	}

	@Override
	public MFATestEntryLocalService getWrappedService() {
		return _mfaTestEntryLocalService;
	}

	@Override
	public void setWrappedService(
		MFATestEntryLocalService mfaTestEntryLocalService) {

		_mfaTestEntryLocalService = mfaTestEntryLocalService;
	}

	private MFATestEntryLocalService _mfaTestEntryLocalService;

}