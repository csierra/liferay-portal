/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.polls.model.v2;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * @author Carlos Sierra Andrés
 */
public interface PollsBuilder {

	public void appendChoice(String description);

	void setTitle(String title);

	void setTitle(String title, Locale locale);

	void setTitle(String title, Locale locale, Locale defaultLocale);

	void setTitleCurrentLanguageId(String languageId);

	void setTitleMap(Map<Locale, String> titleMap);

	void setTitleMap(Map<Locale, String> titleMap, Locale defaultLocale);

	void setDescription(String description);

	void setDescription(String description, Locale locale);

	void setDescription(String description, Locale locale, Locale defaultLocale);

	void setDescriptionCurrentLanguageId(String languageId);

	void setDescriptionMap(Map<Locale, String> descriptionMap);

	void setDescriptionMap(Map<Locale, String> descriptionMap, Locale defaultLocale);

	void setExpirationDate(Date expirationDate);
}
