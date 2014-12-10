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

package com.liferay.polls.service.v2;

import com.liferay.counter.service.CounterLocalService;
import com.liferay.polls.model.PollsChoice;
import com.liferay.polls.model.PollsQuestion;
import com.liferay.polls.model.PollsQuestionWrapper;
import com.liferay.polls.model.impl.PollsQuestionImpl;
import com.liferay.polls.model.impl.v2.PollsBuilderImpl;
import com.liferay.polls.model.v2.PollsBuilder;
import com.liferay.polls.model.v2.PollsChoiceQuerier;
import com.liferay.polls.model.v2.PollsQuestionQuerier;
import com.liferay.polls.model.v2.PollsVoteQuerier;
import com.liferay.polls.service.persistence.PollsChoicePersistence;
import com.liferay.polls.service.persistence.PollsQuestionPersistence;
import com.liferay.portal.kernel.DateContext;
import com.liferay.portal.kernel.AuditableScope;
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.Function;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.service.ResourceLocalServiceUtil;
import com.liferay.services.v2.Command;
import com.liferay.services.v2.ConstraintViolation;
import com.liferay.services.v2.Filter;
import com.liferay.services.v2.ModelAction;
import com.liferay.services.v2.MultipleProducer;
import com.liferay.services.v2.SingleProducer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;


/**
 * @author Carlos Sierra Andrés
 */
public class PollsServiceImpl implements PollsService {

	@Override
	public SingleProducer<PollsQuestionQuerier, PollsContext> create(
		ModelAction<PollsBuilder> consumer) {

		final PollsQuestionImpl pollsQuestion = new PollsQuestionImpl();

		final ArrayList<PollsChoice> addedChoices = new ArrayList<>();

		consumer.consume(new PollsBuilderImpl(pollsQuestion, addedChoices));

		_auditableScope.propagate(pollsQuestion);

		pollsQuestion.setPrimaryKey(_counterLocalService.increment());
		pollsQuestion.setUuid(PortalUUIDUtil.generate());

		_pollsQuestionPersistence.update(pollsQuestion);

		for (PollsChoice choice : addedChoices) {
			choice.setQuestionId(pollsQuestion.getQuestionId());
			choice.setUuid(PortalUUIDUtil.generate());

			_auditableScope.propagate(choice);

			_pollsChoicePersistence.update(choice);
		}

		final PollsContext pollsContext = new PollsContext();

		final PollsQuestionQuerier querierFromBuilder =
			new PollsQuestionQuerierFromBuilder(
				pollsQuestion, addedChoices);

		addGroupPermissions().execute(pollsContext, querierFromBuilder);
		addGuestPermissions().execute(pollsContext, querierFromBuilder);

		return new DefaultSingleProducer<>(pollsContext, querierFromBuilder);
	}

	public static Command<PollsContext, PollsQuestionQuerier>
		addGroupPermissions() {

		return new Command<PollsContext, PollsQuestionQuerier>() {
			@Override
			public void execute(
				PollsContext context, PollsQuestionQuerier question) {

				try {
					ResourceLocalServiceUtil.addResources(
						question.getCompanyId(), question.getGroupId(),
						question.getUserId(), PollsQuestion.class.getName(),
						question.getQuestionId(), false, true, false);
				}
				catch (PortalException e) {
					context.addViolation(new ConstraintViolation(e));
				}
			}
		};
	}

	public static Command<PollsContext, PollsQuestionQuerier>
		addGuestPermissions() {

		return new Command<PollsContext, PollsQuestionQuerier>() {
			@Override
			public void execute(
				PollsContext context, PollsQuestionQuerier question) {

				try {
					ResourceLocalServiceUtil.addResources(
						question.getCompanyId(), question.getGroupId(),
						question.getUserId(), PollsQuestion.class.getName(),
						question.getQuestionId(), false, false, true);
				}
				catch (PortalException e) {
					context.addViolation(new ConstraintViolation(e));
				}
			}
		};
	}

	@Override
	public SingleProducer<PollsQuestionQuerier, PollsContext> with(String id) {

		PollsContext pollsContext = new PollsContext();

		PollsQuestion pollsQuestion =
			_pollsQuestionPersistence.fetchByPrimaryKey(Long.parseLong(id));

		return new DefaultSingleProducer<PollsQuestionQuerier, PollsContext>(
			pollsContext, pollsQuestion);
	}

	@Override
	public MultipleProducer<PollsQuestionQuerier, PollsContext> find(
		Filter<PollsContext> filter) {

		final PollsContext pollsContext = new PollsContext();

		filter.execute(pollsContext);

		final List<PollsQuestion> questions;

		try {
			Callable<List<PollsQuestion>> filterCallable =
				pollsContext.getFilterCallable();

			questions = filterCallable.call();
		}
		catch (Exception e) {
			pollsContext.addViolation(null);

			return new EmptyMultipleProducer<>(pollsContext.getViolations());
		}

		return new DefaultMultipleProducer<PollsQuestionQuerier, PollsContext>(
			pollsContext, questions);

	}

	@Override
	public MultipleProducer<PollsQuestionQuerier, PollsContext> all() {
		final PollsContext pollsContext = new PollsContext();

		List<PollsQuestion> pollsQuestions =
			_pollsQuestionPersistence.findAll();

		return new DefaultMultipleProducer<PollsQuestionQuerier, PollsContext>(
			pollsContext, pollsQuestions);
	}

	@BeanReference
	CounterLocalService _counterLocalService;

	@BeanReference
	PollsQuestionPersistence _pollsQuestionPersistence;

	@BeanReference
	PollsChoicePersistence _pollsChoicePersistence;

	@BeanReference
	DateContext _dateContext;

	@BeanReference
	AuditableScope _auditableScope;

	private static class PollsQuestionQuerierFromBuilder
		extends PollsQuestionWrapper {

		private final ArrayList<PollsChoice> _addedChoices;

		public PollsQuestionQuerierFromBuilder(
			PollsQuestionImpl pollsQuestion,
			ArrayList<PollsChoice> addedChoices) {

			super(pollsQuestion);

			_addedChoices = addedChoices;
		}

		@Override
		public <T> List<T> getChoices(
			Function<PollsChoiceQuerier, T> function) {

			return ListUtil.map(function, _addedChoices);
		}

		@Override
		public <T> List<T> getVotes(
			Function<PollsVoteQuerier, T> function) {

			return Collections.emptyList();
		}

	}

}
