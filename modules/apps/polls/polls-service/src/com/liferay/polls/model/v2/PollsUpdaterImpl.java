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
import com.liferay.polls.model.impl.PollsChoiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Carlos Sierra Andrés
 */
public class PollsUpdaterImpl implements PollsUpdater {

	private PollsQuestion _pollsQuestion;
	private List<PollsChoice> _addedChoices;
	private Map<String, String> _changedDescriptions;

	public PollsUpdaterImpl(
		PollsQuestion pollsQuestion, Map<String, String> changedNames,
		List<PollsChoice> addedChoices) {

		_addedChoices = new ArrayList<>();
		_pollsQuestion = pollsQuestion;
		_changedDescriptions = changedNames;
	}

	@Override
	public void setTitle(String title) {
		_pollsQuestion.setTitle(title);
	}


	@Override
	public void setExpirationDate(Date expirationDate) {
		_pollsQuestion.setExpirationDate(expirationDate);
	}

	@Override
	public void unsetExpiration() {
		_pollsQuestion.setExpirationDate(null);
	}

	@Override
	public void appendChoice(String name, String description) {
		PollsChoiceImpl pollsChoice = new PollsChoiceImpl();

		pollsChoice.setDescription(description);
		pollsChoice.setName(name);

		_addedChoices.add(pollsChoice);
	}

	@Override
	public void changeChoiceDescription(String name, String newDescription) {
		_changedDescriptions.put(name, newDescription);
	}

}
