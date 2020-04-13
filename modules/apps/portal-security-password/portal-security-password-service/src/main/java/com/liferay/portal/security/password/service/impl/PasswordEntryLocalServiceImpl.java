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
import com.liferay.portal.crypto.hash.context.builder.PepperContextBuilder;
import com.liferay.portal.crypto.hash.generation.context.HashGenerationContext;
import com.liferay.portal.crypto.hash.generation.context.salt.SaltCommand;
import com.liferay.portal.crypto.hash.generation.response.HashGenerationResponse;
import com.liferay.portal.crypto.hash.processor.HashProcessor;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.PasswordPolicy;
import com.liferay.portal.kernel.service.PasswordPolicyLocalService;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.security.password.exception.NoSuchEntryException;
import com.liferay.portal.security.password.model.PasswordEntry;
import com.liferay.portal.security.password.model.PasswordHashProvider;
import com.liferay.portal.security.password.service.PasswordMetaLocalService;
import com.liferay.portal.security.password.service.base.PasswordEntryLocalServiceBaseImpl;

import java.util.List;
import java.util.Optional;

import org.json.JSONObject;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * The implementation of the password entry local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>com.liferay.portal.security.password.service.PasswordEntryLocalService</code> interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS passwords because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Arthur Chan
 * @see PasswordEntryLocalServiceBaseImpl
 */
@Component(
	property = "model.class.name=com.liferay.portal.security.password.model.PasswordEntry",
	service = AopService.class
)
public class PasswordEntryLocalServiceImpl
	extends PasswordEntryLocalServiceBaseImpl {

	/**
	 * If current hash provider is no longer considered secured, allow to
	 * apply a new and secured hash provider to the persisted password hash
	 * so that the persisted password will remain secured.
	 *
	 * @param  UserId the userId
	 * @param  PasswordHashProvider the passwordHashProvider
	 * @return Same passowrdEntry with different hash
	 * @throws PortalException
	 */
	@Override
	public PasswordEntry addHashProviderToCurrentPasswordEntryByUserId(
			long userId, PasswordHashProvider passwordHashProvider)
		throws PortalException {

		PasswordEntry passwordEntry =
			passwordEntryLocalService.getCurrentPasswordEntryByUserId(userId);

		return passwordEntryLocalService.addHashProviderToPasswordEntry(
			passwordEntry, passwordHashProvider);
	}

	/**
	 * If current hash provider is no longer considered secured, allow to
	 * apply a new and secured hash provider to the persisted password hash
	 * so that the persisted password will remain secured.
	 *
	 * @param  PasswordEntryId the passwordEntry Id
	 * @param  PasswordHashProvider the passwordHashProvider
	 * @return Same passowrdEntry with different hash
	 * @throws PortalException
	 */
	@Override
	public PasswordEntry addHashProviderToPasswordEntry(
			long passwordEntryId, PasswordHashProvider passwordHashProvider)
		throws PortalException {

		PasswordEntry passwordEntry = passwordEntryPersistence.findByPrimaryKey(
			passwordEntryId);

		return passwordEntryLocalService.addHashProviderToPasswordEntry(
			passwordEntry, passwordHashProvider);
	}

	/**
	 * If current hash provider is no longer considered secured, allow to
	 * apply a new and secured hash provider to the persisted password hash
	 * so that the persisted password will remain secured.
	 *
	 * @param  PasswordEntry the passwordEntry
	 * @param  PasswordHashProvider the passwordHashProvider
	 * @return Same passowrdEntry with different hash
	 * @throws PortalException
	 */
	@Override
	public PasswordEntry addHashProviderToPasswordEntry(
			PasswordEntry passwordEntry,
			PasswordHashProvider passwordHashProvider)
		throws PortalException {

		HashGenerationResponse hashGenerationResponse;

		try {
			PepperContextBuilder pepperContextBuilder =
				_hashProcessor.createHashContextBuilder(
					passwordHashProvider.getHashProviderName(),
					new JSONObject(passwordHashProvider.getHashProviderMeta()));

			HashGenerationContext hashGenerationContext =
				pepperContextBuilder.buildGenerationContext(
					SaltCommand.generateDefaultSizeSalt());

			hashGenerationResponse = _hashProcessor.generate(
				Base64.decode(passwordEntry.getHash()), hashGenerationContext);
		}
		catch (Exception exception) {
			throw new PortalException(exception);
		}

		Optional<byte[]> optionalSalt = hashGenerationResponse.getSalt();

		_passwordMetaLocalService.addPasswordMeta(
			passwordEntry.getPasswordEntryId(),
			passwordHashProvider.getPasswordHashProviderId(),
			optionalSalt.orElse(new byte[0]));

		passwordEntry.setHash(Base64.encode(hashGenerationResponse.getHash()));

		return passwordEntryPersistence.update(passwordEntry);
	}

	/**
	 * Add a passwordEntry for user of userId
	 *
	 * @param  UserId ID of user
	 * @param  Password plain text password
	 * @param PasswordHashProvider the passwordHashProvider
	 * @return Generated PassowrdEntry
	 * @throws PortalException
	 */
	@Override
	public PasswordEntry addPasswordEntry(
			long userId, String password,
			PasswordHashProvider passwordHashProvider)
		throws PortalException {

		PasswordEntry passwordEntry = fetchPasswordEntryByUserId(userId);

		if (passwordEntry != null) {
			throw new PortalException(
				"user " + userId + " already has a password entry");
		}

		return _addPasswordEntry(userId, password, passwordHashProvider);
	}

	/**
	 * Remove all password entries for given user, including current password and history passwords.
	 *
	 * @param  UserId ID of user
	 * @throws PortalException
	 */
	@Override
	public void deleteEntriesByUserId(long userId) throws PortalException {
		List<PasswordEntry> passwordEntries =
			passwordEntryLocalService.fetchPasswordEntryByUserId(userId);

		for (PasswordEntry passwordEntry : passwordEntries) {
			passwordEntryLocalService.deletePasswordEntry(passwordEntry);
		}
	}

	@Override
	public PasswordEntry deletePasswordEntry(long entryId)
		throws PortalException {

		_passwordMetaLocalService.deletePasswordMetasByPasswordEntryId(entryId);

		return passwordEntryPersistence.remove(entryId);
	}

	@Override
	public PasswordEntry deletePasswordEntry(PasswordEntry entry)
		throws PortalException {

		return passwordEntryLocalService.deletePasswordEntry(
			entry.getPasswordEntryId());
	}

	/**
	 * Return all password entries for given user, including current password and history passwords. Ordered by modified date.
	 *
	 * @param  UserId ID of user
	 * @return A list of password entries
	 */
	@Override
	public PasswordEntry fetchPasswordEntryByUserId(long userId) {
		return passwordEntryPersistence.fetchByUserId(userId);
	}

	/**
	 * Return current password for given user.
	 *
	 * @param  UserId ID of user
	 * @return PasswordEntry
	 * @throws PortalException
	 */
	@Override
	public PasswordEntry getCurrentPasswordEntryByUserId(long userId)
		throws PortalException {

		List<PasswordEntry> entries =
			passwordEntryLocalService.fetchPasswordEntryByUserId(userId);

		if (entries.isEmpty()) {
			throw new NoSuchEntryException("for UserId: " + userId);
		}

		return entries.get(entries.size() - 1);
	}

	/**
	 * When updating passwordEntries for a user, two things can happen:
	 * 1. when passwordPolicy is set to have password history,
	 *    remove extra history passwords that are over history count limit,
	 *    and return a newly added password entry for the user.
	 * 2. otherwise,
	 *    remove all passwords and return a newly added password entry.
	 *
	 * @param  UserId ID of user
	 * @param  Password plain text password
	 * @return Updated PassowrdEntry
	 * @throws PortalException
	 */
	@Override
	public PasswordEntry updatePasswordEntriesByUserId(
			long userId, String password,
			PasswordHashProvider passwordHashProvider)
		throws PortalException {

		PasswordPolicy passwordPolicy =
			_passwordPolicyLocalService.getPasswordPolicyByUserId(userId);

		List<PasswordEntry> passwordEntries = fetchPasswordEntryByUserId(
			userId);

		if ((passwordPolicy == null) || !passwordPolicy.isHistory()) {
			for (PasswordEntry passwordEntry : passwordEntries) {
				deletePasswordEntry(passwordEntry);
			}
		}
		else {
			int historyCount = passwordPolicy.getHistoryCount();

			for (int i = passwordEntries.size() - 1; i > (historyCount - 1);
				 --i) {

				deletePasswordEntry(passwordEntries.get(i));
			}
		}

		return _addPasswordEntry(userId, password, passwordHashProvider);
	}

	private PasswordEntry _addPasswordEntry(
			long userId, String password,
			PasswordHashProvider passwordHashProvider)
		throws PortalException {

		long passwordEntryId = counterLocalService.increment();

		PasswordEntry passwordEntry = passwordEntryPersistence.create(
			passwordEntryId);

		HashGenerationResponse hashGenerationResponse;

		try {
			PepperContextBuilder pepperContextBuilder =
				_hashProcessor.createHashContextBuilder(
					passwordHashProvider.getHashProviderName(),
					new JSONObject(passwordHashProvider.getHashProviderMeta()));

			HashGenerationContext hashGenerationContext =
				pepperContextBuilder.buildGenerationContext(
					SaltCommand.generateDefaultSizeSalt());

			hashGenerationResponse = _hashProcessor.generate(
				password.getBytes(), hashGenerationContext);
		}
		catch (Exception exception) {
			throw new PortalException(exception);
		}

		Optional<byte[]> optionalSalt = hashGenerationResponse.getSalt();

		_passwordMetaLocalService.addPasswordMeta(
			passwordEntryId, passwordHashProvider.getPasswordHashProviderId(),
			optionalSalt.orElse(new byte[0]));

		passwordEntry.setUserId(userId);

		passwordEntry.setHash(Base64.encode(hashGenerationResponse.getHash()));

		return passwordEntryPersistence.update(passwordEntry);
	}

	@Reference
	private HashProcessor _hashProcessor;

	@Reference
	private PasswordMetaLocalService _passwordMetaLocalService;

	@Reference
	private PasswordPolicyLocalService _passwordPolicyLocalService;

}