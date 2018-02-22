package com.liferay.oauth2.provider.scopes.spi;

import com.liferay.oauth2.provider.scopes.spi.model.ScopeMatcher;

import java.util.Map;
import java.util.Set;

/**
 * This class is the entry point to the OAuth2 Scopes framework. Applications
 * will need to register one ScopeFinder, or have one registered on their
 * behalf, with the property osgi.jaxrs.name. This name must be unique across
 * the instance. The property matches with the mandatory property for
 * OSGi JAX-RS spec.
 *
 */
public interface ScopeFinder {

	/**
	 * Returns the list of scopes, internal to the application, that the given
	 * ScopeMatcher matches. Implementations may return several scopes for a
	 * given <i>query</i>. For example, if a particular implementation
	 * wants the scope <i>RW</i> (standing for <i>read-write</i>) to imply the
	 * scope <i>RO</i> (standing for <i>read-only</i>) it can test whether "RW"
	 * is a match using <code>scopeMatcher.match("RW")</code> and, if
	 * <i>true</i>, then return a collection containing both <i>RW</i> and
	 * <i>RO</i>.
	 * Implementations <b>SHOULD</b> test all their available scopes against
	 * the matcher to allow for different matching strategies to succeed.
	 * Only the scopes returned can be used on
	 * {@link com.liferay.oauth2.apps.api.RequiresScope} annotation
	 * or in invocations to
	 * {@link com.liferay.oauth2.apps.api.ScopeChecker} methods.
	 *
	 * @param scopeMatcher to filter the application available scopes.
	 * @return a collection of the available scopes that the implementation
	 * will associate with the given {@link ScopeMatcher}.
	 */
	public Map<String, Set<String>> findScopes();
}