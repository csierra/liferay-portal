/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.security.password.service;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the local service utility for PasswordHashProvider. This utility wraps
 * <code>com.liferay.portal.security.password.service.impl.PasswordHashProviderLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Arthur Chan
 * @see PasswordHashProviderLocalService
 * @generated
 */
public class PasswordHashProviderLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.portal.security.password.service.impl.PasswordHashProviderLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * Adds the password hash provider to the database. Also notifies the appropriate model listeners.
	 *
	 * @param passwordHashProvider the password hash provider
	 * @return the password hash provider that was added
	 */
	public static
		com.liferay.portal.security.password.model.PasswordHashProvider
			addPasswordHashProvider(
				com.liferay.portal.security.password.model.PasswordHashProvider
					passwordHashProvider) {

		return getService().addPasswordHashProvider(passwordHashProvider);
	}

	/**
	 * Creates a new password hash provider with the primary key. Does not add the password hash provider to the database.
	 *
	 * @param passwordHashProviderId the primary key for the new password hash provider
	 * @return the new password hash provider
	 */
	public static
		com.liferay.portal.security.password.model.PasswordHashProvider
			createPasswordHashProvider(long passwordHashProviderId) {

		return getService().createPasswordHashProvider(passwordHashProviderId);
	}

	/**
	 * Deletes the password hash provider with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param passwordHashProviderId the primary key of the password hash provider
	 * @return the password hash provider that was removed
	 * @throws PortalException if a password hash provider with the primary key could not be found
	 */
	public static
		com.liferay.portal.security.password.model.PasswordHashProvider
				deletePasswordHashProvider(long passwordHashProviderId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deletePasswordHashProvider(passwordHashProviderId);
	}

	/**
	 * Deletes the password hash provider from the database. Also notifies the appropriate model listeners.
	 *
	 * @param passwordHashProvider the password hash provider
	 * @return the password hash provider that was removed
	 */
	public static
		com.liferay.portal.security.password.model.PasswordHashProvider
			deletePasswordHashProvider(
				com.liferay.portal.security.password.model.PasswordHashProvider
					passwordHashProvider) {

		return getService().deletePasswordHashProvider(passwordHashProvider);
	}

	/**
	 * Delete all password hash providers before a date
	 *
	 * @param date the given date
	 */
	public static void deletePasswordHashProvidersBeforeDate(
		java.util.Date date) {

		getService().deletePasswordHashProvidersBeforeDate(date);
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.portal.security.password.model.impl.PasswordHashProviderModelImpl</code>.
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.portal.security.password.model.impl.PasswordHashProviderModelImpl</code>.
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
		com.liferay.portal.security.password.model.PasswordHashProvider
			fetchPasswordHashProvider(long passwordHashProviderId) {

		return getService().fetchPasswordHashProvider(passwordHashProviderId);
	}

	/**
	 * Returns the password hash provider with the matching UUID and company.
	 *
	 * @param uuid the password hash provider's UUID
	 * @param companyId the primary key of the company
	 * @return the matching password hash provider, or <code>null</code> if a matching password hash provider could not be found
	 */
	public static
		com.liferay.portal.security.password.model.PasswordHashProvider
			fetchPasswordHashProviderByUuidAndCompanyId(
				String uuid, long companyId) {

		return getService().fetchPasswordHashProviderByUuidAndCompanyId(
			uuid, companyId);
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
	 * Get the latest password hash provider ordered by created date
	 *
	 * @return the latest password hash provider ordered by created date
	 */
	public static
		com.liferay.portal.security.password.model.PasswordHashProvider
				getLastPasswordHashProvider()
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getLastPasswordHashProvider();
	}

	/**
	 * Get the latest password hash provider before a date, ordered by created date
	 *
	 * @param date the given date
	 * @return the latest password hash provider before a date,  ordered by created date
	 */
	public static
		com.liferay.portal.security.password.model.PasswordHashProvider
				getLastPasswordHashProviderBeforeDate(java.util.Date date)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getLastPasswordHashProviderBeforeDate(date);
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
	 * Returns the password hash provider with the primary key.
	 *
	 * @param passwordHashProviderId the primary key of the password hash provider
	 * @return the password hash provider
	 * @throws PortalException if a password hash provider with the primary key could not be found
	 */
	public static
		com.liferay.portal.security.password.model.PasswordHashProvider
				getPasswordHashProvider(long passwordHashProviderId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getPasswordHashProvider(passwordHashProviderId);
	}

	/**
	 * Returns the password hash provider with the matching UUID and company.
	 *
	 * @param uuid the password hash provider's UUID
	 * @param companyId the primary key of the company
	 * @return the matching password hash provider
	 * @throws PortalException if a matching password hash provider could not be found
	 */
	public static
		com.liferay.portal.security.password.model.PasswordHashProvider
				getPasswordHashProviderByUuidAndCompanyId(
					String uuid, long companyId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getPasswordHashProviderByUuidAndCompanyId(
			uuid, companyId);
	}

	/**
	 * Returns a range of all the password hash providers.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.portal.security.password.model.impl.PasswordHashProviderModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of password hash providers
	 * @param end the upper bound of the range of password hash providers (not inclusive)
	 * @return the range of password hash providers
	 */
	public static java.util.List
		<com.liferay.portal.security.password.model.PasswordHashProvider>
			getPasswordHashProviders(int start, int end) {

		return getService().getPasswordHashProviders(start, end);
	}

	/**
	 * Returns the number of password hash providers.
	 *
	 * @return the number of password hash providers
	 */
	public static int getPasswordHashProvidersCount() {
		return getService().getPasswordHashProvidersCount();
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
	 * Updates the password hash provider in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param passwordHashProvider the password hash provider
	 * @return the password hash provider that was updated
	 */
	public static
		com.liferay.portal.security.password.model.PasswordHashProvider
			updatePasswordHashProvider(
				com.liferay.portal.security.password.model.PasswordHashProvider
					passwordHashProvider) {

		return getService().updatePasswordHashProvider(passwordHashProvider);
	}

	public static PasswordHashProviderLocalService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<PasswordHashProviderLocalService, PasswordHashProviderLocalService>
			_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			PasswordHashProviderLocalService.class);

		ServiceTracker
			<PasswordHashProviderLocalService, PasswordHashProviderLocalService>
				serviceTracker =
					new ServiceTracker
						<PasswordHashProviderLocalService,
						 PasswordHashProviderLocalService>(
							 bundle.getBundleContext(),
							 PasswordHashProviderLocalService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}