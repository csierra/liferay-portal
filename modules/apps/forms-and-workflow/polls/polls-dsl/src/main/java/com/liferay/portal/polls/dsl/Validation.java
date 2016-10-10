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

import com.liferay.portal.polls.dsl.ValidationResult.Failure;
import com.liferay.portal.polls.dsl.ValidationResult.Success;

import java.util.Collections;

import static com.liferay.portal.polls.dsl.ValidationResult.pure;

/**
 * @author Carlos Sierra Andrés
 */
public interface Validation<T> {
	public ValidationResult<T> validate(T input);

	public default Validation<T> and(Validation<T> other) {
		return (input) -> (ValidationResult<T>) validate(input).flatMap(o -> other.validate(input));
	}

	public static Validation<String> dni = input -> {
		if (input.length() == 9) {
			return new Success<>(input);
		}
		else {
			return new Failure<>(
				Collections.singletonList("DNI must have 9 characters"));
		}
	};

	public static Validation<String> startsWith(String prefix) {
		return (input) -> {
			if (input.startsWith(prefix)) {
				return new Success<>(prefix);
			}
			else return new Failure<>(
				Collections.singletonList(
					input + " does not start with " + prefix));
		};
	}

	public static void main(String[] args) {
		Validation<String> validation = dni.and(startsWith("502"));

		ValidationResult<String> safeDni = validation.validate("50111539");

		ApplicativeInstance<ValidationResult<?>> applicativeInstance =
			new ApplicativeInstance<ValidationResult<?>>() {};

		Applicative<ValidationResult<?>, OptionalInstance.MyClass> result =
			applicativeInstance.lift(
				OptionalInstance.MyClass::new).apply(pure(38), safeDni);

		System.out.println(result);

	}
}
