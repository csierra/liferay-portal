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

package com.liferay.multi.factor.authentication.email.otp.service;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the local service utility for MFAEmailOTP. This utility wraps
 * <code>com.liferay.multi.factor.authentication.email.otp.service.impl.MFAEmailOTPLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Arthur Chan
 * @see MFAEmailOTPLocalService
 * @generated
 */
public class MFAEmailOTPLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.multi.factor.authentication.email.otp.service.impl.MFAEmailOTPLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static
		com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP
				addEmailOTP(long userId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addEmailOTP(userId);
	}

	/**
	 * Adds the mfa email otp to the database. Also notifies the appropriate model listeners.
	 *
	 * @param mfaEmailOTP the mfa email otp
	 * @return the mfa email otp that was added
	 */
	public static
		com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP
			addMFAEmailOTP(
				com.liferay.multi.factor.authentication.email.otp.model.
					MFAEmailOTP mfaEmailOTP) {

		return getService().addMFAEmailOTP(mfaEmailOTP);
	}

	/**
	 * Creates a new mfa email otp with the primary key. Does not add the mfa email otp to the database.
	 *
	 * @param emailOTPId the primary key for the new mfa email otp
	 * @return the new mfa email otp
	 */
	public static
		com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP
			createMFAEmailOTP(long emailOTPId) {

		return getService().createMFAEmailOTP(emailOTPId);
	}

	/**
	 * Deletes the mfa email otp with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param emailOTPId the primary key of the mfa email otp
	 * @return the mfa email otp that was removed
	 * @throws PortalException if a mfa email otp with the primary key could not be found
	 */
	public static
		com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP
				deleteMFAEmailOTP(long emailOTPId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteMFAEmailOTP(emailOTPId);
	}

	/**
	 * Deletes the mfa email otp from the database. Also notifies the appropriate model listeners.
	 *
	 * @param mfaEmailOTP the mfa email otp
	 * @return the mfa email otp that was removed
	 */
	public static
		com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP
			deleteMFAEmailOTP(
				com.liferay.multi.factor.authentication.email.otp.model.
					MFAEmailOTP mfaEmailOTP) {

		return getService().deleteMFAEmailOTP(mfaEmailOTP);
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.multi.factor.authentication.email.otp.model.impl.MFAEmailOTPModelImpl</code>.
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.multi.factor.authentication.email.otp.model.impl.MFAEmailOTPModelImpl</code>.
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
		com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP
			fetchEmailOTPByUserId(long userId) {

		return getService().fetchEmailOTPByUserId(userId);
	}

	public static
		com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP
			fetchMFAEmailOTP(long emailOTPId) {

		return getService().fetchMFAEmailOTP(emailOTPId);
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
	 * Returns the mfa email otp with the primary key.
	 *
	 * @param emailOTPId the primary key of the mfa email otp
	 * @return the mfa email otp
	 * @throws PortalException if a mfa email otp with the primary key could not be found
	 */
	public static
		com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP
				getMFAEmailOTP(long emailOTPId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getMFAEmailOTP(emailOTPId);
	}

	/**
	 * Returns a range of all the mfa email otps.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.multi.factor.authentication.email.otp.model.impl.MFAEmailOTPModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of mfa email otps
	 * @param end the upper bound of the range of mfa email otps (not inclusive)
	 * @return the range of mfa email otps
	 */
	public static java.util.List
		<com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP>
			getMFAEmailOTPs(int start, int end) {

		return getService().getMFAEmailOTPs(start, end);
	}

	/**
	 * Returns the number of mfa email otps.
	 *
	 * @return the number of mfa email otps
	 */
	public static int getMFAEmailOTPsCount() {
		return getService().getMFAEmailOTPsCount();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static com.liferay.portal.kernel.model.PersistedModel
			getPersistedModel(java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getPersistedModel(primaryKeyObj);
	}

	public static
		com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP
				resetFailedAttempts(long userId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().resetFailedAttempts(userId);
	}

	public static
		com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP
				updateAttempts(long userId, String ip, boolean success)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateAttempts(userId, ip, success);
	}

	/**
	 * Updates the mfa email otp in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param mfaEmailOTP the mfa email otp
	 * @return the mfa email otp that was updated
	 */
	public static
		com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP
			updateMFAEmailOTP(
				com.liferay.multi.factor.authentication.email.otp.model.
					MFAEmailOTP mfaEmailOTP) {

		return getService().updateMFAEmailOTP(mfaEmailOTP);
	}

	public static MFAEmailOTPLocalService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<MFAEmailOTPLocalService, MFAEmailOTPLocalService> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(MFAEmailOTPLocalService.class);

		ServiceTracker<MFAEmailOTPLocalService, MFAEmailOTPLocalService>
			serviceTracker =
				new ServiceTracker
					<MFAEmailOTPLocalService, MFAEmailOTPLocalService>(
						bundle.getBundleContext(),
						MFAEmailOTPLocalService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}