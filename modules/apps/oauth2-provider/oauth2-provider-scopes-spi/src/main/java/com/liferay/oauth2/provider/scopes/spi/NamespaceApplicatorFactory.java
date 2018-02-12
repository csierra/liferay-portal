package com.liferay.oauth2.provider.scopes.spi;

/**
 * Convenience interface to create different {@link NamespaceApplicator} for different
 * objects.
 */
public interface NamespaceApplicatorFactory {

	/**
	 * This method allows to create a {@link NamespaceApplicator} using the properties
	 * in the {@link PropertyGetter}
	 *
	 * @param propertyGetter the {@link PropertyGetter} to configure the
	 *                       {@link NamespaceApplicator}
	 * @return the {@link NamespaceApplicator} for the given {@link PropertyGetter}
	 */
	public NamespaceApplicator mapFrom(PropertyGetter propertyGetter);

}