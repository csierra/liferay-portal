/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.kernel.security.xss;

import com.liferay.portal.kernel.util.StringPool;

import java.util.function.Function;

/**
 * @author Tomas Polesovsky
 */
public class XSSEscapedStringImpl implements XSS.EscapedString {
	private String _rawString;

	public XSSEscapedStringImpl(Object input) {
		if (input == null) {
			_rawString = StringPool.BLANK;
		}
		else if (input instanceof XSS.EscapedString) {
			_rawString = ((XSS.EscapedString) input).getRawString();
		}
		else {
			_rawString = input.toString();
		}
	}

	@Override
	public String getRawString() {
		return _rawString;
	}

	@Override
	public XSS.SafeHtml escape(Function<String, XSS.SafeHtml> f) {
		return f.apply(getRawString());
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

}
