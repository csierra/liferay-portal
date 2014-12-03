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

package com.liferay.polls.model.v2;

import com.liferay.polls.model.PollsChoice;
import com.liferay.polls.model.PollsQuestion;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Carlos Sierra Andrés
 */
public class PollsBuilder {

	PollsQuestion _pollsQuestion;

	List<PollsChoice> _addedChoices;

	public PollsBuilder(
		PollsQuestion pollsQuestion, List<PollsChoice> addedChoices) {

		_pollsQuestion = pollsQuestion;

		_addedChoices = addedChoices;
	}

	public void setTitle(String title) {
		_pollsQuestion.setTitle(title);
	}

	public void setTitle(String title, Locale locale) {
		_pollsQuestion.setTitle(title, locale);
	}

	public void setTitle(String title, Locale locale, Locale defaultLocale) {
		_pollsQuestion.setTitle(title, locale, defaultLocale);
	}

	public void setTitleCurrentLanguageId(String languageId) {
		_pollsQuestion.setTitleCurrentLanguageId(languageId);
	}

	public void setTitleMap(Map<Locale, String> titleMap) {
		_pollsQuestion.setTitleMap(titleMap);
	}

	public void setTitleMap(Map<Locale, String> titleMap, Locale defaultLocale) {
		_pollsQuestion.setTitleMap(titleMap, defaultLocale);
	}

	public void setDescription(String description) {
		_pollsQuestion.setDescription(description);
	}

	public void setDescription(String description, Locale locale) {
		_pollsQuestion.setDescription(description, locale);
	}

	public void setDescription(String description, Locale locale, Locale defaultLocale) {
		_pollsQuestion.setDescription(description, locale, defaultLocale);
	}

	public void setDescriptionCurrentLanguageId(String languageId) {
		_pollsQuestion.setDescriptionCurrentLanguageId(languageId);
	}

	public void setDescriptionMap(Map<Locale, String> descriptionMap) {
		_pollsQuestion.setDescriptionMap(descriptionMap);
	}

	public void setDescriptionMap(Map<Locale, String> descriptionMap, Locale defaultLocale) {
		_pollsQuestion.setDescriptionMap(descriptionMap, defaultLocale);
	}

	public void setExpirationDate(Date expirationDate) {
		_pollsQuestion.setExpirationDate(expirationDate);
	}
}
