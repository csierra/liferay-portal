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

package com.liferay.polls.model.v2;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.util.Function;

import java.util.Date;

/**
 * @author Carlos Sierra Andrés
 */
public interface PollsVoteQuerier {
	/**
	 * Returns the uuid of this polls vote.
	 *
	 * @return the uuid of this polls vote
	 */
	@AutoEscape
	String getUuid();

	/**
	 * Returns the vote ID of this polls vote.
	 *
	 * @return the vote ID of this polls vote
	 */
	long getVoteId();

	/**
	 * Returns the user ID of this polls vote.
	 *
	 * @return the user ID of this polls vote
	 */
	long getUserId();

	/**
	 * Returns the create date of this polls vote.
	 *
	 * @return the create date of this polls vote
	 */
	Date getCreateDate();

	/**
	 * Returns the modified date of this polls vote.
	 *
	 * @return the modified date of this polls vote
	 */
	Date getModifiedDate();

	/**
	 * Returns the vote date of this polls vote.
	 *
	 * @return the vote date of this polls vote
	 */
	Date getVoteDate();

	public <T> T getChoice(Function<PollsChoiceQuerier, T> function);
}
