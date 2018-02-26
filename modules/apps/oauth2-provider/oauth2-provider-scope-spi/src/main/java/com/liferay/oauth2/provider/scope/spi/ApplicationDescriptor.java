package com.liferay.oauth2.provider.scope.spi;

import java.util.Locale;

/**
 * Represents the localization information for OAuth2 applications
 */
public interface ApplicationDescriptor {

	/**
	 * Localize an application for a given locale.
	 *
	 * @param locale the locale requested for the description.
	 * @return a description for the applicationName in the requested locale.
	 */
	public String describeApplication(Locale locale);

}