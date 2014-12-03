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

package com.liferay.services.polls;

import com.liferay.model.PollsChoice;
import com.liferay.model.PollsQuestion;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Carlos Sierra Andrés
 */
public class PollsUpdater {

	private PollsQuestion _pollsQuestion;
	private List<PollsChoice> _addedChoices;
	private Map<String, String> _changedDescriptions;

	public PollsUpdater(
		PollsQuestion pollsQuestion, Map<String, String> changedNames,
		List<PollsChoice> addedChoices) {

		_pollsQuestion = pollsQuestion;
		_changedDescriptions = changedNames;
		_addedChoices = addedChoices;
	}

	public void setTitle(String title) {
		_pollsQuestion.setTitle(title);
	}


	public void setExpirationDate(Date expirationDate) {
		_pollsQuestion.setExpirationDate(expirationDate);
		_pollsQuestion.setExpiration(true);
	}

	public void unsetExpiration() {
		_pollsQuestion.setExpirationDate(null);
		_pollsQuestion.setExpiration(false);
	}

	public void appendChoice(String name, String description) {
		_addedChoices.add(new PollsChoice(name, description));
	}

	public void changeChoiceDescription(String name, String newDescription) {
		_changedDescriptions.put(name, newDescription);
	}
}
