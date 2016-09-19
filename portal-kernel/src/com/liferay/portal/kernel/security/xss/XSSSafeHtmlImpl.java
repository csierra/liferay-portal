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

import java.util.function.Function;

/**
 * @author Tomas Polesovsky
 */
public class XSSSafeHtmlImpl extends XSSEscapedStringImpl
	implements XSS.SafeHtml {

	private XSS.EscapedString _escapedString;

	public XSSSafeHtmlImpl(Object safeContent) {
		super(safeContent);

		if (safeContent instanceof XSS.EscapedString) {
			_escapedString = (XSS.EscapedString) safeContent;
		}
	}

	public String getRawString() {
		if (_escapedString != null) {
			return _escapedString.getRawString();
		}
		return super.getRawString();
	}

	public XSS.SafeHtml escape(Function<String, XSS.SafeHtml> f) {
		if (_escapedString != null) {
			return _escapedString.escape(f);
		}

		return XSS.safeHtml(super.getRawString());
	}

	public int length() {
		if (_escapedString != null) {
			return _escapedString.length();
		}
		return super.length();
	}

	public char charAt(int index) {
		if (_escapedString != null) {
			return _escapedString.charAt(index);
		}
		return super.charAt(index);
	}

	public CharSequence subSequence(int start, int end) {
		if (_escapedString != null) {
			return _escapedString.subSequence(start, end);
		}
		return super.subSequence(start, end);
	}
}
