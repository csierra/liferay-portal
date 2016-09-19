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

import com.liferay.portal.kernel.util.HtmlUtil;

import java.util.function.Function;

/**
 * @author Tomas Polesovsky
 */
public class XSS {

	public static SafeHtml safeHtmlOnly(SafeHtml SafeHtml) {
		return SafeHtml;
	}

	public static VerifiedJavaScript verifiedJavaScriptOnly(
			VerifiedJavaScript verifiedJavaScript) {

		return verifiedJavaScript;
	}
	public static SafeHtml safeHtml(Object input) {
		return new XSSSafeHtmlImpl(input);
	}

	public static CharSequence htmlBody(Object input) {
		EscapedString result = getEscapedString(input);

		return result.escape(p -> XSS.safeHtml(HtmlUtil.escape(p)));
	}

	public static CharSequence attribute(Object input) {
		EscapedString result = getEscapedString(input);

		return result.escape(p -> XSS.safeHtml(HtmlUtil.escapeAttribute(p)));}


	public static CharSequence css(Object input) {
		EscapedString result = getEscapedString(input);

		return result.escape(p -> XSS.safeHtml(HtmlUtil.escapeCSS(p)));
	}

	public static CharSequence href(Object input) {
		EscapedString result = getEscapedString(input);

		return result.escape(p -> XSS.safeHtml(HtmlUtil.escapeHREF(p)));
	}

	public static CharSequence src(Object input) {
		EscapedString result = getEscapedString(input);

		return result.escape(p -> XSS.safeHtml(HtmlUtil.escapeHREF(p)));
	}

	public static CharSequence js(Object input) {
		EscapedString result = getEscapedString(input);

		return result.escape(p -> XSS.safeHtml(HtmlUtil.escapeJS(p)));
	}

	protected static EscapedString getEscapedString(Object input) {
		if (input instanceof EscapedString) {
			return (EscapedString) input;
		}
		else {
			return new XSSEscapedStringImpl(input);
		}
	}

	public interface EscapedString extends CharSequence {
		public String getRawString();

		public SafeHtml escape(Function<String, SafeHtml> f);
	}

	public interface SafeHtml extends EscapedString {}
	public interface VerifiedJavaScript extends EscapedString {}

	public interface TranslationContent extends EscapedString {}
}
