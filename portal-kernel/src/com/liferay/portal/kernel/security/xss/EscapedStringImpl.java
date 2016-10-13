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

package com.liferay.portal.kernel.security.xss;

/**
 * @author Carlos Sierra Andrés
 */
public final class EscapedStringImpl
	implements CharSequence, XSS.SafeHtml, XSS.VerifiedJavaScript,
		XSS.TranslationContent {

	EscapedStringImpl(String original) {
		_original = original;
	}

	private final String _original;
	private java.util.function.Function<String, String> _transformation =
		x -> x;
	private String _escaped;
	private EscapeOperation _escapeOperation;

	public EscapedStringImpl map(
		java.util.function.Function<String, String> transformation) {

		_transformation = _transformation.andThen(transformation);

		recalculate();

		return this;
	}

	private void recalculate() {
		_escaped = _transformation.apply(_original);

		_escaped = _escapeOperation.escape(_escaped);
	}

	public EscapedStringImpl apply(EscapeOperation escapeOperation) {
		_escapeOperation = escapeOperation;

		recalculate();

		return this;
	}

	@Override
	public int length() {
		return _escaped.length();
	}

	@Override
	public char charAt(int index) {
		return _escaped.charAt(index);
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return _transformation.apply(_original).substring(start, end);
	}

	@Override
	public String toString() {
		return _escaped.toString();
	}
}
