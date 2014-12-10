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
import com.liferay.portal.kernel.ServiceScope;
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.util.Function;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.User;
import com.liferay.services.v2.Command;
import com.liferay.services.v2.ConstraintViolation;
import com.liferay.services.v2.Filter;
import com.liferay.services.v2.ModelAction;
import com.liferay.services.v2.MultipleProducer;
import com.liferay.services.v2.MultipleRenderer;
import com.liferay.services.v2.Result;
import com.liferay.services.v2.SingleProducer;
import com.liferay.services.v2.SingleRenderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;


/**
 * @author Carlos Sierra Andrés
 */
public class PollsServiceImpl implements PollsService {

	private PortalContextProvider _portalContextProvider;

	@Override
	public void setContextProvider(PortalContextProvider provider) {
		this._portalContextProvider = provider;
	}

	@Override
	public SingleProducer<PollsQuestionQuerier, PollsContext> create(
		ModelAction<PollsBuilder> consumer) {

		final PollsQuestionImpl pollsQuestion = new PollsQuestionImpl();

		final ArrayList<PollsChoice> addedChoices = new ArrayList<>();

		consumer.consume(new PollsBuilderImpl(pollsQuestion, addedChoices));

		Group group = _serviceScope.getGroup();
		User user = _serviceScope.getUser();

		pollsQuestion.setCreateDate(_dateContext.getCurrentDate());
		pollsQuestion.setCompanyId(group.getCompanyId());
		pollsQuestion.setGroupId(group.getGroupId());
		pollsQuestion.setModifiedDate(_dateContext.getCurrentDate());
		pollsQuestion.setPrimaryKey(_counterLocalService.increment());
		pollsQuestion.setUserId(user.getUserId());
		pollsQuestion.setUserName(user.getFullName());
		pollsQuestion.setUuid(PortalUUIDUtil.generate());

		_pollsQuestionPersistence.update(pollsQuestion);

		for (PollsChoice choice : addedChoices) {
			choice.setUuid(PortalUUIDUtil.generate());
			choice.setGroupId(pollsQuestion.getGroupId());
			choice.setCompanyId(pollsQuestion.getCompanyId());
			choice.setUserId(pollsQuestion.getUserId());
			choice.setUserName(pollsQuestion.getUserName());
			choice.setCreateDate(pollsQuestion.getCreateDate());
			choice.setModifiedDate(pollsQuestion.getModifiedDate());
			choice.setQuestionId(pollsQuestion.getQuestionId());

			_pollsChoicePersistence.update(choice);
		}

		final PollsContext pollsContext = new PollsContext();

		final PollsQuestionQuerier querierFromBuilder =
			new PollsQuestionQuerierFromBuilder(
				pollsQuestion, addedChoices);

		return new DefaultSingleProducer(pollsContext, querierFromBuilder);
	}

	@Override
	public SingleProducer<PollsQuestionQuerier, PollsContext> with(String id) {

		PollsContext pollsContext = new PollsContext();

		PollsQuestion pollsQuestion =
			_pollsQuestionPersistence.fetchByPrimaryKey(Long.parseLong(id));

		return new DefaultSingleProducer(pollsContext, pollsQuestion);
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

			return new EmptyMultipleProducer(pollsContext.getViolations());
		}

		return new DefaultMultipleProducer(pollsContext, questions);

	}

	@Override
	public MultipleProducer<PollsQuestionQuerier, PollsContext> all() {
		final PollsContext pollsContext = new PollsContext();

		List<PollsQuestion> pollsQuestions =
			_pollsQuestionPersistence.findAll();

		return new DefaultMultipleProducer(pollsContext, pollsQuestions);
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
	ServiceScope _serviceScope;

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

	private static class SingleResult<R> implements Result<R> {
		private final List<ConstraintViolation> _violations;
		private final R result;

		public SingleResult(List<ConstraintViolation> violations, R result) {
			_violations = violations;
			this.result = result;
		}

		@Override
		public List<ConstraintViolation> getViolations() {
			return _violations;
		}

		@Override
		public R get() {
			return result;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

	}

	private static class DefaultSingleProducer
		implements SingleProducer<PollsQuestionQuerier, PollsContext> {

		private final PollsContext _pollsContext;
		private final PollsQuestionQuerier _querierFromBuilder;

		public DefaultSingleProducer(
			PollsContext pollsContext,
			PollsQuestionQuerier querierFromBuilder) {

			_pollsContext = pollsContext;
			_querierFromBuilder = querierFromBuilder;
		}

		@Override
		public SingleRenderer<PollsQuestionQuerier> execute(
			Command<PollsContext, PollsQuestionQuerier> consumer) {

			consumer.execute(
				_pollsContext, _querierFromBuilder);

			return new SingleRenderer<PollsQuestionQuerier>() {
				@Override
				public <R> Result<R> map(
					final Function<PollsQuestionQuerier, R> function) {

					return new SingleResult<>(
						_pollsContext.getViolations(),
						function.apply(_querierFromBuilder));
				}
			};
		}

		@Override
		public <R> Result<R> map(
			final Function<PollsQuestionQuerier, R> function) {

			return new SingleResult<>(
				_pollsContext.getViolations(),
				function.apply(_querierFromBuilder));
		}
	}

	private static class EmptyMultipleProducer
		implements MultipleProducer<PollsQuestionQuerier, PollsContext> {

		private List<ConstraintViolation> _violations;

		public EmptyMultipleProducer(List<ConstraintViolation> violations) {
			_violations = violations;
		}

		@Override
		public <R> Result<List<R>> map(
			Function<PollsQuestionQuerier, R> function) {

			return new EmptyResult<>(_violations);
		}

		@Override
		public MultipleRenderer execute(
			Command<PollsContext, PollsQuestionQuerier> command) {
			return null;
		}

		private class EmptyResult<R> implements Result<List<R>> {

			private List<ConstraintViolation> _violations;

			public EmptyResult(List<ConstraintViolation> violations) {

				_violations = violations;
			}

			@Override
			public List<ConstraintViolation> getViolations() {
				return _violations;
			}

			@Override
			public List<R> get() {
				return null;
			}

			@Override
			public boolean isEmpty() {
				return true;
			}
		}
	}

	private static class MultipleResult<R> implements Result<List<R>> {

		private List<ConstraintViolation> _violations;
		private List<R> result;

		public MultipleResult(
			List<ConstraintViolation> violations, List<R> result) {

			_violations = violations;
			this.result = result;
		}

		@Override
		public List<ConstraintViolation> getViolations() {
			return _violations;
		}

		@Override
		public List<R> get() {
			return result;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

	}

	private static class DefaultMultipleProducer
		implements MultipleProducer<PollsQuestionQuerier, PollsContext> {

		private final PollsContext _pollsContext;
		private final List<PollsQuestion> _questions;

		public DefaultMultipleProducer(
			PollsContext pollsContext, List<PollsQuestion> questions) {

			_pollsContext = pollsContext;
			_questions = questions;
		}

		@Override
		public <R> Result<List<R>> map(
			Function<PollsQuestionQuerier, R> function) {

			return new MultipleResult<>(
				_pollsContext.getViolations(),
				ListUtil.map(function, _questions));
		}

		@Override
		public MultipleRenderer<PollsQuestionQuerier> execute(
			Command<PollsContext, PollsQuestionQuerier> command) {

			for (PollsQuestion question : _questions) {
				command.execute(_pollsContext, question);
			}

			return new MultipleRenderer<PollsQuestionQuerier>() {

				@Override
				public <R> Result<List<R>> materialize(
					Function<PollsQuestionQuerier, R> function) {

					return new MultipleResult<>(
						_pollsContext.getViolations(),
						ListUtil.map(function, _questions));
				}
			};
		}

	}
}
