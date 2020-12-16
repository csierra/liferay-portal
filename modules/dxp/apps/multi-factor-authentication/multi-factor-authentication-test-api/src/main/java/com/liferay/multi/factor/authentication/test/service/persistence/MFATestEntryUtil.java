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

package com.liferay.multi.factor.authentication.test.service.persistence;

import com.liferay.multi.factor.authentication.test.model.MFATestEntry;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The persistence utility for the mfa test entry service. This utility wraps <code>com.liferay.multi.factor.authentication.test.service.persistence.impl.MFATestEntryPersistenceImpl</code> and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Arthur Chan
 * @see MFATestEntryPersistence
 * @generated
 */
public class MFATestEntryUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache()
	 */
	public static void clearCache() {
		getPersistence().clearCache();
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static void clearCache(MFATestEntry mfaTestEntry) {
		getPersistence().clearCache(mfaTestEntry);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#countWithDynamicQuery(DynamicQuery)
	 */
	public static long countWithDynamicQuery(DynamicQuery dynamicQuery) {
		return getPersistence().countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#fetchByPrimaryKeys(Set)
	 */
	public static Map<Serializable, MFATestEntry> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<MFATestEntry> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {

		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<MFATestEntry> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<MFATestEntry> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<MFATestEntry> orderByComparator) {

		return getPersistence().findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static MFATestEntry update(MFATestEntry mfaTestEntry) {
		return getPersistence().update(mfaTestEntry);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static MFATestEntry update(
		MFATestEntry mfaTestEntry, ServiceContext serviceContext) {

		return getPersistence().update(mfaTestEntry, serviceContext);
	}

	/**
	 * Caches the mfa test entry in the entity cache if it is enabled.
	 *
	 * @param mfaTestEntry the mfa test entry
	 */
	public static void cacheResult(MFATestEntry mfaTestEntry) {
		getPersistence().cacheResult(mfaTestEntry);
	}

	/**
	 * Caches the mfa test entries in the entity cache if it is enabled.
	 *
	 * @param mfaTestEntries the mfa test entries
	 */
	public static void cacheResult(List<MFATestEntry> mfaTestEntries) {
		getPersistence().cacheResult(mfaTestEntries);
	}

	/**
	 * Creates a new mfa test entry with the primary key. Does not add the mfa test entry to the database.
	 *
	 * @param mfaTestEntryId the primary key for the new mfa test entry
	 * @return the new mfa test entry
	 */
	public static MFATestEntry create(long mfaTestEntryId) {
		return getPersistence().create(mfaTestEntryId);
	}

	/**
	 * Removes the mfa test entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param mfaTestEntryId the primary key of the mfa test entry
	 * @return the mfa test entry that was removed
	 * @throws NoSuchEntryException if a mfa test entry with the primary key could not be found
	 */
	public static MFATestEntry remove(long mfaTestEntryId)
		throws com.liferay.multi.factor.authentication.test.exception.
			NoSuchEntryException {

		return getPersistence().remove(mfaTestEntryId);
	}

	public static MFATestEntry updateImpl(MFATestEntry mfaTestEntry) {
		return getPersistence().updateImpl(mfaTestEntry);
	}

	/**
	 * Returns the mfa test entry with the primary key or throws a <code>NoSuchEntryException</code> if it could not be found.
	 *
	 * @param mfaTestEntryId the primary key of the mfa test entry
	 * @return the mfa test entry
	 * @throws NoSuchEntryException if a mfa test entry with the primary key could not be found
	 */
	public static MFATestEntry findByPrimaryKey(long mfaTestEntryId)
		throws com.liferay.multi.factor.authentication.test.exception.
			NoSuchEntryException {

		return getPersistence().findByPrimaryKey(mfaTestEntryId);
	}

	/**
	 * Returns the mfa test entry with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param mfaTestEntryId the primary key of the mfa test entry
	 * @return the mfa test entry, or <code>null</code> if a mfa test entry with the primary key could not be found
	 */
	public static MFATestEntry fetchByPrimaryKey(long mfaTestEntryId) {
		return getPersistence().fetchByPrimaryKey(mfaTestEntryId);
	}

	/**
	 * Returns all the mfa test entries.
	 *
	 * @return the mfa test entries
	 */
	public static List<MFATestEntry> findAll() {
		return getPersistence().findAll();
	}

	/**
	 * Returns a range of all the mfa test entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>MFATestEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of mfa test entries
	 * @param end the upper bound of the range of mfa test entries (not inclusive)
	 * @return the range of mfa test entries
	 */
	public static List<MFATestEntry> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
	}

	/**
	 * Returns an ordered range of all the mfa test entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>MFATestEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of mfa test entries
	 * @param end the upper bound of the range of mfa test entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of mfa test entries
	 */
	public static List<MFATestEntry> findAll(
		int start, int end, OrderByComparator<MFATestEntry> orderByComparator) {

		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the mfa test entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>MFATestEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of mfa test entries
	 * @param end the upper bound of the range of mfa test entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of mfa test entries
	 */
	public static List<MFATestEntry> findAll(
		int start, int end, OrderByComparator<MFATestEntry> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findAll(
			start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Removes all the mfa test entries from the database.
	 */
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	 * Returns the number of mfa test entries.
	 *
	 * @return the number of mfa test entries
	 */
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static MFATestEntryPersistence getPersistence() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<MFATestEntryPersistence, MFATestEntryPersistence> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(MFATestEntryPersistence.class);

		ServiceTracker<MFATestEntryPersistence, MFATestEntryPersistence>
			serviceTracker =
				new ServiceTracker
					<MFATestEntryPersistence, MFATestEntryPersistence>(
						bundle.getBundleContext(),
						MFATestEntryPersistence.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}