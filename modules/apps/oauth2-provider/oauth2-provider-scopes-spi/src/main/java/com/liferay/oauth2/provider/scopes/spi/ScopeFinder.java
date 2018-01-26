package com.liferay.oauth2.provider.scopes.spi;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

/**
 * This class is the entry point to the OAuth2 Scopes framework. Applications
 * will need to register one ScopeFinder, or have one registered on their
 * behalf, with the property osgi.jaxrs.name. This name must be unique across
 * the instance. The property matches with the mandatory property for
 * OSGi JAX-RS spec.
 *
 */
public interface ScopeFinder {

	public Collection<String> findScopes();

	public default String describe(
		Collection<String> scopes, Locale locale) {

		return String.join(", ", scopes);
	}

}