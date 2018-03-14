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

package com.liferay.oauth2.provider.service;

import aQute.bnd.annotation.ProviderType;

import com.liferay.oauth2.provider.exception.NoSuchOAuth2RefreshTokenException;
import com.liferay.oauth2.provider.model.OAuth2RefreshToken;

import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.BaseLocalService;
import com.liferay.portal.kernel.service.PersistedModelLocalService;
import com.liferay.portal.kernel.transaction.Isolation;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.io.Serializable;

import java.util.Collection;
import java.util.List;

/**
 * Provides the local service interface for OAuth2RefreshToken. Methods of this
 * service will not have security checks based on the propagated JAAS
 * credentials because this service can only be accessed from within the same
 * VM.
 *
 * @author Brian Wing Shun Chan
 * @see OAuth2RefreshTokenLocalServiceUtil
 * @see com.liferay.oauth2.provider.service.base.OAuth2RefreshTokenLocalServiceBaseImpl
 * @see com.liferay.oauth2.provider.service.impl.OAuth2RefreshTokenLocalServiceImpl
 * @generated
 */
@ProviderType
@Transactional(isolation = Isolation.PORTAL, rollbackFor =  {
	PortalException.class, SystemException.class})
public interface OAuth2RefreshTokenLocalService extends BaseLocalService,
	PersistedModelLocalService {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link OAuth2RefreshTokenLocalServiceUtil} to access the o auth2 refresh token local service. Add custom service methods to {@link com.liferay.oauth2.provider.service.impl.OAuth2RefreshTokenLocalServiceImpl} and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */

	/**
	* Adds the o auth2 refresh token to the database. Also notifies the appropriate model listeners.
	*
	* @param oAuth2RefreshToken the o auth2 refresh token
	* @return the o auth2 refresh token that was added
	*/
	@Indexable(type = IndexableType.REINDEX)
	public OAuth2RefreshToken addOAuth2RefreshToken(
		OAuth2RefreshToken oAuth2RefreshToken);

	/**
	* Creates a new o auth2 refresh token with the primary key. Does not add the o auth2 refresh token to the database.
	*
	* @param oAuth2RefreshTokenId the primary key for the new o auth2 refresh token
	* @return the new o auth2 refresh token
	*/
	public OAuth2RefreshToken createOAuth2RefreshToken(
		long oAuth2RefreshTokenId);

	/**
	* NOTE FOR DEVELOPERS:
	*
	* Never reference this class directly. Always use {@link OAuth2RefreshTokenLocalServiceUtil} to access the o auth2 refresh token local service.
	*/
	public OAuth2RefreshToken createOAuth2RefreshToken(
		java.lang.String tokenContent);

	/**
	* Deletes the o auth2 refresh token with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param oAuth2RefreshTokenId the primary key of the o auth2 refresh token
	* @return the o auth2 refresh token that was removed
	* @throws PortalException if a o auth2 refresh token with the primary key could not be found
	*/
	@Indexable(type = IndexableType.DELETE)
	public OAuth2RefreshToken deleteOAuth2RefreshToken(
		long oAuth2RefreshTokenId) throws PortalException;

	/**
	* Deletes the o auth2 refresh token from the database. Also notifies the appropriate model listeners.
	*
	* @param oAuth2RefreshToken the o auth2 refresh token
	* @return the o auth2 refresh token that was removed
	*/
	@Indexable(type = IndexableType.DELETE)
	public OAuth2RefreshToken deleteOAuth2RefreshToken(
		OAuth2RefreshToken oAuth2RefreshToken);

	/**
	* @throws PortalException
	*/
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException;

	public DynamicQuery dynamicQuery();

	/**
	* Performs a dynamic query on the database and returns the matching rows.
	*
	* @param dynamicQuery the dynamic query
	* @return the matching rows
	*/
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery);

	/**
	* Performs a dynamic query on the database and returns a range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.oauth2.provider.model.impl.OAuth2RefreshTokenModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param dynamicQuery the dynamic query
	* @param start the lower bound of the range of model instances
	* @param end the upper bound of the range of model instances (not inclusive)
	* @return the range of matching rows
	*/
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery, int start,
		int end);

	/**
	* Performs a dynamic query on the database and returns an ordered range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.oauth2.provider.model.impl.OAuth2RefreshTokenModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param dynamicQuery the dynamic query
	* @param start the lower bound of the range of model instances
	* @param end the upper bound of the range of model instances (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching rows
	*/
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery, int start,
		int end, OrderByComparator<T> orderByComparator);

	/**
	* Returns the number of rows matching the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @return the number of rows matching the dynamic query
	*/
	public long dynamicQueryCount(DynamicQuery dynamicQuery);

	/**
	* Returns the number of rows matching the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @param projection the projection to apply to the query
	* @return the number of rows matching the dynamic query
	*/
	public long dynamicQueryCount(DynamicQuery dynamicQuery,
		Projection projection);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public OAuth2RefreshToken fetchByContent(java.lang.String tokenContent);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public OAuth2RefreshToken fetchOAuth2RefreshToken(long oAuth2RefreshTokenId);

	public Collection<OAuth2RefreshToken> findByApplication(
		long applicationId, int start, int end,
		OrderByComparator<OAuth2RefreshToken> orderByComparator);

	public OAuth2RefreshToken findByContent(java.lang.String tokenContent)
		throws NoSuchOAuth2RefreshTokenException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ActionableDynamicQuery getActionableDynamicQuery();

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public IndexableActionableDynamicQuery getIndexableActionableDynamicQuery();

	/**
	* Returns the o auth2 refresh token with the primary key.
	*
	* @param oAuth2RefreshTokenId the primary key of the o auth2 refresh token
	* @return the o auth2 refresh token
	* @throws PortalException if a o auth2 refresh token with the primary key could not be found
	*/
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public OAuth2RefreshToken getOAuth2RefreshToken(long oAuth2RefreshTokenId)
		throws PortalException;

	/**
	* Returns a range of all the o auth2 refresh tokens.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.oauth2.provider.model.impl.OAuth2RefreshTokenModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of o auth2 refresh tokens
	* @param end the upper bound of the range of o auth2 refresh tokens (not inclusive)
	* @return the range of o auth2 refresh tokens
	*/
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<OAuth2RefreshToken> getOAuth2RefreshTokens(int start, int end);

	/**
	* Returns the number of o auth2 refresh tokens.
	*
	* @return the number of o auth2 refresh tokens
	*/
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public int getOAuth2RefreshTokensCount();

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	public java.lang.String getOSGiServiceIdentifier();

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException;

	/**
	* Updates the o auth2 refresh token in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param oAuth2RefreshToken the o auth2 refresh token
	* @return the o auth2 refresh token that was updated
	*/
	@Indexable(type = IndexableType.REINDEX)
	public OAuth2RefreshToken updateOAuth2RefreshToken(
		OAuth2RefreshToken oAuth2RefreshToken);
}