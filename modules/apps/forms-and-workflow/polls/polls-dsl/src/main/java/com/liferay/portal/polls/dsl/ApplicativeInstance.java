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

	public default <A, T> Applicative<AP, T> lift(
		Function1<A, T> fun, Applicative<AP, A> ap) {

		return ap.pure(fun.curried()).apply(ap);
	}

	public default <A, B, T> Applicative<AP, T> lift(Function2<A, B, T> fun, Applicative<AP, A> a, Applicative<AP, B> b) {
		return a.pure(fun.curried()).apply(a).apply(b);
	}

	public default <A, B, C, T>  Applicative<AP, T> lift(Function3<A, B, C, T> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c) {
		return a.pure(fun.curried()).apply(a).apply(b).apply(c);
	}

//	public default <A, B, C, D, T> Lifted4<AP, A, B, C, D, T> lift(
//		Function4<A, B, C, D, T> fun) {
//
//		return (a) -> (Lifted3<AP, B, C, D, T>) a.fmap(fun::apply);
//	}
//
//	public default <A, B, C, D, E, T> Lifted5<AP, A, B, C, D, E, T> lift(
//		Function5<A, B, C, D, E, T> fun) {
//
//		return (a) -> (Lifted4<AP, B, C, D, E, T>) a.fmap(fun::apply);
//	}
//
//	public default <A, B, C, D, E, F, T> Lifted6<AP, A, B, C, D, E, F, T> lift(
//		Function6<A, B, C, D, E, F, T> fun) {
//
//		return (a) -> (Lifted5<AP, B, C, D, E, F, T>) a.fmap(fun::apply);	}
//
//	public default <A, B, C, D, E, F, G, T> Lifted7<AP, A, B, C, D, E, F, G, T> lift(
//		Function7<A, B, C, D, E, F, G, T> fun) {
//
//		return (a) -> (Lifted6<AP, B, C, D, E, F, G, T>) a.fmap(fun::apply);	}
//
//	public default <A, B, C, D, E, F, G, H, T> Lifted8<AP, A, B, C, D, E, F, G, H, T> lift(
//		Function8<A, B, C, D, E, F, G, H, T> fun) {
//
//		return (a) -> (Lifted7<AP, B, C, D, E, F, G, H, T>) a.fmap(fun::apply);
//	}

//	public interface Lifted1<AP, A, T> {
//		Applicative<AP, T> apply(Applicative<AP, A> a);
//	}
//
//	public interface Lifted2<AP, A, B, T> {
//		Lifted1<AP, B, T> apply(Applicative<AP, A> a);
//	}
//
//	public interface Lifted3<AP, A, B, C, T> {
//		Lifted2<AP, B, C, T> apply(Applicative<AP, A> a);
//	}
//
//	public interface Lifted4<AP, A, B, C, D, T> extends Applicative<AP, Function4<A, B, C, D, T>> {
//		Lifted3<AP, B, C, D, T> apply(Applicative<AP, A> a);
//	}
//
//	public interface Lifted5<AP, A, B, C, D, E, T> extends Applicative<AP, Function5<A, B, C, D, E, T>> {
//		Lifted4<AP, B, C, D, E, T> apply(Applicative<AP, A> a);
//	}
//
//	public interface Lifted6<AP, A, B, C, D, E, F, T> extends Applicative<AP, Function6<A, B, C, D, E, F, T>> {
//		Lifted5<AP, B, C, D, E, F, T> apply(Applicative<AP, A> a);
//	}
//
//	public interface Lifted7<AP, A, B, C, D, E, F, G, T> extends Applicative<AP, Function7<A, B, C, D, E, F, G, T>> {
//		Lifted6<AP, B, C, D, E, F, G, T> apply(Applicative<AP, A> a);
//	}
//
//	public interface Lifted8<AP, A, B, C, D, E, F, G, H, T> extends Applicative<AP, Function8<A, B, C, D, E, F, G, H, T>> {
//		Lifted7<AP, B, C, D, E, F, G, H, T> apply(Applicative<AP, A> a);
//	}

}
