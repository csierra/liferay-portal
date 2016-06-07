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

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Carlos Sierra Andrés
 */
public interface ChoiceQuerier<T> extends Monad<ChoiceQuerier<T>, T> {

	static class Impure<T> extends Monad.Impure<ChoiceQuerier<T>, T> implements
		ChoiceQuerier<T> {}

	static class Pure<T> extends Monad.Pure<ChoiceQuerier<T>, T> implements
		ChoiceQuerier<T> {

		public Pure(T t) {
			super(t);
		}
	}

	static final ChoiceQuerier<String> JUST_NAME = name(n -> just(n));

	static <T> ChoiceQuerier<T> just(T t) {
		return new Pure<T>(t);
	}

	class Name extends Impure<String> {}
	class Description extends Impure<String> {}
	class NumberOfVotes extends Impure<Long> {}

	static <T> ChoiceQuerier<T> name(
		Function<String, ChoiceQuerier<T>> f) {

		return new Name().bind(f);
	}

	static <T> ChoiceQuerier<T> description(
		Function<String, ChoiceQuerier<T>> f) {

		return new Description().bind(f);
	}

	static <T> ChoiceQuerier<T> expiration(
		Function<Long, ChoiceQuerier<T>> f) {

		return new NumberOfVotes().bind(f);
	}

	static <T> ChoiceQuerier<Stream<T>> votes(
		VoteQuerier<T> dsl) {

		return just(Stream.empty());
	}

}
