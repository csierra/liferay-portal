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

package com.liferay.oauth2.provider.web.internal.display.context;

import com.liferay.oauth2.provider.scope.liferay.ApplicationDescriptorLocator;
import com.liferay.oauth2.provider.scope.liferay.LiferayOAuth2Scope;
import com.liferay.oauth2.provider.scope.liferay.ScopeDescriptorLocator;
import com.liferay.oauth2.provider.scope.spi.application.descriptor.ApplicationDescriptor;
import com.liferay.oauth2.provider.scope.spi.scope.descriptor.ScopeDescriptor;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Stian Sigvartsen
 */
public class AuthorizationModel {

	public AuthorizationModel(
		ApplicationDescriptorLocator applicationDescriptorLocator,
		Locale locale, ScopeDescriptorLocator scopeDescriptorLocator) {

		this(
			new HashMap<>(), applicationDescriptorLocator, new HashSet<>(),
			locale, scopeDescriptorLocator);
	}

	public AuthorizationModel(
		Map<String, Set<String>> applicationScopes,
		ApplicationDescriptorLocator applicationDescriptorLocator,
		Set<LiferayOAuth2Scope> liferayOAuth2Scopes,
		Locale locale, ScopeDescriptorLocator scopeDescriptorLocator) {

		_applicationScopes = applicationScopes;
		_liferayOAuth2Scopes = liferayOAuth2Scopes;
		_applicationDescriptorLocator = applicationDescriptorLocator;
		_locale = locale;
		_scopeDescriptorLocator = scopeDescriptorLocator;
	}

	public AuthorizationModel add(AuthorizationModel model2) {
		Set<LiferayOAuth2Scope> liferayOAuth2Scopes = new HashSet<>();

		liferayOAuth2Scopes.addAll(_liferayOAuth2Scopes);
		liferayOAuth2Scopes.addAll(model2.getLiferayOAuth2Scopes());

		Map<String, Set<String>> combinedApplicationScopesMap = new HashMap<>(
			_applicationScopes);

		for (Map.Entry<String, Set<String>> entry :
				model2._applicationScopes.entrySet()) {

			combinedApplicationScopesMap.compute(
				entry.getKey(),
				(key, existingValue) -> {
					HashSet<String> newValue = new HashSet<>(entry.getValue());

					if (existingValue != null) {
						newValue.addAll(existingValue);
					}

					return newValue;
				});
		}

		AuthorizationModel authorizationModel = new AuthorizationModel(
			combinedApplicationScopesMap, _applicationDescriptorLocator,
			liferayOAuth2Scopes, _locale, _scopeDescriptorLocator);

		return authorizationModel;
	}

	public void addLiferayOAuth2Scopes(
		Collection<LiferayOAuth2Scope> liferayOAuth2Scopes) {

		_liferayOAuth2Scopes.addAll(liferayOAuth2Scopes);

		for (LiferayOAuth2Scope liferayOAuth2Scope : liferayOAuth2Scopes) {
			Set<String> applicationScopes = _applicationScopes.computeIfAbsent(
				liferayOAuth2Scope.getApplicationName(), __ -> new HashSet<>());

			applicationScopes.add(liferayOAuth2Scope.getScope());
		}
	}

	public boolean contains(AuthorizationModel model2) {
		if (!_liferayOAuth2Scopes.containsAll(
				model2.getLiferayOAuth2Scopes())) {

				return false;
		}

		for (String applicationName : model2.getApplicationNames()) {
			if (!_applicationScopes.containsKey(applicationName)) {
				return false;
			}

			Set<String> applicationScopes = _applicationScopes.get(
				applicationName);

			if (!applicationScopes.containsAll(
					model2._getApplicationScopes(applicationName))) {

				return false;
			}
		}

		return true;
	}

	@Override
	public boolean equals(Object obj2) {
		if (!(obj2 instanceof AuthorizationModel)) {
			return false;
		}

		AuthorizationModel arm2 = (AuthorizationModel)obj2;

		if (!_liferayOAuth2Scopes.equals(arm2.getLiferayOAuth2Scopes())) {
			return false;
		}

		if (!getApplicationNames().equals(arm2.getApplicationNames())) {
			return false;
		}

		for (String applicationName : getApplicationNames()) {
			if (!_getApplicationScopes(
					applicationName
						).equals(
							arm2._getApplicationScopes(applicationName)
								)) {

				return false;
			}
		}

		return true;
	}

	public String getApplicationDescription(String applicationName) {
		ApplicationDescriptor applicationDescriptor =
			_applicationDescriptorLocator.getApplicationDescriptor(
				applicationName);

		return applicationDescriptor.describeApplication(_locale);
	}

	public String getScopeDescription(LiferayOAuth2Scope liferayOAuth2Scope) {
		ScopeDescriptor scopeDescriptor =
			_scopeDescriptorLocator.getScopeDescriptor(
				liferayOAuth2Scope.getApplicationName());

		return scopeDescriptor.describeScope(
			liferayOAuth2Scope.getScope(), _locale);
	}

	public Set<String> getApplicationNames() {
		Stream<LiferayOAuth2Scope> stream = _liferayOAuth2Scopes.stream();

		return stream.map(
			LiferayOAuth2Scope::getApplicationName
		).collect(
			Collectors.toSet()
		);
	}

	public Map<String, String> getApplicationNamesDescriptions() {
		Stream<LiferayOAuth2Scope> stream = _liferayOAuth2Scopes.stream();

		return stream.map(
			LiferayOAuth2Scope::getApplicationName
		).collect(
			Collectors.toMap(
				Function.identity(), this::getApplicationDescription)
		);
	}

	public Set<String> getApplicationScopeDescription(String applicationName) {
		Stream<LiferayOAuth2Scope> stream = _liferayOAuth2Scopes.stream();

		return stream.filter(
			liferayOAuth2Scope ->
				applicationName.equals(liferayOAuth2Scope.getApplicationName())
		).map(
			this::getScopeDescription
		).collect(
			Collectors.toSet()
		);
	}

	@Override
	public int hashCode() {
		return getApplicationNames().hashCode(); // ???
	}

	public AuthorizationModel reduceToApplication(String applicationName) {
		Stream<LiferayOAuth2Scope> stream = _liferayOAuth2Scopes.stream();

		Set<LiferayOAuth2Scope> liferayOAuth2Scopes = stream.filter(
			liferayOAuth2Scope ->
				applicationName.equals(liferayOAuth2Scope.getApplicationName())
		).collect(
			Collectors.toSet()
		);

		Map<String, Set<String>> applicationScopes = new HashMap<>();

		applicationScopes.put(
			applicationName, _applicationScopes.get(applicationName));

		AuthorizationModel authorizationModel = new AuthorizationModel(
			applicationScopes, _applicationDescriptorLocator,
			liferayOAuth2Scopes, _locale, _scopeDescriptorLocator);

		return authorizationModel;
	}

	public Set<AuthorizationModel> splitByApplicationScopes() {
		Stream<LiferayOAuth2Scope> stream = _liferayOAuth2Scopes.stream();

		return stream.map(
			liferayOAuth2Scope -> new AuthorizationModel(
				Collections.singletonMap(
					liferayOAuth2Scope.getApplicationName(),
					Collections.singleton(liferayOAuth2Scope.getScope())),
				_applicationDescriptorLocator,
				Collections.singleton(liferayOAuth2Scope), _locale,
				_scopeDescriptorLocator)
		).collect(
			Collectors.toSet()
		);
	}

	public AuthorizationModel subtract(AuthorizationModel model2) {
		Set<LiferayOAuth2Scope> liferayOAuth2Scopes =
			new HashSet<>(_liferayOAuth2Scopes);

		liferayOAuth2Scopes.removeAll(model2.getLiferayOAuth2Scopes());

		Map<String, Set<String>> remainingApplicationScopesMap =
			new HashMap<>();

		for (Map.Entry<String, Set<String>> entry :
				_applicationScopes.entrySet()) {

			Set<String> model2ApplicationNames = model2.getApplicationNames();

			if (!model2ApplicationNames.contains(entry.getKey())) {
				remainingApplicationScopesMap.put(
					entry.getKey(), entry.getValue());
			}
			else {
				Set<String> remainingApplicationScopes = new HashSet<>(
					entry.getValue());

				Set<String> model2ApplicationInternalScopes =
					model2._getApplicationScopes(entry.getKey());

				remainingApplicationScopes.removeAll(
					model2ApplicationInternalScopes);

				remainingApplicationScopesMap.put(
					entry.getKey(), remainingApplicationScopes);
			}
		}

		AuthorizationModel authorizationModel = new AuthorizationModel(
			remainingApplicationScopesMap, _applicationDescriptorLocator,
			liferayOAuth2Scopes, _locale, _scopeDescriptorLocator);

		return authorizationModel;
	}

	private Set<String> _getApplicationScopes(String applicationName) {
		Stream<LiferayOAuth2Scope> stream = _liferayOAuth2Scopes.stream();

		return stream.filter(
			liferayOAuth2Scope ->
				applicationName.equals(liferayOAuth2Scope.getApplicationName())
		).map(
			LiferayOAuth2Scope::getScope
		).collect(
			Collectors.toSet()
		);
	}

	public Set<LiferayOAuth2Scope> getLiferayOAuth2Scopes() {
		return _liferayOAuth2Scopes;
	}

	private final ApplicationDescriptorLocator _applicationDescriptorLocator;
	private final Map<String, Set<String>> _applicationScopes;
	private Set<LiferayOAuth2Scope> _liferayOAuth2Scopes = new HashSet<>();
	private final Locale _locale;
	private final ScopeDescriptorLocator _scopeDescriptorLocator;

}