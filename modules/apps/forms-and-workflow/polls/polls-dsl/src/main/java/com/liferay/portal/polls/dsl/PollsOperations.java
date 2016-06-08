/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ServiceContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

/**
 * @author Carlos Sierra Andrés
 */
public interface PollsOperations extends Monad<PollsOperations, Void> {

	static class Impure extends Monad.Impure<PollsOperations, Void>
		implements PollsOperations {}

	static class Pure extends Monad.Pure<PollsOperations, Void> implements
		PollsOperations {

		public Pure(Void t) {
			super(t);
		}
	}

	public static PollsOperations addChoice(
		Function<PollsChoiceBuilder, PollsChoiceBuilder.Final>
			builder) {

		return new AddChoice(builder);
	}

	public static PollsOperations deleteChoice(String name) {
		return new DeleteChoice(name);
	}

	public static PollsOperations voteFor(String s) {
		return null;
	}

	static class AddChoice extends Impure {
		private Function<PollsChoiceBuilder, PollsChoiceBuilder.Final> _builder;

		public AddChoice(
			Function<PollsChoiceBuilder, PollsChoiceBuilder.Final> builder) {

			_builder = builder;
		}

		public Function<PollsChoiceBuilder, PollsChoiceBuilder.Final>
			getBuilder() {

			return _builder;
		}

	}

	static class DeleteChoice extends Impure {
		private String _name;

		public DeleteChoice(String name) {
			_name = name;
		}

		public String getName() {
			return _name;
		}

	}

	class VoteFor extends Impure {

		private String _name;

		public VoteFor(String name) {
			_name = name;
		}

		public String getName() {
			return _name;
		}

	}

	public static class Interpreter {

		private final PollsQuestionLocalService _pollsQuestionLocalService;
		private final PollsChoiceLocalService _pollsChoiceLocalService;
		private final PollsVoteLocalService _pollsVoteLocalService;
		private List<Exception> _exceptions = new ArrayList<>();

		public Interpreter(
			final PollsQuestionLocalService pollsQuestionLocalService,
			final PollsChoiceLocalService pollsChoiceLocalService,
			final PollsVoteLocalService pollsVoteLocalService) {

			_pollsQuestionLocalService = pollsQuestionLocalService;
			_pollsChoiceLocalService = pollsChoiceLocalService;
			_pollsVoteLocalService = pollsVoteLocalService;
		}

		public void interpret(
			long userId, PollsQuestion pollsQuestion, ServiceContext serviceContext,
			PollsOperations pollsOperation) {

			if (pollsOperation == null) {
				System.out.println("END!");

				return;
			}

			if (pollsOperation instanceof AddChoice) {
				AddChoice addChoice = (AddChoice) pollsOperation;

				Function<PollsChoiceBuilder, PollsChoiceBuilder.Final> builder =
					addChoice.getBuilder();

				builder.apply(
					name -> description -> {
						try {
							List<PollsChoice> choices =
								new ArrayList<PollsChoice>(_pollsChoiceLocalService.getChoices(
									pollsQuestion.getQuestionId()));

							PollsChoice pollsChoice =
								_pollsChoiceLocalService.createPollsChoice(0);

							pollsChoice.setName(name);
							pollsChoice.setDescriptionMap(description);

							choices.add(pollsChoice);

							Date expirationDate =
								pollsQuestion.getExpirationDate();

							_pollsQuestionLocalService.updateQuestion(
								userId, pollsChoice.getQuestionId(),
								pollsQuestion.getTitleMap(),
								pollsQuestion.getDescriptionMap(),
								expirationDate.getMonth(),
								expirationDate.getDay(),
								expirationDate.getYear(),
								expirationDate.getHours(),
								expirationDate.getMinutes(),
								expirationDate.getYear() == 0, choices,
								serviceContext);
						}
						catch (PortalException e) {
							_exceptions.add(e);
						}

						return new PollsChoiceBuilder.Final() {};
				});
			}

			if (pollsOperation instanceof DeleteChoice) {
				DeleteChoice deleteChoice = (DeleteChoice) pollsOperation;

				List<PollsChoice> choices =
					_pollsChoiceLocalService.getChoices(
						pollsQuestion.getQuestionId());

				choices.stream().filter(
					c -> c.getName().equals(deleteChoice.getName())).findFirst().
					ifPresent(
						c -> {
							try {
								_pollsChoiceLocalService.deletePollsChoice(
									c.getChoiceId());
							}
							catch (PortalException e) {
								_exceptions.add(e);
							}
						});
			}

			if (pollsOperation instanceof VoteFor) {
				VoteFor voteFor = (VoteFor) pollsOperation;

				List<PollsChoice> choices =
					_pollsChoiceLocalService.getChoices(
						pollsQuestion.getQuestionId());

				choices.stream().filter(
					c -> c.getName().equals(voteFor.getName())).findFirst().
					ifPresent(
						c -> {
							try {
								_pollsVoteLocalService.addVote(
									userId, pollsQuestion.getQuestionId(),
									c.getChoiceId(), serviceContext);
							}
							catch (PortalException e) {
								_exceptions.add(e);
							}
						});
			}

			Impure impure = (Impure) pollsOperation;

			interpret(
				userId, pollsQuestion, serviceContext,
				(PollsOperations) impure.getFunction().apply(null));
		}
	}

}
