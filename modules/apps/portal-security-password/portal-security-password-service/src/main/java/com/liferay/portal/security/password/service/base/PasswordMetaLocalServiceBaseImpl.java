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

package com.liferay.portal.security.password.service.base;

import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DefaultActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.BaseLocalServiceImpl;
import com.liferay.portal.kernel.service.PersistedModelLocalService;
import com.liferay.portal.kernel.service.persistence.BasePersistence;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.security.password.model.PasswordMeta;
import com.liferay.portal.security.password.service.PasswordMetaLocalService;
import com.liferay.portal.security.password.service.persistence.PasswordEntryPersistence;
import com.liferay.portal.security.password.service.persistence.PasswordHashGeneratorPersistence;
import com.liferay.portal.security.password.service.persistence.PasswordMetaPersistence;

import java.io.Serializable;

import java.util.List;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Reference;

/**
 * Provides the base implementation for the password meta local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.portal.security.password.service.impl.PasswordMetaLocalServiceImpl}.
 * </p>
 *
 * @author Arthur Chan
 * @see com.liferay.portal.security.password.service.impl.PasswordMetaLocalServiceImpl
 * @generated
 */
public abstract class PasswordMetaLocalServiceBaseImpl
	extends BaseLocalServiceImpl
	implements AopService, IdentifiableOSGiService, PasswordMetaLocalService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>PasswordMetaLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>com.liferay.portal.security.password.service.PasswordMetaLocalServiceUtil</code>.
	 */

	/**
	 * Adds the password meta to the database. Also notifies the appropriate model listeners.
	 *
	 * @param passwordMeta the password meta
	 * @return the password meta that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public PasswordMeta addPasswordMeta(PasswordMeta passwordMeta) {
		passwordMeta.setNew(true);

		return passwordMetaPersistence.update(passwordMeta);
	}

	/**
	 * Creates a new password meta with the primary key. Does not add the password meta to the database.
	 *
	 * @param passwordMetaId the primary key for the new password meta
	 * @return the new password meta
	 */
	@Override
	@Transactional(enabled = false)
	public PasswordMeta createPasswordMeta(long passwordMetaId) {
		return passwordMetaPersistence.create(passwordMetaId);
	}

	/**
	 * Deletes the password meta with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param passwordMetaId the primary key of the password meta
	 * @return the password meta that was removed
	 * @throws PortalException if a password meta with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public PasswordMeta deletePasswordMeta(long passwordMetaId)
		throws PortalException {

		return passwordMetaPersistence.remove(passwordMetaId);
	}

	/**
	 * Deletes the password meta from the database. Also notifies the appropriate model listeners.
	 *
	 * @param passwordMeta the password meta
	 * @return the password meta that was removed
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public PasswordMeta deletePasswordMeta(PasswordMeta passwordMeta) {
		return passwordMetaPersistence.remove(passwordMeta);
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(
			PasswordMeta.class, clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return passwordMetaPersistence.findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.portal.security.password.model.impl.PasswordMetaModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return passwordMetaPersistence.findWithDynamicQuery(
			dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.portal.security.password.model.impl.PasswordMetaModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<T> orderByComparator) {

		return passwordMetaPersistence.findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery) {
		return passwordMetaPersistence.countWithDynamicQuery(dynamicQuery);
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
		DynamicQuery dynamicQuery, Projection projection) {

		return passwordMetaPersistence.countWithDynamicQuery(
			dynamicQuery, projection);
	}

	@Override
	public PasswordMeta fetchPasswordMeta(long passwordMetaId) {
		return passwordMetaPersistence.fetchByPrimaryKey(passwordMetaId);
	}

	/**
	 * Returns the password meta with the matching UUID and company.
	 *
	 * @param uuid the password meta's UUID
	 * @param companyId the primary key of the company
	 * @return the matching password meta, or <code>null</code> if a matching password meta could not be found
	 */
	@Override
	public PasswordMeta fetchPasswordMetaByUuidAndCompanyId(
		String uuid, long companyId) {

		return passwordMetaPersistence.fetchByUuid_C_First(
			uuid, companyId, null);
	}

	/**
	 * Returns the password meta with the primary key.
	 *
	 * @param passwordMetaId the primary key of the password meta
	 * @return the password meta
	 * @throws PortalException if a password meta with the primary key could not be found
	 */
	@Override
	public PasswordMeta getPasswordMeta(long passwordMetaId)
		throws PortalException {

		return passwordMetaPersistence.findByPrimaryKey(passwordMetaId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery =
			new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(passwordMetaLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(PasswordMeta.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("passwordMetaId");

		return actionableDynamicQuery;
	}

	@Override
	public IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		IndexableActionableDynamicQuery indexableActionableDynamicQuery =
			new IndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setBaseLocalService(
			passwordMetaLocalService);
		indexableActionableDynamicQuery.setClassLoader(getClassLoader());
		indexableActionableDynamicQuery.setModelClass(PasswordMeta.class);

		indexableActionableDynamicQuery.setPrimaryKeyPropertyName(
			"passwordMetaId");

		return indexableActionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {

		actionableDynamicQuery.setBaseLocalService(passwordMetaLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(PasswordMeta.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("passwordMetaId");
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {

		return passwordMetaLocalService.deletePasswordMeta(
			(PasswordMeta)persistedModel);
	}

	public BasePersistence<PasswordMeta> getBasePersistence() {
		return passwordMetaPersistence;
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return passwordMetaPersistence.findByPrimaryKey(primaryKeyObj);
	}

	/**
	 * Returns the password meta with the matching UUID and company.
	 *
	 * @param uuid the password meta's UUID
	 * @param companyId the primary key of the company
	 * @return the matching password meta
	 * @throws PortalException if a matching password meta could not be found
	 */
	@Override
	public PasswordMeta getPasswordMetaByUuidAndCompanyId(
			String uuid, long companyId)
		throws PortalException {

		return passwordMetaPersistence.findByUuid_C_First(
			uuid, companyId, null);
	}

	/**
	 * Returns a range of all the password metas.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.portal.security.password.model.impl.PasswordMetaModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of password metas
	 * @param end the upper bound of the range of password metas (not inclusive)
	 * @return the range of password metas
	 */
	@Override
	public List<PasswordMeta> getPasswordMetas(int start, int end) {
		return passwordMetaPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of password metas.
	 *
	 * @return the number of password metas
	 */
	@Override
	public int getPasswordMetasCount() {
		return passwordMetaPersistence.countAll();
	}

	/**
	 * Updates the password meta in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param passwordMeta the password meta
	 * @return the password meta that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public PasswordMeta updatePasswordMeta(PasswordMeta passwordMeta) {
		return passwordMetaPersistence.update(passwordMeta);
	}

	@Override
	public Class<?>[] getAopInterfaces() {
		return new Class<?>[] {
			PasswordMetaLocalService.class, IdentifiableOSGiService.class,
			PersistedModelLocalService.class
		};
	}

	@Override
	public void setAopProxy(Object aopProxy) {
		passwordMetaLocalService = (PasswordMetaLocalService)aopProxy;
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return PasswordMetaLocalService.class.getName();
	}

	protected Class<?> getModelClass() {
		return PasswordMeta.class;
	}

	protected String getModelClassName() {
		return PasswordMeta.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = passwordMetaPersistence.getDataSource();

			DB db = DBManagerUtil.getDB();

			sql = db.buildSQL(sql);
			sql = PortalUtil.transformSQL(sql);

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(
				dataSource, sql);

			sqlUpdate.update();
		}
		catch (Exception exception) {
			throw new SystemException(exception);
		}
	}

	@Reference
	protected PasswordEntryPersistence passwordEntryPersistence;

	@Reference
	protected PasswordHashGeneratorPersistence passwordHashGeneratorPersistence;

	protected PasswordMetaLocalService passwordMetaLocalService;

	@Reference
	protected PasswordMetaPersistence passwordMetaPersistence;

	@Reference
	protected com.liferay.counter.kernel.service.CounterLocalService
		counterLocalService;

	@Reference
	protected com.liferay.portal.kernel.service.ClassNameLocalService
		classNameLocalService;

	@Reference
	protected com.liferay.portal.kernel.service.ResourceLocalService
		resourceLocalService;

	@Reference
	protected com.liferay.portal.kernel.service.UserLocalService
		userLocalService;

}