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

package com.liferay.polls.repository;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.polls.model.PollsChoice;
import com.liferay.polls.model.impl.PollsQuestionImpl;
import com.liferay.polls.service.PollsQuestionLocalServiceUtil;
import com.liferay.polls.service.persistence.PollsQuestionUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.TransactionAttribute;
import com.liferay.portal.kernel.transaction.TransactionInvokerUtil;

import java.util.Date;
import java.util.concurrent.Callable;

/**
 * [[@]] Trying a Repo.
 */
public class PollsQuestionRepository {

	public void persist(final PollsQuestionImpl pq) {
		try {
			// [[@]] should repo validate? should not - we should validate
			// entity only when it is actually modified. but we can't move from
			// here.
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
							pq.setCreateDate(now);
							pq.setPrimaryKey(CounterLocalServiceUtil.increment());

							PollsQuestionUtil.update(pq);

							// Resources

							boolean defaultGroupPermissions =
								pq.getGroupPermissions() == null;

							boolean defaultGuestPermissions =
								pq.getGuestPermissions() == null;

							if (defaultGroupPermissions ||
								defaultGuestPermissions) {

								PollsQuestionLocalServiceUtil.addQuestionResources(
									pq, defaultGroupPermissions,
									defaultGuestPermissions);
							}
							else {
								PollsQuestionLocalServiceUtil.addQuestionResources(
									pq, pq.getGroupPermissions(),
									pq.getGuestPermissions());
							}
						}

						pq.setModifiedDate(now);

						//if (!pq._addedChoices.isEmpty()) {
							for (PollsChoice choice : pq.getAddedChoices()) {
								choice.setQuestionId(pq.getQuestionId());

								choice.persist();
							}
						//}

						PollsQuestionUtil.update(pq);

						return null;
					}
				});
		}
		catch (Throwable throwable) {
			throw new RuntimeException(throwable);
		}

		pq.clearAddedChoices();
		pq.setNew(false);
	}

}