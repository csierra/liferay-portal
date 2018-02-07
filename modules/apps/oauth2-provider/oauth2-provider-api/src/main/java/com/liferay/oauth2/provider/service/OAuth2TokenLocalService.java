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

import com.liferay.oauth2.provider.model.OAuth2Token;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
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
 * Provides the local service interface for OAuth2Token. Methods of this
 * service will not have security checks based on the propagated JAAS
 * credentials because this service can only be accessed from within the same
 * VM.
 *
 * @author Brian Wing Shun Chan
 * @see OAuth2TokenLocalServiceUtil
 * @see com.liferay.oauth2.provider.service.base.OAuth2TokenLocalServiceBaseImpl
 * @see com.liferay.oauth2.provider.service.impl.OAuth2TokenLocalServiceImpl
 * @generated
 */
@ProviderType
@Transactional(isolation = Isolation.PORTAL, rollbackFor =  {
	PortalException.class, SystemException.class})
public interface OAuth2TokenLocalService extends BaseLocalService,
	PersistedModelLocalService {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link OAuth2TokenLocalServiceUtil} to access the o auth2 token local service. Add custom service methods to {@link com.liferay.oauth2.provider.service.impl.OAuth2TokenLocalServiceImpl} and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */

	/**
	* Adds the o auth2 token to the database. Also notifies the appropriate model listeners.
	*
	* @param oAuth2Token the o auth2 token
	* @return the o auth2 token that was added
	*/
	@Indexable(type = IndexableType.REINDEX)
	public OAuth2Token addOAuth2Token(OAuth2Token oAuth2Token);

	/**
	* Creates a new o auth2 token with the primary key. Does not add the o auth2 token to the database.
	*
	* @param oAuth2TokenId the primary key for the new o auth2 token
	* @return the new o auth2 token
	*/
	public OAuth2Token createOAuth2Token(java.lang.String oAuth2TokenId);

	/**
	* Deletes the o auth2 token from the database. Also notifies the appropriate model listeners.
	*
	* @param oAuth2Token the o auth2 token
	* @return the o auth2 token that was removed
	*/
	@Indexable(type = IndexableType.DELETE)
	public OAuth2Token deleteOAuth2Token(OAuth2Token oAuth2Token);

	/**
	* Deletes the o auth2 token with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param oAuth2TokenId the primary key of the o auth2 token
	* @return the o auth2 token that was removed
	* @throws PortalException if a o auth2 token with the primary key could not be found
	*/
	@Indexable(type = IndexableType.DELETE)
	public OAuth2Token deleteOAuth2Token(java.lang.String oAuth2TokenId)
		throws PortalException;

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
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.oauth2.provider.model.impl.OAuth2TokenModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.oauth2.provider.model.impl.OAuth2TokenModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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
	public OAuth2Token fetchOAuth2Token(java.lang.String oAuth2TokenId);

	public Collection<OAuth2Token> findByApplicationAndUserName(
		long applicationId, java.lang.String username);

	public Collection<OAuth2Token> findByApplicationId(long applicationId,
		int start, int end, OrderByComparator<OAuth2Token> orderByComparator);

	public Collection<OAuth2Token> findByRefreshToken(
		java.lang.String refreshToken);

	/**
	* Returns the o auth2 token with the primary key.
	*
	* @param oAuth2TokenId the primary key of the o auth2 token
	* @return the o auth2 token
	* @throws PortalException if a o auth2 token with the primary key could not be found
	*/
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public OAuth2Token getOAuth2Token(java.lang.String oAuth2TokenId)
		throws PortalException;

	/**
	* Returns a range of all the o auth2 tokens.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.oauth2.provider.model.impl.OAuth2TokenModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of o auth2 tokens
	* @param end the upper bound of the range of o auth2 tokens (not inclusive)
	* @return the range of o auth2 tokens
	*/
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<OAuth2Token> getOAuth2Tokens(int start, int end);

	/**
	* Returns the number of o auth2 tokens.
	*
	* @return the number of o auth2 tokens
	*/
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public int getOAuth2TokensCount();

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
	* Updates the o auth2 token in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param oAuth2Token the o auth2 token
	* @return the o auth2 token that was updated
	*/
	@Indexable(type = IndexableType.REINDEX)
	public OAuth2Token updateOAuth2Token(OAuth2Token oAuth2Token);
}