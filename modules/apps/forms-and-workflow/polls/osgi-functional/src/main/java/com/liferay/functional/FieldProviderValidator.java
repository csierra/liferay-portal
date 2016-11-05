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

package com.liferay.functional;

import javaslang.Function1;

import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.liferay.functional.FieldValidator.*;
import static com.liferay.functional.FieldValidator.longerThan;

/**
 * @author Carlos Sierra Andrés
 */
public class FieldProviderValidator<T>
	implements Functor<T>, Applicative<FieldProviderValidator<?>, T>,
	Monad<FieldProviderValidator<?>, T> {

	private Function1<Map<String, String>, Validation<T>> _f;

	FieldProviderValidator(Function1<Map<String, String>, Validation<T>> f) {
		_f = f;
	}

	@Override
	public <S> FieldProviderValidator<S> map(Function1<T, S> g) {
		return new FieldProviderValidator<>(
			h -> (Validation<S>) _f.apply(h).map(g));
	}

	@Override
	public <S, U> FieldProviderValidator<U> apply(
		Applicative<FieldProviderValidator<?>, S> ap) {

		return new FieldProviderValidator<>(
			h -> (Validation<U>) _f.apply(h).<S, U>apply(((FieldProviderValidator<S>) ap)._f.apply(h)));
	}

	@Override
	public <S> FieldProviderValidator<S> pure(S s) {
		return new FieldProviderValidator<>(h -> new Validation.Success<>(s));
	}

	@Override
	public <S> FieldProviderValidator<S> bind(
		Function1<T, Monad<FieldProviderValidator<?>, S>> fun) {

		return new FieldProviderValidator<>(
			h -> (Validation<S>) _f.apply(h).bind(
				fun.andThen((Function<Monad<FieldProviderValidator<?>, S>, Validation<S>>) mv -> ((FieldProviderValidator<S>)mv)._f.apply(h))));
	}

	public static <T> FieldProviderValidator<T> fpv(
		String fieldName, FieldValidator<Optional<String>, T> fv) {

		return new FieldProviderValidator<>(
			h -> fv.validate(
				new Field<>(fieldName, Optional.ofNullable(h.get(fieldName)))));
	}

	public Validation<T> runValidation(Map<String, String> input) {
		return _f.apply(input);
	}

	static FieldValidator<Optional<String>, Integer> isThereANumber =
		FieldValidator.<String>notEmpty().compose(safeInt);

	public static FieldProviderValidator<Integer> dayUpTo(int upperLimit) {
		return fpv("day", isThereANumber.compose(between(0, upperLimit)));
	}

	static class User {
		String name;
		String surname;
		Date birth;

		@Override
		public String toString() {
			return "User{" +
				   "birth=" + birth +
				   ", name='" + name + '\'' +
				   ", surname='" + surname + '\'' +
				   '}';
		}

		public User(String name, String surname, Date birth) {
			this.name = name;
			this.surname = surname;
			this.birth = birth;
		}
	}

	public static void main(String[] args) {

		FieldValidator<Optional<String>, String> nonBlank =
			FieldValidator.<String>notEmpty().compose(longerThan(0));

		FieldProviderValidator<String> safeName = fpv("name", nonBlank);
		FieldProviderValidator<String> safeSurname = fpv("surname", nonBlank);

		FieldProviderValidator<Integer> safeMonth = fpv(
			"month", isThereANumber.compose(between(0, 13)));

		FieldProviderValidator<Integer> safeYear =
			fpv("year", isThereANumber.compose(between(1900, 10000)));

		FieldProviderValidator<Integer> safeDay =
			safeYear.bind(y ->
			safeMonth.bind(m -> {
				if (Arrays.asList(1, 3, 5, 7, 8, 10, 12).contains(m)) {
					return dayUpTo(32);
				}
				else if (Arrays.asList(4, 6, 9, 11).contains(m)) {
					return dayUpTo(31);
				}
				else {
					if (new GregorianCalendar().isLeapYear(y)) {
						return dayUpTo(30);
					}
					else {
						return dayUpTo(29);
					}
				}
		}));


		Applicative<FieldProviderValidator<?>, Date> safeDate = Applicative.lift(
			GregorianCalendar::new,
			safeYear,
			safeMonth.map(x -> x - 1),
			safeDay).
			map(GregorianCalendar::getTime);

		FieldProviderValidator<User> safeUser =
			(FieldProviderValidator<User>)
				Applicative.lift(User::new, safeName, safeSurname, safeDate);

		System.out.println(
			safeUser.runValidation(
				new HashMap<String, String>() {{
					put("name", "Carlos");
					put("surname", "");
					put("day", "11");
					put("month", "08");
					put("year", "1979");
				}}));
	}
}
