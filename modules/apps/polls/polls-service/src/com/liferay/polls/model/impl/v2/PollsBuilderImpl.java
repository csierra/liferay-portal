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

package com.liferay.polls.model.impl.v2;

import com.liferay.polls.model.PollsChoice;
import com.liferay.polls.model.PollsQuestion;
import com.liferay.polls.model.impl.PollsChoiceImpl;
import com.liferay.polls.model.v2.PollsBuilder;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Carlos Sierra Andrés
 */
public class PollsBuilderImpl implements PollsBuilder {

	PollsQuestion _pollsQuestion;

	List<PollsChoice> _addedChoices;

	public PollsBuilderImpl(
		PollsQuestion pollsQuestion, List<PollsChoice> addedChoices) {

		_pollsQuestion = pollsQuestion;
		_addedChoices = addedChoices;
	}

	@Override
	public void appendChoice(String description) {
		PollsChoice pollsChoice = new PollsChoiceImpl();

		pollsChoice.setDescription(description);

		_addedChoices.add(pollsChoice);
	}

	@Override
	public void setTitle(String title) {
		_pollsQuestion.setTitle(title);
	}

	@Override
	public void setTitle(String title, Locale locale) {
		_pollsQuestion.setTitle(title, locale);
	}

	@Override
	public void setTitle(String title, Locale locale, Locale defaultLocale) {
		_pollsQuestion.setTitle(title, locale, defaultLocale);
	}

	@Override
	public void setTitleCurrentLanguageId(String languageId) {
		_pollsQuestion.setTitleCurrentLanguageId(languageId);
	}

	@Override
	public void setTitleMap(Map<Locale, String> titleMap) {
		_pollsQuestion.setTitleMap(titleMap);
	}

	@Override
	public void setTitleMap(Map<Locale, String> titleMap, Locale defaultLocale) {
		_pollsQuestion.setTitleMap(titleMap, defaultLocale);
	}

	@Override
	public void setDescription(String description) {
		_pollsQuestion.setDescription(description);
	}

	@Override
	public void setDescription(String description, Locale locale) {
		_pollsQuestion.setDescription(description, locale);
	}

	@Override
	public void setDescription(String description, Locale locale, Locale defaultLocale) {
		_pollsQuestion.setDescription(description, locale, defaultLocale);
	}

	@Override
	public void setDescriptionCurrentLanguageId(String languageId) {
		_pollsQuestion.setDescriptionCurrentLanguageId(languageId);
	}

	@Override
	public void setDescriptionMap(Map<Locale, String> descriptionMap) {
		_pollsQuestion.setDescriptionMap(descriptionMap);
	}

	@Override
	public void setDescriptionMap(Map<Locale, String> descriptionMap, Locale defaultLocale) {
		_pollsQuestion.setDescriptionMap(descriptionMap, defaultLocale);
	}

	@Override
	public void setExpirationDate(Date expirationDate) {
		_pollsQuestion.setExpirationDate(expirationDate);
	}
}
