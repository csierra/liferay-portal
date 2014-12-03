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
import com.liferay.polls.model.PollsVote;
import com.liferay.polls.service.persistence.PollsChoicePersistence;
import com.liferay.polls.service.persistence.PollsChoiceUtil;
import com.liferay.polls.service.persistence.PollsVotePersistence;
import com.liferay.polls.service.persistence.PollsVoteUtil;
import com.liferay.portal.kernel.util.Function;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Carlos Sierra Andrés
 */
public class PollsQuerier {

	private PollsQuestion _question;
	private List<PollsChoice> _choices;
	private List<PollsVote> _votes;

	public PollsQuerier(PollsQuestion question) {
		_question = question;
	}

	public String getTitle() {
		return _question.getTitle();
	}

	public String getTitle(Locale locale) {
		return _question.getTitle(locale);
	}

	public String getTitle(Locale locale, boolean useDefault) {
		return _question.getTitle(locale, useDefault);
	}

	public String getTitle(String languageId) {
		return _question.getTitle(languageId);
	}

	public String getTitle(String languageId, boolean useDefault) {
		return _question.getTitle(languageId, useDefault);
	}

	public String getTitleCurrentLanguageId() {
		return _question.getTitleCurrentLanguageId();
	}

	public String getTitleCurrentValue() {
		return _question.getTitleCurrentValue();
	}

	public Map<Locale, String> getTitleMap() {
		return _question.getTitleMap();
	}

	public <T> List<T> getChoices(
		Function<PollsChoiceQuerier, T> function) {
		if (_choices == null) {
			_choices = PollsChoiceUtil.findByQuestionId(
				_question.getQuestionId());
		}

		ArrayList<T> mappedChoices = new ArrayList<>();

		for (PollsChoice choice : _choices) {
			PollsChoiceQuerier choiceQuerier = new PollsChoiceQuerier(choice);

			choiceQuerier.setPollsQuerier(this);
			mappedChoices.add(function.apply(choiceQuerier));
		}

		return mappedChoices;
	}

	public <T> List<T> getVotes(
		Function<PollsVoteQuerier, T> function) {
		if (_votes == null) {
			_votes = PollsVoteUtil.findByQuestionId(
				_question.getQuestionId());
		}

		ArrayList<T> mappedVotes = new ArrayList<>();

		for (PollsVote vote : _votes) {
			PollsVoteQuerier voteQuerier = new PollsVoteQuerier(vote);

			mappedVotes.add(function.apply(voteQuerier));
		}

		return mappedVotes;
	}

	public <T> List<T> getVotes(
		Function<PollsVoteQuerier, T> function, int start, int end) {

		List<PollsVote> votes = PollsVoteUtil.findByQuestionId(
			_question.getQuestionId(), start, end);

		ArrayList<T> mappedVotes = new ArrayList<>(end-start);

		for (PollsVote vote : votes) {
			PollsVoteQuerier voteQuerier = new PollsVoteQuerier(vote);

			mappedVotes.add(function.apply(voteQuerier));
		}

		return mappedVotes;
	}

	public int getVotesCount() {
		return _question.getVotesCount();
	}

	public boolean isExpired() {
		return _question.isExpired();
	}

	public String getUuid() {
		return _question.getUuid();
	}

	public long getPrimaryKey() {
		return _question.getPrimaryKey();
	}

	public long getGroupId() {
		return _question.getGroupId();
	}

	public long getCompanyId() {
		return _question.getCompanyId();
	}

	public long getUserId() {
		return _question.getUserId();
	}

	public Date getCreateDate() {
		return _question.getCreateDate();
	}

	public Date getModifiedDate() {
		return _question.getModifiedDate();
	}

	public String getDescription() {
		return _question.getDescription();
	}

	public String getDescription(Locale locale) {
		return _question.getDescription(locale);
	}

	public String getDescription(Locale locale, boolean useDefault) {
		return _question.getDescription(locale, useDefault);
	}

	public String getDescription(String languageId) {
		return _question.getDescription(languageId);
	}

	public String getDescription(String languageId, boolean useDefault) {
		return _question.getDescription(languageId, useDefault);
	}

	public String getDescriptionCurrentLanguageId() {
		return _question.getDescriptionCurrentLanguageId();
	}

	public String getDescriptionCurrentValue() {
		return _question.getDescriptionCurrentValue();
	}

	public Map<Locale, String> getDescriptionMap() {
		return _question.getDescriptionMap();
	}

	public Date getExpirationDate() {
		return _question.getExpirationDate();
	}

	public Date getLastVoteDate() {
		return _question.getLastVoteDate();
	}

	public String[] getAvailableLanguageIds() {
		return _question.getAvailableLanguageIds();
	}

	public String getDefaultLanguageId() {
		return _question.getDefaultLanguageId();
	}

	protected void setChoices(List<PollsChoice> choices) {
		_choices = choices;
	}
}
