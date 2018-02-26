package com.liferay.oauth2.provider.scope.spi;

import java.util.Collection;

/**
 * This class is the entry point to the OAuth2 Scopes framework. Applications
 * will need to register one ScopeFinder, or have one registered on their
 * behalf, with the property osgi.jaxrs.name. This name must be unique across
 * the instance. The property matches with the mandatory property for
 * OSGi JAX-RS spec.
 *
 * @author Carlos Sierra Andrés
 * @review
 */
public interface ScopeFinder {

	/**
	 * Returns the list of scope, internal to the application.
	 * @return a collection of the available scope.
	 * @review
	 */
	public Collection<String> findScopes();
}