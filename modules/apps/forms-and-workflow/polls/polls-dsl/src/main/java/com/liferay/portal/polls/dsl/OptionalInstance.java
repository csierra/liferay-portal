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

import java.util.Optional;

/**
 * @author Carlos Sierra Andrés
 */
public interface OptionalInstance extends ApplicativeInstance<Optional<?>> {

	public interface OptionalApplicative<T> extends Applicative<Optional<?>, T> {
		@Override
		<S> OptionalApplicative<S> flatMap(
			Function1<T, Applicative<Optional<?>, S>> fun);

		@Override
		<S> OptionalApplicative<S> fmap(Function1<T, S> fun);

		boolean isPresent();
	}

	public static class Nothing<T> implements OptionalApplicative<T> {

		@Override
		public <S> OptionalApplicative<S> fmap(Function1<T, S> fun) {
			return new Nothing<>();
		}

		@Override
		public boolean isPresent() {
			return false;
		}

		@Override
		public <S> OptionalApplicative<S> flatMap(
			Function1<T, Applicative<Optional<?>, S>> fun) {

			return new Nothing<>();
		}

		@Override
		public String toString() {
			return "Nothing";
		}
	}

	public static class Some<T> implements OptionalApplicative<T> {

		private final Optional<T> _t;

		public Some(Optional<T> t) {
			_t = t;
		}

		@Override
		public <S> OptionalApplicative<S> fmap(Function1<T, S> fun) {
			return new Some<>(_t.map(fun));
		}

		@Override
		public boolean isPresent() {
			return true;
		}

		@Override
		public <S> OptionalApplicative<S> flatMap(
			Function1<T, Applicative<Optional<?>, S>> fun) {

			Optional<Applicative<Optional<?>, S>> applicativeOptional =
				_t.map(fun);

			return (OptionalApplicative<S>)applicativeOptional.get();
		}

		@Override
		public String toString() {
			return "Some " + _t.get();
		}
	}

	public static <T> OptionalApplicative<T> pureOptional(T t) {
		return new Some<>(Optional.of(t));
	}

	public static void main(String[] args) {
		OptionalInstance optionalInstance = new OptionalInstance(){};

		OptionalApplicative<MyClass> carlos =
			(OptionalApplicative<MyClass>)
				optionalInstance.lift(MyClass::new).apply(
					pureOptional(38),
					pureOptional("Carlos"));

		if (carlos.isPresent()) {
			System.out.println(carlos);
		}
		else {
			System.out.println("Nothing to be shown");
		}
	}

	public static class MyClass {

		String name;
		int age;

		public MyClass(int age, String name) {
			this.age = age;
			this.name = name;
		}

		@Override
		public String toString() {
			return "MyClass{" +
				   "age=" + age +
				   ", name='" + name + '\'' +
				   '}';
		}
	}

	public static Function2<String, String, String> suma = (a, b) -> a + b;

}
