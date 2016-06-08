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

import java.util.List;

/**
 * @author Carlos Sierra Andrés
 */
public interface PollsContext<C extends PollsContext<C>>
	extends Context<C, PollsOperations>, Trashable<C> {

	C apply(PollsOperations pollsOperations);

	void execute();

	public interface SimplePollsContext extends PollsContext<SimplePollsContext> {
		<T> T execute(QuestionQuerier<T> questionQuerier);
	}

	public interface MultiplePollsContext extends PollsContext<SimplePollsContext> {
		<T> List<T> execute(QuestionQuerier<T> questionQuerier);
	}

}
