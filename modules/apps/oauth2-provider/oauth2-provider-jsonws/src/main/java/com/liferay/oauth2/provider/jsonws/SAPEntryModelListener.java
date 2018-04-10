/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.oauth2.provider.jsonws;

import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.security.service.access.policy.model.SAPEntry;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Tomas Polesovsky
 */
@Component(immediate = true, service = ModelListener.class)
public class SAPEntryModelListener extends BaseModelListener<SAPEntry> {

	@Override
	public void onAfterCreate(SAPEntry model) throws ModelListenerException {
		_sapEntryScopeRegistry.registerSAPEntry(model);
		_oAuth2SAPEntryScopesPublisher.publishSAPEntries(model.getCompanyId());
	}

	@Override
	public void onAfterRemove(SAPEntry model) throws ModelListenerException {
		_sapEntryScopeRegistry.unregisterSAPEntry(model);
		_oAuth2SAPEntryScopesPublisher.publishSAPEntries(model.getCompanyId());
	}

	@Override
	public void onAfterUpdate(SAPEntry model) throws ModelListenerException {
		_sapEntryScopeRegistry.updateSAPEntry(model);
		_oAuth2SAPEntryScopesPublisher.publishSAPEntries(model.getCompanyId());
	}

	@Reference
	private OAuth2SAPEntryScopesPublisher _oAuth2SAPEntryScopesPublisher;

	@Reference
	private SAPEntryScopeRegistry _sapEntryScopeRegistry;

}