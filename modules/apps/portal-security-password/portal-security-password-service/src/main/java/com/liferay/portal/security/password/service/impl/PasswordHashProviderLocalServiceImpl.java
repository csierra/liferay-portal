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

import com.liferay.petra.string.CharPool;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.security.password.model.PasswordHashProvider;
import com.liferay.portal.security.password.service.base.PasswordHashProviderLocalServiceBaseImpl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;

import org.osgi.service.component.annotations.Component;

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
	property = "model.class.name=com.liferay.portal.security.password.model.PasswordHashProvider",
	service = AopService.class
)
public class PasswordHashProviderLocalServiceImpl
	extends PasswordHashProviderLocalServiceBaseImpl {

	@Override
	public PasswordHashProvider addPasswordHashProvider(
			String name, JSONObject meta)
		throws PortalException {

		PasswordHashProvider existingPasswordHashProvider =
			fetchPasswordHashProvider(name, meta);

		if (existingPasswordHashProvider != null) {
			return existingPasswordHashProvider;
		}

		long passwordHashProviderId = counterLocalService.increment();

		PasswordHashProvider passwordHashProvider =
			passwordHashProviderPersistence.create(passwordHashProviderId);

		passwordHashProvider.setHashProviderName(name);

		passwordHashProvider.setHashProviderMeta(_getOrderedString(meta));

		passwordHashProvider = passwordHashProviderPersistence.update(
			passwordHashProvider);

		return passwordHashProvider;
	}

	public PasswordHashProvider addPasswordHashProvider(
			String name, String metaJSON)
		throws PortalException {

		JSONObject meta = new JSONObject(metaJSON);

		return passwordHashProviderLocalService.addPasswordHashProvider(
			name, meta);
	}

	@Override
	public PasswordHashProvider fetchPasswordHashProvider(
		String name, JSONObject meta) {

		return passwordHashProviderPersistence.fetchByN_M(
			name, _getOrderedString(meta));
	}

	private String _getOrderedString(JSONObject jsonObject)
		throws IllegalArgumentException {

		Set<String> keys = jsonObject.keySet();

		Stream<String> keyStream = keys.stream();

		List<String> orderedKeys = keyStream.sorted(
		).collect(
			Collectors.toList()
		);

		StringBuilder sb = new StringBuilder();

		sb.append(CharPool.OPEN_CURLY_BRACE);

		for (int i = 0; i < orderedKeys.size(); ++i) {
			sb.append(CharPool.QUOTE);
			String key = orderedKeys.get(i);
			sb.append(CharPool.QUOTE);
			sb.append(CharPool.COLON);

			Object object = jsonObject.get(key);

			if (object instanceof Double) {
				double value = (Double)object;

				sb.append(value);
			}
			else if (object instanceof Long) {
				long value = (Long)object;

				sb.append(value);
			}
			else if (object instanceof Boolean) {
				boolean value = (Boolean)object;

				sb.append(value);
			}
			else if (object instanceof String) {
				String value = (String)object;

				sb.append(CharPool.QUOTE);
				sb.append(value);
				sb.append(CharPool.QUOTE);
			}
			else if (object instanceof JSONArray) {
				JSONArray value = (JSONArray)object;

				sb.append(CharPool.QUOTE);
				sb.append(value);
				sb.append(CharPool.QUOTE);
			}
			else if (object instanceof JSONObject) {
				String value = _getOrderedString((JSONObject)object);

				sb.append(CharPool.QUOTE);
				sb.append(value);
				sb.append(CharPool.QUOTE);
			}
			else {
				throw new IllegalArgumentException(
					"Custom type of attribute of " + key +
						" is not supported.");
			}

			if (i < (orderedKeys.size() - 1)) {
				sb.append(CharPool.COMMA);
			}
		}

		sb.append(CharPool.CLOSE_CURLY_BRACE);

		return sb.toString();
	}

}