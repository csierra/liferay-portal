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

package com.liferay.portal.security.password.service.persistence;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.security.password.model.PasswordHashProvider;

import java.io.Serializable;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The persistence utility for the password hash provider service. This utility wraps <code>com.liferay.portal.security.password.service.persistence.impl.PasswordHashProviderPersistenceImpl</code> and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Arthur Chan
 * @see PasswordHashProviderPersistence
 * @generated
 */
public class PasswordHashProviderUtil {

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
	public static void clearCache(PasswordHashProvider passwordHashProvider) {
		getPersistence().clearCache(passwordHashProvider);
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
	public static Map<Serializable, PasswordHashProvider> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<PasswordHashProvider> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {

		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<PasswordHashProvider> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<PasswordHashProvider> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<PasswordHashProvider> orderByComparator) {

		return getPersistence().findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static PasswordHashProvider update(
		PasswordHashProvider passwordHashProvider) {

		return getPersistence().update(passwordHashProvider);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static PasswordHashProvider update(
		PasswordHashProvider passwordHashProvider,
		ServiceContext serviceContext) {

		return getPersistence().update(passwordHashProvider, serviceContext);
	}

	/**
	 * Returns all the password hash providers where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching password hash providers
	 */
	public static List<PasswordHashProvider> findByUuid(String uuid) {
		return getPersistence().findByUuid(uuid);
	}

	/**
	 * Returns a range of all the password hash providers where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordHashProviderModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of password hash providers
	 * @param end the upper bound of the range of password hash providers (not inclusive)
	 * @return the range of matching password hash providers
	 */
	public static List<PasswordHashProvider> findByUuid(
		String uuid, int start, int end) {

		return getPersistence().findByUuid(uuid, start, end);
	}

	/**
	 * Returns an ordered range of all the password hash providers where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordHashProviderModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of password hash providers
	 * @param end the upper bound of the range of password hash providers (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching password hash providers
	 */
	public static List<PasswordHashProvider> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<PasswordHashProvider> orderByComparator) {

		return getPersistence().findByUuid(uuid, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the password hash providers where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordHashProviderModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of password hash providers
	 * @param end the upper bound of the range of password hash providers (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching password hash providers
	 */
	public static List<PasswordHashProvider> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<PasswordHashProvider> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByUuid(
			uuid, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first password hash provider in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching password hash provider
	 * @throws NoSuchHashProviderException if a matching password hash provider could not be found
	 */
	public static PasswordHashProvider findByUuid_First(
			String uuid,
			OrderByComparator<PasswordHashProvider> orderByComparator)
		throws com.liferay.portal.security.password.exception.
			NoSuchHashProviderException {

		return getPersistence().findByUuid_First(uuid, orderByComparator);
	}

	/**
	 * Returns the first password hash provider in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching password hash provider, or <code>null</code> if a matching password hash provider could not be found
	 */
	public static PasswordHashProvider fetchByUuid_First(
		String uuid,
		OrderByComparator<PasswordHashProvider> orderByComparator) {

		return getPersistence().fetchByUuid_First(uuid, orderByComparator);
	}

	/**
	 * Returns the last password hash provider in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching password hash provider
	 * @throws NoSuchHashProviderException if a matching password hash provider could not be found
	 */
	public static PasswordHashProvider findByUuid_Last(
			String uuid,
			OrderByComparator<PasswordHashProvider> orderByComparator)
		throws com.liferay.portal.security.password.exception.
			NoSuchHashProviderException {

		return getPersistence().findByUuid_Last(uuid, orderByComparator);
	}

	/**
	 * Returns the last password hash provider in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching password hash provider, or <code>null</code> if a matching password hash provider could not be found
	 */
	public static PasswordHashProvider fetchByUuid_Last(
		String uuid,
		OrderByComparator<PasswordHashProvider> orderByComparator) {

		return getPersistence().fetchByUuid_Last(uuid, orderByComparator);
	}

	/**
	 * Returns the password hash providers before and after the current password hash provider in the ordered set where uuid = &#63;.
	 *
	 * @param passwordHashProviderId the primary key of the current password hash provider
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next password hash provider
	 * @throws NoSuchHashProviderException if a password hash provider with the primary key could not be found
	 */
	public static PasswordHashProvider[] findByUuid_PrevAndNext(
			long passwordHashProviderId, String uuid,
			OrderByComparator<PasswordHashProvider> orderByComparator)
		throws com.liferay.portal.security.password.exception.
			NoSuchHashProviderException {

		return getPersistence().findByUuid_PrevAndNext(
			passwordHashProviderId, uuid, orderByComparator);
	}

	/**
	 * Removes all the password hash providers where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	public static void removeByUuid(String uuid) {
		getPersistence().removeByUuid(uuid);
	}

	/**
	 * Returns the number of password hash providers where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching password hash providers
	 */
	public static int countByUuid(String uuid) {
		return getPersistence().countByUuid(uuid);
	}

	/**
	 * Returns all the password hash providers where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching password hash providers
	 */
	public static List<PasswordHashProvider> findByUuid_C(
		String uuid, long companyId) {

		return getPersistence().findByUuid_C(uuid, companyId);
	}

	/**
	 * Returns a range of all the password hash providers where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordHashProviderModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of password hash providers
	 * @param end the upper bound of the range of password hash providers (not inclusive)
	 * @return the range of matching password hash providers
	 */
	public static List<PasswordHashProvider> findByUuid_C(
		String uuid, long companyId, int start, int end) {

		return getPersistence().findByUuid_C(uuid, companyId, start, end);
	}

	/**
	 * Returns an ordered range of all the password hash providers where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordHashProviderModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of password hash providers
	 * @param end the upper bound of the range of password hash providers (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching password hash providers
	 */
	public static List<PasswordHashProvider> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<PasswordHashProvider> orderByComparator) {

		return getPersistence().findByUuid_C(
			uuid, companyId, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the password hash providers where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordHashProviderModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of password hash providers
	 * @param end the upper bound of the range of password hash providers (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching password hash providers
	 */
	public static List<PasswordHashProvider> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<PasswordHashProvider> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByUuid_C(
			uuid, companyId, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first password hash provider in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching password hash provider
	 * @throws NoSuchHashProviderException if a matching password hash provider could not be found
	 */
	public static PasswordHashProvider findByUuid_C_First(
			String uuid, long companyId,
			OrderByComparator<PasswordHashProvider> orderByComparator)
		throws com.liferay.portal.security.password.exception.
			NoSuchHashProviderException {

		return getPersistence().findByUuid_C_First(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the first password hash provider in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching password hash provider, or <code>null</code> if a matching password hash provider could not be found
	 */
	public static PasswordHashProvider fetchByUuid_C_First(
		String uuid, long companyId,
		OrderByComparator<PasswordHashProvider> orderByComparator) {

		return getPersistence().fetchByUuid_C_First(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the last password hash provider in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching password hash provider
	 * @throws NoSuchHashProviderException if a matching password hash provider could not be found
	 */
	public static PasswordHashProvider findByUuid_C_Last(
			String uuid, long companyId,
			OrderByComparator<PasswordHashProvider> orderByComparator)
		throws com.liferay.portal.security.password.exception.
			NoSuchHashProviderException {

		return getPersistence().findByUuid_C_Last(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the last password hash provider in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching password hash provider, or <code>null</code> if a matching password hash provider could not be found
	 */
	public static PasswordHashProvider fetchByUuid_C_Last(
		String uuid, long companyId,
		OrderByComparator<PasswordHashProvider> orderByComparator) {

		return getPersistence().fetchByUuid_C_Last(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the password hash providers before and after the current password hash provider in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param passwordHashProviderId the primary key of the current password hash provider
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next password hash provider
	 * @throws NoSuchHashProviderException if a password hash provider with the primary key could not be found
	 */
	public static PasswordHashProvider[] findByUuid_C_PrevAndNext(
			long passwordHashProviderId, String uuid, long companyId,
			OrderByComparator<PasswordHashProvider> orderByComparator)
		throws com.liferay.portal.security.password.exception.
			NoSuchHashProviderException {

		return getPersistence().findByUuid_C_PrevAndNext(
			passwordHashProviderId, uuid, companyId, orderByComparator);
	}

	/**
	 * Removes all the password hash providers where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	public static void removeByUuid_C(String uuid, long companyId) {
		getPersistence().removeByUuid_C(uuid, companyId);
	}

	/**
	 * Returns the number of password hash providers where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching password hash providers
	 */
	public static int countByUuid_C(String uuid, long companyId) {
		return getPersistence().countByUuid_C(uuid, companyId);
	}

	/**
	 * Returns all the password hash providers where createDate &lt; &#63;.
	 *
	 * @param createDate the create date
	 * @return the matching password hash providers
	 */
	public static List<PasswordHashProvider> findByLtCreateDate(
		Date createDate) {

		return getPersistence().findByLtCreateDate(createDate);
	}

	/**
	 * Returns a range of all the password hash providers where createDate &lt; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordHashProviderModelImpl</code>.
	 * </p>
	 *
	 * @param createDate the create date
	 * @param start the lower bound of the range of password hash providers
	 * @param end the upper bound of the range of password hash providers (not inclusive)
	 * @return the range of matching password hash providers
	 */
	public static List<PasswordHashProvider> findByLtCreateDate(
		Date createDate, int start, int end) {

		return getPersistence().findByLtCreateDate(createDate, start, end);
	}

	/**
	 * Returns an ordered range of all the password hash providers where createDate &lt; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordHashProviderModelImpl</code>.
	 * </p>
	 *
	 * @param createDate the create date
	 * @param start the lower bound of the range of password hash providers
	 * @param end the upper bound of the range of password hash providers (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching password hash providers
	 */
	public static List<PasswordHashProvider> findByLtCreateDate(
		Date createDate, int start, int end,
		OrderByComparator<PasswordHashProvider> orderByComparator) {

		return getPersistence().findByLtCreateDate(
			createDate, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the password hash providers where createDate &lt; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordHashProviderModelImpl</code>.
	 * </p>
	 *
	 * @param createDate the create date
	 * @param start the lower bound of the range of password hash providers
	 * @param end the upper bound of the range of password hash providers (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching password hash providers
	 */
	public static List<PasswordHashProvider> findByLtCreateDate(
		Date createDate, int start, int end,
		OrderByComparator<PasswordHashProvider> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByLtCreateDate(
			createDate, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first password hash provider in the ordered set where createDate &lt; &#63;.
	 *
	 * @param createDate the create date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching password hash provider
	 * @throws NoSuchHashProviderException if a matching password hash provider could not be found
	 */
	public static PasswordHashProvider findByLtCreateDate_First(
			Date createDate,
			OrderByComparator<PasswordHashProvider> orderByComparator)
		throws com.liferay.portal.security.password.exception.
			NoSuchHashProviderException {

		return getPersistence().findByLtCreateDate_First(
			createDate, orderByComparator);
	}

	/**
	 * Returns the first password hash provider in the ordered set where createDate &lt; &#63;.
	 *
	 * @param createDate the create date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching password hash provider, or <code>null</code> if a matching password hash provider could not be found
	 */
	public static PasswordHashProvider fetchByLtCreateDate_First(
		Date createDate,
		OrderByComparator<PasswordHashProvider> orderByComparator) {

		return getPersistence().fetchByLtCreateDate_First(
			createDate, orderByComparator);
	}

	/**
	 * Returns the last password hash provider in the ordered set where createDate &lt; &#63;.
	 *
	 * @param createDate the create date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching password hash provider
	 * @throws NoSuchHashProviderException if a matching password hash provider could not be found
	 */
	public static PasswordHashProvider findByLtCreateDate_Last(
			Date createDate,
			OrderByComparator<PasswordHashProvider> orderByComparator)
		throws com.liferay.portal.security.password.exception.
			NoSuchHashProviderException {

		return getPersistence().findByLtCreateDate_Last(
			createDate, orderByComparator);
	}

	/**
	 * Returns the last password hash provider in the ordered set where createDate &lt; &#63;.
	 *
	 * @param createDate the create date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching password hash provider, or <code>null</code> if a matching password hash provider could not be found
	 */
	public static PasswordHashProvider fetchByLtCreateDate_Last(
		Date createDate,
		OrderByComparator<PasswordHashProvider> orderByComparator) {

		return getPersistence().fetchByLtCreateDate_Last(
			createDate, orderByComparator);
	}

	/**
	 * Returns the password hash providers before and after the current password hash provider in the ordered set where createDate &lt; &#63;.
	 *
	 * @param passwordHashProviderId the primary key of the current password hash provider
	 * @param createDate the create date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next password hash provider
	 * @throws NoSuchHashProviderException if a password hash provider with the primary key could not be found
	 */
	public static PasswordHashProvider[] findByLtCreateDate_PrevAndNext(
			long passwordHashProviderId, Date createDate,
			OrderByComparator<PasswordHashProvider> orderByComparator)
		throws com.liferay.portal.security.password.exception.
			NoSuchHashProviderException {

		return getPersistence().findByLtCreateDate_PrevAndNext(
			passwordHashProviderId, createDate, orderByComparator);
	}

	/**
	 * Removes all the password hash providers where createDate &lt; &#63; from the database.
	 *
	 * @param createDate the create date
	 */
	public static void removeByLtCreateDate(Date createDate) {
		getPersistence().removeByLtCreateDate(createDate);
	}

	/**
	 * Returns the number of password hash providers where createDate &lt; &#63;.
	 *
	 * @param createDate the create date
	 * @return the number of matching password hash providers
	 */
	public static int countByLtCreateDate(Date createDate) {
		return getPersistence().countByLtCreateDate(createDate);
	}

	/**
	 * Caches the password hash provider in the entity cache if it is enabled.
	 *
	 * @param passwordHashProvider the password hash provider
	 */
	public static void cacheResult(PasswordHashProvider passwordHashProvider) {
		getPersistence().cacheResult(passwordHashProvider);
	}

	/**
	 * Caches the password hash providers in the entity cache if it is enabled.
	 *
	 * @param passwordHashProviders the password hash providers
	 */
	public static void cacheResult(
		List<PasswordHashProvider> passwordHashProviders) {

		getPersistence().cacheResult(passwordHashProviders);
	}

	/**
	 * Creates a new password hash provider with the primary key. Does not add the password hash provider to the database.
	 *
	 * @param passwordHashProviderId the primary key for the new password hash provider
	 * @return the new password hash provider
	 */
	public static PasswordHashProvider create(long passwordHashProviderId) {
		return getPersistence().create(passwordHashProviderId);
	}

	/**
	 * Removes the password hash provider with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param passwordHashProviderId the primary key of the password hash provider
	 * @return the password hash provider that was removed
	 * @throws NoSuchHashProviderException if a password hash provider with the primary key could not be found
	 */
	public static PasswordHashProvider remove(long passwordHashProviderId)
		throws com.liferay.portal.security.password.exception.
			NoSuchHashProviderException {

		return getPersistence().remove(passwordHashProviderId);
	}

	public static PasswordHashProvider updateImpl(
		PasswordHashProvider passwordHashProvider) {

		return getPersistence().updateImpl(passwordHashProvider);
	}

	/**
	 * Returns the password hash provider with the primary key or throws a <code>NoSuchHashProviderException</code> if it could not be found.
	 *
	 * @param passwordHashProviderId the primary key of the password hash provider
	 * @return the password hash provider
	 * @throws NoSuchHashProviderException if a password hash provider with the primary key could not be found
	 */
	public static PasswordHashProvider findByPrimaryKey(
			long passwordHashProviderId)
		throws com.liferay.portal.security.password.exception.
			NoSuchHashProviderException {

		return getPersistence().findByPrimaryKey(passwordHashProviderId);
	}

	/**
	 * Returns the password hash provider with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param passwordHashProviderId the primary key of the password hash provider
	 * @return the password hash provider, or <code>null</code> if a password hash provider with the primary key could not be found
	 */
	public static PasswordHashProvider fetchByPrimaryKey(
		long passwordHashProviderId) {

		return getPersistence().fetchByPrimaryKey(passwordHashProviderId);
	}

	/**
	 * Returns all the password hash providers.
	 *
	 * @return the password hash providers
	 */
	public static List<PasswordHashProvider> findAll() {
		return getPersistence().findAll();
	}

	/**
	 * Returns a range of all the password hash providers.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordHashProviderModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of password hash providers
	 * @param end the upper bound of the range of password hash providers (not inclusive)
	 * @return the range of password hash providers
	 */
	public static List<PasswordHashProvider> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
	}

	/**
	 * Returns an ordered range of all the password hash providers.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordHashProviderModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of password hash providers
	 * @param end the upper bound of the range of password hash providers (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of password hash providers
	 */
	public static List<PasswordHashProvider> findAll(
		int start, int end,
		OrderByComparator<PasswordHashProvider> orderByComparator) {

		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the password hash providers.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordHashProviderModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of password hash providers
	 * @param end the upper bound of the range of password hash providers (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of password hash providers
	 */
	public static List<PasswordHashProvider> findAll(
		int start, int end,
		OrderByComparator<PasswordHashProvider> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findAll(
			start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Removes all the password hash providers from the database.
	 */
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	 * Returns the number of password hash providers.
	 *
	 * @return the number of password hash providers
	 */
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static PasswordHashProviderPersistence getPersistence() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<PasswordHashProviderPersistence, PasswordHashProviderPersistence>
			_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			PasswordHashProviderPersistence.class);

		ServiceTracker
			<PasswordHashProviderPersistence, PasswordHashProviderPersistence>
				serviceTracker =
					new ServiceTracker
						<PasswordHashProviderPersistence,
						 PasswordHashProviderPersistence>(
							 bundle.getBundleContext(),
							 PasswordHashProviderPersistence.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}