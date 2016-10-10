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

/**
 * @author Carlos Sierra Andrés
 */
public interface ApplicativeInstance<AP> {

	public default <A, T> Lifted1<AP, A, T> lift(Function1<A, T> fun) {
		return a -> a.fmap(fun);
	}

	public default <A, B, T> Lifted2<AP, A, B, T> lift(Function2<A, B, T> fun) {
		return (a , b) -> a.fmap(fun.curried()).flatMap(f -> b.fmap(f));
	}

	public default <A, B, C, T> Lifted3<AP, A, B, C, T> lift(Function3<A, B, C, T> fun) {
		return (a , b, c) -> a.fmap(fun.curried()).flatMap(f -> b.fmap(f)).flatMap(f2 -> c.fmap(f2));
	}

	public default <A, B, C, D, T> Lifted4<AP, A, B, C, D, T> lift(
		Function4<A, B, C, D, T> fun) {
		return (a , b, c, d) -> a.fmap(fun.curried()).flatMap(f -> b.fmap(f)).flatMap(f2 -> c.fmap(f2)).flatMap(f3 -> d.fmap(f3));
	}

	public default <A, B, C, D, E, T> Lifted5<AP, A, B, C, D, E, T> lift(
		Function5<A, B, C, D, E, T> fun) {
		return (a , b, c, d, e) -> a.fmap(fun.curried()).flatMap(f -> b.fmap(f)).flatMap(f2 -> c.fmap(f2)).flatMap(f3 -> d.fmap(f3)).flatMap(f4 -> e.fmap(f4));
	}

	public default <A, B, C, D, E, F, T> Lifted6<AP, A, B, C, D, E, F, T> lift(
		Function6<A, B, C, D, E, F, T> fun) {
		return (a , b, c, d, e, f) -> a.fmap(fun.curried()).flatMap(f1 -> b.fmap(f1)).flatMap(f2 -> c.fmap(f2)).flatMap(f3 -> d.fmap(f3)).flatMap(f4 -> e.fmap(f4)).flatMap(f5 -> f.fmap(f5));
	}

	public default <A, B, C, D, E, F, G, T> Lifted7<AP, A, B, C, D, E, F, G, T> lift(
		Function7<A, B, C, D, E, F, G, T> fun) {
		return (a , b, c, d, e, f, g) -> a.fmap(fun.curried()).flatMap(f1 -> b.fmap(f1)).flatMap(f2 -> c.fmap(f2)).flatMap(f3 -> d.fmap(f3)).flatMap(f4 -> e.fmap(f4)).flatMap(f5 -> f.fmap(f5)).flatMap(f6 -> g.fmap(f6));
	}

	public default <A, B, C, D, E, F, G, H, T> Lifted8<AP, A, B, C, D, E, F, G, H, T> lift(
		Function8<A, B, C, D, E, F, G, H, T> fun) {
		return (a , b, c, d, e, f, g, h) -> a.fmap(fun.curried()).flatMap(f1 -> b.fmap(f1)).flatMap(f2 -> c.fmap(f2)).flatMap(f3 -> d.fmap(f3)).flatMap(f4 -> e.fmap(f4)).flatMap(f5 -> f.fmap(f5)).flatMap(f6 -> g.fmap(f6)).flatMap(f7 -> h.fmap(f7));
	}

	public interface Lifted1<AP, A, T> {
		Applicative<AP, T> apply(Applicative<AP, A> a);
	}

	public interface Lifted2<AP, A, B, T>{
		Applicative<AP, T> apply(Applicative<AP, A> a, Applicative<AP, B> b);
	}

	public interface Lifted3<AP, A, B, C, T> {
		Applicative<AP, T> apply(Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c);
	}

	public interface Lifted4<AP, A, B, C, D, T> {
		Applicative<AP, T> apply(
			Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c,
			Applicative<AP, D> d);
	}

	public interface Lifted5<AP, A, B, C, D, E, T> {
		Applicative<AP, T> apply(
			Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c,
			Applicative<AP, D> d, Applicative<AP, E> e);
	}

	public interface Lifted6<AP, A, B, C, D, E, F, T> {
		Applicative<AP, T> apply(
			Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c,
			Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f);
	}

	public interface Lifted7<AP, A, B, C, D, E, F, G, T> {
		Applicative<AP, T> apply(
			Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c,
			Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f,
			Applicative<AP, G> g);
	}

	public interface Lifted8<AP, A, B, C, D, E, F, G, H, T> {
		Applicative<AP, T> apply(
			Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c,
			Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f,
			Applicative<AP, G> g, Applicative<AP, H> h);
	}

}
