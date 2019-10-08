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
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.password.hash.provider.configuration.PasswordHashProviderConfiguration;
import com.liferay.portal.security.password.model.PasswordHashProvider;
import com.liferay.portal.security.password.service.base.PasswordHashProviderLocalServiceBaseImpl;

import java.util.Date;
import java.util.Map;

import org.json.JSONObject;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

/**
 * The implementation of the hash algorithm entry local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>com.liferay.portal.security.password.service.PasswordHashProviderLocalService</code> interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Arthur Chan
 * @see PasswordHashProviderLocalServiceBaseImpl
 */
@Component(
	configurationPid = "com.liferay.portal.security.password.hash.provider.configuration.PasswordHashProviderConfiguration",
	property = "model.class.name=com.liferay.portal.security.password.model.PasswordHashProvider",
	service = AopService.class
)
public class PasswordHashProviderLocalServiceImpl
	extends PasswordHashProviderLocalServiceBaseImpl {

	/**
	 * Delete all password hash providers before a date
	 *
	 * @param date the given date
	 */
	@Override
	public void deletePasswordHashProvidersBeforeDate(Date date) {
		for (PasswordHashProvider passwordHashProvider :
				passwordHashProviderPersistence.findByLtCreateDate(date)) {

			passwordHashProviderPersistence.remove(passwordHashProvider);
		}
	}

	/**
	 * Get the latest password hash provider ordered by created date
	 *
	 * @return the latest password hash provider ordered by created date
	 */
	@Override
	public PasswordHashProvider getLastPasswordHashProvider()
		throws PortalException {

		// OrderByComparator can be null, because nature order is
		// set to ascending by create date in service.xml

		return passwordHashProviderPersistence.findByLtCreateDate_Last(
			new Date(), null);
	}

	/**
	 * Get the latest password hash provider before a date, ordered by created date
	 *
	 * @param date the given date
	 * @return the latest password hash provider before a date,  ordered by created date
	 */
	@Override
	public PasswordHashProvider getLastPasswordHashProviderBeforeDate(Date date)
		throws PortalException {

		// OrderByComparator can be null, because nature order is
		// set to ascending by create date in service.xml

		return passwordHashProviderPersistence.findByLtCreateDate_Last(
			date, null);
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		PasswordHashProviderConfiguration passwordHashProviderConfiguration =
			ConfigurableUtil.createConfigurable(
				PasswordHashProviderConfiguration.class, properties);

		try {
			_addPasswordHashProvider(
				passwordHashProviderConfiguration.hashProviderName(),
				passwordHashProviderConfiguration.hashProviderMetaJSON());
		}
		catch (Exception exception) {
			_log.error(exception, exception);
		}
	}

	/**
	 * Invoked whenever a change to Password Hash Provider configuration
	 *
	 * @param  name the password hash provider name
	 * @param  metaJSON the password hash provider meta in JSON format
	 */
	private void _addPasswordHashProvider(String name, String metaJSON)
		throws Exception {

		long passwordHashProviderId = counterLocalService.increment(
			PasswordHashProvider.class.getName());

		PasswordHashProvider passwordHashProvider =
			passwordHashProviderPersistence.create(passwordHashProviderId);

		passwordHashProvider.setHashProviderName(name);

		if (Validator.isNotNull(metaJSON)) {
			JSONObject meta = new JSONObject(metaJSON);

			passwordHashProvider.setHashProviderMeta(meta.toString());
		}

		passwordHashProviderPersistence.update(passwordHashProvider);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PasswordHashProviderLocalServiceImpl.class);

}