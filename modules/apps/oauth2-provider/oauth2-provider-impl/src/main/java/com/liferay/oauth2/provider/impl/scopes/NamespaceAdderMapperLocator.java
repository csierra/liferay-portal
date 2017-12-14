package com.liferay.oauth2.provider.impl.scopes;

import com.liferay.oauth2.provider.api.scopes.NamespaceAdderMapper;
import com.liferay.portal.kernel.model.Company;

public interface NamespaceAdderMapperLocator {

	public NamespaceAdderMapper locateMapper(Company company);

}
