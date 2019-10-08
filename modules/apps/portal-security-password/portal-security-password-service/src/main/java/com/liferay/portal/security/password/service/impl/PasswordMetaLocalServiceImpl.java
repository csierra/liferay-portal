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

package com.liferay.portal.security.password.service.impl;

import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.security.password.model.PasswordEntry;
import com.liferay.portal.security.password.model.PasswordMeta;
import com.liferay.portal.security.password.service.PasswordHashProviderLocalService;
import com.liferay.portal.security.password.service.base.PasswordMetaLocalServiceBaseImpl;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * The implementation of the password meta local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>PasswordMetaLocalService</code> interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Arthur Chan
 * @see PasswordMetaLocalServiceBaseImpl
 */
@Component(
	property = "model.class.name=com.liferay.portal.security.password.model.PasswordMeta",
	service = AopService.class
)
public class PasswordMetaLocalServiceImpl
	extends PasswordMetaLocalServiceBaseImpl {

	/**
	 * Add a passwordMeta for the given passwordEntry during passwordEntry generation.
	 *
	 * @param  PasswordEntry the passwordEntry
	 * @return Generated meta for passwordEntry
	 * @throws PortalException
	 */
	@Override
	public PasswordMeta addPasswordMeta(
			long passwordEntryId, long passwordHashProviderId, byte[] salt)
		throws PortalException {

		long metaId = counterLocalService.increment();

		PasswordMeta meta = passwordMetaPersistence.create(metaId);

		meta.setPasswordEntryId(passwordEntryId);
		meta.setPasswordHashProviderId(passwordHashProviderId);
		meta.setSalt(Base64.encode(salt));

		return passwordMetaPersistence.update(meta);
	}

	@Override
	public PasswordMeta deletePasswordMeta(long passwordMetaId)
		throws PortalException {

		return passwordMetaLocalService.deletePasswordMeta(
			getPasswordMeta(passwordMetaId));
	}

	@Override
	public PasswordMeta deletePasswordMeta(PasswordMeta passwordMeta) {
		long passwordHashProviderId = passwordMeta.getPasswordHashProviderId();

		int sameProviders =
			passwordMetaPersistence.countByPasswordHashProviderId(
				passwordHashProviderId);

		if (sameProviders == 1) {
			try {
				_passwordHashProviderLocalService.deletePasswordHashProvider(
					passwordHashProviderId);
			}
			catch (PortalException portalException) {
				_log.error(
					"Unable to delete passwordHashProvider " +
						passwordHashProviderId,
					portalException);
			}
		}

		return passwordMetaPersistence.remove(passwordMeta);
	}

	@Override
	public void deletePasswordMetasByPasswordEntry(PasswordEntry passwordEntry)
		throws PortalException {

		passwordMetaLocalService.deletePasswordMetasByPasswordEntryId(
			passwordEntry.getPasswordEntryId());
	}

	@Override
	public void deletePasswordMetasByPasswordEntryId(long passwordEntryId)
		throws PortalException {

		List<PasswordMeta> metas =
			passwordMetaPersistence.findByPasswordEntryId(passwordEntryId);

		for (PasswordMeta meta : metas) {
			passwordMetaLocalService.deletePasswordMeta(meta);
		}
	}

	/**
	 * Return a list of metas of PasswordEntry, ordered by modified date
	 *
	 * @param  PasswordEntry the passwordEntry
	 * @return a list of metas of PasswordEntry
	 * @throws PortalException
	 */
	@Override
	public List<PasswordMeta> getPasswordMetasByPasswordEntry(
			PasswordEntry passwordEntry)
		throws PortalException {

		return passwordMetaLocalService.getPasswordMetasByPasswordEntryId(
			passwordEntry.getPasswordEntryId());
	}

	/**
	 * Return a list of metas of PasswordEntry, ordered by modified date
	 *
	 * @param  PasswordEntryId the ID of passwordEntry
	 * @return a list of metas of PasswordEntry
	 * @throws PortalException
	 */
	@Override
	public List<PasswordMeta> getPasswordMetasByPasswordEntryId(
			long passwordEntryId)
		throws PortalException {

		return passwordMetaPersistence.findByPasswordEntryId(passwordEntryId);
	}

	@Override
	public int getPasswordMetasCountByPasswordEntry(PasswordEntry passwordEntry)
		throws PortalException {

		return passwordMetaLocalService.getPasswordMetasCountByPasswordEntryId(
			passwordEntry.getPasswordEntryId());
	}

	@Override
	public int getPasswordMetasCountByPasswordEntryId(long passwordEntryId)
		throws PortalException {

		return passwordMetaPersistence.countByPasswordEntryId(passwordEntryId);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PasswordMetaLocalServiceImpl.class);

	@Reference
	private PasswordHashProviderLocalService _passwordHashProviderLocalService;

}