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

package com.liferay.oauth2.provider.jsonws.internal.application.descriptor;

import com.liferay.oauth2.provider.jsonws.internal.constants.OAuth2JSONWSConstants;
import com.liferay.oauth2.provider.scope.spi.application.descriptor.ApplicationDescriptor;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ResourceBundleLoader;
import com.liferay.portal.kernel.util.ResourceBundleUtil;

import java.util.Locale;
import java.util.ResourceBundle;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Tomas Polesovsky
 */
@Component(
	property = "osgi.jaxrs.name=" + OAuth2JSONWSConstants.OSGI_JAXRS_APPLICATION_NAME
)
public class OAuth2JSONWSApplicationDescriptor
	implements ApplicationDescriptor {

	@Override
	public String describeApplication(Locale locale) {
		String languageId = LocaleUtil.toLanguageId(locale);

		ResourceBundle resourceBundle =
			_resourceBundleLoader.loadResourceBundle(languageId);

		return ResourceBundleUtil.getString(resourceBundle, _KEY);
	}

	private static final String _KEY =
		"oauth2.application.description." +
			OAuth2JSONWSConstants.OSGI_JAXRS_APPLICATION_NAME;

	@Reference(
		target = "(bundle.symbolic.name=com.liferay.oauth2.provider.jsonws)"
	)
	private ResourceBundleLoader _resourceBundleLoader;

}