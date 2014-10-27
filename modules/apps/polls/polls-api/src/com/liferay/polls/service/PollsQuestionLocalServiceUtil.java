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

package com.liferay.polls.service;

import aQute.bnd.annotation.ProviderType;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the local service utility for PollsQuestion. This utility wraps
 * {@link com.liferay.polls.service.impl.PollsQuestionLocalServiceImpl} and is the
 * primary access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Brian Wing Shun Chan
 * @see PollsQuestionLocalService
 * @see com.liferay.polls.service.base.PollsQuestionLocalServiceBaseImpl
 * @see com.liferay.polls.service.impl.PollsQuestionLocalServiceImpl
 * @generated
 */
@ProviderType
public class PollsQuestionLocalServiceUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to {@link com.liferay.polls.service.impl.PollsQuestionLocalServiceImpl} and rerun ServiceBuilder to regenerate this class.
	 */

	public static void addQuestionResources(
		com.liferay.polls.model.PollsQuestion question,
		boolean addGroupPermissions, boolean addGuestPermissions)
		throws com.liferay.portal.kernel.exception.PortalException {
		getService()
			.addQuestionResources(question, addGroupPermissions,
			addGuestPermissions);
	}

	public static void addQuestionResources(
		com.liferay.polls.model.PollsQuestion question,
		java.lang.String[] groupPermissions, java.lang.String[] guestPermissions)
		throws com.liferay.portal.kernel.exception.PortalException {
		getService()
			.addQuestionResources(question, groupPermissions, guestPermissions);
	}

	public static void addQuestionResources(long questionId,
		boolean addGroupPermissions, boolean addGuestPermissions)
		throws com.liferay.portal.kernel.exception.PortalException {
		getService()
			.addQuestionResources(questionId, addGroupPermissions,
			addGuestPermissions);
	}

	public static void addQuestionResources(long questionId,
		java.lang.String[] groupPermissions, java.lang.String[] guestPermissions)
		throws com.liferay.portal.kernel.exception.PortalException {
		getService()
			.addQuestionResources(questionId, groupPermissions, guestPermissions);
	}

	/**
	* Deletes the polls question from the database. Also notifies the appropriate model listeners.
	*
	* @param pollsQuestion the polls question
	* @return the polls question that was removed
	*/
	public static com.liferay.polls.model.PollsQuestion deletePollsQuestion(
		com.liferay.polls.model.PollsQuestion pollsQuestion) {
		return getService().deletePollsQuestion(pollsQuestion);
	}

	/**
	* Deletes the polls question with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param questionId the primary key of the polls question
	* @return the polls question that was removed
	*/
	public static com.liferay.polls.model.PollsQuestion deletePollsQuestion(
		long questionId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService().deletePollsQuestion(questionId);
	}

	public static void deleteQuestion(
		com.liferay.polls.model.PollsQuestion question)
		throws com.liferay.portal.kernel.exception.PortalException {
		getService().deleteQuestion(question);
	}

	public static void deleteQuestion(long questionId)
		throws com.liferay.portal.kernel.exception.PortalException {
		getService().deleteQuestion(questionId);
	}

	public static void deleteQuestions(long groupId)
		throws com.liferay.portal.kernel.exception.PortalException {
		getService().deleteQuestions(groupId);
	}

	public static com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
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
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.polls.model.PollsQuestionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.polls.model.PollsQuestionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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
		return getService()
				   .dynamicQuery(dynamicQuery, start, end, orderByComparator);
	}

	/**
	* Returns the number of rows that match the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @return the number of rows that match the dynamic query
	*/
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {
		return getService().dynamicQueryCount(dynamicQuery);
	}

	/**
	* Returns the number of rows that match the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @param projection the projection to apply to the query
	* @return the number of rows that match the dynamic query
	*/
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {
		return getService().dynamicQueryCount(dynamicQuery, projection);
	}

	public static com.liferay.polls.model.PollsQuestion fetchPollsQuestion(
		long questionId) {
		return getService().fetchPollsQuestion(questionId);
	}

	/**
	* Returns the polls question matching the UUID and group.
	*
	* @param uuid the polls question's UUID
	* @param groupId the primary key of the group
	* @return the matching polls question, or <code>null</code> if a matching polls question could not be found
	*/
	public static com.liferay.polls.model.PollsQuestion fetchPollsQuestionByUuidAndGroupId(
		java.lang.String uuid, long groupId) {
		return getService().fetchPollsQuestionByUuidAndGroupId(uuid, groupId);
	}

	public static com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery getActionableDynamicQuery() {
		return getService().getActionableDynamicQuery();
	}

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	public static java.lang.String getBeanIdentifier() {
		return getService().getBeanIdentifier();
	}

	public static com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery getExportActionableDynamicQuery(
		com.liferay.portal.kernel.lar.PortletDataContext portletDataContext) {
		return getService().getExportActionableDynamicQuery(portletDataContext);
	}

	/**
	* Returns the polls question with the primary key.
	*
	* @param questionId the primary key of the polls question
	* @return the polls question
	* @throws PortalException if a polls question with the primary key could not be found
	*/
	public static com.liferay.polls.model.PollsQuestion getPollsQuestion(
		long questionId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService().getPollsQuestion(questionId);
	}

	/**
	* Returns the polls question matching the UUID and group.
	*
	* @param uuid the polls question's UUID
	* @param groupId the primary key of the group
	* @return the matching polls question
	* @throws PortalException if a matching polls question could not be found
	*/
	public static com.liferay.polls.model.PollsQuestion getPollsQuestionByUuidAndGroupId(
		java.lang.String uuid, long groupId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService().getPollsQuestionByUuidAndGroupId(uuid, groupId);
	}

	/**
	* Returns a range of all the polls questions.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.polls.model.PollsQuestionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of polls questions
	* @param end the upper bound of the range of polls questions (not inclusive)
	* @return the range of polls questions
	*/
	public static java.util.List<com.liferay.polls.model.PollsQuestion> getPollsQuestions(
		int start, int end) {
		return getService().getPollsQuestions(start, end);
	}

	public static java.util.List<com.liferay.polls.model.PollsQuestion> getPollsQuestionsByUuidAndCompanyId(
		java.lang.String uuid, long companyId) {
		return getService().getPollsQuestionsByUuidAndCompanyId(uuid, companyId);
	}

	public static java.util.List<com.liferay.polls.model.PollsQuestion> getPollsQuestionsByUuidAndCompanyId(
		java.lang.String uuid, long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<? extends com.liferay.polls.model.PollsQuestion> orderByComparator) {
		return getService()
				   .getPollsQuestionsByUuidAndCompanyId(uuid, companyId, start,
						end, orderByComparator);
	}

	/**
	* Returns the number of polls questions.
	*
	* @return the number of polls questions
	*/
	public static int getPollsQuestionsCount() {
		return getService().getPollsQuestionsCount();
	}

	public static com.liferay.polls.model.PollsQuestion getQuestion(
		long questionId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService().getQuestion(questionId);
	}

	public static java.util.List<com.liferay.polls.model.PollsQuestion> getQuestions(
		long groupId) {
		return getService().getQuestions(groupId);
	}

	public static java.util.List<com.liferay.polls.model.PollsQuestion> getQuestions(
		long groupId, int start, int end) {
		return getService().getQuestions(groupId, start, end);
	}

	public static int getQuestionsCount(long groupId) {
		return getService().getQuestionsCount(groupId);
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	public static void setBeanIdentifier(java.lang.String beanIdentifier) {
		getService().setBeanIdentifier(beanIdentifier);
	}

	public static PollsQuestionLocalService getService() {
		return _serviceTracker.getService();
	}

	/**
	 * @deprecated As of 6.2.0
	 */
	@Deprecated
	public void setService(PollsQuestionLocalService service) {
	}

	private static ServiceTracker<PollsQuestionLocalService, PollsQuestionLocalService> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(PollsQuestionLocalServiceUtil.class);

		_serviceTracker = new ServiceTracker<PollsQuestionLocalService, PollsQuestionLocalService>(bundle.getBundleContext(),
				PollsQuestionLocalService.class, null);

		_serviceTracker.open();
	}
}