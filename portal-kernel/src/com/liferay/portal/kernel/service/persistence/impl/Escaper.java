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

import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import java.util.function.Function;

/**
 * @author Carlos Sierra Andrés
 */
public enum Escaper {

	ATTRIBUTE(HtmlUtil::escapeAttribute),
	CSS(HtmlUtil::escapeCSS),
	HREF(HtmlUtil::escapeHREF),
	HTML(HtmlUtil::escape),
	JS(HtmlUtil::escapeJS),
	JS_LINK(HtmlUtil::escapeJSLink),
	REDIRECT(PortalUtil::escapeRedirect),
	URL(HtmlUtil::escapeURL),
	XPATH(HtmlUtil::escapeXPath),
	XPATH_ATTRIBUTE(HtmlUtil::escapeXPathAttribute);

	private final Function<String, String> _escaper;

	private Escaper(Function<String, String> escaper) {
		_escaper = escaper;
	}

	public String escape(String string) {
		return _escaper.apply(string);
	}

	public String escape(UserInputString userInputString) {
		return _escaper.apply(userInputString.getString());
	}

}
