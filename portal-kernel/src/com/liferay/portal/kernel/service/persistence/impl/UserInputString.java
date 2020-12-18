/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

package com.liferay.portal.kernel.service.persistence.impl;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * @author Carlos Sierra Andrés
 */
public final class UserInputString
	implements CharSequence, Comparable<UserInputString> {

	private final String _string;

	String getString() {
		return _string;
	}

	public UserInputString(String string) {
		_string = string;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (object == null || getClass() != object.getClass()) {
			return false;
		}

		UserInputString userInputString = (UserInputString)object;

		return Objects.equals(_string, userInputString._string);
	}

	@Override
	public int hashCode() {
		return Objects.hash(_string);
	}

	@Override
	public String toString() {
		Escaper escaper = EscaperContext.getEscaper();

		if (escaper == null) {
			throw new IllegalStateException("No escape context available");
		}

		return escaper.escape(_string);
	}

	@Override
	public int length() {
		return toString().length();
	}

	@Override
	public char charAt(int index) {
		return toString().charAt(index);
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return toString().subSequence(start, end);
	}

	@Override
	public IntStream chars() {
		return toString().chars();
	}

	@Override
	public IntStream codePoints() {
		return toString().codePoints();
	}

	public UserInputString map(Function<String, String> function) {
		return new UserInputString(function.apply(_string));
	}

	public boolean test(Predicate<String> predicate) {
		return predicate.test(_string);
	}

	public boolean equalsString(String string) {
		return this.test(string::equals);
	}

	@Override
	public int compareTo(UserInputString userInputString) {
		return _string.compareTo(userInputString._string);
	}

}
