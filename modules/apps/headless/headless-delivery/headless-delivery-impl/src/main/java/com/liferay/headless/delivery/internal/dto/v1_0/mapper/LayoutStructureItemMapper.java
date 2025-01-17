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

package com.liferay.headless.delivery.internal.dto.v1_0.mapper;

import com.liferay.headless.delivery.dto.v1_0.PageElement;
import com.liferay.layout.util.structure.LayoutStructureItem;

import java.util.Locale;

/**
 * @author Jürgen Kappler
 */
public interface LayoutStructureItemMapper {

	public String getClassName();

	public PageElement getPageElement(
		long groupId, LayoutStructureItem layoutStructureItem,
		boolean saveInlineContent, boolean saveMappingConfiguration);

	public default PageElement getPageElement(
		long groupId, LayoutStructureItem layoutStructureItem,
		boolean acceptAllLanguages, Locale locale, boolean saveInlineContent,
		boolean saveMappingConfiguration) {

		return getPageElement(
			groupId, layoutStructureItem, saveInlineContent,
			saveMappingConfiguration);
	}

}