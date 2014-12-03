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

import com.liferay.polls.model.PollsVote;
import com.liferay.polls.service.persistence.PollsChoiceUtil;
import com.liferay.polls.service.persistence.PollsQuestionUtil;
import com.liferay.portal.kernel.util.Function;

import java.util.Date;

/**
 * @author Carlos Sierra Andrés
 */
public class PollsVoteQuerier {

	private PollsVote _vote;
	private PollsChoiceQuerier _choiceQuerier;
	private PollsQuerier _pollsQuerier;

	public String getUuid() {
		return _vote.getUuid();
	}

	public long getVoteId() {
		return _vote.getVoteId();
	}

	public long getUserId() {
		return _vote.getUserId();
	}

	public Date getCreateDate() {
		return _vote.getCreateDate();
	}

	public Date getModifiedDate() {
		return _vote.getModifiedDate();
	}

	public Date getVoteDate() {
		return _vote.getVoteDate();
	}

	public PollsVoteQuerier(PollsVote vote) {
		_vote = vote;
	}

	public <T> T getChoice(Function<PollsChoiceQuerier, T> function) {
		if (_choiceQuerier == null) {
			_choiceQuerier = new PollsChoiceQuerier(
				PollsChoiceUtil.fetchByPrimaryKey(_vote.getChoiceId()));
		}

		return function.apply(_choiceQuerier);
	}

	public <T> T getQuestion(Function<PollsQuerier, T> function) {
		if (_pollsQuerier == null) {
			_pollsQuerier = new PollsQuerier(
				PollsQuestionUtil.fetchByPrimaryKey(_vote.getQuestionId()));
		}

		return function.apply(_pollsQuerier);
	}

	protected void setChoiceQuerier(PollsChoiceQuerier choiceQuerier) {
		_choiceQuerier = choiceQuerier;
	}

	protected void setPollsQuerier(PollsQuerier pollsQuerier) {
		_pollsQuerier = pollsQuerier;
	}

}
