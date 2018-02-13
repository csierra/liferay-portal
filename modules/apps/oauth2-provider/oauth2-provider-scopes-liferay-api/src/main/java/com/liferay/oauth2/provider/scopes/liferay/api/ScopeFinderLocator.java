package com.liferay.oauth2.provider.scopes.liferay.api;

import com.liferay.oauth2.provider.model.LiferayOAuth2Scope;

import java.util.Collection;

/**
 * This interface allows to access the ScopeFinders with <i>per-company</i>
 * configurations in a Liferay environment.
 */
public interface ScopeFinderLocator {

	/**
	 * Given a company and a scope name returns a collection of matching
	 * scopes with the configuration for that company.
	 * @param company the company for which the scopes are to be located
	 * @param scope the requested scope
	 * @return a collection of matching scopes for the given company and
	 * scope name
	 */
	Collection<LiferayOAuth2Scope> locateScopes(
		long companyId, String scope);

	/**
	 * Given a company and a scope name returns a collection of matching
	 * scopes with the configuration for that company.
	 * @param company the company for which the scopes are to be located
	 * @param scope the requested scope
	 * @return a collection of matching scopes for the given company and
	 * scope name
	 */
	Collection<LiferayOAuth2Scope> locateScopesForApplication(
		long companyId, String applicationName, String scope);

	/**
	 * Returns a list of the scope names that would be a match if
	 * requested for a company.
	 * @param company the company to list the scope names
	 * @return a collection of scope names that would be a match for the
	 * company.
	 */
	Collection<String> listAliases(long companyId);

	Collection<String> listAliasesForApplication(
		long companyId, String applicationName);

	/*Map<String, Set<String>> describeScopes(
		long companyId, Locale locale, String ... scopes);*/

}