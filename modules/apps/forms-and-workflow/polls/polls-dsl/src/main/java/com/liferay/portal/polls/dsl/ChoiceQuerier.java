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

import javaslang.Function1;
import javaslang.Function2;
import javaslang.Function3;
import javaslang.Function4;
import javaslang.Function5;
import javaslang.Function6;
import javaslang.Function7;
import javaslang.Function8;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Carlos Sierra Andrés
 */
public interface ChoiceQuerier<T> {

	public static <A, T> ChoiceQuerier<T> lift(
		Function1<A, T> fun, ChoiceQuerier<A> a) { return null; }
	public static <A, B, T> ChoiceQuerier<T> lift(
		Function2<A, B, T> fun, ChoiceQuerier<A> a, ChoiceQuerier<B> b) { return null; }
	public static <A, B, C, T> ChoiceQuerier<T> lift(
		Function3<A, B, C, T> fun, ChoiceQuerier<A> a, ChoiceQuerier<B> b, ChoiceQuerier<C> c) { return null; }
	public static <A, B, C, D, T> ChoiceQuerier<T> lift(
		Function4<A, B, C, D, T> fun, ChoiceQuerier<A> a, ChoiceQuerier<B> b, ChoiceQuerier<C> c, ChoiceQuerier<D> d) { return null; }
	public static <A, B, C, D, E, T> ChoiceQuerier<T> lift(
		Function5<A, B, C, D, E, T> fun, ChoiceQuerier<A> a, ChoiceQuerier<B> b, ChoiceQuerier<C> c, ChoiceQuerier<D> d, ChoiceQuerier<E> e) { return null; }
	public static <A, B, C, D, E, F, T> ChoiceQuerier<T> lift(
		Function6<A, B, C, D, E, F, T> fun, ChoiceQuerier<A> a, ChoiceQuerier<B> b, ChoiceQuerier<C> c, ChoiceQuerier<D> d, ChoiceQuerier<E> e, ChoiceQuerier<F> f) { return null; }
	public static <A, B, C, D, E, F, G, T> ChoiceQuerier<T> lift(
		Function7<A, B, C, D, E, F, G, T> fun, ChoiceQuerier<A> a, ChoiceQuerier<B> b, ChoiceQuerier<C> c, ChoiceQuerier<D> d, ChoiceQuerier<E> e, ChoiceQuerier<F> f, ChoiceQuerier<G> g) { return null; }
	public static <A, B, C, D, E, F, G, H, T> ChoiceQuerier<T> lift(
		Function8<A, B, C, D, E, F, G, H, T> fun, ChoiceQuerier<A> a, ChoiceQuerier<B> b, ChoiceQuerier<C> c, ChoiceQuerier<D> d, ChoiceQuerier<E> e, ChoiceQuerier<F> f, ChoiceQuerier<G> g, ChoiceQuerier<H> h) { return null; }

	class Name implements ChoiceQuerier<String> {}
	class Description implements ChoiceQuerier<String> {}
	class NumberOfVotes implements ChoiceQuerier<List<Long>> {}

	Name NAME = new Name();
	Description DESCRIPTION = new Description();
	NumberOfVotes NUMBER_OF_VOTES = new NumberOfVotes();

}
