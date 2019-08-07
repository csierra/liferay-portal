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

package com.liferay.portal.security.credential.service.impl;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.service.PortalPreferencesLocalService;
import com.liferay.portal.kernel.util.PortletKeys;
import com.liferay.portal.security.credential.model.AlgorithmEntry;
import com.liferay.portal.security.credential.service.base.AlgorithmEntryLocalServiceBaseImpl;
import com.liferay.portlet.PortalPreferencesWrapper;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * The implementation of the algorithm entry local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>com.liferay.portal.security.credential.service.AlgorithmEntryLocalService</code> interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author arthurchan35
 * @see AlgorithmEntryLocalServiceBaseImpl
 */
@Component(
	property = "model.class.name=com.liferay.portal.security.credential.model.AlgorithmEntry",
	service = AopService.class
)
public class AlgorithmEntryLocalServiceImpl
	extends AlgorithmEntryLocalServiceBaseImpl {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Use <code>com.liferay.portal.security.credential.service.AlgorithmEntryLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>com.liferay.portal.security.credential.service.AlgorithmEntryLocalServiceUtil</code>.
	 */
	@Override
	public AlgorithmEntry addEntry(String type, long companyId, String meta)
		throws PortalException {

		long entryId = counterLocalService.increment();

		AlgorithmEntry entry = algorithmEntryPersistence.create(entryId);

		entry.setCompanyId(companyId);
		entry.setType(type);
		entry.setMeta(meta);

		entry = algorithmEntryPersistence.update(entry);

		PortalPreferencesWrapper portalPreferencesWrapper =
			(PortalPreferencesWrapper)
				_portalPreferencesLocalService.getPreferences(
					companyId, PortletKeys.PREFS_OWNER_TYPE_COMPANY);

		PortalPreferences portalPreferences =
			portalPreferencesWrapper.getPortalPreferencesImpl();

		portalPreferences.setValue(
			StringPool.BLANK, "LatestAlgorithmEntryId",
			String.valueOf(entryId));

		_portalPreferencesLocalService.updatePreferences(
			companyId, PortletKeys.PREFS_OWNER_TYPE_COMPANY, portalPreferences);

		return entry;
	}

	@Override
	public AlgorithmEntry deleteEntry(AlgorithmEntry entry)
		throws PortalException {

		return algorithmEntryPersistence.remove(entry);
	}

	@Override
	public AlgorithmEntry deleteEntry(long entryId) throws PortalException {
		return algorithmEntryPersistence.remove(entryId);
	}

	@Reference
	private PortalPreferencesLocalService _portalPreferencesLocalService;

}