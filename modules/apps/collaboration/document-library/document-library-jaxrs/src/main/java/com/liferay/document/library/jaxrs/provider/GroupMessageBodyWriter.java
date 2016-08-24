/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.document.library.jaxrs.provider;

import com.liferay.document.library.jaxrs.GroupRepr;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.util.LocaleUtil;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Variant;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * @author Carlos Sierra Andrés
 */
public class GroupMessageBodyWriter extends
	DelegatingMessageBodyWriter<Group, GroupRepr> {

	@Context
	HttpServletRequest _httpServletRequest;

	@Context
	Request request;

	protected Class<Group> getOriginClass() {
		return Group.class;
	}

	protected Class<GroupRepr> getDestination() {
		return GroupRepr.class;
	}

	protected GroupRepr map(Group group) {
		Variant.VariantListBuilder variantListBuilder =
			Variant.VariantListBuilder.newInstance();

		Set<Locale> availableLocales = group.getNameMap().keySet();

		variantListBuilder = variantListBuilder.languages(
			availableLocales.toArray(new Locale[0]));

		List<Variant> variants = variantListBuilder.build();

		Locale language;

		if (variants == null || variants.isEmpty()) {
			language = LocaleUtil.getDefault();
		}
		else {
			Variant variant = request.selectVariant(variants);

			language = variant.getLanguage();
		}

		return new GroupRepr(
			group.getGroupId(),
			_httpServletRequest.getRequestURI() + "/" +
			group.getGroupId(), group.getName(language, true),
			group.getFriendlyURL(), group.getType());
	}
}
