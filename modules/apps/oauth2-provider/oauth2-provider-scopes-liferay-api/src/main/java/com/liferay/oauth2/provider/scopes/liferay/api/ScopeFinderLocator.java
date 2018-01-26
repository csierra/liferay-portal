package com.liferay.oauth2.provider.scopes.liferay.api;

import com.liferay.oauth2.provider.model.LiferayOAuth2Scope;
import com.liferay.portal.kernel.model.Company;

import java.util.Collection;

/**
 * This interface allows to access the ScopeFinders with <i>per-company</i>
 * configurations in a Liferay environment.
 */
public interface ScopeFinderLocator {

	/**
	 * Returns a list of the scope names that would be a match if
	 * requested for a company.
	 * @param company the company to list the scope names
	 * @return a collection of scope names that would be a match for the
	 * company.
	 */
	Collection<LiferayOAuth2Scope> listScopes();

}