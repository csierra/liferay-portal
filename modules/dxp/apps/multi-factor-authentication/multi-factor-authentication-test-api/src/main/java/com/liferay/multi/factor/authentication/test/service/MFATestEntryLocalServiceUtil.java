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

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the local service utility for MFATestEntry. This utility wraps
 * <code>com.liferay.multi.factor.authentication.test.service.impl.MFATestEntryLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Arthur Chan
 * @see MFATestEntryLocalService
 * @generated
 */
public class MFATestEntryLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.multi.factor.authentication.test.service.impl.MFATestEntryLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

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
	public static
		com.liferay.multi.factor.authentication.test.model.MFATestEntry
			addMFATestEntry(
				com.liferay.multi.factor.authentication.test.model.MFATestEntry
					mfaTestEntry) {

		return getService().addMFATestEntry(mfaTestEntry);
	}

	/**
	 * Creates a new mfa test entry with the primary key. Does not add the mfa test entry to the database.
	 *
	 * @param mfaTestEntryId the primary key for the new mfa test entry
	 * @return the new mfa test entry
	 */
	public static
		com.liferay.multi.factor.authentication.test.model.MFATestEntry
			createMFATestEntry(long mfaTestEntryId) {

		return getService().createMFATestEntry(mfaTestEntryId);
	}

	/**
	 * @throws PortalException
	 */
	public static com.liferay.portal.kernel.model.PersistedModel
			createPersistedModel(java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().createPersistedModel(primaryKeyObj);
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
	public static
		com.liferay.multi.factor.authentication.test.model.MFATestEntry
				deleteMFATestEntry(long mfaTestEntryId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteMFATestEntry(mfaTestEntryId);
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
	public static
		com.liferay.multi.factor.authentication.test.model.MFATestEntry
			deleteMFATestEntry(
				com.liferay.multi.factor.authentication.test.model.MFATestEntry
					mfaTestEntry) {

		return getService().deleteMFATestEntry(mfaTestEntry);
	}

	/**
	 * @throws PortalException
	 */
	public static com.liferay.portal.kernel.model.PersistedModel
			deletePersistedModel(
				com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deletePersistedModel(persistedModel);
	}

	public static <T> T dslQuery(
		com.liferay.petra.sql.dsl.query.DSLQuery dslQuery) {

		return getService().dslQuery(dslQuery);
	}

	public static com.liferay.portal.kernel.dao.orm.DynamicQuery
		dynamicQuery() {

		return getService().dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return getService().dynamicQuery(dynamicQuery);
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
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return getService().dynamicQuery(dynamicQuery, start, end);
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
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return getService().dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return getService().dynamicQueryCount(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return getService().dynamicQueryCount(dynamicQuery, projection);
	}

	public static
		com.liferay.multi.factor.authentication.test.model.MFATestEntry
			fetchMFATestEntry(long mfaTestEntryId) {

		return getService().fetchMFATestEntry(mfaTestEntryId);
	}

	public static com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return getService().getActionableDynamicQuery();
	}

	public static
		com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
			getIndexableActionableDynamicQuery() {

		return getService().getIndexableActionableDynamicQuery();
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
	public static java.util.List
		<com.liferay.multi.factor.authentication.test.model.MFATestEntry>
			getMFATestEntries(int start, int end) {

		return getService().getMFATestEntries(start, end);
	}

	/**
	 * Returns the number of mfa test entries.
	 *
	 * @return the number of mfa test entries
	 */
	public static int getMFATestEntriesCount() {
		return getService().getMFATestEntriesCount();
	}

	/**
	 * Returns the mfa test entry with the primary key.
	 *
	 * @param mfaTestEntryId the primary key of the mfa test entry
	 * @return the mfa test entry
	 * @throws PortalException if a mfa test entry with the primary key could not be found
	 */
	public static
		com.liferay.multi.factor.authentication.test.model.MFATestEntry
				getMFATestEntry(long mfaTestEntryId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getMFATestEntry(mfaTestEntryId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	public static com.liferay.portal.kernel.model.PersistedModel
			getPersistedModel(java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getPersistedModel(primaryKeyObj);
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
	public static
		com.liferay.multi.factor.authentication.test.model.MFATestEntry
			updateMFATestEntry(
				com.liferay.multi.factor.authentication.test.model.MFATestEntry
					mfaTestEntry) {

		return getService().updateMFATestEntry(mfaTestEntry);
	}

	public static MFATestEntryLocalService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<MFATestEntryLocalService, MFATestEntryLocalService> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(MFATestEntryLocalService.class);

		ServiceTracker<MFATestEntryLocalService, MFATestEntryLocalService>
			serviceTracker =
				new ServiceTracker
					<MFATestEntryLocalService, MFATestEntryLocalService>(
						bundle.getBundleContext(),
						MFATestEntryLocalService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}