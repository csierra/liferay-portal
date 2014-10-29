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

package com.liferay.polls.service.impl;

import com.liferay.polls.exception.QuestionExpirationDateException;
import com.liferay.polls.model.PollsChoice;
import com.liferay.polls.model.PollsQuestion;
import com.liferay.polls.model.PollsQuestionRepository;
import com.liferay.polls.service.base.PollsQuestionServiceBaseImpl;
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortalUtil;
import com.liferay.java8.util.Optional;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Brian Wing Shun Chan
 * @author Julio Camarero
 */
public class PollsQuestionServiceImpl extends PollsQuestionServiceBaseImpl {

	@Override
	public PollsQuestion addQuestion(
			Map<Locale, String> titleMap, Map<Locale, String> descriptionMap,
			int expirationDateMonth, int expirationDateDay,
			int expirationDateYear, int expirationDateHour,
			int expirationDateMinute, boolean neverExpire,
			List<Map<String, Object>> choices, ServiceContext serviceContext)
		throws PortalException {

		PollsQuestion pollsQuestion = new PollsQuestion();

		pollsQuestion.setTitleMap(titleMap);
		pollsQuestion.setDescriptionMap(descriptionMap);

		if (!neverExpire) {
			Date date = PortalUtil.getDate(
				expirationDateMonth, expirationDateDay, expirationDateYear,
				expirationDateHour, expirationDateMinute,
				QuestionExpirationDateException.class);

			pollsQuestion.setExpirationDate(date);
		}

		for (Map<String, Object> choiceMap : choices) {
			PollsChoice choice = new PollsChoice();

			choice.setModelAttributes(choiceMap);

			pollsQuestion.addChoice(choice);
		}

		_pollsQuestionRepository.store(pollsQuestion);

		return pollsQuestion;
	}

	@Override
	public void deleteQuestion(long questionId) throws PortalException {
		Optional<PollsQuestion> maybeQuestion =
			_pollsQuestionRepository.retrieve(Long.toString(questionId));

		if (maybeQuestion.isPresent()) {
			_pollsQuestionRepository.delete(maybeQuestion.get());
		}
	}

	@Override
	public PollsQuestion getQuestion(long questionId) throws PortalException {
		Optional<PollsQuestion> maybeQuestion =
			_pollsQuestionRepository.retrieve(Long.toString(questionId));

		if (!maybeQuestion.isPresent()) {
			throw new IllegalArgumentException();
		}

		return maybeQuestion.get();
	}

	@Override
	public PollsQuestion updateQuestion(
			long questionId, Map<Locale, String> titleMap,
			Map<Locale, String> descriptionMap, int expirationDateMonth,
			int expirationDateDay, int expirationDateYear,
			int expirationDateHour, int expirationDateMinute,
			boolean neverExpire, List<Map<String, Object>> choices,
			ServiceContext serviceContext)
		throws PortalException {

		Optional<PollsQuestion> maybeQuestion =
			_pollsQuestionRepository.retrieve(Long.toString(questionId));

		PollsQuestion pollsQuestion = maybeQuestion.get();

		pollsQuestion.setTitleMap(titleMap);
		pollsQuestion.setDescriptionMap(descriptionMap);

		Date date;

		if (neverExpire) {
			date = null;
		}
		else {
			date = PortalUtil.getDate(
				expirationDateMonth, expirationDateDay, expirationDateYear,
				expirationDateHour, expirationDateMinute,
				QuestionExpirationDateException.class);
		}

		pollsQuestion.setExpirationDate(date);

		List<PollsChoice> currentChoices = pollsQuestion.getChoices();

		List<String> choiceNames = ListUtil.toList(
			currentChoices, PollsChoice.CHOICE_NAME_ACCESSOR);

		for (Map<String, Object> choiceMap : choices) {
			int index = choiceNames.indexOf(choiceMap.get("name"));

			PollsChoice choice;

			if (index == -1) {
				choice = new PollsChoice();
				pollsQuestion.addChoice(choice);
			}
			else {
				choice = currentChoices.get(index);
			}

			choice.setModelAttributes(choiceMap);

			//[[*]] maybe the persist(question) actually saves choices for us now.
			_pollsQuestionRepository.store(choice);
		}

		_pollsQuestionRepository.store(pollsQuestion);

		return pollsQuestion;
	}
	
	@BeanReference(type = PollsQuestionRepository.class)
	private PollsQuestionRepository _pollsQuestionRepository;

}