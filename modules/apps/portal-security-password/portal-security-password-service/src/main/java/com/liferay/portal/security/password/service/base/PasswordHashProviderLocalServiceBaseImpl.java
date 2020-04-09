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

import com.liferay.exportimport.kernel.lar.ExportImportHelperUtil;
import com.liferay.exportimport.kernel.lar.ManifestSummary;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DefaultActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery;
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
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.security.password.model.PasswordHashProvider;
import com.liferay.portal.security.password.service.PasswordHashProviderLocalService;
import com.liferay.portal.security.password.service.persistence.PasswordEntryPersistence;
import com.liferay.portal.security.password.service.persistence.PasswordHashProviderPersistence;
import com.liferay.portal.security.password.service.persistence.PasswordMetaPersistence;

import java.io.Serializable;

import java.util.List;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Reference;

/**
 * Provides the base implementation for the password hash provider local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.portal.security.password.service.impl.PasswordHashProviderLocalServiceImpl}.
 * </p>
 *
 * @author Arthur Chan
 * @see com.liferay.portal.security.password.service.impl.PasswordHashProviderLocalServiceImpl
 * @generated
 */
public abstract class PasswordHashProviderLocalServiceBaseImpl
	extends BaseLocalServiceImpl
	implements AopService, IdentifiableOSGiService,
			   PasswordHashProviderLocalService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>PasswordHashProviderLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>com.liferay.portal.security.password.service.PasswordHashProviderLocalServiceUtil</code>.
	 */

	/**
	 * Adds the password hash provider to the database. Also notifies the appropriate model listeners.
	 *
	 * @param passwordHashProvider the password hash provider
	 * @return the password hash provider that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public PasswordHashProvider addPasswordHashProvider(
		PasswordHashProvider passwordHashProvider) {

		passwordHashProvider.setNew(true);

		return passwordHashProviderPersistence.update(passwordHashProvider);
	}

	/**
	 * Creates a new password hash provider with the primary key. Does not add the password hash provider to the database.
	 *
	 * @param passwordHashProviderId the primary key for the new password hash provider
	 * @return the new password hash provider
	 */
	@Override
	@Transactional(enabled = false)
	public PasswordHashProvider createPasswordHashProvider(
		long passwordHashProviderId) {

		return passwordHashProviderPersistence.create(passwordHashProviderId);
	}

	/**
	 * Deletes the password hash provider with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param passwordHashProviderId the primary key of the password hash provider
	 * @return the password hash provider that was removed
	 * @throws PortalException if a password hash provider with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public PasswordHashProvider deletePasswordHashProvider(
			long passwordHashProviderId)
		throws PortalException {

		return passwordHashProviderPersistence.remove(passwordHashProviderId);
	}

	/**
	 * Deletes the password hash provider from the database. Also notifies the appropriate model listeners.
	 *
	 * @param passwordHashProvider the password hash provider
	 * @return the password hash provider that was removed
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public PasswordHashProvider deletePasswordHashProvider(
		PasswordHashProvider passwordHashProvider) {

		return passwordHashProviderPersistence.remove(passwordHashProvider);
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(
			PasswordHashProvider.class, clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return passwordHashProviderPersistence.findWithDynamicQuery(
			dynamicQuery);
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
	@Override
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return passwordHashProviderPersistence.findWithDynamicQuery(
			dynamicQuery, start, end);
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
	@Override
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<T> orderByComparator) {

		return passwordHashProviderPersistence.findWithDynamicQuery(
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
		return passwordHashProviderPersistence.countWithDynamicQuery(
			dynamicQuery);
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

		return passwordHashProviderPersistence.countWithDynamicQuery(
			dynamicQuery, projection);
	}

	@Override
	public PasswordHashProvider fetchPasswordHashProvider(
		long passwordHashProviderId) {

		return passwordHashProviderPersistence.fetchByPrimaryKey(
			passwordHashProviderId);
	}

	/**
	 * Returns the password hash provider with the matching UUID and company.
	 *
	 * @param uuid the password hash provider's UUID
	 * @param companyId the primary key of the company
	 * @return the matching password hash provider, or <code>null</code> if a matching password hash provider could not be found
	 */
	@Override
	public PasswordHashProvider fetchPasswordHashProviderByUuidAndCompanyId(
		String uuid, long companyId) {

		return passwordHashProviderPersistence.fetchByUuid_C_First(
			uuid, companyId, null);
	}

	/**
	 * Returns the password hash provider with the primary key.
	 *
	 * @param passwordHashProviderId the primary key of the password hash provider
	 * @return the password hash provider
	 * @throws PortalException if a password hash provider with the primary key could not be found
	 */
	@Override
	public PasswordHashProvider getPasswordHashProvider(
			long passwordHashProviderId)
		throws PortalException {

		return passwordHashProviderPersistence.findByPrimaryKey(
			passwordHashProviderId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery =
			new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(
			passwordHashProviderLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(PasswordHashProvider.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName(
			"passwordHashProviderId");

		return actionableDynamicQuery;
	}

	@Override
	public IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		IndexableActionableDynamicQuery indexableActionableDynamicQuery =
			new IndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setBaseLocalService(
			passwordHashProviderLocalService);
		indexableActionableDynamicQuery.setClassLoader(getClassLoader());
		indexableActionableDynamicQuery.setModelClass(
			PasswordHashProvider.class);

		indexableActionableDynamicQuery.setPrimaryKeyPropertyName(
			"passwordHashProviderId");

		return indexableActionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {

		actionableDynamicQuery.setBaseLocalService(
			passwordHashProviderLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(PasswordHashProvider.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName(
			"passwordHashProviderId");
	}

	@Override
	public ExportActionableDynamicQuery getExportActionableDynamicQuery(
		final PortletDataContext portletDataContext) {

		final ExportActionableDynamicQuery exportActionableDynamicQuery =
			new ExportActionableDynamicQuery() {

				@Override
				public long performCount() throws PortalException {
					ManifestSummary manifestSummary =
						portletDataContext.getManifestSummary();

					StagedModelType stagedModelType = getStagedModelType();

					long modelAdditionCount = super.performCount();

					manifestSummary.addModelAdditionCount(
						stagedModelType, modelAdditionCount);

					long modelDeletionCount =
						ExportImportHelperUtil.getModelDeletionCount(
							portletDataContext, stagedModelType);

					manifestSummary.addModelDeletionCount(
						stagedModelType, modelDeletionCount);

					return modelAdditionCount;
				}

			};

		initActionableDynamicQuery(exportActionableDynamicQuery);

		exportActionableDynamicQuery.setAddCriteriaMethod(
			new ActionableDynamicQuery.AddCriteriaMethod() {

				@Override
				public void addCriteria(DynamicQuery dynamicQuery) {
					portletDataContext.addDateRangeCriteria(
						dynamicQuery, "modifiedDate");
				}

			});

		exportActionableDynamicQuery.setCompanyId(
			portletDataContext.getCompanyId());

		exportActionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod
				<PasswordHashProvider>() {

				@Override
				public void performAction(
						PasswordHashProvider passwordHashProvider)
					throws PortalException {

					StagedModelDataHandlerUtil.exportStagedModel(
						portletDataContext, passwordHashProvider);
				}

			});
		exportActionableDynamicQuery.setStagedModelType(
			new StagedModelType(
				PortalUtil.getClassNameId(
					PasswordHashProvider.class.getName())));

		return exportActionableDynamicQuery;
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {

		return passwordHashProviderLocalService.deletePasswordHashProvider(
			(PasswordHashProvider)persistedModel);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return passwordHashProviderPersistence.findByPrimaryKey(primaryKeyObj);
	}

	/**
	 * Returns the password hash provider with the matching UUID and company.
	 *
	 * @param uuid the password hash provider's UUID
	 * @param companyId the primary key of the company
	 * @return the matching password hash provider
	 * @throws PortalException if a matching password hash provider could not be found
	 */
	@Override
	public PasswordHashProvider getPasswordHashProviderByUuidAndCompanyId(
			String uuid, long companyId)
		throws PortalException {

		return passwordHashProviderPersistence.findByUuid_C_First(
			uuid, companyId, null);
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
	@Override
	public List<PasswordHashProvider> getPasswordHashProviders(
		int start, int end) {

		return passwordHashProviderPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of password hash providers.
	 *
	 * @return the number of password hash providers
	 */
	@Override
	public int getPasswordHashProvidersCount() {
		return passwordHashProviderPersistence.countAll();
	}

	/**
	 * Updates the password hash provider in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param passwordHashProvider the password hash provider
	 * @return the password hash provider that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public PasswordHashProvider updatePasswordHashProvider(
		PasswordHashProvider passwordHashProvider) {

		return passwordHashProviderPersistence.update(passwordHashProvider);
	}

	@Override
	public Class<?>[] getAopInterfaces() {
		return new Class<?>[] {
			PasswordHashProviderLocalService.class,
			IdentifiableOSGiService.class, PersistedModelLocalService.class
		};
	}

	@Override
	public void setAopProxy(Object aopProxy) {
		passwordHashProviderLocalService =
			(PasswordHashProviderLocalService)aopProxy;
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return PasswordHashProviderLocalService.class.getName();
	}

	protected Class<?> getModelClass() {
		return PasswordHashProvider.class;
	}

	protected String getModelClassName() {
		return PasswordHashProvider.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource =
				passwordHashProviderPersistence.getDataSource();

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

	protected PasswordHashProviderLocalService passwordHashProviderLocalService;

	@Reference
	protected PasswordHashProviderPersistence passwordHashProviderPersistence;

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