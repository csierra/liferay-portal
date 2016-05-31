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


import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Carlos Sierra Andrés
 */
public interface PollsQuerierDSL<T> {
	static PollsQuerierDSL<String> title = (PollsQuerierDSL<String>) new Object();

	static PollsQuerierDSL<String> description = (PollsQuerierDSL<String>) new Object();

	static PollsQuerierDSL<Optional<Date>> expiration = (PollsQuerierDSL<Optional<Date>>) new Object();

	PollsChoiceDSL<PollChoiceWithVotes> CHOICE_WITH_VOTES =
		PollsChoiceDSL.name.bind(name ->
		PollsChoiceDSL.description.bind(choiceDescription ->
		PollsChoiceDSL.votes(PollsVoteDSL.userId).bind(votes ->
		PollsChoiceDSL.pure(
			new PollChoiceWithVotes(
				name, choiceDescription, votes.collect(Collectors.toList()))))));

	static PollsQuerierDSL<PollsQuestionWithChoices>
		POLLS_QUESTION_WITH_CHOICES_AND_VOTES =
			title.bind( title ->
			description.bind( description ->
			expiration.bind( maybeExpiration ->
			choices(CHOICE_WITH_VOTES).bind(choices ->
			pure(new PollsQuestionWithChoices(title, description,
				maybeExpiration, choices.collect(Collectors.toList()))
			)))));


	static <T> PollsQuerierDSL<Stream<T>> choices(PollsChoiceDSL<T> dsl) {
		return (PollsQuerierDSL<Stream<T>>) new Object();
	}

	static <T> PollsQuerierDSL<T> pure(T t) {
		return (PollsQuerierDSL<T>) new Object();
	}

	<S> PollsQuerierDSL<S> bind(Function<T, PollsQuerierDSL<S>> function);

	interface PollsChoiceDSL<T> {
		static <T> PollsChoiceDSL<T> pure(T t) {
			return (PollsChoiceDSL<T>) new Object();
		}

		static PollsChoiceDSL<String> name = (PollsChoiceDSL<String>) new Object();

		static PollsChoiceDSL<String> description = (PollsChoiceDSL<String>) new Object();

		static PollsChoiceDSL<Long> numberOfVotes = (PollsChoiceDSL<Long>) new Object();

		static <T> PollsChoiceDSL<Stream<T>> votes(PollsVoteDSL<T> dsl) {
			return (PollsChoiceDSL<Stream<T>>) new Object();
		}

		<S> PollsChoiceDSL<S> bind(Function<T, PollsChoiceDSL<S>> function);
	}

	interface PollsVoteDSL<T> {
		static PollsVoteDSL<Long> userId = (PollsVoteDSL<Long>) new Object();
	}

	public static class PollsQuestionWithChoices {
		public final String title;
		public final String description;
		public final Optional<Date> expiration;
		public final List<PollChoiceWithVotes> choices;

		public PollsQuestionWithChoices(
			String title, String description, Optional<Date> expiration,
			List<PollChoiceWithVotes> choices) {

			this.choices = choices;
			this.title = title;
			this.description = description;
			this.expiration = expiration;
		}
	}

	public static class PollChoiceWithVotes {
		public final String name;
		public final String description;
		public List<Long> votes;

		public PollChoiceWithVotes(String description, String name, List<Long> votes) {
			this.description = description;
			this.name = name;
			this.votes = votes;
		}
	}

}
