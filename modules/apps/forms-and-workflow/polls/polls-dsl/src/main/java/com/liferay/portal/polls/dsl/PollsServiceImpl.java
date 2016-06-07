/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.polls.dsl;

import com.liferay.polls.model.PollsChoice;
import com.liferay.polls.model.PollsQuestion;
import com.liferay.polls.service.PollsChoiceLocalService;
import com.liferay.polls.service.PollsQuestionLocalService;
import com.liferay.polls.service.PollsVoteLocalService;
import com.liferay.polls.service.persistence.PollsQuestionFinder;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.polls.dsl.PollsContext.MultiplePollContext;
import com.liferay.portal.polls.dsl.PollsContext.SinglePollContext;
import com.liferay.portal.polls.dsl.PollsOperations.Interpreter;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.liferay.portal.polls.dsl.PollsContext.all;
import static com.liferay.portal.polls.dsl.PollsContext.findById;
import static com.liferay.portal.polls.dsl.QuestionQuerier.POLLS_QUESTION_WITH_CHOICES_AND_VOTES;

/**
 * @author Carlos Sierra Andrés
 */
@Component(
	immediate = true,
	property = {
		"osgi.command.function=findPoll",
		"osgi.command.function=findAll",
		"osgi.command.scope=dsl"
	}
)
public class PollsServiceImpl implements PollsService {

	public void findPoll(long userId, long id) {
		try {
			System.out.println(execute(
			   userId, new ServiceContext(),
			   findById(id), POLLS_QUESTION_WITH_CHOICES_AND_VOTES));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void findAll(long userId, long groupId) {
		try {
			System.out.println(execute(
				userId, new ServiceContext(),
				all(groupId), POLLS_QUESTION_WITH_CHOICES_AND_VOTES));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private volatile PollsQuestionLocalService _pollsQuestionLocalService;
	private volatile PollsChoiceLocalService _pollsChoiceLocalService;
	private volatile PollsVoteLocalService _pollsVoteLocalService;
	private volatile PollsQuestionFinder _pollsQuestionFinder;

	@Reference
	protected void setPollsQuestionFinder(PollsQuestionFinder pollsQuestionFinder) {
		_pollsQuestionFinder = pollsQuestionFinder;
	}

	@Reference
	protected void setPollsVoteLocalService(PollsVoteLocalService pollsVoteLocalService) {
		_pollsVoteLocalService = pollsVoteLocalService;
	}

	@Reference
	protected void setPollsChoiceLocalService(PollsChoiceLocalService pollsChoiceLocalService) {
		_pollsChoiceLocalService = pollsChoiceLocalService;
	}

	@Reference
	protected void setPollsQuestionLocalService(PollsQuestionLocalService pollsQuestionLocalService) {
		_pollsQuestionLocalService = pollsQuestionLocalService;
	}

	public <T> T execute(
		long userId, ServiceContext serviceContext,
		SinglePollContext pc, QuestionQuerier<T> querier) {

		Interpreter interpreter = getPollsOperationInterpreter();

		if (pc instanceof PollsContext.IdContext) {
			PollsContext.IdContext idContext = (PollsContext.IdContext) pc;

			PollsQuestion pollsQuestion =
				_pollsQuestionLocalService.fetchPollsQuestion(idContext._id);

			interpreter.interpret(
				userId, pollsQuestion, serviceContext,
				idContext.getPollsOperations());

			QuestionQuerier.PollsQuerierInterpreter pollsQuerierInterpreter =
				new QuestionQuerier.PollsQuerierInterpreter();

			return pollsQuerierInterpreter.interpret(pollsQuestion, querier);
		}

		return null;
	}

	public <T> List<T> execute(
		long userId, ServiceContext serviceContext,
		MultiplePollContext mpc, QuestionQuerier<T> querier) {

		if (mpc instanceof PollsContext.All) {
			PollsContext.All all = (PollsContext.All) mpc;

			List<PollsQuestion> questions =
				_pollsQuestionLocalService.getPollsQuestions(-1, -1);

			QuestionQuerier.PollsQuerierInterpreter pollsQuerierInterpreter =
				new QuestionQuerier.PollsQuerierInterpreter();

			return questions.stream().map(pq ->
				pollsQuerierInterpreter.interpret(pq, querier)).
				collect(Collectors.toList());
		};

		return null;
	}

	public <C extends PollsContext<C>> void execute(
		long userId, ServiceContext serviceContext,
		PollsContext<C> pc) {

		Interpreter pollsOperationInterpreter =
			getPollsOperationInterpreter();

		if (pc instanceof PollsContext.IdContext) {
			PollsContext.IdContext idContext = (PollsContext.IdContext) pc;

			PollsQuestion pollsQuestion =
				_pollsQuestionLocalService.fetchPollsQuestion(idContext._id);

			pollsOperationInterpreter.interpret(
				userId, pollsQuestion, serviceContext,
				idContext.getPollsOperations());
		}

		if (pc instanceof PollsContext.CreateContext) {
			PollsContext.CreateContext createContext =
				(PollsContext.CreateContext) pc;

			MyQuestionBuilder questionBuilder = new MyQuestionBuilder();

			createContext._pollsQuestionBuilder.apply(questionBuilder);

			Integer day =
				questionBuilder._maybeDate.map(Date::getDay).orElse(0);
			Integer month =
				questionBuilder._maybeDate.map(Date::getMonth).orElse(0);
			Integer year =
				questionBuilder._maybeDate.map(Date::getYear).orElse(0);
			Integer hour =
				questionBuilder._maybeDate.map(Date::getHours).orElse(0);
			Integer minute =
				questionBuilder._maybeDate.map(Date::getMinutes).orElse(0);

			List<PollsChoice> collect =
				questionBuilder._choiceBuilders.stream().map(cb ->
					{
						PollsChoice pollsChoice =
							_pollsChoiceLocalService.createPollsChoice(0);
						pollsChoice.setName(cb._name);
						pollsChoice.setDescriptionMap(cb._descriptions);

						return pollsChoice;
					}
				).collect(Collectors.toList());

			try {
				PollsQuestion pollsQuestion =
					_pollsQuestionLocalService.addQuestion(
						userId,
						questionBuilder._titles, questionBuilder._descriptions,
						month, day, year, hour, minute,
						!questionBuilder._maybeDate.isPresent(),
						collect, serviceContext);

				pollsOperationInterpreter.interpret(
					userId, pollsQuestion, serviceContext,
					createContext.getPollsOperations());
			}
			catch (PortalException e) {
				e.printStackTrace();
			}
		}
		if (pc instanceof PollsContext.All) {
			PollsContext.All all = (PollsContext.All) pc;

			List<PollsQuestion> questions =
				_pollsQuestionLocalService.getQuestions(all._groupId);

			PollsOperations pollsOperations = all.getPollsOperations();

			questions.stream().forEach(
				pq -> pollsOperationInterpreter.interpret(
					userId, pq, serviceContext, pollsOperations));
		}
	}

	protected Interpreter getPollsOperationInterpreter() {

		return new Interpreter(
			_pollsQuestionLocalService, _pollsChoiceLocalService,
			_pollsVoteLocalService);
	}

	private static class MyQuestionBuilder implements QuestionBuilder,
		QuestionBuilder.Step1, QuestionBuilder.Step2, QuestionBuilder.Step3 {
		private Optional<Date> _maybeDate;
		private List<MyChoiceBuilder> _choiceBuilders = new ArrayList<>();
		private Map<Locale, String> _titles;
		private Map<Locale, String> _descriptions;

		public Step1 title(Map<Locale, String> titles) {
			_titles = titles;

			return this;
		}

		public Step2 description(Map<Locale, String> descriptions) {
			_descriptions = descriptions;

			return this;
		}

		public Step3 expires(Date date) {
			_maybeDate = Optional.ofNullable(date);

			return this;
		}

		public Step3 neverExpires() {
			_maybeDate = Optional.empty();

			return this;
		}

		public Step4 addChoice(
			Function<PollsChoiceBuilder, PollsChoiceBuilder.Final>
				choiceBuilder) {

			MyChoiceBuilder choiceBuilder1 = new MyChoiceBuilder();

			choiceBuilder.apply(choiceBuilder1);

			return new Step4();
		}

		class Step4 implements QuestionBuilder.Step4 {

			public Final addChoice(
				Function<PollsChoiceBuilder, PollsChoiceBuilder.Final> choiceBuilder) {

				MyChoiceBuilder choiceBuilder1 = new MyChoiceBuilder();

				choiceBuilder.apply(choiceBuilder1);

				return new Final();
			}
		}

		class Final implements QuestionBuilder.Final {

			public QuestionBuilder.Final addChoice(
				Function<PollsChoiceBuilder, PollsChoiceBuilder.Final>
					choiceBuilder) {

				MyChoiceBuilder choiceBuilder1 = new MyChoiceBuilder();

				choiceBuilder.apply(choiceBuilder1);

				return this;
			}
		}

		class MyChoiceBuilder implements PollsChoiceBuilder,
			PollsChoiceBuilder.Step1, PollsChoiceBuilder.Final {

			private String _name;
			private Map<Locale, String> _descriptions;

			public Step1 name(String name) {
				_name = name;

				return this;
			}

			public Final description(Map<Locale, String> descriptions) {
				_descriptions = descriptions;

				return this;
			}

		}
	}

	private static class MyFinderBuilder implements
		PollsContext.FinderBuilder, PollsContext.FinderBuilder.Final {

		private String _title;
		private String _description;

		public <F extends PollsContext.FinderBuilder & Final> F byTitle(
			String title) {
			_title = title;

			return (F)this;
		}

		public <F extends PollsContext.FinderBuilder & Final> F byDescription(
			String description) {
			_description = description;

			return (F)this;
		}
	}
}
