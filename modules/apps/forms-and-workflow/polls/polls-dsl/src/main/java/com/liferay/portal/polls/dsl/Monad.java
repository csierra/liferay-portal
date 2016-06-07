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
public interface Monad<M extends Monad<M, T>, T> {
	<S, N extends Monad<N, S>> N bind(Function<T, N> function);

	default <S, N extends Monad<N, S>> N then(N next) {
		return bind( t -> next );
	}

	public static <S, N extends Monad<N, S>> N sequence(N ... operations) {

		for (int i = 0; i < operations.length - 1; i++) {
			N operation = operations[i];

			operation.then(operations[i + 1]);
		}

		return operations[0];
	}

	class Impure<M extends Monad<M, T>, T> implements Monad<M, T> {

		private Function _function;

		public Function getFunction() {
			return _function;
		}

		public <S, N extends Monad<N, S>> N bind(
			Function<T, N> function) {

			_function = function;

			return (N)this;
		}

	}

	class Pure<M extends Monad<M, T>, T> implements Monad<M, T> {

		public T _t;

		public Pure(T t) {
			_t = t;
		}

		public T unwrap() {
			return _t;
		}

		public <S, N extends Monad<N, S>> N bind(
			Function<T, N> function) {

			return function.apply(_t);
		}

	}

}
