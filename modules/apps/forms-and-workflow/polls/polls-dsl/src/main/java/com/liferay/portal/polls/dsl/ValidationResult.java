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

import com.liferay.portal.polls.dsl.OptionalInstance.MyClass;
import javaslang.Function1;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Carlos Sierra Andrés
 */
public interface ValidationResult<T>
	extends Applicative<ValidationResult<?>, T> {

	public class Success<T> implements ValidationResult<T> {

		private T _t;

		public Success(T t) {
			_t = t;
		}

		@Override
		public <S> Applicative<ValidationResult<?>, S> fmap(
			Function1<T, S> fun) {

			S apply = fun.apply(_t);

			return new Success<>(apply);
		}

		@Override
		public <S> Applicative<ValidationResult<?>, S> flatMap(
			Function1<T, Applicative<ValidationResult<?>, S>> fun) {

			return fun.apply(_t);
		}

		@Override
		public String toString() {
			return "Success {" + _t + '}';
		}
	}

	public class Failure<T> implements ValidationResult<T> {

		private List<String> _reasons;

		public Failure(List<String> reasons) {
			_reasons = reasons;
		}

		@Override
		public <S> Applicative<ValidationResult<?>, S> fmap(
			Function1<T, S> fun) {

			return (Failure<S>)this;
		}

		@Override
		public <S> Applicative<ValidationResult<?>, S> flatMap(
			Function1<T, Applicative<ValidationResult<?>, S>> fun) {

			Function1 fun1 = (Function1) fun;

			Object apply = fun1.apply((Function1) o -> o);

			ArrayList<String> reasons = new ArrayList<>();

			reasons.addAll(_reasons);

			if (apply instanceof Failure) {
				Failure<?> failure = (Failure) apply;

				reasons.addAll(failure._reasons);
			}

			return new Failure<>(reasons);
		}

		@Override
		public String toString() {
			return "Failure {" + "Reasons: " + _reasons + '}';
		}
	}

	public static <T> Applicative<ValidationResult<?>, T> pure(T t) {
		return new Success<>(t);
	}

	public static void main(String[] args) {
		ApplicativeInstance<ValidationResult<?>> applicativeInstance =
			new ApplicativeInstance<ValidationResult<?>>() {};

		Applicative<ValidationResult<?>, MyClass> carlos =
			applicativeInstance.lift(MyClass::new).apply(
				pure(38),
				pure("Carlos"));

		System.out.println(carlos);
	}


}
