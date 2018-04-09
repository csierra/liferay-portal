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

package com.liferay.oauth2.provider.rest.internal.endpoint;

import com.liferay.oauth2.provider.rest.internal.endpoint.constants.OAuth2ProviderRestEndpointConstants;
import com.liferay.oauth2.provider.rest.internal.endpoint.liferay.LiferayOAuthDataProvider;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.apache.cxf.rs.security.oauth2.provider.OAuthJSONProvider;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Carlos Sierra Andrés
 */
@ApplicationPath("/")
@Component(immediate = true, service = Application.class)
public class OAuth2EndpointApplication extends Application {

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(" + OAuth2ProviderRestEndpointConstants.LIFERAY_OAUTH2_ENDPOINT_CLASS + "=true)",
		unbind = "removeOAuth2Class"
	)
	public void addOAuth2Class(Class<?> cls) {
		_liferayOAuth2Classes.add(cls);
	}

	@Reference(
		cardinality = ReferenceCardinality.AT_LEAST_ONE,
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(" + OAuth2ProviderRestEndpointConstants.LIFERAY_OAUTH2_ENDPOINT + "=true)",
		unbind = "removeOAuth2Endpoint"
	)
	public void addOAuth2Endpoint(Object endpoint) {
		_liferayOauth2Endpoints.add(endpoint);
	}

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<>(_liferayOAuth2Classes);

		classes.add(OAuthJSONProvider.class);

		return classes;
	}

	@Override
	public Set<Object> getSingletons() {
		return new HashSet<>(_liferayOauth2Endpoints);
	}

	public void removeOAuth2Class(Class<?> cls) {
		_liferayOAuth2Classes.remove(cls);
	}

	public void removeOAuth2Endpoint(Object endpoint) {
		_liferayOauth2Endpoints.remove(endpoint);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		OAuth2EndpointApplication.class);

	private final List<Class<?>> _liferayOAuth2Classes = new ArrayList<>();
	private final List<Object> _liferayOauth2Endpoints = new ArrayList<>();

	@Reference(policyOption = ReferencePolicyOption.GREEDY)
	private LiferayOAuthDataProvider _liferayOAuthDataProvider;

	@Reference
	private Portal _portal;

}