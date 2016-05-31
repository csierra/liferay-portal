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

import com.liferay.portal.polls.dsl.PollsQuerierDSL.PollsQuestionWithChoices;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.liferay.portal.polls.dsl.PollsOperations.addChoice;
import static com.liferay.portal.polls.dsl.PollsOperations.voteFor;
import static com.liferay.portal.polls.dsl.PollsQuerierDSL.POLLS_QUESTION_WITH_CHOICES_AND_VOTES;

/**
 * @author Carlos Sierra Andrés
 */
public interface PollsContext {

	static PollsContext create(
		Function<PollsBuilder, PollsBuilder.Final>
			pollsQuestionBuilder) {

		return (PollsContext) new Object();
	}

	static PollsContext findById(long id) {

		return (PollsContext) new Object();
	}

	static MultipleContext findByTitle(String title) {
		return (MultipleContext) new Object();
	}

	static <T> T execute(
		PollsContext pollsContext,
		PollsQuerierDSL<T> dsl) {

		return (T)new Object();
	}

	static <T> List<T> execute(
		MultipleContext multipleContext,
		PollsQuerierDSL<T> dsl) {

		return Collections.emptyList();
	}

	interface MultipleContext {
		<T> MultipleContext apply(PollsOperations<T> pollsOperations);
	}

	<T> PollsContext apply(PollsOperations<T> pollsOperations);

	public static void main(String[] args) {
		PollsContext pollsContext = create(p -> p.
			title("a Polls Title").
			description("a Polls Description").
			neverExpires().
			addChoice(c ->
				c.name("a").description("Choice a")
			).
			addChoice(c ->
				c.name("b").description("Choice b")
			)
		);

		addChoice(c -> c.
			name("c").
			description("Choice c")).
		then(
			voteFor(
				ChoiceContext.findByName("c")));

		List<PollsQuestionWithChoices> pollsQuestions =
			execute(findByTitle("name"),
				POLLS_QUESTION_WITH_CHOICES_AND_VOTES);

		Stream<Stream<Long>> s = Stream.empty();

		Stream<Long> longStream = s.flatMap(Function.identity());

	}
}
