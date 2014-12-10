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

package com.liferay.polls.model.impl;

import com.liferay.polls.model.PollsQuestion;
import com.liferay.polls.model.v2.PollsQuestionQuerier;
import com.liferay.polls.model.v2.PollsVoteQuerier;
import com.liferay.polls.service.PollsVoteLocalServiceUtil;
import com.liferay.polls.service.persistence.PollsQuestionUtil;
import com.liferay.portal.kernel.util.Function;

import java.util.List;

/**
 * @author Brian Wing Shun Chan
 */
public class PollsChoiceImpl extends PollsChoiceBaseImpl {

	@Override
	public int getVotesCount() {
		return PollsVoteLocalServiceUtil.getChoiceVotesCount(getChoiceId());
	}

	@Override
	public <T> T getQuestion(Function<PollsQuestionQuerier, T> function) {
		PollsQuestion pollsQuestion = PollsQuestionUtil.fetchByPrimaryKey(
			getQuestionId());

		return function.apply(pollsQuestion);
	}

}