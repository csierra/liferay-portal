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

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link MFAEmailOTPLocalService}.
 *
 * @author Arthur Chan
 * @see MFAEmailOTPLocalService
 * @generated
 */
public class MFAEmailOTPLocalServiceWrapper
	implements MFAEmailOTPLocalService,
			   ServiceWrapper<MFAEmailOTPLocalService> {

	public MFAEmailOTPLocalServiceWrapper(
		MFAEmailOTPLocalService mfaEmailOTPLocalService) {

		_mfaEmailOTPLocalService = mfaEmailOTPLocalService;
	}

	@Override
	public com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP
			addEmailOTP(long userId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _mfaEmailOTPLocalService.addEmailOTP(userId);
	}

	/**
	 * Adds the mfa email otp to the database. Also notifies the appropriate model listeners.
	 *
	 * @param mfaEmailOTP the mfa email otp
	 * @return the mfa email otp that was added
	 */
	@Override
	public com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP
		addMFAEmailOTP(
			com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP
				mfaEmailOTP) {

		return _mfaEmailOTPLocalService.addMFAEmailOTP(mfaEmailOTP);
	}

	/**
	 * Creates a new mfa email otp with the primary key. Does not add the mfa email otp to the database.
	 *
	 * @param emailOTPId the primary key for the new mfa email otp
	 * @return the new mfa email otp
	 */
	@Override
	public com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP
		createMFAEmailOTP(long emailOTPId) {

		return _mfaEmailOTPLocalService.createMFAEmailOTP(emailOTPId);
	}

	/**
	 * Deletes the mfa email otp with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param emailOTPId the primary key of the mfa email otp
	 * @return the mfa email otp that was removed
	 * @throws PortalException if a mfa email otp with the primary key could not be found
	 */
	@Override
	public com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP
			deleteMFAEmailOTP(long emailOTPId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _mfaEmailOTPLocalService.deleteMFAEmailOTP(emailOTPId);
	}

	/**
	 * Deletes the mfa email otp from the database. Also notifies the appropriate model listeners.
	 *
	 * @param mfaEmailOTP the mfa email otp
	 * @return the mfa email otp that was removed
	 */
	@Override
	public com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP
		deleteMFAEmailOTP(
			com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP
				mfaEmailOTP) {

		return _mfaEmailOTPLocalService.deleteMFAEmailOTP(mfaEmailOTP);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
			com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _mfaEmailOTPLocalService.deletePersistedModel(persistedModel);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _mfaEmailOTPLocalService.dynamicQuery();
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

		return _mfaEmailOTPLocalService.dynamicQuery(dynamicQuery);
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
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return _mfaEmailOTPLocalService.dynamicQuery(dynamicQuery, start, end);
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
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return _mfaEmailOTPLocalService.dynamicQuery(
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

		return _mfaEmailOTPLocalService.dynamicQueryCount(dynamicQuery);
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

		return _mfaEmailOTPLocalService.dynamicQueryCount(
			dynamicQuery, projection);
	}

	@Override
	public com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP
		fetchEmailOTPByUserId(long userId) {

		return _mfaEmailOTPLocalService.fetchEmailOTPByUserId(userId);
	}

	@Override
	public com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP
		fetchMFAEmailOTP(long emailOTPId) {

		return _mfaEmailOTPLocalService.fetchMFAEmailOTP(emailOTPId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return _mfaEmailOTPLocalService.getActionableDynamicQuery();
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		return _mfaEmailOTPLocalService.getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the mfa email otp with the primary key.
	 *
	 * @param emailOTPId the primary key of the mfa email otp
	 * @return the mfa email otp
	 * @throws PortalException if a mfa email otp with the primary key could not be found
	 */
	@Override
	public com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP
			getMFAEmailOTP(long emailOTPId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _mfaEmailOTPLocalService.getMFAEmailOTP(emailOTPId);
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
	@Override
	public java.util.List
		<com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP>
			getMFAEmailOTPs(int start, int end) {

		return _mfaEmailOTPLocalService.getMFAEmailOTPs(start, end);
	}

	/**
	 * Returns the number of mfa email otps.
	 *
	 * @return the number of mfa email otps
	 */
	@Override
	public int getMFAEmailOTPsCount() {
		return _mfaEmailOTPLocalService.getMFAEmailOTPsCount();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _mfaEmailOTPLocalService.getOSGiServiceIdentifier();
	}

	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _mfaEmailOTPLocalService.getPersistedModel(primaryKeyObj);
	}

	@Override
	public com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP
			resetFailedAttempts(long userId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _mfaEmailOTPLocalService.resetFailedAttempts(userId);
	}

	@Override
	public com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP
			updateAttempts(long userId, String ip, boolean success)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _mfaEmailOTPLocalService.updateAttempts(userId, ip, success);
	}

	/**
	 * Updates the mfa email otp in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param mfaEmailOTP the mfa email otp
	 * @return the mfa email otp that was updated
	 */
	@Override
	public com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP
		updateMFAEmailOTP(
			com.liferay.multi.factor.authentication.email.otp.model.MFAEmailOTP
				mfaEmailOTP) {

		return _mfaEmailOTPLocalService.updateMFAEmailOTP(mfaEmailOTP);
	}

	@Override
	public MFAEmailOTPLocalService getWrappedService() {
		return _mfaEmailOTPLocalService;
	}

	@Override
	public void setWrappedService(
		MFAEmailOTPLocalService mfaEmailOTPLocalService) {

		_mfaEmailOTPLocalService = mfaEmailOTPLocalService;
	}

	private MFAEmailOTPLocalService _mfaEmailOTPLocalService;

}