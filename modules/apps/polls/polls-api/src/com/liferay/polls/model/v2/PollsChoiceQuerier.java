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
import com.liferay.polls.service.persistence.PollsQuestionUtil;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * @author Carlos Sierra Andrés
 */
public class PollsChoiceQuerier {

	PollsChoice _pollsChoice;
	PollsQuerier _pollsQuerier;

	public PollsChoiceQuerier(PollsChoice pollsChoice) {
		_pollsChoice = pollsChoice;
	}

	public int getVotesCount() {
		return _pollsChoice.getVotesCount();
	}

	public String getUuid() {
		return _pollsChoice.getUuid();
	}

	public long getChoiceId() {
		return _pollsChoice.getChoiceId();
	}

	public long getUserId() {
		return _pollsChoice.getUserId();
	}

	public Date getCreateDate() {
		return _pollsChoice.getCreateDate();
	}

	public Date getModifiedDate() {
		return _pollsChoice.getModifiedDate();
	}

	public String getName() {
		return _pollsChoice.getName();
	}

	public String getDescription() {
		return _pollsChoice.getDescription();
	}

	public String getDescription(Locale locale) {
		return _pollsChoice.getDescription(locale);
	}

	public String getDescription(Locale locale, boolean useDefault) {
		return _pollsChoice.getDescription(locale, useDefault);
	}

	public String getDescription(String languageId) {
		return _pollsChoice.getDescription(languageId);
	}

	public String getDescription(String languageId, boolean useDefault) {
		return _pollsChoice.getDescription(languageId, useDefault);
	}

	public String getDescriptionCurrentLanguageId() {
		return _pollsChoice.getDescriptionCurrentLanguageId();
	}

	public String getDescriptionCurrentValue() {
		return _pollsChoice.getDescriptionCurrentValue();
	}

	public Map<Locale, String> getDescriptionMap() {
		return _pollsChoice.getDescriptionMap();
	}

	public PollsQuerier getQuestion() {
		if (_pollsQuerier == null) {
				_pollsQuerier = new PollsQuerier(
					PollsQuestionUtil.fetchByPrimaryKey(
						_pollsChoice.getQuestionId()));
		}

		return _pollsQuerier;
	}

	protected void setPollsQuerier(PollsQuerier pollsQuerier) {
		_pollsQuerier = pollsQuerier;
	}

}
