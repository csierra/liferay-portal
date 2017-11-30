package com.liferay.oauth2.provider.api.scopes;

import com.liferay.portal.kernel.model.Company;

public interface ScopeFinderLocator {

	ScopeFinder locate(Company company);

}
