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
import com.liferay.oauth2.provider.scope.liferay.ScopeDescriptorLocator;
import com.liferay.oauth2.provider.scope.liferay.ScopeLocator;
import com.liferay.oauth2.provider.scope.spi.application.descriptor.ApplicationDescriptor;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Stian Sigvartsen
 */
public class AssignScopesModel {

	public AssignScopesModel(
		long companyId, Locale locale, ScopeLocator scopeLocator,
		ApplicationDescriptorLocator applicationDescriptorLocator,
		ScopeDescriptorLocator scopeDescriptorLocator) {

		_applicationDescriptor = applicationDescriptorLocator;
		_locale = locale;

		init(
			companyId, scopeLocator, applicationDescriptorLocator,
			scopeDescriptorLocator);
	}

	public Map<AuthorizationModel, Relations>
		buildAssignmentModelForApplication(String applicationName) {

		Map<AuthorizationModel, Relations> assignmentModel = new HashMap<>();

		Set<AuthorizationModel> localAuthorizationModels =
			_localAuthorizationModels.get(applicationName);

		Map<AuthorizationModel, Relations> localRelations = new HashMap<>();

		if (localAuthorizationModels != null) {
			localRelations.putAll(
				getAuthorizationModelsRelations(localAuthorizationModels));
		}

		Set<AuthorizationModel> globalAuthorizationModels =
			_globalAuthorizationModels.get(applicationName);

		if (globalAuthorizationModels == null) {
			return localRelations;
		}

		assignmentModel.putAll(localRelations);

		for (AuthorizationModel globalAuthorizationModel :
			globalAuthorizationModels) {

			AuthorizationModel applicationAuthorizationModel =
				globalAuthorizationModel.reduceToApplication(applicationName);

			for (Map.Entry<AuthorizationModel, Relations> localEntry :
					localRelations.entrySet()) {

				if (globalAuthorizationModel.contains(localEntry.getKey())) {
					Relations ownedEntryRelations = localEntry.getValue();

					ownedEntryRelations._masterAuthorizationModels.add(
						globalAuthorizationModel);
				}

				applicationAuthorizationModel =
					applicationAuthorizationModel.subtract(localEntry.getKey());
			}

			Relations relations = assignmentModel.computeIfAbsent(
				applicationAuthorizationModel,
				authorizationModel -> new Relations());

			relations._masterAuthorizationModels.add(globalAuthorizationModel);
		}

//		return _normalize(assignmentModel);

		Map<AuthorizationModel, Relations>
			combinedAuthorizationModelsRelations = new HashMap<>();

		for (Map.Entry<AuthorizationModel, Relations>
			authorizationModelsRelationsEntry :
			assignmentModel.entrySet()) {

			Relations authorizationModelRelations =
				authorizationModelsRelationsEntry.getValue();

			Set<String> scopeAliases =
				authorizationModelRelations.getScopeAliases();

			AuthorizationModel authorizationModel =
				authorizationModelsRelationsEntry.getKey();

			// Preserve AuthorizationModels that are assigned an alias

			if ((scopeAliases != null) && !scopeAliases.isEmpty()) {
				combinedAuthorizationModelsRelations.put(
					authorizationModel, authorizationModelRelations);
				continue;
			}

			// Reduce other AuthorizationModels down to individual
			// application scopes. But keep the master AuthorizationModel
			// relations of each original AuthorizationModel

			Set<AuthorizationModel> applicationScopeAuthorizationModels =
				authorizationModel.splitByApplicationScopes();

			for (AuthorizationModel applicationScopeAuthorizationModel :
				applicationScopeAuthorizationModels) {

				Relations combinedRelations =
					combinedAuthorizationModelsRelations.computeIfAbsent(
						applicationScopeAuthorizationModel,
						__ -> new Relations());

				combinedRelations._masterAuthorizationModels.addAll(
					authorizationModelRelations._masterAuthorizationModels);
			}
		}

		HashMap<Relations, AuthorizationModel> relationsAuthorizationModels =
			new HashMap<>();

		// Finally merge those by identical master AuthorizationModel relations

		for (Map.Entry<AuthorizationModel, Relations>
			entry : combinedAuthorizationModelsRelations.entrySet()) {

			relationsAuthorizationModels.compute(
				entry.getValue(),
				(key, existingValue) -> {
					if (existingValue != null) {
						return existingValue.add(entry.getKey());
					}
					else {
						return entry.getKey();
					}
				});
		}

		return _invertMap(relationsAuthorizationModels);
	}

	public String getApplicationDescription(String applicationName) {
		ApplicationDescriptor applicationDescriptor =
			_applicationDescriptor.getApplicationDescriptor(applicationName);

		return applicationDescriptor.describeApplication(_locale);
	}

	public Set<String> getApplicationNames() {
		Set<String> applicationNames = new HashSet<>();

		applicationNames.addAll(_localAuthorizationModels.keySet());
		applicationNames.addAll(_globalAuthorizationModels.keySet());

		return applicationNames;
	}

	public Map<String, String> getApplicationNamesDescriptions() {
		Set<String> applicationNames = getApplicationNames();

		Stream<String> applicationNamesStream = applicationNames.stream();

		return applicationNamesStream.collect(
			Collectors.toMap(
				Function.identity(), this::getApplicationDescription));
	}

	public Map<AuthorizationModel, Relations>
		getGlobalAuthorizationModelsRelations() {

		Collection<Set<AuthorizationModel>> authorizationModels =
			_globalAuthorizationModels.values();

		Stream<Set<AuthorizationModel>> stream = authorizationModels.stream();

		return stream.flatMap(
			Set::stream
		).collect(
			Collectors.toSet()
		).stream(
		).filter(
			_authorizationModelsRelations::containsKey
		).collect(
			Collectors.toMap(
				Function.identity(), _authorizationModelsRelations::get)
		);
	}

	public class Relations {

		public Relations() {
			_scopeAliases = new HashSet<>();
			_masterAuthorizationModels = new HashSet<>();
		}

		public Relations(
			Set<String> scopeAliases,
			Set<AuthorizationModel> masterAuthorizationModels) {

			_scopeAliases = new HashSet<>(scopeAliases);
			_masterAuthorizationModels = new HashSet<>(
				masterAuthorizationModels);
		}

		public boolean equals(Object obj2) {
			if (!(obj2 instanceof Relations)) {
				return false;
			}

			Relations relations2 = (Relations)obj2;

			if (((_scopeAliases == null) &&
				 (relations2._scopeAliases != null)) ||
				((_scopeAliases != null) &&
				 !_scopeAliases.equals(relations2._scopeAliases))) {

				return false;
			}

			if (((_masterAuthorizationModels == null) &&
				 (relations2._masterAuthorizationModels != null)) ||
				((_masterAuthorizationModels != null) &&
				 !_masterAuthorizationModels.equals(
					 relations2._masterAuthorizationModels))) {

				return false;
			}

			return true;
		}

		public Set<String> getMasterAuthorizationModelsScopeAliases() {
			Map<AuthorizationModel, Relations>
				masterAuthorizationModelsRelations =
				getAuthorizationModelsRelations(_masterAuthorizationModels);

			Collection<Relations> masterAuthorizationModelRelations =
				masterAuthorizationModelsRelations.values();

			Stream<Relations> masterAuthorizationModelRelationsStream =
				masterAuthorizationModelRelations.stream();

			Set<String> masterScopeAliases =
				masterAuthorizationModelRelationsStream.flatMap(
					relations -> relations.getScopeAliases().stream()
				).collect(
					Collectors.toSet()
				);

			return masterScopeAliases;
		}

		public Set<String> getScopeAliases() {
			return _scopeAliases;
		}

		@Override
		public int hashCode() {
			return _masterAuthorizationModels.hashCode();
		}

		private Set<AuthorizationModel> _masterAuthorizationModels;
		private Set<String> _scopeAliases;

	}

	protected Map<AuthorizationModel, Relations>
		getAuthorizationModelsRelations(
			Set<AuthorizationModel> authorizationModels) {

		Stream<AuthorizationModel> authorizationModelsStream =
			authorizationModels.stream();

		return authorizationModelsStream.filter(
			_authorizationModelsRelations::containsKey
		).collect(
			Collectors.toMap(
				Function.identity(), _authorizationModelsRelations::get)
		);
	}

	protected void init(
		long companyId, ScopeLocator scopeLocator,
		ApplicationDescriptorLocator applicationDescriptorLocator,
		ScopeDescriptorLocator scopeDescriptorLocator) {

		Set<String> scopeAliases = new HashSet<>(
			scopeLocator.getScopeAliases(companyId));

		for (String scopeAlias : scopeAliases) {
			AuthorizationModel authorizationModel = new AuthorizationModel(
				applicationDescriptorLocator, _locale, scopeDescriptorLocator);

			authorizationModel.addLiferayOAuth2Scopes(
				scopeLocator.getLiferayOAuth2Scopes(companyId, scopeAlias));

			_authorizationModelsRelations.put(
				authorizationModel,
				new Relations(
					Collections.singleton(scopeAlias), new HashSet<>()));

			Set<String> applicationNames =
				authorizationModel.getApplicationNames();

			boolean globalScopeAlias = false;

			if (applicationNames.size() > 1) {
				globalScopeAlias = true;
			}

			if (globalScopeAlias) {
				for (String applicationName : applicationNames) {
					Set<AuthorizationModel> authorizationModels =
						_globalAuthorizationModels.computeIfAbsent(
							applicationName, appName -> new HashSet<>());

					authorizationModels.add(authorizationModel);
				}
			}
			else if (applicationNames.size() == 1) {
				Iterator<String> iterator = applicationNames.iterator();

				String applicationName = iterator.next();

				Set<AuthorizationModel> authorizationModels =
					_localAuthorizationModels.computeIfAbsent(
						applicationName, __ -> new HashSet<>());

				authorizationModels.add(authorizationModel);
			}
		}
	}

	private <K, V> Map<V, K> _invertMap(Map<K, V> map) {
		Map<V, K> ret = new HashMap<>(map.size());

		for (Map.Entry<K, V> entry : map.entrySet()) {
			ret.put(entry.getValue(), entry.getKey());
		}

		return ret;
	}

	private Map<AuthorizationModel, Relations> _normalize(
		Map<AuthorizationModel, Relations> authorizationModelsRelations) {

		Map<AuthorizationModel, Relations>
			combinedAuthorizationModelsRelations = new HashMap<>();

		for (Map.Entry<AuthorizationModel, Relations>
				authorizationModelsRelationsEntry :
					authorizationModelsRelations.entrySet()) {

			Relations authorizationModelRelations =
				authorizationModelsRelationsEntry.getValue();

			Set<String> scopeAliases =
				authorizationModelRelations.getScopeAliases();

			AuthorizationModel authorizationModel =
				authorizationModelsRelationsEntry.getKey();

			// Preserve AuthorizationModels that are assigned an alias

			if ((scopeAliases != null) && !scopeAliases.isEmpty()) {
				combinedAuthorizationModelsRelations.put(
					authorizationModel, authorizationModelRelations);
				continue;
			}

			// Reduce other AuthorizationModels down to individual
			// application scopes. But keep the master AuthorizationModel
			// relations of each original AuthorizationModel

			Set<AuthorizationModel> applicationScopeAuthorizationModels =
				authorizationModel.splitByApplicationScopes();

			for (AuthorizationModel applicationScopeAuthorizationModel :
					applicationScopeAuthorizationModels) {

				Relations combinedRelations =
					combinedAuthorizationModelsRelations.computeIfAbsent(
						applicationScopeAuthorizationModel,
						__ -> new Relations());

				combinedRelations._masterAuthorizationModels.addAll(
					authorizationModelRelations._masterAuthorizationModels);
			}
		}

		HashMap<Relations, AuthorizationModel> relationsAuthorizationModels =
			new HashMap<>();

		// Finally merge those by identical master AuthorizationModel relations

		for (Map.Entry<AuthorizationModel, Relations>
				entry : combinedAuthorizationModelsRelations.entrySet()) {

			relationsAuthorizationModels.compute(
				entry.getValue(),
				(key, existingValue) -> {
					if (existingValue != null) {
						return existingValue.add(entry.getKey());
					}
					else {
						return entry.getKey();
					}
				});
		}

		return _invertMap(relationsAuthorizationModels);
	}

	private final ApplicationDescriptorLocator _applicationDescriptor;
	private Map<AuthorizationModel, Relations> _authorizationModelsRelations =
		new HashMap<>();
	private Map<String, Set<AuthorizationModel>> _globalAuthorizationModels =
		new HashMap<>();
	private Map<String, Set<AuthorizationModel>> _localAuthorizationModels =
		new HashMap<>();
	private final Locale _locale;

}