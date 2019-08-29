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
import com.liferay.portal.security.credential.generator.registry.CredentialGeneratorRegistry;
import com.liferay.portal.security.credential.generator.spi.CredentialGenerator;
import com.liferay.portal.security.credential.model.CredentialEntry;
import com.liferay.portal.security.credential.model.CredentialMeta;
import com.liferay.portal.security.credential.service.CredentialMetaLocalService;
import com.liferay.portal.security.credential.service.base.CredentialEntryLocalServiceBaseImpl;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * The implementation of the credential entry local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>com.liferay.portal.security.credential.service.CredentialEntryLocalService</code> interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author arthurchan35
 * @see CredentialEntryLocalServiceBaseImpl
 */
@Component(
	property = "model.class.name=com.liferay.portal.security.credential.model.CredentialEntry",
	service = AopService.class
)
public class CredentialEntryLocalServiceImpl
	extends CredentialEntryLocalServiceBaseImpl {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Use <code>com.liferay.portal.security.credential.service.CredentialEntryLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>com.liferay.portal.security.credential.service.CredentialEntryLocalServiceUtil</code>.
	 */
	@Override
	public CredentialEntry addEntry(
			long userId, long companyId, String password, boolean reset)
		throws PortalException {

		long entryId = counterLocalService.increment();

		CredentialEntry entry = credentialEntryPersistence.create(entryId);

		entry.setCompanyId(companyId);
		entry.setUserId(userId);
		entry.setReset(reset);

		CredentialMeta meta = _credentialMetaLocalService.addMeta(entry);

		CredentialGenerator credentialGenerator =
			_credentialGeneratorRegistry.getCredentialGenerator(meta);

		try {
			String secret = credentialGenerator.generateSecret(password, meta);

			entry.setSecret(secret);
		}
		catch (Exception e) {
			throw new PortalException(e.getMessage());
		}

		entry = credentialEntryPersistence.update(entry);

		return entry;
	}

	@Override
	public CredentialEntry deleteEntry(CredentialEntry entry)
		throws PortalException {

		_credentialMetaLocalService.deleteMetasByCredentialEntry(entry);

		return credentialEntryPersistence.remove(entry);
	}

	@Override
	public CredentialEntry getEntryByUserId(long userId)
		throws PortalException {

		return credentialEntryPersistence.findByUserId(userId);
	}

	@Override
	public CredentialEntry updateEntry(
			long userId, String secret, boolean reset)
		throws PortalException {

		CredentialEntry entry = credentialEntryPersistence.findByUserId(userId);

		entry.setSecret(secret);
		entry.setReset(reset);

		entry = credentialEntryPersistence.update(entry);

		_credentialMetaLocalService.resetMetasByCredentialEntry(entry);

		return entry;
	}

	@Reference
	private CredentialGeneratorRegistry _credentialGeneratorRegistry;

	@Reference
	private CredentialMetaLocalService _credentialMetaLocalService;

}