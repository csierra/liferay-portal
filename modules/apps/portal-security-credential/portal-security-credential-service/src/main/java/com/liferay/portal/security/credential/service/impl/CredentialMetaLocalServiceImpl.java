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

import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.PortalPreferencesLocalService;
import com.liferay.portal.kernel.util.PortletKeys;
import com.liferay.portal.security.credential.generator.registry.CredentialGeneratorRegistry;
import com.liferay.portal.security.credential.generator.spi.CredentialGenerator;
import com.liferay.portal.security.credential.model.AlgorithmEntry;
import com.liferay.portal.security.credential.model.CredentialEntry;
import com.liferay.portal.security.credential.model.CredentialMeta;
import com.liferay.portal.security.credential.service.AlgorithmEntryLocalService;
import com.liferay.portal.security.credential.service.base.CredentialMetaLocalServiceBaseImpl;

import java.util.List;

import javax.portlet.PortletPreferences;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * The implementation of the credential meta local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>com.liferay.portal.security.credential.service.CredentialMetaLocalService</code> interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author arthurchan35
 * @see CredentialMetaLocalServiceBaseImpl
 */
@Component(
	property = "model.class.name=com.liferay.portal.security.credential.model.CredentialMeta",
	service = AopService.class
)
public class CredentialMetaLocalServiceImpl
	extends CredentialMetaLocalServiceBaseImpl {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Use <code>com.liferay.portal.security.credential.service.CredentialMetaLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>com.liferay.portal.security.credential.service.CredentialMetaLocalServiceUtil</code>.
	 */
	@Override
	public CredentialMeta addMeta(CredentialEntry credentialEntry)
		throws PortalException {

		PortletPreferences portletPreferences =
			_portalPreferencesLocalService.getPreferences(
				credentialEntry.getCompanyId(),
				PortletKeys.PREFS_OWNER_TYPE_COMPANY);

		String idString = portletPreferences.getValue(
			"LatestAlgorithmEntryId", null);

		long latestAglorithmEntryId = -1;

		try {
			latestAglorithmEntryId = Long.parseLong(idString);
		}
		catch (NumberFormatException nfe) {
			throw new PortalException(nfe);
		}

		int order = getMetasCountByCredentialEntry(credentialEntry) + 1;

		long metaId = counterLocalService.increment();

		CredentialMeta meta = credentialMetaPersistence.create(metaId);

		meta.setCredentialEntryId(credentialEntry.getEntryId());
		meta.setAlgorithmEntryId(latestAglorithmEntryId);
		meta.setOrderInEntry(order);

		AlgorithmEntry algorithm =
			_algorithmEntryLocalService.getAlgorithmEntry(
				latestAglorithmEntryId);

		meta.setAlgorithmEntry(algorithm);

		CredentialGenerator credentialGenerator =
			_credentialGeneratorRegistry.getCredentialGenerator(algorithm);

		try {
			String salt = credentialGenerator.generateSalt(algorithm);

			meta.setSalt(salt);
		}
		catch (Exception e) {
			throw new PortalException(e.getMessage());
		}

		return credentialMetaPersistence.update(meta);
	}

	@Override
	public CredentialMeta deleteMeta(CredentialMeta credentialMeta)
		throws PortalException {

		long algorithmEntryId = credentialMeta.getAlgorithmEntryId();

		int countsWithSameAlgorithm =
			credentialMetaPersistence.countByAlgorithmEntryId(algorithmEntryId);

		if (countsWithSameAlgorithm == 1) {
			_algorithmEntryLocalService.deleteEntry(algorithmEntryId);
		}

		return credentialMetaPersistence.remove(credentialMeta);
	}

	@Override
	public void deleteMetasByCredentialEntry(CredentialEntry credentialEntry)
		throws PortalException {

		List<CredentialMeta> metas =
			credentialMetaPersistence.findByCredentialEntryId(
				credentialEntry.getEntryId());

		for (CredentialMeta meta : metas) {
			deleteMeta(meta);
		}
	}

	@Override
	public List<CredentialMeta> getMetasByCredentialEntry(
			CredentialEntry credentialEntry)
		throws PortalException {

		List<CredentialMeta> metas =
			credentialMetaPersistence.findByCredentialEntryId(
				credentialEntry.getEntryId());

		for (CredentialMeta meta : metas) {
			AlgorithmEntry algorithmEntry =
				_algorithmEntryLocalService.getAlgorithmEntry(
					meta.getAlgorithmEntryId());

			meta.setAlgorithmEntry(algorithmEntry);
		}

		return metas;
	}

	@Override
	public int getMetasCountByCredentialEntry(CredentialEntry credentialEntry)
		throws PortalException {

		return credentialMetaPersistence.countByCredentialEntryId(
			credentialEntry.getEntryId());
	}

	@Override
	public CredentialMeta resetMetasByCredentialEntry(
			CredentialEntry credentialEntry)
		throws PortalException {

		PortletPreferences portletPreferences =
			_portalPreferencesLocalService.getPreferences(
				credentialEntry.getCompanyId(),
				PortletKeys.PREFS_OWNER_TYPE_COMPANY);

		String idString = portletPreferences.getValue(
			"LatestAlgorithmEntryId", null);

		long latestAglorithmEntryId = -1;

		try {
			latestAglorithmEntryId = Long.parseLong(idString);
		}
		catch (NumberFormatException nfe) {
			throw new PortalException(nfe);
		}

		List<CredentialMeta> metas =
			credentialMetaPersistence.findByCredentialEntryId(
				credentialEntry.getEntryId());

		for (int i = 1; i < metas.size(); ++i) {
			deleteMeta(metas.get(i));
		}

		CredentialMeta meta = metas.get(0);

		meta.setAlgorithmEntryId(latestAglorithmEntryId);

		AlgorithmEntry algorithm =
			_algorithmEntryLocalService.getAlgorithmEntry(
				latestAglorithmEntryId);

		CredentialGenerator credentialGenerator =
			_credentialGeneratorRegistry.getCredentialGenerator(algorithm);

		try {
			String salt = credentialGenerator.generateSalt(algorithm);

			meta.setSalt(salt);
		}
		catch (Exception e) {
			throw new PortalException(e.getMessage());
		}

		return credentialMetaPersistence.update(meta);
	}

	@Reference
	private AlgorithmEntryLocalService _algorithmEntryLocalService;

	@Reference
	private CredentialGeneratorRegistry _credentialGeneratorRegistry;

	@Reference
	private PortalPreferencesLocalService _portalPreferencesLocalService;

}