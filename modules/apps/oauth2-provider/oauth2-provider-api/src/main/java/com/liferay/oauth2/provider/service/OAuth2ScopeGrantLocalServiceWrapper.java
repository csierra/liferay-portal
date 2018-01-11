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

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link OAuth2ScopeGrantLocalService}.
 *
 * @author Brian Wing Shun Chan
 * @see OAuth2ScopeGrantLocalService
 * @generated
 */
@ProviderType
public class OAuth2ScopeGrantLocalServiceWrapper
	implements OAuth2ScopeGrantLocalService,
		ServiceWrapper<OAuth2ScopeGrantLocalService> {
	public OAuth2ScopeGrantLocalServiceWrapper(
		OAuth2ScopeGrantLocalService oAuth2ScopeGrantLocalService) {
		_oAuth2ScopeGrantLocalService = oAuth2ScopeGrantLocalService;
	}

	/**
	* Adds the o auth2 scope grant to the database. Also notifies the appropriate model listeners.
	*
	* @param oAuth2ScopeGrant the o auth2 scope grant
	* @return the o auth2 scope grant that was added
	*/
	@Override
	public com.liferay.oauth2.provider.model.OAuth2ScopeGrant addOAuth2ScopeGrant(
		com.liferay.oauth2.provider.model.OAuth2ScopeGrant oAuth2ScopeGrant) {
		return _oAuth2ScopeGrantLocalService.addOAuth2ScopeGrant(oAuth2ScopeGrant);
	}

	/**
	* Creates a new o auth2 scope grant with the primary key. Does not add the o auth2 scope grant to the database.
	*
	* @param oAuth2ScopeGrantPK the primary key for the new o auth2 scope grant
	* @return the new o auth2 scope grant
	*/
	@Override
	public com.liferay.oauth2.provider.model.OAuth2ScopeGrant createOAuth2ScopeGrant(
		com.liferay.oauth2.provider.service.persistence.OAuth2ScopeGrantPK oAuth2ScopeGrantPK) {
		return _oAuth2ScopeGrantLocalService.createOAuth2ScopeGrant(oAuth2ScopeGrantPK);
	}

	/**
	* Deletes the o auth2 scope grant from the database. Also notifies the appropriate model listeners.
	*
	* @param oAuth2ScopeGrant the o auth2 scope grant
	* @return the o auth2 scope grant that was removed
	*/
	@Override
	public com.liferay.oauth2.provider.model.OAuth2ScopeGrant deleteOAuth2ScopeGrant(
		com.liferay.oauth2.provider.model.OAuth2ScopeGrant oAuth2ScopeGrant) {
		return _oAuth2ScopeGrantLocalService.deleteOAuth2ScopeGrant(oAuth2ScopeGrant);
	}

	/**
	* Deletes the o auth2 scope grant with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param oAuth2ScopeGrantPK the primary key of the o auth2 scope grant
	* @return the o auth2 scope grant that was removed
	* @throws PortalException if a o auth2 scope grant with the primary key could not be found
	*/
	@Override
	public com.liferay.oauth2.provider.model.OAuth2ScopeGrant deleteOAuth2ScopeGrant(
		com.liferay.oauth2.provider.service.persistence.OAuth2ScopeGrantPK oAuth2ScopeGrantPK)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _oAuth2ScopeGrantLocalService.deleteOAuth2ScopeGrant(oAuth2ScopeGrantPK);
	}

	/**
	* @throws PortalException
	*/
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
		com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _oAuth2ScopeGrantLocalService.deletePersistedModel(persistedModel);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _oAuth2ScopeGrantLocalService.dynamicQuery();
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
		return _oAuth2ScopeGrantLocalService.dynamicQuery(dynamicQuery);
	}

	/**
	* Performs a dynamic query on the database and returns a range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.oauth2.provider.model.impl.OAuth2ScopeGrantModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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
		return _oAuth2ScopeGrantLocalService.dynamicQuery(dynamicQuery, start,
			end);
	}

	/**
	* Performs a dynamic query on the database and returns an ordered range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.oauth2.provider.model.impl.OAuth2ScopeGrantModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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
		return _oAuth2ScopeGrantLocalService.dynamicQuery(dynamicQuery, start,
			end, orderByComparator);
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
		return _oAuth2ScopeGrantLocalService.dynamicQueryCount(dynamicQuery);
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
		return _oAuth2ScopeGrantLocalService.dynamicQueryCount(dynamicQuery,
			projection);
	}

	@Override
	public com.liferay.oauth2.provider.model.OAuth2ScopeGrant fetchOAuth2ScopeGrant(
		com.liferay.oauth2.provider.service.persistence.OAuth2ScopeGrantPK oAuth2ScopeGrantPK) {
		return _oAuth2ScopeGrantLocalService.fetchOAuth2ScopeGrant(oAuth2ScopeGrantPK);
	}

	@Override
	public java.util.Optional<com.liferay.oauth2.provider.model.OAuth2ScopeGrant> findByA_BNS_BV_C_O_T(
		java.lang.String applicationName, java.lang.String bundleSymbolicName,
		java.lang.String bundleVersion, long companyId, java.lang.String scope,
		java.lang.String tokenId) {
		return _oAuth2ScopeGrantLocalService.findByA_BNS_BV_C_O_T(applicationName,
			bundleSymbolicName, bundleVersion, companyId, scope, tokenId);
	}

	@Override
	public java.util.Collection<com.liferay.oauth2.provider.model.OAuth2ScopeGrant> findByA_BNS_BV_C_T(
		java.lang.String applicationName, java.lang.String bundleSymbolicName,
		java.lang.String bundleVersion, long companyId, java.lang.String tokenId) {
		return _oAuth2ScopeGrantLocalService.findByA_BNS_BV_C_T(applicationName,
			bundleSymbolicName, bundleVersion, companyId, tokenId);
	}

	@Override
	public java.util.Collection<com.liferay.oauth2.provider.model.OAuth2ScopeGrant> findByTokenId(
		java.lang.String tokenId) {
		return _oAuth2ScopeGrantLocalService.findByTokenId(tokenId);
	}

	/**
	* Returns the o auth2 scope grant with the primary key.
	*
	* @param oAuth2ScopeGrantPK the primary key of the o auth2 scope grant
	* @return the o auth2 scope grant
	* @throws PortalException if a o auth2 scope grant with the primary key could not be found
	*/
	@Override
	public com.liferay.oauth2.provider.model.OAuth2ScopeGrant getOAuth2ScopeGrant(
		com.liferay.oauth2.provider.service.persistence.OAuth2ScopeGrantPK oAuth2ScopeGrantPK)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _oAuth2ScopeGrantLocalService.getOAuth2ScopeGrant(oAuth2ScopeGrantPK);
	}

	/**
	* Returns a range of all the o auth2 scope grants.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.oauth2.provider.model.impl.OAuth2ScopeGrantModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of o auth2 scope grants
	* @param end the upper bound of the range of o auth2 scope grants (not inclusive)
	* @return the range of o auth2 scope grants
	*/
	@Override
	public java.util.List<com.liferay.oauth2.provider.model.OAuth2ScopeGrant> getOAuth2ScopeGrants(
		int start, int end) {
		return _oAuth2ScopeGrantLocalService.getOAuth2ScopeGrants(start, end);
	}

	/**
	* Returns the number of o auth2 scope grants.
	*
	* @return the number of o auth2 scope grants
	*/
	@Override
	public int getOAuth2ScopeGrantsCount() {
		return _oAuth2ScopeGrantLocalService.getOAuth2ScopeGrantsCount();
	}

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	@Override
	public java.lang.String getOSGiServiceIdentifier() {
		return _oAuth2ScopeGrantLocalService.getOSGiServiceIdentifier();
	}

	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
		java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _oAuth2ScopeGrantLocalService.getPersistedModel(primaryKeyObj);
	}

	@Override
	public java.util.Collection<com.liferay.oauth2.provider.model.OAuth2ScopeGrant> grantScopesToToken(
		java.lang.String tokenString,
		java.lang.Iterable<com.liferay.oauth2.provider.model.LiferayOAuth2ScopeInternalIdentifier> scopes)
		throws com.liferay.oauth2.provider.exception.NoSuchOAuth2TokenException {
		return _oAuth2ScopeGrantLocalService.grantScopesToToken(tokenString,
			scopes);
	}

	/**
	* Updates the o auth2 scope grant in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param oAuth2ScopeGrant the o auth2 scope grant
	* @return the o auth2 scope grant that was updated
	*/
	@Override
	public com.liferay.oauth2.provider.model.OAuth2ScopeGrant updateOAuth2ScopeGrant(
		com.liferay.oauth2.provider.model.OAuth2ScopeGrant oAuth2ScopeGrant) {
		return _oAuth2ScopeGrantLocalService.updateOAuth2ScopeGrant(oAuth2ScopeGrant);
	}

	@Override
	public OAuth2ScopeGrantLocalService getWrappedService() {
		return _oAuth2ScopeGrantLocalService;
	}

	@Override
	public void setWrappedService(
		OAuth2ScopeGrantLocalService oAuth2ScopeGrantLocalService) {
		_oAuth2ScopeGrantLocalService = oAuth2ScopeGrantLocalService;
	}

	private OAuth2ScopeGrantLocalService _oAuth2ScopeGrantLocalService;
}