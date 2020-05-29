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

package com.liferay.portal.security.sso.openid.connect.internal;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.sso.openid.connect.OpenIdConnectProvider;
import com.liferay.portal.security.sso.openid.connect.OpenIdConnectProviderRegistry;
import com.liferay.portal.security.sso.openid.connect.OpenIdConnectServiceException;
import com.liferay.portal.security.sso.openid.connect.internal.configuration.OpenIdConnectProviderConfiguration;

import com.nimbusds.openid.connect.sdk.op.OIDCProviderMetadata;
import com.nimbusds.openid.connect.sdk.rp.OIDCClientMetadata;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Collection;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.framework.Constants;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.service.component.annotations.Component;

/**
 * @author Thuong Dinh
 * @author Edward C. Han
 */
@Component(
	immediate = true,
	property = Constants.SERVICE_PID + "=com.liferay.portal.security.sso.openid.connect.internal.configuration.OpenIdConnectProviderConfiguration",
	service = {ManagedServiceFactory.class, OpenIdConnectProviderRegistry.class}
)
public class OpenIdConnectProviderRegistryImpl
	implements ManagedServiceFactory,
			   OpenIdConnectProviderRegistry
				   <OIDCClientMetadata, OIDCProviderMetadata> {

	@Override
	public void deleted(String pid) {
		Dictionary<String, ?> properties = _configurationPidsProperties.remove(
			pid);

		removeOpenIdConnectProvider(
			GetterUtil.getLong(properties.get("companyId")),
			ConfigurableUtil.createConfigurable(
				OpenIdConnectProviderConfiguration.class, properties));
	}

	@Override
	public OpenIdConnectProvider<OIDCClientMetadata, OIDCProviderMetadata>
			findOpenIdConnectProvider(long companyId, String name)
		throws OpenIdConnectServiceException.ProviderException {

		OpenIdConnectProvider<OIDCClientMetadata, OIDCProviderMetadata>
			openIdConnectProvider = getOpenIdConnectProvider(companyId, name);

		if (openIdConnectProvider == null) {
			throw new OpenIdConnectServiceException.ProviderException(
				"Unable to find an OpenId Connect provider with name " + name);
		}

		return openIdConnectProvider;
	}

	@Override
	public String getName() {
		return "OpenId Connect Provider Factory";
	}

	@Override
	public OpenIdConnectProvider<OIDCClientMetadata, OIDCProviderMetadata>
		getOpenIdConnectProvider(long companyId, String name) {

		Map
			<String,
			 OpenIdConnectProvider<OIDCClientMetadata, OIDCProviderMetadata>>
				openIdConnectProviderMap =
					_companyIdProviderNameOpedIdConnectProviders.get(companyId);

		if (openIdConnectProviderMap != null) {
			OpenIdConnectProvider<OIDCClientMetadata, OIDCProviderMetadata>
				openIdConnectProvider = openIdConnectProviderMap.get(name);

			if (openIdConnectProvider != null) {
				return openIdConnectProvider;
			}
		}

		openIdConnectProviderMap =
			_companyIdProviderNameOpedIdConnectProviders.get(
				CompanyConstants.SYSTEM);

		if (openIdConnectProviderMap != null) {
			OpenIdConnectProvider<OIDCClientMetadata, OIDCProviderMetadata>
				openIdConnectProvider = openIdConnectProviderMap.get(name);

			if (openIdConnectProvider != null) {
				return openIdConnectProvider;
			}
		}

		return null;
	}

	@Override
	public Collection<String> getOpenIdConnectProviderNames(long companyId) {
		Set<String> openIdConnectProviderNames = new HashSet<>();

		Map
			<String,
			 OpenIdConnectProvider<OIDCClientMetadata, OIDCProviderMetadata>>
				openIdConnectProviderMap =
					_companyIdProviderNameOpedIdConnectProviders.get(companyId);

		if (openIdConnectProviderMap != null) {
			openIdConnectProviderNames.addAll(
				openIdConnectProviderMap.keySet());
		}

		openIdConnectProviderMap =
			_companyIdProviderNameOpedIdConnectProviders.get(
				CompanyConstants.SYSTEM);

		if (openIdConnectProviderMap != null) {
			openIdConnectProviderNames.addAll(
				openIdConnectProviderMap.keySet());
		}

		return openIdConnectProviderNames;
	}

	@Override
	public void updated(String pid, Dictionary<String, ?> properties)
		throws ConfigurationException {

		Dictionary<String, ?> oldProperties = _configurationPidsProperties.put(
			pid, properties);

		OpenIdConnectProviderConfiguration openIdConnectProviderConfiguration =
			ConfigurableUtil.createConfigurable(
				OpenIdConnectProviderConfiguration.class, properties);

		if (oldProperties != null) {
			removeOpenIdConnectProvider(
				GetterUtil.getLong(properties.get("companyId")),
				openIdConnectProviderConfiguration);
		}

		addOpenIdConnectProvider(
			GetterUtil.getLong(properties.get("companyId")),
			createOpenIdConnectProvider(openIdConnectProviderConfiguration));
	}

	protected void addOpenIdConnectProvider(
		long companyId,
		OpenIdConnectProvider<OIDCClientMetadata, OIDCProviderMetadata>
			openIdConnectProvider) {

		_companyIdProviderNameOpedIdConnectProviders.compute(
			companyId,
			(cid, openIdConnectProviderMap) -> {
				if (openIdConnectProviderMap == null) {
					openIdConnectProviderMap = new ConcurrentHashMap<>();
				}

				if (openIdConnectProviderMap.containsKey(
						openIdConnectProvider.getName())) {

					throw new RuntimeException("Duplicated name");
				}

				openIdConnectProviderMap.put(
					openIdConnectProvider.getName(), openIdConnectProvider);

				return openIdConnectProviderMap;
			});
	}

	protected OpenIdConnectProvider<OIDCClientMetadata, OIDCProviderMetadata>
			createOpenIdConnectProvider(
				OpenIdConnectProviderConfiguration
					openIdConnectProviderConfiguration)
		throws ConfigurationException {

		try {
			return new OpenIdConnectProviderImpl(
				openIdConnectProviderConfiguration.providerName(),
				openIdConnectProviderConfiguration.openIdConnectClientId(),
				openIdConnectProviderConfiguration.openIdConnectClientSecret(),
				openIdConnectProviderConfiguration.scopes(),
				getOpenIdConnectMetadataFactory(
					openIdConnectProviderConfiguration));
		}
		catch (Exception exception) {
			throw new ConfigurationException(
				null,
				StringBundler.concat(
					"Unable to instantiate provider metadata factory for ",
					openIdConnectProviderConfiguration.providerName(), ": ",
					exception.getMessage()),
				exception);
		}
	}

	protected OpenIdConnectMetadataFactory getOpenIdConnectMetadataFactory(
			OpenIdConnectProviderConfiguration
				openIdConnectProviderConfiguration)
		throws MalformedURLException,
			   OpenIdConnectServiceException.ProviderException {

		if (Validator.isNotNull(
				openIdConnectProviderConfiguration.discoveryEndPoint())) {

			return new OpenIdConnectMetadataFactoryImpl(
				openIdConnectProviderConfiguration.providerName(),
				new URL(openIdConnectProviderConfiguration.discoveryEndPoint()),
				openIdConnectProviderConfiguration.
					discoveryEndPointCacheInMillis());
		}

		return new OpenIdConnectMetadataFactoryImpl(
			openIdConnectProviderConfiguration.providerName(),
			openIdConnectProviderConfiguration.idTokenSigningAlgValues(),
			openIdConnectProviderConfiguration.issuerURL(),
			openIdConnectProviderConfiguration.subjectTypes(),
			openIdConnectProviderConfiguration.jwksURI(),
			openIdConnectProviderConfiguration.authorizationEndPoint(),
			openIdConnectProviderConfiguration.tokenEndPoint(),
			openIdConnectProviderConfiguration.userInfoEndPoint());
	}

	protected void removeOpenIdConnectProvider(
		long companyId,
		OpenIdConnectProviderConfiguration openIdConnectProviderConfiguration) {

		_companyIdProviderNameOpedIdConnectProviders.compute(
			companyId,
			(cid, openIdConnectProviderMap) -> {
				if (openIdConnectProviderMap == null) {
					return null;
				}

				openIdConnectProviderMap.remove(
					openIdConnectProviderConfiguration.providerName());

				if (openIdConnectProviderMap.isEmpty()) {
					return null;
				}

				return openIdConnectProviderMap;
			});
	}

	private final Map
		<Long,
		 Map
			 <String,
			  OpenIdConnectProvider<OIDCClientMetadata, OIDCProviderMetadata>>>
				_companyIdProviderNameOpedIdConnectProviders =
					new ConcurrentHashMap<>();
	private final Map<String, Dictionary<String, ?>>
		_configurationPidsProperties = new ConcurrentHashMap<>();

}