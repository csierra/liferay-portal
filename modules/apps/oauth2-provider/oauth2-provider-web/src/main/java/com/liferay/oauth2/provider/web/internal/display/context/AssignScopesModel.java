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
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.Validator;

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

					ownedEntryRelations._globalAuthorizationModels.add(
						globalAuthorizationModel);
				}

				applicationAuthorizationModel =
					applicationAuthorizationModel.subtract(localEntry.getKey());
			}

			Relations relations = assignmentModel.computeIfAbsent(
				applicationAuthorizationModel,
				authorizationModel -> new Relations());

			relations._globalAuthorizationModels.add(globalAuthorizationModel);
		}

		return _normalize(assignmentModel);
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
			_globalAuthorizationModels = new HashSet<>();
		}

		public Relations(
			String scopeAlias,
			Set<AuthorizationModel> globalAuthorizationModels) {

			_scopeAlias = scopeAlias;
			_globalAuthorizationModels = new HashSet<>(
				globalAuthorizationModels);
		}

		public boolean equals(Object obj2) {
			if (!(obj2 instanceof Relations)) {
				return false;
			}

			Relations relations2 = (Relations)obj2;

			if (((_scopeAlias == null) &&
				 (relations2._scopeAlias != null)) ||
				((_scopeAlias != null) &&
				 !_scopeAlias.equals(relations2._scopeAlias))) {

				return false;
			}

			if (((_globalAuthorizationModels == null) &&
				 (relations2._globalAuthorizationModels != null)) ||
				((_globalAuthorizationModels != null) &&
				 !_globalAuthorizationModels.equals(
					 relations2._globalAuthorizationModels))) {

				return false;
			}

			return true;
		}

		public Set<String> getGlobalScopeAliases() {
			Map<AuthorizationModel, Relations>
				globalAuthorizationModelsRelations =
					getAuthorizationModelsRelations(_globalAuthorizationModels);

			Collection<Relations> globalRelations =
				globalAuthorizationModelsRelations.values();

			Stream<Relations> globalRelationsStream =
				globalRelations.stream();

			Set<String> globalScopeAliases =
				globalRelationsStream.map(
					Relations::getScopeAlias
				).collect(
					Collectors.toSet()
				);

			return globalScopeAliases;
		}

		public String getScopeAlias() {
			return _scopeAlias;
		}

		@Override
		public int hashCode() {
			return _globalAuthorizationModels.hashCode();
		}

		private Set<AuthorizationModel> _globalAuthorizationModels;
		private String _scopeAlias = StringPool.BLANK;

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
				authorizationModel, new Relations(scopeAlias, new HashSet<>()));

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

			String scopeAlias =
				authorizationModelRelations.getScopeAlias();

			AuthorizationModel authorizationModel =
				authorizationModelsRelationsEntry.getKey();

			// Preserve AuthorizationModels that are assigned an alias

			if (!Validator.isBlank(scopeAlias)) {
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

				combinedRelations._globalAuthorizationModels.addAll(
					authorizationModelRelations._globalAuthorizationModels);
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