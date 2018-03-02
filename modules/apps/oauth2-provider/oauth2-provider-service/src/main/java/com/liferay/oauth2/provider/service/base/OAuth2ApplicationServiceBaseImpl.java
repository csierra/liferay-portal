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

package com.liferay.oauth2.provider.service.base;

import com.liferay.oauth2.provider.model.OAuth2Application;
import com.liferay.oauth2.provider.service.OAuth2ApplicationService;
import com.liferay.oauth2.provider.service.persistence.OAuth2ApplicationPersistence;
import com.liferay.oauth2.provider.service.persistence.OAuth2RefreshTokenPersistence;
import com.liferay.oauth2.provider.service.persistence.OAuth2ScopeGrantFinder;
import com.liferay.oauth2.provider.service.persistence.OAuth2ScopeGrantPersistence;
import com.liferay.oauth2.provider.service.persistence.OAuth2TokenPersistence;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.service.BaseServiceImpl;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.spring.extender.service.ServiceReference;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the o auth2 application remote service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.oauth2.provider.service.impl.OAuth2ApplicationServiceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.oauth2.provider.service.impl.OAuth2ApplicationServiceImpl
 * @see com.liferay.oauth2.provider.service.OAuth2ApplicationServiceUtil
 * @generated
 */
public abstract class OAuth2ApplicationServiceBaseImpl extends BaseServiceImpl
	implements OAuth2ApplicationService, IdentifiableOSGiService {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link com.liferay.oauth2.provider.service.OAuth2ApplicationServiceUtil} to access the o auth2 application remote service.
	 */

	/**
	 * Returns the o auth2 application local service.
	 *
	 * @return the o auth2 application local service
	 */
	public com.liferay.oauth2.provider.service.OAuth2ApplicationLocalService getOAuth2ApplicationLocalService() {
		return oAuth2ApplicationLocalService;
	}

	/**
	 * Sets the o auth2 application local service.
	 *
	 * @param oAuth2ApplicationLocalService the o auth2 application local service
	 */
	public void setOAuth2ApplicationLocalService(
		com.liferay.oauth2.provider.service.OAuth2ApplicationLocalService oAuth2ApplicationLocalService) {
		this.oAuth2ApplicationLocalService = oAuth2ApplicationLocalService;
	}

	/**
	 * Returns the o auth2 application remote service.
	 *
	 * @return the o auth2 application remote service
	 */
	public OAuth2ApplicationService getOAuth2ApplicationService() {
		return oAuth2ApplicationService;
	}

	/**
	 * Sets the o auth2 application remote service.
	 *
	 * @param oAuth2ApplicationService the o auth2 application remote service
	 */
	public void setOAuth2ApplicationService(
		OAuth2ApplicationService oAuth2ApplicationService) {
		this.oAuth2ApplicationService = oAuth2ApplicationService;
	}

	/**
	 * Returns the o auth2 application persistence.
	 *
	 * @return the o auth2 application persistence
	 */
	public OAuth2ApplicationPersistence getOAuth2ApplicationPersistence() {
		return oAuth2ApplicationPersistence;
	}

	/**
	 * Sets the o auth2 application persistence.
	 *
	 * @param oAuth2ApplicationPersistence the o auth2 application persistence
	 */
	public void setOAuth2ApplicationPersistence(
		OAuth2ApplicationPersistence oAuth2ApplicationPersistence) {
		this.oAuth2ApplicationPersistence = oAuth2ApplicationPersistence;
	}

	/**
	 * Returns the counter local service.
	 *
	 * @return the counter local service
	 */
	public com.liferay.counter.kernel.service.CounterLocalService getCounterLocalService() {
		return counterLocalService;
	}

	/**
	 * Sets the counter local service.
	 *
	 * @param counterLocalService the counter local service
	 */
	public void setCounterLocalService(
		com.liferay.counter.kernel.service.CounterLocalService counterLocalService) {
		this.counterLocalService = counterLocalService;
	}

	/**
	 * Returns the resource local service.
	 *
	 * @return the resource local service
	 */
	public com.liferay.portal.kernel.service.ResourceLocalService getResourceLocalService() {
		return resourceLocalService;
	}

	/**
	 * Sets the resource local service.
	 *
	 * @param resourceLocalService the resource local service
	 */
	public void setResourceLocalService(
		com.liferay.portal.kernel.service.ResourceLocalService resourceLocalService) {
		this.resourceLocalService = resourceLocalService;
	}

	/**
	 * Returns the o auth2 refresh token local service.
	 *
	 * @return the o auth2 refresh token local service
	 */
	public com.liferay.oauth2.provider.service.OAuth2RefreshTokenLocalService getOAuth2RefreshTokenLocalService() {
		return oAuth2RefreshTokenLocalService;
	}

	/**
	 * Sets the o auth2 refresh token local service.
	 *
	 * @param oAuth2RefreshTokenLocalService the o auth2 refresh token local service
	 */
	public void setOAuth2RefreshTokenLocalService(
		com.liferay.oauth2.provider.service.OAuth2RefreshTokenLocalService oAuth2RefreshTokenLocalService) {
		this.oAuth2RefreshTokenLocalService = oAuth2RefreshTokenLocalService;
	}

	/**
	 * Returns the o auth2 refresh token remote service.
	 *
	 * @return the o auth2 refresh token remote service
	 */
	public com.liferay.oauth2.provider.service.OAuth2RefreshTokenService getOAuth2RefreshTokenService() {
		return oAuth2RefreshTokenService;
	}

	/**
	 * Sets the o auth2 refresh token remote service.
	 *
	 * @param oAuth2RefreshTokenService the o auth2 refresh token remote service
	 */
	public void setOAuth2RefreshTokenService(
		com.liferay.oauth2.provider.service.OAuth2RefreshTokenService oAuth2RefreshTokenService) {
		this.oAuth2RefreshTokenService = oAuth2RefreshTokenService;
	}

	/**
	 * Returns the o auth2 refresh token persistence.
	 *
	 * @return the o auth2 refresh token persistence
	 */
	public OAuth2RefreshTokenPersistence getOAuth2RefreshTokenPersistence() {
		return oAuth2RefreshTokenPersistence;
	}

	/**
	 * Sets the o auth2 refresh token persistence.
	 *
	 * @param oAuth2RefreshTokenPersistence the o auth2 refresh token persistence
	 */
	public void setOAuth2RefreshTokenPersistence(
		OAuth2RefreshTokenPersistence oAuth2RefreshTokenPersistence) {
		this.oAuth2RefreshTokenPersistence = oAuth2RefreshTokenPersistence;
	}

	/**
	 * Returns the o auth2 scope grant local service.
	 *
	 * @return the o auth2 scope grant local service
	 */
	public com.liferay.oauth2.provider.service.OAuth2ScopeGrantLocalService getOAuth2ScopeGrantLocalService() {
		return oAuth2ScopeGrantLocalService;
	}

	/**
	 * Sets the o auth2 scope grant local service.
	 *
	 * @param oAuth2ScopeGrantLocalService the o auth2 scope grant local service
	 */
	public void setOAuth2ScopeGrantLocalService(
		com.liferay.oauth2.provider.service.OAuth2ScopeGrantLocalService oAuth2ScopeGrantLocalService) {
		this.oAuth2ScopeGrantLocalService = oAuth2ScopeGrantLocalService;
	}

	/**
	 * Returns the o auth2 scope grant persistence.
	 *
	 * @return the o auth2 scope grant persistence
	 */
	public OAuth2ScopeGrantPersistence getOAuth2ScopeGrantPersistence() {
		return oAuth2ScopeGrantPersistence;
	}

	/**
	 * Sets the o auth2 scope grant persistence.
	 *
	 * @param oAuth2ScopeGrantPersistence the o auth2 scope grant persistence
	 */
	public void setOAuth2ScopeGrantPersistence(
		OAuth2ScopeGrantPersistence oAuth2ScopeGrantPersistence) {
		this.oAuth2ScopeGrantPersistence = oAuth2ScopeGrantPersistence;
	}

	/**
	 * Returns the o auth2 scope grant finder.
	 *
	 * @return the o auth2 scope grant finder
	 */
	public OAuth2ScopeGrantFinder getOAuth2ScopeGrantFinder() {
		return oAuth2ScopeGrantFinder;
	}

	/**
	 * Sets the o auth2 scope grant finder.
	 *
	 * @param oAuth2ScopeGrantFinder the o auth2 scope grant finder
	 */
	public void setOAuth2ScopeGrantFinder(
		OAuth2ScopeGrantFinder oAuth2ScopeGrantFinder) {
		this.oAuth2ScopeGrantFinder = oAuth2ScopeGrantFinder;
	}

	/**
	 * Returns the o auth2 token local service.
	 *
	 * @return the o auth2 token local service
	 */
	public com.liferay.oauth2.provider.service.OAuth2TokenLocalService getOAuth2TokenLocalService() {
		return oAuth2TokenLocalService;
	}

	/**
	 * Sets the o auth2 token local service.
	 *
	 * @param oAuth2TokenLocalService the o auth2 token local service
	 */
	public void setOAuth2TokenLocalService(
		com.liferay.oauth2.provider.service.OAuth2TokenLocalService oAuth2TokenLocalService) {
		this.oAuth2TokenLocalService = oAuth2TokenLocalService;
	}

	/**
	 * Returns the o auth2 token remote service.
	 *
	 * @return the o auth2 token remote service
	 */
	public com.liferay.oauth2.provider.service.OAuth2TokenService getOAuth2TokenService() {
		return oAuth2TokenService;
	}

	/**
	 * Sets the o auth2 token remote service.
	 *
	 * @param oAuth2TokenService the o auth2 token remote service
	 */
	public void setOAuth2TokenService(
		com.liferay.oauth2.provider.service.OAuth2TokenService oAuth2TokenService) {
		this.oAuth2TokenService = oAuth2TokenService;
	}

	/**
	 * Returns the o auth2 token persistence.
	 *
	 * @return the o auth2 token persistence
	 */
	public OAuth2TokenPersistence getOAuth2TokenPersistence() {
		return oAuth2TokenPersistence;
	}

	/**
	 * Sets the o auth2 token persistence.
	 *
	 * @param oAuth2TokenPersistence the o auth2 token persistence
	 */
	public void setOAuth2TokenPersistence(
		OAuth2TokenPersistence oAuth2TokenPersistence) {
		this.oAuth2TokenPersistence = oAuth2TokenPersistence;
	}

	public void afterPropertiesSet() {
	}

	public void destroy() {
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return OAuth2ApplicationService.class.getName();
	}

	protected Class<?> getModelClass() {
		return OAuth2Application.class;
	}

	protected String getModelClassName() {
		return OAuth2Application.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = oAuth2ApplicationPersistence.getDataSource();

			DB db = DBManagerUtil.getDB();

			sql = db.buildSQL(sql);
			sql = PortalUtil.transformSQL(sql);

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(dataSource,
					sql);

			sqlUpdate.update();
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	@BeanReference(type = com.liferay.oauth2.provider.service.OAuth2ApplicationLocalService.class)
	protected com.liferay.oauth2.provider.service.OAuth2ApplicationLocalService oAuth2ApplicationLocalService;
	@BeanReference(type = OAuth2ApplicationService.class)
	protected OAuth2ApplicationService oAuth2ApplicationService;
	@BeanReference(type = OAuth2ApplicationPersistence.class)
	protected OAuth2ApplicationPersistence oAuth2ApplicationPersistence;
	@ServiceReference(type = com.liferay.counter.kernel.service.CounterLocalService.class)
	protected com.liferay.counter.kernel.service.CounterLocalService counterLocalService;
	@ServiceReference(type = com.liferay.portal.kernel.service.ResourceLocalService.class)
	protected com.liferay.portal.kernel.service.ResourceLocalService resourceLocalService;
	@BeanReference(type = com.liferay.oauth2.provider.service.OAuth2RefreshTokenLocalService.class)
	protected com.liferay.oauth2.provider.service.OAuth2RefreshTokenLocalService oAuth2RefreshTokenLocalService;
	@BeanReference(type = com.liferay.oauth2.provider.service.OAuth2RefreshTokenService.class)
	protected com.liferay.oauth2.provider.service.OAuth2RefreshTokenService oAuth2RefreshTokenService;
	@BeanReference(type = OAuth2RefreshTokenPersistence.class)
	protected OAuth2RefreshTokenPersistence oAuth2RefreshTokenPersistence;
	@BeanReference(type = com.liferay.oauth2.provider.service.OAuth2ScopeGrantLocalService.class)
	protected com.liferay.oauth2.provider.service.OAuth2ScopeGrantLocalService oAuth2ScopeGrantLocalService;
	@BeanReference(type = OAuth2ScopeGrantPersistence.class)
	protected OAuth2ScopeGrantPersistence oAuth2ScopeGrantPersistence;
	@BeanReference(type = OAuth2ScopeGrantFinder.class)
	protected OAuth2ScopeGrantFinder oAuth2ScopeGrantFinder;
	@BeanReference(type = com.liferay.oauth2.provider.service.OAuth2TokenLocalService.class)
	protected com.liferay.oauth2.provider.service.OAuth2TokenLocalService oAuth2TokenLocalService;
	@BeanReference(type = com.liferay.oauth2.provider.service.OAuth2TokenService.class)
	protected com.liferay.oauth2.provider.service.OAuth2TokenService oAuth2TokenService;
	@BeanReference(type = OAuth2TokenPersistence.class)
	protected OAuth2TokenPersistence oAuth2TokenPersistence;
}