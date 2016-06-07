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

import java.util.function.Function;

/**
 * @author Carlos Sierra Andrés
 */
public interface PollsContext<C extends PollsContext<C>>
	extends Context<C, PollsOperations>, Trashable<C> {

	static CreateContext create(
		Function<QuestionBuilder, QuestionBuilder.Final>
			pollsQuestionBuilder) {

		return new CreateContext(pollsQuestionBuilder);
	}

	static IdContext findById(long id) {
		return new IdContext(id);
	}

	static MultiplePollContext findByKeywords(String keywords) {
		return new KeywordsFinder(keywords);
	}

	static MultiplePollContext find(
		Function<FinderBuilder, FinderBuilder.Final> finder) {
		return new FindByFinder(finder);
	}
	static MultiplePollContext all(long groupId) {
		return new All(groupId);
	}

	interface FinderBuilder {
		<F extends FinderBuilder & Final> F byTitle(String title);

		<F extends FinderBuilder & Final> F byDescription(
			String description);

		interface Final {}
	}

	static abstract class BasePollContext<C extends BasePollContext<C>>
		implements PollsContext<C> {

		public PollsOperations getPollsOperations() {
			return _pollsOperations;
		}

		public TrashOperations getTrashOperations() {
			return _trashOperations;
		}

		private PollsOperations _pollsOperations;
		private TrashOperations _trashOperations;

		public C apply(PollsOperations pollsOperations) {
			_pollsOperations = pollsOperations;

			return (C)this;
		}

		public C apply(TrashOperations trashOperations) {
			_trashOperations = trashOperations;

			return (C)this;
		}
	}

	static abstract class SinglePollContext
		extends BasePollContext<SinglePollContext> {

	}

	static abstract class MultiplePollContext
		extends BasePollContext<MultiplePollContext> {


	}

	static class CreateContext extends SinglePollContext {

		public final Function<QuestionBuilder, QuestionBuilder.Final>
			_pollsQuestionBuilder;

		public CreateContext(
			Function<QuestionBuilder, QuestionBuilder.Final> pollsQuestionBuilder) {

			_pollsQuestionBuilder = pollsQuestionBuilder;
		}

	}
	static class IdContext extends SinglePollContext {
		public final long _id;

		public IdContext(long id) {
			_id = id;
		}

	}

	static class KeywordsFinder extends MultiplePollContext {
		public final String _keywords;

		public KeywordsFinder(
			String keywords) {

			_keywords = keywords;
		}

	}

	static class FindByFinder extends MultiplePollContext {
		public final Function<FinderBuilder, FinderBuilder.Final>
			_finder;

		public FindByFinder(
			Function<FinderBuilder, FinderBuilder.Final> finder) {
			_finder = finder;
		}

	}

	static class All extends MultiplePollContext {
		public final long _groupId;

		public All(long groupId) {
			_groupId = groupId;
		}
	}
}
