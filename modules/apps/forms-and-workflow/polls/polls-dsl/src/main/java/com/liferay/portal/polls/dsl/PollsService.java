/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.polls.dsl;

import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.polls.dsl.PollsContext.MultiplePollContext;
import com.liferay.portal.polls.dsl.PollsContext.SinglePollContext;

import java.util.List;

/**
 * @author Carlos Sierra Andrés
 */
public interface PollsService {

	<T> T execute(
		long userId, ServiceContext serviceContext, SinglePollContext spc, QuestionQuerier<T> querier);
	<T> List<T> execute(long userId, ServiceContext serviceContext, MultiplePollContext mpc, QuestionQuerier<T> querier);

	<C extends PollsContext<C>> void execute(
		long userId, ServiceContext serviceContext, PollsContext<C> pc);



}
