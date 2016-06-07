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


import com.liferay.polls.model.PollsQuestion;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.liferay.portal.polls.dsl.ChoiceQuerier.name;

/**
 * @author Carlos Sierra Andrés
 */
public interface QuestionQuerier<T> extends Monad<QuestionQuerier<T>, T>{

	static class Impure<T> extends Monad.Impure<QuestionQuerier<T>, T>
		implements QuestionQuerier<T> {}

	static class Pure<T> extends Monad.Pure<QuestionQuerier<T>, T> implements
		QuestionQuerier<T> {

		public Pure(T t) {
			super(t);
		}
	}

	class Title extends Impure<Map<Locale, String>> {}
	class Description extends Impure<String> {}
	class Expiration extends Impure<Optional<Date>> {}
	class Choices<T> extends Impure<Stream<T>> {

		public ChoiceQuerier<T> _dsl;

		public Choices(ChoiceQuerier<T> dsl) {
			_dsl = dsl;
		}

	}

	static <T> QuestionQuerier<T> title(
		Function<Map<Locale, String>, QuestionQuerier<T>> f) {

		return new Title().bind(f);
	}

	static <T> QuestionQuerier<T> description(
		Function<String, QuestionQuerier<T>> f) {

		return new Description().bind(f);
	}

	static <T> QuestionQuerier<T> expiration(
		Function<Optional<Date>, QuestionQuerier<T>> f) {

		return new Expiration().bind(f);
	}

	static <T, S> QuestionQuerier<S> choices(
		ChoiceQuerier<T> dsl, Function<Stream<T>, QuestionQuerier<S>> f) {
		return new Choices<>(dsl).bind(f);
	}

	static <T> QuestionQuerier<T> just(T t) {
		return new Pure<T>(t);
	}

	class PollsQuestionWithChoices {
		public final Map<Locale, String> title;
		public final String description;
		public final Optional<Date> expiration;
		public final List<PollChoiceWithVotes> choices;

		public PollsQuestionWithChoices(
			Map<Locale, String> title, String description, Optional<Date> expiration,
			List<PollChoiceWithVotes> choices) {

			this.choices = choices;
			this.title = title;
			this.description = description;
			this.expiration = expiration;
		}

		public String toString() {
			return "PollsQuestionWithChoices{" +
				   "choices=" + choices +
				   ", title='" + title + '\'' +
				   ", description='" + description + '\'' +
				   ", expiration=" + expiration +
				   '}';
		}
	}

	class PollChoiceWithVotes {
		public final String name;
		public final String description;
		public final List<Long> votes;

		public PollChoiceWithVotes(
			String description, String name, List<Long> votes) {

			this.description = description;
			this.name = name;
			this.votes = votes;
		}
	}

	static final ChoiceQuerier<PollChoiceWithVotes> CHOICE_WITH_VOTES =
		name(name ->
		ChoiceQuerier.description(choiceDescription ->
		ChoiceQuerier.votes(VoteQuerier.JUST_USER_ID).bind(votes ->
		ChoiceQuerier.just(
			new PollChoiceWithVotes(
				name, choiceDescription, votes.collect(Collectors.toList()))))));

	static final QuestionQuerier<PollsQuestionWithChoices>
		POLLS_QUESTION_WITH_CHOICES_AND_VOTES =
			title( t ->
			description( d ->
			expiration( exp ->
			choices(CHOICE_WITH_VOTES, choices ->
				just(new PollsQuestionWithChoices(
					t, d, exp,
					Collections.unmodifiableList(
						choices.collect(Collectors.toList()))))))));


	class PollsQuerierInterpreter {

		public <T> T interpret(
			PollsQuestion pq, QuestionQuerier<T> questionQuerier) {

			if (questionQuerier instanceof Title) {
				Title title = (Title) questionQuerier;

				Function function = title.getFunction();

				return (T)interpret(
					pq, (QuestionQuerier<T>) function.apply(pq.getTitleMap()));
			}
			if (questionQuerier instanceof Description) {
				Description description = (Description) questionQuerier;

				Function function = description.getFunction();

				return (T)interpret(
					pq, (QuestionQuerier<T>) function.apply(pq.getDescription()));
			}
			if (questionQuerier instanceof Expiration) {
				Expiration expiration = (Expiration) questionQuerier;

				Function function = expiration.getFunction();

				return (T)interpret(
					pq, (QuestionQuerier<T>) function.apply(
						Optional.ofNullable(pq.getExpirationDate())));
			}
			if (questionQuerier instanceof Choices) {
				Choices choices = (Choices) questionQuerier;

				// interpret choices._dsl;

				Function function = choices.getFunction();

				return (T)interpret(
					pq, (QuestionQuerier<T>) function.apply(
						pq.getChoices().stream()));

			}
			if (questionQuerier instanceof Pure) {
				Pure pure = (Pure) questionQuerier;

				return (T)pure.unwrap();
			}

			return null;
		}

	}

	static void main(String[] args) {


	}
}
