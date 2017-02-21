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

package com.liferay.frontend.editor.alloyeditor.accessibility.editor.dynamic;

import com.liferay.portal.kernel.bean.BeanPropertiesUtil;
import com.liferay.portal.kernel.servlet.taglib.TagDynamicIdFactory;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Antonio Pol
 */
@Component(
	immediate = true,
	property = {
		"tagClassName=com.liferay.taglib.util.HtmlTopTag"
	},
	service = TagDynamicIdFactory.class
)
public class EditorAccessibilityDynamicIdFactory implements TagDynamicIdFactory {

	@Override
	public String getTagDynamicId(
		HttpServletRequest request, HttpServletResponse response, Object tag) {

		System.out.println("HTML TOP TAG INCLUDE");

		String portletId = PortalUtil.getPortletId(request);

		// if (Validator.isNull(portletId)) {
		// 	return null;
		// }

		// String name = BeanPropertiesUtil.getStringSilent(tag, "name");

		// if (Validator.isNull(name)) {
		// 	return null;
		// }

		return portletId.concat(StringPool.DASH).concat("alloyEditor");
	}

}