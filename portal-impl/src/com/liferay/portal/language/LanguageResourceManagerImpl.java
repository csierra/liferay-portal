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
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PropertiesUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.InputStream;

import java.net.URL;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Adolfo Pérez
 */
public class LanguageResourceManagerImpl implements LanguageResourceManager {

	@Override
	public String getMessage(Locale locale, String key) {
		if (locale == null) {
			return null;
		}

		Map<String, String> languageMap = _languageMaps.get(locale);

		if (languageMap == null) {
			languageMap = _loadLocale(locale);
		}

		String value = languageMap.get(key);

		if (value == null) {
			return getMessage(_getSuperLocale(locale), key);
		}
		else {
			return value;
		}
	}

	@Override
	public ResourceBundle getResourceBundle(Locale locale) {
		return new LanguageResourcesBundle(locale);
	}

	/**
	 * @deprecated As of 7.0.0, with no direct replacement
	 */
	@Deprecated
	@Override
	public Locale getSuperLocale(Locale locale) {
		return _getSuperLocale(locale);
	}

	private Locale _getSuperLocale(Locale locale) {
		Locale superLocale = _superLocales.get(locale);

		if (superLocale != null) {
			if (superLocale == _nullLocale) {
				return null;
			}

			return superLocale;
		}

		superLocale = _createSuperLocale(locale);

		if (superLocale == null) {
			_superLocales.put(locale, _nullLocale);
		}
		else {
			_superLocales.put(locale, superLocale);
		}

		return superLocale;
	}

	@Override
	public Map<String, String> putLanguageMap(
		Locale locale, Map<String, String> languageMap) {

		Map<String, String> oldLanguageMap = _languageMaps.get(locale);

		if (oldLanguageMap == null) {
			_loadLocale(locale);

			oldLanguageMap = _languageMaps.get(locale);
		}

		Map<String, String> newLanguageMap = new HashMap<>();

		if (oldLanguageMap != null) {
			newLanguageMap.putAll(oldLanguageMap);
		}

		newLanguageMap.putAll(languageMap);

		_languageMaps.put(locale, newLanguageMap);

		return oldLanguageMap;
	}

	public void setConfig(String config) {
		_configNames = StringUtil.split(
			config.replace(CharPool.PERIOD, CharPool.SLASH));
	}

	private Locale _createSuperLocale(Locale locale) {
		String variant = locale.getVariant();

		if (variant.length() > 0) {
			return new Locale(locale.getLanguage(), locale.getCountry());
		}

		String country = locale.getCountry();

		if (country.length() > 0) {
			Locale priorityLocale = LanguageUtil.getLocale(
				locale.getLanguage());

			if ((priorityLocale != null) && !locale.equals(priorityLocale)) {
				return new Locale(
					priorityLocale.getLanguage(), priorityLocale.getCountry());
			}

			return LocaleUtil.fromLanguageId(locale.getLanguage(), false, true);
		}

		String language = locale.getLanguage();

		if (language.length() > 0) {
			return _blankLocale;
		}

		return null;
	}

	private Map<String, String> _loadLocale(Locale locale) {
		Map<String, String> languageMap = null;

		if (_configNames.length > 0) {
			String localeName = locale.toString();

			languageMap = new HashMap<>();

			for (String name : _configNames) {
				StringBundler sb = new StringBundler(4);

				sb.append(name);

				if (localeName.length() > 0) {
					sb.append(StringPool.UNDERLINE);
					sb.append(localeName);
				}

				sb.append(".properties");

				Properties properties = _loadProperties(sb.toString());

				LanguageResources.fixValues(languageMap, properties);
			}
		}
		else {
			languageMap = Collections.emptyMap();
		}

		_languageMaps.put(locale, languageMap);

		return languageMap;
	}

	private Properties _loadProperties(String name) {
		Properties properties = new Properties();

		try {
			ClassLoader classLoader = LanguageResources.class.getClassLoader();

			Enumeration<URL> enu = classLoader.getResources(name);

			if (_log.isDebugEnabled() && !enu.hasMoreElements()) {
				_log.debug("No resources found for " + name);
			}

			while (enu.hasMoreElements()) {
				URL url = enu.nextElement();

				if (_log.isInfoEnabled()) {
					_log.info("Loading " + name + " from " + url);
				}

				try (InputStream inputStream = url.openStream()) {
					Properties inputStreamProperties = PropertiesUtil.load(
						inputStream, StringPool.UTF8);

					properties.putAll(inputStreamProperties);

					if (_log.isInfoEnabled()) {
						_log.info(
							"Loading " + url + " with " +
								inputStreamProperties.size() + " values");
					}
				}
			}
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e, e);
			}
		}

		return properties;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LanguageResourceManagerImpl.class);

	private static final Locale _blankLocale = new Locale(StringPool.BLANK);
	private static final Locale _nullLocale = new Locale(StringPool.BLANK);

	private String[] _configNames;
	private final Map<Locale, Map<String, String>> _languageMaps =
		new ConcurrentHashMap<>(64);
	private final Map<Locale, Locale> _superLocales = new ConcurrentHashMap<>();

	private class LanguageResourcesBundle extends ResourceBundle {

		@Override
		public Enumeration<String> getKeys() {
			Set<String> keySet = _languageMap.keySet();

			if (parent == null) {
				return Collections.enumeration(keySet);
			}

			return new ResourceBundleEnumeration(keySet, parent.getKeys());
		}

		@Override
		public Locale getLocale() {
			return _locale;
		}

		@Override
		protected Object handleGetObject(String key) {
			return _languageMap.get(key);
		}

		@Override
		protected Set<String> handleKeySet() {
			return _languageMap.keySet();
		}

		private LanguageResourcesBundle(Locale locale) {
			_locale = locale;

			Map<String, String> languageMap = _languageMaps.get(locale);

			if (languageMap == null) {
				languageMap = _loadLocale(locale);
			}

			_languageMap = languageMap;

			Locale superLocale = _getSuperLocale(locale);

			if (superLocale != null) {
				setParent(new LanguageResourcesBundle(superLocale));
			}
		}

		private final Map<String, String> _languageMap;
		private final Locale _locale;

	}

}