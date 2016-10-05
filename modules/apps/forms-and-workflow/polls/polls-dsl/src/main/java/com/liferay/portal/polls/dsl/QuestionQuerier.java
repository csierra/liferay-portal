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


import javaslang.Function2;
import javaslang.Function3;
import javaslang.Function4;
import javaslang.Function5;
import javaslang.Function6;
import javaslang.Function7;
import javaslang.Function8;

import java.security.cert.PKIXRevocationChecker;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.liferay.portal.polls.dsl.ChoiceQuerier.NAME;
import static com.liferay.portal.polls.dsl.ChoiceQuerier.NUMBER_OF_VOTES;

/**
 * @author Carlos Sierra Andrés
 */
public interface QuestionQuerier<T> {

	class Title implements QuestionQuerier<Map<Locale, String>> {}
	class Description implements QuestionQuerier<String> {}
	class Expiration implements QuestionQuerier<Optional<Date>> {}
	class Choices<T> implements QuestionQuerier<Stream<T>> {

		public ChoiceQuerier<T> _dsl;

		public Choices(ChoiceQuerier<T> dsl) {
			_dsl = dsl;
		}

	}

	Title TITLE = new Title();
	Description DESCRIPTION = new Description();
	Expiration EXPIRATION = new Expiration();

	static <T> QuestionQuerier<List<T>> CHOICES(ChoiceQuerier<T> dsl) { return null;}

	public static <A, T> QuestionQuerier<T> lift(
		Function<A, T> fun, QuestionQuerier<A> a) {

		return null;
	}

	public static <A, B, T> QuestionQuerier<T> lift(
		Function2<A, B, T> fun, QuestionQuerier<A> a, QuestionQuerier<B> b) {

		return null;
	}

	public static <A, B, C, T> QuestionQuerier<T> lift(
		Function3<A, B, C, T> fun, QuestionQuerier<A> a, QuestionQuerier<B> b, QuestionQuerier<C> c) {

		return null;
	}

	public static <A, B, C, D ,T> QuestionQuerier<T> lift(
		Function4<A, B, C, D, T> fun, QuestionQuerier<A> a, QuestionQuerier<B> b,
		QuestionQuerier<C> c, QuestionQuerier<D> d) {

		return null;
	}

	public static <A, B, C, D , E, T> QuestionQuerier<T> lift(
		Function5<A, B, C, D, E, T> fun, QuestionQuerier<A> a,
		QuestionQuerier<B> b, QuestionQuerier<C> c, QuestionQuerier<D> d,
		QuestionQuerier<E> e) {

		return null;
	}

	public static <A, B, C, D , E, F, T> QuestionQuerier<T> lift(
		Function6<A, B, C, D, E, F, T> fun, QuestionQuerier<A> a,
		QuestionQuerier<B> b, QuestionQuerier<C> c, QuestionQuerier<D> d,
		QuestionQuerier<E> e, QuestionQuerier<F> f) {

		return null;
	}

	public static <A, B, C, D, E, F, G, T> QuestionQuerier<T> lift(
		Function7<A, B, C, D, E, F, G, T> fun, QuestionQuerier<A> a,
		QuestionQuerier<B> b, QuestionQuerier<C> c, QuestionQuerier<D> d,
		QuestionQuerier<E> e, QuestionQuerier<F> f, QuestionQuerier<G> g) {

		return null;
	}

	public static <A, B, C, D, E, F, G, H, T> QuestionQuerier<T> lift(
		Function8<A, B, C, D, E, F, G, H, T> fun, QuestionQuerier<A> a,
		QuestionQuerier<B> b, QuestionQuerier<C> c, QuestionQuerier<D> d,
		QuestionQuerier<E> e, QuestionQuerier<F> f, QuestionQuerier<G> g,
		QuestionQuerier<H> h) {

		return null;
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

	static class ReflexAppl<T> implements QuestionQuerier<T> {

		ReflexAppl(Object f, QuestionQuerier<?> ... params) {

		}
	}

	ChoiceQuerier<PollChoiceWithVotes> CHOICE_WITH_VOTES =
		ChoiceQuerier.lift(
			PollChoiceWithVotes::new, NAME, ChoiceQuerier.DESCRIPTION,
			NUMBER_OF_VOTES);

	QuestionQuerier<PollsQuestionWithChoices>
		POLLS_QUESTION_WITH_CHOICES_AND_VOTES =
			QuestionQuerier.lift(
				PollsQuestionWithChoices::new,
					TITLE, DESCRIPTION, EXPIRATION, CHOICES(CHOICE_WITH_VOTES));

	static void main(String[] args) {
		Optional<String> s =
			maybeName().flatMap(
				x -> maybeCount().map(
					y -> x + y));
	}

	static Optional<String> maybeName() {
		return Optional.of("Carlos");
	}

	static Optional<Integer> maybeCount() {
		return Optional.of(5);
	}
}
