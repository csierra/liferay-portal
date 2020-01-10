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

package com.liferay.multi.factor.authentication.email.otp.service.persistence;

import com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP;
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
 * The persistence utility for the mfa email otp service. This utility wraps <code>com.liferay.multi.factor.authentication.email.otp.service.persistence.impl.MFAEmailOTPPersistenceImpl</code> and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Arthur Chan
 * @see MFAEmailOTPPersistence
 * @generated
 */
public class MFAEmailOTPUtil {

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
	public static void clearCache(MFAEmailOTP mfaEmailOTP) {
		getPersistence().clearCache(mfaEmailOTP);
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
	public static Map<Serializable, MFAEmailOTP> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<MFAEmailOTP> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {

		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<MFAEmailOTP> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<MFAEmailOTP> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<MFAEmailOTP> orderByComparator) {

		return getPersistence().findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static MFAEmailOTP update(MFAEmailOTP mfaEmailOTP) {
		return getPersistence().update(mfaEmailOTP);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static MFAEmailOTP update(
		MFAEmailOTP mfaEmailOTP, ServiceContext serviceContext) {

		return getPersistence().update(mfaEmailOTP, serviceContext);
	}

	/**
	 * Returns the mfa email otp where userId = &#63; or throws a <code>NoSuchEmailOTPException</code> if it could not be found.
	 *
	 * @param userId the user ID
	 * @return the matching mfa email otp
	 * @throws NoSuchEmailOTPException if a matching mfa email otp could not be found
	 */
	public static MFAEmailOTP findByUserId(long userId)
		throws com.liferay.multi.factor.authentication.email.otp.exception.
			NoSuchEmailOTPException {

		return getPersistence().findByUserId(userId);
	}

	/**
	 * Returns the mfa email otp where userId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param userId the user ID
	 * @return the matching mfa email otp, or <code>null</code> if a matching mfa email otp could not be found
	 */
	public static MFAEmailOTP fetchByUserId(long userId) {
		return getPersistence().fetchByUserId(userId);
	}

	/**
	 * Returns the mfa email otp where userId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param userId the user ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching mfa email otp, or <code>null</code> if a matching mfa email otp could not be found
	 */
	public static MFAEmailOTP fetchByUserId(
		long userId, boolean useFinderCache) {

		return getPersistence().fetchByUserId(userId, useFinderCache);
	}

	/**
	 * Removes the mfa email otp where userId = &#63; from the database.
	 *
	 * @param userId the user ID
	 * @return the mfa email otp that was removed
	 */
	public static MFAEmailOTP removeByUserId(long userId)
		throws com.liferay.multi.factor.authentication.email.otp.exception.
			NoSuchEmailOTPException {

		return getPersistence().removeByUserId(userId);
	}

	/**
	 * Returns the number of mfa email otps where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @return the number of matching mfa email otps
	 */
	public static int countByUserId(long userId) {
		return getPersistence().countByUserId(userId);
	}

	/**
	 * Caches the mfa email otp in the entity cache if it is enabled.
	 *
	 * @param mfaEmailOTP the mfa email otp
	 */
	public static void cacheResult(MFAEmailOTP mfaEmailOTP) {
		getPersistence().cacheResult(mfaEmailOTP);
	}

	/**
	 * Caches the mfa email otps in the entity cache if it is enabled.
	 *
	 * @param mfaEmailOTPs the mfa email otps
	 */
	public static void cacheResult(List<MFAEmailOTP> mfaEmailOTPs) {
		getPersistence().cacheResult(mfaEmailOTPs);
	}

	/**
	 * Creates a new mfa email otp with the primary key. Does not add the mfa email otp to the database.
	 *
	 * @param emailOTPId the primary key for the new mfa email otp
	 * @return the new mfa email otp
	 */
	public static MFAEmailOTP create(long emailOTPId) {
		return getPersistence().create(emailOTPId);
	}

	/**
	 * Removes the mfa email otp with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param emailOTPId the primary key of the mfa email otp
	 * @return the mfa email otp that was removed
	 * @throws NoSuchEmailOTPException if a mfa email otp with the primary key could not be found
	 */
	public static MFAEmailOTP remove(long emailOTPId)
		throws com.liferay.multi.factor.authentication.email.otp.exception.
			NoSuchEmailOTPException {

		return getPersistence().remove(emailOTPId);
	}

	public static MFAEmailOTP updateImpl(MFAEmailOTP mfaEmailOTP) {
		return getPersistence().updateImpl(mfaEmailOTP);
	}

	/**
	 * Returns the mfa email otp with the primary key or throws a <code>NoSuchEmailOTPException</code> if it could not be found.
	 *
	 * @param emailOTPId the primary key of the mfa email otp
	 * @return the mfa email otp
	 * @throws NoSuchEmailOTPException if a mfa email otp with the primary key could not be found
	 */
	public static MFAEmailOTP findByPrimaryKey(long emailOTPId)
		throws com.liferay.multi.factor.authentication.email.otp.exception.
			NoSuchEmailOTPException {

		return getPersistence().findByPrimaryKey(emailOTPId);
	}

	/**
	 * Returns the mfa email otp with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param emailOTPId the primary key of the mfa email otp
	 * @return the mfa email otp, or <code>null</code> if a mfa email otp with the primary key could not be found
	 */
	public static MFAEmailOTP fetchByPrimaryKey(long emailOTPId) {
		return getPersistence().fetchByPrimaryKey(emailOTPId);
	}

	/**
	 * Returns all the mfa email otps.
	 *
	 * @return the mfa email otps
	 */
	public static List<MFAEmailOTP> findAll() {
		return getPersistence().findAll();
	}

	/**
	 * Returns a range of all the mfa email otps.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>MFAEmailOTPModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of mfa email otps
	 * @param end the upper bound of the range of mfa email otps (not inclusive)
	 * @return the range of mfa email otps
	 */
	public static List<MFAEmailOTP> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
	}

	/**
	 * Returns an ordered range of all the mfa email otps.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>MFAEmailOTPModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of mfa email otps
	 * @param end the upper bound of the range of mfa email otps (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of mfa email otps
	 */
	public static List<MFAEmailOTP> findAll(
		int start, int end, OrderByComparator<MFAEmailOTP> orderByComparator) {

		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the mfa email otps.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>MFAEmailOTPModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of mfa email otps
	 * @param end the upper bound of the range of mfa email otps (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of mfa email otps
	 */
	public static List<MFAEmailOTP> findAll(
		int start, int end, OrderByComparator<MFAEmailOTP> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findAll(
			start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Removes all the mfa email otps from the database.
	 */
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	 * Returns the number of mfa email otps.
	 *
	 * @return the number of mfa email otps
	 */
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static MFAEmailOTPPersistence getPersistence() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<MFAEmailOTPPersistence, MFAEmailOTPPersistence> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(MFAEmailOTPPersistence.class);

		ServiceTracker<MFAEmailOTPPersistence, MFAEmailOTPPersistence>
			serviceTracker =
				new ServiceTracker
					<MFAEmailOTPPersistence, MFAEmailOTPPersistence>(
						bundle.getBundleContext(), MFAEmailOTPPersistence.class,
						null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}