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

package com.liferay.multi.factor.authentication.checker.email.otp.web.internal.portlet;

import com.liferay.multi.factor.authentication.checker.email.otp.web.internal.constants.MFAPortletKeys;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.PortletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Tomas Polesovsky
 * @author Marta Medio
 */
public class MFAPortletURLFactory {

	public static final String MFA_USER_ID =
		MFAPortletURLFactory.class.getName() + "#MFA_USER_ID";

	public static LiferayPortletURL createVerifyURL(
		HttpServletRequest httpServletRequest, String redirectURL,
		long userId) {

		httpServletRequest = PortalUtil.getOriginalServletRequest(
			httpServletRequest);

		HttpSession httpSession = httpServletRequest.getSession();

		httpSession.setAttribute(MFA_USER_ID, userId);

		long plid = 0;
		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		if (themeDisplay != null) {
			plid = themeDisplay.getPlid();
		}

		LiferayPortletURL liferayPortletURL = PortletURLFactoryUtil.create(
			httpServletRequest, MFAPortletKeys.MFA_VERIFY_PORTLET, plid,
			PortletRequest.RENDER_PHASE);

		liferayPortletURL.setParameter(
			"mvcRenderCommandName", "/mfa_verify/view");
		liferayPortletURL.setParameter("redirect", redirectURL);
		liferayPortletURL.setParameter(
			"saveLastPath", Boolean.FALSE.toString());

		return liferayPortletURL;
	}

}