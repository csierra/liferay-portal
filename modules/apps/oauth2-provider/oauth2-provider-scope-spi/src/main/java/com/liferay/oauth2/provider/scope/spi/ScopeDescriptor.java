package com.liferay.oauth2.provider.scope.spi;

import java.util.Locale;

public interface ScopeDescriptor {

	/**
	 * Localize a scope for a given locale.
	 *
	 * @param scope the scope to be described.
	 * @param locale the locale requested for the description.
	 * @return a description for the scope in the requested locale.
	 */
	public String describeScope(String scope, Locale locale);

}