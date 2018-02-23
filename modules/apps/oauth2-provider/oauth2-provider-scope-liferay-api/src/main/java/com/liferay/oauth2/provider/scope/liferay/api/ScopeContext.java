package com.liferay.oauth2.provider.scope.liferay.api;

import com.liferay.portal.kernel.model.Company;
import org.osgi.framework.Bundle;

/**
 * This interface represents the context associated to the scope.
 * This scope, together with the scope name, will univocally identify
 * a checking point in a Liferay environment.
 */
public interface ScopeContext {

	void setCompany(Company company);

	void setBundle(Bundle bundle);

	void setApplicationName(String applicationName);

	String getTokenString();

	void setTokenString(String tokenString);

	void clear();
}