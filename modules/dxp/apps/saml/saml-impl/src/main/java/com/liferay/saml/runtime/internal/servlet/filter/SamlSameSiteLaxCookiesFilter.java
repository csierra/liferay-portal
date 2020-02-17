/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.saml.runtime.internal.servlet.filter;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.ResourceBundleLoader;
import com.liferay.portal.kernel.util.ResourceBundleUtil;

import java.io.PrintWriter;

import java.util.ResourceBundle;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liferay.portal.kernel.util.Validator;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Stian Sigvartsen
 */
@Component(
	immediate = true,
	property = {
		"before-filter=Session Id Filter", "dispatcher=REQUEST",
		"init-param.url-regex-ignore-pattern=^/html/.+\\.(css|gif|html|ico|jpg|js|png)(\\?.*)?$",
		"servlet-context-name=",
		"servlet-filter-name=SAML SameSite Lax Support Filter",
		"url-pattern=/c/portal/saml/acs", "url-pattern=/c/portal/saml/slo",
		"url-pattern=/c/portal/saml/sso"
	},
	service = Filter.class
)
public class SamlSameSiteLaxCookiesFilter extends BaseSamlPortalFilter {

	@Override
	public boolean isFilterEnabled() {
		return true;
	}

	@Override
	public boolean isFilterEnabled(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) {

		if (ParamUtil.getBoolean(httpServletRequest, "continue") ||
			(!ParamUtil.getBoolean(httpServletRequest, "noscript") &&
			 (httpServletRequest.getSession(false) != null))) {

			return false;
		}

		return true;
	}

	@Override
	protected void doProcessFilter(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, FilterChain filterChain)
		throws Exception {

		httpServletResponse.setContentType("text/html");

		PrintWriter printWriter = httpServletResponse.getWriter();

		if (ParamUtil.getBoolean(httpServletRequest, "noscript")) {
			ResourceBundle resourceBundle =
				_resourceBundleLoader.loadResourceBundle(
					_portal.getLocale(httpServletRequest));

			printWriter.write(
				StringBundler.concat(
					"<!DOCTYPE html>\n<html><body>",
					ResourceBundleUtil.getString(
						resourceBundle,
						"your-browser-must-support-javascript-to-proceed"),
					"</body></html>"));

			printWriter.close();

			return;
		}

		StringBundler sb = new StringBundler(16);

		String relayState = ParamUtil.getString(
			httpServletRequest, "RelayState");
		String samlRequest = ParamUtil.getString(
			httpServletRequest, "SAMLRequest");
		String samlResponse = ParamUtil.getString(
			httpServletRequest, "SAMLResponse");

		sb.append("<!DOCTYPE html>\n");
		sb.append("<html><body onload=\"document.forms[0].submit()\">");
		sb.append(
			"<form action=\"?continue=true\" method=\"post\" name=\"fm\">");

		if (Validator.isNotNull(relayState)) {
			sb.append("<input type=\"hidden\" name=\"RelayState\" value=\"");
			sb.append(relayState);
			sb.append("\"/>");
		}

		if (Validator.isNotNull(samlRequest)) {
			sb.append("<input type=\"hidden\" name=\"SAMLRequest\" value=\"");
			sb.append(samlRequest);
			sb.append("\"/>");
		}

		if (Validator.isNotNull(samlResponse)) {
			sb.append("<input type=\"hidden\" name=\"SAMLResponse\" value=\"");
			sb.append(samlResponse);
			sb.append("\"/>");
		}

		sb.append("<noscript><iframe src=\"?noscript=true\" ");
		sb.append("style=\"width: 100%; border: 0;\">");
		sb.append("</iframe></noscript></form></body></html>");
		sb.append("</html>");

		printWriter.write(sb.toString());
		printWriter.close();
	}

	@Override
	protected Log getLog() {
		return _log;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SamlSameSiteLaxCookiesFilter.class);

	@Reference
	private Portal _portal;

	@Reference(
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(bundle.symbolic.name=com.liferay.saml.impl)"
	)
	private volatile ResourceBundleLoader _resourceBundleLoader;

}
