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

package com.liferay.oauth2.provider.jsonws.internal.service.access.policy.scope;

import com.liferay.oauth2.provider.jsonws.internal.configuration.OAuth2JSONWSApplication;
import com.liferay.oauth2.provider.scope.spi.scope.finder.ScopeFinder;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;

/**
* @author Carlos Sierra Andrés
*/
@Component(
	configurationPolicy = ConfigurationPolicy.REQUIRE,
	configurationPid = "com.liferay.oauth2.provider.jsonws.internal.configuration.OAuth2JSONWSApplication",
	property = "sap.scope.finder=true", service = ScopeFinder.class
)
public class ConfigurableSAPEntryScopeFinder implements ScopeFinder {

	@Override
	public Collection<String> findScopes() {
		return _scopes;
	}

	@Activate
	protected void activate(Map<String, Object> properties) {
		OAuth2JSONWSApplication oAuth2JSONWSApplication =
			ConfigurableUtil.createConfigurable(
				OAuth2JSONWSApplication.class, properties);

		_scopes = Arrays.asList(
			oAuth2JSONWSApplication.oauth2RepublishedScopes());
	}

	private List<String> _scopes;

}