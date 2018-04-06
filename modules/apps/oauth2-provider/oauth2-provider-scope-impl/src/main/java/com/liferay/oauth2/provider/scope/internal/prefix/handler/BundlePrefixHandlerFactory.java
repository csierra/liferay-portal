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

package com.liferay.oauth2.provider.scope.internal.prefix.handler;

import com.liferay.oauth2.provider.scope.internal.configuration.BundlePrefixHandlerFactoryConfiguration;
import com.liferay.oauth2.provider.scope.spi.prefix.handler.PrefixHandler;
import com.liferay.oauth2.provider.scope.spi.prefix.handler.PrefixHandlerFactory;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

import java.io.IOException;
import java.io.StringReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;

/**
 * @author Carlos Sierra Andrés
 * @author Stian Sigvartsen
 */
@Component(
	configurationPid = "com.liferay.oauth2.provider.scope.internal.configuration.BundlePrefixHandlerFactoryConfiguration",
	configurationPolicy = ConfigurationPolicy.OPTIONAL, immediate = true,
	property = {"default=true"}
)
public class BundlePrefixHandlerFactory implements PrefixHandlerFactory {

	@Override
	public PrefixHandler create(
		Function<String, Object> propertyAccessorFunction) {

		List<String> parts = new ArrayList<>(_serviceProperties.size() + 1);

		if (_includeBundleSymbolicName) {
			long bundleId = Long.parseLong(
				propertyAccessorFunction.apply("service.bundleid").toString());

			Bundle bundle = _bundleContext.getBundle(bundleId);

			parts.add(bundle.getSymbolicName());
		}

		for (String serviceProperty : _serviceProperties) {
			int modifiersStart = serviceProperty.indexOf(StringPool.SPACE);
			String modifiersString = StringPool.BLANK;

			if (modifiersStart > -1) {
				modifiersString = serviceProperty.substring(modifiersStart);
				serviceProperty = serviceProperty.substring(0, modifiersStart);
			}

			Object applyResult = propertyAccessorFunction.apply(
				serviceProperty);

			if (applyResult != null) {
				parts.add(applyResult.toString());
				continue;
			}

			String propertiesFormat = modifiersString.replaceAll(
				StringPool.SPACE, "\n");

			Properties modifiers = new Properties();

			try {
				modifiers.load(new StringReader(propertiesFormat));
			}
			catch (IOException ioe) {
				throw new IllegalArgumentException(ioe);
			}

			parts.add(GetterUtil.getString(modifiers.getProperty("default")));
		}

		PrefixHandler prefixHandler = create(
			parts.toArray(_EMPTY_STRING_ARRAY));

		return input -> {
			if (_excludedScope.contains(input)) {
				return input;
			}

			return prefixHandler.addPrefix(input);
		};
	}

	public PrefixHandler create(String... prefixes) {
		StringBundler sb = new StringBundler(prefixes.length * 2);

		for (String prefix : prefixes) {
			sb.append(prefix);
			sb.append(_separator);
		}

		return input -> sb.toString() + input;
	}

	@Activate
	protected void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		BundlePrefixHandlerFactoryConfiguration
			bundlePrefixHandlerFactoryConfiguration =
				ConfigurableUtil.createConfigurable(
					BundlePrefixHandlerFactoryConfiguration.class, properties);

		_bundleContext = bundleContext;

		Stream<String> stream = Arrays.stream(
			bundlePrefixHandlerFactoryConfiguration.excludedScopes());

		_excludedScope = stream.filter(
			e -> !Validator.isBlank(e)
		).collect(
			Collectors.toList()
		);

		_includeBundleSymbolicName =
			bundlePrefixHandlerFactoryConfiguration.includeBundleSymbolicName();

		_separator = bundlePrefixHandlerFactoryConfiguration.separator();

		stream = Arrays.stream(
			bundlePrefixHandlerFactoryConfiguration.serviceProperties());

		_serviceProperties = stream.filter(
			e -> !Validator.isBlank(e)
		).collect(
			Collectors.toList()
		);
	}

	private static final String[] _EMPTY_STRING_ARRAY = new String[0];

	private BundleContext _bundleContext;
	private List<String> _excludedScope;
	private boolean _includeBundleSymbolicName;
	private String _separator = StringPool.SLASH;
	private List<String> _serviceProperties;

}