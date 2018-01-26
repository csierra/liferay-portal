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

package com.liferay.oauth2.provider.service.persistence;

import aQute.bnd.annotation.ProviderType;

import com.liferay.oauth2.provider.model.OAuth2RefreshToken;

import com.liferay.osgi.util.ServiceTrackerFactory;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;

import org.osgi.util.tracker.ServiceTracker;

import java.util.List;

/**
 * The persistence utility for the o auth2 refresh token service. This utility wraps {@link com.liferay.oauth2.provider.service.persistence.impl.OAuth2RefreshTokenPersistenceImpl} and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see OAuth2RefreshTokenPersistence
 * @see com.liferay.oauth2.provider.service.persistence.impl.OAuth2RefreshTokenPersistenceImpl
 * @generated
 */
@ProviderType
public class OAuth2RefreshTokenUtil {
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
	public static void clearCache(OAuth2RefreshToken oAuth2RefreshToken) {
		getPersistence().clearCache(oAuth2RefreshToken);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#countWithDynamicQuery(DynamicQuery)
	 */
	public static long countWithDynamicQuery(DynamicQuery dynamicQuery) {
		return getPersistence().countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<OAuth2RefreshToken> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {
		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<OAuth2RefreshToken> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {
		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<OAuth2RefreshToken> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<OAuth2RefreshToken> orderByComparator) {
		return getPersistence()
				   .findWithDynamicQuery(dynamicQuery, start, end,
			orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static OAuth2RefreshToken update(
		OAuth2RefreshToken oAuth2RefreshToken) {
		return getPersistence().update(oAuth2RefreshToken);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static OAuth2RefreshToken update(
		OAuth2RefreshToken oAuth2RefreshToken, ServiceContext serviceContext) {
		return getPersistence().update(oAuth2RefreshToken, serviceContext);
	}

	/**
	* Returns all the o auth2 refresh tokens where oAuth2ApplicationId = &#63;.
	*
	* @param oAuth2ApplicationId the o auth2 application ID
	* @return the matching o auth2 refresh tokens
	*/
	public static List<OAuth2RefreshToken> findByA(
		java.lang.String oAuth2ApplicationId) {
		return getPersistence().findByA(oAuth2ApplicationId);
	}

	/**
	* Returns a range of all the o auth2 refresh tokens where oAuth2ApplicationId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link OAuth2RefreshTokenModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param oAuth2ApplicationId the o auth2 application ID
	* @param start the lower bound of the range of o auth2 refresh tokens
	* @param end the upper bound of the range of o auth2 refresh tokens (not inclusive)
	* @return the range of matching o auth2 refresh tokens
	*/
	public static List<OAuth2RefreshToken> findByA(
		java.lang.String oAuth2ApplicationId, int start, int end) {
		return getPersistence().findByA(oAuth2ApplicationId, start, end);
	}

	/**
	* Returns an ordered range of all the o auth2 refresh tokens where oAuth2ApplicationId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link OAuth2RefreshTokenModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param oAuth2ApplicationId the o auth2 application ID
	* @param start the lower bound of the range of o auth2 refresh tokens
	* @param end the upper bound of the range of o auth2 refresh tokens (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching o auth2 refresh tokens
	*/
	public static List<OAuth2RefreshToken> findByA(
		java.lang.String oAuth2ApplicationId, int start, int end,
		OrderByComparator<OAuth2RefreshToken> orderByComparator) {
		return getPersistence()
				   .findByA(oAuth2ApplicationId, start, end, orderByComparator);
	}

	/**
	* Returns an ordered range of all the o auth2 refresh tokens where oAuth2ApplicationId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link OAuth2RefreshTokenModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param oAuth2ApplicationId the o auth2 application ID
	* @param start the lower bound of the range of o auth2 refresh tokens
	* @param end the upper bound of the range of o auth2 refresh tokens (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @param retrieveFromCache whether to retrieve from the finder cache
	* @return the ordered range of matching o auth2 refresh tokens
	*/
	public static List<OAuth2RefreshToken> findByA(
		java.lang.String oAuth2ApplicationId, int start, int end,
		OrderByComparator<OAuth2RefreshToken> orderByComparator,
		boolean retrieveFromCache) {
		return getPersistence()
				   .findByA(oAuth2ApplicationId, start, end, orderByComparator,
			retrieveFromCache);
	}

	/**
	* Returns the first o auth2 refresh token in the ordered set where oAuth2ApplicationId = &#63;.
	*
	* @param oAuth2ApplicationId the o auth2 application ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching o auth2 refresh token
	* @throws NoSuchOAuth2RefreshTokenException if a matching o auth2 refresh token could not be found
	*/
	public static OAuth2RefreshToken findByA_First(
		java.lang.String oAuth2ApplicationId,
		OrderByComparator<OAuth2RefreshToken> orderByComparator)
		throws com.liferay.oauth2.provider.exception.NoSuchOAuth2RefreshTokenException {
		return getPersistence()
				   .findByA_First(oAuth2ApplicationId, orderByComparator);
	}

	/**
	* Returns the first o auth2 refresh token in the ordered set where oAuth2ApplicationId = &#63;.
	*
	* @param oAuth2ApplicationId the o auth2 application ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching o auth2 refresh token, or <code>null</code> if a matching o auth2 refresh token could not be found
	*/
	public static OAuth2RefreshToken fetchByA_First(
		java.lang.String oAuth2ApplicationId,
		OrderByComparator<OAuth2RefreshToken> orderByComparator) {
		return getPersistence()
				   .fetchByA_First(oAuth2ApplicationId, orderByComparator);
	}

	/**
	* Returns the last o auth2 refresh token in the ordered set where oAuth2ApplicationId = &#63;.
	*
	* @param oAuth2ApplicationId the o auth2 application ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching o auth2 refresh token
	* @throws NoSuchOAuth2RefreshTokenException if a matching o auth2 refresh token could not be found
	*/
	public static OAuth2RefreshToken findByA_Last(
		java.lang.String oAuth2ApplicationId,
		OrderByComparator<OAuth2RefreshToken> orderByComparator)
		throws com.liferay.oauth2.provider.exception.NoSuchOAuth2RefreshTokenException {
		return getPersistence()
				   .findByA_Last(oAuth2ApplicationId, orderByComparator);
	}

	/**
	* Returns the last o auth2 refresh token in the ordered set where oAuth2ApplicationId = &#63;.
	*
	* @param oAuth2ApplicationId the o auth2 application ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching o auth2 refresh token, or <code>null</code> if a matching o auth2 refresh token could not be found
	*/
	public static OAuth2RefreshToken fetchByA_Last(
		java.lang.String oAuth2ApplicationId,
		OrderByComparator<OAuth2RefreshToken> orderByComparator) {
		return getPersistence()
				   .fetchByA_Last(oAuth2ApplicationId, orderByComparator);
	}

	/**
	* Returns the o auth2 refresh tokens before and after the current o auth2 refresh token in the ordered set where oAuth2ApplicationId = &#63;.
	*
	* @param oAuth2RefreshTokenId the primary key of the current o auth2 refresh token
	* @param oAuth2ApplicationId the o auth2 application ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next o auth2 refresh token
	* @throws NoSuchOAuth2RefreshTokenException if a o auth2 refresh token with the primary key could not be found
	*/
	public static OAuth2RefreshToken[] findByA_PrevAndNext(
		java.lang.String oAuth2RefreshTokenId,
		java.lang.String oAuth2ApplicationId,
		OrderByComparator<OAuth2RefreshToken> orderByComparator)
		throws com.liferay.oauth2.provider.exception.NoSuchOAuth2RefreshTokenException {
		return getPersistence()
				   .findByA_PrevAndNext(oAuth2RefreshTokenId,
			oAuth2ApplicationId, orderByComparator);
	}

	/**
	* Removes all the o auth2 refresh tokens where oAuth2ApplicationId = &#63; from the database.
	*
	* @param oAuth2ApplicationId the o auth2 application ID
	*/
	public static void removeByA(java.lang.String oAuth2ApplicationId) {
		getPersistence().removeByA(oAuth2ApplicationId);
	}

	/**
	* Returns the number of o auth2 refresh tokens where oAuth2ApplicationId = &#63;.
	*
	* @param oAuth2ApplicationId the o auth2 application ID
	* @return the number of matching o auth2 refresh tokens
	*/
	public static int countByA(java.lang.String oAuth2ApplicationId) {
		return getPersistence().countByA(oAuth2ApplicationId);
	}

	/**
	* Returns all the o auth2 refresh tokens where oAuth2ApplicationId = &#63; and userName = &#63;.
	*
	* @param oAuth2ApplicationId the o auth2 application ID
	* @param userName the user name
	* @return the matching o auth2 refresh tokens
	*/
	public static List<OAuth2RefreshToken> findByA_U(
		java.lang.String oAuth2ApplicationId, java.lang.String userName) {
		return getPersistence().findByA_U(oAuth2ApplicationId, userName);
	}

	/**
	* Returns a range of all the o auth2 refresh tokens where oAuth2ApplicationId = &#63; and userName = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link OAuth2RefreshTokenModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param oAuth2ApplicationId the o auth2 application ID
	* @param userName the user name
	* @param start the lower bound of the range of o auth2 refresh tokens
	* @param end the upper bound of the range of o auth2 refresh tokens (not inclusive)
	* @return the range of matching o auth2 refresh tokens
	*/
	public static List<OAuth2RefreshToken> findByA_U(
		java.lang.String oAuth2ApplicationId, java.lang.String userName,
		int start, int end) {
		return getPersistence()
				   .findByA_U(oAuth2ApplicationId, userName, start, end);
	}

	/**
	* Returns an ordered range of all the o auth2 refresh tokens where oAuth2ApplicationId = &#63; and userName = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link OAuth2RefreshTokenModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param oAuth2ApplicationId the o auth2 application ID
	* @param userName the user name
	* @param start the lower bound of the range of o auth2 refresh tokens
	* @param end the upper bound of the range of o auth2 refresh tokens (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching o auth2 refresh tokens
	*/
	public static List<OAuth2RefreshToken> findByA_U(
		java.lang.String oAuth2ApplicationId, java.lang.String userName,
		int start, int end,
		OrderByComparator<OAuth2RefreshToken> orderByComparator) {
		return getPersistence()
				   .findByA_U(oAuth2ApplicationId, userName, start, end,
			orderByComparator);
	}

	/**
	* Returns an ordered range of all the o auth2 refresh tokens where oAuth2ApplicationId = &#63; and userName = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link OAuth2RefreshTokenModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param oAuth2ApplicationId the o auth2 application ID
	* @param userName the user name
	* @param start the lower bound of the range of o auth2 refresh tokens
	* @param end the upper bound of the range of o auth2 refresh tokens (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @param retrieveFromCache whether to retrieve from the finder cache
	* @return the ordered range of matching o auth2 refresh tokens
	*/
	public static List<OAuth2RefreshToken> findByA_U(
		java.lang.String oAuth2ApplicationId, java.lang.String userName,
		int start, int end,
		OrderByComparator<OAuth2RefreshToken> orderByComparator,
		boolean retrieveFromCache) {
		return getPersistence()
				   .findByA_U(oAuth2ApplicationId, userName, start, end,
			orderByComparator, retrieveFromCache);
	}

	/**
	* Returns the first o auth2 refresh token in the ordered set where oAuth2ApplicationId = &#63; and userName = &#63;.
	*
	* @param oAuth2ApplicationId the o auth2 application ID
	* @param userName the user name
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching o auth2 refresh token
	* @throws NoSuchOAuth2RefreshTokenException if a matching o auth2 refresh token could not be found
	*/
	public static OAuth2RefreshToken findByA_U_First(
		java.lang.String oAuth2ApplicationId, java.lang.String userName,
		OrderByComparator<OAuth2RefreshToken> orderByComparator)
		throws com.liferay.oauth2.provider.exception.NoSuchOAuth2RefreshTokenException {
		return getPersistence()
				   .findByA_U_First(oAuth2ApplicationId, userName,
			orderByComparator);
	}

	/**
	* Returns the first o auth2 refresh token in the ordered set where oAuth2ApplicationId = &#63; and userName = &#63;.
	*
	* @param oAuth2ApplicationId the o auth2 application ID
	* @param userName the user name
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching o auth2 refresh token, or <code>null</code> if a matching o auth2 refresh token could not be found
	*/
	public static OAuth2RefreshToken fetchByA_U_First(
		java.lang.String oAuth2ApplicationId, java.lang.String userName,
		OrderByComparator<OAuth2RefreshToken> orderByComparator) {
		return getPersistence()
				   .fetchByA_U_First(oAuth2ApplicationId, userName,
			orderByComparator);
	}

	/**
	* Returns the last o auth2 refresh token in the ordered set where oAuth2ApplicationId = &#63; and userName = &#63;.
	*
	* @param oAuth2ApplicationId the o auth2 application ID
	* @param userName the user name
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching o auth2 refresh token
	* @throws NoSuchOAuth2RefreshTokenException if a matching o auth2 refresh token could not be found
	*/
	public static OAuth2RefreshToken findByA_U_Last(
		java.lang.String oAuth2ApplicationId, java.lang.String userName,
		OrderByComparator<OAuth2RefreshToken> orderByComparator)
		throws com.liferay.oauth2.provider.exception.NoSuchOAuth2RefreshTokenException {
		return getPersistence()
				   .findByA_U_Last(oAuth2ApplicationId, userName,
			orderByComparator);
	}

	/**
	* Returns the last o auth2 refresh token in the ordered set where oAuth2ApplicationId = &#63; and userName = &#63;.
	*
	* @param oAuth2ApplicationId the o auth2 application ID
	* @param userName the user name
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching o auth2 refresh token, or <code>null</code> if a matching o auth2 refresh token could not be found
	*/
	public static OAuth2RefreshToken fetchByA_U_Last(
		java.lang.String oAuth2ApplicationId, java.lang.String userName,
		OrderByComparator<OAuth2RefreshToken> orderByComparator) {
		return getPersistence()
				   .fetchByA_U_Last(oAuth2ApplicationId, userName,
			orderByComparator);
	}

	/**
	* Returns the o auth2 refresh tokens before and after the current o auth2 refresh token in the ordered set where oAuth2ApplicationId = &#63; and userName = &#63;.
	*
	* @param oAuth2RefreshTokenId the primary key of the current o auth2 refresh token
	* @param oAuth2ApplicationId the o auth2 application ID
	* @param userName the user name
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next o auth2 refresh token
	* @throws NoSuchOAuth2RefreshTokenException if a o auth2 refresh token with the primary key could not be found
	*/
	public static OAuth2RefreshToken[] findByA_U_PrevAndNext(
		java.lang.String oAuth2RefreshTokenId,
		java.lang.String oAuth2ApplicationId, java.lang.String userName,
		OrderByComparator<OAuth2RefreshToken> orderByComparator)
		throws com.liferay.oauth2.provider.exception.NoSuchOAuth2RefreshTokenException {
		return getPersistence()
				   .findByA_U_PrevAndNext(oAuth2RefreshTokenId,
			oAuth2ApplicationId, userName, orderByComparator);
	}

	/**
	* Removes all the o auth2 refresh tokens where oAuth2ApplicationId = &#63; and userName = &#63; from the database.
	*
	* @param oAuth2ApplicationId the o auth2 application ID
	* @param userName the user name
	*/
	public static void removeByA_U(java.lang.String oAuth2ApplicationId,
		java.lang.String userName) {
		getPersistence().removeByA_U(oAuth2ApplicationId, userName);
	}

	/**
	* Returns the number of o auth2 refresh tokens where oAuth2ApplicationId = &#63; and userName = &#63;.
	*
	* @param oAuth2ApplicationId the o auth2 application ID
	* @param userName the user name
	* @return the number of matching o auth2 refresh tokens
	*/
	public static int countByA_U(java.lang.String oAuth2ApplicationId,
		java.lang.String userName) {
		return getPersistence().countByA_U(oAuth2ApplicationId, userName);
	}

	/**
	* Caches the o auth2 refresh token in the entity cache if it is enabled.
	*
	* @param oAuth2RefreshToken the o auth2 refresh token
	*/
	public static void cacheResult(OAuth2RefreshToken oAuth2RefreshToken) {
		getPersistence().cacheResult(oAuth2RefreshToken);
	}

	/**
	* Caches the o auth2 refresh tokens in the entity cache if it is enabled.
	*
	* @param oAuth2RefreshTokens the o auth2 refresh tokens
	*/
	public static void cacheResult(List<OAuth2RefreshToken> oAuth2RefreshTokens) {
		getPersistence().cacheResult(oAuth2RefreshTokens);
	}

	/**
	* Creates a new o auth2 refresh token with the primary key. Does not add the o auth2 refresh token to the database.
	*
	* @param oAuth2RefreshTokenId the primary key for the new o auth2 refresh token
	* @return the new o auth2 refresh token
	*/
	public static OAuth2RefreshToken create(
		java.lang.String oAuth2RefreshTokenId) {
		return getPersistence().create(oAuth2RefreshTokenId);
	}

	/**
	* Removes the o auth2 refresh token with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param oAuth2RefreshTokenId the primary key of the o auth2 refresh token
	* @return the o auth2 refresh token that was removed
	* @throws NoSuchOAuth2RefreshTokenException if a o auth2 refresh token with the primary key could not be found
	*/
	public static OAuth2RefreshToken remove(
		java.lang.String oAuth2RefreshTokenId)
		throws com.liferay.oauth2.provider.exception.NoSuchOAuth2RefreshTokenException {
		return getPersistence().remove(oAuth2RefreshTokenId);
	}

	public static OAuth2RefreshToken updateImpl(
		OAuth2RefreshToken oAuth2RefreshToken) {
		return getPersistence().updateImpl(oAuth2RefreshToken);
	}

	/**
	* Returns the o auth2 refresh token with the primary key or throws a {@link NoSuchOAuth2RefreshTokenException} if it could not be found.
	*
	* @param oAuth2RefreshTokenId the primary key of the o auth2 refresh token
	* @return the o auth2 refresh token
	* @throws NoSuchOAuth2RefreshTokenException if a o auth2 refresh token with the primary key could not be found
	*/
	public static OAuth2RefreshToken findByPrimaryKey(
		java.lang.String oAuth2RefreshTokenId)
		throws com.liferay.oauth2.provider.exception.NoSuchOAuth2RefreshTokenException {
		return getPersistence().findByPrimaryKey(oAuth2RefreshTokenId);
	}

	/**
	* Returns the o auth2 refresh token with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param oAuth2RefreshTokenId the primary key of the o auth2 refresh token
	* @return the o auth2 refresh token, or <code>null</code> if a o auth2 refresh token with the primary key could not be found
	*/
	public static OAuth2RefreshToken fetchByPrimaryKey(
		java.lang.String oAuth2RefreshTokenId) {
		return getPersistence().fetchByPrimaryKey(oAuth2RefreshTokenId);
	}

	public static java.util.Map<java.io.Serializable, OAuth2RefreshToken> fetchByPrimaryKeys(
		java.util.Set<java.io.Serializable> primaryKeys) {
		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	* Returns all the o auth2 refresh tokens.
	*
	* @return the o auth2 refresh tokens
	*/
	public static List<OAuth2RefreshToken> findAll() {
		return getPersistence().findAll();
	}

	/**
	* Returns a range of all the o auth2 refresh tokens.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link OAuth2RefreshTokenModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of o auth2 refresh tokens
	* @param end the upper bound of the range of o auth2 refresh tokens (not inclusive)
	* @return the range of o auth2 refresh tokens
	*/
	public static List<OAuth2RefreshToken> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
	}

	/**
	* Returns an ordered range of all the o auth2 refresh tokens.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link OAuth2RefreshTokenModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of o auth2 refresh tokens
	* @param end the upper bound of the range of o auth2 refresh tokens (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of o auth2 refresh tokens
	*/
	public static List<OAuth2RefreshToken> findAll(int start, int end,
		OrderByComparator<OAuth2RefreshToken> orderByComparator) {
		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	* Returns an ordered range of all the o auth2 refresh tokens.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link OAuth2RefreshTokenModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of o auth2 refresh tokens
	* @param end the upper bound of the range of o auth2 refresh tokens (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @param retrieveFromCache whether to retrieve from the finder cache
	* @return the ordered range of o auth2 refresh tokens
	*/
	public static List<OAuth2RefreshToken> findAll(int start, int end,
		OrderByComparator<OAuth2RefreshToken> orderByComparator,
		boolean retrieveFromCache) {
		return getPersistence()
				   .findAll(start, end, orderByComparator, retrieveFromCache);
	}

	/**
	* Removes all the o auth2 refresh tokens from the database.
	*/
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	* Returns the number of o auth2 refresh tokens.
	*
	* @return the number of o auth2 refresh tokens
	*/
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static OAuth2RefreshTokenPersistence getPersistence() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<OAuth2RefreshTokenPersistence, OAuth2RefreshTokenPersistence> _serviceTracker =
		ServiceTrackerFactory.open(OAuth2RefreshTokenPersistence.class);
}