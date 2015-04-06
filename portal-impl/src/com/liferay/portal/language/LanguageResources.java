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

package com.liferay.portal.language;

import com.liferay.portal.kernel.language.LanguageResourceManager;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.tools.LangBuilder;
import com.liferay.registry.Filter;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceTracker;
import com.liferay.registry.ServiceTrackerCustomizer;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * @author Shuyang Zhou
 * @author Kamesh Sampath
 */
public class LanguageResources {

	public static String fixValue(String value) {
		if (value.endsWith(LangBuilder.AUTOMATIC_COPY)) {
			value = value.substring(
				0, value.length() - LangBuilder.AUTOMATIC_COPY.length());
		}

		if (value.endsWith(LangBuilder.AUTOMATIC_TRANSLATION)) {
			value = value.substring(
				0, value.length() - LangBuilder.AUTOMATIC_TRANSLATION.length());
		}

		return value;
	}

	public static void fixValues(
		Map<String, String> languageMap, Properties properties) {

		for (Map.Entry<Object, Object> entry : properties.entrySet()) {
			String key = (String)entry.getKey();
			String value = (String)entry.getValue();

			value = fixValue(value);

			languageMap.put(key, value);
		}
	}

	public static String getMessage(Locale locale, String key) {
		return _languageResourceManager.getMessage(locale, key);
	}

	public static ResourceBundle getResourceBundle(Locale locale) {
		return _languageResourceManager.getResourceBundle(locale);
	}

	/**
	 * @deprecated As of 7.0.0, with no direct replacement
	 */
	@Deprecated
	public static Locale getSuperLocale(Locale locale) {
		return _languageResourceManager.getSuperLocale(locale);
	}

	public void setLanguageResourceManager(
		LanguageResourceManager languageResourceManager) {

		_languageResourceManager = languageResourceManager;
	}

	private static LanguageResourceManager _languageResourceManager;
	private static final ServiceTracker<ResourceBundle, ResourceBundle>
		_serviceTracker;

	static {
		Registry registry = RegistryUtil.getRegistry();

		Filter languageResourceFilter = registry.getFilter(
			"(&(!(javax.portlet.name=*))(language.id=*)(objectClass=" +
				ResourceBundle.class.getName() + "))");

		_serviceTracker = registry.trackServices(
			languageResourceFilter,
			new LanguageResourceServiceTrackerCustomizer());

		_serviceTracker.open();
	}

	private static class LanguageResourceServiceTrackerCustomizer
		implements ServiceTrackerCustomizer<ResourceBundle, ResourceBundle> {

		@Override
		public ResourceBundle addingService(
			ServiceReference<ResourceBundle> serviceReference) {

			Registry registry = RegistryUtil.getRegistry();

			ResourceBundle resourceBundle = registry.getService(
				serviceReference);

			String languageId = GetterUtil.getString(
				serviceReference.getProperty("language.id"), StringPool.BLANK);
			Map<String, String> languageMap = new HashMap<>();
			Locale locale = null;

			if (Validator.isNotNull(languageId)) {
				locale = LocaleUtil.fromLanguageId(languageId, true);
			}
			else {
				locale = new Locale(StringPool.BLANK);
			}

			Enumeration<String> keys = resourceBundle.getKeys();

			while (keys.hasMoreElements()) {
				String key = keys.nextElement();

				String value = resourceBundle.getString(key);

				languageMap.put(key, value);
			}

			Map<String, String> oldLanguageMap =
				_languageResourceManager.putLanguageMap(locale, languageMap);

			_oldLanguageMaps.put(serviceReference, oldLanguageMap);

			return resourceBundle;
		}

		@Override
		public void modifiedService(
			ServiceReference<ResourceBundle> serviceReference,
			ResourceBundle resourceBundle) {
		}

		@Override
		public void removedService(
			ServiceReference<ResourceBundle> serviceReference,
			ResourceBundle resourceBundle) {

			Registry registry = RegistryUtil.getRegistry();

			registry.ungetService(serviceReference);

			String languageId = GetterUtil.getString(
				serviceReference.getProperty("language.id"), StringPool.BLANK);
			Locale locale = null;

			if (Validator.isNotNull(languageId)) {
				locale = LocaleUtil.fromLanguageId(languageId, true);
			}
			else {
				locale = new Locale(StringPool.BLANK);
			}

			Map<String, String> languageMap = _oldLanguageMaps.get(
				serviceReference);

			_languageResourceManager.putLanguageMap(locale, languageMap);
		}

		private final Map<ServiceReference<?>, Map<String, String>>
			_oldLanguageMaps = new HashMap<>();

	}

}