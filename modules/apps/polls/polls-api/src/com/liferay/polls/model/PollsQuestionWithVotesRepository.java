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

package com.liferay.polls.model;

import com.liferay.kernel.LazyPageableResult;
import com.liferay.kernel.ReadOnlyRepository;
import com.liferay.polls.service.PollsVoteLocalService;
import com.liferay.polls.service.persistence.PollsQuestionPersistence;
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import java8.util.Function;
import java8.util.Optional;

import java.util.List;

/**
 * @author Carlos Sierra Andrés
 */
public class PollsQuestionWithVotesRepository
	implements ReadOnlyRepository<PollsQuestionWithVotes> {

	@Override
	public Optional<PollsQuestionWithVotes> retrieve(String id) {
		return pollsQuestionRepository.retrieve(id).map(
			new Function<PollsQuestion, PollsQuestionWithVotes>() {

			@Override
			public PollsQuestionWithVotes apply(final PollsQuestion question) {
				return new PollsQuestionWithVotes(
					question, new PollsVoteLazyResult(question));
			}
		});
	}

	@Override
	public Optional<String> toId(PollsQuestionWithVotes question) {
		return pollsQuestionRepository.toId(question.getWrappedQuestion());
	}

	public PollsQuestionWithVotes withVotes(PollsQuestion question) {
		return new PollsQuestionWithVotes(
			question, new PollsVoteLazyResult(question));
	}

	private class PollsVoteLazyResult
		implements LazyPageableResult<PollsVote> {

		private PollsVoteLazyResult(PollsQuestion question) {
			_question = question;
		}

		@Override
		public List<PollsVote> subList(int start, int end) {
			return pollsVoteLocalService.getQuestionVotes(
				_question.getQuestionId(), start, end);
		}

		@Override
		public List<PollsVote> all() {
			return pollsVoteLocalService.getQuestionVotes(
				_question.getQuestionId(), QueryUtil.ALL_POS,
				QueryUtil.ALL_POS);
		}

		private PollsQuestion _question;
	}

	@BeanReference(type = PollsQuestionRepository.class)
	PollsQuestionRepository pollsQuestionRepository;

	@BeanReference(type = PollsQuestionPersistence.class)
	PollsQuestionPersistence pollsQuestionPersistence;

	@BeanReference(type = PollsVoteLocalService.class)
	PollsVoteLocalService pollsVoteLocalService;
}
