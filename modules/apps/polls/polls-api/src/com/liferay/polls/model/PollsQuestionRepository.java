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

package com.liferay.polls.model;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.kernel.Repository;
import com.liferay.polls.exception.NoSuchQuestionException;
import com.liferay.polls.service.PollsQuestionLocalService;
import com.liferay.polls.service.permission.PollsQuestionPermission;
import com.liferay.polls.service.persistence.PollsChoicePersistence;
import com.liferay.polls.service.persistence.PollsQuestionPersistence;
import com.liferay.portal.NoSuchModelException;
import com.liferay.portal.kernel.CompanyProvider;
import com.liferay.portal.kernel.GroupProvider;
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.TransactionAttribute;
import com.liferay.portal.kernel.transaction.TransactionInvokerUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.ResourceLocalService;
import java8.util.Optional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * [[@]] Trying a Repo.
 */
public class PollsQuestionRepository implements Repository<PollsQuestion> {

	public static final String ID_PREFIX = "PollsQuestion:";

	@Override
	public Optional<PollsQuestion> retrieve(String id) {
		String longString = parseId(id);

		try {
			long primaryKey = GetterUtil.getLong(longString);

			PollsQuestion pollsQuestion =
				pollsQuestionPersistence.findByPrimaryKey(primaryKey);

			pollsQuestion.addChoices(
				pollsChoicePersistence.findByQuestionId(primaryKey));

			return Optional.of(pollsQuestion);
		}
		catch (NoSuchQuestionException e) {
			return Optional.empty();
		}
		catch (NumberFormatException e) {
			throw new IllegalArgumentException(
				"Invalid id for this repo:\nRepo: "+ this +"\nId: " + id);
		}
	}

	private String parseId(String rawString) {
		if (!rawString.startsWith(ID_PREFIX)) {
			throw new IllegalArgumentException(
				"Invalid id for this repo:\nRepo: "+ this +"\nId: " + rawString);
		}

		return rawString.substring(14);
	}

	@Override
	public Optional<String> toId(PollsQuestion pq) {
		if (pq.getPrimaryKey() == 0) {
			return Optional.empty();
		}

		return Optional.of(doToId(pq));
	}

	@Override
	public void delete(String id) {

		long questionId = Long.parseLong(parseId(id));

		try {
			_pollsQuestionPermission.check(
				PermissionThreadLocal.getPermissionChecker(),
				questionId, ActionKeys.DELETE);
		}
		catch (PortalException e) {
			throw new RuntimeException(e);
		}

		try {
			pollsQuestionPersistence.remove(questionId);
		}
		catch (NoSuchQuestionException e) {
			//TODO: [[@]] Should we care?!?!
		}
	}

	@Override
	public void delete(PollsQuestion question) {
		try {
			_pollsQuestionPermission.check(
				PermissionThreadLocal.getPermissionChecker(),
				question.getQuestionId(), ActionKeys.DELETE);


			pollsQuestionPersistence.remove(question);

			// Resources

			long companyId = _companyProvider.get().getCompanyId();

			resourceLocalService.deleteResource(
				companyId, PollsQuestion.class.getName(),
				ResourceConstants.SCOPE_INDIVIDUAL, question.getQuestionId());

			// Choices

			List<PollsChoice> choices = question.getChoices();

			for (PollsChoice choice : choices) {
				remove(choice);
			}
		}
		catch (PortalException e) {
			throw new RuntimeException(e);
		}
	}

	private void remove(PollsChoice choice) {
		try {
			pollsQuestionPersistence.remove(choice);
		} catch (NoSuchModelException e) {
			// TODO: [[@]] Should we care?!?!
		}
	}

	protected String doToId(PollsQuestion pq) {
		return ID_PREFIX + Long.toString(pq.getPrimaryKey());
	}

	@Override
	public String store(final PollsQuestion pq) {
		try {
			/* [[@]] should repo validate? should not - we should validate
			 entity only when it is actually modified. but we can't move from
			 here.
			 I guess the repo should validate and the model could cache the
			 validation. Validation should not throw exceptions but to
			 return a List<ConstraintValidation> as in JSR 303. If the model
			 has not been modified it could return always the same set of
			 validations. Although properly invalidate caches is always tricky*/
			pq.validate();
		} catch (PortalException pe) {
			throw new IllegalStateException(pe);
		}

		try {
			TransactionAttribute.Builder builder =
				new TransactionAttribute.Builder();

			TransactionAttribute transactionAttribute = builder.setPropagation(
				Propagation.SUPPORTS).build();

			final Date now = new Date();

			TransactionInvokerUtil.invoke(
				transactionAttribute, new Callable<Void>() {

					@Override
					public Void call() throws Exception {
						if (pq.isNew()) {
							long scopeGroupId =
								_groupProvider.get().getGroupId();

							_pollsQuestionPermission.check(
								PermissionThreadLocal.getPermissionChecker(),
								scopeGroupId, ActionKeys.ADD_QUESTION);

							pq.setCreateDate(now);
							pq.setPrimaryKey(
								CounterLocalServiceUtil.increment());

							// [[*]] REMOVE THIS

							pq.setCompanyId(_companyProvider.get().getCompanyId());
							pq.setGroupId(scopeGroupId);

							pollsQuestionPersistence.update(pq);

							// Resources

							boolean defaultGroupPermissions =
								pq.getGroupPermissions() == null;

							boolean defaultGuestPermissions =
								pq.getGuestPermissions() == null;

							if (defaultGroupPermissions ||
								defaultGuestPermissions) {

								_pollsQuestionLocalService.addQuestionResources(
									pq, defaultGroupPermissions,
									defaultGuestPermissions);
							}
							else {
								_pollsQuestionLocalService.addQuestionResources(
									pq, pq.getGroupPermissions(),
									pq.getGuestPermissions());
							}
						}
						else {
							_pollsQuestionPermission.check(
								PermissionThreadLocal.getPermissionChecker(),
								pq.getQuestionId(), ActionKeys.UPDATE);
						}

						pq.setModifiedDate(now);

						//if (!pq._addedChoices.isEmpty()) {
							for (PollsChoice choice : pq.getChoices()) {
								long id = choice.getChoiceId();

								if (id == 0) {
									choice.setQuestionId(pq.getQuestionId());
									store(choice);
								}
							}
						//}

						pollsQuestionPersistence.update(pq);

						return null;
					}
				});
		}
		catch (Throwable throwable) {
			throw new RuntimeException(throwable);
		}

		pq.setNew(false);

		return doToId(pq);
	}


	public void store(final PollsChoice pc) {
		try {
			pc.validate();
		} catch (PortalException pe) {
			throw new IllegalStateException(pe);
		}
		try {
			TransactionAttribute.Builder builder =
				new TransactionAttribute.Builder();

			TransactionAttribute transactionAttribute = builder.setPropagation(
				Propagation.SUPPORTS).build();

			TransactionInvokerUtil.invoke(
				transactionAttribute, new Callable<Void>() {

					@Override
					public Void call() throws Exception {
						Date now = new Date();

						if (pc.isNew()) {
							pc.setCreateDate(now);
							pc.setPrimaryKey(CounterLocalServiceUtil.increment());
							pc.setCompanyId(pc.getPollsQuestion().getCompanyId());
							pc.setGroupId(pc.getPollsQuestion().getGroupId());
						}

						pc.setModifiedDate(now);

						pollsChoicePersistence.update(pc);

						return null;
					}
				}
			);
		}
		catch (Throwable throwable) {
			throw new RuntimeException(throwable);
		}

	}


	@BeanReference(type = PollsQuestionPersistence.class)
	protected PollsQuestionPersistence pollsQuestionPersistence;

	@BeanReference(type = PollsChoicePersistence.class)
	protected PollsChoicePersistence pollsChoicePersistence;

	@BeanReference(type = ResourceLocalService.class)
	protected ResourceLocalService resourceLocalService;

	@BeanReference(type = GroupProvider.class)
	protected GroupProvider _groupProvider;

	@BeanReference(type = CompanyProvider.class)
	protected CompanyProvider _companyProvider;

	@BeanReference(type = PollsQuestionPermission.class)
	private PollsQuestionPermission _pollsQuestionPermission;

	@BeanReference(type = PollsQuestionLocalService.class)
	private PollsQuestionLocalService _pollsQuestionLocalService;

}