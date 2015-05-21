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

package com.liferay.portlet.configuration.icon.minimize;

import com.liferay.portal.kernel.portlet.configuration.BasePortletConfigurationIcon;
import com.liferay.portal.kernel.portlet.configuration.PortletConfigurationIcon;
import com.liferay.portal.theme.PortletDisplay;
import com.liferay.portal.theme.ThemeDisplay;

import org.osgi.service.component.annotations.Component;

/**
 * @author Eudaldo Alonso
 */
@Component(immediate = true, service = PortletConfigurationIcon.class)
public class MinimizePortletConfigurationIcon
	extends BasePortletConfigurationIcon {

	@Override
	public String getCssClass() {
		return "portlet-minimize portlet-minimize-icon";
	}

	@Override
	public String getImage() {
		ThemeDisplay themeDisplay = _themeDisplayThreadLocal.get();

		PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();

		if (portletDisplay.isStateMin()) {
			return "../aui/resize-vertical";
		}

		return "../aui/minus";
	}

	@Override
	public String getMessage() {
		ThemeDisplay themeDisplay = _themeDisplayThreadLocal.get();

		PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();

		if (portletDisplay.isStateMin()) {
			return "restore";
		}

		return "minimize";
	}

	@Override
	public String getOnClick() {
		ThemeDisplay themeDisplay = _themeDisplayThreadLocal.get();

		PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();

		return "Liferay.Portlet.minimize('#p_p_id_".concat(
			portletDisplay.getId()).concat("_', this); return false;");
	}

	@Override
	public String getURL() {
		ThemeDisplay themeDisplay = _themeDisplayThreadLocal.get();

		PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();

		return portletDisplay.getURLMin();
	}

	@Override
	public double getWeight() {
		return 6.0;
	}

	@Override
	public boolean isShow() {
		ThemeDisplay themeDisplay = _themeDisplayThreadLocal.get();

		PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();

		return portletDisplay.isShowMinIcon();
	}

	@Override
	public boolean isToolTip() {
		return false;
	}

}