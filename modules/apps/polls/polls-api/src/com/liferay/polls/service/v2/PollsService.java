/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.polls.service.v2;

import com.liferay.polls.model.v2.PollsBuilder;
import com.liferay.polls.model.v2.PollsQuestionQuerier;
import com.liferay.polls.model.v2.PollsUpdater;
import com.liferay.services.v2.Command;
import com.liferay.services.v2.ModelAction;
import com.liferay.services.v2.Service;

/**
 * @author Carlos Sierra Andrés
 */
public interface PollsService
	extends Service<PollsBuilder, PollsQuestionQuerier, PollsContext> {
	Command<PollsContext, PollsQuestionQuerier>
		addGroupPermissions();

	Command<PollsContext, PollsQuestionQuerier>
			addGuestPermissions();

	Command<PollsContext, PollsQuestionQuerier> update(
		ModelAction<PollsUpdater> updater);
}
