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

import com.liferay.petra.string.StringPool;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.password.model.PasswordHashProvider;
import com.liferay.portal.security.password.service.base.PasswordHashProviderLocalServiceBaseImpl;

import java.util.Map;

import javax.portlet.PortletPreferences;

import org.json.JSONObject;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * The implementation of the hash algorithm entry local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>com.liferay.portal.security.password.service.HashAlgorithmEntryLocalService</code> interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Arthur Chan
 * @see PasswordHashProviderLocalServiceBaseImpl
 */
@Component(
	property = "model.class.name=com.liferay.portal.security.password.model.PasswordHashProvider",
	service = AopService.class
)
public class PasswordHashProviderLocalServiceImpl
	extends PasswordHashProviderLocalServiceBaseImpl {

	@Override
	public PasswordHashProvider addPasswordHashProvider(String name, JSONObject meta)
		throws PortalException {

		PasswordHashProvider latestPasswordHashProvider = _getLastetPasswordHashProvider();

		// Compare the given info with latestPasswordHashProvider
		// If matches, then simply return
		// else add new one

		long passwordHashProviderId = counterLocalService.increment();

		PasswordHashProvider passwordHashProvider =
			passwordHashProviderPersistence.create(passwordHashProviderId);

		passwordHashProvider.setName(name);
		passwordHashProvider.setMeta(meta.toString());

		passwordHashProvider = passwordHashProviderPersistence.update(
			passwordHashProvider);

		return passwordHashProvider;
	}

	// get the latest entry order by modified date

	public PasswordHashProvider _getLastetPasswordHashProvider()
		throws PortalException {

		/*
		 * String name = _passwordHashProviderConfiguration.hashProviderName();
		 * String[] metasString = _passwordHashProviderConfiguration.hashProviderMeta();
		 * 
		 * JSONObject metas = new JSONObject();
		 * 
		 * for (String meta : metasString) { String[] keyValue =
		 * meta.split(StringPool.COLON);
		 * 
		 * metas.put(keyValue[0], keyValue[1]); }
		 * 
		 * return _addPasswordHashProvider(name, metas);
		 */

		return null;

	}

}