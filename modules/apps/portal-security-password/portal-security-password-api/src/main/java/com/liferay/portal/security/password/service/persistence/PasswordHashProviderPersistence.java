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

import com.liferay.portal.kernel.service.persistence.BasePersistence;
import com.liferay.portal.security.password.exception.NoSuchHashProviderException;
import com.liferay.portal.security.password.model.PasswordHashProvider;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The persistence interface for the password hash provider service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Arthur Chan
 * @see PasswordHashProviderUtil
 * @generated
 */
@ProviderType
public interface PasswordHashProviderPersistence
	extends BasePersistence<PasswordHashProvider> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link PasswordHashProviderUtil} to access the password hash provider persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	 * Returns all the password hash providers where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching password hash providers
	 */
	public java.util.List<PasswordHashProvider> findByUuid(String uuid);

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
	public java.util.List<PasswordHashProvider> findByUuid(
		String uuid, int start, int end);

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
	public java.util.List<PasswordHashProvider> findByUuid(
		String uuid, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<PasswordHashProvider>
			orderByComparator);

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
	public java.util.List<PasswordHashProvider> findByUuid(
		String uuid, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<PasswordHashProvider>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first password hash provider in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching password hash provider
	 * @throws NoSuchHashProviderException if a matching password hash provider could not be found
	 */
	public PasswordHashProvider findByUuid_First(
			String uuid,
			com.liferay.portal.kernel.util.OrderByComparator
				<PasswordHashProvider> orderByComparator)
		throws NoSuchHashProviderException;

	/**
	 * Returns the first password hash provider in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching password hash provider, or <code>null</code> if a matching password hash provider could not be found
	 */
	public PasswordHashProvider fetchByUuid_First(
		String uuid,
		com.liferay.portal.kernel.util.OrderByComparator<PasswordHashProvider>
			orderByComparator);

	/**
	 * Returns the last password hash provider in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching password hash provider
	 * @throws NoSuchHashProviderException if a matching password hash provider could not be found
	 */
	public PasswordHashProvider findByUuid_Last(
			String uuid,
			com.liferay.portal.kernel.util.OrderByComparator
				<PasswordHashProvider> orderByComparator)
		throws NoSuchHashProviderException;

	/**
	 * Returns the last password hash provider in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching password hash provider, or <code>null</code> if a matching password hash provider could not be found
	 */
	public PasswordHashProvider fetchByUuid_Last(
		String uuid,
		com.liferay.portal.kernel.util.OrderByComparator<PasswordHashProvider>
			orderByComparator);

	/**
	 * Returns the password hash providers before and after the current password hash provider in the ordered set where uuid = &#63;.
	 *
	 * @param passwordHashProviderId the primary key of the current password hash provider
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next password hash provider
	 * @throws NoSuchHashProviderException if a password hash provider with the primary key could not be found
	 */
	public PasswordHashProvider[] findByUuid_PrevAndNext(
			long passwordHashProviderId, String uuid,
			com.liferay.portal.kernel.util.OrderByComparator
				<PasswordHashProvider> orderByComparator)
		throws NoSuchHashProviderException;

	/**
	 * Removes all the password hash providers where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	public void removeByUuid(String uuid);

	/**
	 * Returns the number of password hash providers where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching password hash providers
	 */
	public int countByUuid(String uuid);

	/**
	 * Returns all the password hash providers where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching password hash providers
	 */
	public java.util.List<PasswordHashProvider> findByUuid_C(
		String uuid, long companyId);

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
	public java.util.List<PasswordHashProvider> findByUuid_C(
		String uuid, long companyId, int start, int end);

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
	public java.util.List<PasswordHashProvider> findByUuid_C(
		String uuid, long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<PasswordHashProvider>
			orderByComparator);

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
	public java.util.List<PasswordHashProvider> findByUuid_C(
		String uuid, long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<PasswordHashProvider>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first password hash provider in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching password hash provider
	 * @throws NoSuchHashProviderException if a matching password hash provider could not be found
	 */
	public PasswordHashProvider findByUuid_C_First(
			String uuid, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator
				<PasswordHashProvider> orderByComparator)
		throws NoSuchHashProviderException;

	/**
	 * Returns the first password hash provider in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching password hash provider, or <code>null</code> if a matching password hash provider could not be found
	 */
	public PasswordHashProvider fetchByUuid_C_First(
		String uuid, long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<PasswordHashProvider>
			orderByComparator);

	/**
	 * Returns the last password hash provider in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching password hash provider
	 * @throws NoSuchHashProviderException if a matching password hash provider could not be found
	 */
	public PasswordHashProvider findByUuid_C_Last(
			String uuid, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator
				<PasswordHashProvider> orderByComparator)
		throws NoSuchHashProviderException;

	/**
	 * Returns the last password hash provider in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching password hash provider, or <code>null</code> if a matching password hash provider could not be found
	 */
	public PasswordHashProvider fetchByUuid_C_Last(
		String uuid, long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<PasswordHashProvider>
			orderByComparator);

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
	public PasswordHashProvider[] findByUuid_C_PrevAndNext(
			long passwordHashProviderId, String uuid, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator
				<PasswordHashProvider> orderByComparator)
		throws NoSuchHashProviderException;

	/**
	 * Removes all the password hash providers where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	public void removeByUuid_C(String uuid, long companyId);

	/**
	 * Returns the number of password hash providers where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching password hash providers
	 */
	public int countByUuid_C(String uuid, long companyId);

	/**
	 * Returns all the password hash providers where hashProviderName = &#63;.
	 *
	 * @param hashProviderName the hash provider name
	 * @return the matching password hash providers
	 */
	public java.util.List<PasswordHashProvider> findByHashProviderName(
		String hashProviderName);

	/**
	 * Returns a range of all the password hash providers where hashProviderName = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordHashProviderModelImpl</code>.
	 * </p>
	 *
	 * @param hashProviderName the hash provider name
	 * @param start the lower bound of the range of password hash providers
	 * @param end the upper bound of the range of password hash providers (not inclusive)
	 * @return the range of matching password hash providers
	 */
	public java.util.List<PasswordHashProvider> findByHashProviderName(
		String hashProviderName, int start, int end);

	/**
	 * Returns an ordered range of all the password hash providers where hashProviderName = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordHashProviderModelImpl</code>.
	 * </p>
	 *
	 * @param hashProviderName the hash provider name
	 * @param start the lower bound of the range of password hash providers
	 * @param end the upper bound of the range of password hash providers (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching password hash providers
	 */
	public java.util.List<PasswordHashProvider> findByHashProviderName(
		String hashProviderName, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<PasswordHashProvider>
			orderByComparator);

	/**
	 * Returns an ordered range of all the password hash providers where hashProviderName = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>PasswordHashProviderModelImpl</code>.
	 * </p>
	 *
	 * @param hashProviderName the hash provider name
	 * @param start the lower bound of the range of password hash providers
	 * @param end the upper bound of the range of password hash providers (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching password hash providers
	 */
	public java.util.List<PasswordHashProvider> findByHashProviderName(
		String hashProviderName, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<PasswordHashProvider>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first password hash provider in the ordered set where hashProviderName = &#63;.
	 *
	 * @param hashProviderName the hash provider name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching password hash provider
	 * @throws NoSuchHashProviderException if a matching password hash provider could not be found
	 */
	public PasswordHashProvider findByHashProviderName_First(
			String hashProviderName,
			com.liferay.portal.kernel.util.OrderByComparator
				<PasswordHashProvider> orderByComparator)
		throws NoSuchHashProviderException;

	/**
	 * Returns the first password hash provider in the ordered set where hashProviderName = &#63;.
	 *
	 * @param hashProviderName the hash provider name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching password hash provider, or <code>null</code> if a matching password hash provider could not be found
	 */
	public PasswordHashProvider fetchByHashProviderName_First(
		String hashProviderName,
		com.liferay.portal.kernel.util.OrderByComparator<PasswordHashProvider>
			orderByComparator);

	/**
	 * Returns the last password hash provider in the ordered set where hashProviderName = &#63;.
	 *
	 * @param hashProviderName the hash provider name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching password hash provider
	 * @throws NoSuchHashProviderException if a matching password hash provider could not be found
	 */
	public PasswordHashProvider findByHashProviderName_Last(
			String hashProviderName,
			com.liferay.portal.kernel.util.OrderByComparator
				<PasswordHashProvider> orderByComparator)
		throws NoSuchHashProviderException;

	/**
	 * Returns the last password hash provider in the ordered set where hashProviderName = &#63;.
	 *
	 * @param hashProviderName the hash provider name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching password hash provider, or <code>null</code> if a matching password hash provider could not be found
	 */
	public PasswordHashProvider fetchByHashProviderName_Last(
		String hashProviderName,
		com.liferay.portal.kernel.util.OrderByComparator<PasswordHashProvider>
			orderByComparator);

	/**
	 * Returns the password hash providers before and after the current password hash provider in the ordered set where hashProviderName = &#63;.
	 *
	 * @param passwordHashProviderId the primary key of the current password hash provider
	 * @param hashProviderName the hash provider name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next password hash provider
	 * @throws NoSuchHashProviderException if a password hash provider with the primary key could not be found
	 */
	public PasswordHashProvider[] findByHashProviderName_PrevAndNext(
			long passwordHashProviderId, String hashProviderName,
			com.liferay.portal.kernel.util.OrderByComparator
				<PasswordHashProvider> orderByComparator)
		throws NoSuchHashProviderException;

	/**
	 * Removes all the password hash providers where hashProviderName = &#63; from the database.
	 *
	 * @param hashProviderName the hash provider name
	 */
	public void removeByHashProviderName(String hashProviderName);

	/**
	 * Returns the number of password hash providers where hashProviderName = &#63;.
	 *
	 * @param hashProviderName the hash provider name
	 * @return the number of matching password hash providers
	 */
	public int countByHashProviderName(String hashProviderName);

	/**
	 * Returns the password hash provider where hashProviderName = &#63; and hashProviderMeta = &#63; or throws a <code>NoSuchHashProviderException</code> if it could not be found.
	 *
	 * @param hashProviderName the hash provider name
	 * @param hashProviderMeta the hash provider meta
	 * @return the matching password hash provider
	 * @throws NoSuchHashProviderException if a matching password hash provider could not be found
	 */
	public PasswordHashProvider findByN_M(
			String hashProviderName, String hashProviderMeta)
		throws NoSuchHashProviderException;

	/**
	 * Returns the password hash provider where hashProviderName = &#63; and hashProviderMeta = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param hashProviderName the hash provider name
	 * @param hashProviderMeta the hash provider meta
	 * @return the matching password hash provider, or <code>null</code> if a matching password hash provider could not be found
	 */
	public PasswordHashProvider fetchByN_M(
		String hashProviderName, String hashProviderMeta);

	/**
	 * Returns the password hash provider where hashProviderName = &#63; and hashProviderMeta = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param hashProviderName the hash provider name
	 * @param hashProviderMeta the hash provider meta
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching password hash provider, or <code>null</code> if a matching password hash provider could not be found
	 */
	public PasswordHashProvider fetchByN_M(
		String hashProviderName, String hashProviderMeta,
		boolean useFinderCache);

	/**
	 * Removes the password hash provider where hashProviderName = &#63; and hashProviderMeta = &#63; from the database.
	 *
	 * @param hashProviderName the hash provider name
	 * @param hashProviderMeta the hash provider meta
	 * @return the password hash provider that was removed
	 */
	public PasswordHashProvider removeByN_M(
			String hashProviderName, String hashProviderMeta)
		throws NoSuchHashProviderException;

	/**
	 * Returns the number of password hash providers where hashProviderName = &#63; and hashProviderMeta = &#63;.
	 *
	 * @param hashProviderName the hash provider name
	 * @param hashProviderMeta the hash provider meta
	 * @return the number of matching password hash providers
	 */
	public int countByN_M(String hashProviderName, String hashProviderMeta);

	/**
	 * Caches the password hash provider in the entity cache if it is enabled.
	 *
	 * @param passwordHashProvider the password hash provider
	 */
	public void cacheResult(PasswordHashProvider passwordHashProvider);

	/**
	 * Caches the password hash providers in the entity cache if it is enabled.
	 *
	 * @param passwordHashProviders the password hash providers
	 */
	public void cacheResult(
		java.util.List<PasswordHashProvider> passwordHashProviders);

	/**
	 * Creates a new password hash provider with the primary key. Does not add the password hash provider to the database.
	 *
	 * @param passwordHashProviderId the primary key for the new password hash provider
	 * @return the new password hash provider
	 */
	public PasswordHashProvider create(long passwordHashProviderId);

	/**
	 * Removes the password hash provider with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param passwordHashProviderId the primary key of the password hash provider
	 * @return the password hash provider that was removed
	 * @throws NoSuchHashProviderException if a password hash provider with the primary key could not be found
	 */
	public PasswordHashProvider remove(long passwordHashProviderId)
		throws NoSuchHashProviderException;

	public PasswordHashProvider updateImpl(
		PasswordHashProvider passwordHashProvider);

	/**
	 * Returns the password hash provider with the primary key or throws a <code>NoSuchHashProviderException</code> if it could not be found.
	 *
	 * @param passwordHashProviderId the primary key of the password hash provider
	 * @return the password hash provider
	 * @throws NoSuchHashProviderException if a password hash provider with the primary key could not be found
	 */
	public PasswordHashProvider findByPrimaryKey(long passwordHashProviderId)
		throws NoSuchHashProviderException;

	/**
	 * Returns the password hash provider with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param passwordHashProviderId the primary key of the password hash provider
	 * @return the password hash provider, or <code>null</code> if a password hash provider with the primary key could not be found
	 */
	public PasswordHashProvider fetchByPrimaryKey(long passwordHashProviderId);

	/**
	 * Returns all the password hash providers.
	 *
	 * @return the password hash providers
	 */
	public java.util.List<PasswordHashProvider> findAll();

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
	public java.util.List<PasswordHashProvider> findAll(int start, int end);

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
	public java.util.List<PasswordHashProvider> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<PasswordHashProvider>
			orderByComparator);

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
	public java.util.List<PasswordHashProvider> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<PasswordHashProvider>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Removes all the password hash providers from the database.
	 */
	public void removeAll();

	/**
	 * Returns the number of password hash providers.
	 *
	 * @return the number of password hash providers
	 */
	public int countAll();

}